<script setup lang="ts">
import { useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import { useMessage, NButton, NCard, NSpace, NEmpty, NGrid, NGi } from 'naive-ui'

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
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/00000000-0000-0000-0000-000000000001`
    )
    const data = await response.json()
    locataires.value = data
  } catch (error) {
    console.error('Erreur lors du chargement des locataires :', error)
    message.error('Impossible de charger les locataires')
  }
}

function demarrerCreation() {
  router.push('/locataire_workflow/etape-1')
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
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucun locataire enregistré." v-else />
  </NSpace>
</template>

<style scoped></style>
