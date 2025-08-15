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
        <!-- Tableau d'évolution des immobilisations -->
        <n-card title="État des immobilisations" size="small">
          <div v-if="!isMobile">
            <n-table :bordered="false" :single-line="false" size="small">
                             <thead>
                 <tr>
                   <th class="text-left">Numéro de compte</th>
                   <th class="text-left">Libellé</th>
                   <th class="text-center">Valeur brute au début de l'exercice</th>
                   <th class="text-center">Augmentations</th>
                   <th class="text-center">Diminutions</th>
                   <th class="text-center">Valeur brute à la fin de l'exercice</th>
                 </tr>
               </thead>
              <tbody>
                                 <tr v-for="categorie in categoriesImmobilisations" :key="categorie.code">
                   <td class="font-medium text-center">{{ categorie.code }}</td>
                   <td class="font-medium">{{ categorie.nom }}</td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">{{ categorie.codeDebut }}</span>
                      <span class="montant">{{ formatCurrency(categorie.valeurDebut) }}</span>
                    </div>
                  </td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">{{ categorie.codeAugmentation }}</span>
                      <span class="montant">{{ formatCurrency(categorie.augmentations) }}</span>
                    </div>
                  </td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">{{ categorie.codeDiminution }}</span>
                      <span class="montant">{{ formatCurrency(categorie.diminutions) }}</span>
                    </div>
                  </td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">{{ categorie.codeFin }}</span>
                      <span class="montant">{{ formatCurrency(categorie.valeurFin) }}</span>
                    </div>
                  </td>
                </tr>
              </tbody>
                             <tfoot>
                 <tr class="font-bold bg-gray-50">
                   <td class="text-center">490-496</td>
                   <td>TOTAL</td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">490</span>
                      <span class="montant">{{ formatCurrency(totalValeurDebut) }}</span>
                    </div>
                  </td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">492</span>
                      <span class="montant">{{ formatCurrency(totalAugmentations) }}</span>
                    </div>
                  </td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">494</span>
                      <span class="montant">{{ formatCurrency(totalDiminutions) }}</span>
                    </div>
                  </td>
                  <td class="text-center">
                    <div class="code-montant">
                      <span class="code">496</span>
                      <span class="montant">{{ formatCurrency(totalValeurFin) }}</span>
                    </div>
                  </td>
                </tr>
              </tfoot>
            </n-table>
          </div>
          <div v-else class="cards-mobile-list">
                         <n-card v-for="categorie in categoriesImmobilisations" :key="categorie.code" class="mobile-detail-card mb-2" size="small">
               <div class="font-bold mb-2">{{ categorie.code }} - {{ categorie.nom }}</div>
              <div class="text-xs text-gray-500 mb-1">
                <b>Valeur brute au début :</b> {{ categorie.codeDebut }} {{ formatCurrency(categorie.valeurDebut) }}
              </div>
              <div class="text-xs text-gray-500 mb-1">
                <b>Augmentations :</b> {{ categorie.codeAugmentation }} {{ formatCurrency(categorie.augmentations) }}
              </div>
              <div class="text-xs text-gray-500 mb-1">
                <b>Diminutions :</b> {{ categorie.codeDiminution }} {{ formatCurrency(categorie.diminutions) }}
              </div>
              <div class="text-xs text-gray-500 mb-1">
                <b>Valeur brute à la fin :</b> {{ categorie.codeFin }} {{ formatCurrency(categorie.valeurFin) }}
              </div>
            </n-card>
          </div>
        </n-card>

        <!-- Tableau des immobilisations détaillé -->
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

// Calcul des catégories d'immobilisations
const categoriesImmobilisations = computed(() => {
  if (!suiviData.value?.immobilisations) return []
  
  const categories = [
    {
      nom: 'Terrains',
      code: '420',
      codeDebut: '420',
      codeAugmentation: '422',
      codeDiminution: '424',
      codeFin: '426',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Constructions',
      code: '430',
      codeDebut: '430',
      codeAugmentation: '432',
      codeDiminution: '434',
      codeFin: '436',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Installations techniques matériel et outillage industriels',
      code: '440',
      codeDebut: '440',
      codeAugmentation: '442',
      codeDiminution: '444',
      codeFin: '446',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Installations générales agencement divers',
      code: '450',
      codeDebut: '450',
      codeAugmentation: '452',
      codeDiminution: '454',
      codeFin: '456',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Autres immobilisations corporelles',
      code: '470',
      codeDebut: '470',
      codeAugmentation: '472',
      codeDiminution: '474',
      codeFin: '476',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Immobilisations financières',
      code: '480',
      codeDebut: '480',
      codeAugmentation: '482',
      codeDiminution: '484',
      codeFin: '486',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
  ]
  
  // Calculer les valeurs pour chaque catégorie
  suiviData.value.immobilisations.forEach((immo: any) => {
    const montantHT = immo.montantHT || 0
    const cumulAnterieur = immo.cumulAmortissementsAnterieurs || 0
    const dotationExercice = immo.dotationExercice || 0
    
    // Déterminer la catégorie selon la nature
    let categorieIndex = -1
    if (immo.nature.toLowerCase().includes('terrain')) {
      categorieIndex = 0 // Terrains
    } else if (immo.nature.toLowerCase().includes('construction') || immo.nature.toLowerCase().includes('bâtiment')) {
      categorieIndex = 1 // Constructions
    } else if (immo.nature.toLowerCase().includes('technique') || immo.nature.toLowerCase().includes('industriel')) {
      categorieIndex = 2 // Installations techniques
    } else if (immo.nature.toLowerCase().includes('général') || immo.nature.toLowerCase().includes('agencement')) {
      categorieIndex = 3 // Installations générales
    } else if (immo.nature.toLowerCase().includes('financier')) {
      categorieIndex = 5 // Immobilisations financières
    } else {
      categorieIndex = 4 // Autres immobilisations corporelles
    }
    
    if (categorieIndex >= 0) {
      // Valeur brute = montant HT (pas de déduction des amortissements)
      categories[categorieIndex].valeurDebut += montantHT
      // Les dotations d'amortissement sont des diminutions
      categories[categorieIndex].diminutions += dotationExercice
      // Valeur brute à la fin = valeur brute début - dotations
      categories[categorieIndex].valeurFin += montantHT - dotationExercice
      // Pas d'augmentations pour l'instant (à adapter selon les besoins)
    }
  })
  
  return categories
})

// Calcul des totaux
const totalValeurDebut = computed(() => {
  return categoriesImmobilisations.value.reduce((sum, cat) => sum + cat.valeurDebut, 0)
})

const totalAugmentations = computed(() => {
  return categoriesImmobilisations.value.reduce((sum, cat) => sum + cat.augmentations, 0)
})

const totalDiminutions = computed(() => {
  return categoriesImmobilisations.value.reduce((sum, cat) => sum + cat.diminutions, 0)
})

const totalValeurFin = computed(() => {
  return categoriesImmobilisations.value.reduce((sum, cat) => sum + cat.valeurFin, 0)
})
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

/* Styles pour le tableau d'état des immobilisations */
.code-montant {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.code-montant .code {
  font-weight: bold;
  color: #1e40af;
  font-size: 0.85em;
  background-color: #e0f2fe;
  padding: 2px 6px;
  border-radius: 4px;
  min-width: 40px;
  text-align: center;
}

.code-montant .montant {
  font-weight: 500;
  font-variant-numeric: tabular-nums;
  color: #374151;
}

.n-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #374151;
  text-align: center;
}

.n-table th:first-child {
  text-align: left;
}

.n-table td {
  padding: 8px 12px;
  vertical-align: middle;
}

.n-table tfoot tr {
  border-top: 2px solid #e5e7eb;
}

.n-table tfoot .code-montant .code {
  background-color: #d1fae5;
  color: #065f46;
}

.n-table tfoot .code-montant .montant {
  font-weight: bold;
  color: #065f46;
}
</style> 