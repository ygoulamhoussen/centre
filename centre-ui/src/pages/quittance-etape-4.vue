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
  NIcon
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref, onMounted, onUnmounted } from 'vue'
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
const { quittanceDTO } = storeToRefs(store)
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

function precedent() {
  router.push('/quittance-etape-3')
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

</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <div class="mb-8" v-if="!isMobile">
          <NSteps :current="3" size="small">
            <NStep title="Location" status="finish" description="Choix de la location" />
            <NStep title="Période" status="finish" description="Année et mois/trimestre" />
            <NStep title="Détails" status="finish" description="Montants et statut" />
            <NStep title="Récapitulatif" status="process" description="Vérification finale" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 4/4</div>
          <div class="step-mobile-label">{{ stepTitles[3] }}</div>
        </div>
        
        <div class="content-area">
          <NH2 class="titre-principal mb-4">Étape 4 : Récapitulatif</NH2>
          <NDescriptions label-placement="top" bordered :column="isMobile ? 1 : 2">
            <NDescriptionsItem label="Location ID">
              {{ quittanceDTO.locationId }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Date début">
              {{ quittanceDTO.dateDebut }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Date fin">
              {{ quittanceDTO.dateFin || '—' }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Date émission">
              {{ quittanceDTO.dateEmission }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Loyer (€)">
              {{ quittanceDTO.montantLoyer }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Charges (€)">
              {{ quittanceDTO.montantCharges }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Inclure caution">
              {{ quittanceDTO.inclureCaution ? 'Oui' : 'Non' }}
            </NDescriptionsItem>
            <NDescriptionsItem v-if="quittanceDTO.inclureCaution" label="Montant caution (€)">
              {{ quittanceDTO.depotGarantie }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Montant total (€)">
              {{ quittanceDTO.montantTotal }}
            </NDescriptionsItem>
            <NDescriptionsItem label="Statut">
              {{ quittanceDTO.statut }}
            </NDescriptionsItem>
          </NDescriptions>
        </div>

        <div class="button-container">
          <NSpace justify="space-between">
            <NButton @click="precedent" title="Précédent">
              <template #icon>
                <NIcon :component="ArrowLeft24Filled" />
              </template>
            </NButton>
            <NButton type="primary" @click="enregistrer" :loading="loading" title="Valider">
              <template #icon>
                <NIcon :component="Checkmark24Filled" />
              </template>
            </NButton>
          </NSpace>
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