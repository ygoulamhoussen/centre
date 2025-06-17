<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { Box24Regular, Building24Regular, Home24Regular, VehicleCar24Regular } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NEmpty,
  NGi,
  NGrid,
  NIcon,
  NPopconfirm,
  NSpace,
  useMessage
} from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Mes propriétés',
    icon: 'mdi:home-city',
    order: 2,
  }
})

const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()
const proprietes = ref<any[]>([])
const store = useUnifiedStore()

async function fetchProprietes() {
  try {
    const userId = authStore.userInfo.userId
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${userId}`
    )
    proprietes.value = await response.json()
  } catch (error) {
    console.error('Erreur lors du chargement des propriétés :', error)
    message.error('Impossible de charger les propriétés')
  }
}

async function supprimerPropriete(id: string) {
  try {
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deletePropriete/${id}`, {
      method: 'DELETE'
    })
    message.success('Propriété supprimée')
    await fetchProprietes()
  } catch (error) {
    console.error('Erreur lors de la suppression :', error)
    message.error('Erreur lors de la suppression')
  }
}

function demarrerCreation() {
  // Réinitialiser le DTO de la propriété
  store.resetProprieteDTO()
  router.push('/propriete-etape-1')
}

function allerAuDetail(id: string) {
  store.setSelectedProprieteId(id)
  router.push('/propriete-detail')
}

function getIconComponent(typeBien: string) {
  switch (typeBien.toUpperCase()) {
    case 'APPARTEMENT':
      return Building24Regular
    case 'MAISON':
      return Home24Regular
    case 'PARKING':
      return VehicleCar24Regular
    case 'BOX':
      return Box24Regular
    default:
      return Home24Regular
  }
}

onMounted(fetchProprietes)
</script>

<template>
  <NSpace vertical :size="24">
    <div class="flex items-center justify-between">
      <h1 class="text-xl font-bold">Mes propriétés</h1>
      <NButton type="primary" @click="demarrerCreation">
        Ajouter une propriété
      </NButton>
    </div>

    <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16" v-if="proprietes.length">
      <NGi v-for="propriete in proprietes" :key="propriete.id">
        <NCard
          class="hoverable-card"
          :title="propriete.nom"
          size="small"
          content-style="cursor: pointer;"
          @click="allerAuDetail(propriete.id)"
        >
          <div class="mb-2 flex items-center gap-3">
            <NIcon :component="getIconComponent(propriete.typeBien)" size="40" :depth="1" />
            <div>
              <p>{{ propriete.adresse }}, {{ propriete.ville }}</p>
              <p><strong>Type :</strong> {{ propriete.typeBien }}</p>
            </div>
          </div>
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucune propriété enregistrée." v-else />
  </NSpace>
</template>

<style scoped>
.hoverable-card {
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s,
    border-color 0.2s,
    background-color 0.2s;
  border-radius: 8px;
}
.hoverable-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  background-color: #f9f9f9;
  border: 1px solid #409eff;
}
.flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
