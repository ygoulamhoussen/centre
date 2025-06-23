<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { NButton, NCard, NDataTable, NDatePicker, NForm, NFormItem, NH1, NInput, NInputNumber, NModal, NPopconfirm, NRadio, NRadioGroup, NSelect, NSpin, NTabPane, NTabs, useMessage } from 'naive-ui'
import { useRoute, useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Ajouter une quittance - Étape 1',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})

const route = useRoute()
const router = useRouter()
const message = useMessage()
const quittanceId = route.params.id as string
const loading = ref(false)
const saving = ref(false)
const quittance = ref<any>(null)
const paiements = ref<any[]>([])
const paiementLoading = ref(false)
const paiementModalVisible = ref(false)
const paiementModalEdit = ref(false)
const paiementForm = ref<{
  id: string | null
  datePaiement: number | null
  montant: number | null
  moyenPaiement: string
  reference: string
  commentaire: string
}>({
  id: null,
  datePaiement: null,
  montant: null,
  moyenPaiement: '',
  reference: '',
  commentaire: '',
})
const moyenPaiementOptions = [
  { label: 'Carte bancaire', value: 'CARTE' },
  { label: 'Virement', value: 'VIREMENT' },
  { label: 'Chèque', value: 'CHEQUE' },
  { label: 'Espèces', value: 'ESPECES' },
  { label: 'Autre', value: 'AUTRE' },
]

const statutOptions = [
  { label: 'Payée', value: 'PAYEE' },
  { label: 'Partielle', value: 'PARTIELLE' },
  { label: 'Impayée', value: 'IMPAYEE' },
]

const showAddPaiement = ref(false)

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

async function fetchPaiements() {
  paiementLoading.value = true
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getPaiementsByQuittance/${quittanceId}`)
    paiements.value = await res.json()
  } catch (e) {
    message.error('Erreur de chargement des paiements')
  } finally {
    paiementLoading.value = false
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

function openAddPaiementModal() {
  paiementModalEdit.value = false
  paiementForm.value = {
    id: null,
    datePaiement: null,
    montant: null,
    moyenPaiement: '',
    reference: '',
    commentaire: '',
  }
  paiementModalVisible.value = true
}

function openEditPaiementModal(row: any) {
  paiementModalEdit.value = true
  paiementForm.value = {
    ...row,
    datePaiement: row.datePaiement && row.datePaiement !== '' ? Date.parse(row.datePaiement) : null,
  }
  paiementModalVisible.value = true
}

function closePaiementModal() {
  paiementModalVisible.value = false
}

async function savePaiementModal() {
  try {
    const isNew = !paiementForm.value.id
    const dateStr = paiementForm.value.datePaiement ? new Date(paiementForm.value.datePaiement).toISOString().slice(0, 10) : null
    const payload = {
      ...paiementForm.value,
      quittanceId,
      montant: paiementForm.value.montant?.toString() || '0',
      datePaiement: dateStr,
    }
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/${isNew ? 'createPaiement' : 'updatePaiement'}`
    const res = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!res.ok) throw new Error('Erreur serveur')
    message.success(isNew ? 'Paiement ajouté' : 'Paiement modifié')
    paiementModalVisible.value = false
    fetchPaiements()
  } catch (e: any) {
    message.error(e.message || 'Erreur inconnue')
  }
}

async function deletePaiement(row: any) {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deletePaiement/${row.id}`, { method: 'DELETE' })
    if (!res.ok) throw new Error('Erreur lors de la suppression')
    message.success('Paiement supprimé')
    fetchPaiements()
  } catch (e) {
    message.error('Erreur lors de la suppression')
  }
}

onMounted(() => {
  fetchQuittance()
  fetchPaiements()
})

const paiementColumns = [
  { title: 'Date', key: 'datePaiement' },
  { title: 'Montant', key: 'montant' },
  { title: 'Moyen', key: 'moyenPaiement' },
  { title: 'Référence', key: 'reference' },
  { title: 'Commentaire', key: 'commentaire' },
  { title: 'Actions', key: 'actions' },
]

function getPaiementTableData() {
  // Affiche la ligne d'ajout seulement si showAddPaiement est true
  return showAddPaiement.value
    ? [{ ...newPaiement.value }, ...(Array.isArray(paiements.value) ? paiements.value.map((p: any) => ({ ...p })) : [])]
    : (Array.isArray(paiements.value) ? paiements.value.map((p: any) => ({ ...p })) : [])
}
</script>

<template>
  <div class="p-4">
    <NSpin :show="loading">
      <NCard v-if="quittance" :bordered="false">
        <NH1 class="mb-4">Modifier la quittance</NH1>
        <NTabs type="line" class="mt-8">
          <NTabPane name="detail" tab="Détail">
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
          </NTabPane>
          <NTabPane name="paiements" tab="Paiements">
            <div class="flex justify-end mb-2">
              <NButton size="small" type="primary" ghost @click="openAddPaiementModal">Ajouter un paiement</NButton>
            </div>
            <NDataTable :columns="paiementColumns" :data="Array.isArray(paiements) ? paiements : []" :loading="paiementLoading">
              <template #cell-actions="{ row }">
                <NButton size="small" ghost @click="() => openEditPaiementModal(row)">Modifier</NButton>
                <NButton size="small" type="error" ghost @click="() => deletePaiement(row)">Supprimer</NButton>
              </template>
            </NDataTable>
            <NModal v-model:show="paiementModalVisible" preset="dialog" title="Paiement" style="width: 400px">
              <NForm label-placement="top">
                <NFormItem label="Date">
                  <NDatePicker v-model:value="paiementForm.datePaiement" value-format="yyyy-MM-dd" type="date" style="width: 100%" />
                </NFormItem>
                <NFormItem label="Montant (€)">
                  <NInputNumber v-model:value="paiementForm.montant" min="0" style="width: 100%" />
                </NFormItem>
                <NFormItem label="Moyen">
                  <NSelect v-model:value="paiementForm.moyenPaiement" :options="moyenPaiementOptions" style="width: 100%" />
                </NFormItem>
                <NFormItem label="Référence">
                  <NInput v-model:value="paiementForm.reference" style="width: 100%" />
                </NFormItem>
                <NFormItem label="Commentaire">
                  <NInput v-model:value="paiementForm.commentaire" style="width: 100%" />
                </NFormItem>
                <div class="flex justify-end gap-2 mt-4">
                  <NButton ghost @click="closePaiementModal">Annuler</NButton>
                  <NButton type="primary" @click="savePaiementModal">Enregistrer</NButton>
                </div>
              </NForm>
            </NModal>
          </NTabPane>
        </NTabs>
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