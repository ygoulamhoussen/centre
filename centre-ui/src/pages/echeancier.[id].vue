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
        <div class="info-item"><strong>Type :</strong> {{ credit.type }}</div>
        <div class="info-item"><strong>Date de début :</strong> {{ formatDate(credit.dateDebut) }}</div>
        <div class="info-item"><strong>Propriété :</strong> {{ credit.proprieteNom }}</div>
      </div>
    </NCard>
    <NCard title="Échéancier" class="table-card">
      <NDataTable :columns="echeancierColumns" :data="echeancier" striped />
    </NCard>
  </div>
</template>

<script setup lang="ts">
import { definePage } from 'vue-router'
import { h, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { NButton, NCard, NDataTable, useMessage } from 'naive-ui'

const route = useRoute()
const router = useRouter()
const credit = ref<any>(null)
const echeancier = ref<any[]>([])
const message = useMessage()

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
  if (!date) return ''
  const d = typeof date === 'number' ? new Date(date) : new Date(date)
  return d.toLocaleDateString('fr-FR')
}
onMounted(async () => {
  const id = route.params.id as string | undefined
  console.log('ID du crédit demandé pour échéancier:', id)
  if (!id) {
    message.error('ID du crédit manquant')
    console.error('ID du crédit manquant')
    return
  }
  try {
    // Charger le crédit (on récupère tous les crédits de l'utilisateur et on filtre)
    const creditRes = await fetch('http://localhost:8080/api/getCreditsByUtilisateur/00000000-0000-0000-0000-000000000003')
    if (!creditRes.ok) throw new Error('Erreur lors du chargement du crédit')
    const credits = await creditRes.json()
    credit.value = credits.find((c: any) => c.id === id) || null
    if (!credit.value) throw new Error('Crédit non trouvé')
    // Charger les échéances du crédit
    const echeancesRes = await fetch(`http://localhost:8080/api/getEcheancesByCredit/${id}`)
    if (!echeancesRes.ok) throw new Error('Erreur lors du chargement des échéances')
    echeancier.value = await echeancesRes.json()
  } catch (e: any) {
    message.error('Erreur lors du chargement du crédit ou des échéances : ' + (e?.message || e))
    console.error('Erreur lors du chargement du crédit ou des échéances', e)
  }
})

const echeancierColumns = [
  { title: 'N°', key: 'numero', width: 60, render: (_row, index) => index + 1 },
  { title: 'Date', key: 'dateEcheance', width: 120, render: row => formatDate(row.dateEcheance) },
  { title: 'Capital remboursé', key: 'capitalRembourse', width: 120 },
  { title: 'Intérêts', key: 'interet', width: 100 },
  { title: 'Assurance', key: 'assurance', width: 100 },
  { title: 'Total échéance', key: 'totalEcheance', width: 120 },
]
</script>

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