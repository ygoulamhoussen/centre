<template>
  <div class="suivi-immobilisations">
    <n-card title="📄 Formulaire 2033-C — Suivi des immobilisations et amortissements" class="mb-4">
      <div :class="['header-row', isMobile ? 'header-row-mobile' : 'header-row-desktop']">
        <div class="flex items-center gap-4">
          <n-select
            v-model:value="selectedYear"
            :options="yearOptions"
            placeholder="Sélectionner l'année"
            style="width: 150px"
            @update:value="loadData"
          />
        </div>
        <div class="text-sm text-gray-600">
          <p><strong>Année fiscale :</strong> {{ selectedYear }}</p>
        </div>
      </div>

      <n-alert v-if="loading" type="info" class="mb-4">
        <template #icon>
          <n-spin size="small" />
        </template>
        Chargement des données...
      </n-alert>

      <n-alert v-if="error" type="error" class="mb-4">
        {{ error }}
      </n-alert>

      <div v-if="suiviData" class="space-y-6">
        <!-- Tableau des immobilisations -->
        <n-card title="Détail des immobilisations" size="small">
          <div v-if="!isMobile">
            <n-table :bordered="false" :single-line="false" size="small">
              <thead>
                <tr>
                  <th class="text-left">Nature de l'immobilisation</th>
                  <th class="text-center">Date d'acquisition</th>
                  <th class="text-right">Montant HT</th>
                  <th class="text-right">Base amortissable</th>
                  <th class="text-center">Durée (ans)</th>
                  <th class="text-center">Taux (%)</th>
                  <th class="text-right">Cumul antérieur</th>
                  <th class="text-right">Dotation exercice</th>
                  <th class="text-right">Amort. exceptionnel</th>
                  <th class="text-right">Cumul clôture</th>
                  <th class="text-right">VNC</th>
                  <th class="text-left">Propriété</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="immo in suiviData.immobilisations" :key="immo.id">
                  <td class="font-medium">{{ immo.nature }}</td>
                  <td class="text-center">{{ formatDate(immo.dateAcquisition) }}</td>
                  <td class="text-right">{{ formatCurrency(immo.montantHT) }}</td>
                  <td class="text-right">{{ formatCurrency(immo.baseAmortissable) }}</td>
                  <td class="text-center">{{ immo.dureeAmortissement }}</td>
                  <td class="text-center">{{ formatPercentage(immo.tauxAmortissement) }}</td>
                  <td class="text-right">{{ formatCurrency(immo.cumulAmortissementsAnterieurs) }}</td>
                  <td class="text-right font-medium text-blue-600">{{ formatCurrency(immo.dotationExercice) }}</td>
                  <td class="text-right">{{ formatCurrency(immo.amortissementExceptionnel) }}</td>
                  <td class="text-right">{{ formatCurrency(immo.cumulAmortissementsCloture) }}</td>
                  <td class="text-right font-bold text-green-600">{{ formatCurrency(immo.valeurNetteComptable) }}</td>
                  <td class="text-sm text-gray-600">{{ immo.propriete }}</td>
                </tr>
              </tbody>
              <tfoot>
                <tr class="font-bold bg-gray-50">
                  <td colspan="3" class="text-right">TOTAUX :</td>
                  <td class="text-right">{{ formatCurrency(suiviData.totalBaseAmortissable) }}</td>
                  <td></td>
                  <td></td>
                  <td class="text-right">{{ formatCurrency(suiviData.totalCumulAmortissementsAnterieurs) }}</td>
                  <td class="text-right text-blue-600">{{ formatCurrency(suiviData.totalDotationExercice) }}</td>
                  <td></td>
                  <td class="text-right">{{ formatCurrency(suiviData.totalCumulAmortissementsCloture) }}</td>
                  <td class="text-right text-green-600">{{ formatCurrency(suiviData.totalValeurNetteComptable) }}</td>
                  <td></td>
                </tr>
              </tfoot>
            </n-table>
          </div>
          <div v-else class="cards-mobile-list">
            <n-card v-for="immo in suiviData.immobilisations" :key="immo.id" class="mobile-detail-card mb-2" size="small">
              <div class="font-bold mb-1">{{ immo.nature }}</div>
              <div class="text-xs text-gray-500 mb-1">Date d'acquisition : <b>{{ formatDate(immo.dateAcquisition) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Montant HT : <b>{{ formatCurrency(immo.montantHT) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Base amortissable : <b>{{ formatCurrency(immo.baseAmortissable) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Durée : <b>{{ immo.dureeAmortissement }}</b> ans</div>
              <div class="text-xs text-gray-500 mb-1">Taux : <b>{{ formatPercentage(immo.tauxAmortissement) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Cumul antérieur : <b>{{ formatCurrency(immo.cumulAmortissementsAnterieurs) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Dotation exercice : <b class="text-blue-600">{{ formatCurrency(immo.dotationExercice) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Amort. exceptionnel : <b>{{ formatCurrency(immo.amortissementExceptionnel) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Cumul clôture : <b>{{ formatCurrency(immo.cumulAmortissementsCloture) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">VNC : <b class="text-green-600">{{ formatCurrency(immo.valeurNetteComptable) }}</b></div>
              <div class="text-xs text-gray-500 mb-1">Propriété : <b>{{ immo.propriete }}</b></div>
            </n-card>
          </div>
        </n-card>

        <!-- Résumé -->
        <div v-if="!isMobile">
          <n-card title="Résumé de l'exercice" size="small">
            <n-grid :cols="4" :x-gap="12" :y-gap="8">
              <n-grid-item>
                <n-statistic label="Base amortissable totale" :value="formatCurrency(suiviData.totalBaseAmortissable)" />
              </n-grid-item>
              <n-grid-item>
                <n-statistic label="Dotations de l'exercice" :value="formatCurrency(suiviData.totalDotationExercice)" />
              </n-grid-item>
              <n-grid-item>
                <n-statistic label="Cumul des amortissements" :value="formatCurrency(suiviData.totalCumulAmortissementsCloture)" />
              </n-grid-item>
              <n-grid-item>
                <n-statistic label="Valeur nette comptable" :value="formatCurrency(suiviData.totalValeurNetteComptable)" />
              </n-grid-item>
            </n-grid>
          </n-card>
        </div>
        <div v-else class="summary-cards-mobile">
          <n-card class="summary-mobile-card mb-2"><n-statistic label="Base amortissable totale" :value="formatCurrency(suiviData.totalBaseAmortissable)" /></n-card>
          <n-card class="summary-mobile-card mb-2"><n-statistic label="Dotations de l'exercice" :value="formatCurrency(suiviData.totalDotationExercice)" /></n-card>
          <n-card class="summary-mobile-card mb-2"><n-statistic label="Cumul des amortissements" :value="formatCurrency(suiviData.totalCumulAmortissementsCloture)" /></n-card>
          <n-card class="summary-mobile-card mb-2"><n-statistic label="Valeur nette comptable" :value="formatCurrency(suiviData.totalValeurNetteComptable)" /></n-card>
        </div>

        <!-- Informations complémentaires -->
        <n-card title="Informations complémentaires" size="small">
          <n-space vertical>
            <n-alert type="info">
              <template #icon>
                <n-icon><InformationCircleOutline /></n-icon>
              </template>
              <div class="space-y-2">
                <p><strong>Base amortissable :</strong> Montant de l'immobilisation hors valeur terrain (non amortissable)</p>
                <p><strong>Taux d'amortissement :</strong> Calculé selon la méthode linéaire (100% / durée)</p>
                <p><strong>VNC :</strong> Valeur Nette Comptable = Base amortissable - Cumul des amortissements</p>
                <p><strong>Amortissement exceptionnel :</strong> Rare en LMNP, généralement à 0</p>
              </div>
            </n-alert>
          </n-space>
        </n-card>
      </div>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useMessage } from 'naive-ui'
import { InformationCircleOutline } from '@vicons/ionicons5'
import { useWindowSize } from '@vueuse/core'
import { suiviImmobilisationsApi } from '@/service/api/immobilisation'
import { useAuthStore } from '@/store/modules/auth'

definePage({
  meta: {
    title: '2033-C - Suivi des immobilisations et amortissements',
    hideInMenu: true,
    activeMenu: '/liasse-fiscale',
  },
})

const message = useMessage()
const authStore = useAuthStore()

// Données réactives
const selectedYear = ref(new Date().getFullYear())
const loading = ref(false)
const error = ref('')
const suiviData = ref<any>(null)

// Options pour les années
const yearOptions = Array.from({ length: 10 }, (_, i) => ({
  label: (new Date().getFullYear() - 5 + i).toString(),
  value: new Date().getFullYear() - 5 + i
}))

// Chargement des données
const loadData = async () => {
  if (!selectedYear.value) return
  
  loading.value = true
  error.value = ''
  
  try {
    // Récupérer l'utilisateur connecté depuis le store
    const utilisateurId = authStore.userInfo?.userId
    
    if (!utilisateurId) {
      throw new Error('Utilisateur non connecté')
    }
    
    const response = await suiviImmobilisationsApi(selectedYear.value, utilisateurId)
    suiviData.value = response
    
    message.success('Données chargées avec succès')
  } catch (err) {
    console.error('Erreur lors du chargement des données:', err)
    error.value = 'Erreur lors du chargement des données'
    message.error('Erreur lors du chargement des données')
  } finally {
    loading.value = false
  }
}

// Formatage des données
const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('fr-FR')
}

const formatCurrency = (amount: number) => {
  if (amount === null || amount === undefined) return '0,00 €'
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2
  }).format(amount)
}

const formatPercentage = (rate: number) => {
  if (rate === null || rate === undefined) return '0%'
  return `${rate.toFixed(1)}%`
}

// Chargement initial
onMounted(() => {
  loadData()
})

const { width } = useWindowSize()
const isMobile = computed(() => width.value <= 640)
</script>

<style scoped>
.suivi-immobilisations {
  padding: 20px;
}

.n-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #374151;
}

.n-table td {
  padding: 8px 12px;
}

.n-table tfoot tr {
  border-top: 2px solid #e5e7eb;
}
.cards-mobile-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.mobile-detail-card {
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  border-radius: 10px;
  padding: 12px 10px;
}
@media (max-width: 640px) {
  .mobile-detail-card {
    font-size: 0.97rem;
    padding: 10px 6px;
  }
}
.header-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}
.header-row-mobile {
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
}
.summary-cards-mobile {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 18px;
}
.summary-mobile-card {
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  border-radius: 10px;
  padding: 10px 8px;
}
@media (max-width: 640px) {
  .header-row {
    gap: 8px;
  }
}
</style> 