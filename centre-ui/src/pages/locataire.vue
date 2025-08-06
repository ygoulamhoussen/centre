<script setup lang="ts">
import { Add24Filled, Delete24Regular, Document24Filled, Person24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NEmpty,
  NGi,
  NGrid,
  NIcon,
  NPopconfirm,
  NSpin,
  NSpace,
  NH1,
  NH2,
  NTag,
  NText,
  useMessage,
} from 'naive-ui'
import { h, onMounted, ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'

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
const gridCols = ref(4)
function updateGridCols() {
  const w = window.innerWidth
  if (w < 600) gridCols.value = 1
  else if (w < 900) gridCols.value = 2
  else if (w < 1200) gridCols.value = 3
  else if (w < 1600) gridCols.value = 4
  else gridCols.value = 5
}

// Charger les locataires
async function fetchLocataires() {
  try {
    loading.value = true
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/${utilisateurId}`,
    )
    if (!response.ok) {
      throw new Error('Erreur lors du chargement des locataires')
    }
    locataires.value = await response.json()
  } catch (error) {
    console.error('Erreur lors du chargement des locataires :', error)
    message.error('Impossible de charger les locataires')
  }
  finally {
    loading.value = false
  }
}

// Rediriger vers la page de détail d'un locataire
function viewLocataireDetails(id: string) {
  router.push(`/locataire/${id}`)
}

// Rediriger vers la page d'ajout d'un locataire
function addNewLocataire() {
  router.push('/locataire-etape-1')
}

async function supprimerLocataire(id: string) {
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteLocataire/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || 'Erreur lors de la suppression')
    }

    message.success('Locataire supprimé')
    await fetchLocataires()
  } catch (error) {
    console.error('Erreur lors de la suppression :', error)
    message.error(error instanceof Error ? error.message : 'Erreur lors de la suppression')
  }
}

// Formater la date
function formatDate(dateString: string) {
  if (!dateString) {
    return 'Non spécifié'
  }
  return new Date(dateString).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

// Ajout utilitaire pour couleur d'avatar
function stringToColor(str: string) {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash)
  }
  let color = '#'
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xff
    color += ('00' + value.toString(16)).slice(-2)
  }
  return color + '22' // Opacité légère
}

// Charger les données au montage
onMounted(() => {
  updateGridCols()
  window.addEventListener('resize', updateGridCols)
  fetchLocataires()
})
onUnmounted(() => {
  window.removeEventListener('resize', updateGridCols)
})
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-6">
      <NH1 class="titre-principal">Mes locataires</NH1>
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

      <NGrid v-else :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
        <NGi v-for="locataire in locataires" :key="locataire.id">
          <NCard
            hoverable
            class="locataire-card"
            @click="viewLocataireDetails(locataire.id)"
          >
            <div class="flex items-start gap-4">
              <div class="locataire-avatar">
                <span v-if="!locataire.photo" class="avatar-initiales">{{ (locataire.prenom?.[0] || '') + (locataire.nom?.[0] || '') }}</span>
                <!-- Si photo, afficher ici -->
              </div>
              <div class="flex-1">
                <NH3 class="locataire-nom mb-2">{{ locataire.prenom }} {{ locataire.nom }}</NH3>
                <div class="text-sm space-y-1">
                  <div v-if="locataire.telephone" class="flex items-center gap-2">
                    <span class="i-carbon-phone" />
                    <NText depth="3">{{ locataire.telephone }}</NText>
                  </div>
                  <div v-if="locataire.email" class="flex items-center gap-2">
                    <span class="i-carbon-email" />
                    <NText depth="3">{{ locataire.email }}</NText>
                  </div>
                  <div v-if="locataire.dateNaissance" class="flex items-center gap-2">
                    <span class="i-carbon-calendar" />
                    <NText depth="3">{{ formatDate(locataire.dateNaissance) }}</NText>
                  </div>
                </div>
              </div>
            </div>
            <template #footer>
              <div class="flex justify-end gap-2">
                <NPopconfirm
                  @positive-click="supprimerLocataire(locataire.id)"
                  positive-text="Supprimer"
                  negative-text="Annuler"
                >
                  <template #trigger>
                    <NButton
                      size="small"
                      type="error"
                      ghost
                      @click.stop
                    >
                      <template #icon>
                        <NIcon :component="Delete24Regular" />
                      </template>
                    </NButton>
                  </template>
                  Êtes-vous sûr de vouloir supprimer ce locataire ?
                </NPopconfirm>
              </div>
            </template>
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
.locataire-nom {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
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
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
  .locataire-nom {
    font-size: 1rem;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-6 {
    margin-bottom: 1rem !important;
  }
  .flex {
    flex-direction: column !important;
    align-items: stretch !important;
    gap: 12px !important;
  }
  .titre-principal {
    text-align: center;
    margin-bottom: 0.5rem;
  }
  .n-button {
    width: 100%;
    max-width: 320px;
    align-self: center;
  }
}
</style>
