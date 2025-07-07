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
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
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
const selectedYear = ref(new Date().getFullYear())
const selectedMonth = ref<number | null>(null)
const selectedQuarter = ref<number | null>(null)

// calcul du loyer selon la fréquence
const isTrimestriel = computed(() => {
  if (!selectedLocation.value) {
    return false
  }
  return (selectedLocation.value.frequenceLoyer || '').toString().trim().toLowerCase() === 'trimestriel'
})

const loyerDetail = computed(() => {
  if (!selectedLocation.value) {
    return ''
  }
  const freq = (selectedLocation.value.frequenceLoyer || '').toString().trim().toLowerCase()
  const loyerMensuel = Number.parseFloat(selectedLocation.value.loyerMensuel || '0')
  if (freq === 'trimestriel') {
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
    if (dateDebut && loc) {
      quittanceDTO.value.dateEcheance = computeEcheanceFromDebut(dateDebut, loc.jourEcheance)
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

function suivant() {
  if (!selectedYear.value || (frequence.value === 'MENSUEL' && selectedMonth.value === null) || (frequence.value === 'TRIMESTRIEL' && selectedQuarter.value === null)) {
    message.warning('Veuillez sélectionner la période complète')
    return
  }
  // Calcul de la période selon la fréquence
  let startDate: Date | null = null
  let endDate: Date | null = null

  if (selectedYear.value) {
    if (frequence.value === 'MENSUEL' && selectedMonth.value !== null) {
      startDate = new Date(selectedYear.value, selectedMonth.value, 1)
      endDate = new Date(selectedYear.value, selectedMonth.value + 1, 0)
    } else if (frequence.value === 'TRIMESTRIEL' && selectedQuarter.value !== null) {
      const startMonth = (selectedQuarter.value - 1) * 3
      startDate = new Date(selectedYear.value, startMonth, 1)
      endDate = new Date(selectedYear.value, startMonth + 3, 0)
    } else if (frequence.value === 'ANNUEL') {
      startDate = new Date(selectedYear.value, 0, 1)
      endDate = new Date(selectedYear.value, 11, 31)
    }
  }

  quittanceDTO.value.dateDebut = startDate
    ? `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(2, '0')}-${String(startDate.getDate()).padStart(2, '0')}`
    : null
  quittanceDTO.value.dateFin = endDate
    ? `${endDate.getFullYear()}-${String(endDate.getMonth() + 1).padStart(2, '0')}-${String(endDate.getDate()).padStart(2, '0')}`
    : null

  router.push('/quittance-etape-3')
}

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
          <NFormItemGi v-if="frequence" label="Année">
            <NSelect v-model:value="selectedYear" :options="yearOptions" size="large" />
          </NFormItemGi>
          <NFormItemGi v-if="frequence === 'MENSUEL'" label="Mois">
            <NSelect
              v-model:value="selectedMonth"
              :options="monthOptions"
              clearable
              placeholder="Choisir un mois"
              size="large"
            />
          </NFormItemGi>
          <NFormItemGi v-if="frequence === 'TRIMESTRIEL'" label="Trimestre">
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
      <div class="flex justify-between mt-8">
        <NButton @click="precedent" size="large">Précédent</NButton>
        <NButton type="primary" @click="suivant" size="large">Suivant</NButton>
      </div>
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
</style>
