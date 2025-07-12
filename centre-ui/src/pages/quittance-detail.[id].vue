<script setup lang="ts">
import { NButton, NCard, NDataTable, NDatePicker, NForm, NFormItem, NH1, NInput, NInputNumber, NModal, NPopconfirm, NRadio, NRadioGroup, NSelect, NSpin, NTabPane, NTabs, useMessage, NH2, NEmpty, NIcon } from 'naive-ui'
import { h, nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Add24Filled, ArrowLeft24Filled, Delete24Filled, Dismiss24Filled, Edit24Filled, Info24Filled, Save24Filled, Calculator24Filled } from '@vicons/fluent'
import { createEcritureComptableQuittance } from '@/service/api/charges-recettes'

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
const quittanceId = typeof route.params.id === 'string' ? route.params.id : Array.isArray(route.params.id) ? route.params.id[0] : ''
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

const tabsWrapperRef = ref<HTMLElement | null>(null)
const activeTab = ref('detail')
const showEditModal = ref(false)
const oldStatut = ref<string | null>(null)

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
    const data = await res.json()
    paiements.value = Array.isArray(data)
      ? data.map((p: any) => ({ ...p, actions: '' }))
      : []
  } catch (e) {
    message.error('Erreur de chargement des paiements')
  } finally {
    paiementLoading.value = false
  }
}

function startEditing() {
  oldStatut.value = quittance.value.statut
  showEditModal.value = true
}

function cancelEditing() {
  showEditModal.value = false
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
    let ecritureCreee = false
    // Si le statut est PAYEE, on crée l'écriture comptable uniquement si elle n'existe pas déjà
    if (quittance.value.statut === 'PAYEE' && !quittance.value.ecritureComptableId) {
      await creerEcritureComptable()
      ecritureCreee = true
    }
    message.success(ecritureCreee ? 'Quittance modifiée et écriture comptable créée !' : 'Quittance modifiée !')
    showEditModal.value = false
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
    let url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createPaiement`
    let method = 'POST'
    if (!isNew) {
      url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePaiement`
      method = 'PUT'
    }
    const res = await fetch(url, {
      method,
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

async function creerEcritureComptable() {
  try {
    await createEcritureComptableQuittance(quittanceId)
    message.success('Écriture comptable créée avec succès !')
  } catch (e: any) {
    message.error(e.message || 'Erreur lors de la création de l\'écriture comptable')
  }
}

function centerActiveTab() {
  nextTick(() => {
    const wrapper = tabsWrapperRef.value
    if (!wrapper) return
    const activeTabEl = wrapper.querySelector('.n-tabs-tab.n-tabs-tab--active') as HTMLElement
    if (activeTabEl && wrapper) {
      const wrapperRect = wrapper.getBoundingClientRect()
      const tabRect = activeTabEl.getBoundingClientRect()
      const scrollLeft = activeTabEl.offsetLeft - (wrapperRect.width / 2) + (tabRect.width / 2)
      wrapper.scrollTo({ left: scrollLeft, behavior: 'smooth' })
    }
  })
}

watch(activeTab, () => {
  centerActiveTab()
})

onMounted(() => {
  fetchQuittance()
  fetchPaiements()
  centerActiveTab()
})

const paiementColumns = [
  { title: 'Date', key: 'datePaiement' },
  { title: 'Montant', key: 'montant' },
  { title: 'Moyen', key: 'moyenPaiement' },
  { title: 'Référence', key: 'reference' },
  { title: 'Commentaire', key: 'commentaire' },
  {
    title: 'Actions',
    key: 'actions',
    render: (row: any) =>
      h('div', { class: 'flex gap-2' }, [
        h(
          NButton,
          { size: 'small', ghost: true, onClick: () => openEditPaiementModal(row) },
          { default: () => 'Modifier' }
        ),
        h(
          NButton,
          { size: 'small', type: 'error', ghost: true, onClick: () => deletePaiement(row) },
          { default: () => 'Supprimer' }
        ),
      ]),
  },
]
</script>

<template>
  <div class="p-4">
    <NSpin :show="loading">
      <NCard v-if="quittance" :bordered="false">
        <NH1 class="titre-principal mb-4">Modifier la quittance</NH1>
        <div ref="tabsWrapperRef" class="tabs-scrollable">
          <NTabs v-model:value="activeTab" type="line" class="mt-8">
            <NTabPane name="detail" :tab="h('span', [h(NIcon, { component: Info24Filled, size: 20 }), ' Détail'])">
              <NH2 class="sous-titre mb-4">Détail de la quittance</NH2>
              <NCard class="clickable-card" @click="startEditing">
                <div class="space-y-4">
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <div class="text-sm text-gray-500">Montant loyer</div>
                      <div class="text-base">{{ quittance.montantLoyer }} €</div>
                    </div>
                    <div>
                      <div class="text-sm text-gray-500">Montant charges</div>
                      <div class="text-base">{{ quittance.montantCharges }} €</div>
                    </div>
                    <div>
                      <div class="text-sm text-gray-500">Inclure caution</div>
                      <div class="text-base">{{ quittance.inclureCaution ? 'Oui' : 'Non' }}</div>
                    </div>
                    <div v-if="quittance.inclureCaution">
                      <div class="text-sm text-gray-500">Montant caution</div>
                      <div class="text-base">{{ quittance.depotGarantie }} €</div>
                    </div>
                    <div>
                      <div class="text-sm text-gray-500">Statut</div>
                      <div class="text-base">{{ quittance.statut === 'PAYEE' ? 'Payée' : (quittance.statut === 'PARTIELLE' ? 'Partielle' : 'Impayée') }}</div>
                    </div>
                  </div>
                </div>
                <div class="edit-hint">
                  <NIcon :component="Edit24Filled" size="16" />
                  <span>Cliquez pour modifier</span>
                </div>
              </NCard>
              <div class="flex justify-between mt-8">
                <NButton ghost @click="router.push('/quittance')" title="Précédent">
                  <template #icon><NIcon :component="ArrowLeft24Filled" /></template>
                </NButton>
                <div class="flex gap-2">
                  <NPopconfirm @positive-click="supprimer" positive-text="Supprimer" negative-text="Annuler">
                    <template #trigger>
                      <NButton type="error" ghost title="Supprimer">
                        <template #icon><NIcon :component="Delete24Filled" /></template>
                      </NButton>
                    </template>
                    Confirmer la suppression ?
                  </NPopconfirm>
                </div>
              </div>
            </NTabPane>
            <NTabPane name="paiements" :tab="h('span', [h(NIcon, { component: Add24Filled, size: 20 }), ' Paiements'])">
              <div class="flex justify-between items-center mb-2">
                <NH2 class="sous-titre">Paiements associés</NH2>
                <NButton size="small" type="primary" ghost @click="openAddPaiementModal" title="Ajouter un paiement">
                  <template #icon><NIcon :component="Add24Filled" /></template>
                </NButton>
              </div>
              <div class="paiement-cards">
                <NCard
                  v-for="p in Array.isArray(paiements) ? paiements : []"
                  :key="p.id"
                  class="paiement-card"
                  :bordered="true"
                  size="medium"
                >
                  <div class="flex justify-between items-center mb-2">
                    <div class="font-bold">Date : {{ p.datePaiement }}</div>
                    <div class="flex gap-2">
                      <NButton size="small" ghost @click="() => openEditPaiementModal(p)" title="Modifier">
                        <template #icon><NIcon :component="Edit24Filled" /></template>
                      </NButton>
                      <NButton size="small" type="error" ghost @click="() => deletePaiement(p)" title="Supprimer">
                        <template #icon><NIcon :component="Delete24Filled" /></template>
                      </NButton>
                    </div>
                  </div>
                  <div class="mb-1"><span class="label">Montant :</span> {{ p.montant }} €</div>
                  <div class="mb-1"><span class="label">Moyen :</span> {{ p.moyenPaiement }}</div>
                  <div class="mb-1"><span class="label">Référence :</span> {{ p.reference }}</div>
                  <div class="mb-1"><span class="label">Commentaire :</span> {{ p.commentaire }}</div>
                </NCard>
                <NEmpty v-if="!paiements || paiements.length === 0" description="Aucun paiement pour le moment" />
              </div>
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
                    <NButton ghost @click="closePaiementModal" title="Annuler">
                      <template #icon><NIcon :component="Dismiss24Filled" /></template>
                    </NButton>
                    <NButton type="primary" @click="savePaiementModal" title="Enregistrer">
                      <template #icon><NIcon :component="Save24Filled" /></template>
                    </NButton>
                  </div>
                </NForm>
              </NModal>
            </NTabPane>
          </NTabs>
        </div>
      </NCard>

      <!-- Modal de modification des informations -->
      <NModal v-model:show="showEditModal" :mask-closable="false">
        <NCard
          style="width: 700px; max-width: 90vw;"
          title="Modifier les informations de la quittance"
          :bordered="false"
          size="huge"
          role="dialog"
          aria-modal="true"
        >
          <NForm label-placement="top">
            <NFormItem label="Montant loyer (€)">
              <NInputNumber v-model:value="quittance.montantLoyer" min="0" placeholder="0.00" size="large" inputmode="decimal" />
            </NFormItem>
            <NFormItem label="Montant charges (€)">
              <NInputNumber v-model:value="quittance.montantCharges" min="0" placeholder="0.00" size="large" inputmode="decimal" />
            </NFormItem>
            <NFormItem label="Inclure caution ?">
              <NRadioGroup v-model:value="quittance.inclureCaution">
                <NRadio :value="true">Oui</NRadio>
                <NRadio :value="false">Non</NRadio>
              </NRadioGroup>
            </NFormItem>
            <NFormItem v-if="quittance.inclureCaution" label="Montant caution (€)">
              <NInputNumber v-model:value="quittance.depotGarantie" min="0" placeholder="0.00" size="large" inputmode="decimal" />
            </NFormItem>
            <NFormItem label="Statut">
              <NSelect v-model:value="quittance.statut" :options="statutOptions" size="large" />
            </NFormItem>
          </NForm>
          <template #footer>
            <div class="flex justify-end gap-2">
              <NButton @click="cancelEditing">Annuler</NButton>
              <NButton 
                type="primary" 
                :loading="saving"
                @click="enregistrer"
              >
                Enregistrer
              </NButton>
            </div>
          </template>
        </NCard>
      </NModal>
    </NSpin>
  </div>
</template>

<style scoped>
.titre-principal, h1, h2, h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.sous-titre {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
}
.flex {
  display: flex;
}
.justify-between {
  justify-content: space-between;
  align-items: center;
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
.paiement-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
.paiement-card {
  min-width: 0;
  width: 100%;
  box-sizing: border-box;
}
.label {
  font-weight: 600;
  color: var(--n-text-color);
  margin-right: 4px;
}
@media (max-width: 768px) {
  .titre-principal, h1, h2, h3 {
    font-size: 1.25rem !important;
  }
  .sous-titre {
    font-size: 1rem;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mt-8, .mb-4 {
    margin-top: 1rem !important;
    margin-bottom: 1rem !important;
  }
  .paiement-cards {
    grid-template-columns: 1fr;
    gap: 12px;
  }
}
.tab-label-hover {
  display: none;
  margin-left: 6px;
  vertical-align: middle;
  font-size: 15px;
  font-weight: 500;
}
.n-tabs-tab:hover .tab-label-hover {
  display: inline;
}

.clickable-card {
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.clickable-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: var(--n-color-primary);
}

.edit-hint {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.85rem;
  color: var(--n-text-color-disabled);
  opacity: 0.7;
  transition: opacity 0.2s;
}

.clickable-card:hover .edit-hint {
  opacity: 1;
  color: var(--n-color-primary);
}

.tabs-scrollable {
  overflow-x: auto;
  scrollbar-width: thin;
  -webkit-overflow-scrolling: touch;
  margin-bottom: 16px;
}
.tabs-scrollable .n-tabs {
  min-width: 400px;
}
.n-tabs-tab {
  min-width: unset;
  max-width: unset;
  justify-content: flex-start;
  text-align: left;
  padding: 0 16px !important;
  position: relative;
}
.n-tabs-tab .n-icon {
  margin: 0 8px 0 0;
  display: inline-block;
}
</style> 