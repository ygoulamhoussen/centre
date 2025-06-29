<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import type { DataTableColumns } from 'naive-ui'
import { NButton, NDataTable, NSelect, useMessage } from 'naive-ui'

import type { Immobilisation } from '@/types/immobilisation'
import { immobilisationApi } from '@/service/api/immobilisation'
import { CATEGORIE_FISCALE_LABELS, TYPE_IMMOBILISATION_LABELS } from '@/types/immobilisation-constants'
definePage({
  meta: {
    title: 'Immobilisations',
    icon: 'material-symbols:real-estate-agent',
    order: 7,
  },
})
const router = useRouter()
const message = useMessage()

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

// État
const immobilisations = ref<Immobilisation[]>([])
const proprietes = ref<any[]>([])
const loading = ref(false)

// Filtres
const filters = ref({
  proprieteId: null as string | null,
  typeImmobilisation: null as string | null,
  categorieFiscale: null as string | null,
})

// Navigation vers la page de création
function navigateToCreate() {
  router.push('/immobilisations-create')
}

// Options pour les filtres
const proprieteOptions = computed(() =>
  proprietes.value.map(p => ({
    label: p.nom,
    value: p.id,
  }))
)

const typeOptions = computed(() =>
  Object.entries(TYPE_IMMOBILISATION_LABELS).map(([value, label]) => ({
    label,
    value,
  }))
)

const categorieOptions = computed(() =>
  Object.entries(CATEGORIE_FISCALE_LABELS).map(([value, label]) => ({
    label,
    value,
  }))
)

// Filtrage des immobilisations
const filteredImmobilisations = computed(() => {
  let filtered = immobilisations.value

  if (filters.value.proprieteId) {
    filtered = filtered.filter(i => i.proprieteId === filters.value.proprieteId)
  }

  if (filters.value.typeImmobilisation) {
    filtered = filtered.filter(i => i.typeImmobilisation === filters.value.typeImmobilisation)
  }

  if (filters.value.categorieFiscale) {
    filtered = filtered.filter(i => i.categorieFiscale === filters.value.categorieFiscale)
  }

  return filtered
})

// Fonctions
async function loadImmobilisations() {
  loading.value = true
  try {
    immobilisations.value = await immobilisationApi.getImmobilisationsByUtilisateur('00000000-0000-0000-0000-000000000003')
  } catch (error) {
    message.error('Erreur lors du chargement des immobilisations')
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function loadProprietes() {
  try {
    const response = await fetch('http://localhost:8080/api/getProprietesByUtilisateur/00000000-0000-0000-0000-000000000003')
    proprietes.value = await response.json()
  } catch (error) {
    console.error('Erreur lors du chargement des propriétés:', error)
  }
}

function clearFilters() {
  filters.value = {
    proprieteId: null,
    typeImmobilisation: null,
    categorieFiscale: null,
  }
}

async function deleteImmobilisation(id: string) {
  try {
    await immobilisationApi.deleteImmobilisation(id)
    message.success('Immobilisation supprimée avec succès')
    await loadImmobilisations()
  } catch (error) {
    message.error('Erreur lors de la suppression de l\'immobilisation')
    console.error(error)
  }
}

function viewAmortissements(immobilisation: Immobilisation) {
  // Navigation vers la page des amortissements avec l'ID de l'immobilisation
  router.push(`/amortissements/${immobilisation.id}`)
}

// Colonnes du tableau des immobilisations
const columns: DataTableColumns<Immobilisation> = [
  {
    title: 'Intitulé',
    key: 'intitule',
    width: 200,
  },
  {
    title: 'Montant',
    key: 'montant',
    width: 120,
    render: row => formatCurrency(row.montant),
  },
  {
    title: 'Type',
    key: 'typeImmobilisation',
    width: 120,
    render: row => TYPE_IMMOBILISATION_LABELS[row.typeImmobilisation],
  },
  {
    title: 'Catégorie',
    key: 'categorieFiscale',
    width: 150,
    render: row => CATEGORIE_FISCALE_LABELS[row.categorieFiscale],
  },
  {
    title: 'Propriété',
    key: 'proprieteNom',
    width: 150,
  },
  {
    title: 'Date acquisition',
    key: 'dateAcquisition',
    width: 120,
    render: row => formatDate(row.dateAcquisition),
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 120,
    render: row => h('div', { class: 'actions' }, [
      h(NButton, {
        size: 'small',
        quaternary: true,
        onClick: () => viewAmortissements(row),
      }, { 
        icon: () => h(Icon, { icon: 'material-symbols:schedule' }),
      }),
      h(NButton, {
        size: 'small',
        quaternary: true,
        type: 'error',
        onClick: () => deleteImmobilisation(row.id),
      }, { 
        icon: () => h(Icon, { icon: 'material-symbols:delete-outline' }),
      }),
    ]),
  },
]

// Pagination
const pagination = {
  page: 1,
  pageSize: 10,
  showSizePicker: true,
  pageSizes: [10, 20, 30, 40],
  onChange: (page: number) => {
    pagination.page = page
  },
  onUpdatePageSize: (pageSize: number) => {
    pagination.pageSize = pageSize
    pagination.page = 1
  },
}

// Initialisation
onMounted(async () => {
  await Promise.all([loadImmobilisations(), loadProprietes()])
})
</script>

<template>
  <div class="immobilisations-page">
    <div class="page-header">
      <h1>Gestion des Immobilisations</h1>
      <NButton type="primary" @click="navigateToCreate">
        <template #icon>
          <Icon icon="material-symbols:add" />
        </template>
        Nouvelle Immobilisation
      </NButton>
    </div>

    <!-- Filtres -->
    <div class="filters">
      <NSelect
        v-model:value="filters.proprieteId"
        :options="proprieteOptions"
        placeholder="Filtrer par propriété"
        clearable
        style="width: 200px"
      />
      <NSelect
        v-model:value="filters.typeImmobilisation"
        :options="typeOptions"
        placeholder="Filtrer par type"
        clearable
        style="width: 200px"
      />
      <NSelect
        v-model:value="filters.categorieFiscale"
        :options="categorieOptions"
        placeholder="Filtrer par catégorie"
        clearable
        style="width: 200px"
      />
      <NButton @click="clearFilters">Effacer les filtres</NButton>
    </div>

    <!-- Tableau des immobilisations -->
    <NDataTable
      :columns="columns"
      :data="filteredImmobilisations"
      :pagination="pagination"
      :loading="loading"
      :bordered="false"
      striped
    />
  </div>
</template>

<style scoped>
.immobilisations-page {
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
  color: #333;
}

.filters {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 40px;
}

.actions {
  display: flex;
  gap: 8px;
}
</style> 