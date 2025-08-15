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

// === STRUCTURE SIMPLIFIÉE SELON L'IMAGE ===
const postesBilan = [
  // ACTIF
  { code: '02', label: 'Immobilisations corporelles', comptes: ['211','212','213','214','215','217','218','219'] },
  { code: '12', label: 'Disponibilités', comptes: ['51','53'] },
  // PASSIF
  { code: '14', label: 'Capital social ou individuel', comptes: ['101'] },
  { code: '17', label: 'Résultat de l\'exercice', comptes: ['120','129'] },
  { code: '19', label: 'Emprunts et dettes assimilées', comptes: ['164','168'] },
]

// Libellés des comptes principaux
const libellesComptes: { [key: string]: string } = {
  // Immobilisations corporelles
  '211': 'Terrains',
  '212': 'Constructions',
  '213': 'Installations techniques, matériel et outillage industriels',
  '214': 'Concessions et droits similaires, brevets, licences, marques, procédés, logiciels, droits et valeurs similaires',
  '215': 'Fonds commercial',
  '217': 'Autres immobilisations corporelles',
  '218': 'Immobilisations corporelles en cours',
  '219': 'Avances et acomptes versés sur immobilisations corporelles',
  
  // Disponibilités
  '51': 'Banques, établissements financiers et assimilés',
  '53': 'Caisse',
  
  // Capital
  '101': 'Capital social ou individuel',
  
  // Résultat
  '120': 'Résultat de l\'exercice (bénéfice)',
  '129': 'Résultat de l\'exercice (perte)',
  
  // Emprunts
  '164': 'Emprunts bancaires et crédits de même nature',
  '168': 'Autres emprunts',
}

// Filtrer toutes les écritures jusqu'à la date de clôture de l'année sélectionnée
const lignesFECBilan = computed(() =>
  lignesFEC.value.filter(l =>
    l.dateEcriture && new Date(l.dateEcriture) <= new Date(`${selectedYear.value}-12-31`)
  )
)

function sumPosteBilan(
  poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] },
  sens: 'debit' | 'credit' = 'debit'
): number {
  // Poste 17 : résultat de l'exercice = calcul dynamique
  if (poste.code === '17') {
    return resultatExerciceBilan.value
  }
  // Poste 14 : capital social = variable d'équilibre (Actif - Passif hors capital)
  if (poste.code === '14') {
    // Total actif
    const totalActif = postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => {
      return sum + sumPosteBilan(p)
    }, 0)
    // Total passif hors capital (seulement résultat et emprunts)
    const totalPassifHorsCapital = postesBilan.filter(p => ['17', '19'].includes(p.code)).reduce((sum, p) => {
      return sum + sumPosteBilan(p)
    }, 0)
    return totalActif - totalPassifHorsCapital
  }
  // Correction pour Disponibilités (poste 12) : solde = débit - crédit
  if (poste.code === '12') {
    return lignesFECBilan.value
      .filter(l => poste.comptes.some((c: string) => l.compteNum.startsWith(c)))
      .reduce((sum, l) => sum + (Number(l.debit) || 0) - (Number(l.credit) || 0), 0)
  }
  // Correction pour Immobilisations corporelles (poste 02) : net = brut - amortissements
  if (poste.code === '02') {
    const brut = sumPosteBilanBrut(poste)
    const amortissements = sumPosteBilanAmortissement(poste)
    return brut - amortissements
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

// Fonction pour calculer le brut (valeur d'origine)
function sumPosteBilanBrut(
  poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] }
): number {
  // Pour les immobilisations, le brut = valeur d'origine (débit des comptes 21x)
  if (poste.code === '02') {
    return lignesFECBilan.value
      .filter(l => poste.comptes.some((c: string) => l.compteNum.startsWith(c)))
      .reduce((sum, l) => sum + (Number(l.debit) || 0), 0)
  }
  // Pour les disponibilités, brut = net
  if (poste.code === '12') {
    return sumPosteBilan(poste)
  }
  // Pour les postes passifs, pas de valeur brute (vide dans l'image)
  return 0
}

// Fonction pour calculer les amortissements
function sumPosteBilanAmortissement(
  poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] }
): number {
  // Pour les immobilisations corporelles, calculer les amortissements cumulés
  if (poste.code === '02') {
    return lignesFECBilan.value
      .filter(l => l.compteNum.startsWith('28'))
      .reduce((sum, l) => sum + (Number(l.credit) || 0), 0)
  }
  return 0
}

// Fonction pour calculer l'exercice N-1
function sumPosteBilanN1(
  poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] }
): number {
  const anneePrecedente = selectedYear.value ? selectedYear.value - 1 : null
  if (!anneePrecedente) return 0
  
  // Filtrer les écritures jusqu'à la fin de l'année précédente
  const lignesFECN1 = lignesFEC.value.filter(l =>
    l.dateEcriture && new Date(l.dateEcriture) <= new Date(`${anneePrecedente}-12-31`)
  )
  
  // Poste 14 : capital social N-1 = variable d'équilibre pour N-1
  if (poste.code === '14') {
    // Total actif N-1
    const totalActifN1 = postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => {
      return sum + sumPosteBilanN1(p)
    }, 0)
    // Total passif hors capital N-1 (seulement résultat et emprunts)
    const totalPassifHorsCapitalN1 = postesBilan.filter(p => ['17', '19'].includes(p.code)).reduce((sum, p) => {
      return sum + sumPosteBilanN1(p)
    }, 0)
    return totalActifN1 - totalPassifHorsCapitalN1
  }
  
  // Même logique que sumPosteBilan mais avec les écritures N-1
  if (poste.code === '12') {
    return lignesFECN1
      .filter(l => poste.comptes.some((c: string) => l.compteNum.startsWith(c)))
      .reduce((sum, l) => sum + (Number(l.debit) || 0) - (Number(l.credit) || 0), 0)
  }
  // Correction pour Immobilisations corporelles (poste 02) N-1 : net = brut - amortissements
  if (poste.code === '02') {
    const brutN1 = lignesFECN1
      .filter(l => poste.comptes.some((c: string) => l.compteNum.startsWith(c)))
      .reduce((sum, l) => sum + (Number(l.debit) || 0), 0)
    const amortissementsN1 = lignesFECN1
      .filter(l => l.compteNum.startsWith('28'))
      .reduce((sum, l) => sum + (Number(l.credit) || 0), 0)
    return brutN1 - amortissementsN1
  }
  
  return lignesFECN1
    .filter(l => {
      if (poste.exclude && poste.exclude.some((ex: string) => l.compteNum.startsWith(ex))) return false
      return poste.comptes.some((c: string) => l.compteNum.startsWith(c))
    })
    .reduce((sum, l) => {
      let val = 0
      if (poste.negatif) {
        val = -(Number(l.credit) || 0)
      } else if (Number(poste.code) >= 14) {
        val = Number(l.credit) || 0
      } else {
        val = Number(l.debit) || 0
      }
      return sum + val
    }, 0)
}

// Fonction pour obtenir le code brut
function getCodeBrut(poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] }): string {
  // Codes selon l'image de référence
  const codesBrut: { [key: string]: string } = {
    '02': '028', // Immobilisations corporelles
    '12': '084', // Disponibilités
    '14': '120', // Capital social ou individuel
    '17': '136', // Résultat de l'exercice
    '19': '156', // Emprunts et dettes assimilées
  }
  return codesBrut[poste.code] || ''
}

// Fonction pour obtenir le code brut total actif
function getCodeBrutTotal(): string {
  return '110'
}

// Fonction pour obtenir le code brut total passif
function getCodeBrutTotalPassif(): string {
  return '180'
}

// Fonction pour obtenir le libellé d'un compte
function getLibelleCompte(compte: string): string {
  return libellesComptes[compte] || compte
}

// Fonction pour obtenir les libellés des comptes d'un poste
function getLibellesComptesPoste(poste: { code: string, label: string, comptes: string[], negatif?: boolean, exclude?: string[] }): string {
  return poste.comptes.map(compte => `${compte} - ${getLibelleCompte(compte)}`).join(', ')
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
                  <div v-if="isMobile" class="bilan-mobile">
            <div class="bilan-section">ACTIF</div>
            <div v-for="poste in postesBilan.filter(p => ['02', '12'].includes(p.code))" :key="poste.code" class="bilan-mobile-card">
              <div class="bilan-mobile-row">
                <span class="bilan-mobile-code">{{ poste.code }}</span> 
                <span class="bilan-mobile-label">{{ poste.label }}</span>
              </div>
              <div class="bilan-mobile-details">
                <div class="bilan-mobile-detail">
                  <span class="detail-label">Brut:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilanBrut(poste)) }}</span>
                </div>
                <div class="bilan-mobile-detail">
                  <span class="detail-label">Amort.:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilanAmortissement(poste)) }}</span>
                </div>
                <div class="bilan-mobile-detail">
                  <span class="detail-label">Net N:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilan(poste)) }}</span>
                </div>
                <div class="bilan-mobile-detail">
                  <span class="detail-label">N-1:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilanN1(poste)) }}</span>
                </div>
              </div>
            </div>
            <div class="bilan-section">PASSIF</div>
            <div v-for="poste in postesBilan.filter(p => !['02', '12'].includes(p.code))" :key="poste.code" class="bilan-mobile-card">
              <div class="bilan-mobile-row">
                <span class="bilan-mobile-code">{{ poste.code }}</span> 
                <span class="bilan-mobile-label">{{ poste.label }}</span>
              </div>
              <div class="bilan-mobile-details">
                <div class="bilan-mobile-detail">
                  <span class="detail-label">Brut:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilanBrut(poste)) }}</span>
                </div>
                <div class="bilan-mobile-detail">
                  <span class="detail-label">Amort.:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilanAmortissement(poste)) }}</span>
                </div>
                <div class="bilan-mobile-detail">
                  <span class="detail-label">Net N:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilan(poste)) }}</span>
                </div>
                <div class="bilan-mobile-detail">
                  <span class="detail-label">N-1:</span>
                  <span class="detail-value">{{ formatCurrency(sumPosteBilanN1(poste)) }}</span>
                </div>
              </div>
            </div>
                     <div class="bilan-mobile-total">
             <div><strong>total</strong></div>
             <div class="bilan-mobile-amount"><strong>{{ formatCurrency(postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0)) }}</strong></div>
           </div>
           <div class="bilan-mobile-total">
             <div><strong>Total</strong></div>
             <div class="bilan-mobile-amount"><strong>{{ formatCurrency(postesBilan.filter(p => !['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0)) }}</strong></div>
           </div>
        </div>
        <table v-else class="bilan-table">
          <thead>
            <tr><th colspan="6">Bilan Simplifié 2033-A au {{ formatDate(selectedYear + '-12-31') }}</th></tr>
            <tr class="header-row">
              <th></th>
              <th>Brut</th>
              <th></th>
              <th>Amortissement</th>
              <th>Net Exercice N</th>
              <th>Exercice N-1</th>
            </tr>
          </thead>
                      <tbody>
              <tr class="section"><td colspan="6">ACTIF</td></tr>
                             <tr v-for="poste in postesBilan.filter(p => ['02', '12'].includes(p.code))" :key="poste.code" class="data-row">
                 <td class="code">{{ poste.code }} - {{ poste.label }}</td>
                 <td class="brut">{{ formatCurrency(sumPosteBilanBrut(poste)) }}</td>
                 <td class="code-brut">{{ getCodeBrut(poste) }}</td>
                 <td class="amortissement">{{ formatCurrency(sumPosteBilanAmortissement(poste)) }}</td>
                 <td class="net">{{ formatCurrency(sumPosteBilan(poste)) }}</td>
                 <td class="n1">{{ formatCurrency(sumPosteBilanN1(poste)) }}</td>
               </tr>
                                        <tr class="final-total">
               <td class="code"><strong>Total</strong></td>
               <td class="brut"><strong>{{ formatCurrency(postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilanBrut(p), 0)) }}</strong></td>
               <td class="code-brut">{{ getCodeBrutTotal() }}</td>
               <td class="amortissement"><strong>{{ formatCurrency(sumPosteBilanAmortissement({ code: '02', label: '', comptes: [] })) }}</strong></td>
               <td class="net"><strong>{{ formatCurrency(postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0)) }}</strong></td>
               <td class="n1"><strong>{{ formatCurrency(postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilanN1(p), 0)) }}</strong></td>
             </tr>
              <tr class="section"><td colspan="6">PASSIF</td></tr>
                             <tr v-for="poste in postesBilan.filter(p => !['02', '12'].includes(p.code))" :key="poste.code" class="data-row">
                 <td class="code">{{ poste.code }} - {{ poste.label }}</td>
                 <td class="brut"></td>
                 <td class="code-brut">{{ getCodeBrut(poste) }}</td>
                 <td class="amortissement"></td>
                 <td class="net">{{ formatCurrency(sumPosteBilan(poste)) }}</td>
                 <td class="n1">{{ formatCurrency(sumPosteBilanN1(poste)) }}</td>
               </tr>

             <tr class="final-total">
               <td colspan="2"><strong>Total</strong></td>
               <td class="code-brut">{{ getCodeBrutTotalPassif() }}</td>
               <td class="amortissement"><strong></strong></td>
               <td class="net"><strong>{{ formatCurrency(postesBilan.filter(p => !['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0)) }}</strong></td>
               <td class="n1"><strong>{{ formatCurrency(postesBilan.filter(p => !['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilanN1(p), 0)) }}</strong></td>
             </tr>
          </tbody>
        </table>
                 <div class="equilibre-info" v-if="Math.abs(postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0) - postesBilan.filter(p => ['14', '17', '19'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0)) < 0.01">
           <Icon icon="material-symbols:check-circle" class="equilibre-icon" />
           <span>Le bilan est équilibré (Actif = Passif)</span>
         </div>
         <div class="equilibre-info error" v-else>
           <Icon icon="material-symbols:error" class="equilibre-icon" />
           <span>Le bilan n'est pas équilibré (différence: {{ formatCurrency(postesBilan.filter(p => ['02', '12'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0) - postesBilan.filter(p => ['14', '17', '19'].includes(p.code)).reduce((sum, p) => sum + sumPosteBilan(p), 0)) }})</span>
         </div>
         
         <!-- Détail des comptes avec libellés -->
         <NCollapse class="details-collapse">
           <NCollapseItem title="Détail des comptes utilisés" name="comptes">
             <div class="comptes-detail">
               <div v-for="poste in postesBilan" :key="poste.code" class="poste-comptes">
                 <h4>{{ poste.code }} - {{ poste.label }}</h4>
                 <div class="comptes-list">
                   <div v-for="compte in poste.comptes" :key="compte" class="compte-item">
                     <span class="compte-code">{{ compte }}</span>
                     <span class="compte-libelle">{{ getLibelleCompte(compte) }}</span>
                   </div>
                 </div>
               </div>
             </div>
           </NCollapseItem>
         </NCollapse>
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

.bilan-table .header-row th {
  background: #f8fafc;
  border-bottom: 2px solid #e5e7eb;
  text-align: center;
}

.bilan-table .section td {
  background: #1e40af;
  color: white;
  font-weight: bold;
  border-top: 2px solid #1e3a8a;
}

.bilan-table .data-row td {
  background: #f8fafc;
}

.bilan-table .code {
  font-weight: bold;
  color: #1e40af;
  text-align: left;
  width: 200px;
  padding-left: 16px;
}

.bilan-table .brut {
  text-align: right;
  font-variant-numeric: tabular-nums;
  background: #e0f2fe;
}

.bilan-table .code-brut {
  text-align: center;
  font-weight: bold;
  color: #1e40af;
  width: 60px;
}

.bilan-table .amortissement {
  text-align: right;
  font-variant-numeric: tabular-nums;
  background: #e0f2fe;
}

.bilan-table .net {
  text-align: right;
  font-variant-numeric: tabular-nums;
  background: #e0f2fe;
  font-weight: bold;
}

.bilan-table .n1 {
  text-align: right;
  font-variant-numeric: tabular-nums;
  background: #e0f2fe;
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

.bilan-table .final-total .net {
  background: #d4edda;
}

.bilan-table .final-total .brut,
.bilan-table .final-total .amortissement,
.bilan-table .final-total .n1 {
  background: #d4edda;
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

.comptes-detail {
  padding: 16px;
  background: #f8fafc;
  border-radius: 8px;
}

.poste-comptes {
  margin-bottom: 20px;
}

.poste-comptes h4 {
  color: #1e40af;
  font-weight: bold;
  margin-bottom: 8px;
  padding-bottom: 4px;
  border-bottom: 2px solid #e5e7eb;
}

.comptes-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.compte-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 8px;
  background: white;
  border-radius: 4px;
  border-left: 3px solid #1e40af;
}

.compte-code {
  font-weight: bold;
  color: #1e40af;
  min-width: 60px;
  font-family: monospace;
}

.compte-libelle {
  color: #374151;
  flex: 1;
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

.bilan-mobile {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.bilan-section {
  background: #1e40af;
  color: white;
  font-weight: bold;
  padding: 8px 12px;
  border-radius: 6px;
  margin-top: 12px;
}
.bilan-mobile-card {
  background: #f1f5f9;
  border-radius: 8px;
  padding: 10px 12px;
  display: flex;
  flex-direction: column;
  margin-bottom: 4px;
  box-shadow: 0 1px 2px #0001;
}
.bilan-mobile-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}
.bilan-mobile-code {
  font-weight: bold;
  color: #1e40af;
  min-width: 2.5em;
}
.bilan-mobile-label {
  flex: 1;
}
.bilan-mobile-amount {
  text-align: right;
  font-variant-numeric: tabular-nums;
  font-size: 1.1em;
  font-weight: bold;
  color: #0f5132;
  margin-top: 2px;
}

.bilan-mobile-details {
  margin-top: 8px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
  font-size: 0.9em;
}

.bilan-mobile-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2px 4px;
  background: #f8fafc;
  border-radius: 4px;
}

.detail-label {
  font-weight: 500;
  color: #64748b;
}

.detail-value {
  font-variant-numeric: tabular-nums;
  font-weight: 600;
  color: #0f5132;
}
.bilan-mobile-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #d4edda;
  border-radius: 8px;
  padding: 10px 12px;
  margin-top: 8px;
  font-weight: bold;
  font-size: 1.08em;
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
  .bilan-table {
    display: none;
  }
  .bilan-mobile {
    display: flex;
  }
}
@media (min-width: 769px) {
  .bilan-mobile {
    display: none;
  }
}
</style> 