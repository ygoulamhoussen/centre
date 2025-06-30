<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowRight24Filled } from '@vicons/fluent'
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
    locations.value = await res.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement des locations')
  }
}

function suivant() {
  if (!quittanceDTO.value.locationId || !quittanceDTO.value.dateDebut || !quittanceDTO.value.dateFin) {
    message.warning('Veuillez compléter tous les champs')
    return
  }
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
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="1" class="mb-8">
          <NStep title="Sélection" description="Location et période" />
          <NStep title="Détails" description="Montants et statut" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <NH2 class="titre-principal mb-4">Étape 1 : Sélection de la location et de la période</NH2>
      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
          <NFormItemGi :span="2" label="Location">
            <NSelect
              v-model:value="quittanceDTO.locationId"
              :options="locations.map(l => ({ label: `${l.proprieteNom} - ${l.locataireNom}`, value: l.id }))"
              placeholder="Choisir une location"
              size="large"
            />
          </NFormItemGi>
          
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
      <div class="flex justify-end mt-8">
        <NButton type="primary" @click="suivant" title="Suivant">
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
          </template>
        </NButton>
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
.justify-end {
  justify-content: flex-end;
}
.mt-8 {
  margin-top: 2rem;
}
@media (max-width: 768px) {
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
    padding: 1rem !important;
  }
  .mb-8,
  .mt-8 {
    margin-bottom: 1rem !important;
    margin-top: 1rem !important;
  }
}
</style>