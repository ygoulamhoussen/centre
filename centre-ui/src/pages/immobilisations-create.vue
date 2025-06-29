<template>
  <div class="immobilisation-create-page">
    <div class="page-header">
      <h1>Nouvelle Immobilisation</h1>
      <NButton @click="goBack">
        <template #icon>
          <Icon icon="material-symbols:arrow-back" />
        </template>
        Retour
      </NButton>
    </div>

    <!-- Indicateur de progression -->
    <NCard class="progress-card">
      <div class="progress-steps">
        <div 
          v-for="(step, index) in steps" 
          :key="index"
          class="step"
          :class="{ 
            'active': currentStep === index,
            'completed': currentStep > index,
            'disabled': currentStep < index
          }"
        >
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-label">{{ step.label }}</div>
        </div>
      </div>
    </NCard>

    <!-- Étape 1: Sélection de la propriété -->
    <NCard v-if="currentStep === 0" title="Étape 1: Sélection de la propriété" class="step-card">
      <NForm label-placement="left" label-width="auto">
        <NFormItem label="Propriété" required>
          <NSelect
            v-model:value="selectedPropriete"
            :options="proprieteOptions"
            placeholder="Sélectionner une propriété"
            style="width: 100%"
            @update:value="onProprieteSelect"
          />
        </NFormItem>
      </NForm>
      
      <div class="step-actions">
        <NButton @click="goBack">Annuler</NButton>
        <NButton 
          type="primary" 
          :disabled="!selectedPropriete"
          @click="nextStep"
        >
          Suivant
          <template #icon>
            <Icon icon="material-symbols:arrow-forward" />
          </template>
        </NButton>
      </div>
    </NCard>

    <!-- Étape 2: Décomposition de l'immobilisation -->
    <NCard v-if="currentStep === 1" title="Étape 2: Décomposition de l'immobilisation" class="step-card">
      <div class="decomposition-info">
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

      <!-- Décomposition automatique -->
      <div class="decomposition-section">
        <div class="section-header">
          <h3>Décomposition par composants</h3>
          <NButton size="small" @click="applyDefaultDecomposition">
            <template #icon>
              <Icon icon="material-symbols:refresh" />
            </template>
            Appliquer la décomposition standard
          </NButton>
        </div>

        <div class="components-grid">
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
                <span class="detail-label">Montant :</span>
                <NInputNumber
                  v-model:value="component.montant"
                  :precision="2"
                  :min="0"
                  :max="formData.montant"
                  style="width: 100%"
                  @update:value="updateTotalMontant"
                />
              </div>
              
              <div class="detail-item">
                <span class="detail-label">Durée d'amortissement :</span>
                <NSelect
                  v-model:value="component.dureeAmortissement"
                  :options="getDureeOptions(component)"
                  style="width: 100%"
                />
              </div>
              
              <div class="detail-item">
                <span class="detail-label">Type :</span>
                <NSelect
                  v-model:value="component.typeImmobilisation"
                  :options="typeImmobilisationOptions"
                  style="width: 100%"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- Résumé de la décomposition -->
        <div class="decomposition-summary">
          <div class="summary-header">
            <h3>Résumé de la décomposition</h3>
          </div>
          <div class="summary-content">
            <div class="summary-item">
              <span>Total des composants :</span>
              <span class="summary-value">{{ formatCurrency(totalComponents) }}</span>
            </div>
            <div class="summary-item" :class="{ 'error': totalComponents !== formData.montant }">
              <span>Différence :</span>
              <span class="summary-value">{{ formatCurrency(formData.montant - totalComponents) }}</span>
            </div>
            <div class="summary-item">
              <span>Pourcentage utilisé :</span>
              <span class="summary-value">{{ ((totalComponents / formData.montant) * 100).toFixed(1) }}%</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Informations générales -->
      <div class="general-info">
        <h3>Informations générales</h3>
        <NForm
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-placement="left"
          label-width="auto"
          require-mark-placement="right-hanging"
        >
          <NFormItem label="Intitulé global" path="intitule">
            <NInput v-model:value="formData.intitule" placeholder="Ex: Rénovation complète" />
          </NFormItem>

          <NFormItem label="Date d'acquisition" path="dateAcquisition">
            <NDatePicker
              v-model:value="formData.dateAcquisition"
              type="date"
              style="width: 100%"
            />
          </NFormItem>

          <NFormItem label="Commentaire" path="commentaire">
            <NInput
              v-model:value="formData.commentaire"
              type="textarea"
              placeholder="Commentaire optionnel"
              :rows="3"
            />
          </NFormItem>
        </NForm>
      </div>

      <div class="step-actions">
        <NButton @click="previousStep">
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Précédent
        </NButton>
        <NButton 
          type="primary" 
          :disabled="totalComponents !== formData.montant"
          @click="nextStep"
        >
          Suivant
          <template #icon>
            <Icon icon="material-symbols:arrow-forward" />
          </template>
        </NButton>
      </div>
    </NCard>

    <!-- Étape 3: Récapitulatif et validation -->
    <NCard v-if="currentStep === 2" title="Étape 3: Récapitulatif et validation" class="step-card">
      <div class="recap-section">
        <h3>Informations de la propriété</h3>
        <div class="recap-item">
          <strong>Nom :</strong> {{ selectedProprieteInfo?.nom }}
        </div>
        <div class="recap-item">
          <strong>Adresse :</strong> {{ selectedProprieteInfo?.adresse }}, {{ selectedProprieteInfo?.codePostal }} {{ selectedProprieteInfo?.ville }}
        </div>
      </div>

      <div class="recap-section">
        <h3>Détails de l'immobilisation</h3>
        <div class="recap-item">
          <strong>Intitulé :</strong> {{ formData.intitule }}
        </div>
        <div class="recap-item">
          <strong>Montant :</strong> {{ formatCurrency(formData.montant) }}
        </div>
        <div class="recap-item">
          <strong>Date d'acquisition :</strong> {{ formatDate(formData.dateAcquisition) }}
        </div>
        <div class="recap-item">
          <strong>Type :</strong> {{ TYPE_IMMOBILISATION_LABELS[formData.typeImmobilisation] }}
        </div>
        <div class="recap-item">
          <strong>Catégorie fiscale :</strong> {{ CATEGORIE_FISCALE_LABELS[formData.categorieFiscale] }}
        </div>
        <div class="recap-item">
          <strong>Durée d'amortissement :</strong> {{ formData.dureeAmortissement }} ans
        </div>
        <div v-if="formData.valeurTerrain" class="recap-item">
          <strong>Valeur du terrain :</strong> {{ formatCurrency(formData.valeurTerrain) }}
        </div>
        <div v-if="formData.commentaire" class="recap-item">
          <strong>Commentaire :</strong> {{ formData.commentaire }}
        </div>
      </div>

      <div class="step-actions">
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
          <template #icon>
            <Icon icon="material-symbols:check" />
          </template>
          Créer l'immobilisation
        </NButton>
      </div>
    </NCard>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
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
  useMessage 
} from 'naive-ui'

import type { CategorieFiscale, TypeImmobilisation } from '@/types/immobilisation.d'
import { CATEGORIE_FISCALE_DUREES, CATEGORIE_FISCALE_LABELS, TYPE_IMMOBILISATION_LABELS } from '@/types/immobilisation-constants'
import { immobilisationApi } from '@/service/api/immobilisation'

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

// État réactif
const saving = ref(false)
const proprietes = ref<any[]>([])
const currentStep = ref(0)
const selectedPropriete = ref('')

// Composants immobiliers avec leurs pourcentages par défaut
const immobilisationComponents = ref([
  {
    key: 'terrain',
    label: 'Terrain',
    defaultPercentage: 15,
    montant: 0,
    dureeAmortissement: null,
    typeImmobilisation: null,
    amortissable: false,
  },
  {
    key: 'structure',
    label: 'Structure / Gros œuvre',
    defaultPercentage: 55,
    montant: 0,
    dureeAmortissement: '40',
    typeImmobilisation: 'BIEN_IMMOBILIER',
    amortissable: true,
  },
  {
    key: 'toiture',
    label: 'Toiture',
    defaultPercentage: 7,
    montant: 0,
    dureeAmortissement: '25',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'installations',
    label: 'Installations techniques',
    defaultPercentage: 8,
    montant: 0,
    dureeAmortissement: '15',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'menuiseries',
    label: 'Menuiseries / Huisseries',
    defaultPercentage: 7,
    montant: 0,
    dureeAmortissement: '18',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'revetements',
    label: 'Revêtements sols/murs',
    defaultPercentage: 3,
    montant: 0,
    dureeAmortissement: '12',
    typeImmobilisation: 'TRAVAUX',
    amortissable: true,
  },
  {
    key: 'mobilier',
    label: 'Mobilier et électroménager',
    defaultPercentage: 7,
    montant: 0,
    dureeAmortissement: '8',
    typeImmobilisation: 'MOBILIER',
    amortissable: true,
  },
  {
    key: 'frais',
    label: 'Frais d\'acquisition',
    defaultPercentage: 8,
    montant: 0,
    dureeAmortissement: '8',
    typeImmobilisation: 'FRAIS',
    amortissable: true,
  },
])

// Workflow steps
const steps = [
  { label: 'Sélection propriété' },
  { label: 'Décomposition' },
  { label: 'Validation' },
]

// Formulaire
const formRef = ref()
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
  utilisateurId: '00000000-0000-0000-0000-000000000003', // Yussouf par défaut
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

// Calcul du total des composants
const totalComponents = computed(() =>
  immobilisationComponents.value.reduce((total, component) => total + (component.montant || 0), 0),
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
  
  // Initialiser les valeurs par défaut
  formData.value.intitule = `Immobilisation - ${selectedProprieteInfo.value?.nom || 'Propriété'}`
  formData.value.dateAcquisition = Date.now()
  formData.value.typeImmobilisation = 'TRAVAUX' as TypeImmobilisation
  formData.value.categorieFiscale = 'TRAVAUX_5_ANS' as CategorieFiscale
  formData.value.dureeAmortissement = '10'
  formData.value.montant = 0
  formData.value.valeurTerrain = 0
  formData.value.commentaire = ''
  
  // Appliquer la décomposition par défaut
  applyDefaultDecomposition()
}

async function loadProprietes() {
  try {
    const response = await fetch('http://localhost:8080/api/getProprietesByUtilisateur/00000000-0000-0000-0000-000000000003')
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

    // Validation supplémentaire
    if (!formData.value.typeImmobilisation || !formData.value.categorieFiscale) {
      message.error('Veuillez sélectionner le type et la catégorie fiscale')
      return
    }

    // Créer une immobilisation pour chaque composant amortissable
    const composantsAmortissables = immobilisationComponents.value.filter(c => c.amortissable && c.montant > 0)
    
    for (const composant of composantsAmortissables) {
      const data = {
        intitule: `${formData.value.intitule} - ${composant.label}`,
        montant: composant.montant.toString(),
        dateAcquisition: formData.value.dateAcquisition ? new Date(formData.value.dateAcquisition).toISOString().split('T')[0] : '',
        typeImmobilisation: composant.typeImmobilisation as TypeImmobilisation,
        categorieFiscale: getCategorieFiscaleFromDuree(composant.dureeAmortissement),
        dureeAmortissement: composant.dureeAmortissement,
        proprieteId: formData.value.proprieteId,
        commentaire: formData.value.commentaire,
        utilisateurId: formData.value.utilisateurId,
      }

      console.error('=== CRÉATION COMPOSANT ===')
      console.error('Composant:', composant.label)
      console.error('Données à envoyer:', data)
      
      const result = await immobilisationApi.createImmobilisation(data)
      console.error('Résultat de la création:', result)
    }

    message.success(`${composantsAmortissables.length} immobilisation(s) créée(s) avec succès`)
    router.push('/immobilisations')
  } catch (error) {
    console.error('Erreur complète:', error)
    message.error('Erreur lors de la création de l\'immobilisation')
    console.error(error)
  } finally {
    saving.value = false
  }
}

function getCategorieFiscaleFromDuree(duree: string | null): CategorieFiscale {
  if (!duree) return 'TRAVAUX_5_ANS'
  
  const dureeNum = parseInt(duree)
  if (dureeNum >= 30) return 'BIEN_IMMOBILIER_30_ANS'
  if (dureeNum >= 20) return 'TRAVAUX_20_ANS'
  if (dureeNum >= 15) return 'TRAVAUX_15_ANS'
  if (dureeNum >= 10) return 'TRAVAUX_10_ANS'
  return 'TRAVAUX_5_ANS'
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
  const totalMontant = formData.value.montant
  immobilisationComponents.value.forEach(component => {
    component.montant = (totalMontant * component.defaultPercentage) / 100
  })
}

function updateTotalMontant() {
  // Cette fonction est appelée quand un montant de composant change
  // Le total est calculé automatiquement via le computed totalComponents
}

function getDureeOptions(component: any) {
  if (!component.amortissable) {
    return [{ label: 'Non amortissable', value: null }]
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

// Initialisation
onMounted(async () => {
  await loadProprietes()
})
</script>

<style scoped>
.immobilisation-create-page {
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

.progress-card {
  margin-bottom: 20px;
}

.progress-steps {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  position: relative;
}

.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 20px;
  left: 50%;
  width: 100%;
  height: 2px;
  background-color: #e5e7eb;
  z-index: 1;
}

.step.completed:not(:last-child)::after {
  background-color: #10b981;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  background-color: #e5e7eb;
  color: #6b7280;
  z-index: 2;
  position: relative;
}

.step.active .step-number {
  background-color: #3b82f6;
  color: white;
}

.step.completed .step-number {
  background-color: #10b981;
  color: white;
}

.step-label {
  font-size: 14px;
  color: #6b7280;
  text-align: center;
}

.step.active .step-label {
  color: #3b82f6;
  font-weight: 500;
}

.step.completed .step-label {
  color: #10b981;
  font-weight: 500;
}

.step-card {
  margin-bottom: 20px;
}

.step-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
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

.general-info {
  margin-bottom: 24px;
}

.general-info h3 {
  margin: 0 0 16px 0;
  color: #374151;
  font-size: 16px;
  font-weight: 600;
}
</style> 