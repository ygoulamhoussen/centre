<script setup lang="ts">
import { Add24Filled, Document24Filled, Person24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NEmpty,
  NGi,
  NGrid,
  NIcon,
  NSpin,
  NSpace,
  NH1,
  NH2,
  NTag,
  NText,
  useMessage,
} from 'naive-ui'
import { h, onMounted, ref } from 'vue'
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
  fetchLocataires()
})
</script>

<template>
  <div class="p-4 locataire-page">
    <div class="header-locataire">
      <div class="header-title">
        <NIcon :component="Person24Filled" size="28" class="mr-2 text-primary" />
        <NH1 class="titre-principal">Mes locataires</NH1>
      </div>
      <NButton type="primary" class="ajouter-btn" @click="addNewLocataire">
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
            tabindex="0"
            @keydown.enter="viewLocataireDetails(locataire.id)"
          >
            <div class="flex items-start gap-4">
              <div class="locataire-avatar" :style="{ background: stringToColor(locataire.nom + locataire.prenom) }">
                <span v-if="!locataire.photo" class="avatar-initiales">{{ (locataire.prenom?.[0] || '') + (locataire.nom?.[0] || '') }}</span>
                <!-- Si photo, afficher ici -->
              </div>
              <div class="flex-1">
                <NH2 class="locataire-nom mb-1">{{ locataire.prenom }} {{ locataire.nom }}</NH2>
                <div class="text-gray-600 text-sm space-y-1">
                  <div v-if="locataire.telephone" class="flex items-center gap-2">
                    <span class="i-carbon-phone" />
                    <NText depth="3">{{ locataire.telephone }}</NText>
                    <NTag type="success" size="small" v-if="locataire.telephone">Vérifié</NTag>
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
                <div class="actions-rapides mt-2">
                  <NButton v-if="locataire.telephone" size="tiny" tertiary tag="a" :href="'tel:' + locataire.telephone" @click.stop>
                    <span class="i-carbon-phone" /> Appeler
                  </NButton>
                  <NButton v-if="locataire.email" size="tiny" tertiary tag="a" :href="'mailto:' + locataire.email" @click.stop>
                    <span class="i-carbon-email" /> Email
                  </NButton>
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
.locataire-page {
  max-width: 1200px;
  margin: 0 auto;
}
.header-locataire {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}
.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
}
.ajouter-btn {
  min-width: 180px;
  font-weight: 500;
}
.titre-principal {
  color: var(--n-text-color) !important;
  font-weight: bold;
  font-size: 1.5rem;
}
.locataire-nom {
  color: var(--n-text-color) !important;
  font-weight: 600;
  font-size: 1.1rem;
}
.locataire-card {
  cursor: pointer;
  transition:
    box-shadow 0.2s,
    transform 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 1rem 0.5rem;
  outline: none;
}
.locataire-card:hover, .locataire-card:focus {
  transform: translateY(-2px) scale(1.01);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.13);
  border-color: #2563eb;
}
.locataire-avatar {
  background-color: #f0f4ff;
  color: #3b82f6;
  border-radius: 50%;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.3rem;
  font-weight: bold;
  box-shadow: 0 2px 8px #0001;
}
.avatar-initiales {
  color: #2563eb;
  font-weight: bold;
  font-size: 1.2em;
}
.locataires-grid {
  margin: 0 -8px;
}
.actions-rapides {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
:deep(.n-card__content) {
  flex: 1;
}
:deep(.n-card__footer) {
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  margin-top: 12px;
}
@media (max-width: 900px) {
  .locataire-page {
    padding: 0 2px;
  }
  .header-locataire {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }
  .ajouter-btn {
    width: 100%;
    min-width: 0;
    max-width: 320px;
    align-self: center;
  }
  .titre-principal {
    font-size: 1.2rem;
    text-align: center;
  }
}
@media (max-width: 600px) {
  .locataire-avatar {
    width: 44px;
    height: 44px;
    font-size: 1rem;
  }
  .locataire-nom {
    font-size: 1rem;
  }
  .header-title {
    font-size: 1rem;
  }
}
</style>
