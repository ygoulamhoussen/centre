<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import {
  NButton,
  NCard,
  NEmpty,
  NGrid,
  NGi,
  NIcon,
  useMessage,
  NSpin,
} from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Add24Filled, Home24Filled } from '@vicons/fluent'

definePage({
  meta: {
    title: 'Mes locations',
    icon: 'mdi:key-outline',
    order: 4,
  },
})

const router = useRouter()
const message = useMessage()
const locations = ref<any[]>([])
const loading = ref(true)
const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId

async function fetchLocations() {
  try {
    loading.value = true
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocationsByUtilisateur/${utilisateurId}`
    )
    if (!response.ok) throw new Error('Erreur chargement locations')
    locations.value = await response.json()
  } catch (error) {
    console.error(error)
    message.error('Impossible de charger les locations')
  } finally {
    loading.value = false
  }
}

function viewLocationDetails(id: string) {
  router.push(`/location/${id}`)
}

function demarrerCreation() {
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

onMounted(() => {
  fetchLocations()
})
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Mes locations</h1>
      <NButton type="primary" @click="demarrerCreation">
        <template #icon>
          <NIcon :component="Add24Filled" />
        </template>
        Créer une location
      </NButton>
    </div>

    <NSpin :show="loading">
      <div v-if="locations.length === 0" class="text-center py-12">
        <NEmpty description="Aucune location trouvée">
          <template #extra>
            <NButton type="primary" @click="demarrerCreation">
              Créer une location
            </NButton>
          </template>
        </NEmpty>
      </div>

      <NGrid v-else :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
        <NGi v-for="location in locations" :key="location.id">
          <NCard
            hoverable
            class="location-card"
            @click="viewLocationDetails(location.id)"
          >
            <div class="flex items-start">
              <div class="location-avatar">
                <NIcon :component="Home24Filled" size="32" />
              </div>
              <div class="ml-4 flex-1">
                <h3 class="text-lg font-semibold mb-1">
                  {{ location.proprieteNom }}
                </h3>
                <div class="text-gray-600 text-sm space-y-1">
                  <div class="flex items-center">
                    <span class="i-carbon-user mr-2" />
                    {{ location.locataireNom }} {{ location.locatairePrenom }}
                  </div>
                  <div class="flex items-center">
                    <span class="i-carbon-calendar mr-2" />
                    Début: {{ formatDate(location.dateDebut) }}
                  </div>
                  <div class="flex items-center">
                    <span class="i-carbon-money mr-2" />
                    {{ location.loyerMensuel }}€ + {{ location.chargesMensuelles }}€
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
.location-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.location-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.location-avatar {
  background-color: #e6f7ff;
  color: #1890ff;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.n-card__content) {
  flex: 1;
}
</style>
