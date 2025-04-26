<script setup lang="ts">
import { NButton, NCard, NEmpty, NGi, NGrid, NPopconfirm, NSpace, useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

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

async function fetchLocataires() {
  try {
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/00000000-0000-0000-0000-000000000001`,
    )
    const data = await response.json()
    locataires.value = data
  }
  catch (error) {
    console.error('Erreur lors du chargement des locataires :', error)
    message.error('Impossible de charger les locataires')
  }
}

async function supprimerLocataire(id: string) {
  try {
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteLocataire/${id}`, {
      method: 'DELETE',
    })
    message.success('Locataire supprimé')
    await fetchLocataires()
  }
  catch (error) {
    console.error('Erreur lors de la suppression du locataire :', error)
    message.error('Erreur lors de la suppression')
  }
}

function demarrerCreation() {
  router.push('/locataire-etape-1')
}

onMounted(() => {
  fetchLocataires()
})
</script>

<template>
  <NSpace vertical :size="24">
    <div class="flex items-center justify-between">
      <h1 class="text-xl font-bold">Mes locataires</h1>
      <NButton type="primary" icon="ri-add-line" @click="demarrerCreation">
        Ajouter un locataire
      </NButton>
    </div>

    <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16" v-if="locataires.length > 0">
      <NGi v-for="locataire in locataires" :key="locataire.id">
        <NCard :title="locataire.nom" size="small">
          <p><strong>Email :</strong> {{ locataire.email }}</p>
          <p><strong>Téléphone :</strong> {{ locataire.telephone }}</p>
          <p>{{ locataire.adresse }}, {{ locataire.ville }}</p>

          <template #footer>
            <div class="flex justify-end">
              <NPopconfirm
                @positive-click="() => supprimerLocataire(locataire.id)"
                negative-text="Annuler"
                positive-text="Supprimer"
              >
                <template #trigger>
                  <NButton size="small" type="error" ghost>
                    Supprimer
                  </NButton>
                </template>
                Êtes-vous sûr de vouloir supprimer ce locataire ?
              </NPopconfirm>
            </div>
          </template>
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucun locataire enregistré." v-else />
  </NSpace>
</template>

<style scoped></style>
