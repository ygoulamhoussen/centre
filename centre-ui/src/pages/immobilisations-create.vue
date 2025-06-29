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

    <NCard title="Informations de l'immobilisation" class="form-card">
      <NForm
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <NFormItem label="Intitulé" path="intitule">
          <NInput v-model:value="formData.intitule" placeholder="Ex: Rénovation cuisine" />
        </NFormItem>

        <NFormItem label="Montant" path="montant">
          <NInputNumber
            v-model:value="formData.montant"
            placeholder="0.00"
            :precision="2"
            :min="0"
            style="width: 100%"
          />
        </NFormItem>

        <NFormItem label="Date d'acquisition" path="dateAcquisition">
          <NDatePicker
            v-model:value="formData.dateAcquisition"
            type="date"
            style="width: 100%"
          />
        </NFormItem>

        <NFormItem label="Type d'immobilisation" path="typeImmobilisation">
          <NSelect
            v-model:value="formData.typeImmobilisation"
            :options="typeImmobilisationOptions"
            placeholder="Sélectionner un type"
            @update:value="onTypeChange"
          />
        </NFormItem>

        <NFormItem label="Catégorie fiscale" path="categorieFiscale">
          <NSelect
            v-model:value="formData.categorieFiscale"
            :options="categorieFiscaleOptions"
            placeholder="Sélectionner une catégorie"
            @update:value="onCategorieChange"
          />
        </NFormItem>

        <NFormItem
          v-if="formData.typeImmobilisation === 'BIEN_IMMOBILIER'"
          label="Valeur du terrain"
          path="valeurTerrain"
        >
          <NInputNumber
            v-model:value="formData.valeurTerrain"
            placeholder="0.00"
            :precision="2"
            :min="0"
            style="width: 100%"
          />
        </NFormItem>

        <NFormItem label="Propriété" path="proprieteId">
          <NSelect
            v-model:value="formData.proprieteId"
            :options="proprieteOptions"
            placeholder="Sélectionner une propriété"
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

      <div class="form-actions">
        <NButton @click="goBack">Annuler</NButton>
        <NButton type="primary" :loading="saving" @click="saveImmobilisation">
          Enregistrer
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

const router = useRouter()
const message = useMessage()

// État réactif
const saving = ref(false)
const proprietes = ref<any[]>([])

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
  proprieteId: {
    required: true,
    message: 'La propriété est requise',
    trigger: 'blur',
  },
}

// Options pour les selects
const proprieteOptions = computed(() =>
  proprietes.value.map((p: any) => ({ label: p.nom, value: p.id }))
)

const typeImmobilisationOptions = computed(() =>
  Object.entries(TYPE_IMMOBILISATION_LABELS).map(([value, label]) => ({ label, value }))
)

const categorieFiscaleOptions = computed(() =>
  Object.entries(CATEGORIE_FISCALE_LABELS).map(([value, label]) => ({ label, value }))
)

// Méthodes
function goBack() {
  router.push('/immobilisations')
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

    const data = {
      ...formData.value,
      montant: formData.value.montant.toString(),
      dateAcquisition: formData.value.dateAcquisition ? new Date(formData.value.dateAcquisition).toISOString().split('T')[0] : '',
      valeurTerrain: formData.value.valeurTerrain ? formData.value.valeurTerrain.toString() : undefined,
      dureeAmortissement: formData.value.dureeAmortissement,
      typeImmobilisation: formData.value.typeImmobilisation,
      categorieFiscale: formData.value.categorieFiscale,
    }

    console.error('=== CRÉATION IMMOBILISATION ===')
    console.error('Données à envoyer:', data)
    
    const result = await immobilisationApi.createImmobilisation(data)
    console.error('Résultat de la création:', result)

    message.success('Immobilisation créée avec succès')
    router.push('/immobilisations')
  } catch (error) {
    console.error('Erreur complète:', error)
    message.error('Erreur lors de la création de l\'immobilisation')
    console.error(error)
  } finally {
    saving.value = false
  }
}

function onTypeChange(value: TypeImmobilisation) {
  // Réinitialiser la catégorie fiscale quand le type change
  formData.value.categorieFiscale = null
}

function onCategorieChange(value: CategorieFiscale) {
  // Mettre à jour la durée d'amortissement automatiquement
  if (value) {
    formData.value.dureeAmortissement = CATEGORIE_FISCALE_DUREES[value].toString()
  }
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

.form-card {
  max-width: 800px;
  margin: 0 auto;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}
</style> 