<template>
  <div class="repartition-charges-container">
    <n-card title="📊 Formulaire 2033-E — Répartition des charges" class="main-card">
      <template #header-extra>
        <n-space>
          <n-select
            v-model:value="selectedYear"
            :options="yearOptions"
            placeholder="Sélectionner l'année"
            style="width: 120px"
            @update:value="loadRepartitionCharges"
          />
          <n-button type="primary" @click="loadRepartitionCharges" :loading="loading">
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
            Calcul de la répartition des charges...
          </template>
        </n-spin>
      </div>

      <div v-else-if="repartitionCharges" class="repartition-content">
        <!-- Résumé des totaux -->
        <n-card title="📈 Résumé des charges par catégorie" class="summary-card">
          <n-grid :cols="4" :x-gap="16" :y-gap="16">
            <n-grid-item>
              <n-statistic label="Achats" :value="formatCurrency(repartitionCharges.achats)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Autres charges externes" :value="formatCurrency(repartitionCharges.autresChargesExternes)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Impôts et taxes" :value="formatCurrency(repartitionCharges.impotsTaxes)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Charges de personnel" :value="formatCurrency(repartitionCharges.chargesPersonnel)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Charges sociales" :value="formatCurrency(repartitionCharges.chargesSociales)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Dotations amortissements" :value="formatCurrency(repartitionCharges.dotationsAmortissements)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Autres charges" :value="formatCurrency(repartitionCharges.autresCharges)" />
            </n-grid-item>
            <n-grid-item>
              <n-statistic label="Total des charges" :value="formatCurrency(repartitionCharges.totalCharges)" class="total-statistic" />
            </n-grid-item>
          </n-grid>
        </n-card>

        <!-- Détails par catégorie -->
        <n-space vertical size="large" class="details-section">
          
          <!-- Achats -->
          <n-card title="🛒 Achats (matières, fournitures, petit matériel)" v-if="repartitionCharges.achats > 0">
            <template #header-extra>
              <n-tag type="info">{{ formatCurrency(repartitionCharges.achats) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsAchats" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Autres charges externes -->
          <n-card title="🔧 Autres charges externes" v-if="repartitionCharges.autresChargesExternes > 0">
            <template #header-extra>
              <n-tag type="warning">{{ formatCurrency(repartitionCharges.autresChargesExternes) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsChargesExternes" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Impôts et taxes -->
          <n-card title="🏛️ Impôts et taxes" v-if="repartitionCharges.impotsTaxes > 0">
            <template #header-extra>
              <n-tag type="error">{{ formatCurrency(repartitionCharges.impotsTaxes) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsImpotsTaxes" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Charges de personnel -->
          <n-card title="👥 Charges de personnel" v-if="repartitionCharges.chargesPersonnel > 0">
            <template #header-extra>
              <n-tag type="success">{{ formatCurrency(repartitionCharges.chargesPersonnel) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsChargesPersonnel" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Charges sociales -->
          <n-card title="🏥 Charges sociales de l'exploitant" v-if="repartitionCharges.chargesSociales > 0">
            <template #header-extra>
              <n-tag type="warning">{{ formatCurrency(repartitionCharges.chargesSociales) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsChargesSociales" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Dotations aux amortissements -->
          <n-card title="📉 Dotations aux amortissements" v-if="repartitionCharges.dotationsAmortissements > 0">
            <template #header-extra>
              <n-tag type="info">{{ formatCurrency(repartitionCharges.dotationsAmortissements) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsAmortissements" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Autres charges -->
          <n-card title="💼 Autres charges" v-if="repartitionCharges.autresCharges > 0">
            <template #header-extra>
              <n-tag type="default">{{ formatCurrency(repartitionCharges.autresCharges) }}</n-tag>
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
                <tr v-for="detail in repartitionCharges.detailsAutresCharges" :key="detail.libelle + detail.dateCharge">
                  <td>{{ formatDate(detail.dateCharge) }}</td>
                  <td>{{ detail.libelle }}</td>
                  <td><n-tag size="small">{{ detail.compteComptable }}</n-tag></td>
                  <td>{{ formatCurrency(detail.montant) }}</td>
                  <td>{{ detail.commentaire }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>

          <!-- Message si aucune charge -->
          <n-empty v-if="repartitionCharges.totalCharges === 0" description="Aucune charge trouvée pour cette année">
            <template #icon>
              <DocumentTextOutline />
            </template>
          </n-empty>
        </n-space>
      </div>

      <div v-else-if="error" class="error-container">
        <n-result status="error" title="Erreur lors du chargement" :description="error">
          <template #footer>
            <n-button type="primary" @click="loadRepartitionCharges">
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
import { RefreshOutline, DocumentTextOutline } from '@vicons/ionicons5'
import { repartitionChargesApi, type RepartitionChargesDTO } from '@/service/api/repartition-charges'
import { useAuthStore } from '@/store/modules/auth'

const message = useMessage()
const authStore = useAuthStore()

// État réactif
const loading = ref(false)
const error = ref('')
const repartitionCharges = ref<RepartitionChargesDTO | null>(null)
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
const loadRepartitionCharges = async () => {
  if (!authStore.userInfo?.userId) {
    message.error('Utilisateur non connecté')
    return
  }

  loading.value = true
  error.value = ''

  try {
    const response = await repartitionChargesApi.getRepartitionCharges(selectedYear.value, authStore.userInfo.userId)
    repartitionCharges.value = response
    message.success('Répartition des charges chargée avec succès')
  } catch (err) {
    console.error('Erreur lors du chargement de la répartition des charges:', err)
    error.value = 'Erreur lors du chargement des données'
    message.error('Erreur lors du chargement de la répartition des charges')
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
  loadRepartitionCharges()
})
</script>

<style scoped>
.repartition-charges-container {
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

.repartition-content {
  margin-top: 20px;
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