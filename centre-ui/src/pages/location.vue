<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { Add24Filled, Home24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NEmpty,
  NGi,
  NGrid,
  NH1,
  NH3,
  NIcon,
  NSpin,
  NText,
  useMessage,
} from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

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
      <NH1 class="titre-principal">Mes locations</NH1>
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
            <div class="flex items-start gap-4">
              <div class="location-avatar">
                <NIcon :component="Home24Filled" size="32" />
              </div>
              <div class="flex-1">
                <NH3 class="location-titre mb-2">
                  {{ location.proprieteNom }}
                </NH3>
                <div class="text-sm space-y-1">
                  <div class="flex items-center gap-2">
                    <span class="i-carbon-user" />
                    <NText depth="3">{{ location.locataireNom }} {{ location.locatairePrenom }}</NText>
                  </div>
                  <div class="flex items-center gap-2">
                    <span class="i-carbon-calendar" />
                    <NText depth="3">Début: {{ formatDate(location.dateDebut) }}</NText>
                  </div>
                  <div class="flex items-center gap-2">
                    <span class="i-carbon-money" />
                    <NText depth="3">{{ location.loyerMensuel }}€ + {{ location.chargesMensuelles }}€</NText>
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
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.location-titre {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
}
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
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
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

@media (max-width: 768px) {
  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }
  .location-titre {
    font-size: 1rem;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-6 {
    margin-bottom: 1rem !important;
  }
}
</style>
