<script setup lang="ts">
// @ts-expect-error - definePage is a macro
import { definePage } from 'vue-router'
import { computed, h, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { NButton, NCard, NDataTable, NTabPane, NTabs, useMessage } from 'naive-ui'
import type { ChargeDTO } from '@/types/dto'
import { createCharge, createEcritureComptableCharge } from '@/service/api/charges-recettes'
import { useAppStore } from '@/store/modules/app'

const route = useRoute()
const router = useRouter()
const credit = ref<any>(null)
const echeancier = ref<any[]>([])
const message = useMessage()
const writingInProgress = ref<Record<number, boolean>>({})
const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL
const isMobile = useAppStore().isMobile

definePage({
  meta: {
    title: 'Échéancier Crédit',
    icon: 'material-symbols:calendar-month',
    hideInMenu: true,
  },
})

function goBack() {
  router.push('/credits')
}

function formatDate(date: number | string | null) {
  if (!date) {
    return ''
  }
  const d = typeof date === 'number' ? new Date(date) : new Date(date)
  return d.toLocaleDateString('fr-FR')
}

onMounted(async () => {
  const id = route.params.id as string | undefined
  const utilisateurId = '00000000-0000-0000-0000-000000000003' // ID utilisateur codé en dur pour l'exemple
  if (!id) {
    message.error('ID du crédit manquant')
    console.error('ID du crédit manquant')
    return
  }
  try {
    // Charger le crédit
    const creditRes = await fetch(`${baseUrl}/api/getCreditsByUtilisateur/${utilisateurId}`)
    if (!creditRes.ok) {
      throw new Error('Erreur lors du chargement du crédit')
    }
    const credits = await creditRes.json()
    const foundCredit = credits.find((c: any) => c.id === id)
    if (!foundCredit) {
      throw new Error('Crédit non trouvé')
    }
    // Adapter les champs pour l'affichage
    credit.value = {
      intitule: foundCredit.intitule || (foundCredit.banque ? `Crédit ${foundCredit.banque}` : 'Crédit'),
      montant: foundCredit.montantEmprunte,
      duree: foundCredit.dureeMois,
      taux: foundCredit.tauxInteretAnnuel,
      typeBien: foundCredit.typeBien,
      dateDebut: foundCredit.dateDebut,
      proprieteNom: foundCredit.proprieteNom,
      // Ajoute d'autres champs si besoin
    }

    // Charger les échéances
    const echeancesRes = await fetch(`${baseUrl}/api/getEcheancesByCredit/${id}`)
    if (!echeancesRes.ok) {
      throw new Error('Erreur lors du chargement des échéances')
    }
    echeancier.value = await echeancesRes.json()
  }
  catch (e: any) {
    message.error(`Erreur lors du chargement : ${e.message}`)
    console.error('Erreur de chargement', e)
  }
})

const echeancierColumns = [
  { title: 'N°', key: 'numero', width: 60, render: (_row: any, index: number) => index + 1 },
  { title: 'Date', key: 'dateEcheance', width: 120, render: (row: any) => formatDate(row.dateEcheance) },
  { title: 'Capital remboursé', key: 'capitalRembourse', width: 120 },
  { title: 'Intérêts', key: 'interet', width: 100 },
  { title: 'Assurance', key: 'assurance', width: 100 },
  { title: 'Total échéance', key: 'totalEcheance', width: 120 },
]

const chargesAnnuelles = computed(() => {
  const charges: { [year: number]: { year: number, totalInterets: number, totalAssurance: number, totalCharges: number } } = {}

  echeancier.value.forEach((e) => {
    const year = new Date(e.dateEcheance).getFullYear()
    if (!charges[year]) {
      charges[year] = { year, totalInterets: 0, totalAssurance: 0, totalCharges: 0 }
    }
    charges[year].totalInterets += Number(e.interet) || 0
    charges[year].totalAssurance += Number(e.assurance) || 0
    charges[year].totalCharges = charges[year].totalInterets + charges[year].totalAssurance
  })

  return Object.values(charges).sort((a, b) => a.year - b.year)
})

async function passerEcriture(chargeAnnuelle: { year: number, totalCharges: number }) {
  if (!credit.value || !credit.value.proprieteId || !credit.value.utilisateurId) {
    message.error('Impossible de trouver les informations du crédit pour ce crédit.')
    return
  }

  writingInProgress.value[chargeAnnuelle.year] = true
  try {
    const chargeData: Partial<ChargeDTO> = {
      proprieteId: credit.value.proprieteId,
      utilisateurId: credit.value.utilisateurId,
      nature: 'INTERETS_EMPRUNT',
      dateCharge: `${chargeAnnuelle.year}-12-31`,
      montant: chargeAnnuelle.totalCharges.toString(),
      intitule: `Charges financières crédit ${credit.value.intitule || ''} ${chargeAnnuelle.year}`,
      commentaire: `Charges financières (intérêts + assurance) pour le crédit ${credit.value.intitule || ''} - Année ${chargeAnnuelle.year}`,
      statut: 'PAYEE',
    }

    await createCharge(chargeData)
    message.success(`Écriture comptable pour ${chargeAnnuelle.year} passée avec succès.`)
  }
  catch (error: any) {
    message.error(`Erreur lors de l'écriture comptable : ${error.message}`)
  }
  finally {
    writingInProgress.value[chargeAnnuelle.year] = false
  }
}

const chargesAnnuellesColumns = [
  { title: 'Année', key: 'year' },
  { title: 'Total Intérêts', key: 'totalInterets', render: (row: { totalInterets: number }) => `${row.totalInterets.toFixed(2)} €` },
  { title: 'Total Assurance', key: 'totalAssurance', render: (row: { totalAssurance: number }) => `${row.totalAssurance.toFixed(2)} €` },
  { title: 'Total Charges', key: 'totalCharges', render: (row: { totalCharges: number }) => `${row.totalCharges.toFixed(2)} €` },
  {
    title: 'Actions',
    key: 'actions',
    render: (row: { year: number, totalCharges: number }) => {
      return h(NButton, {
        size: 'small',
        type: 'primary',
        ghost: true,
        onClick: () => passerEcriture(row),
        loading: writingInProgress.value[row.year] || false,
      }, { default: () => "Passer l'écriture" })
    },
  },
]
</script>

<template>
  <div class="echeancier-page">
    <div class="page-header">
      <div v-if="!isMobile" class="flex justify-between items-center">
        <NButton @click="goBack" quaternary>
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Retour
        </NButton>
        <h1>Échéancier du Crédit</h1>
      </div>
      <div v-else class="mobile-header">
        <h1 class="mobile-title">Échéancier</h1>
        <NButton block size="small" quaternary class="mobile-journal-btn" @click="goBack">
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Retour
        </NButton>
      </div>
    </div>
    <NCard v-if="credit" title="Informations du crédit" class="info-card">
      <div v-if="!isMobile" class="credit-info">
        <div class="info-item"><strong>Intitulé :</strong> {{ credit.intitule }}</div>
        <div class="info-item"><strong>Montant :</strong> {{ credit.montant }} €</div>
        <div class="info-item"><strong>Durée :</strong> {{ credit.duree }} mois</div>
        <div class="info-item"><strong>Taux :</strong> {{ credit.taux }} %</div>
        <div class="info-item"><strong>Type :</strong> {{ credit.typeBien }}</div>
        <div class="info-item"><strong>Date de début :</strong> {{ formatDate(credit.dateDebut) }}</div>
        <div class="info-item"><strong>Propriété :</strong> {{ credit.proprieteNom }}</div>
      </div>
      <div v-else class="mobile-card">
        <div><b>Intitulé :</b> {{ credit.intitule }}</div>
        <div><b>Montant :</b> {{ credit.montant }} €</div>
        <div><b>Durée :</b> {{ credit.duree }} mois</div>
        <div><b>Taux :</b> {{ credit.taux }} %</div>
        <div><b>Type :</b> {{ credit.typeBien }}</div>
        <div><b>Date de début :</b> {{ formatDate(credit.dateDebut) }}</div>
        <div><b>Propriété :</b> {{ credit.proprieteNom }}</div>
      </div>
    </NCard>
    <NCard class="table-card">
      <NTabs type="line" animated>
        <NTabPane name="echeancier" tab="Échéancier">
          <NDataTable v-if="!isMobile" :columns="echeancierColumns" :data="echeancier" striped />
          <div v-else>
            <NCard v-for="(e, idx) in echeancier" :key="idx" class="mobile-card">
              <div><b>N° :</b> {{ idx + 1 }}</div>
              <div><b>Date :</b> {{ formatDate(e.dateEcheance) }}</div>
              <div><b>Capital remboursé :</b> {{ e.capitalRembourse }}</div>
              <div><b>Intérêts :</b> {{ e.interet }}</div>
              <div><b>Assurance :</b> {{ e.assurance }}</div>
              <div><b>Total échéance :</b> {{ e.totalEcheance }}</div>
            </NCard>
          </div>
        </NTabPane>
        <NTabPane name="charges" tab="Charges Financières Annuelles">
          <NDataTable v-if="!isMobile" :columns="chargesAnnuellesColumns" :data="chargesAnnuelles" />
          <div v-else>
            <NCard v-for="(c, idx) in chargesAnnuelles" :key="idx" class="mobile-card">
              <div><b>Année :</b> {{ c.year }}</div>
              <div><b>Total Intérêts :</b> {{ c.totalInterets.toFixed(2) }} €</div>
              <div><b>Total Assurance :</b> {{ c.totalAssurance.toFixed(2) }} €</div>
              <div><b>Total Charges :</b> {{ c.totalCharges.toFixed(2) }} €</div>
              <div class="actions">
                <NButton size="small" type="primary" ghost @click="passerEcriture(c)">
                  Passer l'écriture
                </NButton>
              </div>
            </NCard>
          </div>
        </NTabPane>
      </NTabs>
    </NCard>
  </div>
</template>

<style scoped>
.echeancier-page {
  padding: 20px;
}
.page-header {
  margin-bottom: 1.5rem;
}
.mobile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-bottom: 1.5rem;
}
.mobile-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 12px 0 0 0;
  text-align: center;
  color: #222;
}
.mobile-journal-btn {
  margin-bottom: 10px;
  max-width: 320px;
  width: 100%;
  align-self: center;
}
.mobile-card {
  margin-bottom: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
  background: #fff;
}
.mobile-card .actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}
.info-card {
  margin-bottom: 20px;
}
.credit-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}
.info-item {
  padding: 8px 0;
}
.table-card {
  margin-bottom: 20px;
}
</style>