<script setup lang="ts">
import { h, onMounted, ref } from 'vue'
import { Icon } from '@iconify/vue'
import { useRouter } from 'vue-router'
import { NButton, NDataTable, NPopconfirm, NSpace, useMessage, NCard, NH1 } from 'naive-ui'
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

// Suppression de la définition des colonnes car on utilise des cards maintenant

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
    <div v-if="!isMobile" class="flex items-center justify-between">
      <NH1 class="titre-principal">Gestion des Crédits</NH1>
      <NButton type="primary" @click="navigateToCreate">
        <template #icon>
          <Icon icon="material-symbols:add" />
        </template>
        Nouveau Crédit
      </NButton>
    </div>
    <div v-else class="mobile-header">
      <NH1 class="titre-principal mobile-title">Crédits</NH1>
      <NButton block size="small" type="primary" class="mobile-journal-btn" @click="navigateToCreate">
        <template #icon>
          <Icon icon="material-symbols:add" />
        </template>
        Nouveau Crédit
      </NButton>
    </div>
    <div v-if="!isMobile" class="desktop-cards">
      <NCard 
        v-for="credit in credits" 
        :key="credit.id" 
        class="credit-card clickable-card" 
        @click="viewEcheancier(credit)"
      >
        <div class="card-header">
          <div class="credit-info">
            <div class="credit-title">{{ credit.intitule }}</div>
            <div class="credit-amount">{{ credit.montant.toLocaleString('fr-FR') }} €</div>
            <div class="credit-details">
              <span class="detail-item">{{ credit.duree }} mois</span>
              <span class="detail-item">{{ credit.taux }}%</span>
            </div>
            <div class="credit-property">{{ credit.proprieteNom }}</div>
          </div>
          <NPopconfirm @positive-click="deleteCredit(credit)">
            <template #trigger>
              <NButton 
                size="small" 
                quaternary 
                type="error" 
                class="delete-btn"
                @click.stop
              >
                <Icon icon="material-symbols:delete-outline" />
              </NButton>
            </template>
            Êtes-vous sûr de vouloir supprimer ce crédit et toutes ses échéances ?
          </NPopconfirm>
        </div>
        <div class="edit-hint">
          <Icon icon="material-symbols:schedule" />
          <span>Cliquez pour voir l'échéancier</span>
        </div>
      </NCard>
    </div>
    <div v-else>
      <NCard 
        v-for="credit in credits" 
        :key="credit.id" 
        class="mobile-card clickable-card" 
        @click="viewEcheancier(credit)"
      >
        <div class="card-header">
          <div class="credit-info">
            <div class="credit-title">{{ credit.intitule }}</div>
            <div class="credit-amount">{{ credit.montant.toLocaleString('fr-FR') }} €</div>
            <div class="credit-details">
              <span class="detail-item">{{ credit.duree }} mois</span>
              <span class="detail-item">{{ credit.taux }}%</span>
            </div>
            <div class="credit-property">{{ credit.proprieteNom }}</div>
          </div>
          <NPopconfirm @positive-click="deleteCredit(credit)">
            <template #trigger>
              <NButton 
                size="small" 
                quaternary 
                type="error" 
                class="delete-btn"
                @click.stop
              >
                <Icon icon="material-symbols:delete-outline" />
              </NButton>
            </template>
            Êtes-vous sûr de vouloir supprimer ce crédit et toutes ses échéances ?
          </NPopconfirm>
        </div>
        <div class="edit-hint">
          <Icon icon="material-symbols:schedule" />
          <span>Cliquez pour voir l'échéancier</span>
        </div>
      </NCard>
    </div>
  </div>
</template>

<style scoped>
.credits-page {
  padding: 20px;
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
.actions {
  display: flex;
  gap: 8px;
}

/* Styles pour les cards cliquables */
.clickable-card {
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #e0e0e0;
}

.clickable-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #1890ff;
}

/* Layout pour desktop */
.desktop-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.credit-card {
  min-height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.delete-btn {
  flex-shrink: 0;
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.delete-btn:hover {
  opacity: 1;
}

.credit-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.credit-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1890ff;
}

.credit-amount {
  font-size: 1.2rem;
  font-weight: bold;
  color: #52c41a;
}

.credit-details {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.detail-item {
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #666;
}

.credit-property {
  font-size: 0.9rem;
  color: #666;
  font-style: italic;
}

.edit-hint {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
  font-size: 0.8rem;
  color: #999;
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
}
</style> 