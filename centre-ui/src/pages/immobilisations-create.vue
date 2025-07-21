<template>
  <div class="p-4 immobilisation-create-page">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="currentStep + 1" size="small">
          <NStep title="Propriété" status="process" description="Choix du bien" />
          <NStep title="Décomposition" description="Décomposition des composants" />
          <NStep title="Validation" description="Vérification finale" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape {{ currentStep + 1 }}/3</div>
        <div class="step-mobile-label">{{ steps[currentStep].label }}</div>
      </div>
      <NH2 v-if="currentStep === 0" class="titre-principal mb-4">Étape 1 : Sélection de la propriété</NH2>
      <NH2 v-else-if="currentStep === 1" class="titre-principal mb-4">Étape 2 : Décomposition</NH2>
      <NH2 v-else-if="currentStep === 2" class="titre-principal mb-4">Étape 3 : Validation</NH2>
      <div v-if="currentStep === 0">
        <NGrid :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
          <NGi v-for="propriete in proprietes" :key="propriete.id">
            <NCard
              hoverable
              class="propriete-card"
              @click="selectProprieteImmobilisation(propriete)"
            >
              <div class="flex items-start gap-4">
                <div class="propriete-avatar">
                  <NIcon :component="BuildingHome24Filled" size="32" />
                </div>
                <div class="flex-1">
                  <div class="propriete-nom mb-2">{{ propriete.nom }}</div>
                  <div class="text-sm space-y-1">
                    <div v-if="propriete.adresse">{{ propriete.adresse }}</div>
                    <div v-if="propriete.surface">Surface : {{ propriete.surface }} m²</div>
                    <div v-if="propriete.type">Type : {{ propriete.type }}</div>
                  </div>
                </div>
              </div>
            </NCard>
          </NGi>
        </NGrid>
      </div>
      <!-- Étape 2 et 3 inchangées -->
      <div v-if="currentStep === 1">
        <div class="decomposition-info mb-4">
          <p class="info-text">
            Propriété sélectionnée : <strong>{{ selectedProprieteInfo?.nom }}</strong>
          </p>
          <p class="info-text">
            Montant total : <strong>{{ formatCurrency(formData.montant) }}</strong>
          </p>
          <p class="info-text">
            Nous proposons une décomposition automatique selon les standards immobiliers. Vous pouvez ajuster les montants.
          </p>
        </div>
        <div class="decomposition-section mb-4">
          <div class="section-header" :class="{ 'section-header-mobile': isMobile }">
            <h3 :class="{ 'mobile-title': isMobile }">Décomposition par composants</h3>
            <div v-if="isMobile" class="section-header-actions">
              <NButton size="small" @click="applyDefaultDecomposition">
                <template #icon>
                  <Icon icon="material-symbols:refresh" />
                </template>
                Appliquer la décomposition standard
              </NButton>
              <NButton size="small" @click="setAllToZero">
                <template #icon>
                  <Icon icon="material-symbols:close" />
                </template>
                Tout mettre à 0
              </NButton>
            </div>
            <template v-else>
              <NButton size="small" @click="applyDefaultDecomposition">
                <template #icon>
                  <Icon icon="material-symbols:refresh" />
                </template>
                Appliquer la décomposition standard
              </NButton>
              <NButton size="small" @click="setAllToZero">
                <template #icon>
                  <Icon icon="material-symbols:close" />
                </template>
                Tout mettre à 0
              </NButton>
            </template>
          </div>
          <div class="components-grid" :class="{ 'components-grid-mobile': isMobile }">
            <div 
              v-for="component in immobilisationComponents" 
              :key="component.key"
              class="component-card"
            >
              <div class="component-header">
                <h4>{{ component.label }}</h4>
                <span class="component-percentage">{{ component.defaultPercentage }}%</span>
              </div>
              <div class="component-details">
                <div class="detail-item">
                  <span class="detail-label">Pourcentage :</span>
                  <NInputNumber
                    v-model:value="component.percent"
                    :min="0"
                    :max="100"
                    :precision="2"
                    suffix="%"
                  />
                </div>
                <div class="detail-item">
                  <span class="detail-label">Durée d'amortissement :</span>
                  <NSelect
                    :value="String(component.dureeAmortissement)"
                    :options="getDureeOptions(component)"
                    style="width: 100%"
                    @update:value="val => component.dureeAmortissement = String(val)"
                  />
                </div>
                <div class="detail-item">
                  <span class="detail-label">Type :</span>
                  <NSelect
                    v-model:value="component.typeImmobilisation"
                    :options="typeImmobilisationOptions"
                    style="width: 100%"
                    @update:value="val => component.typeImmobilisation = val"
                  />
                </div>
              </div>
              <div class="component-montant">Montant : {{ formatCurrency(Number(component.percent) * formData.montant / 100) }}</div>
            </div>
            <div class="component-card acquisition-fees-card">
              <div class="component-header">
                <h4>Frais d'acquisition</h4>
              </div>
              <div class="component-details">
                <div class="detail-item">
                  <span class="detail-label">Montant :</span>
                  <span>{{ formatCurrency(fraisAcquisition) }}</span>
                </div>
              </div>
              <div class="component-montant">Montant : {{ formatCurrency(fraisAcquisition) }}</div>
            </div>
          </div>
          <div class="decomposition-summary mt-4">
            <div class="summary-header">
              <h3>Résumé de la décomposition</h3>
            </div>
            <div class="summary-content">
              <div class="summary-item">
                <span>Total des composants :</span>
                <span class="summary-value">{{ formatCurrency(totalComponents) }}</span>
              </div>
              <div class="summary-item" :class="{ error: totalComponents !== formData.montant }">
                <span>Différence :</span>
                <span class="summary-value">{{ formatCurrency(formData.montant - totalComponents) }}</span>
              </div>
              <div class="summary-item">
                <span>Pourcentage utilisé :</span>
                <span class="summary-value">{{ totalPercent }}%</span>
              </div>
              <div class="summary-item" :class="{ error: Math.abs(totalPercent - 100) > 0.01 }">
                <span>Total des pourcentages :</span>
                <span class="summary-value">{{ totalPercent }} %</span>
              </div>
              <div v-if="Math.abs(totalPercent - 100) > 0.01" class="summary-item error">⚠️ La somme des pourcentages doit faire 100 %</div>
            </div>
          </div>
        </div>
        <div class="step-actions mt-8">
          <NButton @click="previousStep">
            <template #icon>
              <Icon icon="material-symbols:arrow-back" />
            </template>
            Précédent
          </NButton>
          <NButton 
            type="primary" 
            :disabled="Math.abs(totalPercent - 100) > 0.01"
            @click="nextStep"
          >
            Suivant
            <template #icon>
              <Icon icon="material-symbols:arrow-forward" />
            </template>
          </NButton>
        </div>
      </div>
      <!-- Étape 3: Récapitulatif et validation -->
      <div v-if="currentStep === 2">
        <div class="recap-section mb-4">
          <h3>Informations de la propriété</h3>
          <div class="recap-item">
            <strong>Nom :</strong> {{ selectedProprieteInfo?.nom }}
          </div>
          <div class="recap-item">
            <strong>Adresse :</strong> {{ selectedProprieteInfo?.adresse }}, {{ selectedProprieteInfo?.codePostal }} {{ selectedProprieteInfo?.ville }}
          </div>
        </div>
        <div class="recap-section mb-4">
          <h3>Décomposition de l'immobilisation</h3>
          <div class="recap-table-wrapper">
            <table class="recap-table">
              <thead>
                <tr>
                  <th>Composant</th>
                  <th>%</th>
                  <th>Montant</th>
                  <th>Durée</th>
                  <th>Type</th>
                  <th>Catégorie fiscale</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="component in immobilisationComponents" :key="component.key">
                  <td>{{ component.label }}</td>
                  <td>{{ component.percent }}</td>
                  <td>{{ formatCurrency(component.percent * formData.montant / 100) }}</td>
                  <td>{{ component.dureeAmortissement || '—' }}</td>
                  <td>{{ TYPE_IMMOBILISATION_LABELS[component.typeImmobilisation as TypeImmobilisation] || '—' }}</td>
                  <td>{{ getCategorieFiscaleLabel(component) }}</td>
                </tr>
              </tbody>
              <tfoot>
                <tr>
                  <th>Total</th>
                  <th>{{ totalPercent }}</th>
                  <th>{{ formatCurrency(totalComponents) }}</th>
                  <th colspan="3"></th>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>
        <div class="step-actions mt-8">
          <NButton @click="previousStep">
            <template #icon>
              <Icon icon="material-symbols:arrow-back" />
            </template>
            Précédent
          </NButton>
          <NButton 
            type="primary" 
            :loading="saving"
            @click="saveImmobilisation"
          >
            Valider
            <template #icon>
              <Icon icon="material-symbols:check" />
            </template>
          </NButton>
        </div>
      </div>
    </NCard>
  </div>
</template>

<script setup lang="ts">
import type { StepsProps } from 'naive-ui'
import { ref, computed, h, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { 
  NButton, 
  NCard, 
  NDatePicker, 
  NForm, 
  NFormItem, 
  NInput, 
  NInputNumber, 
  NSelect, 
  useMessage, 
  NSteps, 
  NStep,
  NH2,
  NGrid,
  NGi,
  NIcon
} from 'naive-ui'

import { CategorieFiscale, TypeImmobilisation } from '@/types/immobilisation.d'
import { CATEGORIE_FISCALE_DUREES, CATEGORIE_FISCALE_LABELS, TYPE_IMMOBILISATION_LABELS } from '@/types/immobilisation-constants'
import { immobilisationApi } from '@/service/api/immobilisation'
import { useAppStore } from '@/store/modules/app'
import { useAuthStore } from '@/store/modules/auth'
import { BuildingHome24Filled } from '@vicons/fluent'

definePage({
  meta: {
    title: 'Nouvelle Immobilisation',
    icon: 'material-symbols:add-circle',
    order: 9,
    hideInMenu: true,
  },
})

const router = useRouter()
const message = useMessage()
const isMobile = useAppStore().isMobile

// État réactif
const saving = ref(false)
const proprietes = ref<any[]>([])
const currentStep = ref(0)
const selectedPropriete = ref('')
const currentStatus = ref<StepsProps['status']>('process')

// Composants immobiliers avec leurs pourcentages par défaut
const immobilisationComponents = ref([
  {
    key: 'terrain',
    label: 'Terrain',
    defaultPercentage: 15,
    percent: 15,
    dureeAmortissement: '',
    typeImmobilisation: '',
    amortissable: false,
  },
  {
    key: 'structure',
    label: 'Structure / Gros œuvre',
    defaultPercentage: 55,
    percent: 55,
    dureeAmortissement: '40',
    typeImmobilisation: 'BIEN_IMMOBILIER',
    amortissable: true,
  },
  {
    key: 'toiture',
    label: 'Toiture',
    defaultPercentage: 7,
    percent: 7,
    dureeAmortissement: '25',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'installations',
    label: 'Installations techniques',
    defaultPercentage: 8,
    percent: 8,
    dureeAmortissement: '15',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'menuiseries',
    label: 'Menuiseries / Huisseries',
    defaultPercentage: 7,
    percent: 7,
    dureeAmortissement: '18',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'revetements',
    label: 'Revêtements sols/murs',
    defaultPercentage: 3,
    percent: 3,
    dureeAmortissement: '12',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'mobilier',
    label: 'Mobilier et électroménager',
    defaultPercentage: 5,
    percent: 5,
    dureeAmortissement: '8',
    typeImmobilisation: 'MOBILIER',
    amortissable: true,
  },
])

// 2. Ajout d'une variable pour les frais d'acquisition récupérés de la propriété sélectionnée
const fraisAcquisition = computed(() => {
  const propriete = selectedProprieteInfo.value
  return propriete?.fraisNotaire || 0
})

// Workflow steps
const steps = [
  { label: 'Sélection propriété' },
  { label: 'Décomposition' },
  { label: 'Validation'},
]

// Formulaire
const formRef = ref()
const authStore = useAuthStore()
const formData = ref({
  intitule: '',
  montant: 0,
  dateAcquisition: null as number | null,
  typeImmobilisation: null as TypeImmobilisation | null,
  categorieFiscale: null as CategorieFiscale | null,
  valeurTerrain: 0,
  proprieteId: '',
  commentaire: '',
  dureeAmortissement: '',
  utilisateurId: authStore.userInfo.userId,
})

// Règles de validation
const rules = {
  intitule: {
    required: true,
    message: 'L\'intitulé est requis',
    trigger: 'blur',
  },
  montant: {
    required: true,
    validator: (rule: any, value: any) => {
      if (value === null || value === undefined || value === 0) {
        return new Error('Le montant est requis')
      }
      return true
    },
    trigger: 'blur',
  },
  dateAcquisition: {
    required: true,
    validator: (rule: any, value: any) => {
      if (value === null || value === undefined) {
        return new Error('La date d\'acquisition est requise')
      }
      return true
    },
    trigger: 'blur',
  },
  typeImmobilisation: {
    required: true,
    message: 'Le type d\'immobilisation est requis',
    trigger: 'blur',
  },
  categorieFiscale: {
    required: true,
    message: 'La catégorie fiscale est requise',
    trigger: 'blur',
  },
}

// Options pour les selects
const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL
const proprieteOptions = computed(() =>
  proprietes.value.map((p: any) => ({ label: p.nom, value: p.id })),
)

const selectedProprieteInfo = computed(() =>
  proprietes.value.find(p => p.id === selectedPropriete.value),
)

const typeImmobilisationOptions = computed(() =>
  Object.entries(TYPE_IMMOBILISATION_LABELS).map(([value, label]) => ({ label, value })),
)

const categorieFiscaleOptions = computed(() =>
  Object.entries(CATEGORIE_FISCALE_LABELS).map(([value, label]) => ({ label, value })),
)

// Calcul du total des composants (en montant)
const totalComponents = computed(() =>
  immobilisationComponents.value.reduce((total, component) => total + (Number(component.percent) * formData.value.montant / 100), 0) + Number(fraisAcquisition.value)
)

// Calcul du total des pourcentages
const totalPercent = computed(() =>
  immobilisationComponents.value.reduce((total, component) => total + Number(component.percent || 0), 0),
)

// Fonctions utilitaires
function formatCurrency(value: string | number) {
  const num = typeof value === 'string' ? Number.parseFloat(value) : value
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(num)
}

function formatDate(dateString: string | number | null) {
  if (!dateString) return 'Non définie'
  const date = typeof dateString === 'number' ? new Date(dateString) : new Date(dateString)
  return date.toLocaleDateString('fr-FR')
}

// Navigation
function goBack() {
  router.push('/immobilisations')
}

function nextStep() {
  if (currentStep.value < steps.length - 1) {
    currentStep.value++
  }
}

function previousStep() {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

// Gestion de la sélection de propriété
function onProprieteSelect(proprieteId: string) {
  selectedPropriete.value = proprieteId
  formData.value.proprieteId = proprieteId
  
  // Récupérer les informations de la propriété sélectionnée
  const propriete = selectedProprieteInfo.value
  const montantAcquisition = propriete?.montantAcquisition || 0
  
  formData.value.intitule = `Immobilisation - ${propriete?.nom || 'Propriété'}`
  
  // Utiliser la date d'acquisition de la propriété si disponible, sinon la date actuelle
  if (propriete?.dateAcquisition) {
    formData.value.dateAcquisition = new Date(propriete.dateAcquisition).getTime()
  } else {
    formData.value.dateAcquisition = Date.now()
  }
  
  formData.value.typeImmobilisation = 'TRAVAUX' as TypeImmobilisation
  formData.value.categorieFiscale = 'TRAVAUX_5_ANS' as CategorieFiscale
  formData.value.dureeAmortissement = '10'
  formData.value.montant = montantAcquisition
  formData.value.valeurTerrain = 0
  formData.value.commentaire = ''
  
  // Appliquer la décomposition par défaut
  applyDefaultDecomposition()
}

async function loadProprietes() {
  try {
    const response = await fetch(`${baseUrl}/api/getProprietesByUtilisateur/${authStore.userInfo.userId}`)
    proprietes.value = await response.json()
  } catch (error) {
    message.error('Erreur lors du chargement des propriétés')
    console.error(error)
  }
}

async function saveImmobilisation() {
  try {
    await formRef.value?.validate()
    saving.value = true

    if (!formData.value.typeImmobilisation || !formData.value.categorieFiscale) {
      message.error('Veuillez sélectionner le type et la catégorie fiscale')
      return
    }

    const composantsAEnregistrer = immobilisationComponents.value.filter(c => Number(c.percent) > 0)

    // Enregistrer les composants classiques
    for (const composant of composantsAEnregistrer) {
      const montant = (Number(composant.percent) * formData.value.montant) / 100
      const typeImmobilisationValue = composant.typeImmobilisation || (composant.key === 'terrain' ? 'TERRAIN' : undefined)
      const data: any = {
        intitule: `${formData.value.intitule} - ${composant.label}`,
        montant: montant.toString(),
        dateAcquisition: formData.value.dateAcquisition ? new Date(formData.value.dateAcquisition).toISOString().split('T')[0] : '',
        proprieteId: formData.value.proprieteId,
        commentaire: formData.value.commentaire,
        utilisateurId: formData.value.utilisateurId,
      }
      if (typeImmobilisationValue) {
        data.typeImmobilisation = typeImmobilisationValue
      }
      if (composant.amortissable && composant.dureeAmortissement) {
        data.categorieFiscale = getCategorieFiscaleFromDuree(String(composant.dureeAmortissement))
        data.dureeAmortissement = String(composant.dureeAmortissement)
      }
      await immobilisationApi.createImmobilisation(data)
    }

    // Enregistrement séparé pour les frais d'acquisition
    if (fraisAcquisition.value > 0) {
      const dataFrais = {
        intitule: `${formData.value.intitule} - Frais d'acquisition`,
        montant: fraisAcquisition.value.toString(),
        dateAcquisition: formData.value.dateAcquisition ? new Date(formData.value.dateAcquisition).toISOString().split('T')[0] : '',
        proprieteId: formData.value.proprieteId,
        commentaire: formData.value.commentaire,
        utilisateurId: formData.value.utilisateurId,
        typeImmobilisation: 'FRAIS',
        categorieFiscale: getCategorieFiscaleFromDuree('8'),
        dureeAmortissement: '8',
      }
      await immobilisationApi.createImmobilisation(dataFrais)
    }

    message.success(`${composantsAEnregistrer.length + (fraisAcquisition.value > 0 ? 1 : 0)} immobilisation(s) créée(s) avec succès`)
    router.push('/immobilisations')
  } catch (error) {
    console.error('Erreur complète:', error)
    message.error('Erreur lors de la création de l\'immobilisation')
    console.error(error)
  } finally {
    saving.value = false
  }
}

function getCategorieFiscaleFromDuree(duree: string): CategorieFiscale {
  const dureeNum = parseInt(duree)
  if (dureeNum >= 10) return CategorieFiscale.TRAVAUX_10_ANS
  return CategorieFiscale.TRAVAUX_5_ANS
}

function onTypeChange(_value: TypeImmobilisation) {
  // Réinitialiser la catégorie fiscale quand le type change
  formData.value.categorieFiscale = null
}

function onCategorieChange(value: CategorieFiscale) {
  // Mettre à jour la durée d'amortissement automatiquement
  if (value) {
    formData.value.dureeAmortissement = CATEGORIE_FISCALE_DUREES[value].toString()
  }
}

// Fonctions pour la décomposition
function applyDefaultDecomposition() {
  immobilisationComponents.value.forEach(component => {
    component.percent = component.defaultPercentage
  })
}

function setAllToZero() {
  immobilisationComponents.value.forEach(component => {
    component.percent = 0
  })
}

function updateTotalMontant() {
  // Cette fonction est appelée quand un montant de composant change
  // Le total est calculé automatiquement via le computed totalComponents
}

function getDureeOptions(component: any) {
  if (!component.amortissable) {
    return [{ label: 'Non amortissable', value: '' }]
  }
  const durees = []
  if (component.key === 'structure') {
    durees.push({ label: '30 ans', value: '30' })
    durees.push({ label: '40 ans', value: '40' })
    durees.push({ label: '50 ans', value: '50' })
  } else if (component.key === 'toiture') {
    durees.push({ label: '20 ans', value: '20' })
    durees.push({ label: '25 ans', value: '25' })
    durees.push({ label: '30 ans', value: '30' })
  } else if (component.key === 'installations') {
    durees.push({ label: '10 ans', value: '10' })
    durees.push({ label: '15 ans', value: '15' })
    durees.push({ label: '20 ans', value: '20' })
  } else if (component.key === 'menuiseries') {
    durees.push({ label: '15 ans', value: '15' })
    durees.push({ label: '18 ans', value: '18' })
    durees.push({ label: '20 ans', value: '20' })
  } else if (component.key === 'revetements') {
    durees.push({ label: '10 ans', value: '10' })
    durees.push({ label: '12 ans', value: '12' })
    durees.push({ label: '15 ans', value: '15' })
  } else {
    durees.push({ label: '5 ans', value: '5' })
    durees.push({ label: '8 ans', value: '8' })
    durees.push({ label: '10 ans', value: '10' })
  }
  return durees
}

function getStepStatus(index: number) {
  if (index < currentStep.value) return 'finish'
  if (index === currentStep.value) return 'process'
  return 'wait'
}

function selectProprieteImmobilisation(propriete: any) {
  selectedPropriete.value = propriete.id
  onProprieteSelect(propriete.id)
  nextStep()
}

// Ajout d'une fonction utilitaire pour afficher la catégorie fiscale réelle
function getCategorieFiscaleLabel(component: any) {
  if (component.key === 'terrain') {
    return 'Non amortissable'
  }
  if (component.key === 'structure') {
    return `Bien immobilier (${component.dureeAmortissement || '—'} ans)`
  }
  if (component.key === 'mobilier') {
    return `Mobilier (${component.dureeAmortissement || '—'} ans)`
  }
  // Pour tous les autres composants
  return `Travaux (${component.dureeAmortissement || '—'} ans)`
}

// Initialisation
onMounted(async () => {
  await loadProprietes()
})
</script>

<style scoped>
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.propriete-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.propriete-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border-color: #1976d2;
}
.propriete-avatar {
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.propriete-nom {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
}
.mb-8 {
  margin-bottom: 2rem !important;
}
.mb-4 {
  margin-bottom: 1rem !important;
}
.p-4 {
  padding: 1rem !important;
}
.decomposition-info {
  background-color: #f3f4f6;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.info-text {
  margin: 0 0 8px 0;
  color: #374151;
}

.info-text:last-child {
  margin-bottom: 0;
}

.recap-section {
  margin-bottom: 24px;
}

.recap-section h3 {
  margin: 0 0 12px 0;
  color: #374151;
  font-size: 16px;
  font-weight: 600;
}

.recap-item {
  padding: 8px 0;
  border-bottom: 1px solid #f3f4f6;
  display: flex;
  justify-content: space-between;
}

.recap-item:last-child {
  border-bottom: none;
}

.recap-item strong {
  color: #374151;
}

.decomposition-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h3 {
  margin: 0;
  color: #374151;
  font-size: 16px;
  font-weight: 600;
}

.components-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.component-card {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  background-color: #f9fafb;
}

.component-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.component-header h4 {
  margin: 0;
  color: #374151;
  font-size: 14px;
  font-weight: 600;
}

.component-percentage {
  background-color: #3b82f6;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.component-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-label {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
}

.decomposition-summary {
  background-color: #f3f4f6;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
}

.summary-header h3 {
  margin: 0 0 12px 0;
  color: #374151;
  font-size: 16px;
  font-weight: 600;
}

.summary-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
  font-size: 14px;
}

.summary-item.error {
  color: #dc2626;
  font-weight: 500;
}

.summary-value {
  font-weight: 600;
  color: #374151;
}

.section-header-mobile {
  flex-direction: column;
  align-items: stretch;
  gap: 8px;
}
.section-header-mobile h3,
.mobile-title {
  text-align: center;
  width: 100%;
  margin-bottom: 8px;
}
.section-header-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}
.components-grid-mobile {
  display: flex !important;
  flex-direction: column;
  gap: 12px !important;
  overflow-x: auto;
  padding-bottom: 8px;
}
.components-grid-mobile .component-card {
  min-width: 220px;
  max-width: 100%;
  padding: 10px;
}
.step-actions-mobile {
  flex-direction: column !important;
  gap: 10px !important;
  align-items: stretch !important;
}
.step-actions-mobile .n-button {
  width: 100%;
  max-width: 320px;
  align-self: center;
}
.recap-table-wrapper {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}
.recap-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 24px;
}
.recap-table th, .recap-table td {
  border: 1px solid #e5e7eb;
  padding: 8px 12px;
  text-align: left;
}
.recap-table th {
  background: #f3f4f6;
}
.recap-table tfoot th {
  font-weight: bold;
  background: #f9fafb;
}
@media (max-width: 600px) {
  .recap-table th, .recap-table td {
    font-size: 12px;
    padding: 6px 6px;
  }
  .recap-table {
    min-width: 500px;
  }
}
.progress-steps-mobile {
  overflow-x: auto;
  flex-wrap: nowrap;
  gap: 10px;
  padding: 12px 0 12px 4px;
}
.progress-steps-mobile .step {
  min-width: 120px;
  padding: 8px 6px;
}
.progress-steps-mobile .step-number {
  width: 32px;
  height: 32px;
  font-size: 1rem;
}
.progress-steps-mobile .step-label {
  font-size: 12px;
  text-align: center;
}
.progress-steps-mobile-simple {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
  gap: 4px;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #9C27B0;
}
.step-mobile-label {
  font-size: 1rem;
  color: #757575;
  text-align: center;
}

@media (max-width: 768px) {
  .general-info,
  .general-info .n-form,
  .general-info .n-form-item,
  .general-info .n-input,
  .general-info .n-input-number,
  .general-info .n-date-picker {
    width: 100% !important;
    min-width: 0 !important;
    max-width: 100vw !important;
    box-sizing: border-box !important;
  }
  .general-info {
    padding: 12px 6px 16px 6px;
    background: #f9f9fb;
    border-radius: 10px;
    margin-bottom: 18px;
    overflow-x: hidden !important;
  }
  .general-info h3 {
    font-size: 1.15rem;
    margin-bottom: 12px;
    text-align: center;
  }
  .general-info .n-form {
    grid-template-columns: 1fr !important;
    gap: 14px !important;
    width: 100%;
  }
  .general-info .n-form-item {
    margin-bottom: 0 !important;
    width: 100%;
  }
  .general-info .n-input,
  .general-info .n-input-number,
  .general-info .n-date-picker {
    width: 100% !important;
    font-size: 1.08rem;
  }
  .general-info label,
  .general-info .n-form-item-label {
    font-size: 1.08rem !important;
    font-weight: 600;
    margin-bottom: 2px;
  }
  .general-info .n-input[readonly],
  .general-info .n-input-number[readonly] {
    background: #f3f3f7 !important;
    color: #333 !important;
    border: 1px solid #e0e0e0 !important;
    min-height: 40px;
    opacity: 1 !important;
  }
}
</style>
