<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowLeft24Filled, Checkmark24Filled } from '@vicons/fluent'
import {
  NButton,
  NDescriptions,
  NDescriptionsItem,
  NSpace,
  useMessage,
  NCard,
  NSteps,
  NStep,
  NH2,
  NIcon,
  NForm,
  NFormItemGi,
  NDatePicker,
  NInputNumber,
  NRadioGroup,
  NRadio,
  NSelect
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref, onMounted, onUnmounted, computed, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle quittance – Récapitulatif',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})

const store = useUnifiedStore()
const { quittanceDTO, locations } = storeToRefs(store)
const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()
const loading = ref(false)

const pdfSrc = ref<string | undefined>()
const stepTitles = ['Location', 'Période', 'Détails', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

async function genererPdfPreview() {
  try {
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/generateQuittancePdf/${quittanceDTO.value.id}`,
      { method: 'GET' },
    )

    if (!res.ok) throw new Error('Erreur lors de la génération du PDF')

    const blob = await res.blob()
    pdfSrc.value = URL.createObjectURL(blob)
  }
  catch (err) {
    message.error('Impossible de charger la prévisualisation PDF')
    console.error(err)
  }
}

function precedent() {
  router.push('/quittance-etape-2')
}

const errors = ref({
  dateEmission: false,
  montantLoyer: false,
  montantCharges: false,
  statut: false
})

// Calcul automatique de la période si absente à l'arrivée sur l'étape 3
function calculerPeriodeSiAbsente() {
  if (!quittanceDTO.value.dateDebut || !quittanceDTO.value.dateFin) {
    const freq = selectedLocation.value?.frequenceLoyer ? selectedLocation.value.frequenceLoyer.toString().toUpperCase() : ''
    const year = quittanceDTO.value.selectedYear || new Date().getFullYear()
    const month = quittanceDTO.value.selectedMonth
    const quarter = quittanceDTO.value.selectedQuarter
    let startDate = null
    let endDate = null
    if (year) {
      if (freq === 'MENSUEL' && month !== null && month !== undefined) {
        startDate = new Date(year, month, 1)
        endDate = new Date(year, month + 1, 0)
      } else if (freq === 'TRIMESTRIEL' && quarter !== null && quarter !== undefined) {
        const startMonth = (quarter - 1) * 3
        startDate = new Date(year, startMonth, 1)
        endDate = new Date(year, startMonth + 3, 0)
      } else if (freq === 'ANNUEL') {
        startDate = new Date(year, 0, 1)
        endDate = new Date(year, 11, 31)
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
  }
}

onMounted(() => {
  console.log('DTO reçu à l\'étape 3', JSON.parse(JSON.stringify(quittanceDTO.value)))
  calculerPeriodeSiAbsente()
})

function suivant() {
  // Réinitialise les erreurs
  errors.value = {
    dateEmission: false,
    montantLoyer: false,
    montantCharges: false,
    statut: false
  }
  let hasError = false
  if (!quittanceDTO.value.dateEmission) {
    errors.value.dateEmission = true
    hasError = true
  }
  if (!quittanceDTO.value.montantLoyer || Number(quittanceDTO.value.montantLoyer) <= 0) {
    errors.value.montantLoyer = true
    hasError = true
  }
  if (!quittanceDTO.value.montantCharges || Number(quittanceDTO.value.montantCharges) < 0) {
    errors.value.montantCharges = true
    hasError = true
  }
  if (!quittanceDTO.value.statut) {
    errors.value.statut = true
    hasError = true
  }
  // Log debug JSON.stringify
  console.log('DTO étape 3', JSON.parse(JSON.stringify(quittanceDTO.value)))
  // Calcul automatique de la période si besoin
  calculerPeriodeSiAbsente()
  // On vérifie aussi dateDebut/dateFin (étape précédente)
  if (!quittanceDTO.value.dateDebut?.trim() || !quittanceDTO.value.dateFin?.trim()) {
    message.warning('Merci de renseigner la période (étape précédente)')
    return
  }
  if (hasError) {
    message.warning('Merci de renseigner tous les champs obligatoires (*)')
    return
  }
  quittanceDTO.value.montantTotal = computedTotal.value.toFixed(2)
  router.push('/quittance-etape-4')
}

async function enregistrer() {
  loading.value = true
  try {
    const payload = {
      ...quittanceDTO.value,
      montantTotal: Number(quittanceDTO.value.montantTotal) || 0,
      locationId: quittanceDTO.value.locationId,
      utilisateurId: authStore.userInfo.userId,
      inclure_caution: quittanceDTO.value.inclureCaution ?? false
    }
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createQuittance`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }
    )
    if (!res.ok) throw new Error('Erreur serveur')
    if (quittanceDTO.value.statut === 'IMPAYEE') {
      message.success('Quittance créée !')
    } else {
      message.success('Quittance créée ! Une écriture comptable a été générée automatiquement.')
    }
    store.resetQuittanceDTO()
    router.push('/quittance')
  } catch (e: any) {
    message.error(e.message || 'Erreur inconnue')
  } finally {
    loading.value = false
  }
}

const computedTotal = computed(() => {
  const loyer = Number.parseFloat(quittanceDTO.value.montantLoyer || '0')
  const charges = Number.parseFloat(quittanceDTO.value.montantCharges || '0')
  const caution = quittanceDTO.value.inclureCaution
    ? Number.parseFloat(quittanceDTO.value.depotGarantie || '0')
    : 0
  return (loyer + charges + caution)
})

const selectedLocation = computed(() => {
  return locations.value.find(loc => loc.id === quittanceDTO.value.locationId)
})

// Calcul automatique de la date d'échéance à partir de la date de début et du jour d'échéance de la location
watch(
  [() => quittanceDTO.value.dateDebut, selectedLocation],
  ([dateDebut, loc]) => {
    console.log('dateDebut', dateDebut, 'loc', loc)
    if (dateDebut && loc && loc.jourEcheance) {
      const refDate = new Date(dateDebut)
      const annee = refDate.getFullYear()
      const mois = refDate.getMonth() + 1 // JS: 0-based
      const jour = Math.min(Number(loc.jourEcheance), new Date(annee, mois, 0).getDate())
      const result = `${annee}-${mois.toString().padStart(2, '0')}-${jour.toString().padStart(2, '0')}`
      console.log('dateEcheance calculée', result)
      quittanceDTO.value.dateEcheance = result
    }
  },
  { immediate: true }
)

</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <div class="mb-8" v-if="!isMobile">
          <NSteps :current="2" size="small">
            <NStep title="Location" status="finish" description="Choix de la location" />
            <NStep title="Période" status="finish" description="Année et mois/trimestre" />
            <NStep title="Détails" status="process" description="Montants et statut" />
            <NStep title="Récapitulatif" description="Vérification finale" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 3/4</div>
          <div class="step-mobile-label">{{ stepTitles[2] }}</div>
        </div>
        
        <div class="content-area">
          <NH2 class="titre-principal mb-4">Étape 3 : Détails de la quittance</NH2>
          <NForm label-placement="top">
            <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
              <NFormItemGi label="Date émission *" :validation-status="errors.dateEmission ? 'error' : undefined" :feedback="errors.dateEmission ? 'Ce champ est obligatoire' : ''">
                <NDatePicker :value="quittanceDTO.dateEmission || null" @update:value="val => quittanceDTO.dateEmission = val" type="date" value-format="yyyy-MM-dd" size="large" />
              </NFormItemGi>
              <NFormItemGi label="Date échéance">
                <NDatePicker
                  :value="quittanceDTO.dateEcheance || null"
                  @update:value="val => quittanceDTO.dateEcheance = val"
                  type="date"
                  value-format="yyyy-MM-dd"
                  size="large"
                  placeholder="Date calculée automatiquement"
                />
              </NFormItemGi>
              <NFormItemGi label="Montant loyer (€) *" :validation-status="errors.montantLoyer ? 'error' : undefined" :feedback="errors.montantLoyer ? 'Ce champ est obligatoire' : ''">
                <NInputNumber :value="quittanceDTO.montantLoyer ? Number.parseFloat(quittanceDTO.montantLoyer) : null" @update:value="val => quittanceDTO.montantLoyer = String(val)" min="0" placeholder="0.00" size="large" />
              </NFormItemGi>
              <NFormItemGi label="Montant charges (€) *" :validation-status="errors.montantCharges ? 'error' : undefined" :feedback="errors.montantCharges ? 'Ce champ est obligatoire' : ''">
                <NInputNumber :value="quittanceDTO.montantCharges ? Number.parseFloat(quittanceDTO.montantCharges) : null" @update:value="val => quittanceDTO.montantCharges = String(val)" min="0" placeholder="0.00" size="large" />
              </NFormItemGi>
              <NFormItemGi label="Inclure caution ?">
                <NRadioGroup v-model:value="quittanceDTO.inclureCaution">
                  <NRadio :value="true">Oui</NRadio>
                  <NRadio :value="false">Non</NRadio>
                </NRadioGroup>
              </NFormItemGi>
              <NFormItemGi v-if="quittanceDTO.inclureCaution" label="Montant caution (€)">
                <NInputNumber :value="quittanceDTO.depotGarantie ? Number.parseFloat(quittanceDTO.depotGarantie) : null" @update:value="val => quittanceDTO.depotGarantie = String(val)" min="0" placeholder="0.00" size="large" />
              </NFormItemGi>
              <NFormItemGi label="Montant total (€)">
                <NInputNumber :value="computedTotal" disabled size="large" />
              </NFormItemGi>
              <NFormItemGi label="Statut *" :validation-status="errors.statut ? 'error' : undefined" :feedback="errors.statut ? 'Ce champ est obligatoire' : ''">
                <NSelect
                  v-model:value="quittanceDTO.statut"
                  :options="[
                    { label: 'PAYÉE', value: 'PAYEE' },
                    { label: 'PARTIELLE', value: 'PARTIELLE' },
                    { label: 'IMPAYÉE', value: 'IMPAYEE' },
                  ]"
                  size="large"
                />
              </NFormItemGi>
            </NGrid>
          </NForm>
        </div>

        <div class="button-container">
          <div class="flex justify-between">
            <NButton @click="precedent" size="large">Précédent</NButton>
            <NButton type="primary" @click="suivant" size="large">Suivant</NButton>
          </div>
        </div>
      </NCard>
    </div>
  </div>
</template>

<style scoped>
/* Layout principal */
.page-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.recap-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: calc(100vh - 2rem);
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem;
}

.button-container {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
  background: white;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

/* Styles existants */
.titre-principal, h1, h2, h3 {
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

@media (max-width: 768px) {
  .page-container {
    min-height: 100vh;
  }

  .recap-card {
    min-height: calc(100vh - 2rem);
  }

  .content-area {
    max-height: calc(100vh - 200px);
    overflow-y: auto;
  }

  .button-container {
    position: sticky;
    bottom: 0;
    background: white;
    padding: 1rem 0;
    margin-top: 1rem;
    border-top: 1px solid #f0f0f0;
  }

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

  .titre-principal, h1, h2, h3 {
    font-size: 1.25rem !important;
  }

  .p-4 {
    padding: 1rem !important;
  }

  .mb-8, .mt-8 {
    margin-bottom: 1rem !important;
    margin-top: 1rem !important;
  }
}
</style>
