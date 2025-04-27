<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth' // adapte si ton chemin est différent
import { NButton, NCard, NEmpty, NGi, NGrid, NH1, NPopconfirm, NSpace, useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Mes locations',
    icon: 'mdi:home-city-outline', // Icône plus adapté pour "location"
    order: 4,
  },
})

const router = useRouter()
const message = useMessage()
const locations = ref<any[]>([])

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId // 💥 le vrai utilisateur connecté !

async function fetchLocations() {
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocationsByUtilisateur/${utilisateurId}`)
    locations.value = await response.json()
  } catch (error) {
    console.error('Erreur fetchLocations', error)
    message.error('Erreur de chargement des locations')
  }
}

async function supprimerLocation(id: string) {
  try {
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteLocation/${id}`, { method: 'DELETE' })
    message.success('Location supprimée')
    await fetchLocations()
  } catch (error) {
    console.error('Erreur deleteLocation', error)
    message.error('Erreur lors de la suppression')
  }
}

function demarrerCreation() {
  router.push('/location-etape-1')
}

onMounted(() => {
  fetchLocations()
})
</script>

<template>
  <NSpace vertical :size="24">
    <div class="flex justify-between items-center">
      <NH1>Mes locations</NH1>
      <NButton type="primary" @click="demarrerCreation">Créer une location</NButton>
    </div>

    <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16" v-if="locations.length">
      <NGi v-for="location in locations" :key="location.id">
        <NCard :title="location.proprieteNom">
          <p><strong>Locataire:</strong> {{ location.locataireNom }}</p>
          <p><strong>Propriété:</strong> {{ location.proprieteNom }}</p>
          <p><strong>Loyer:</strong> {{ location.loyerMensuel }} €</p>
          <template #footer>
            <div class="flex justify-end">
              <NPopconfirm
                @positive-click="() => supprimerLocation(location.id)"
                positive-text="Supprimer"
                negative-text="Annuler"
              >
                <template #trigger>
                  <NButton size="small" type="error" ghost>Supprimer</NButton>
                </template>
                Confirmer la suppression ?
              </NPopconfirm>
            </div>
          </template>
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucune location enregistrée." v-else />
  </NSpace>
</template>

<style scoped></style>
