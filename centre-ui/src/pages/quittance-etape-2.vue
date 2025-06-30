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
const selectedLocation = computed(() => {
  return locations.value.find(loc => loc.id === quittanceDTO.value.locationId)
})

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

// Calcul automatique de la date d'échéance
function computeEcheance(dateRef: string, jourEcheance: string) {
  if (!dateRef || !jourEcheance) {
    return ''
  }
  const ref = new Date(dateRef)
  let annee = ref.getFullYear()
  let mois = ref.getMonth()
  const jour = Number.parseInt(jourEcheance, 10)
  // Si le jour d'échéance est avant la date de référence, passer au mois suivant
  if (jour <= ref.getDate()) {
    mois += 1
    if (mois > 11) {
      mois = 0
      annee += 1
    }
  }
  // Gérer les dépassements de jours (ex: 31 février)
  const dateEcheance = new Date(annee, mois, jour)
  return format(dateEcheance, 'yyyy-MM-dd')
}

// pré-remplir les champs quand la location est trouvée
watch(
  selectedLocation,
  (newLocation) => {
    if (newLocation) {
      // La caution NE DOIT JAMAIS être multipliée, même pour un loyer trimestriel
      const freq = (newLocation.frequenceLoyer || '').toString().trim().toLowerCase()
      const loyerMensuel = newLocation.loyerMensuel
      quittanceDTO.value.montantCharges = newLocation.chargesMensuelles
      quittanceDTO.value.depotGarantie = newLocation.depotGarantie
      if (freq === 'trimestriel') {
        quittanceDTO.value.montantLoyer = String(Number.parseFloat(loyerMensuel || '0') * 3)
      } else {
        quittanceDTO.value.montantLoyer = loyerMensuel
      }
      quittanceDTO.value.dateEmission = getToday()
      quittanceDTO.value.dateEcheance = computeEcheance(
        quittanceDTO.value.dateEmission,
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

function suivant() {
  // validation sommaire
  if (
    !quittanceDTO.value.dateDebut
    || !quittanceDTO.value.dateFin
    || !quittanceDTO.value.dateEmission
    || !quittanceDTO.value.statut
  ) {
    message.warning('Merci de renseigner tous les champs obligatoires (*)')
    return
  }
  // on stocke le total calculé
  quittanceDTO.value.montantTotal = computedTotal.value.toFixed(2)
  router.push('/quittance-etape-3')
}

const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="2" class="mb-8">
          <NStep title="Sélection" description="Location et période" />
          <NStep title="Détails" description="Montants et statut" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <NH2 class="titre-principal mb-4">Étape 2 : Détails de la quittance</NH2>
      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
          <NFormItemGi label="Date émission *">
            <NDatePicker v-model:formatted-value="quittanceDTO.dateEmission" type="date" value-format="yyyy-MM-dd" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Date échéance">
            <NDatePicker v-model:formatted-value="quittanceDTO.dateEcheance" type="date" value-format="yyyy-MM-dd" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Montant loyer (€) *">
            <NInputNumber :value="Number.parseFloat(quittanceDTO.montantLoyer || '0')" @update:value="val => quittanceDTO.montantLoyer = String(val)" min="0" placeholder="0.00" size="large" />
            <div v-if="isTrimestriel" style="color: #1976d2; font-weight: bold; font-size: 1em; margin-top: 2px;">
              {{ loyerDetail }}
            </div>
          </NFormItemGi>
          <NFormItemGi label="Montant charges (€) *">
            <NInputNumber :value="Number.parseFloat(quittanceDTO.montantCharges || '0')" @update:value="val => quittanceDTO.montantCharges = String(val)" min="0" placeholder="0.00" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Inclure caution ?">
            <NRadioGroup v-model:value="quittanceDTO.inclureCaution">
              <NRadio :value="true">Oui</NRadio>
              <NRadio :value="false">Non</NRadio>
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="quittanceDTO.inclureCaution" label="Montant caution (€)">
            <NInputNumber :value="Number.parseFloat(quittanceDTO.depotGarantie || '0')" @update:value="val => quittanceDTO.depotGarantie = String(val)" min="0" placeholder="0.00" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Montant total (€)">
            <NInputNumber :value="computedTotal" disabled size="large" />
          </NFormItemGi>
          <NFormItemGi label="Statut *">
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
      <div class="flex justify-between mt-8">
        <NButton @click="$router.back()" size="large">Précédent</NButton>
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
</style>
