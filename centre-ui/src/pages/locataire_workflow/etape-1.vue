<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { useMessage } from 'naive-ui'
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'

definePage({
  meta: {
    title: 'Ajouter un locataire - Étape 1',
    hideInMenu: true,
  },
})

const router = useRouter()
const message = useMessage()

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)

function suivant() {
  if (!locataireDTO.value.nom || !locataireDTO.value.telephone || !locataireDTO.value.email) {
    message.warning('Veuillez remplir tous les champs.')
    return
  }

  router.push('/locataire_workflow/etape-2') // À adapter selon ta structure
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter un locataire - Étape 1</h1>

    <div>
      <label class="block mb-1">Nom du locataire</label>
      <NInput v-model:value="locataireDTO.nom" placeholder="Ex : Jean Dupont" />
    </div>

    <div>
      <label class="block mb-1">Téléphone</label>
      <NInput v-model:value="locataireDTO.telephone" placeholder="Ex : 0601020304" />
    </div>

    <div>
      <label class="block mb-1">Email</label>
      <NInput v-model:value="locataireDTO.email" placeholder="Ex : jean.dupont@email.com" />
    </div>

    <div class="flex justify-end">
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>
