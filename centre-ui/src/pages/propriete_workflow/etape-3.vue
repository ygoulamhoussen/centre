<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import {
  NSpace,
  NInput,
  NInputNumber,
  NDatePicker,
  NButton,
  useMessage
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
  router.push('/propriete/etape-2')
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
    <h1 class="text-xl font-bold">Ajouter une propriété - Étape 3</h1>

    <p class="text-gray-500">Détails de l'acquisition</p>

    <div>
      <label class="block mb-1">Date d'acquisition *</label>
      <NDatePicker
        v-model:formatted-value="proprieteDTO.dateAcquisition"
        value-format="yyyy-MM-dd"
        type="date"
        clearable
      />
    </div>

    <div>
      <label class="block mb-1">Montant d'acquisition (€) *</label>
      <NInputNumber
        v-model:value="proprieteDTO.montantAcquisition"
        min="0"
        placeholder="0.00"
      />
    </div>

    <div>
      <label class="block mb-1">Frais de notaire (€)</label>
      <NInputNumber
        v-model:value="proprieteDTO.fraisNotaire"
        min="0"
        placeholder="0.00"
      />
    </div>

    <div>
      <label class="block mb-1">Frais d'agence (€)</label>
      <NInputNumber
        v-model:value="proprieteDTO.fraisAgence"
        min="0"
        placeholder="0.00"
      />
    </div>

    <div>
      <label class="block mb-1">Tantième</label>
      <NInputNumber
        v-model:value="proprieteDTO.tantieme"
        min="0"
        step="1"
        placeholder="ex: 1000"
      />
    </div>

    <div class="flex justify-between pt-2">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>

    <p v-if="erreur" class="text-red-500 text-center mt-2">{{ erreur }}</p>
  </NSpace>
</template>

<style scoped></style>
