<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useAuthStore } from '@/store/modules/auth'
import { useRouter } from 'vue-router'
import {
  NSpace,
  NH1,
  NButton,
  NDescriptions,
  NDescriptionsItem,
  useMessage
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'

definePage({
  meta: {
    title: 'Nouvelle location - Récapitulatif',
    hideInMenu: true
  }
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId

const router = useRouter()
const message = useMessage()
const chargement = ref(false)

async function enregistrer() {
  chargement.value = true
  try {
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createLocation`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...locationDTO.value,
          utilisateurId
        })
      }
    )
    if (!response.ok) {
      throw new Error(`Erreur serveur (${response.status})`)
    }
    message.success('Location créée avec succès !')
    store.resetLocationDTO()
    router.push('/location')
  } catch (error: any) {
    console.error(error)
    message.error(error.message || 'Erreur lors de l’enregistrement')
  } finally {
    chargement.value = false
  }
}

function precedent() {
  router.push('/location-etape-2')
}
</script>

<template>
  <NSpace vertical :size="24">
    <NH1>Nouvelle location - Récapitulatif</NH1>

    <NDescriptions label-placement="top" bordered>
      <NDescriptionsItem label="Propriété">
        {{ locationDTO.proprieteId }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Locataire">
        {{ locationDTO.locataireId }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Date début">
        {{ locationDTO.dateDebut }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Date fin">
        {{ locationDTO.dateFin || 'Non définie' }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Loyer (€)">
        {{ locationDTO.loyerMensuel }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Charges (€)">
        {{ locationDTO.chargesMensuelles }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Dépôt de garantie (€)">
        {{ locationDTO.depotGarantie }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Fréquence">
        {{ locationDTO.frequenceLoyer }}
      </NDescriptionsItem>
      <NDescriptionsItem label="Jour échéance">
        {{ locationDTO.jourEcheance }}
      </NDescriptionsItem>
    </NDescriptions>

    <div class="flex justify-between pt-4">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" :loading="chargement" @click="enregistrer">
        Enregistrer
      </NButton>
    </div>
  </NSpace>
</template>
