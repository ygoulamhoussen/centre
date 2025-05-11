<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { NDescriptions, NDescriptionsItem, NH1, NSpin, useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

definePage({
  meta: {
    title: 'Détail de la propriété',
    activeMenu: '/propriete',
    hideInMenu: true,
  },
})
const store = useUnifiedStore()
const message = useMessage()
const propriete = ref<any | null>(null)
const loading = ref(true)

async function fetchProprieteDetails() {
  try {
    const id = store.selectedProprieteId
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getPropriete/${id}`)
    if (!res.ok) {
      throw new Error('Propriété introuvable')
    }
    propriete.value = await res.json()
  }
  catch (e) {
    message.error('Erreur de chargement de la propriété.')
  } finally {
    loading.value = false
  }
}

onMounted(fetchProprieteDetails)
</script>

<template>
  <div>
    <NH1>Détails de la propriété</NH1>
    <NSpin :show="loading">
      <NDescriptions label-placement="top" bordered v-if="propriete">
        <NDescriptionsItem label="Nom">{{ propriete.nom }}</NDescriptionsItem>
        <NDescriptionsItem label="Adresse">{{ propriete.adresse }}</NDescriptionsItem>
        <NDescriptionsItem label="Ville">{{ propriete.ville }}</NDescriptionsItem>
        <NDescriptionsItem label="Type">{{ propriete.typeBien }}</NDescriptionsItem>
        <NDescriptionsItem label="Montant acquisition">{{ propriete.montantAcquisition }} €</NDescriptionsItem>
        <NDescriptionsItem label="Date acquisition">{{ propriete.dateAcquisition }}</NDescriptionsItem>
        <NDescriptionsItem label="Tantième">{{ propriete.tantieme }}</NDescriptionsItem>
      </NDescriptions>
    </NSpin>
  </div>
</template>
