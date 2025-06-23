<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NSpace,
  useMessage,
  NCard,
  NSteps,
  NStep,
  NIcon,
  NDescriptions,
  NDescriptionsItem,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft24Filled, Save24Filled } from '@vicons/fluent'

definePage({
  meta: {
    title: 'Nouveau locataire - Récapitulatif',
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
          utilisateurId,
        }),
      },
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
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="3" class="mb-8">
        <NStep title="Informations" description="Nom, contact, etc." />
        <NStep title="Adresse" description="Lieu de résidence" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-4">Étape 3: Récapitulatif</h2>

      <NDescriptions label-placement="top" bordered :column="2">
        <NDescriptionsItem label="Nom complet">
          {{ locataireDTO.nom }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Téléphone">
          {{ locataireDTO.telephone }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Email">
          {{ locataireDTO.email }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Adresse">
          {{ `${locataireDTO.adresse}, ${locataireDTO.codePostal} ${locataireDTO.ville}` }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Complément d'adresse" :span="2">
          {{ locataireDTO.complementAdresse || 'N/A' }}
        </NDescriptionsItem>
      </NDescriptions>

      <NSpace class="mt-8" justify="space-between">
        <NButton @click="precedent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" @click="enregistrer" :loading="chargement">
          <template #icon>
            <NIcon :component="Save24Filled" />
          </template>
          Enregistrer
        </NButton>
      </NSpace>
    </NCard>
  </div>
</template>

<style scoped>
/* rien à personnaliser ici */
</style>
