<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth' // ajout pour récupérer l'utilisateur connecté
import { useUnifiedStore } from '@/store/unifiedStore'
import { NButton, NForm, NFormItem, NH1, NSelect, NSpace, useMessage } from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle location - Étape 1',
    hideInMenu: true
  }
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId // 👈 récupération de l'ID connecté

const router = useRouter()
const message = useMessage()

const proprietes = ref<any[]>([])
const locataires = ref<any[]>([])

async function fetchData() {
  try {
    const propResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${utilisateurId}`)
    proprietes.value = await propResponse.json()

    const locResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/${utilisateurId}`)
    locataires.value = await locResponse.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement')
  }
}

function suivant() {
  if (!locationDTO.value.proprieteId || !locationDTO.value.locataireId) {
    message.warning('Merci de choisir une propriété et un locataire')
    return
  }
  router.push('/location-etape-2')
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <NSpace vertical :size="24">
    <NH1>Nouvelle location - Étape 1</NH1>
{{ utilisateurId }}
    <NForm label-placement="top">
      <NFormItem label="Propriété">
        <NSelect
          v-model:value="locationDTO.proprieteId"
          :options="proprietes.map(p => ({ label: p.nom, value: p.id }))"
          placeholder="Choisir une propriété"
        />
      </NFormItem>

      <NFormItem label="Locataire">
        <NSelect
          v-model:value="locationDTO.locataireId"
          :options="locataires.map(l => ({ label: l.nom, value: l.id }))"
          placeholder="Choisir un locataire"
        />
      </NFormItem>
    </NForm>

    <div class="flex justify-end gap-2">
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>
