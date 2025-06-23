<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NCard, NForm, NFormItem, NInputNumber, NSelect, NRadioGroup, NRadio, NButton, NPopconfirm, useMessage, NSpin, NH1 } from 'naive-ui'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const quittanceId = route.params.id as string
const loading = ref(false)
const saving = ref(false)
const quittance = ref<any>(null)

const statutOptions = [
  { label: 'Payée', value: 'PAYEE' },
  { label: 'Partielle', value: 'PARTIELLE' },
  { label: 'Impayée', value: 'IMPAYEE' },
]

async function fetchQuittance() {
  loading.value = true
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getQuittanceById/${quittanceId}`)
    const data = await res.json()
    // Conversion explicite pour NInputNumber
    data.montantLoyer = data.montantLoyer !== undefined && data.montantLoyer !== null ? Number(data.montantLoyer) : 0
    data.montantCharges = data.montantCharges !== undefined && data.montantCharges !== null ? Number(data.montantCharges) : 0
    data.depotGarantie = data.depotGarantie !== undefined && data.depotGarantie !== null ? Number(data.depotGarantie) : 0
    quittance.value = data
  } catch (e) {
    message.error('Erreur de chargement de la quittance')
  } finally {
    loading.value = false
  }
}

async function enregistrer() {
  saving.value = true
  try {
    const payload = { ...quittance.value }
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updateQuittance`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!res.ok) throw new Error('Erreur serveur')
    message.success('Quittance modifiée !')
    router.push('/quittance')
  } catch (e: any) {
    message.error(e.message || 'Erreur inconnue')
  } finally {
    saving.value = false
  }
}

async function supprimer() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteQuittance/${quittanceId}`, { method: 'DELETE' })
    if (!res.ok) throw new Error('Erreur lors de la suppression')
    message.success('Quittance supprimée')
    router.push('/quittance')
  } catch (e) {
    message.error('Erreur lors de la suppression')
  }
}

onMounted(fetchQuittance)
</script>

<template>
  <div class="p-4">
    <NSpin :show="loading">
      <NCard v-if="quittance" :bordered="false">
        <NH1 class="mb-4">Modifier la quittance</NH1>
        <NForm label-placement="top">
          <NFormItem label="Montant loyer (€)">
            <NInputNumber v-model:value="quittance.montantLoyer" min="0" placeholder="0.00" size="large" />
          </NFormItem>
          <NFormItem label="Montant charges (€)">
            <NInputNumber v-model:value="quittance.montantCharges" min="0" placeholder="0.00" size="large" />
          </NFormItem>
          <NFormItem label="Inclure caution ?">
            <NRadioGroup v-model:value="quittance.inclureCaution">
              <NRadio :value="true">Oui</NRadio>
              <NRadio :value="false">Non</NRadio>
            </NRadioGroup>
          </NFormItem>
          <NFormItem v-if="quittance.inclureCaution" label="Montant caution (€)">
            <NInputNumber v-model:value="quittance.depotGarantie" min="0" placeholder="0.00" size="large" />
          </NFormItem>
          <NFormItem label="Statut">
            <NSelect v-model:value="quittance.statut" :options="statutOptions" size="large" />
          </NFormItem>
        </NForm>
        <div class="flex justify-between mt-8">
          <NButton @click="router.push('/quittance')" ghost>Précédent</NButton>
          <div class="flex gap-2">
            <NPopconfirm @positive-click="supprimer" positive-text="Supprimer" negative-text="Annuler">
              <template #trigger>
                <NButton type="error" ghost>Supprimer</NButton>
              </template>
              Confirmer la suppression ?
            </NPopconfirm>
            <NButton type="primary" :loading="saving" @click="enregistrer">Enregistrer</NButton>
          </div>
        </div>
      </NCard>
    </NSpin>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
}
.justify-between {
  justify-content: space-between;
}
.gap-2 {
  gap: 0.5rem;
}
.mt-8 {
  margin-top: 2rem;
}
.mb-4 {
  margin-bottom: 1rem;
}
</style> 