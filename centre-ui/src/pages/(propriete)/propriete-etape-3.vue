<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NDatePicker,
  NForm,
  NFormItem,
  NH1,
  NInputNumber,
  NSpace,
  NText,
  NGrid,
  NGi,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

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
  router.push('/propriete-etape-2')
}

function suivant() {
  if (!proprieteDTO.value.dateAcquisition || !proprieteDTO.value.montantAcquisition) {
    message.warning('Veuillez remplir les champs obligatoires.')
    return
  }

  router.push('/propriete-etape-4')
}
</script>

<template>
  <n-space vertical :size="24">
    <n-h1>Ajouter une propriété - Étape 3</n-h1>
    <n-text depth="3">Détails de l'acquisition</n-text>

    <n-form label-placement="top">
      <n-grid cols="1 s:1 m:2 l:2" x-gap="24" y-gap="16">
        <n-gi>
          <n-form-item label="Date d'acquisition *">
            <n-date-picker
              v-model:formatted-value="proprieteDTO.dateAcquisition"
              value-format="yyyy-MM-dd"
              type="date"
              clearable
              style="width: 100%"
            />
          </n-form-item>
        </n-gi>

        <n-gi>
          <n-form-item label="Montant d'acquisition (€) *">
            <n-input-number
              v-model:value="proprieteDTO.montantAcquisition"
              min="0"
              placeholder="0.00"
              style="width: 100%"
            />
          </n-form-item>
        </n-gi>

        <n-gi>
          <n-form-item label="Frais de notaire (€)">
            <n-input-number
              v-model:value="proprieteDTO.fraisNotaire"
              min="0"
              placeholder="0.00"
              style="width: 100%"
            />
          </n-form-item>
        </n-gi>

        <n-gi>
          <n-form-item label="Frais d'agence (€)">
            <n-input-number
              v-model:value="proprieteDTO.fraisAgence"
              min="0"
              placeholder="0.00"
              style="width: 100%"
            />
          </n-form-item>
        </n-gi>

        <n-gi :span="2">
          <n-form-item label="Tantième">
            <n-input-number
              v-model:value="proprieteDTO.tantieme"
              min="0"
              step="1"
              placeholder="ex: 1000"
              style="width: 100%"
            />
          </n-form-item>
        </n-gi>
      </n-grid>
    </n-form>

    <div class="flex justify-between pt-2">
      <n-button @click="precedent">Précédent</n-button>
      <n-button type="primary" @click="suivant">Suivant</n-button>
    </div>

    <n-text v-if="erreur" type="error" class="text-center">{{ erreur }}</n-text>
  </n-space>
</template>

<style scoped></style>
