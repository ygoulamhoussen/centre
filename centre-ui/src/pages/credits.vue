<template>
  <div class="credits-page">
    <div class="page-header">
      <h1>Gestion des Crédits</h1>
      <NButton type="primary" @click="navigateToCreate">
        <template #icon>
          <Icon icon="material-symbols:add" />
        </template>
        Nouveau Crédit
      </NButton>
    </div>
    <NDataTable
      :columns="columns"
      :data="credits"
      :loading="loading"
      :bordered="false"
      striped
    />
  </div>
</template>

<script setup lang="ts">
definePage({
  meta: {
    title: 'Crédits',
    icon: 'material-symbols:credit-card',
    order: 20,
  },
})
import { h, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { NButton, NDataTable, useMessage } from 'naive-ui'

// Types à adapter selon le backend
interface Credit {
  id: string
  intitule: string
  montant: number
  duree: number
  taux: number
  proprieteNom: string
}

const router = useRouter()
const message = useMessage()
const credits = ref<Credit[]>([])
const loading = ref(false)

function navigateToCreate() {
  router.push('/credits-create')
}

function viewEcheancier(credit: Credit) {
  if (!credit.id) {
    message.error('ID du crédit manquant')
    return
  }
  router.push(`/echeancier/${credit.id}`)
}

const columns = [
  { title: 'Intitulé', key: 'intitule', width: 200 },
  { title: 'Montant', key: 'montant', width: 120 },
  { title: 'Durée (mois)', key: 'duree', width: 120 },
  { title: 'Taux (%)', key: 'taux', width: 100 },
  { title: 'Propriété', key: 'proprieteNom', width: 150 },
  {
    title: 'Actions',
    key: 'actions',
    width: 120,
    render: (row: Credit) => h('div', { class: 'actions' }, [
      h(NButton, {
        size: 'small',
        quaternary: true,
        onClick: () => viewEcheancier(row),
      }, {
        icon: () => h(Icon, { icon: 'material-symbols:schedule' }),
      }),
    ]),
  },
]

onMounted(async () => {
  loading.value = true
  try {
    const response = await fetch('http://localhost:8080/api/getCreditsByUtilisateur/00000000-0000-0000-0000-000000000003')
    if (!response.ok) throw new Error('Erreur lors du chargement des crédits')
    const data = await response.json()
    credits.value = data.map((c: any) => ({
      id: c.id,
      intitule: c.banque ? `Crédit ${c.banque}` : 'Crédit',
      montant: c.montantEmprunte,
      duree: c.dureeMois,
      taux: c.tauxInteretAnnuel,
      proprieteNom: c.proprieteNom || '',
    }))
  } catch (e) {
    message.error('Erreur lors du chargement des crédits')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.credits-page {
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
.actions {
  display: flex;
  gap: 8px;
}
</style> 