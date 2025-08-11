<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowRight24Filled, Home24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDatePicker,
  NForm,
  NFormItem,
  NFormItemGi,
  NGrid,
  NH2,
  NIcon,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import QuittancePeriodeStep from './QuittancePeriodeStep.vue'

definePage({
  meta: {
    title: 'Ajouter une quittance - Étape 1',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})

const router = useRouter()
const message = useMessage()
const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)
const locations = ref<any[]>([])
const authStore = useAuthStore()
const userId = authStore.userInfo.userId
const isMobile = ref(window.innerWidth < 768)

// --- Logique pour la sélection de période dynamique ---
const selectedLocation = computed(() => locations.value.find(l => l.id === quittanceDTO.value.locationId))
const frequence = computed(() => selectedLocation.value?.frequenceLoyer)

const selectedYear = ref(new Date().getFullYear())
const selectedMonth = ref<number | null>(null)
const selectedQuarter = ref<number | null>(null)

const currentYear = new Date().getFullYear()

watch(
  [selectedYear, selectedMonth, selectedQuarter, frequence],
  ([year, month, quarter, freq]) => {
    let startDate: Date | null = null
    let endDate: Date | null = null

    const formatDate = (date: Date) => {
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    }

    if (year) {
      switch (freq) {
        case 'MENSUEL':
          if (month !== null) {
            startDate = new Date(year, month, 1)
            endDate = new Date(year, month + 1, 0)
          }
          break
        case 'TRIMESTRIEL':
          if (quarter !== null) {
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
  { immediate: true },
)

// Réinitialiser la période si la location change
watch(() => quittanceDTO.value.locationId, () => {
  selectedMonth.value = null
  selectedQuarter.value = null
})
// --- Fin de la logique de période ---

async function fetchLocations() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocationsByUtilisateur/${userId}`)
    const data = await res.json()
    locations.value = data
    store.setLocations(data)
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement des locations')
  }
}

function selectLocation(id: string) {
  quittanceDTO.value.locationId = id
  router.push('/quittance-etape-2')
}

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  fetchLocations()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <div class="mb-8" v-if="!isMobile">
          <NSteps :current="0" size="small">
            <NStep title="Location" status="process" description="Choix de la location" />
            <NStep title="Période" description="Année et mois/trimestre" />
            <NStep title="Détails" description="Montants et statut" />
            <NStep title="Récapitulatif" description="Vérification finale" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 1/4</div>
          <div class="step-mobile-label">Sélection de la location</div>
        </div>
        
        <div class="content-area">
          <NH2 class="titre-principal mb-4">Étape 1 : Sélection de la location</NH2>
          <NForm label-placement="top">
            <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
              <NFormItemGi :span="2" label="Location">
                <div class="locations-grid">
                  <NCard
                    v-for="loc in locations"
                    :key="loc.id"
                    class="location-card"
                    hoverable
                    @click="selectLocation(loc.id)"
                  >
                    <div class="flex items-start gap-4">
                      <div class="location-avatar">
                        <NIcon :component="Home24Filled" size="32" />
                      </div>
                      <div class="flex-1">
                        <div class="location-titre mb-2">{{ loc.proprieteNom }}</div>
                        <div class="text-sm space-y-1">
                          <div class="flex items-center gap-2">
                            <span class="i-carbon-user" />
                            <span>{{ loc.locataireNom }} {{ loc.locatairePrenom }}</span>
                          </div>
                          <div class="flex items-center gap-2">
                            <span class="i-carbon-calendar" />
                            <span>Début: {{ loc.dateDebut ? new Date(loc.dateDebut).toLocaleDateString('fr-FR', { year: 'numeric', month: 'long', day: 'numeric' }) : 'Non spécifié' }}</span>
                          </div>
                          <div class="flex items-center gap-2">
                            <span class="i-carbon-money" />
                            <span>{{ loc.loyerMensuel }}€ + {{ loc.chargesMensuelles }}€</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </NCard>
                </div>
              </NFormItemGi>
            </NGrid>
          </NForm>
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

/* Styles existants */
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

.justify-end {
  justify-content: flex-end;
}

.mt-8 {
  margin-top: 2rem;
}

.locations-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  width: 100%;
  margin: 0;
  max-width: 100%;
}

.location-card {
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  box-sizing: border-box;
  margin: 0;
}

.location-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.location-avatar {
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.location-titre {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
}

:deep(.n-card__content) {
  flex: 1;
}

.location-card-selected {
  border: 2px solid var(--n-color-primary);
  background: #f5faff;
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

  .mb-8 {
    margin-bottom: 1rem !important;
  }

  .steps-wrapper {
    overflow-x: auto !important;
  }

  .n-steps {
    font-size: 12px !important;
    min-width: 400px;
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
    padding: 0 !important;
  }

  .mb-8,
  .mt-8 {
    margin-bottom: 1rem !important;
    margin-top: 1rem !important;
  }
}
</style>