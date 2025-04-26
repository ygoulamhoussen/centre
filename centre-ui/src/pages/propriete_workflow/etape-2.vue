<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

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

function precedent() {
  router.push('/propriete_workflow/etape-1')
}

function valider() {
  router.push('/propriete_workflow/etape-3')
}
</script>

<template>
  <n-space vertical :size="24">
    <n-h1>Ajouter une propriété - Étape 2</n-h1>

    <n-text strong class="block">Adresse du bien</n-text>

    <n-form :model="proprieteDTO" label-placement="top">
      <n-form-item label="Adresse" path="adresse">
        <n-input v-model:value="proprieteDTO.adresse" placeholder="Adresse..." />
      </n-form-item>

      <n-form-item label="Complément d'adresse" path="complementAdresse">
        <n-input v-model:value="proprieteDTO.complementAdresse" placeholder="Étage, bâtiment, etc." />
      </n-form-item>

      <n-form-item label="Code postal" path="codePostal">
        <n-input v-model:value="proprieteDTO.codePostal" placeholder="75000" />
      </n-form-item>

      <n-form-item label="Ville" path="ville">
        <n-input v-model:value="proprieteDTO.ville" placeholder="Paris" />
      </n-form-item>
    </n-form>

    <div class="flex justify-between">
      <n-button @click="precedent" secondary>Précédent</n-button>
      <n-button type="primary" @click="valider">Suivant</n-button>
    </div>
  </n-space>
</template>
