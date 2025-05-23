<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NForm,
  NFormItem,
  NH1,
  NSpace,
  NText,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Ajouter un locataire - Étape 3',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId

const router = useRouter()
const message = useMessage()
const chargement = ref(false)

async function enregistrer() {
  chargement.value = true
  try {
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createLocataire`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...locataireDTO.value,
          utilisateurId
        })
      }
    )
    if (!response.ok) {
      throw new Error(`Erreur lors de la création du locataire (${response.status})`)
    }
    message.success('Locataire enregistré avec succès !')
    store.resetLocataireDTO()
    router.push('/locataire')
  } catch (e: any) {
    console.error(e)
    message.error(e.message || 'Erreur inconnue')
  } finally {
    chargement.value = false
  }
}

function precedent() {
  router.push('/locataire-etape-2')
}
</script>

<template>
  <NSpace vertical :size="24">
    <NH1>Ajouter un locataire - Étape 3</NH1>

    <NForm label-placement="top" :show-require-mark="false">
      <NFormItem label="Nom complet">
        <NText>{{ locataireDTO.nom }}</NText>
      </NFormItem>
      <NFormItem label="Téléphone">
        <NText>{{ locataireDTO.telephone }}</NText>
      </NFormItem>
      <NFormItem label="Email">
        <NText>{{ locataireDTO.email }}</NText>
      </NFormItem>
      <NFormItem label="Adresse">
        <NText>{{ locataireDTO.adresse }}</NText>
      </NFormItem>
      <NFormItem label="Complément d'adresse">
        <NText>{{ locataireDTO.complementAdresse }}</NText>
      </NFormItem>
      <NFormItem label="Code postal">
        <NText>{{ locataireDTO.codePostal }}</NText>
      </NFormItem>
      <NFormItem label="Ville">
        <NText>{{ locataireDTO.ville }}</NText>
      </NFormItem>
    </NForm>

    <div class="flex justify-end gap-3">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="enregistrer" :loading="chargement">
        Enregistrer
      </NButton>
    </div>
  </NSpace>
</template>

<style scoped>
/* rien à personnaliser ici */
</style>
