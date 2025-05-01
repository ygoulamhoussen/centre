<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { NButton, NCard, NGi, NGrid, NH1, NPopconfirm, NSpace, useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const message = useMessage()
const quittances = ref<any[]>([])
const authStore = useAuthStore()
const userId = authStore.userInfo.userId

definePage({
  meta: {
    title: 'Mes quittances',
    icon: 'mdi:account-group',
    order: 5,
  },
})

async function fetchQuittances() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getQuittancesByUtilisateur/${userId}`)
    quittances.value = await res.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement des quittances')
  }
}

async function supprimerQuittance(id: string) {
  try {
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteQuittance/${id}`, { method: 'DELETE' })
    message.success('Quittance supprimée')
    fetchQuittances()
  }
 catch (e) {
    console.error(e)
    message.error('Erreur lors de la suppression')
  }
}

function demarrerCreation() {
  router.push('/quittance-etape-1')
}

async function telechargerQuittance(id: string) {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/generateQuittancePdf/${id}`, {
      method: 'GET'
    })
    const blob = await res.blob()
    const url = window.URL.createObjectURL(blob)

    const link = document.createElement('a')
    link.href = url
    link.download = `quittance-${id}.pdf`
    link.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    console.error(e)
    message.error("Erreur lors du téléchargement du PDF")
  }
}


onMounted(() => fetchQuittances())
</script>

<template>
  <NSpace vertical :size="24">
    <div class="flex justify-between items-center">
      <NH1>Mes quittances</NH1>
      <NButton type="primary" @click="demarrerCreation">Créer une quittance</NButton>
    </div>

    <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16" v-if="quittances.length">
      <NGi v-for="q in quittances" :key="q.id">
        <NCard :title="`Quittance ${q.id}`">
          <p><strong>Période :</strong> {{ q.dateDebut }} - {{ q.dateFin }}</p>
          <p><strong>Loyer :</strong> {{ q.montantLoyer }} €</p>
          <p><strong>Charges :</strong> {{ q.montantCharges }} €</p>
          <p><strong>Total :</strong> {{ q.montantTotal }} €</p>
          <template #footer>
            <div class="flex justify-end">
              <NPopconfirm
                @positive-click="() => supprimerQuittance(q.id)"
                positive-text="Supprimer"
                negative-text="Annuler"
              >
                <template #trigger>
                  <NButton size="small" type="error" ghost>Supprimer</NButton>
                </template>
                Confirmer la suppression ?
              </NPopconfirm>
            </div>
            <div >
            <NButton size="small" type="primary" ghost @click="telechargerQuittance(q.id)">
  Télécharger
</NButton>
</div>
          </template>
        </NCard>
      </NGi>
    </NGrid>

    <NEmpty description="Aucune quittance enregistrée." v-else />
  </NSpace>
</template>

<style scoped></style>
