<script setup lang="ts">
import type { DataTableColumns } from 'naive-ui'
import type { ResultatFiscal } from '@/service/api/resultat-fiscal'

type RecetteDetail = {
  intitule: string
  proprieteNom: string
  dateRecette: string
  montant: number
}
type ChargeDetail = {
  intitule: string
  proprieteNom: string
  nature: string
  dateCharge: string
  montant: number
}
type AmortissementDetail = {
  immobilisationIntitule: string
  proprieteNom: string
  annee: number
  montantAmortissement: number
}

import { computed, onMounted, ref } from 'vue'
import { Icon } from '@iconify/vue'
import { resultatFiscalApi } from '@/service/api/resultat-fiscal'
import { NButton, NCard, NCollapse, NCollapseItem, NDataTable, NEmpty, NGi, NGrid, NSelect, NSpin, NStatistic, useMessage, NH1 } from 'naive-ui'

definePage({
  meta: {
    title: 'Résultat Fiscal',
    icon: 'material-symbols:account-balance-outline',
    order: 10,
  },
})

const message = useMessage()

// State
const loading = ref(false)
const loadingData = ref(false)
const selectedYear = ref<number | null>(new Date().getFullYear())
const selectedProprieteIds = ref<string[] | null>(null)
const proprietes = ref<any[]>([])
const resultat = ref<ResultatFiscal | null>(null)
const utilisateurId = '00000000-0000-0000-0000-000000000003' // Hardcoded for now
const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL

// Options for selects
const yearOptions = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => ({
    label: (currentYear - i).toString(),
    value: currentYear - i,
  }))
})

const proprieteOptions = computed(() =>
  proprietes.value.map(p => ({
    label: p.nom,
    value: p.id,
  }))
)

const allProprieteIds = computed(() => proprietes.value.map(p => p.id))

// Functions
async function loadProprietes() {
  loadingData.value = true
  try {
    // Assuming a generic API to fetch properties, similar to other pages
    const response = await fetch(`${baseUrl}/api/getProprietesByUtilisateur/${utilisateurId}`)
    proprietes.value = await response.json()
    // Select all properties by default
    selectedProprieteIds.value = allProprieteIds.value
  } catch (error) {
    message.error('Erreur lors du chargement des propriétés')
    console.error(error)
  } finally {
    loadingData.value = false
  }
}

async function calculerResultat() {
  if (!selectedYear.value || !selectedProprieteIds.value || selectedProprieteIds.value.length === 0) {
    message.warning('Veuillez sélectionner une année et au moins une propriété.')
    return
  }

  loading.value = true
  resultat.value = null
  try {
    resultat.value = await resultatFiscalApi.calculerResultatFiscal(
      selectedYear.value,
      selectedProprieteIds.value,
      utilisateurId,
    )
  } catch (error) {
    message.error('Erreur lors du calcul du résultat fiscal.')
    console.error(error)
  } finally {
    loading.value = false
  }
}

function formatCurrency(value?: number) {
  if (value === undefined || value === null) {
    return 'N/A'
  }
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(value)
}

function formatDate(dateString: string) {
  if (!dateString) {
    return 'N/A'
  }
  return new Date(dateString).toLocaleDateString('fr-FR')
}

// Columns for detail tables
const recetteColumns: DataTableColumns<RecetteDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Date', key: 'dateRecette', render: row => formatDate(row.dateRecette) },
  { title: 'Montant', key: 'montant', render: row => formatCurrency(row.montant), align: 'right' },
]

const chargeColumns: DataTableColumns<ChargeDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Nature', key: 'nature' },
  { title: 'Date', key: 'dateCharge', render: row => formatDate(row.dateCharge) },
  { title: 'Montant', key: 'montant', render: row => formatCurrency(row.montant), align: 'right' },
]

const amortissementColumns: DataTableColumns<AmortissementDetail> = [
  { title: 'Immobilisation', key: 'immobilisationIntitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Année', key: 'annee' },
  { title: 'Montant', key: 'montantAmortissement', render: row => formatCurrency(row.montantAmortissement), align: 'right' },
]

function getResultatClass(value?: number) {
  if (value === undefined || value === null) {
    return ''
  }
  return value > 0 ? 'result-positive' : 'result-negative'
}

onMounted(async () => {
  await loadProprietes()
  if (proprietes.value.length > 0) {
    await calculerResultat()
  }
})
</script>

<template>
  <div class="resultat-fiscal-page">
    <div class="page-header flex items-center justify-between">
      <NH1 class="titre-principal">Compte de Résultat Simplifié (2033-B)</NH1>
    </div>

    <NCard class="filters-card">
      <div class="filters">
        <NSelect
          v-model:value="selectedYear"
          :options="yearOptions"
          placeholder="Année"
          style="width: 150px"
        />
        <NSelect
          v-model:value="selectedProprieteIds"
          :options="proprieteOptions"
          placeholder="Toutes les propriétés"
          multiple
          clearable
          style="flex-grow: 1; min-width: 250px;"
        />
        <NButton type="primary" :loading="loading" @click="calculerResultat">
          <template #icon>
            <Icon icon="material-symbols:calculate-outline" />
          </template>
          Calculer
        </NButton>
      </div>
    </NCard>

    <div v-if="loading" class="loading-container">
      <NSpin size="large" />
    </div>

    <div v-if="!loading && resultat" class="result-container">
      <NCard class="recap-2033b-card">
        <table class="recap-2033b-table">
          <thead>
            <tr><th colspan="2">Compte de Résultat Simplifié (2033-B)</th></tr>
          </thead>
          <tbody>
            <tr class="section"><td colspan="2">1. Produits d'exploitation</td></tr>
            <tr>
              <td>Recettes locatives</td>
              <td class="amount">{{ formatCurrency(resultat.recettesLocatives) }}</td>
            </tr>
            <tr>
              <td>Produits accessoires</td>
              <td class="amount">N/A</td>
            </tr>
            <tr class="section"><td colspan="2">2. Charges d'exploitation</td></tr>
            <tr>
              <td>Charges externes</td>
              <td class="amount">{{ formatCurrency(resultat.chargesDeductibles) }}</td>
            </tr>
            <tr>
              <td>Impôts et taxes</td>
              <td class="amount">N/A</td>
            </tr>
            <tr>
              <td>Intérêts d’emprunt</td>
              <td class="amount">N/A</td>
            </tr>
            <tr>
              <td>Amortissements (2033-C)</td>
              <td class="amount">{{ formatCurrency(resultat.amortissements) }}</td>
            </tr>
            <tr class="section"><td colspan="2">3. Résultat d'exploitation</td></tr>
            <tr>
              <td><strong>Total produits – Total charges d’exploitation</strong></td>
              <td class="amount"><strong>{{ formatCurrency((resultat.recettesLocatives ?? 0) - (resultat.chargesDeductibles ?? 0) - (resultat.amortissements ?? 0)) }}</strong></td>
            </tr>
            <tr class="section"><td colspan="2">4. Résultat financier</td></tr>
            <tr>
              <td>Produits financiers</td>
              <td class="amount">N/A</td>
            </tr>
            <tr>
              <td>Charges financières (intérêts d’emprunt)</td>
              <td class="amount">N/A</td>
            </tr>
            <tr class="section"><td colspan="2">5. Résultat courant avant impôt</td></tr>
            <tr>
              <td><strong>Résultat d'exploitation + résultat financier</strong></td>
              <td class="amount"><strong>{{ formatCurrency((resultat.recettesLocatives ?? 0) - (resultat.chargesDeductibles ?? 0) - (resultat.amortissements ?? 0)) }}</strong></td>
            </tr>
            <tr class="section"><td colspan="2">6. Résultat exceptionnel</td></tr>
            <tr>
              <td>Produits exceptionnels</td>
              <td class="amount">N/A</td>
            </tr>
            <tr>
              <td>Charges exceptionnelles</td>
              <td class="amount">N/A</td>
            </tr>
            <tr class="section"><td colspan="2">7. Impôt sur les bénéfices</td></tr>
            <tr>
              <td>Impôt sur les bénéfices</td>
              <td class="amount">0 €</td>
            </tr>
            <tr class="section"><td colspan="2">8. Résultat net</td></tr>
            <tr>
              <td><strong>Résultat net</strong></td>
              <td class="amount"><strong>{{ formatCurrency(resultat.resultatFiscal) }}</strong></td>
            </tr>
          </tbody>
        </table>
        <div class="actions-footer">
          <NButton type="default" disabled>
            <template #icon>
              <Icon icon="material-symbols:download" />
            </template>
            Télécharger la 2033-B (bientôt disponible)
          </NButton>
        </div>
      </NCard>
      <NCollapse arrow-placement="right" class="details-collapse">
        <NCollapseItem title="Détail des recettes" :name="1">
          <NDataTable :columns="recetteColumns" :data="resultat?.recettesDetail ?? []" :bordered="false" size="small" />
        </NCollapseItem>
        <NCollapseItem title="Détail des charges" :name="2">
          <NDataTable :columns="chargeColumns" :data="resultat?.chargesDetail ?? []" :bordered="false" size="small" />
        </NCollapseItem>
        <NCollapseItem title="Détail des amortissements" :name="3">
          <NDataTable :columns="amortissementColumns" :data="resultat?.amortissementsDetail ?? []" :bordered="false" size="small" />
        </NCollapseItem>
      </NCollapse>
    </div>
    <div v-if="!loading && !resultat" class="empty-state">
      <NEmpty description="Aucun résultat à afficher. Veuillez lancer un calcul." />
    </div>
  </div>
</template>

<style scoped>
.resultat-fiscal-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
}

.filters-card {
  margin-bottom: 20px;
}

.filters {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.loading-container {
  display: flex;
  justify-content: center;
  padding: 40px;
}

.result-container {
  margin-top: 20px;
}

.recap-2033b-card {
  margin-bottom: 24px;
  overflow-x: auto;
}

.recap-2033b-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 0.5rem;
  background: #f8fafc;
  border-radius: 8px;
  overflow: hidden;
}
.recap-2033b-table th,
.recap-2033b-table td {
  padding: 8px 12px;
  border-bottom: 1px solid #e5e7eb;
  text-align: left;
}
.recap-2033b-table th {
  background: #e3eaf3;
  font-weight: bold;
  font-size: 1.1em;
}
.recap-2033b-table .section td {
  background: #f1f5f9;
  font-weight: bold;
  border-top: 2px solid #cbd5e1;
}
.recap-2033b-table .amount {
  text-align: right;
  font-variant-numeric: tabular-nums;
}

.details-collapse {
  margin-top: 20px;
}

.actions-footer {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
  text-align: center;
}

.empty-state {
  margin-top: 40px;
}

:deep(.result-positive .n-statistic-value) {
  color: #d03050; /* naive-ui error color */
}

:deep(.result-negative .n-statistic-value) {
  color: #18a058; /* naive-ui success color */
}

.titre-principal, h1, h2, h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

@media (max-width: 768px) {
  .titre-principal, h1, h2, h3 {
    font-size: 1.25rem !important;
  }
  .flex {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  .recap-2033b-table th, .recap-2033b-table td {
    padding: 6px 4px;
    font-size: 0.95em;
  }
}
</style> 