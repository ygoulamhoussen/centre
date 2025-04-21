<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { NInput, NButton, useMessage, NSpace } from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter une propriété - Étape 2',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

function valider() {
  if (!proprieteDTO.value.adresse || !proprieteDTO.value.codePostal || !proprieteDTO.value.ville) {
    message.warning('Veuillez remplir tous les champs obligatoires.')
    return
  }

  router.push('/propriete_workflow/etape-3')
}

function precedent() {
  router.push('/propriete_workflow/etape-1')
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter une propriété - Étape 2</h1>
    <p class="text-gray-500">Adresse du bien</p>

    <div>
      <label class="block mb-1">Adresse *</label>
      <NInput v-model:value="proprieteDTO.adresse" />
    </div>

    <div>
      <label class="block mb-1">Complément d'adresse</label>
      <NInput v-model:value="proprieteDTO.complementAdresse" />
    </div>

    <div>
      <label class="block mb-1">Code postal *</label>
      <NInput v-model:value="proprieteDTO.codePostal" />
    </div>

    <div>
      <label class="block mb-1">Ville *</label>
      <NInput v-model:value="proprieteDTO.ville" />
    </div>

    <div class="flex justify-between pt-2">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="valider">Suivant</NButton>
    </div>
  </NSpace>
</template>

<style scoped></style>
