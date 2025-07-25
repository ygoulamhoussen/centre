<script setup lang="ts">
import type { DataTableColumns } from 'naive-ui'
import type { ResultatFiscal } from '@/service/api/resultat-fiscal'
import { resultatFiscalApi } from '@/service/api/resultat-fiscal'
import { Icon } from '@iconify/vue'
import { useWindowSize } from '@vueuse/core'
import { computed, onMounted, ref } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { NButton, NCard, NCollapse, NCollapseItem, NDataTable, NEmpty, NGi, NGrid, NSelect, NSpin, NStatistic, NH1, useMessage } from 'naive-ui'
import { getEcrituresComptablesWithProprieteNom } from '@/service/api/charges-recettes'

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


definePage({
  meta: {
    title: '2033-B - Compte de résultat',
    hideInMenu: true,
    activeMenu: '/liasse-fiscale',
  },
})

const message = useMessage()

// State
const loading = ref(false)
const selectedYear = ref<number | null>(new Date().getFullYear())
const resultat = ref<ResultatFiscal | null>(null)
const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId
const lignesFEC = ref<any[]>([])

// Options for selects
const yearOptions = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => ({
    label: (currentYear - i).toString(),
    value: currentYear - i,
  }))
})

// Computed values for detailed breakdown
const chargesExternes = computed(() => {
  if (!resultat.value?.chargesDetail) return 0
  return resultat.value.chargesDetail
    .filter(charge => 
      charge.nature === 'ENTRETIEN_REPARATION' || 
      charge.nature === 'COPROPRIETE' || 
      charge.nature === 'GESTION_LOCATIVE' ||
      charge.nature === 'EXPERT_COMPTABLE' ||
      charge.nature === 'LOGICIEL_LMNP' ||
      charge.nature === 'FRAIS_COMMUNICATION' ||
      charge.nature === 'FOURNITURES'
    )
    .reduce((sum, charge) => sum + charge.montant, 0)
})

const impotsEtTaxes = computed(() => {
  if (!resultat.value?.chargesDetail) return 0
  return resultat.value.chargesDetail
    .filter(charge => 
      charge.nature === 'TAXE_FONCIERE' || 
      charge.nature === 'CFE'
    )
    .reduce((sum, charge) => sum + charge.montant, 0)
})

const interetsEmprunt = computed(() => {
  if (!resultat.value?.chargesDetail) return 0
  return resultat.value.chargesDetail
    .filter(charge => 
      charge.nature === 'INTERETS_EMPRUNT' || 
      charge.nature === 'ASSURANCE_EMPRUNTEUR'
    )
    .reduce((sum, charge) => sum + charge.montant, 0)
})

const produitsAccessoires = computed(() => {
  if (!resultat.value?.recettesDetail) return 0
  return resultat.value.recettesDetail
    .filter(recette => 
      recette.intitule.toLowerCase().includes('charges') || 
      recette.intitule.toLowerCase().includes('récupérables') ||
      recette.intitule.toLowerCase().includes('accessoires') ||
      recette.intitule.toLowerCase().includes('indemnité') ||
      recette.intitule.toLowerCase().includes('assurance')
    )
    .reduce((sum, recette) => sum + recette.montant, 0)
})

const loyersNets = computed(() => {
  return resultat.value?.recettesLocatives ?? 0
})

const totalProduits = computed(() => {
  return loyersNets.value + produitsAccessoires.value
})

const totalChargesExploitation = computed(() => {
  return chargesExternes.value + impotsEtTaxes.value + (resultat.value?.amortissements ?? 0)
})

const resultatExploitation = computed(() => {
  return totalProduits.value - totalChargesExploitation.value
})

const resultatFinancier = computed(() => {
  return -interetsEmprunt.value // Les intérêts sont des charges financières
})

const resultatCourant = computed(() => {
  return resultatExploitation.value + resultatFinancier.value
})

const comptesProduitsExceptionnels = [
  '758000', '758100', '758200', '758300', '775000', '777000'
]

const produitsExceptionnels = computed(() => {
  if (!resultat.value?.recettesDetail) return 0
  return resultat.value.recettesDetail
    .filter(recette =>
      recette.compteNum && comptesProduitsExceptionnels.includes(recette.compteNum)
    )
    .reduce((sum, recette) => sum + (recette.montant ?? 0), 0)
})

const chargesExceptionnelles = computed(() => {
  if (!resultat.value?.chargesDetail) return 0
  return resultat.value.chargesDetail
    .filter(charge =>
      charge.nature === 'CHARGE_EXCEPTIONNELLE'
    )
    .reduce((sum, charge) => sum + (charge.montant ?? 0), 0)
})

const resultatExceptionnel = computed(() => {
  return produitsExceptionnels.value - chargesExceptionnelles.value
})

// Functions
async function calculerResultat() {
  if (!selectedYear.value) {
    message.warning('Veuillez sélectionner une année.')
    return
  }

  loading.value = true
  resultat.value = null
  try {
    // Passer seulement l'année et l'utilisateur pour prendre en compte toutes les propriétés
    resultat.value = await resultatFiscalApi.calculerResultatFiscal(
      selectedYear.value,
      utilisateurId,
    )
  } catch (error) {
    message.error('Erreur lors du calcul du résultat fiscal.')
    console.error(error)
  } finally {
    loading.value = false
  }
}

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
})

// Comptes principaux (à adapter selon ton plan comptable)
const comptesProduits = ['706000', '708000', '758000']
const comptesCharges = ['606000', '606100', '606200', '606300', '606400', '606800', '615000', '614000', '622000', '623000', '626000', '627000', '635100', '635800', '637000', '661100', '681100']

function sumFEC(comptes: string[], sens: 'debit'|'credit' = 'debit') {
  return lignesFEC.value
    .filter(l => comptes.includes(l.compteNum))
    .reduce((sum, l) => sum + (Number(l[sens]) || 0), 0)
}

const totalProduitsFEC = computed(() => sumFEC(comptesProduits, 'credit'))
const totalChargesFEC = computed(() => sumFEC(comptesCharges, 'debit'))
const resultatFiscalFEC = computed(() => totalProduitsFEC.value - totalChargesFEC.value)

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

const { width } = useWindowSize()
const isMobile = computed(() => width.value <= 640)

</script>

<template>
  <div class="resultat-fiscal-page">
    <div class="page-header flex items-center justify-between">
      <NH1 class="titre-principal">Compte de Résultat LMNP (2033-B)</NH1>
    </div>
    <NCard class="filters-card">
      <div class="filters">
        <NSelect
          v-model:value="selectedYear"
          :options="yearOptions"
          placeholder="Année"
          style="width: 150px"
          @update:value="calculerResultat"
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
      <div v-if="!isMobile">
        <NCard class="recap-2033b-card">
          <table class="recap-2033b-table">
            <thead>
              <tr><th colspan="2">Compte de Résultat LMNP (2033-B)</th></tr>
            </thead>
            <tbody>
              <tr class="section"><td colspan="2">1. Produits d'exploitation</td></tr>
              <tr>
                <td>Loyers meublés (706000)</td>
                <td class="amount">{{ formatCurrency(loyersNets) }}</td>
              </tr>
              <tr>
                <td>Produits accessoires (708000)</td>
                <td class="amount">{{ formatCurrency(produitsAccessoires) }}</td>
              </tr>
              <tr class="total-row">
                <td><strong>Total produits d'exploitation</strong></td>
                <td class="amount"><strong>{{ formatCurrency(totalProduits) }}</strong></td>
              </tr>
              
              <tr class="section"><td colspan="2">2. Charges d'exploitation</td></tr>
              <tr>
                <td>Charges externes (606000, 614000, 615000, 622000, 623000, 626000)</td>
                <td class="amount">{{ formatCurrency(chargesExternes) }}</td>
              </tr>
              <tr>
                <td>Impôts et taxes (635100, 637000)</td>
                <td class="amount">{{ formatCurrency(impotsEtTaxes) }}</td>
              </tr>
              <tr>
                <td>Dotations aux amortissements (681100)</td>
                <td class="amount">{{ formatCurrency(resultat.amortissements) }}</td>
              </tr>
              <tr class="total-row">
                <td><strong>Total charges d'exploitation</strong></td>
                <td class="amount"><strong>{{ formatCurrency(totalChargesExploitation) }}</strong></td>
              </tr>
              
              <tr class="section"><td colspan="2">3. Résultat d'exploitation</td></tr>
              <tr class="result-row">
                <td><strong>Total produits – Total charges d'exploitation</strong></td>
                <td class="amount"><strong>{{ formatCurrency(resultatExploitation) }}</strong></td>
              </tr>
              
              <tr class="section"><td colspan="2">4. Résultat financier</td></tr>
              <tr>
                <td>Charges financières - Intérêts d'emprunt (661100)</td>
                <td class="amount">{{ formatCurrency(-interetsEmprunt) }}</td>
              </tr>
              <tr class="result-row">
                <td><strong>Résultat financier</strong></td>
                <td class="amount"><strong>{{ formatCurrency(resultatFinancier) }}</strong></td>
              </tr>
              
              <tr class="section"><td colspan="2">5. Résultat courant avant impôt</td></tr>
              <tr class="result-row">
                <td><strong>Résultat d'exploitation + résultat financier</strong></td>
                <td class="amount"><strong>{{ formatCurrency(resultatCourant) }}</strong></td>
              </tr>
              
              <tr class="section"><td colspan="2">6. Résultat exceptionnel</td></tr>
              <tr>
                <td>Produits exceptionnels</td>
                <td class="amount">{{ formatCurrency(produitsExceptionnels) }}</td>
              </tr>
              <tr>
                <td>Charges exceptionnelles</td>
                <td class="amount">{{ formatCurrency(chargesExceptionnelles) }}</td>
              </tr>
              <tr class="result-row">
                <td><strong>Résultat exceptionnel</strong></td>
                <td class="amount"><strong>{{ formatCurrency(resultatExceptionnel) }}</strong></td>
              </tr>
              
              <tr class="section"><td colspan="2">7. Impôt sur les bénéfices</td></tr>
              <tr>
                <td>Impôt sur les bénéfices</td>
                <td class="amount">0,00 €</td>
              </tr>
              
              <tr class="section"><td colspan="2">8. Résultat net</td></tr>
              <tr class="final-result">
                <td><strong>Résultat net de l'exercice</strong></td>
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
      <div v-else class="recap-mobile-list">
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">1. Produits d'exploitation</div>
          <div class="recap-row"><b>Loyers meublés :</b> {{ formatCurrency(loyersNets) }}</div>
          <div class="recap-row"><b>Produits accessoires :</b> {{ formatCurrency(produitsAccessoires) }}</div>
          <div class="recap-row recap-total"><b>Total produits :</b> {{ formatCurrency(totalProduits) }}</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">2. Charges d'exploitation</div>
          <div class="recap-row"><b>Charges externes :</b> {{ formatCurrency(chargesExternes) }}</div>
          <div class="recap-row"><b>Impôts et taxes :</b> {{ formatCurrency(impotsEtTaxes) }}</div>
          <div class="recap-row"><b>Dotations aux amortissements :</b> {{ formatCurrency(resultat.amortissements) }}</div>
          <div class="recap-row recap-total"><b>Total charges :</b> {{ formatCurrency(totalChargesExploitation) }}</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">3. Résultat d'exploitation</div>
          <div class="recap-row recap-total"><b>Résultat d'exploitation :</b> {{ formatCurrency(resultatExploitation) }}</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">4. Résultat financier</div>
          <div class="recap-row"><b>Charges financières - Intérêts d'emprunt :</b> {{ formatCurrency(-interetsEmprunt) }}</div>
          <div class="recap-row recap-total"><b>Résultat financier :</b> {{ formatCurrency(resultatFinancier) }}</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">5. Résultat courant avant impôt</div>
          <div class="recap-row recap-total"><b>Résultat courant :</b> {{ formatCurrency(resultatCourant) }}</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">6. Résultat exceptionnel</div>
          <div class="recap-row"><b>Produits exceptionnels :</b> {{ formatCurrency(produitsExceptionnels) }}</div>
          <div class="recap-row"><b>Charges exceptionnelles :</b> {{ formatCurrency(chargesExceptionnelles) }}</div>
          <div class="recap-row recap-total"><b>Résultat exceptionnel :</b> {{ formatCurrency(resultatExceptionnel) }}</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">7. Impôt sur les bénéfices</div>
          <div class="recap-row"><b>Impôt sur les bénéfices :</b> 0,00 €</div>
        </NCard>
        <NCard class="recap-mobile-card mb-2">
          <div class="recap-section-title">8. Résultat net</div>
          <div class="recap-row recap-final"><b>Résultat net de l'exercice :</b> {{ formatCurrency(resultat.resultatFiscal) }}</div>
        </NCard>
        <div class="actions-footer">
          <NButton type="default" disabled>
            <template #icon>
              <Icon icon="material-symbols:download" />
            </template>
            Télécharger la 2033-B (bientôt disponible)
          </NButton>
        </div>
        <NCollapse arrow-placement="right" class="details-collapse mt-4">
          <NCollapseItem title="Détail des recettes" :name="1">
            <div v-if="resultat?.recettesDetail?.length">
              <NCard v-for="(rec, i) in resultat.recettesDetail" :key="'recette-' + i" class="mobile-detail-card mb-2">
                <div class="recap-row"><b>Intitulé :</b> {{ rec.intitule }}</div>
                <div class="recap-row"><b>Propriété :</b> {{ rec.proprieteNom }}</div>
                <div class="recap-row"><b>Date :</b> {{ formatDate(rec.dateRecette) }}</div>
                <div class="recap-row"><b>Montant :</b> {{ formatCurrency(rec.montant) }}</div>
              </NCard>
            </div>
            <div v-else class="text-gray-500 text-sm">Aucune recette</div>
          </NCollapseItem>
          <NCollapseItem title="Détail des charges" :name="2">
            <div v-if="resultat?.chargesDetail?.length">
              <NCard v-for="(charge, i) in resultat.chargesDetail" :key="'charge-' + i" class="mobile-detail-card mb-2">
                <div class="recap-row"><b>Intitulé :</b> {{ charge.intitule }}</div>
                <div class="recap-row"><b>Propriété :</b> {{ charge.proprieteNom }}</div>
                <div class="recap-row"><b>Nature :</b> {{ charge.nature }}</div>
                <div class="recap-row"><b>Date :</b> {{ formatDate(charge.dateCharge) }}</div>
                <div class="recap-row"><b>Montant :</b> {{ formatCurrency(charge.montant) }}</div>
              </NCard>
            </div>
            <div v-else class="text-gray-500 text-sm">Aucune charge</div>
          </NCollapseItem>
          <NCollapseItem title="Détail des amortissements" :name="3">
            <div v-if="resultat?.amortissementsDetail?.length">
              <NCard v-for="(amort, i) in resultat.amortissementsDetail" :key="'amort-' + i" class="mobile-detail-card mb-2">
                <div class="recap-row"><b>Immobilisation :</b> {{ amort.immobilisationIntitule }}</div>
                <div class="recap-row"><b>Propriété :</b> {{ amort.proprieteNom }}</div>
                <div class="recap-row"><b>Année :</b> {{ amort.annee }}</div>
                <div class="recap-row"><b>Montant :</b> {{ formatCurrency(amort.montantAmortissement) }}</div>
              </NCard>
            </div>
            <div v-else class="text-gray-500 text-sm">Aucun amortissement</div>
          </NCollapseItem>
        </NCollapse>
      </div>
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
.recap-2033b-table .total-row {
  background: #f8f9fa;
  border-top: 1px solid #dee2e6;
}
.recap-2033b-table .result-row {
  background: #e8f4fd;
  border-top: 1px solid #bee5eb;
}
.recap-2033b-table .final-result {
  background: #d4edda;
  border-top: 2px solid #28a745;
  font-weight: bold;
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

.recap-mobile-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.recap-mobile-card {
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  border-radius: 10px;
  padding: 12px 10px;
}
.recap-section-title {
  font-weight: bold;
  font-size: 1.08rem;
  margin-bottom: 4px;
  color: #2563eb;
}
.recap-row {
  font-size: 0.98em;
  margin-bottom: 2px;
}
.recap-total {
  font-weight: bold;
  color: #18a058;
}
.recap-final {
  font-weight: bold;
  color: #d03050;
}
.mobile-detail-card {
  box-shadow: 0 1px 4px #0001;
  border-radius: 8px;
  padding: 10px 8px;
  background: #fff;
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
@media (max-width: 640px) {
  .recap-mobile-card {
    font-size: 0.97rem;
    padding: 10px 6px;
  }
  .mobile-detail-card {
    font-size: 0.97rem;
    padding: 8px 5px;
  }
}
</style> 