<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { useMessage, NInput, NButton, NSpace } from 'naive-ui'
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'

definePage({
  meta: {
    title: 'Ajouter un locataire - Étape 2',
    hideInMenu: true,
  },
})

const router = useRouter()
const message = useMessage()

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)

function precedent() {
  router.push('/locataire_workflow/etape-1')
}

function suivant() {
  if (!locataireDTO.value.adresse || !locataireDTO.value.codePostal || !locataireDTO.value.ville) {
    message.warning('Veuillez remplir tous les champs requis.')
    return
  }

  router.push('/locataire_workflow/etape-3') // Étape suivante à adapter si besoin
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter un locataire - Étape 2</h1>

    <div>
      <label class="block mb-1">Adresse</label>
      <NInput v-model:value="locataireDTO.adresse" placeholder="Ex : 10 rue des Lilas" />
    </div>

    <div>
      <label class="block mb-1">Complément d'adresse</label>
      <NInput v-model:value="locataireDTO.complementAdresse" placeholder="Bâtiment, étage..." />
    </div>

    <div>
      <label class="block mb-1">Code postal</label>
      <NInput v-model:value="locataireDTO.codePostal" placeholder="Ex : 75015" />
    </div>

    <div>
      <label class="block mb-1">Ville</label>
      <NInput v-model:value="locataireDTO.ville" placeholder="Ex : Paris" />
    </div>

    <div class="flex justify-between">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>
