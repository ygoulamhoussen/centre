<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { NSpace, NButton, NInput, NForm, NFormItem, useMessage } from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter un locataire - Étape 2',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)
const router = useRouter()

function precedent() {
  router.push('/locataire_workflow/etape-1')
}

function suivant() {
  router.push('/locataire_workflow/etape-3')
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter un locataire - Étape 2</h1>
    <NForm>
      <NFormItem label="Adresse">
        <NInput v-model:value="locataireDTO.adresse" placeholder="Adresse" />
      </NFormItem>
      <NFormItem label="Complément d'adresse">
        <NInput v-model:value="locataireDTO.complementAdresse" placeholder="Complément" />
      </NFormItem>
      <NFormItem label="Code postal">
        <NInput v-model:value="locataireDTO.codePostal" placeholder="Code postal" />
      </NFormItem>
      <NFormItem label="Ville">
        <NInput v-model:value="locataireDTO.ville" placeholder="Ville" />
      </NFormItem>
    </NForm>
    <div class="flex justify-between">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>
