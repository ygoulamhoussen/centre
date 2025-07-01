<script setup lang="ts">
import { h, onMounted, ref } from 'vue'
import { Icon } from '@iconify/vue'
import { useRouter } from 'vue-router'
import { NButton, NDataTable, NPopconfirm, NSpace, useMessage, NCard } from 'naive-ui'
import { getCreditsByUtilisateur, deleteCreditById } from '@/service/api/immobilisation'
import { useAuthStore } from '@/store/modules/auth'

definePage({
  meta: {
    title: 'Crédits',
    icon: 'material-symbols:credit-card',
    order: 8,
  },
})

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
const authStore = useAuthStore()
const credits = ref<Credit[]>([])
const loading = ref(false)
const isMobile = ref(false)

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

async function deleteCredit(credit: Credit) {
  try {
    await deleteCreditById(credit.id)
    message.success('Crédit supprimé avec succès')
    await loadCredits()
  } catch (error) {
    const errorMessage = error instanceof Error ? error.message : 'Erreur lors de la suppression du crédit'
    message.error(errorMessage)
  }
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
    render: (row: Credit) => h(NSpace, null, {
      default: () => [
        h(NButton, {
          size: 'small',
          quaternary: true,
          onClick: () => viewEcheancier(row),
        }, {
          icon: () => h(Icon, { icon: 'material-symbols:schedule' }),
        }),
        h(NPopconfirm, {
          onPositiveClick: () => deleteCredit(row),
        }, {
          trigger: () => h(NButton, {
            size: 'small',
            quaternary: true,
            type: 'error',
          }, {
            icon: () => h(Icon, { icon: 'material-symbols:delete-outline' }),
          }),
          default: () => 'Êtes-vous sûr de vouloir supprimer ce crédit et toutes ses échéances ?',
        }),
      ],
    }),
  },
]

async function loadCredits() {
  loading.value = true
  try {
    if (authStore.userInfo.userId) {
      const data = await getCreditsByUtilisateur(authStore.userInfo.userId)
      credits.value = data.map((c: any) => ({
        id: c.id,
        intitule: c.banque ? `Crédit ${c.banque}` : 'Crédit',
        montant: c.montantEmprunte,
        duree: c.dureeMois,
        taux: c.tauxInteretAnnuel,
        proprieteNom: c.proprieteNom || '',
      }))
    }
  } catch (e) {
    const errorMessage = e instanceof Error ? e.message : 'Erreur lors du chargement des crédits'
    message.error(errorMessage)
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await loadCredits()
})
</script>

<template>
  <div class="credits-page">
    <div class="page-header">
      <div v-if="!isMobile" class="page-header-row">
        <h1>Gestion des Crédits</h1>
        <NButton type="primary" @click="navigateToCreate">
          <template #icon>
            <Icon icon="material-symbols:add" />
          </template>
          Nouveau Crédit
        </NButton>
      </div>
      <div v-else class="mobile-header">
        <h1 class="mobile-title">Crédits</h1>
        <NButton block size="small" type="primary" class="mobile-journal-btn" @click="navigateToCreate">
          <template #icon>
            <Icon icon="material-symbols:add" />
          </template>
          Nouveau Crédit
        </NButton>
      </div>
    </div>
    <NDataTable
      v-if="!isMobile"
      :columns="columns"
      :data="credits"
      :loading="loading"
      :bordered="false"
      striped
    />
    <div v-else>
      <NCard v-for="credit in credits" :key="credit.id" class="mobile-card">
        <div><b>Intitulé :</b> {{ credit.intitule }}</div>
        <div><b>Montant :</b> {{ credit.montant }} €</div>
        <div><b>Durée :</b> {{ credit.duree }} mois</div>
        <div><b>Taux :</b> {{ credit.taux }} %</div>
        <div><b>Propriété :</b> {{ credit.proprieteNom }}</div>
        <div class="actions">
          <NButton size="small" @click="viewEcheancier(credit)">Échéancier</NButton>
          <NButton size="small" type="error" @click="deleteCredit(credit)">Supprimer</NButton>
        </div>
      </NCard>
    </div>
  </div>
</template>

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
.page-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
</style> 