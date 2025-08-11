<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowRight24Filled, BuildingHome24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItem,
  NH2,
  NIcon,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage,
  NGrid,
  NGi,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle location - Étape 1',
    hideInMenu: true,
    activeMenu: '/location',
  },
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId

const router = useRouter()
const message = useMessage()

const proprietes = ref<any[]>([])
const stepTitles = ['Propriété', 'Locataire', 'Détails du bail', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}

async function fetchData() {
  try {
    const propResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${utilisateurId}`)
    proprietes.value = await propResponse.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement')
  }
}

function selectPropriete(propriete: any) {
  locationDTO.value.proprieteId = propriete.id
  locationDTO.value.proprieteNom = propriete.nom
  router.push('/location-etape-2')
}

onMounted(() => {
  fetchData()
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
            <NStep title="Propriété" status="process" description="Choix du bien" />
            <NStep title="Locataire" description="Choix du locataire" />
            <NStep title="Détails du bail" description="Loyer, dates, etc." />
            <NStep title="Récapitulatif" description="Vérification finale" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 1/4</div>
          <div class="step-mobile-label">{{ stepTitles[0] }}</div>
        </div>
        
        <div class="content-area">
          <NH2 class="titre-principal mb-4">Étape 1: Sélection de la propriété</NH2>
          <NGrid :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
            <NGi v-for="propriete in proprietes" :key="propriete.id">
              <NCard
                hoverable
                class="propriete-card"
                @click="selectPropriete(propriete)"
              >
                <div class="flex items-start gap-4">
                  <div class="propriete-avatar">
                    <NIcon :component="BuildingHome24Filled" size="32" />
                  </div>
                  <div class="flex-1">
                    <div class="propriete-nom mb-2">{{ propriete.nom }}</div>
                    <div class="text-sm space-y-1">
                      <div v-if="propriete.adresse">{{ propriete.adresse }}</div>
                      <div v-if="propriete.surface">Surface : {{ propriete.surface }} m²</div>
                      <div v-if="propriete.type">Type : {{ propriete.type }}</div>
                    </div>
                  </div>
                </div>
              </NCard>
            </NGi>
          </NGrid>
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

.propriete-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.propriete-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border-color: #1976d2;
}

.propriete-avatar {
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.propriete-nom {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
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

  .mb-4,
  .mb-8 {
    margin-bottom: 1rem !important;
  }
}
</style>
