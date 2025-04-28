<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { NButton, NDescriptions, NDescriptionsItem, NH1, NSpace, useMessage } from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const message = useMessage()
const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)
const authStore = useAuthStore()
const userId = authStore.userInfo.userId
const loading = ref(false)

const total = computed(() => {
  const loyer = parseFloat(quittanceDTO.value.montantLoyer || '0')
  const charges = parseFloat(quittanceDTO.value.montantCharges || '0')
  return (loyer + charges).toFixed(2)
})

function precedent() {
  router.push('/quittance-etape-2')
}

async function enregistrer() {
  loading.value = true
  try {
    const payload = { ...quittanceDTO.value, utilisateurId: userId }
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/createQuittance`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload)
    })
    if (!res.ok) throw new Error('Erreur serveur')
    message.success('Quittance créée')
    store.resetQuittanceDTO()
    router.push('/quittance')
  } catch (e) {
    console.error(e)
    message.error('Erreur lors de la création')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <NSpace vertical :size="24">
    <NH1>Nouvelle quittance - Récapitulatif</NH1>
    <NDescriptions label-placement="top" bordered>
      <NDescriptionsItem label="Location ID">{{ quittanceDTO.locationId }}</NDescriptionsItem>
      <NDescriptionsItem label="Date début">{{ quittanceDTO.dateDebut }}</NDescriptionsItem>
      <NDescriptionsItem label="Date fin">{{ quittanceDTO.dateFin }}</NDescriptionsItem>
      <NDescriptionsItem label="Loyer (€)">{{ quittanceDTO.montantLoyer }}</NDescriptionsItem>
      <NDescriptionsItem label="Charges (€)">{{ quittanceDTO.montantCharges }}</NDescriptionsItem>
      <NDescriptionsItem label="Total (€)">{{ total }}</NDescriptionsItem>
      <NDescriptionsItem label="Statut">{{ quittanceDTO.statut }}</NDescriptionsItem>
    </NDescriptions>
    <div class="flex justify-between pt-4">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="enregistrer" :loading="loading">Enregistrer</NButton>
    </div>
  </NSpace>
</template>

<style scoped></style>
