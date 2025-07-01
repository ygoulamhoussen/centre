<script setup lang="ts">
// @ts-expect-error - definePage is a macro
import { definePage } from 'vue-router'
import { computed, h, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { NButton, NCard, NDataTable, NTabPane, NTabs, useMessage } from 'naive-ui'
import type { ChargeDTO } from '@/types/dto'
import { createCharge, createEcritureComptableCharge } from '@/service/api/charges-recettes'

const route = useRoute()
const router = useRouter()
const credit = ref<any>(null)
const echeancier = ref<any[]>([])
const message = useMessage()
const writingInProgress = ref<Record<number, boolean>>({})
const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL

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
      <NButton @click="goBack" quaternary>
        <template #icon>
          <Icon icon="material-symbols:arrow-back" />
        </template>
        Retour
      </NButton>
      <h1>Échéancier du Crédit</h1>
    </div>
    <NCard v-if="credit" title="Informations du crédit" class="info-card">
      <div class="credit-info">
        <div class="info-item"><strong>Intitulé :</strong> {{ credit.intitule }}</div>
        <div class="info-item"><strong>Montant :</strong> {{ credit.montant }} €</div>
        <div class="info-item"><strong>Durée :</strong> {{ credit.duree }} mois</div>
        <div class="info-item"><strong>Taux :</strong> {{ credit.taux }} %</div>
        <div class="info-item"><strong>Type :</strong> {{ credit.typeBien }}</div>
        <div class="info-item"><strong>Date de début :</strong> {{ formatDate(credit.dateDebut) }}</div>
        <div class="info-item"><strong>Propriété :</strong> {{ credit.proprieteNom }}</div>
      </div>
    </NCard>
    <NCard class="table-card">
      <NTabs type="line" animated>
        <NTabPane name="echeancier" tab="Échéancier">
          <NDataTable :columns="echeancierColumns" :data="echeancier" striped />
        </NTabPane>
        <NTabPane name="charges" tab="Charges Financières Annuelles">
          <NDataTable :columns="chargesAnnuellesColumns" :data="chargesAnnuelles" />
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.header-left h1 {
  margin: 0;
  color: #333;
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