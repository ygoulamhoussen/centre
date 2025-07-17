<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowLeft24Filled, ArrowRight24Filled, Person24Filled } from '@vicons/fluent'
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
    title: 'Nouvelle location - Étape 2',
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

const locataires = ref<any[]>([])
const stepTitles = ['Propriété', 'Locataire', 'Détails du bail', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}

async function fetchData() {
  try {
    const locResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/${utilisateurId}`)
    locataires.value = await locResponse.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement')
  }
}

function handleLocataireChange(value: string) {
  const selected = locataires.value.find(l => l.id === value)
  if (selected) {
    locationDTO.value.locataireId = selected.id
    locationDTO.value.locataireNom = selected.nom
  }
}

function suivant() {
  if (!locationDTO.value.locataireId) {
    message.warning('Merci de choisir un locataire')
    return
  }
  router.push('/location-etape-3')
}

function precedent() {
  router.push('/location-etape-1')
}

function formatDate(dateString: string) {
  if (!dateString) return 'Non spécifié'
  return new Date(dateString).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}
function selectLocataire(locataire: any) {
  locationDTO.value.locataireId = locataire.id
  locationDTO.value.locataireNom = locataire.nom
  router.push('/location-etape-3')
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
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="1" size="small">
          <NStep title="Propriété" status="finish" description="Choix du bien" />
          <NStep title="Locataire" status="process" description="Choix du locataire" />
          <NStep title="Détails du bail" description="Loyer, dates, etc." />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 2/4</div>
        <div class="step-mobile-label">{{ stepTitles[1] }}</div>
      </div>

      <NH2 class="titre-principal mb-4">Étape 2: Sélection du locataire</NH2>

      <NGrid :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
        <NGi v-for="locataire in locataires" :key="locataire.id">
          <NCard
            hoverable
            class="locataire-card"
            @click="selectLocataire(locataire)"
          >
            <div class="flex items-start gap-4">
              <div class="locataire-avatar">
                <span v-if="!locataire.photo" class="avatar-initiales">{{ (locataire.prenom?.[0] || '') + (locataire.nom?.[0] || '') }}</span>
                <!-- Si photo, afficher ici -->
              </div>
              <div class="flex-1">
                <div class="locataire-nom mb-2">{{ locataire.prenom }} {{ locataire.nom }}</div>
                <div class="text-sm space-y-1">
                  <div v-if="locataire.telephone" class="flex items-center gap-2">
                    <span class="i-carbon-phone" />
                    <span>{{ locataire.telephone }}</span>
                  </div>
                  <div v-if="locataire.email" class="flex items-center gap-2">
                    <span class="i-carbon-email" />
                    <span>{{ locataire.email }}</span>
                  </div>
                  <div v-if="locataire.dateNaissance" class="flex items-center gap-2">
                    <span class="i-carbon-calendar" />
                    <span>{{ formatDate(locataire.dateNaissance) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </NCard>
        </NGi>
      </NGrid>
      <NSpace class="flex justify-between mt-8">
        <NButton @click="precedent" size="large" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
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
  .mb-4,
  .mb-8 {
    margin-bottom: 1rem !important;
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
.locataire-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.locataire-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border-color: #1976d2;
}
.locataire-avatar {
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-initiales {
  color: #2563eb;
  font-weight: bold;
  font-size: 1.2em;
}
.locataire-nom {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
}
</style>
