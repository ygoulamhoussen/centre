<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowLeft24Filled, Checkmark24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDescriptions,
  NDescriptionsItem,
  NH2,
  NIcon,
  NSpace,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

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
const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

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
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="3" class="mb-8">
          <NStep title="Informations" description="Nom, contact, etc." />
          <NStep title="Adresse" description="Lieu de résidence" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>

      <NH2 class="titre-principal mb-4">Étape 3: Récapitulatif</NH2>

      <NDescriptions label-placement="top" bordered :column="isMobile ? 1 : 2">
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

      <NSpace justify="center" class="mt-8">
        <NButton @click="precedent" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
        <NButton type="primary" @click="enregistrer" :loading="chargement" title="Enregistrer">
          <template #icon>
            <NIcon :component="Checkmark24Filled" />
          </template>
        </NButton>
      </NSpace>
    </NCard>
  </div>
</template>

<style scoped>
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
@media (max-width: 768px) {
  .mb-8 {
    margin-bottom: 1rem !important;
  }
  .n-steps {
    font-size: 12px !important;
    min-width: 400px;
  }
  .n-steps,
  .n-steps .n-steps-main {
    overflow-x: auto !important;
    white-space: nowrap !important;
    display: block !important;
  }
  .n-step {
    min-width: 120px !important;
  }
  .n-step__description {
    display: none !important;
  }
  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-4,
  .mb-6,
  .mb-8 {
    margin-bottom: 1rem !important;
  }
}
</style>
