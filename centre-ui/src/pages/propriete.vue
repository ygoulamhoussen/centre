<script setup lang="ts">
import { Box24Regular, Building24Regular, Home24Regular, VehicleCar24Regular } from '@vicons/fluent'


// Importation des icônes depuis @vicons/fluent

import {
  NButton,
  NCard,
  NEmpty,
  NGi,
  NGrid,
  NPopconfirm,
  NSpace,
  useMessage,
} from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

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
  }
 catch (error) {
    console.error('Erreur lors du chargement des propriétés :', error)
    message.error('Impossible de charger les propriétés')
  }
}

async function supprimerPropriete(id: string) {
  try {
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deletePropriete/${id}`, {
      method: 'DELETE',
    })
    message.success('Propriété supprimée')
    await fetchProprietes()
  }
 catch (error) {
    console.error('Erreur lors de la suppression :', error)
    message.error('Erreur lors de la suppression')
  }
}

function demarrerCreation() {
  router.push('/propriete_workflow/etape-1')
}

// Fonction pour obtenir l'icône en fonction du type de bien
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
          <div class="mb-2 flex items-center gap-3">
            <n-icon :component="getIconComponent(propriete.typeBien)" size="40" :depth="1" />
            <div>
              <p>{{ propriete.adresse }}, {{ propriete.ville }}</p>
              <p><strong>Type :</strong> {{ propriete.typeBien }}</p>
            </div>
          </div>

          <template #footer>
            <div class="flex justify-end">
              <NPopconfirm
                @positive-click="() => supprimerPropriete(propriete.id)"
                positive-text="Supprimer"
                negative-text="Annuler"
              >
                <template #trigger>
                  <NButton size="small" type="error" ghost>
                    Supprimer
                  </NButton>
                </template>
                Êtes-vous sûr de vouloir supprimer cette propriété ?
              </NPopconfirm>
            </div>
          </template>
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucune propriété enregistrée." v-else />
  </NSpace>
</template>

<style scoped></style>
