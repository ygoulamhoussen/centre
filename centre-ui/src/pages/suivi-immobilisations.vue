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
                 <!-- Tableau d'évolution des amortissements -->
         <n-card title="État des amortissements" size="small">
           <div v-if="!isMobile">
             <n-data-table
               :columns="amortissementColumns"
               :data="categoriesImmobilisations"
               :pagination="false"
               :bordered="false"
               size="small"
               :row-class-name="getRowClassName"
             />
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
             <n-data-table
               :columns="detailColumns"
               :data="detailImmobilisations"
               :pagination="false"
               :bordered="false"
               size="small"
               :row-class-name="getDetailRowClassName"
             />
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
import { ref, onMounted, computed, h } from 'vue'
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
const yearOptions = Array.from({ length: 20 }, (_, i) => ({
  label: (new Date().getFullYear() - 10 + i).toString(),
  value: new Date().getFullYear() - 10 + i
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
  
  // Formatage personnalisé pour correspondre au tableau
  const formatted = new Intl.NumberFormat('fr-FR', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    useGrouping: true
  }).format(amount)
  
  return `${formatted} €`
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

// Calcul des catégories d'amortissements selon le tableau fourni
const categoriesImmobilisations = computed(() => {
  if (!suiviData.value?.immobilisations) return []
  
  const categories = [
    {
      nom: 'Immobilisations incorporelles',
      code: '500',
      codeDebut: '500',
      codeAugmentation: '502',
      codeDiminution: '504',
      codeFin: '506',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Terrains',
      code: '510',
      codeDebut: '510',
      codeAugmentation: '512',
      codeDiminution: '514',
      codeFin: '516',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Constructions',
      code: '520',
      codeDebut: '520',
      codeAugmentation: '522',
      codeDiminution: '524',
      codeFin: '526',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Installations techniques matériel et outillage industriels',
      code: '530',
      codeDebut: '530',
      codeAugmentation: '532',
      codeDiminution: '534',
      codeFin: '536',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Installations générales agencement divers',
      code: '540',
      codeDebut: '540',
      codeAugmentation: '542',
      codeDiminution: '544',
      codeFin: '546',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
    {
      nom: 'Autres immobilisations corporelles',
      code: '560',
      codeDebut: '560',
      codeAugmentation: '562',
      codeDiminution: '564',
      codeFin: '566',
      valeurDebut: 0,
      augmentations: 0,
      diminutions: 0,
      valeurFin: 0,
    },
  ]
  
  // Calculer les montants dynamiquement à partir des données du backend
  suiviData.value.immobilisations.forEach((immo: any) => {
    const nature = immo.nature.toLowerCase()
    const cumulAnterieurs = Number(immo.cumulAmortissementsAnterieurs) || 0
    const dotationExercice = Number(immo.dotationExercice) || 0
    const cumulCloture = Number(immo.cumulAmortissementsCloture) || 0
    
    // Catégoriser selon la nature de l'immobilisation
    if (nature.includes('terrain')) {
      // 510 - Terrains (non amortissables)
      categories[1].valeurDebut += cumulAnterieurs
      categories[1].augmentations += dotationExercice
      categories[1].valeurFin += cumulCloture
    } else if (nature.includes('construction') || nature.includes('gros œuvre') || nature.includes('structure')) {
      // 520 - Constructions
      categories[2].valeurDebut += cumulAnterieurs
      categories[2].augmentations += dotationExercice
      categories[2].valeurFin += cumulCloture
    } else if (nature.includes('installation') || nature.includes('technique') || nature.includes('façade') || nature.includes('étanchéité') || nature.includes('igt')) {
      // 530 - Installations techniques
      categories[3].valeurDebut += cumulAnterieurs
      categories[3].augmentations += dotationExercice
      categories[3].valeurFin += cumulCloture
    } else if (nature.includes('agencement') || nature.includes('cloisons') || nature.includes('chapes') || nature.includes('plateries') || nature.includes('revetement') || nature.includes('plomberie') || nature.includes('cablage')) {
      // 540 - Installations générales
      categories[4].valeurDebut += cumulAnterieurs
      categories[4].augmentations += dotationExercice
      categories[4].valeurFin += cumulCloture
    } else {
      // 560 - Autres immobilisations corporelles
      categories[5].valeurDebut += cumulAnterieurs
      categories[5].augmentations += dotationExercice
      categories[5].valeurFin += cumulCloture
    }
  })
  
  console.log('Catégories calculées:', categories)
  
  // Calculer les totaux
  const totalValeurDebut = categories.reduce((sum, cat) => sum + cat.valeurDebut, 0)
  const totalAugmentations = categories.reduce((sum, cat) => sum + cat.augmentations, 0)
  const totalDiminutions = categories.reduce((sum, cat) => sum + cat.diminutions, 0)
  const totalValeurFin = categories.reduce((sum, cat) => sum + cat.valeurFin, 0)
  
  // Ajouter la ligne de total
  const categoriesWithTotal = [...categories, {
    nom: 'TOTAL',
    code: '570-576',
    codeDebut: '570',
    codeAugmentation: '572',
    codeDiminution: '574',
    codeFin: '576',
    valeurDebut: totalValeurDebut,
    augmentations: totalAugmentations,
    diminutions: totalDiminutions,
    valeurFin: totalValeurFin,
  }]
  
  return categoriesWithTotal
})

// Configuration des colonnes pour NDataTable
const amortissementColumns = [
  {
    title: 'Numéro de compte',
    key: 'code',
    width: 120,
    align: 'center',
    className: 'font-medium'
  },
  {
    title: 'Libellé',
    key: 'nom',
    width: 300,
    align: 'left',
    className: 'font-medium'
  },
  {
    title: 'Code',
    key: 'codeDebut',
    width: 60,
    align: 'center',
    className: 'font-medium'
  },
  {
    title: 'Montant des amortissements au début de l\'exercice',
    key: 'valeurDebut',
    width: 180,
    align: 'center',
    render: (row: any) => formatCurrency(row.valeurDebut)
  },
  {
    title: 'Code',
    key: 'codeAugmentation',
    width: 60,
    align: 'center',
    className: 'font-medium'
  },
  {
    title: 'Augmentations (dotations de l\'exercice)',
    key: 'augmentations',
    width: 180,
    align: 'center',
    render: (row: any) => formatCurrency(row.augmentations)
  },
  {
    title: 'Code',
    key: 'codeDiminution',
    width: 60,
    align: 'center',
    className: 'font-medium'
  },
  {
    title: 'Diminutions (amortissements afférents aux éléments sortis de l\'actif et reprises)',
    key: 'diminutions',
    width: 180,
    align: 'center',
    render: (row: any) => formatCurrency(row.diminutions)
  },
  {
    title: 'Code',
    key: 'codeFin',
    width: 60,
    align: 'center',
    className: 'font-medium'
  },
  {
    title: 'Montant des amortissements à la fin de l\'exercice',
    key: 'valeurFin',
    width: 180,
    align: 'center',
    render: (row: any) => formatCurrency(row.valeurFin)
  }
]

// Fonction pour appliquer des classes CSS aux lignes
const getRowClassName = (row: any) => {
  if (row.code === '570-576') {
    return 'total-row'
  }
  return ''
}

// Données détaillées des immobilisations avec ligne de total
const detailImmobilisations = computed(() => {
  if (!suiviData.value?.immobilisations) return []
  
  const immobilisations = suiviData.value.immobilisations.map((immo: any) => ({
    id: immo.id,
    nature: immo.nature,
    dateAcquisition: immo.dateAcquisition,
    montantHT: Number(immo.montantHT) || 0,
    baseAmortissable: Number(immo.baseAmortissable) || 0,
    dureeAmortissement: immo.dureeAmortissement,
    tauxAmortissement: Number(immo.tauxAmortissement) || 0,
    cumulAmortissementsAnterieurs: Number(immo.cumulAmortissementsAnterieurs) || 0,
    dotationExercice: Number(immo.dotationExercice) || 0,
    amortissementExceptionnel: Number(immo.amortissementExceptionnel) || 0,
    cumulAmortissementsCloture: Number(immo.cumulAmortissementsCloture) || 0,
    valeurNetteComptable: Number(immo.valeurNetteComptable) || 0,
    propriete: immo.propriete,
  }))
  
  // Calculer les totaux
  const totalBaseAmortissable = immobilisations.reduce((sum, immo) => sum + immo.baseAmortissable, 0)
  const totalCumulAmortissementsAnterieurs = immobilisations.reduce((sum, immo) => sum + immo.cumulAmortissementsAnterieurs, 0)
  const totalDotationExercice = immobilisations.reduce((sum, immo) => sum + immo.dotationExercice, 0)
  const totalCumulAmortissementsCloture = immobilisations.reduce((sum, immo) => sum + immo.cumulAmortissementsCloture, 0)
  const totalValeurNetteComptable = immobilisations.reduce((sum, immo) => sum + immo.valeurNetteComptable, 0)
  
  // Ajouter la ligne de total
  const immobilisationsWithTotal = [...immobilisations, {
    id: 'total',
    nature: 'TOTAUX',
    dateAcquisition: '',
    montantHT: 0,
    baseAmortissable: totalBaseAmortissable,
    dureeAmortissement: '',
    tauxAmortissement: 0,
    cumulAmortissementsAnterieurs: totalCumulAmortissementsAnterieurs,
    dotationExercice: totalDotationExercice,
    amortissementExceptionnel: 0,
    cumulAmortissementsCloture: totalCumulAmortissementsCloture,
    valeurNetteComptable: totalValeurNetteComptable,
    propriete: '',
  }]
  
  return immobilisationsWithTotal
})

// Configuration des colonnes pour le tableau détaillé
const detailColumns = [
  {
    title: 'Nature de l\'immobilisation',
    key: 'nature',
    width: 250,
    align: 'left',
    className: 'font-medium'
  },
  {
    title: 'Date d\'acquisition',
    key: 'dateAcquisition',
    width: 120,
    align: 'center',
    render: (row: any) => formatDate(row.dateAcquisition)
  },
  {
    title: 'Montant HT',
    key: 'montantHT',
    width: 120,
    align: 'right',
    render: (row: any) => formatCurrency(row.montantHT)
  },
  {
    title: 'Base amortissable',
    key: 'baseAmortissable',
    width: 140,
    align: 'right',
    render: (row: any) => formatCurrency(row.baseAmortissable)
  },
  {
    title: 'Durée (ans)',
    key: 'dureeAmortissement',
    width: 100,
    align: 'center'
  },
  {
    title: 'Taux (%)',
    key: 'tauxAmortissement',
    width: 100,
    align: 'center',
    render: (row: any) => formatPercentage(row.tauxAmortissement)
  },
  {
    title: 'Cumul antérieur',
    key: 'cumulAmortissementsAnterieurs',
    width: 140,
    align: 'right',
    render: (row: any) => formatCurrency(row.cumulAmortissementsAnterieurs)
  },
  {
    title: 'Dotation exercice',
    key: 'dotationExercice',
    width: 140,
    align: 'right',
    render: (row: any) => formatCurrency(row.dotationExercice),
    className: 'text-blue-600 font-medium'
  },
  {
    title: 'Amort. exceptionnel',
    key: 'amortissementExceptionnel',
    width: 140,
    align: 'right',
    render: (row: any) => formatCurrency(row.amortissementExceptionnel)
  },
  {
    title: 'Cumul clôture',
    key: 'cumulAmortissementsCloture',
    width: 140,
    align: 'right',
    render: (row: any) => formatCurrency(row.cumulAmortissementsCloture)
  },
  {
    title: 'VNC',
    key: 'valeurNetteComptable',
    width: 120,
    align: 'right',
    render: (row: any) => formatCurrency(row.valeurNetteComptable),
    className: 'text-green-600 font-bold'
  },
  {
    title: 'Propriété',
    key: 'propriete',
    width: 150,
    align: 'left',
    className: 'text-sm text-gray-600'
  }
]

// Fonction pour appliquer des classes CSS aux lignes du tableau détaillé
const getDetailRowClassName = (row: any) => {
  if (row.id === 'total') {
    return 'detail-total-row'
  }
  return ''
}
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
  gap: 4px;
  padding: 8px 4px;
}

.code-montant-horizontal {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  padding: 8px 4px;
  justify-content: flex-start;
}

.code-montant .code {
  font-weight: 600;
  color: #1e40af;
  font-size: 0.75em;
  background: linear-gradient(135deg, #e0f2fe 0%, #b3e5fc 100%);
  padding: 3px 6px;
  border-radius: 6px;
  min-width: 40px;
  text-align: center;
  border: 1px solid #81d4fa;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.code-montant .montant {
  font-weight: 600;
  font-variant-numeric: tabular-nums;
  color: #1f2937;
  font-size: 0.95em;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.n-table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.n-table th {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  font-weight: 700;
  color: #1e293b;
  text-align: center;
  padding: 16px 12px;
  font-size: 0.85em;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 2px solid #cbd5e1;
  line-height: 1.3;
  white-space: normal;
  vertical-align: middle;
  min-height: 60px;
}

.n-table th:first-child {
  text-align: left;
  width: 80px;
  min-width: 80px;
}

.n-table th:nth-child(2) {
  text-align: left;
  width: 300px;
  min-width: 300px;
  max-width: 400px;
}

/* Styles spécifiques pour le tableau détaillé */
.n-table tbody tr td:nth-child(7),
.n-table tbody tr td:nth-child(8),
.n-table tbody tr td:nth-child(10),
.n-table tbody tr td:nth-child(11) {
  font-weight: 600;
  font-variant-numeric: tabular-nums;
}

.n-table tbody tr td:nth-child(8) {
  color: #2563eb;
  font-weight: 700;
}

.n-table tbody tr td:nth-child(11) {
  color: #059669;
  font-weight: 700;
}

/* Styles pour les totaux du tableau détaillé */
.n-table tfoot tr td:nth-child(4),
.n-table tfoot tr td:nth-child(7),
.n-table tfoot tr td:nth-child(8),
.n-table tfoot tr td:nth-child(10),
.n-table tfoot tr td:nth-child(11) {
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}

.n-table tfoot tr td:nth-child(8) {
  color: #2563eb;
}

.n-table tfoot tr td:nth-child(11) {
  color: #059669;
}

.n-table td {
  padding: 12px;
  vertical-align: middle;
  border-bottom: 1px solid #f1f5f9;
  transition: background-color 0.2s ease;
}

.n-table tbody tr:hover {
  background-color: #f8fafc;
}

.n-table tbody tr:nth-child(even) {
  background-color: #fafbfc;
}

.n-table tfoot tr {
  border-top: 3px solid #3b82f6;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
}

.n-table tfoot .code-montant .code {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #065f46;
  border: 1px solid #6ee7b7;
  font-weight: 700;
}

.n-table tfoot .code-montant .montant {
  font-weight: 700;
  color: #065f46;
  font-size: 1em;
}

/* Styles pour les cellules avec codes */
.n-table td:first-child {
  font-weight: 600;
  color: #1e40af;
  background-color: #f8fafc;
  width: 80px;
  min-width: 80px;
}

.n-table td:nth-child(2) {
  font-weight: 600;
  color: #374151;
  width: 300px;
  min-width: 300px;
  max-width: 400px;
}

/* Colonnes avec uniquement des montants - plus petites */
.n-table td:nth-child(3),
.n-table td:nth-child(4),
.n-table td:nth-child(5),
.n-table td:nth-child(6) {
  width: 140px;
  min-width: 140px;
  max-width: 160px;
}

.n-table th:nth-child(3),
.n-table th:nth-child(4),
.n-table th:nth-child(5),
.n-table th:nth-child(6) {
  width: 140px;
  min-width: 140px;
  max-width: 160px;
}

/* Animation pour les montants */
.code-montant .montant {
  transition: transform 0.2s ease;
}

.code-montant:hover .montant {
  transform: scale(1.05);
}

/* Responsive design amélioré */
@media (max-width: 768px) {
  .n-table th,
  .n-table td {
    padding: 8px 6px;
    font-size: 0.85em;
  }
  
  .code-montant .code {
    font-size: 0.75em;
    padding: 2px 6px;
    min-width: 35px;
  }
  
  .code-montant .montant {
    font-size: 0.85em;
  }
}

/* Styles pour NDataTable */
.n-data-table .total-row {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  font-weight: 700;
  border-top: 3px solid #3b82f6;
}

.n-data-table .total-row .code-montant .code,
.n-data-table .total-row .code-montant-horizontal .code {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #065f46;
  border: 1px solid #6ee7b7;
  font-weight: 700;
}

.n-data-table .total-row .code-montant .montant,
.n-data-table .total-row .code-montant-horizontal .montant {
  font-weight: 700;
  color: #065f46;
  font-size: 1em;
}

/* Styles pour la ligne de total du tableau détaillé */
.n-data-table .detail-total-row {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  font-weight: 700;
  border-top: 3px solid #3b82f6;
}

.n-data-table .detail-total-row td {
  font-weight: 700;
  color: #1e293b;
}

.n-data-table .detail-total-row td:nth-child(4),
.n-data-table .detail-total-row td:nth-child(7),
.n-data-table .detail-total-row td:nth-child(8),
.n-data-table .detail-total-row td:nth-child(10),
.n-data-table .detail-total-row td:nth-child(11) {
  font-variant-numeric: tabular-nums;
}

.n-data-table .detail-total-row td:nth-child(8) {
  color: #2563eb;
}

.n-data-table .detail-total-row td:nth-child(11) {
  color: #059669;
}
</style> 