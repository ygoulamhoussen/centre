<template>
  <div class="capital-identites-page">
    <n-card title="📄 Formulaire 2033-D — Capital et identités" class="mb-4">
      <div class="text-gray-600 mb-6">
        <p>Renseignements relatifs aux associés ou à l'exploitant LMNP</p>
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

      <n-alert v-if="success" type="success" class="mb-4">
        {{ success }}
      </n-alert>

      <div v-if="!loading" :class="isMobile ? 'mobile-form' : 'space-y-6'">
        <!-- Formulaire -->
        <n-card title="Informations de l'exploitant" size="small" :class="isMobile ? 'mobile-card' : ''">
          <n-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            label-placement="top"
            require-mark-placement="right-hanging"
          >
            <n-grid :cols="2" :x-gap="16" :y-gap="16">
              <!-- Nom et Prénom -->
              <n-grid-item>
                <n-form-item label="Nom de l'exploitant" path="nomExploitant">
                  <n-input
                    v-model:value="formData.nomExploitant"
                    placeholder="Nom"
                    clearable
                  />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="Prénom de l'exploitant" path="prenomExploitant">
                  <n-input
                    v-model:value="formData.prenomExploitant"
                    placeholder="Prénom"
                    clearable
                  />
                </n-form-item>
              </n-grid-item>

              <!-- Adresse -->
              <n-grid-item :span="2">
                <n-form-item label="Adresse de l'exploitant" path="adresseExploitant">
                  <n-input
                    v-model:value="formData.adresseExploitant"
                    placeholder="Numéro et rue"
                    clearable
                  />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="Code postal" path="codePostalExploitant">
                  <n-input
                    v-model:value="formData.codePostalExploitant"
                    placeholder="75000"
                    clearable
                  />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="Ville" path="villeExploitant">
                  <n-input
                    v-model:value="formData.villeExploitant"
                    placeholder="Paris"
                    clearable
                  />
                </n-form-item>
              </n-grid-item>

              <!-- Qualité -->
              <n-grid-item :span="2">
                <n-form-item label="Qualité" path="qualite">
                  <n-select
                    v-model:value="formData.qualite"
                    :options="qualiteOptions"
                    placeholder="Sélectionner la qualité"
                    clearable
                  />
                </n-form-item>
              </n-grid-item>
            </n-grid>
          </n-form>
        </n-card>

        <!-- Apports -->
        <n-card title="Apports au capital" size="small" :class="isMobile ? 'mobile-card' : ''">
          <n-grid :cols="3" :x-gap="16" :y-gap="16">
            <n-grid-item>
              <n-form-item label="Apports en numéraire" path="apportsNumeraires">
                <n-input-number
                  v-model:value="formData.apportsNumeraires"
                  placeholder="0.00"
                  :precision="2"
                  :min="0"
                  style="width: 100%"
                />
                <template #suffix>€</template>
              </n-form-item>
            </n-grid-item>
            <n-grid-item>
              <n-form-item label="Apports en nature" path="apportsNature">
                <n-input-number
                  v-model:value="formData.apportsNature"
                  placeholder="0.00"
                  :precision="2"
                  :min="0"
                  style="width: 100%"
                />
                <template #suffix>€</template>
              </n-form-item>
            </n-grid-item>
            <n-grid-item>
              <n-form-item label="Apports en industrie" path="apportsIndustrie">
                <n-input-number
                  v-model:value="formData.apportsIndustrie"
                  placeholder="0.00"
                  :precision="2"
                  :min="0"
                  style="width: 100%"
                />
                <template #suffix>€</template>
              </n-form-item>
            </n-grid-item>
          </n-grid>

          <!-- Parts détenues -->
          <n-form-item label="Parts détenues" path="partsDetenues" class="mt-4">
            <n-input
              v-model:value="formData.partsDetenues"
              placeholder="N/A (entreprise individuelle)"
              clearable
            />
          </n-form-item>
        </n-card>

        <!-- Résumé -->
        <n-card title="Résumé" size="small" :class="isMobile ? 'mobile-card' : ''">
          <template v-if="!isMobile">
            <n-descriptions :column="1" bordered>
              <n-descriptions-item label="Total du capital">
                <span class="text-lg font-bold text-blue-600">
                  {{ formatCurrency(totalCapital) }}
                </span>
              </n-descriptions-item>
            </n-descriptions>
          </template>
          <template v-else>
            <div class="mobile-summary">
              <div class="summary-label">Total du capital</div>
              <div class="summary-value">{{ formatCurrency(totalCapital) }}</div>
            </div>
          </template>
        </n-card>

        <!-- Actions -->
        <div :class="isMobile ? 'mobile-actions' : 'flex justify-end space-x-4'">
          <n-button @click="resetForm" :disabled="saving">
            Réinitialiser
          </n-button>
          <n-button type="primary" @click="saveData" :loading="saving">
            {{ isEditing ? 'Mettre à jour' : 'Enregistrer' }}
          </n-button>
        </div>
      </div>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useMessage } from 'naive-ui'
import { useAuthStore } from '@/store/modules/auth'
import { capitalIdentitesApi } from '@/service/api/capital-identites'

definePage({
  meta: {
    title: '2033-D - Capital et identités',
    hideInMenu: true,
    activeMenu: '/liasse-fiscale',
  },
})

const message = useMessage()
const authStore = useAuthStore()

// Données réactives
const loading = ref(false)
const saving = ref(false)
const error = ref('')
const success = ref('')
const isEditing = ref(false)

// Détection mobile
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
if (typeof window !== 'undefined') {
  window.addEventListener('resize', handleResize)
}

// Référence du formulaire
const formRef = ref()

// Données du formulaire
const formData = ref({
  id: '',
  nomExploitant: '',
  prenomExploitant: '',
  adresseExploitant: '',
  codePostalExploitant: '',
  villeExploitant: '',
  qualite: 'Exploitant individuel',
  apportsNumeraires: 0,
  apportsNature: 0,
  apportsIndustrie: 0,
  partsDetenues: 'N/A',
  utilisateurId: ''
})

// Options pour la qualité
const qualiteOptions = [
  { label: 'Exploitant individuel', value: 'Exploitant individuel' },
  { label: 'Associé unique', value: 'Associé unique' },
  { label: 'Gérant', value: 'Gérant' }
]

// Règles de validation
const rules = {
  nomExploitant: {
    required: true,
    message: 'Le nom est obligatoire',
    trigger: 'blur'
  },
  prenomExploitant: {
    required: true,
    message: 'Le prénom est obligatoire',
    trigger: 'blur'
  },
  adresseExploitant: {
    required: true,
    message: 'L\'adresse est obligatoire',
    trigger: 'blur'
  },
  codePostalExploitant: {
    required: true,
    message: 'Le code postal est obligatoire',
    trigger: 'blur'
  },
  villeExploitant: {
    required: true,
    message: 'La ville est obligatoire',
    trigger: 'blur'
  },
  qualite: {
    required: true,
    message: 'La qualité est obligatoire',
    trigger: 'change'
  }
}

// Calcul du total du capital
const totalCapital = computed(() => {
  return (formData.value.apportsNumeraires || 0) + 
         (formData.value.apportsNature || 0) + 
         (formData.value.apportsIndustrie || 0)
})

// Formatage des montants
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2
  }).format(amount)
}

// Chargement des données
const loadData = async () => {
  if (!authStore.userInfo?.userId) return
  
  loading.value = true
  error.value = ''
  
  try {
    const response = await capitalIdentitesApi.getByUtilisateur(authStore.userInfo.userId)
    if (response) {
      formData.value = {
        id: response.id || '',
        nomExploitant: response.nomExploitant || '',
        prenomExploitant: response.prenomExploitant || '',
        adresseExploitant: response.adresseExploitant || '',
        codePostalExploitant: response.codePostalExploitant || '',
        villeExploitant: response.villeExploitant || '',
        qualite: response.qualite || 'Exploitant individuel',
        apportsNumeraires: response.apportsNumeraires || 0,
        apportsNature: response.apportsNature || 0,
        apportsIndustrie: response.apportsIndustrie || 0,
        partsDetenues: response.partsDetenues || 'N/A',
        utilisateurId: authStore.userInfo.userId
      }
      isEditing.value = true
    } else {
      // Initialiser avec les données de l'utilisateur connecté
      formData.value.utilisateurId = authStore.userInfo.userId
      formData.value.nomExploitant = authStore.userInfo.nom || ''
      formData.value.prenomExploitant = authStore.userInfo.prenom || ''
    }
  } catch (err) {
    console.error('Erreur lors du chargement des données:', err)
    error.value = 'Erreur lors du chargement des données'
    message.error('Erreur lors du chargement des données')
  } finally {
    loading.value = false
  }
}

// Sauvegarde des données
const saveData = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }
  
  saving.value = true
  error.value = ''
  success.value = ''
  
  try {
    const dataToSave = {
      ...formData.value,
      totalCapital: totalCapital.value
    }
    
    if (isEditing.value) {
      await capitalIdentitesApi.update(dataToSave)
      message.success('Données mises à jour avec succès')
    } else {
      await capitalIdentitesApi.create(dataToSave)
      message.success('Données enregistrées avec succès')
      isEditing.value = true
    }
    
    success.value = isEditing.value ? 'Données mises à jour avec succès' : 'Données enregistrées avec succès'
  } catch (err) {
    console.error('Erreur lors de la sauvegarde:', err)
    error.value = 'Erreur lors de la sauvegarde'
    message.error('Erreur lors de la sauvegarde')
  } finally {
    saving.value = false
  }
}

// Réinitialisation du formulaire
const resetForm = () => {
  formData.value = {
    id: '',
    nomExploitant: authStore.userInfo?.nom || '',
    prenomExploitant: authStore.userInfo?.prenom || '',
    adresseExploitant: '',
    codePostalExploitant: '',
    villeExploitant: '',
    qualite: 'Exploitant individuel',
    apportsNumeraires: 0,
    apportsNature: 0,
    apportsIndustrie: 0,
    partsDetenues: 'N/A',
    utilisateurId: authStore.userInfo?.userId || ''
  }
  isEditing.value = false
  error.value = ''
  success.value = ''
}

// Chargement initial
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.capital-identites-page {
  padding: 20px;
}

.n-form-item {
  margin-bottom: 16px;
}

.mobile-card {
  margin-bottom: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
  background: #fff;
  box-shadow: 0 1px 4px #0001;
}
.mobile-form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.mobile-summary {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
  padding: 8px 0;
}
.summary-label {
  font-size: 1em;
  color: #888;
}
.summary-value {
  font-size: 1.3em;
  font-weight: bold;
  color: #2563eb;
}
.mobile-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
  margin-top: 10px;
}
@media (max-width: 768px) {
  .mobile-card {
    font-size: 0.98em;
    padding: 10px 8px;
  }
}
</style> 