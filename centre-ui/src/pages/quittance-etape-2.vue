<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowLeft24Filled, ArrowRight24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDatePicker,
  NForm,
  NFormItem,
  NFormItemGi,
  NGrid,
  NH2,
  NInputNumber,
  NRadio,
  NRadioGroup,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage,
  NIcon,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, onMounted, onUnmounted, ref, watch, nextTick } from 'vue'
import { format } from 'date-fns'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Ajouter une quittance - Étape 2',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})

const store = useUnifiedStore()
const { quittanceDTO, locations } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

// trouver la location choisie à l'étape 1
const selectedLocation = computed(() => locations.value.find(l => l.id === quittanceDTO.value.locationId))
const frequence = computed(() => selectedLocation.value?.frequenceLoyer)
const selectedYear = computed({
  get: () => quittanceDTO.value.selectedYear ?? new Date().getFullYear(),
  set: (val: number) => { quittanceDTO.value.selectedYear = val }
})
const selectedMonth = computed({
  get: () => quittanceDTO.value.selectedMonth ?? null,
  set: (val: number | null) => { quittanceDTO.value.selectedMonth = val }
})
const selectedQuarter = computed({
  get: () => quittanceDTO.value.selectedQuarter ?? null,
  set: (val: number | null) => { quittanceDTO.value.selectedQuarter = val }
})

// calcul du loyer selon la fréquence
const isTrimestriel = computed(() => {
  if (!selectedLocation.value) {
    return false
  }
  return (selectedLocation.value.frequenceLoyer || '').toUpperCase() === 'TRIMESTRIEL'
})

const loyerDetail = computed(() => {
  if (!selectedLocation.value) {
    return ''
  }
  const freq = (selectedLocation.value.frequenceLoyer || '').toUpperCase()
  const loyerMensuel = Number.parseFloat(selectedLocation.value.loyerMensuel || '0')
  if (freq === 'TRIMESTRIEL') {
    return `= 3 × ${loyerMensuel.toFixed(2)} € = ${(3 * loyerMensuel).toFixed(2)} €`
  }
  return ''
})

// Fonction utilitaire pour formater la date du jour au format yyyy-MM-dd
function getToday() {
  return format(new Date(), 'yyyy-MM-dd')
}

// Calcul automatique de la date d'échéance côté frontend
function computeEcheanceFromDebut(dateDebut: string, jourEcheance: number | string) {
  if (!dateDebut) return ''
  const ref = new Date(dateDebut)
  const annee = ref.getFullYear()
  const mois = ref.getMonth() + 1 // JS: 0-based
  const jour = Math.min(Number(jourEcheance || 1), new Date(annee, mois, 0).getDate())
  return `${annee}-${mois.toString().padStart(2, '0')}-${jour.toString().padStart(2, '0')}`
}

watch(
  [() => quittanceDTO.value.dateDebut, selectedLocation],
  ([dateDebut, loc]) => {
    if (loc) {
      quittanceDTO.value.dateEcheance = computeEcheanceFromDebut(dateDebut || '', loc.jourEcheance)
    }
  },
  { immediate: true }
)

// pré-remplir les champs quand la location est trouvée
watch(
  selectedLocation,
  (newLocation) => {
    if (newLocation) {
      // La caution NE DOIT JAMAIS être multipliée, même pour un loyer trimestriel
      const freq = (newLocation.frequenceLoyer || '').toString().trim().toLowerCase()
      const loyerMensuel = Number.parseFloat(newLocation.loyerMensuel || '0')
      const chargesMensuelles = Number.parseFloat(newLocation.chargesMensuelles || '0')
      quittanceDTO.value.depotGarantie = newLocation.depotGarantie
      if (freq === 'trimestriel') {
        quittanceDTO.value.montantLoyer = String(loyerMensuel * 3)
        quittanceDTO.value.montantCharges = String(chargesMensuelles * 3)
      } else {
        quittanceDTO.value.montantLoyer = String(loyerMensuel)
        quittanceDTO.value.montantCharges = String(chargesMensuelles)
      }
      quittanceDTO.value.dateEmission = getToday()
      quittanceDTO.value.dateEcheance = computeEcheanceFromDebut(
        quittanceDTO.value.dateDebut,
        newLocation.jourEcheance || '1'
      )
    }
  },
  { immediate: true },
)

// calcul du total en temps réel
const computedTotal = computed(() => {
  const loyer = Number.parseFloat(quittanceDTO.value.montantLoyer || '0')
  const charges = Number.parseFloat(quittanceDTO.value.montantCharges || '0')
  const caution = quittanceDTO.value.inclureCaution
    ? Number.parseFloat(quittanceDTO.value.depotGarantie || '0')
    : 0
  return (loyer + charges + caution)
})

function precedent() {
  router.push('/quittance-etape-1')
}

const errors = ref({
  year: false,
  month: false,
  quarter: false
})

async function suivant() {
  errors.value = { year: false, month: false, quarter: false }
  let hasError = false
  if (!selectedYear.value) {
    errors.value.year = true
    hasError = true
  }
  if (frequence.value && frequence.value.toString().toUpperCase() === 'MENSUEL' && selectedMonth.value === null) {
    errors.value.month = true
    hasError = true
  }
  if (frequence.value && frequence.value.toString().toUpperCase() === 'TRIMESTRIEL' && selectedQuarter.value === null) {
    errors.value.quarter = true
    hasError = true
  }
  if (hasError) {
    message.warning('Veuillez sélectionner la période complète')
    return
  }
  // Stocker explicitement les sélections dans le DTO
  quittanceDTO.value.selectedYear = selectedYear.value
  quittanceDTO.value.selectedMonth = selectedMonth.value
  quittanceDTO.value.selectedQuarter = selectedQuarter.value
  // Harmonisation de la casse de la fréquence
  const freq = frequence.value ? frequence.value.toString().toUpperCase() : ''
  let startDate = null
  let endDate = null
  if (selectedYear.value) {
    if (freq === 'MENSUEL' && selectedMonth.value !== null) {
      startDate = new Date(selectedYear.value, selectedMonth.value, 1)
      endDate = new Date(selectedYear.value, selectedMonth.value + 1, 0)
    } else if (freq === 'TRIMESTRIEL' && selectedQuarter.value !== null) {
      const startMonth = (selectedQuarter.value - 1) * 3
      startDate = new Date(selectedYear.value, startMonth, 1)
      endDate = new Date(selectedYear.value, startMonth + 3, 0)
    } else if (freq === 'ANNUEL') {
      startDate = new Date(selectedYear.value, 0, 1)
      endDate = new Date(selectedYear.value, 11, 31)
    }
  }
  const formatDate = (date) => {
    const y = date.getFullYear()
    const m = String(date.getMonth() + 1).padStart(2, '0')
    const d = String(date.getDate()).padStart(2, '0')
    return `${y}-${m}-${d}`
  }
  quittanceDTO.value.dateDebut = startDate ? formatDate(startDate) : null
  quittanceDTO.value.dateFin = endDate ? formatDate(endDate) : null
  // Log debug JSON.stringify
  console.log('DTO transmis à l\'étape 3', JSON.parse(JSON.stringify(quittanceDTO.value)))
  await nextTick()
  router.push('/quittance-etape-3')
}

watch([
  selectedYear, selectedMonth, selectedQuarter, frequence],
  ([year, month, quarter, freq]) => {
    console.log('WATCH:', { year, month, quarter, freq });
    let startDate: Date | null = null
    let endDate: Date | null = null
    const formatDate = (date: Date) => {
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    }
    if (year) {
      const freqUpper = (freq || '').toUpperCase();
      switch (freqUpper) {
        case 'MENSUEL':
          if (month !== null && month !== undefined) {
            startDate = new Date(year, month, 1)
            endDate = new Date(year, month + 1, 0)
          }
          break
        case 'TRIMESTRIEL':
          if (quarter !== null && quarter !== undefined) {
            const startMonth = (quarter - 1) * 3
            startDate = new Date(year, startMonth, 1)
            endDate = new Date(year, startMonth + 3, 0)
          }
          break
        case 'ANNUEL':
          startDate = new Date(year, 0, 1)
          endDate = new Date(year, 11, 31)
          break
      }
    }
    quittanceDTO.value.dateDebut = startDate ? formatDate(startDate) : null
    quittanceDTO.value.dateFin = endDate ? formatDate(endDate) : null
  },
  { immediate: true }
)

const stepTitles = ['Location', 'Période', 'Détails', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

const currentYear = new Date().getFullYear()
const yearOptions = Array.from({ length: 10 }, (_, i) => ({
  label: (currentYear - 5 + i).toString(),
  value: currentYear - 5 + i,
})).reverse()

const monthOptions = Array.from({ length: 12 }, (_, i) => ({
  label: new Date(2000, i).toLocaleString('fr-FR', { month: 'long' }).replace(/^\w/, c => c.toUpperCase()),
  value: i,
}))

const quarterOptions = [
  { label: 'Trimestre 1 (Jan-Mar)', value: 1 },
  { label: 'Trimestre 2 (Avr-Juin)', value: 2 },
  { label: 'Trimestre 3 (Juil-Sep)', value: 3 },
  { label: 'Trimestre 4 (Oct-Déc)', value: 4 },
]
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="1" size="small">
          <NStep title="Location" status="finish" description="Choix de la location" />
          <NStep title="Période" status="process" description="Année et mois/trimestre" />
          <NStep title="Détails" description="Montants et statut" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 2/4</div>
        <div class="step-mobile-label">Sélection de la période</div>
      </div>
      <NH2 class="titre-principal mb-4">Étape 2 : Sélection de la période</NH2>
      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
          <NFormItemGi v-if="frequence" label="Année" :validation-status="errors.year ? 'error' : undefined" :feedback="errors.year ? 'Ce champ est obligatoire' : ''">
            <NSelect v-model:value="selectedYear" :options="yearOptions" size="large" />
          </NFormItemGi>
          <NFormItemGi v-if="frequence && frequence.toString().toUpperCase() === 'MENSUEL'" label="Mois" :validation-status="errors.month ? 'error' : undefined" :feedback="errors.month ? 'Ce champ est obligatoire' : ''">
            <NSelect
              v-model:value="selectedMonth"
              :options="monthOptions"
              clearable
              placeholder="Choisir un mois"
              size="large"
            />
          </NFormItemGi>
          <NFormItemGi v-if="frequence && frequence.toString().toUpperCase() === 'TRIMESTRIEL'" label="Trimestre" :validation-status="errors.quarter ? 'error' : undefined" :feedback="errors.quarter ? 'Ce champ est obligatoire' : ''">
            <NSelect
              v-model:value="selectedQuarter"
              :options="quarterOptions"
              clearable
              placeholder="Choisir un trimestre"
              size="large"
            />
          </NFormItemGi>
        </NGrid>
      </NForm>
      <div v-if="quittanceDTO && quittanceDTO.value && quittanceDTO.value.dateDebut && quittanceDTO.value.dateFin" class="periode-resume mt-4 mb-2">
        <strong>Période sélectionnée :</strong>
        du {{ quittanceDTO.value.dateDebut }} au {{ quittanceDTO.value.dateFin }}
      </div>
      <pre>{{ quittanceDTO }}</pre>
      <NSpace justify="end" class="mt-8">
        <NButton type="primary" @click="suivant" size="large" title="Suivant">
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
          </template>
        </NButton>
      </NSpace>
    </NCard>
  </div>
</template>

<style scoped>
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.flex {
  display: flex;
}
.justify-between {
  justify-content: space-between;
}
.mt-8 {
  margin-top: 2rem;
}
@media (max-width: 768px) {
  .mb-8 {
    margin-bottom: 1rem !important;
  }
  .n-steps {
    font-size: 12px !important;
    min-width: 400px;
  }
  .n-steps,
  .n-steps .n-steps-main {
    overflow-x: auto !important;
    white-space: nowrap !important;
    display: block !important;
  }
  .n-step {
    min-width: 120px !important;
  }
  .n-step__description {
    display: none !important;
  }
  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-8,
  .mt-8 {
    margin-bottom: 1rem !important;
    margin-top: 1rem !important;
  }
}
.mobile-stepper {
  text-align: center;
  margin-bottom: 1.5rem;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1976d2;
}
.step-mobile-label {
  font-size: 1.2rem;
  color: #222;
  margin-bottom: 1rem;
}
.periode-resume {
  font-size: 1.1rem;
  color: #1976d2;
  background: #f5faff;
  border-radius: 6px;
  padding: 0.5rem 1rem;
  display: inline-block;
}
</style>
