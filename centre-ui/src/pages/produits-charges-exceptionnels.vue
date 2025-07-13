<template>
  <div class="produits-charges-exceptionnels-container">
    <n-card title="📄 Formulaire 2033-F — Produits et charges exceptionnels" class="main-card">
      <template #header-extra>
        <n-space>
          <n-select
            v-model:value="selectedYear"
            :options="yearOptions"
            placeholder="Sélectionner l'année"
            style="width: 120px"
            @update:value="loadProduitsChargesExceptionnels"
          />
          <n-button type="primary" @click="loadProduitsChargesExceptionnels" :loading="loading">
            <template #icon>
              <RefreshOutline />
            </template>
            Actualiser
          </n-button>
        </n-space>
      </template>

      <div v-if="loading" class="loading-container">
        <n-spin size="large">
          <template #description>
            Calcul des opérations exceptionnelles...
          </template>
        </n-spin>
      </div>

      <div v-else-if="produitsChargesExceptionnels" class="exceptionnels-content">
        <!-- Résumé des totaux -->
        <n-card title="📈 Résumé des opérations exceptionnelles" class="summary-card">
          <n-grid :cols="4" :x-gap="16" :y-gap="16">
            <n-grid-item>
              <n-statistic label="Produits exceptionnels" :value="formatCurrency(produitsChargesExceptionnels.produitsExceptionnels)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Charges exceptionnelles" :value="formatCurrency(produitsChargesExceptionnels.chargesExceptionnelles)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Plus-values à long terme" :value="formatCurrency(produitsChargesExceptionnels.plusValuesLongTerme)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Moins-values à long terme" :value="formatCurrency(produitsChargesExceptionnels.moinsValuesLongTerme)" />
            </n-grid-item>
            <n-grid-item :span="4">
              <n-divider />
              <n-statistic label="Total des opérations exceptionnelles" :value="formatCurrency(produitsChargesExceptionnels.totalOperationsExceptionnelles)" class="total-statistic" />
            </n-grid-item>
          </n-grid>
        </n-card>

        <!-- Détails par catégorie -->
        <n-space vertical size="large" class="details-section">
          
          <!-- Produits exceptionnels -->
          <n-card title="💰 Produits exceptionnels" v-if="produitsChargesExceptionnels.produitsExceptionnels > 0">
            <template #header-extra>
              <n-tag type="success">{{ formatCurrency(produitsChargesExceptionnels.produitsExceptionnels) }}</n-tag>
            </template>
            <n-table :bordered="false" :single-line="false" size="small">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Libellé</th>
                  <th>Compte</th>
                  <th>Montant</th>
                  <th>Commentaire</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="detail in produitsChargesExceptionnels.detailsProduitsExceptionnels" :key="detail.libelle + detail.dateOperation">
                  <td>{{ formatDate(detail.dateOperation) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Charges exceptionnelles -->
          <n-card title="💸 Charges exceptionnelles" v-if="produitsChargesExceptionnels.chargesExceptionnelles > 0">
            <template #header-extra>
              <n-tag type="error">{{ formatCurrency(produitsChargesExceptionnels.chargesExceptionnelles) }}</n-tag>
            </template>
            <n-table :bordered="false" :single-line="false" size="small">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Libellé</th>
                  <th>Compte</th>
                  <th>Montant</th>
                  <th>Commentaire</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="detail in produitsChargesExceptionnels.detailsChargesExceptionnelles" :key="detail.libelle + detail.dateOperation">
                  <td>{{ formatDate(detail.dateOperation) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Plus-values à long terme -->
          <n-card title="📈 Plus-values à long terme" v-if="produitsChargesExceptionnels.plusValuesLongTerme > 0">
            <template #header-extra>
              <n-tag type="success">{{ formatCurrency(produitsChargesExceptionnels.plusValuesLongTerme) }}</n-tag>
            </template>
            <n-table :bordered="false" :single-line="false" size="small">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Libellé</th>
                  <th>Compte</th>
                  <th>Montant</th>
                  <th>Commentaire</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="detail in produitsChargesExceptionnels.detailsPlusValues" :key="detail.libelle + detail.dateOperation">
                  <td>{{ formatDate(detail.dateOperation) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Moins-values à long terme -->
          <n-card title="📉 Moins-values à long terme" v-if="produitsChargesExceptionnels.moinsValuesLongTerme > 0">
            <template #header-extra>
              <n-tag type="error">{{ formatCurrency(produitsChargesExceptionnels.moinsValuesLongTerme) }}</n-tag>
            </template>
            <n-table :bordered="false" :single-line="false" size="small">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Libellé</th>
                  <th>Compte</th>
                  <th>Montant</th>
                  <th>Commentaire</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="detail in produitsChargesExceptionnels.detailsMoinsValues" :key="detail.libelle + detail.dateOperation">
                  <td>{{ formatDate(detail.dateOperation) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Message si aucune opération exceptionnelle -->
          <n-empty v-if="produitsChargesExceptionnels.totalOperationsExceptionnelles === 0" description="Aucune opération exceptionnelle trouvée pour cette année">
            <template #icon>
              <DocumentTextOutline />
            </template>
            <template #description>
              <div class="empty-description">
                <p>Aucune opération exceptionnelle trouvée pour cette année.</p>
                <p class="text-sm text-gray-500">
                  En LMNP, ce formulaire est souvent vide mais doit être inclus dans la liasse fiscale.
                </p>
              </div>
            </template>
          </n-empty>
        </n-space>

        <!-- Informations complémentaires -->
        <n-card title="ℹ️ Informations sur les opérations exceptionnelles" size="small" class="mt-6">
          <n-alert type="info">
            <template #icon>
              <n-icon><InformationCircleOutline /></n-icon>
            </template>
            <div class="space-y-2">
              <p><strong>Opérations exceptionnelles en LMNP :</strong></p>
              <ul class="list-disc list-inside space-y-1 text-sm">
                <li><strong>Produits exceptionnels :</strong> Vente d'un bien, indemnité d'assurance, etc.</li>
                <li><strong>Charges exceptionnelles :</strong> Pénalité, redressement, don, dépréciation, etc.</li>
                <li><strong>Plus-values à long terme :</strong> Vente d'un bien immobilier avec plus-value</li>
                <li><strong>Moins-values à long terme :</strong> Vente d'un bien immobilier avec moins-value</li>
                <li>Ce formulaire est souvent vide en LMNP mais doit être inclus dans la liasse fiscale</li>
              </ul>
            </div>
          </n-alert>
        </n-card>
      </div>

      <div v-else-if="error" class="error-container">
        <n-result status="error" title="Erreur lors du chargement" :description="error">
          <template #footer>
            <n-button type="primary" @click="loadProduitsChargesExceptionnels">
              Réessayer
            </n-button>
          </template>
        </n-result>
      </div>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useMessage } from 'naive-ui'
import { RefreshOutline, DocumentTextOutline, InformationCircleOutline } from '@vicons/ionicons5'
import { produitsChargesExceptionnelsApi, type ProduitsChargesExceptionnelsDTO } from '@/service/api/produits-charges-exceptionnels'
import { useAuthStore } from '@/store/modules/auth'

const message = useMessage()
const authStore = useAuthStore()

// État réactif
const loading = ref(false)
const error = ref('')
const produitsChargesExceptionnels = ref<ProduitsChargesExceptionnelsDTO | null>(null)
const selectedYear = ref(new Date().getFullYear())

// Options pour les années
const yearOptions = computed(() => {
  const currentYear = new Date().getFullYear()
  const options = []
  for (let year = currentYear; year >= currentYear - 10; year--) {
    options.push({
      label: year.toString(),
      value: year
    })
  }
  return options
})

// Méthodes
const loadProduitsChargesExceptionnels = async () => {
  if (!authStore.userInfo?.userId) {
    message.error('Utilisateur non connecté')
    return
  }

  loading.value = true
  error.value = ''

  try {
    const response = await produitsChargesExceptionnelsApi.getProduitsChargesExceptionnels(selectedYear.value, authStore.userInfo.userId)
    produitsChargesExceptionnels.value = response
    message.success('Opérations exceptionnelles chargées avec succès')
  } catch (err) {
    console.error('Erreur lors du chargement des opérations exceptionnelles:', err)
    error.value = 'Erreur lors du chargement des données'
    message.error('Erreur lors du chargement des opérations exceptionnelles')
  } finally {
    loading.value = false
  }
}

const formatCurrency = (value: number) => {
  if (!value) return '0,00 €'
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR'
  }).format(value)
}

const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('fr-FR')
}

// Chargement initial
onMounted(() => {
  loadProduitsChargesExceptionnels()
})
</script>

<style scoped>
.produits-charges-exceptionnels-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.main-card {
  margin-bottom: 20px;
}

.summary-card {
  margin-bottom: 30px;
}

.total-statistic {
  font-weight: bold;
  color: #18a058;
}

.details-section {
  margin-top: 20px;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}

.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}

.exceptionnels-content {
  margin-top: 20px;
}

.empty-description {
  text-align: center;
}

:deep(.n-card-header) {
  border-bottom: 1px solid #f0f0f0;
}

:deep(.n-table th) {
  background-color: #fafafa;
  font-weight: 600;
}

:deep(.n-tag) {
  font-weight: 500;
}
</style> 