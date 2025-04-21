<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import {
  NSpace,
  NInputNumber,
  NDatePicker,
  NButton,
  NForm,
  NFormItem,
  useMessage,
  NH1,
  NText
} from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter une propriété - Étape 3',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const erreur = ref('')

function precedent() {
  router.push('/propriete_workflow/etape-2')
}

function suivant() {
  if (!proprieteDTO.value.dateAcquisition || !proprieteDTO.value.montantAcquisition) {
    message.warning('Veuillez remplir les champs obligatoires.')
    return
  }

  router.push('/propriete_workflow/etape-4')
}
</script>

<template>
  <NSpace vertical :size="24">
    <n-h1>Ajouter une propriété - Étape 3</n-h1>
    <n-text depth="3">Détails de l'acquisition</n-text>

    <n-form label-placement="top">
      <n-form-item label="Date d'acquisition *">
        <n-date-picker
          v-model:formatted-value="proprieteDTO.dateAcquisition"
          value-format="yyyy-MM-dd"
          type="date"
          clearable
        />
      </n-form-item>

      <n-form-item label="Montant d'acquisition (€) *">
        <n-input-number
          v-model:value="proprieteDTO.montantAcquisition"
          min="0"
          placeholder="0.00"
        />
      </n-form-item>

      <n-form-item label="Frais de notaire (€)">
        <n-input-number
          v-model:value="proprieteDTO.fraisNotaire"
          min="0"
          placeholder="0.00"
        />
      </n-form-item>

      <n-form-item label="Frais d'agence (€)">
        <n-input-number
          v-model:value="proprieteDTO.fraisAgence"
          min="0"
          placeholder="0.00"
        />
      </n-form-item>

      <n-form-item label="Tantième">
        <n-input-number
          v-model:value="proprieteDTO.tantieme"
          min="0"
          step="1"
          placeholder="ex: 1000"
        />
      </n-form-item>
    </n-form>

    <div class="flex justify-between pt-2">
      <n-button @click="precedent">Précédent</n-button>
      <n-button type="primary" @click="suivant">Suivant</n-button>
    </div>

    <n-text v-if="erreur" type="error" class="text-center">{{ erreur }}</n-text>
  </NSpace>
</template>
