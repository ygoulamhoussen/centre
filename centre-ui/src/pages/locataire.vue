<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { 
  NButton, 
  NCard, 
  NEmpty, 
  NGrid, 
  NGi, 
  NSpace, 
  NIcon,
  useMessage,
  NSpin
} from 'naive-ui'
import { onMounted, ref, h } from 'vue'
import { useRouter } from 'vue-router'
import { Person24Filled, Add24Filled, Document24Filled } from '@vicons/fluent'

definePage({
  meta: {
    title: 'Mes locataires',
    icon: 'mdi:account-group',
    order: 3,
  },
})

const router = useRouter()
const message = useMessage()
const locataires = ref<any[]>([])
const loading = ref(true)
const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId

// Charger les locataires
async function fetchLocataires() {
  try {
    loading.value = true
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/${utilisateurId}`
    )
    if (!response.ok) throw new Error('Erreur lors du chargement des locataires')
    locataires.value = await response.json()
  } catch (error) {
    console.error('Erreur lors du chargement des locataires :', error)
    message.error('Impossible de charger les locataires')
  } finally {
    loading.value = false
  }
}

// Rediriger vers la page de détail d'un locataire
function viewLocataireDetails(id: string) {
  router.push(`/locataire/${id}`)
}

// Rediriger vers la page d'ajout d'un locataire
function addNewLocataire() {
  router.push('/locataire/nouveau')
}

// Formater la date
function formatDate(dateString: string) {
  if (!dateString) return 'Non spécifié'
  return new Date(dateString).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

// Charger les données au montage
onMounted(() => {
  fetchLocataires()
})
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Mes locataires</h1>
      <NButton type="primary" @click="addNewLocataire">
        <template #icon>
          <NIcon :component="Add24Filled" />
        </template>
        Ajouter un locataire
      </NButton>
    </div>

    <NSpin :show="loading">
      <div v-if="locataires.length === 0" class="text-center py-12">
        <NEmpty description="Aucun locataire trouvé">
          <template #extra>
            <NButton type="primary" @click="addNewLocataire">
              Ajouter un locataire
            </NButton>
          </template>
        </NEmpty>
      </div>

      <NGrid v-else :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4" class="locataires-grid">
        <NGi v-for="locataire in locataires" :key="locataire.id">
          <NCard 
            hoverable 
            class="locataire-card" 
            @click="viewLocataireDetails(locataire.id)"
          >
            <div class="flex items-start">
              <div class="locataire-avatar">
                <NIcon :component="Person24Filled" size="32" />
              </div>
              <div class="ml-4 flex-1">
                <h3 class="text-lg font-semibold mb-1">{{ locataire.prenom }} {{ locataire.nom }}</h3>
                <div class="text-gray-600 text-sm space-y-1">
                  <div v-if="locataire.telephone" class="flex items-center">
                    <span class="i-carbon-phone mr-2" />
                    {{ locataire.telephone }}
                  </div>
                  <div v-if="locataire.email" class="flex items-center">
                    <span class="i-carbon-email mr-2" />
                    {{ locataire.email }}
                  </div>
                  <div v-if="locataire.dateNaissance" class="flex items-center">
                    <span class="i-carbon-calendar mr-2" />
                    {{ formatDate(locataire.dateNaissance) }}
                  </div>
                </div>
              </div>
            </div>
          </NCard>
        </NGi>
      </NGrid>
    </NSpin>
  </div>
</template>

<style scoped>
.locataire-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.locataire-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.locataire-avatar {
  background-color: #f0f4ff;
  color: #3b82f6;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.locataires-grid {
  margin: 0 -8px;
}

:deep(.n-card__content) {
  flex: 1;
}

:deep(.n-card__footer) {
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  margin-top: 12px;
}
</style>
