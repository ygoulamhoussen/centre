<template>
  <n-form
    ref="formRef"
    :model="formData"
    :rules="rules"
    label-placement="left"
    label-width="auto"
    require-mark-placement="right-hanging"
    size="large"
    @submit.prevent="save"
    class="p-4"
  >
    <n-grid :cols="24" :x-gap="24" :y-gap="16">
      <!-- L'ID de la quittance est généré par le backend -->
      <n-form-item-gi v-if="isEdit" :span="24" label="ID de la quittance">
        <n-input
          v-model:value="formData.quittanceId"
          placeholder="Généré par le système"
          disabled
        />
      </n-form-item-gi>
      
      <n-form-item-gi :span="24" label="Quittance" path="quittanceId" required>
        <n-select
          v-model:value="formData.quittanceId"
          :options="quittanceOptions"
          placeholder="Sélectionner une quittance"
          :disabled="isEdit"
          :loading="loadingQuittances"
          clearable
          filterable
          style="width: 100%"
        />
      </n-form-item-gi>

      <n-form-item-gi :span="24" label="Date de paiement" path="datePaiement" required>
        <n-date-picker
          v-model:formatted-value="formData.datePaiement"
          value-format="yyyy-MM-dd"
          type="date"
          style="width: 100%"
        />
      </n-form-item-gi>

      <n-form-item-gi :span="24" label="Montant" path="montant" required>
        <n-input-number
          v-model:value="formData.montant"
          placeholder="Entrez le montant"
          :min="0"
          :max="montantTotalQuittance"
          :step="0.01"
          :status="montantValide ? undefined : 'error'"
          style="width: 100%"
        >
          <template #suffix>€</template>
          <template v-if="!montantValide" #feedback>
            <n-text type="error">Le montant ne peut pas dépasser {{ montantTotalQuittance }}€</n-text>
          </template>
        </n-input-number>
        <n-text v-if="selectedQuittance" depth="3" class="ml-2 d-block mt-1">
          <div>Détail de la quittance :</div>
          <div class="ml-3">
            <div>Loyer : {{ selectedQuittance.montantLoyer }}€</div>
            <div>Charges : {{ selectedQuittance.montantCharges }}€</div>
            <div class="font-weight-medium">Total : {{ montantTotalQuittance }}€</div>
          </div>
        </n-text>
      </n-form-item-gi>

      <n-form-item-gi :span="24" label="Moyen de paiement" path="moyenPaiement" required>
        <n-select
          v-model:value="formData.moyenPaiement"
          :options="moyenPaiementOptions"
          placeholder="Sélectionnez un moyen de paiement"
          clearable
          filterable
          style="width: 100%"
        />
      </n-form-item-gi>

      <n-form-item-gi :span="24" label="Référence" path="reference">
        <n-input
          v-model:value="formData.reference"
          placeholder="Référence du paiement"
          clearable
        />
      </n-form-item-gi>

      <n-form-item-gi :span="24" label="Commentaire" path="commentaire">
        <n-input
          v-model:value="formData.commentaire"
          type="textarea"
          placeholder="Ajoutez un commentaire si nécessaire"
          :autosize="{ minRows: 2, maxRows: 5 }"
        />
      </n-form-item-gi>

      <n-form-item-gi :span="24" label="Statut" path="estValide">
        <n-switch v-model:value="formData.estValide">
          <template #checked>Validé</template>
          <template #unchecked>En attente</template>
        </n-switch>
      </n-form-item-gi>
    </n-grid>

    <div class="flex justify-end gap-3 mt-6">
      <n-button @click="closeForm">Annuler</n-button>
      <n-button type="primary" @click="save" :loading="isSaving">
        {{ isEdit ? 'Mettre à jour' : 'Enregistrer' }}
      </n-button>
    </div>
  </n-form>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue'
import { useMessage } from 'naive-ui'
import { useAuthStore } from '@/store/modules/auth'
import type { PaiementDTO } from '@/types/dto'

defineOptions({
  name: 'PaiementFormDialog',
})

const props = defineProps({
  paiement: {
    type: Object,
    default: () => ({}),
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits<{
  (e: 'saved', data: PaiementDTO): void
  (e: 'cancel'): void
}>()

const message = useMessage()
const formRef = ref<any>(null)
const isSaving = ref(false)

interface PaiementFormData {
  quittanceId: string
  datePaiement: string
  montant: number | null
  moyenPaiement: MoyenPaiement | null
  reference: string
  commentaire: string
  estValide: boolean
  utilisateurId: string
}

const formData = ref<PaiementFormData>({
  quittanceId: '',
  datePaiement: new Date().toISOString().split('T')[0], // Date du jour par défaut
  montant: null,
  moyenPaiement: null,
  reference: '',
  commentaire: '',
  estValide: false,
  utilisateurId: useAuthStore().userInfo?.userId || '',
})

// Gestion du timestamp pour le date-picker
const dateTimestamp = ref(Date.now())

const moyenPaiementOptions = [
  { label: 'Carte bancaire', value: 'CARTE' },
  { label: 'Virement', value: 'VIREMENT' },
  { label: 'Chèque', value: 'CHEQUE' },
  { label: 'Espèces', value: 'ESPECES' },
  { label: 'Autre', value: 'AUTRE' },
] as const

// Type pour les valeurs de moyen de paiement
type MoyenPaiement = typeof moyenPaiementOptions[number]['value']

const loadingQuittances = ref(false)
interface QuittanceOption {
  label: string
  value: string
}

const quittanceOptions = ref<QuittanceOption[]>([])
const selectedQuittance = ref<any>(null)

// Calculer le montant total de la quittance (loyer + charges)
const montantTotalQuittance = computed(() => {
  if (!selectedQuittance.value) return 0
  const loyer = parseFloat(selectedQuittance.value.montantLoyer || 0)
  const charges = parseFloat(selectedQuittance.value.montantCharges || 0)
  return (loyer + charges).toFixed(2)
})

// Vérifier si le montant saisi dépasse le montant de la quittance
const montantValide = computed(() => {
  if (!formData.value.montant || !selectedQuittance.value) return true
  return Number(formData.value.montant) <= Number(montantTotalQuittance.value)
})

// Déclarer le type pour les règles de validation
interface FormRules {
  [key: string]: Array<{
    required?: boolean
    type?: string
    min?: number
    message: string
    trigger: string
  }>
}

// Charger les quittances disponibles
async function loadQuittances() {
  try {
    loadingQuittances.value = true
    const authStore = useAuthStore()
    const userId = authStore.userInfo?.userId
    if (userId) {
      const response = await fetch(
        `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getQuittancesByUtilisateur/${userId}`,
        {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include'
        }
      )
      if (response.ok) {
        const quittances = await response.json()
        quittanceOptions.value = quittances.map((q: any) => ({
          label: `Quittance #${q.id} - ${q.montantTotal}€`,
          value: q.id,
          ...q // Conserver toutes les propriétés de la quittance
        }))
      }
    }
  } catch (error) {
    console.error('Erreur lors du chargement des quittances:', error)
    message.error('Erreur lors du chargement des quittances')
  } finally {
    loadingQuittances.value = false
  }
}

const rules: FormRules = {
  quittanceId: [
    { required: true, message: 'La quittance est requise', trigger: 'change' }
  ],
  montant: [
    { required: true, message: 'Le montant est requis', trigger: 'blur' },
    {
      validator: (_, value) => {
        if (value === null || value === undefined || value === '') return true
        const numValue = Number(value)
        return !isNaN(numValue) && numValue > 0 && numValue <= Number(montantTotalQuittance.value)
      },
      message: `Le montant doit être supérieur à 0 et ne pas dépasser le montant de la quittance`,
      trigger: ['blur', 'change']
    }
  ],
  datePaiement: [
    { required: true, message: 'La date de paiement est requise', trigger: 'blur' },
  ],
  montant: [
    { required: true, type: 'number', min: 0.01, message: 'Le montant doit être supérieur à 0', trigger: 'blur' },
  ],
  moyenPaiement: [
    { required: true, message: 'Le moyen de paiement est requis', trigger: 'change' },
  ],
  reference: [
    { required: true, message: 'La référence est requise', trigger: 'blur' },
  ],
}

// Initialiser le formulaire avec les données du paiement si en mode édition
onMounted(() => {
  if (props.isEdit && props.paiement) {
    formData.value = { 
      ...props.paiement,
      // Convertir les valeurs si nécessaire
      montant: props.paiement.montant ? parseFloat(props.paiement.montant) : null,
      estValide: props.paiement.estValide === 'true' || props.paiement.estValide === true
    }
    // Mettre à jour le timestamp si une date est fournie
    if (props.paiement.datePaiement) {
      dateTimestamp.value = new Date(props.paiement.datePaiement).getTime()
    }
  } else {
    // Définir la date du jour par défaut pour les nouveaux paiements
    formData.value.datePaiement = new Date().toISOString().split('T')[0]
    dateTimestamp.value = Date.now()
  }
})

// Réinitialiser le formulaire quand le dialogue est fermé
watch(
  () => props.modelValue,
  (newVal) => {
    if (!newVal) {
      formData.value = {
        quittanceId: '',
        datePaiement: '',
        montant: null,
        moyenPaiement: null,
        reference: '',
        commentaire: '',
        estValide: false,
        utilisateurId: '',
      }
      emit('closed')
    }
  },
)

async function save() {
  try {
    isSaving.value = true
    
    // Valider le formulaire
    const valid = await formRef.value?.validate()
    if (!valid) {
      return
    }

    // Vérifier que les champs requis sont présents
    if (!formData.value.quittanceId) {
      message.error('Veuillez sélectionner une quissance')
      return
    }

    // Formater les données avant l'envoi
    const formDataToEmit: PaiementDTO = {
      id: props.paiement?.id,
      quittanceId: formData.value.quittanceId,
      datePaiement: formData.value.datePaiement,
      montant: formData.value.montant?.toString() || '0',
      moyenPaiement: formData.value.moyenPaiement || '',
      reference: formData.value.reference,
      commentaire: formData.value.commentaire,
      estValide: formData.value.estValide ? 'true' : 'false',
      utilisateurId: useAuthStore().userInfo?.userId || ''
    }
    
    // Émettre l'événement avec les données du formulaire
    emit('saved', formDataToEmit)
  } catch (error) {
    console.error('Erreur lors de la validation du formulaire:', error)
    const errorMessage = error instanceof Error ? error.message : 'Une erreur est survenue lors de la validation du formulaire'
    message.error(errorMessage)
  } finally {
    isSaving.value = false
  }
};

// Réinitialiser le formulaire pour un nouvel enregistrement
function resetForm() {
  if (!props.isEdit) {
    formData.value = {
      quittanceId: '',
      datePaiement: new Date().toISOString().split('T')[0],
      montant: null,
      moyenPaiement: null,
      reference: '',
      commentaire: '',
      estValide: false,
      utilisateurId: useAuthStore().userInfo?.userId || ''
    }
  }
}

// Mettre à jour les données du formulaire quand une quittance est sélectionnée
watch(() => formData.value.quittanceId, (newQuittanceId) => {
  if (newQuittanceId) {
    const quittance = quittanceOptions.value.find(q => q.value === newQuittanceId)
    if (quittance) {
      selectedQuittance.value = quittance
      // Si c'est un nouvel enregistrement, on peut pré-remplir le montant avec le montant total
      if (!props.isEdit) {
        formData.value.montant = Number(quittance.montantTotal)
      }
    }
  } else {
    selectedQuittance.value = null
  }
})

// Charger les quittances au montage du composant
onMounted(() => {
  loadQuittances()
  resetForm()
})

// Réinitialiser le formulaire quand on passe en mode édition
watch(() => props.isEdit, (newVal) => {
  if (newVal && props.paiement) {
    formData.value = {
      ...props.paiement,
      montant: props.paiement.montant ? parseFloat(props.paiement.montant) : null,
      estValide: props.paiement.estValide === 'true'
    }
  } else {
    resetForm()
  }
}, { immediate: true })

// Fermer le formulaire
function closeForm() {
  emit('cancel')
  resetForm()
}
</script>
