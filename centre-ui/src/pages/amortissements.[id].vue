<template>
  <div class="amortissements-page">
    <div class="page-header">
      <div class="header-left">
        <NButton @click="goBack" quaternary>
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Retour
        </NButton>
        <h1>Plan d'Amortissement</h1>
      </div>
    </div>

    <!-- Informations de l'immobilisation -->
    <NCard v-if="immobilisation" title="Informations de l'immobilisation" class="info-card">
      <div class="immobilisation-info">
        <div class="info-item">
          <strong>Intitulé :</strong> {{ immobilisation.intitule }}
        </div>
        <div class="info-item">
          <strong>Montant :</strong> {{ formatCurrency(immobilisation.montant) }}
        </div>
        <div class="info-item">
          <strong>Type :</strong> {{ TYPE_IMMOBILISATION_LABELS[immobilisation.typeImmobilisation] }}
        </div>
        <div class="info-item">
          <strong>Catégorie fiscale :</strong> {{ CATEGORIE_FISCALE_LABELS[immobilisation.categorieFiscale] }}
        </div>
        <div class="info-item">
          <strong>Date d'acquisition :</strong> {{ formatDate(immobilisation.dateAcquisition) }}
        </div>
        <div class="info-item">
          <strong>Durée d'amortissement :</strong> {{ immobilisation.dureeAmortissement }} ans
        </div>
      </div>
    </NCard>

    <!-- Tableau des amortissements -->
    <NCard title="Plan d'amortissement" class="table-card">
      <div v-if="loading" class="loading-container">
        <NSpin size="large" />
        <p>Chargement des amortissements...</p>
      </div>
      
      <NDataTable
        v-else
        :columns="columns"
        :data="amortissements"
        :bordered="false"
        striped
      />
    </NCard>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import type { DataTableColumns } from 'naive-ui'
import { 
  NButton, 
  NCard, 
  NDataTable, 
  NSpin, 
  useMessage 
} from 'naive-ui'

import type { Amortissement, Immobilisation } from '@/types/immobilisation'
import { amortissementApi, immobilisationApi } from '@/service/api/immobilisation'
import { CATEGORIE_FISCALE_LABELS, TYPE_IMMOBILISATION_LABELS } from '@/types/immobilisation-constants'

definePage({
  meta: {
    title: 'Plan d\'Amortissement',
    icon: 'material-symbols:schedule',
    order: 10,
    hideInMenu: true,
  },
})

const route = useRoute()
const router = useRouter()
const message = useMessage()

// État
const loading = ref(false)
const immobilisation = ref<Immobilisation | null>(null)
const amortissements = ref<Amortissement[]>([])

// Fonctions utilitaires
function formatCurrency(value: string | number) {
  const num = typeof value === 'string' ? Number.parseFloat(value) : value
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(num)
}

function formatDate(dateString: string) {
  return new Date(dateString).toLocaleDateString('fr-FR')
}

// Navigation
function goBack() {
  router.push('/immobilisations')
}

// Chargement des données
async function loadImmobilisation() {
  const immobilisationId = route.params.id as string
  try {
    // Récupérer l'immobilisation depuis la liste des immobilisations
    const allImmobilisations = await immobilisationApi.getImmobilisationsByUtilisateur('00000000-0000-0000-0000-000000000003')
    immobilisation.value = allImmobilisations.find(i => i.id === immobilisationId) || null
    
    if (!immobilisation.value) {
      message.error('Immobilisation introuvable')
      router.push('/immobilisations')
      return
    }
  } catch (error) {
    message.error('Erreur lors du chargement de l\'immobilisation')
    console.error(error)
    router.push('/immobilisations')
  }
}

async function loadAmortissements() {
  const immobilisationId = route.params.id as string
  loading.value = true
  try {
    amortissements.value = await amortissementApi.getAmortissementsByImmobilisation(immobilisationId)
  } catch (error) {
    message.error('Erreur lors du chargement des amortissements')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// Colonnes du tableau des amortissements
const columns: DataTableColumns<Amortissement> = [
  {
    title: 'Année',
    key: 'annee',
    width: 80,
  },
  {
    title: 'Montant amortissement',
    key: 'montantAmortissement',
    width: 150,
    render: row => formatCurrency(row.montantAmortissement),
  },
  {
    title: 'Valeur résiduelle',
    key: 'valeurResiduelle',
    width: 150,
    render: row => formatCurrency(row.valeurResiduelle),
  },
  {
    title: 'Cumul amortissements',
    key: 'cumulAmortissements',
    width: 150,
    render: row => formatCurrency(row.cumulAmortissements),
  },
  {
    title: 'Taux (%)',
    key: 'tauxAmortissement',
    width: 100,
    render: row => `${row.tauxAmortissement}%`,
  },
]

// Initialisation
onMounted(async () => {
  await Promise.all([loadImmobilisation(), loadAmortissements()])
})
</script>

<style scoped>
.amortissements-page {
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

.immobilisation-info {
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

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 40px;
}
</style> 