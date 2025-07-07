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
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
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
  <div class="p-4">
    <NCard :bordered="false">
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
      <NH2 class="titre-principal mb-4">Étape 3 : Détails de la quittance</NH2>
      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
          <NFormItemGi label="Date émission *">
            <NDatePicker :value="quittanceDTO.dateEmission || undefined" @update:value="val => quittanceDTO.dateEmission = val" type="date" value-format="yyyy-MM-dd" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Date échéance">
            <NDatePicker
              :value="quittanceDTO.dateEcheance || undefined"
              @update:value="val => quittanceDTO.dateEcheance = val"
              type="date"
              value-format="yyyy-MM-dd"
              size="large"
              placeholder="Date calculée automatiquement"
            />
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
        <NButton @click="precedent" size="large">Précédent</NButton>
        <NButton type="primary" @click="suivant" size="large">Suivant</NButton>
      </div>
    </NCard>
  </div>
</template>

<style scoped>
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
