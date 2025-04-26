<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { NSpace, NButton, NInput, NForm, NFormItem, useMessage } from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter un locataire - Étape 1',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)
const router = useRouter()

function suivant() {
  if (!locataireDTO.value.nom || !locataireDTO.value.telephone || !locataireDTO.value.email) {
    useMessage().warning('Veuillez remplir tous les champs obligatoires.')
    return
  }
  router.push('/locataire_workflow/etape-2')
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter un locataire - Étape 1</h1>
    <NForm>
      <NFormItem label="Nom complet *">
        <NInput v-model:value="locataireDTO.nom" placeholder="Nom du locataire" />
      </NFormItem>
      <NFormItem label="Téléphone *">
        <NInput v-model:value="locataireDTO.telephone" placeholder="Numéro de téléphone" />
      </NFormItem>
      <NFormItem label="Email *">
        <NInput v-model:value="locataireDTO.email" placeholder="Adresse email" />
      </NFormItem>
    </NForm>
    <div class="flex justify-end">
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>
