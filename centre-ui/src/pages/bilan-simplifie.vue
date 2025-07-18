<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { NButton, NCard, NCollapse, NCollapseItem, NDataTable, NEmpty, NSelect, NSpin, useMessage, NH1, type DataTableColumns } from 'naive-ui'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/store/modules/auth'
import { bilanSimplifieApi } from '@/service/api/bilan-simplifie'
import { getEcrituresComptablesWithProprieteNom } from '@/service/api/charges-recettes'

// --- CALCUL DU BILAN À PARTIR DU JOURNAL COMPTABLE (FEC) ---

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

const lignesFEC = ref<any[]>([])

async function chargerEcritures() {
  try {
    const data = await getEcrituresComptablesWithProprieteNom(utilisateurId)
    const lignes: any[] = []
    for (const e of data as any[]) {
      if (Array.isArray(e.lignes)) {
        for (const l of e.lignes) {
          lignes.push({
            ...l,
            dateEcriture: e.dateEcriture,
            compteNum: l.compteNum,
            debit: l.debit,
            credit: l.credit,
          })
        }
      }
    }
    lignesFEC.value = lignes
  } catch (error) {
    message.error('Erreur lors du chargement des écritures')
  }
}

onMounted(async () => {
  await chargerEcritures()
  // Log diagnostic : somme dotations 681100 année sélectionnée
  const dotations = lignesFEC.value
    .filter(l => l.compteNum === '681100' && l.dateEcriture && new Date(l.dateEcriture).getFullYear() === selectedYear.value)
    .reduce((sum, l) => sum + (Number(l.debit) || 0), 0)
  console.log('Somme dotations amortissement (681100) année', selectedYear.value, ':', dotations)
  // Log diagnostic : cumul crédits 281xx jusqu'au 31/12 année sélectionnée
  const comptesAmortissements = ['28161', '28185']
  const cumulAmortissements = lignesFEC.value
    .filter(l => comptesAmortissements.includes(l.compteNum) && l.dateEcriture && new Date(l.dateEcriture) <= new Date(`${selectedYear.value}-12-31`))
    .reduce((sum, l) => sum + (Number(l.credit) || 0), 0)
  console.log('Cumul crédits amortissements (281xx) jusqu\'au 31/12/', selectedYear.value, ':', cumulAmortissements)
})

// Comptes principaux (à adapter selon ton plan comptable)
// Terrains (211000) ~0-20%, Immeubles (213100) ~40-60%, Travaux (213500) ~10-20%, Frais (203000) ~5-10%, Mobilier (218100) ~5-10%
const comptesImmobilisations = ['211000', '213100', '213500', '203000', '218100']
const comptesAmortissements = ['28161', '28185']
const comptesEmprunts = ['164000']
const comptesTresorerie = ['512000', '530000']
const comptesCreances = ['411000']
const comptesDettesFournisseurs = ['401000']
const comptesCapital = ['101000']
const comptesResultat = ['120000']

const selectedYear = ref<number | null>(new Date().getFullYear())

const lignesFECFiltrees = computed(() => {
  if (!selectedYear.value) return lignesFEC.value
  return lignesFEC.value.filter(l => l.dateEcriture && new Date(l.dateEcriture).getFullYear() === selectedYear.value)
})

function sumFEC(comptes: string[], sens: 'debit'|'credit' = 'debit') {
  return lignesFECFiltrees.value
    .filter(l => comptes.includes(l.compteNum))
    .reduce((sum, l) => sum + (Number(l[sens]) || 0), 0)
}

function soldeCumuleComptesFiltre(filtreCompte: (compte: string) => boolean, annee: number | null) {
  if (!annee) return 0
  return lignesFEC.value
    .filter(l => filtreCompte(l.compteNum) && l.dateEcriture && new Date(l.dateEcriture) <= new Date(`${annee}-12-31`))
    .reduce((sum, l) => sum + (Number(l.debit) || 0) - (Number(l.credit) || 0), 0)
}

const totalImmobilisationsBrutes = computed(() => soldeCumuleComptesFiltre(
  compte => comptesImmobilisations.includes(compte),
  selectedYear.value
))
const totalAmortissementsCumules = computed(() => {
  return -soldeCumuleComptesFiltre(
    compte => compte.startsWith('28'),
    selectedYear.value
  )
}) // amortissements = crédit
const totalImmobilisationsNettes = computed(() => totalImmobilisationsBrutes.value - totalAmortissementsCumules.value)
const totalEmprunts = computed(() => sumFEC(comptesEmprunts, 'credit'))
const totalTresorerie = computed(() =>
  lignesFEC.value
    .filter(l =>
      comptesTresorerie.includes(l.compteNum) &&
      l.dateEcriture &&
      new Date(l.dateEcriture) <= new Date(`${selectedYear.value}-12-31`)
    )
    .reduce((sum, l) => sum + (Number(l.debit) || 0) - (Number(l.credit) || 0), 0)
)
const totalCreances = computed(() => sumFEC(comptesCreances, 'debit'))
const totalDettesFournisseurs = computed(() => sumFEC(comptesDettesFournisseurs, 'credit'))
const totalCapital = computed(() => sumFEC(comptesCapital, 'credit'))
const totalResultat = computed(() => sumFEC(comptesResultat, 'credit'))

const totalActif = computed(() => totalImmobilisationsNettes.value + totalCreances.value + totalTresorerie.value)
// Comptes produits/charges pour le calcul dynamique du résultat
const comptesProduits = ['706000', '708000', '758000']
const comptesCharges = ['606000', '606100', '606200', '606300', '606400', '606800', '615000', '614000', '622000', '623000', '626000', '627000', '635100', '635800', '637000', '661100', '681100']

const totalProduits = computed(() => sumFEC(comptesProduits, 'credit'))
const totalCharges = computed(() => sumFEC(comptesCharges, 'debit'))
const resultatExercice = computed(() => totalProduits.value - totalCharges.value)

const totalPassif = computed(() => totalCapital.value + resultatExercice.value + totalEmprunts.value + totalDettesFournisseurs.value)

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
  { title: 'Valeur brute', key: 'montantBrut', render: (row: ImmobilisationDetail) => formatCurrency(row.montantBrut), align: 'right' },
  { title: 'Amortissements cumulés', key: 'amortissementsCumules', render: (row: ImmobilisationDetail) => formatCurrency(row.amortissementsCumules), align: 'right' },
  { title: 'Valeur nette', key: 'valeurNette', render: (row: ImmobilisationDetail) => formatCurrency(row.valeurNette), align: 'right' },
]

const empruntColumns: DataTableColumns<EmpruntDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Banque', key: 'banque' },
  { title: 'Capital restant dû', key: 'capitalRestantDu', render: (row: EmpruntDetail) => formatCurrency(row.capitalRestantDu), align: 'right' },
  { title: 'Échéance', key: 'dateFin', render: (row: EmpruntDetail) => formatDate(row.dateFin) },
]

const tresorerieColumns: DataTableColumns<TresorerieDetail> = [
  { title: 'Compte', key: 'compte' },
  { title: 'Solde', key: 'solde', render: (row: TresorerieDetail) => formatCurrency(row.solde), align: 'right' },
  { title: 'Date du solde', key: 'dateSolde', render: (row: TresorerieDetail) => formatDate(row.dateSolde) },
]

// Détection mobile
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
if (typeof window !== 'undefined') {
  window.addEventListener('resize', handleResize)
}

const message = useMessage()
const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId
const loading = ref(false)

// Options pour le select d'années (calculées à partir du FEC)
const anneesDisponibles = computed(() => {
  const allYears = lignesFEC.value.map(l => l.dateEcriture ? new Date(l.dateEcriture).getFullYear() : null).filter((a): a is number => a !== null)
  return Array.from(new Set(allYears)).sort((a, b) => b - a)
})

onMounted(async () => {
  // Lancer le calcul automatiquement à l'arrivée sur la page
})

// Ajout des sous-totaux par type d'immobilisation
const totalTerrains = computed(() => soldeCumuleComptesFiltre(
  compte => compte === '211000',
  selectedYear.value
))
const totalImmeubles = computed(() => soldeCumuleComptesFiltre(
  compte => compte === '213100',
  selectedYear.value
))
const totalTravaux = computed(() => soldeCumuleComptesFiltre(
  compte => compte === '213500',
  selectedYear.value
))
const totalFrais = computed(() => soldeCumuleComptesFiltre(
  compte => compte === '203000',
  selectedYear.value
))
const totalMobilier = computed(() => soldeCumuleComptesFiltre(
  compte => compte === '218100',
  selectedYear.value
))
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
          :options="anneesDisponibles.map(a => ({ label: a ? a.toString() : '', value: a }))"
          placeholder="Année"
          style="width: 150px"
        />
        <NButton type="primary" :loading="loading" @click="chargerEcritures">
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

    <div v-if="!loading" class="result-container">
      <NCard class="bilan-card">
        <table class="bilan-table">
          <thead>
            <tr><th colspan="2">Bilan Simplifié LMNP (2033-A) au {{ formatDate(selectedYear + '-12-31') }}</th></tr>
          </thead>
          <tbody>
            <!-- ACTIF -->
            <tr class="section"><td colspan="2">ACTIF (ce que vous possédez)</td></tr>
            
            <tr class="subsection"><td colspan="2">Immobilisations</td></tr>
            <tr><td>Terrains (211000) ~0-20%</td><td class="amount">{{ formatCurrency(totalTerrains) }}</td></tr>
            <tr><td>Immeubles (213100) ~40-60%</td><td class="amount">{{ formatCurrency(totalImmeubles) }}</td></tr>
            <tr><td>Travaux/Agencements (213500) ~10-20%</td><td class="amount">{{ formatCurrency(totalTravaux) }}</td></tr>
            <tr><td>Frais d'acquisition (203000) ~5-10%</td><td class="amount">{{ formatCurrency(totalFrais) }}</td></tr>
            <tr><td>Mobilier (218100) ~5-10%</td><td class="amount">{{ formatCurrency(totalMobilier) }}</td></tr>
            <tr>
              <td>Amortissements cumulés (28xxxx)</td>
              <td class="amount negative">{{ formatCurrency(-totalAmortissementsCumules) }}</td>
            </tr>
            <tr class="total-row">
              <td><strong>Immobilisations nettes</strong></td>
              <td class="amount"><strong>{{ formatCurrency(totalImmobilisationsNettes) }}</strong></td>
            </tr>
            
            <tr class="subsection"><td colspan="2">Actif circulant</td></tr>
            <tr>
              <td>Créances clients (411000)</td>
               <td class="amount">{{ formatCurrency(totalCreances) }}</td>
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
               <td class="amount">{{ formatCurrency(totalCapital) }}</td>
            </tr>
            <tr>
              <td>Résultat de l'exercice</td>
              <td class="amount">{{ formatCurrency(resultatExercice) }}</td>
            </tr>
            
            <tr class="subsection"><td colspan="2">Dettes</td></tr>
            <tr>
              <td>Emprunts bancaires (164000)</td>
              <td class="amount">{{ formatCurrency(totalEmprunts) }}</td>
            </tr>
            <tr>
              <td>Dettes fournisseurs (401000)</td>
               <td class="amount">{{ formatCurrency(totalDettesFournisseurs) }}</td>
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
    </div>
    
    <div v-if="!loading && lignesFECFiltrees.length === 0" class="empty-state">
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

.mobile-card {
  margin-bottom: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
  background: #fff;
  box-shadow: 0 1px 4px #0001;
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
  .bilan-card {
    padding: 4px !important;
  }
  .mobile-card {
    font-size: 0.98em;
    padding: 10px 8px;
  }
}
</style> 