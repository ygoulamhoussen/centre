<script setup lang="ts">
import { immobilisationApi } from '@/service/api/immobilisation'
import { useAppStore } from '@/store/modules/app'
import { useAuthStore } from '@/store/modules/auth'
import { CATEGORIE_FISCALE_LABELS, TYPE_IMMOBILISATION_LABELS } from '@/types/immobilisation-constants'
import { Icon } from '@iconify/vue'
import { NButton, NCard, NForm, NFormItem, NInput, NInputNumber, NSelect, NDatePicker, NAlert, useMessage, NSteps, NStep, NH2, NGrid, NGi, NIcon } from 'naive-ui'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { BuildingHome24Filled } from '@vicons/fluent'

definePage({
  meta: {
    title: 'Immobilisation Simple',
    icon: 'mdi:domain',
    order: 8,
    hideInMenu: true,
  },
})

const router = useRouter()
const message = useMessage()
const isMobile = useAppStore().isMobile
const authStore = useAuthStore()
const userId = authStore.userInfo.userId

// État du workflow
const currentStep = ref(0)
const selectedPropriete = ref('')

// Workflow steps
const steps = [
  { label: 'Sélection propriété' },
  { label: 'Type d\'immobilisation' },
  { label: 'Détails immobilisation' },
]

// État du formulaire
const formData = ref({
  libelle: '',
  montantTtc: null as number | null,
  dateAcquisition: null as number | null, // Changé de Date à number pour NDatePicker
  dureeAnnees: null as number | null,
})

// Type d'immobilisation sélectionné
const selectedType = ref('')

// État de l'interface
const loading = ref(false)
const proprietes = ref<any[]>([])

// Validation
const formRef = ref()
const rules = {
  libelle: {
    required: true,
    message: 'Le libellé est requis',
    trigger: 'blur'
  },
  montantTtc: {
    required: true,
    message: 'Le montant TTC est requis',
    trigger: 'blur',
    validator: (rule: any, value: any) => {
      if (!value || value <= 0) {
        return new Error('Le montant doit être supérieur à 0')
      }
      return true
    }
  },
  dateAcquisition: {
    required: true,
    message: 'La date d\'acquisition est requise',
    trigger: 'change',
    validator: (rule: any, value: any) => {
      if (!value) {
        return new Error('La date d\'acquisition est requise')
      }
      const today = new Date()
      today.setHours(23, 59, 59, 999)
      if (value > today) {
        return new Error('La date d\'acquisition ne peut pas être dans le futur')
      }
      return true
    }
  },
  dureeAnnees: {
    required: true,
    message: 'La durée est requise',
    trigger: 'blur',
    validator: (rule: any, value: any) => {
      if (!value || value <= 0) {
        return new Error('La durée doit être supérieure à 0')
      }
      return true
    }
  }
}

// Options pour les types d'immobilisation
const typeOptions = computed(() => [
  { 
    label: 'Appartement', 
    value: 'APPARTEMENT',
    icon: '🏠',
    dureeDefaut: 25 // Correspond à BATIMENT_25_ANS
  },
  { 
    label: 'Travaux', 
    value: 'TRAVAUX',
    icon: '🛠️',
    dureeDefaut: 10 // Correspond à TRAVAUX_10_ANS
  },
  { 
    label: 'Mobilier', 
    value: 'MOBILIER',
    icon: '🪑',
    dureeDefaut: 5 // Correspond à MOBILIER_5_ANS
  },
  { 
    label: 'Équipement', 
    value: 'EQUIPEMENT',
    icon: '🔌',
    dureeDefaut: 5 // Correspond à EQUIPEMENT_5_ANS
  },
  { 
    label: 'Frais d\'acquisition', 
    value: 'FRAIS_ACQUISITION',
    icon: '📑',
    dureeDefaut: 5 // Correspond à TRAVAUX_5_ANS
  },
])

// Informations de la propriété sélectionnée
const selectedProprieteInfo = computed(() =>
  proprietes.value.find(p => p.id === selectedPropriete.value),
)

// Fonctions
async function loadProprietes() {
  try {
    const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL
    const response = await fetch(`${baseUrl}/api/getProprietesByUtilisateur/${userId}`)
    if (response.ok) {
      const data = await response.json()
      proprietes.value = data
      console.log('Propriétés chargées:', data)
    } else {
      console.error('Erreur HTTP:', response.status, response.statusText)
      message.error('Erreur lors du chargement des propriétés')
    }
  } catch (error) {
    console.error('Erreur lors du chargement des propriétés:', error)
    message.error('Erreur lors du chargement des propriétés')
  }
}

// Sélection de propriété
function selectPropriete(propriete: any) {
  selectedPropriete.value = propriete.id
  
  // Pré-remplir le formulaire avec les informations de la propriété
  formData.value.libelle = `Immobilisation - ${propriete.nom}`
  
  // Utiliser la date d'acquisition de la propriété si disponible
  if (propriete.dateAcquisition) {
    formData.value.dateAcquisition = new Date(propriete.dateAcquisition).getTime()
  } else {
    formData.value.dateAcquisition = Date.now()
  }
  
  // Passer à l'étape suivante
  nextStep()
}

// Sélection de type d'immobilisation
function selectType(typeValue: string) {
  selectedType.value = typeValue
  
  // Pré-remplir la durée selon le type sélectionné
  const typeInfo = typeOptions.value.find(t => t.value === typeValue)
  if (typeInfo) {
    formData.value.dureeAnnees = typeInfo.dureeDefaut
  }
  
  // Passer à l'étape suivante
  nextStep()
}

// Navigation
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

async function submitForm() {
  try {
    await formRef.value?.validate()
    
    loading.value = true
    
    // Mapper les champs selon la structure attendue par le backend
    const immobilisationData = {
      proprieteId: selectedPropriete.value,
      utilisateurId: userId,
      typeImmobilisation: selectedType.value,
      intitule: formData.value.libelle, // Le backend attend 'intitule', pas 'libelle'
      montant: formData.value.montantTtc?.toString() || '0', // Le backend attend 'montant', pas 'montantTtc'
      dateAcquisition: formData.value.dateAcquisition ? new Date(formData.value.dateAcquisition).toISOString().split('T')[0] : new Date().toISOString().split('T')[0],
      dureeAmortissement: formData.value.dureeAnnees?.toString() || '0', // Le backend attend 'dureeAmortissement', pas 'dureeAnnees'
      categorieFiscale: getCategorieFiscaleFromType(selectedType.value), // Ajouter la catégorie fiscale
      commentaire: '' // Champ requis par le backend
    }
    
    await immobilisationApi.createImmobilisation(immobilisationData)
    
    message.success('Immobilisation créée avec succès')
    router.push('/immobilisations')
    
  } catch (error) {
    console.error('Erreur lors de la création:', error)
    message.error('Erreur lors de la création de l\'immobilisation')
  } finally {
    loading.value = false
  }
}

// Fonction pour déterminer la catégorie fiscale selon le type d'immobilisation
function getCategorieFiscaleFromType(type: string): string {
  switch (type) {
    case 'APPARTEMENT':
      return 'BATIMENT_25_ANS' // Bâtiment avec durée standard
    case 'TRAVAUX':
      return 'TRAVAUX_10_ANS' // Travaux avec durée standard
    case 'MOBILIER':
      return 'MOBILIER_5_ANS' // Mobilier avec durée standard
    case 'EQUIPEMENT':
      return 'EQUIPEMENT_5_ANS' // Équipement avec durée standard
    case 'FRAIS_ACQUISITION':
      return 'TRAVAUX_5_ANS' // Frais d'acquisition assimilés aux travaux
    default:
      return 'MOBILIER_5_ANS'
  }
}

function cancel() {
  router.push('/immobilisations')
}

// Initialisation
onMounted(async () => {
  await loadProprietes()
})
</script>

<template>
  <div class="immobilisation-simple-page">
    <div class="header">
      <NButton quaternary @click="cancel">
        <template #icon>
          <Icon icon="material-symbols:arrow-back" />
        </template>
        Retour
      </NButton>
      <h1 class="titre-principal">Nouvelle Immobilisation Simple</h1>
    </div>

    <NCard class="workflow-card">
      <!-- Stepper -->
             <div class="mb-8" v-if="!isMobile">
         <NSteps :current="currentStep + 1" size="small">
           <NStep title="Propriété" status="process" description="Choix du bien" />
           <NStep title="Type" description="Type d'immobilisation" />
           <NStep title="Détails" description="Informations immobilisation" />
         </NSteps>
       </div>
       <div v-else class="mobile-stepper mb-8">
         <div class="step-mobile-number">Étape {{ currentStep + 1 }}/3</div>
         <div class="step-mobile-label">{{ steps[currentStep].label }}</div>
       </div>

      <!-- Étape 1: Sélection de propriété -->
      <div v-if="currentStep === 0">
        <NH2 class="titre-principal mb-4">Étape 1 : Sélection de la propriété</NH2>
        
        <NAlert type="info" class="mb-4">
          <template #icon>
            <Icon icon="material-symbols:info-outline" />
          </template>
          Sélectionnez la propriété pour laquelle vous souhaitez créer une immobilisation simple.
        </NAlert>

                 <div v-if="proprietes.length === 0" class="no-proprietes">
           <NAlert type="warning">
             <template #icon>
               <Icon icon="material-symbols:warning" />
             </template>
             Aucune propriété trouvée. Vous devez d'abord créer une propriété avant de pouvoir créer une immobilisation.
           </NAlert>
           <div class="mt-4">
             <NButton type="primary" @click="router.push('/proprietes')">
               <template #icon>
                 <Icon icon="material-symbols:add" />
               </template>
               Créer une propriété
             </NButton>
           </div>
         </div>
         
         <NGrid v-else :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
           <NGi v-for="propriete in proprietes" :key="propriete.id">
             <NCard
               hoverable
               class="propriete-card"
               @click="selectPropriete(propriete)"
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

             <!-- Étape 2: Sélection du type d'immobilisation -->
       <div v-if="currentStep === 1">
         <NH2 class="titre-principal mb-4">Étape 2 : Sélection du type d'immobilisation</NH2>
         
         <div class="propriete-info mb-4">
           <NAlert type="success">
             <template #icon>
               <Icon icon="material-symbols:check-circle" />
             </template>
             Propriété sélectionnée : <strong>{{ selectedProprieteInfo?.nom }}</strong>
           </NAlert>
         </div>

         <NAlert type="info" class="mb-4">
           <template #icon>
             <Icon icon="material-symbols:info-outline" />
           </template>
           Sélectionnez le type d'immobilisation approprié. La durée d'amortissement sera pré-remplie automatiquement.
         </NAlert>

         <div class="type-grid">
           <NCard
             v-for="type in typeOptions"
             :key="type.value"
             hoverable
             class="type-card"
             @click="selectType(type.value)"
           >
             <div class="type-content">
               <div class="type-icon">{{ type.icon }}</div>
               <div class="type-label">{{ type.label }}</div>
               <div class="type-duree">{{ type.dureeDefaut }} ans</div>
             </div>
           </NCard>
         </div>
       </div>

       <!-- Étape 3: Formulaire d'immobilisation -->
       <div v-if="currentStep === 2">
         <NH2 class="titre-principal mb-4">Étape 3 : Détails de l'immobilisation</NH2>
        
        <div class="propriete-info mb-4">
          <NAlert type="success">
            <template #icon>
              <Icon icon="material-symbols:check-circle" />
            </template>
            Propriété sélectionnée : <strong>{{ selectedProprieteInfo?.nom }}</strong>
          </NAlert>
        </div>

                 <NForm
           ref="formRef"
           :model="formData"
           :rules="rules"
           label-placement="left"
           label-width="auto"
           require-mark-placement="right-hanging"
           size="large"
         >
           <div class="form-grid">
             <NFormItem label="Libellé" path="libelle">
               <NInput
                 v-model:value="formData.libelle"
                 placeholder="Ex: Ordinateur portable, Mobilier de bureau..."
                 clearable
               />
             </NFormItem>

             <NFormItem label="Montant TTC (€)" path="montantTtc">
               <NInputNumber
                 v-model:value="formData.montantTtc"
                 placeholder="0,00"
                 :min="0"
                 :precision="2"
                 clearable
                 style="width: 100%"
               />
             </NFormItem>

             <NFormItem label="Type sélectionné" path="selectedType">
               <NInput
                 :value="selectedType ? typeOptions.find(t => t.value === selectedType)?.label : ''"
                 readonly
                 style="width: 100%"
               />
             </NFormItem>

             <NFormItem label="Date d'acquisition" path="dateAcquisition">
               <NDatePicker
                 v-model:value="formData.dateAcquisition"
                 type="date"
                 placeholder="Sélectionnez une date"
                 clearable
                 style="width: 100%"
               />
             </NFormItem>

             <NFormItem label="Durée (années)" path="dureeAnnees">
               <NInputNumber
                 v-model:value="formData.dureeAnnees"
                 placeholder="Ex: 5"
                 :min="1"
                 :max="50"
                 clearable
                 style="width: 100%"
               />
             </NFormItem>
           </div>
         </NForm>
      </div>

             <!-- Boutons de navigation -->
       <div class="button-container">
         <div v-if="currentStep === 1" class="step-actions">
           <NButton @click="previousStep">
             <template #icon>
               <Icon icon="material-symbols:arrow-back" />
             </template>
             Précédent
           </NButton>
         </div>
         <div v-if="currentStep === 2" class="step-actions">
           <NButton @click="previousStep">
             <template #icon>
               <Icon icon="material-symbols:arrow-back" />
             </template>
             Précédent
           </NButton>
           <NButton
             type="primary"
             :loading="loading"
             @click="submitForm"
           >
             <template #icon>
               <Icon icon="material-symbols:save" />
             </template>
             Créer l'immobilisation
           </NButton>
         </div>
       </div>
    </NCard>
  </div>
</template>

<style scoped>
.immobilisation-simple-page {
  padding: 20px;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
}

.header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.titre-principal {
  color: var(--n-text-color);
  font-weight: bold;
  font-size: 1.5rem;
  margin: 0;
}

.workflow-card {
  background: var(--n-color);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.mobile-stepper {
  text-align: center;
  padding: 16px;
  background: var(--n-color);
  border-radius: 8px;
  border: 1px solid var(--n-border-color);
}

.step-mobile-number {
  font-size: 0.875rem;
  color: var(--n-text-color-2);
  margin-bottom: 4px;
}

.step-mobile-label {
  font-weight: 600;
  color: var(--n-text-color);
}

.propriete-card {
  cursor: pointer;
  transition: all 0.2s ease;
  border: 2px solid transparent;
}

.propriete-card:hover {
  border-color: var(--n-primary-color);
  transform: translateY(-2px);
}

.propriete-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: var(--n-primary-color);
  color: white;
  border-radius: 8px;
  flex-shrink: 0;
}

.propriete-nom {
  font-weight: 600;
  color: var(--n-text-color);
  font-size: 1rem;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
  margin-bottom: 32px;
  width: 100%;
}

.full-width {
  grid-column: 1 / -1;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid var(--n-border-color);
}

.step-actions {
  display: flex;
  gap: 16px;
}

.propriete-info {
  margin-bottom: 24px;
}

.no-proprietes {
  text-align: center;
  padding: 40px 20px;
}

.categorie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-top: 24px;
  width: 100%;
}

.categorie-card {
  cursor: pointer;
  transition: all 0.2s ease;
  border: 2px solid transparent;
  padding: 20px;
}

.categorie-card:hover {
  border-color: var(--n-primary-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.categorie-content {
  text-align: center;
}

.categorie-code {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--n-primary-color);
  margin-bottom: 8px;
}

.categorie-label {
  font-size: 0.9rem;
  color: var(--n-text-color-2);
  line-height: 1.4;
}

/* Styles pour les cartes de type d'immobilisation */
.type-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-top: 24px;
  width: 100%;
}

.type-card {
  cursor: pointer;
  transition: all 0.2s ease;
  border: 2px solid transparent;
  padding: 20px;
  text-align: center;
}

.type-card:hover {
  border-color: var(--n-primary-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.type-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.type-icon {
  font-size: 2rem;
  margin-bottom: 8px;
}

.type-label {
  font-weight: 600;
  color: var(--n-text-color);
  font-size: 1rem;
}

.type-duree {
  font-size: 0.8rem;
  color: var(--n-text-color-2);
  background: var(--n-color-modal);
  padding: 4px 8px;
  border-radius: 4px;
}

/* Amélioration de la lisibilité des champs de formulaire */
.form-grid .n-form-item {
  margin-bottom: 0;
}

.form-grid .n-input,
.form-grid .n-input-number,
.form-grid .n-date-picker {
  background-color: var(--n-color);
  border: 1px solid var(--n-border-color);
  border-radius: 6px;
  color: var(--n-text-color);
}

.form-grid .n-input:hover,
.form-grid .n-input-number:hover,
.form-grid .n-date-picker:hover {
  border-color: var(--n-primary-color);
}

.form-grid .n-input:focus,
.form-grid .n-input-number:focus,
.form-grid .n-date-picker:focus {
  border-color: var(--n-primary-color);
  box-shadow: 0 0 0 2px rgba(var(--n-primary-color), 0.2);
}

.form-grid .n-input__input,
.form-grid .n-input-number__input {
  color: var(--n-text-color);
  background-color: transparent;
}

.form-grid .n-input__input::placeholder,
.form-grid .n-input-number__input::placeholder {
  color: var(--n-text-color-3);
}

.form-grid .n-form-item-label {
  color: var(--n-text-color);
  font-weight: 500;
}

.form-grid .n-form-item-label__text {
  color: var(--n-text-color);
}

/* Amélioration du champ readonly */
.form-grid .n-input[readonly] {
  background-color: var(--n-color-modal);
  color: var(--n-text-color-2);
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .immobilisation-simple-page {
    padding: 12px;
    width: 100%;
  }
  
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .titre-principal {
    font-size: 1.25rem;
  }
  
  .form-grid {
    grid-template-columns: 1fr;
    gap: 20px;
    width: 100%;
  }
  
  .categorie-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .categorie-card {
    padding: 16px;
  }
  
  .button-container {
    flex-direction: column;
    gap: 12px;
  }
  
  .step-actions {
    flex-direction: column;
    gap: 12px;
  }
  
  .step-actions .n-button {
    width: 100%;
  }
  
  .propriete-card {
    padding: 16px;
  }
  
  .propriete-avatar {
    width: 40px;
    height: 40px;
  }
  
  .propriete-nom {
    font-size: 0.9rem;
  }
  
  /* Amélioration de la lisibilité des champs sur mobile */
  .form-grid .n-input,
  .form-grid .n-input-number,
  .form-grid .n-date-picker {
    font-size: 16px; /* Évite le zoom sur iOS */
  }
  
  .form-grid .n-form-item-label {
    font-size: 0.9rem;
    margin-bottom: 8px;
  }
}

@media (max-width: 480px) {
  .immobilisation-simple-page {
    padding: 8px;
  }
  
  .workflow-card {
    border-radius: 8px;
  }
  
  .form-grid {
    gap: 12px;
  }
  
  .categorie-grid {
    gap: 8px;
  }
  
  .categorie-card {
    padding: 12px;
  }
  
  .categorie-code {
    font-size: 1.25rem;
  }
  
  .categorie-label {
    font-size: 0.8rem;
  }
}
</style>
