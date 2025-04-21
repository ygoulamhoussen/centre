<script setup lang="ts">
import { useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import { useMessage } from 'naive-ui'

definePage({
  meta: {
    title: 'Mes propriétés',
    icon: 'mdi:home-city',
    order: 2,
  },
})

const router = useRouter()
const message = useMessage()

const proprietes = ref<any[]>([])

async function fetchProprietes() {
  try {
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/00000000-0000-0000-0000-000000000001`
    )
    const data = await response.json()
    proprietes.value = data
  } catch (error) {
    console.error('Erreur lors du chargement des propriétés :', error)
    message.error('Impossible de charger les propriétés')
  }
}

function demarrerCreation() {
  router.push('/propriete_workflow/etape-1')
}

onMounted(() => {
  fetchProprietes()
})
</script>

<template>
  <NSpace vertical :size="24">
    <div class="flex items-center justify-between">
      <h1 class="text-xl font-bold">Mes propriétés</h1>
      <NButton type="primary" icon="ri-add-line" @click="demarrerCreation">
        Ajouter une propriété
      </NButton>
    </div>

    <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16" v-if="proprietes.length > 0">
      <NGi v-for="propriete in proprietes" :key="propriete.id">
        <NCard :title="propriete.nom" size="small">
          <p>{{ propriete.adresse }}, {{ propriete.ville }}</p>
          <p><strong>Type:</strong> {{ propriete.typeBien }}</p>
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucune propriété enregistrée." v-else />
  </NSpace>
</template>

<style scoped></style>
