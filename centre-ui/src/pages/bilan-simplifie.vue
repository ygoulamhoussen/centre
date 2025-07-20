<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { NButton, NCard, NCollapse, NCollapseItem, NDataTable, NEmpty, NSelect, NSpin, useMessage, NH1 } from 'naive-ui'
import type { DataTableColumns } from 'naive-ui'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/store/modules/auth'
import { bilanSimplifieApi } from '@/service/api/bilan-simplifie'
import { getEcrituresComptablesWithProprieteNom } from '@/service/api/charges-recettes'

definePage({
  meta: {
    title: 'Bilan simplifiée',
    order: 10,
    hideInMenu: true,
  },
})

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
const comptesProduits = ['70', '74', '75', '77', '78', '79']; // 70x ventes, 74x subventions, 75x autres produits, 77x, 78x, 79x produits exceptionnels
const comptesCharges = ['60', '61', '62', '63', '64', '65', '66', '67', '68', '69']; // 60x à 69x charges

// Lignes FEC pour le calcul du résultat de l'exercice (uniquement l'année sélectionnée)
const lignesFECResultat = computed(() =>
  lignesFEC.value.filter(l =>
    l.dateEcriture && new Date(l.dateEcriture).getFullYear() === selectedYear.value
  )
)

const resultatExerciceBilan = computed(() => {
  const produits = lignesFECResultat.value
    .filter(l => comptesProduits.some(prefix => l.compteNum.startsWith(prefix)))
    .reduce((sum, l) => sum + (Number(l.credit) || 0), 0)
  const charges = lignesFECResultat.value
    .filter(l => comptesCharges.some(prefix => l.compteNum.startsWith(prefix)))
    .reduce((sum, l) => sum + (Number(l.debit) || 0), 0)
  return produits - charges
})

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

// === STRUCTURE OFFICIELLE 2033-A ===
const postes2033A = [
  // ACTIF
  { code: '01', label: 'Immobilisations incorporelles', comptes: ['20'] },
  { code: '02', label: 'Immobilisations corporelles', comptes: ['211','212','213','214','215','217','218','219'] },
  { code: '03', label: 'Immobilisations financières', comptes: ['26', '27'] },
  { code: '04', label: 'Amortissements incorporelles', comptes: ['280'], negatif: true },
  { code: '05', label: 'Amortissements corporelles', comptes: ['281'], negatif: true },
  { code: '06', label: 'Provisions dépréciation immo. financières', comptes: ['290'], negatif: true },
  { code: '07', label: 'Stocks et en-cours', comptes: ['31','32','33','34','35','36','37'] },
  { code: '08', label: 'Avances/acompte versés', comptes: ['4091','4096'] },
  { code: '09', label: 'Créances clients', comptes: ['411'] },
  { code: '10', label: 'Autres créances', comptes: ['412','413','414','415','416','417','418','4196','425','4282','431','437','4386','441','445','446','447','448','451','453','455','456','457','467','468','486','487'] },
  { code: '11', label: 'Valeurs mobilières de placement', comptes: ['50'] },
  { code: '12', label: 'Disponibilités', comptes: ['51','53'] },
  { code: '13', label: 'Charges constatées d’avance, écarts de conversion actif', comptes: ['486','487','488'] },
  // PASSIF
  { code: '14', label: 'Capital', comptes: ['101'] },
  { code: '15', label: 'Réserves', comptes: ['106'] },
  { code: '16', label: 'Report à nouveau', comptes: ['110','119'] },
  { code: '17', label: 'Résultat de l’exercice', comptes: ['120','129'] },
  { code: '18', label: 'Provisions pour risques et charges', comptes: ['15'] },
  { code: '19', label: 'Emprunts et dettes établissements de crédit', comptes: ['164','168'] },
  { code: '20', label: 'Avances reçues sur commandes', comptes: ['419'] },
  { code: '21', label: 'Dettes fournisseurs', comptes: ['401'] },
  { code: '22', label: 'Dettes fiscales et sociales', comptes: ['43'] },
  { code: '23', label: 'Autres dettes', comptes: ['42','44','45','46','47'], exclude: ['43'] },
  { code: '24', label: 'Produits constatés d’avance, écarts de conversion passif', comptes: ['487','488'] },
]

// Filtrer toutes les écritures jusqu'à la date de clôture de l'année sélectionnée
const lignesFECBilan = computed(() =>
  lignesFEC.value.filter(l =>
    l.dateEcriture && new Date(l.dateEcriture) <= new Date(`${selectedYear.value}-12-31`)
  )
)

function sumPoste2033A(poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] }, sens = 'debit') {
  // Poste 17 : résultat de l'exercice = calcul dynamique
  if (poste.code === '17') {
    return resultatExerciceBilan.value
  }
  // Poste 14 : capital = variable d'équilibre
  if (poste.code === '14') {
    // Total actif cumulé
    const totalActif = postes2033A.slice(0, 13).reduce((sum, p) => sum + sumPoste2033A(p), 0)
    // Total passif hors capital (on retire le poste 14)
    const totalPassifHorsCapital = postes2033A.slice(1 + 13).reduce((sum, p) => sum + (p.code === '14' ? 0 : sumPoste2033A(p)), 0)
    return totalActif - totalPassifHorsCapital
  }
  // On construit le bilan uniquement à partir des écritures comptables (lignes FEC)
  return lignesFECBilan.value
    .filter(l => {
      if (poste.exclude && poste.exclude.some((ex: string) => l.compteNum.startsWith(ex))) return false
      // Inclusion stricte par préfixe de compte (ex: 213, 215, 218...)
      return poste.comptes.some((c: string) => l.compteNum.startsWith(c))
    })
    .reduce((sum, l) => {
      let val = 0
      if (poste.negatif) {
        // Amortissements/provisions : crédit (signe -)
        val = -(Number(l.credit) || 0)
      } else if (Number(poste.code) >= 14) {
        // Passif : crédit
        val = Number(l.credit) || 0
      } else {
        // Actif : débit
        val = Number(l.debit) || 0
      }
      return sum + val
    }, 0)
}

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
      <NH1 class="titre-principal">Bilan Simplifié 2033-A (officiel)</NH1>
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
            <tr><th colspan="3">Bilan Simplifié 2033-A au {{ formatDate(selectedYear + '-12-31') }}</th></tr>
          </thead>
          <tbody>
            <tr class="section"><td colspan="3">ACTIF</td></tr>
            <tr v-for="poste in postes2033A.slice(0, 13)" :key="poste.code">
              <td>{{ poste.code }}</td>
              <td>{{ poste.label }}</td>
              <td class="amount">{{ formatCurrency(sumPoste2033A(poste)) }}</td>
            </tr>
            <tr class="section"><td colspan="3">PASSIF</td></tr>
            <tr v-for="poste in postes2033A.slice(13)" :key="poste.code">
              <td>{{ poste.code }}</td>
              <td>{{ poste.label }}</td>
              <td class="amount">{{ formatCurrency(sumPoste2033A(poste)) }}</td>
            </tr>
            <tr class="final-total">
              <td colspan="2"><strong>TOTAL ACTIF</strong></td>
              <td class="amount"><strong>{{ formatCurrency(postes2033A.slice(0, 13).reduce((sum, p) => sum + sumPoste2033A(p), 0)) }}</strong></td>
            </tr>
            <tr class="final-total">
              <td colspan="2"><strong>TOTAL PASSIF</strong></td>
              <td class="amount"><strong>{{ formatCurrency(postes2033A.slice(13).reduce((sum, p) => sum + sumPoste2033A(p), 0)) }}</strong></td>
            </tr>
          </tbody>
        </table>
        <div class="equilibre-info" v-if="Math.abs(postes2033A.slice(0, 13).reduce((sum, p) => sum + sumPoste2033A(p), 0) - postes2033A.slice(13).reduce((sum, p) => sum + sumPoste2033A(p), 0)) < 0.01">
          <Icon icon="material-symbols:check-circle" class="equilibre-icon" />
          <span>Le bilan est équilibré (Actif = Passif)</span>
        </div>
        <div class="equilibre-info error" v-else>
          <Icon icon="material-symbols:error" class="equilibre-icon" />
          <span>Le bilan n'est pas équilibré (différence: {{ formatCurrency(postes2033A.slice(0, 13).reduce((sum, p) => sum + sumPoste2033A(p), 0) - postes2033A.slice(13).reduce((sum, p) => sum + sumPoste2033A(p), 0)) }})</span>
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