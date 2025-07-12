<script setup lang="ts">
import type { DataTableColumns } from 'naive-ui'

type ImmobilisationDetail = {
  intitule: string
  proprieteNom: string
  montantBrut: number
  amortissementsCumules: number
  valeurNette: number
  typeImmobilisation: string
}

type EmpruntDetail = {
  intitule: string
  proprieteNom: string
  capitalRestantDu: number
  banque: string
  dateFin: string
}

type TresorerieDetail = {
  compte: string
  solde: number
  dateSolde: string
}

import { computed, onMounted, ref } from 'vue'
import { Icon } from '@iconify/vue'
import { bilanSimplifieApi } from '@/service/api/bilan-simplifie'
import { NButton, NCard, NCollapse, NCollapseItem, NDataTable, NEmpty, NSelect, NSpin, useMessage, NH1 } from 'naive-ui'

definePage({
  meta: {
    title: 'Bilan Simplifié',
    icon: 'material-symbols:account-balance',
    order: 11,
  },
})

const message = useMessage()

// State
const loading = ref(false)
const selectedYear = ref<number | null>(new Date().getFullYear())
const bilan = ref<any>(null)
const utilisateurId = '00000000-0000-0000-0000-000000000003' // Hardcoded for now

// Options for selects
const yearOptions = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => ({
    label: (currentYear - i).toString(),
    value: currentYear - i,
  }))
})

// Computed values for bilan
const totalImmobilisationsBrutes = computed(() => {
  if (!bilan.value?.immobilisationsDetail) return 0
  return bilan.value.immobilisationsDetail.reduce((sum: number, immo: any) => sum + immo.montantBrut, 0)
})

const totalAmortissementsCumules = computed(() => {
  if (!bilan.value?.immobilisationsDetail) return 0
  return bilan.value.immobilisationsDetail.reduce((sum: number, immo: any) => sum + immo.amortissementsCumules, 0)
})

const totalImmobilisationsNettes = computed(() => {
  return totalImmobilisationsBrutes.value - totalAmortissementsCumules.value
})

const totalEmprunts = computed(() => {
  if (!bilan.value?.empruntsDetail) return 0
  return bilan.value.empruntsDetail.reduce((sum: number, emprunt: any) => sum + emprunt.capitalRestantDu, 0)
})

const totalTresorerie = computed(() => {
  if (!bilan.value?.tresorerieDetail) return 0
  return bilan.value.tresorerieDetail.reduce((sum: number, treso: any) => sum + treso.solde, 0)
})

const totalActif = computed(() => {
  return totalImmobilisationsNettes.value + (bilan.value?.creancesClients ?? 0) + totalTresorerie.value
})

const totalPassif = computed(() => {
  return (bilan.value?.capital ?? 0) + (bilan.value?.resultatExercice ?? 0) + totalEmprunts.value + (bilan.value?.dettesFournisseurs ?? 0)
})

// Functions
async function calculerBilan() {
  if (!selectedYear.value) {
    message.warning('Veuillez sélectionner une année.')
    return
  }

  loading.value = true
  bilan.value = null
  try {
    bilan.value = await bilanSimplifieApi.calculerBilanSimplifie(
      selectedYear.value,
      utilisateurId,
    )
  } catch (error) {
    message.error('Erreur lors du calcul du bilan.')
    console.error(error)
  } finally {
    loading.value = false
  }
}

function formatCurrency(value?: number) {
  if (value === undefined || value === null) {
    return '0,00 €'
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
const immobilisationColumns: DataTableColumns<ImmobilisationDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Type', key: 'typeImmobilisation' },
  { title: 'Valeur brute', key: 'montantBrut', render: row => formatCurrency(row.montantBrut), align: 'right' },
  { title: 'Amortissements cumulés', key: 'amortissementsCumules', render: row => formatCurrency(row.amortissementsCumules), align: 'right' },
  { title: 'Valeur nette', key: 'valeurNette', render: row => formatCurrency(row.valeurNette), align: 'right' },
]

const empruntColumns: DataTableColumns<EmpruntDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Banque', key: 'banque' },
  { title: 'Capital restant dû', key: 'capitalRestantDu', render: row => formatCurrency(row.capitalRestantDu), align: 'right' },
  { title: 'Échéance', key: 'dateFin', render: row => formatDate(row.dateFin) },
]

const tresorerieColumns: DataTableColumns<TresorerieDetail> = [
  { title: 'Compte', key: 'compte' },
  { title: 'Solde', key: 'solde', render: row => formatCurrency(row.solde), align: 'right' },
  { title: 'Date du solde', key: 'dateSolde', render: row => formatDate(row.dateSolde) },
]

onMounted(async () => {
  // Lancer le calcul automatiquement à l'arrivée sur la page
  await calculerBilan()
})
</script>

<template>
  <div class="bilan-simplifie-page">
    <div class="page-header flex items-center justify-between">
      <NH1 class="titre-principal">Bilan Simplifié LMNP (2033-A)</NH1>
    </div>

    <NCard class="filters-card">
      <div class="filters">
        <NSelect
          v-model:value="selectedYear"
          :options="yearOptions"
          placeholder="Année"
          style="width: 150px"
          @update:value="calculerBilan"
        />
        <NButton type="primary" :loading="loading" @click="calculerBilan">
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

    <div v-if="!loading && bilan" class="result-container">
      <NCard class="bilan-card">
        <table class="bilan-table">
          <thead>
            <tr><th colspan="2">Bilan Simplifié LMNP (2033-A) au {{ formatDate(selectedYear + '-12-31') }}</th></tr>
          </thead>
          <tbody>
            <!-- ACTIF -->
            <tr class="section"><td colspan="2">ACTIF (ce que vous possédez)</td></tr>
            
            <tr class="subsection"><td colspan="2">Immobilisations</td></tr>
            <tr>
              <td>Immobilisations corporelles (213000, 215000)</td>
              <td class="amount">{{ formatCurrency(totalImmobilisationsBrutes) }}</td>
            </tr>
            <tr>
              <td>Amortissements cumulés (280000)</td>
              <td class="amount negative">{{ formatCurrency(-totalAmortissementsCumules) }}</td>
            </tr>
            <tr class="total-row">
              <td><strong>Immobilisations nettes</strong></td>
              <td class="amount"><strong>{{ formatCurrency(totalImmobilisationsNettes) }}</strong></td>
            </tr>
            
            <tr class="subsection"><td colspan="2">Actif circulant</td></tr>
            <tr>
              <td>Créances clients (411000)</td>
              <td class="amount">{{ formatCurrency(bilan.creancesClients) }}</td>
            </tr>
            <tr>
              <td>Disponibilités (512000, 530000)</td>
              <td class="amount">{{ formatCurrency(totalTresorerie) }}</td>
            </tr>
            
            <tr class="final-total">
              <td><strong>TOTAL ACTIF</strong></td>
              <td class="amount"><strong>{{ formatCurrency(totalActif) }}</strong></td>
            </tr>
            
            <!-- PASSIF -->
            <tr class="section"><td colspan="2">PASSIF (ce que vous devez)</td></tr>
            
            <tr class="subsection"><td colspan="2">Capitaux propres</td></tr>
            <tr>
              <td>Capital personnel (101000)</td>
              <td class="amount">{{ formatCurrency(bilan.capital) }}</td>
            </tr>
            <tr>
              <td>Résultat de l'exercice (120000)</td>
              <td class="amount">{{ formatCurrency(bilan.resultatExercice) }}</td>
            </tr>
            
            <tr class="subsection"><td colspan="2">Dettes</td></tr>
            <tr>
              <td>Emprunts bancaires (164000)</td>
              <td class="amount">{{ formatCurrency(totalEmprunts) }}</td>
            </tr>
            <tr>
              <td>Dettes fournisseurs (401000)</td>
              <td class="amount">{{ formatCurrency(bilan.dettesFournisseurs) }}</td>
            </tr>
            
            <tr class="final-total">
              <td><strong>TOTAL PASSIF</strong></td>
              <td class="amount"><strong>{{ formatCurrency(totalPassif) }}</strong></td>
            </tr>
          </tbody>
        </table>
        
        <div class="equilibre-info" v-if="Math.abs(totalActif - totalPassif) < 0.01">
          <Icon icon="material-symbols:check-circle" class="equilibre-icon" />
          <span>Le bilan est équilibré (Actif = Passif)</span>
        </div>
        <div class="equilibre-info error" v-else>
          <Icon icon="material-symbols:error" class="equilibre-icon" />
          <span>Le bilan n'est pas équilibré (différence: {{ formatCurrency(totalActif - totalPassif) }})</span>
        </div>
        
        <div class="actions-footer">
          <NButton type="default" disabled>
            <template #icon>
              <Icon icon="material-symbols:download" />
            </template>
            Télécharger le bilan (bientôt disponible)
          </NButton>
        </div>
      </NCard>
      
      <NCollapse arrow-placement="right" class="details-collapse">
        <NCollapseItem title="Détail des immobilisations" :name="1">
          <NDataTable :columns="immobilisationColumns" :data="bilan?.immobilisationsDetail ?? []" :bordered="false" size="small" />
        </NCollapseItem>
        <NCollapseItem title="Détail des emprunts" :name="2">
          <NDataTable :columns="empruntColumns" :data="bilan?.empruntsDetail ?? []" :bordered="false" size="small" />
        </NCollapseItem>
        <NCollapseItem title="Détail de la trésorerie" :name="3">
          <NDataTable :columns="tresorerieColumns" :data="bilan?.tresorerieDetail ?? []" :bordered="false" size="small" />
        </NCollapseItem>
      </NCollapse>
    </div>
    
    <div v-if="!loading && !bilan" class="empty-state">
      <NEmpty description="Aucun bilan à afficher. Veuillez lancer un calcul." />
    </div>
  </div>
</template>

<style scoped>
.bilan-simplifie-page {
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

.bilan-card {
  margin-bottom: 24px;
  overflow-x: auto;
}

.bilan-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 0.5rem;
  background: #f8fafc;
  border-radius: 8px;
  overflow: hidden;
}

.bilan-table th,
.bilan-table td {
  padding: 8px 12px;
  border-bottom: 1px solid #e5e7eb;
  text-align: left;
}

.bilan-table th {
  background: #e3eaf3;
  font-weight: bold;
  font-size: 1.1em;
}

.bilan-table .section td {
  background: #1e40af;
  color: white;
  font-weight: bold;
  border-top: 2px solid #1e3a8a;
}

.bilan-table .subsection td {
  background: #3b82f6;
  color: white;
  font-weight: bold;
  border-top: 1px solid #2563eb;
}

.bilan-table .amount {
  text-align: right;
  font-variant-numeric: tabular-nums;
}

.bilan-table .amount.negative {
  color: #dc2626;
}

.bilan-table .total-row {
  background: #f8f9fa;
  border-top: 1px solid #dee2e6;
}

.bilan-table .final-total {
  background: #d4edda;
  border-top: 2px solid #28a745;
  font-weight: bold;
  font-size: 1.1em;
}

.equilibre-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 16px;
  padding: 12px;
  background: #d4edda;
  border: 1px solid #c3e6cb;
  border-radius: 6px;
  color: #155724;
}

.equilibre-info.error {
  background: #f8d7da;
  border-color: #f5c6cb;
  color: #721c24;
}

.equilibre-icon {
  font-size: 20px;
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
  
  .bilan-table th, .bilan-table td {
    padding: 6px 4px;
    font-size: 0.95em;
  }
}
</style> 