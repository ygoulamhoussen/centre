<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  Save24Filled,
} from '@vicons/fluent'
import {
  NAlert,
  NButton,
  NCard,
  NDescriptions,
  NDescriptionsItem,
  NForm,
  NFormItem,
  NGi,
  NGrid,
  NIcon,
  NInputNumber,
  NRadio,
  NRadioGroup,
  NSpace,
  NStep,
  NSteps,
  NText,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle propriété - Récapitulatif',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()

const chargement = ref(false)

function precedent() {
  router.push('/propriete-etape-3')
}

async function enregistrer() {
  chargement.value = true
  try {
    const utilisateurId = authStore.userInfo.userId
    const finalDTO = {
      ...proprieteDTO.value,
      dateAcquisition: proprieteDTO.value.dateAcquisition || null,
      montantAcquisition: proprieteDTO.value.montantAcquisition || '0',
      fraisNotaire: proprieteDTO.value.fraisNotaire || '0',
      fraisAgence: proprieteDTO.value.fraisAgence || '0',
      tantieme: proprieteDTO.value.tantieme || '0',
    }
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createPropriete/${utilisateurId}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(finalDTO),
      },
    )
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || 'Erreur lors de la sauvegarde de la propriété')
    }
    store.resetProprieteDTO()
    message.success('Propriété créée avec succès')
    router.push('/propriete')
  }
  catch (e: any) {
    message.error(e.message || 'Erreur inconnue lors de la création de la propriété')
  }
  finally {
    chargement.value = false
  }
}

const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps v-if="!isMobile" :current="4" class="mb-8">
          <NStep title="Type et Nom" description="Identification du bien" />
          <NStep title="Adresse" description="Localisation du bien" />
          <NStep title="Détails" description="Informations financières" />
          <NStep title="Récapitulatif" description="Vérification et sauvegarde" />
        </NSteps>
        <div v-else class="stepper-mobile mb-8">
          Étape 4/4 : Récapitulatif
        </div>
      </div>

      <NH2 class="titre-principal mb-6">Étape 4: Récapitulatif et Amortissement</NH2>

      <NDescriptions label-placement="top" bordered :columns="2" title="Récapitulatif de la propriété">
        <NDescriptionsItem label="Nom de la propriété" :label-style="{ fontWeight: 'bold' }">{{
          proprieteDTO.nom
        }}</NDescriptionsItem>
        <NDescriptionsItem label="Type de bien" :label-style="{ fontWeight: 'bold' }">{{
          proprieteDTO.typeBien
        }}</NDescriptionsItem>
        <NDescriptionsItem label="Adresse" :label-style="{ fontWeight: 'bold' }" :span="2">
          {{ proprieteDTO.adresse }}, {{ proprieteDTO.codePostal }} {{ proprieteDTO.ville }}
          <br>
          <span v-if="proprieteDTO.complementAdresse">{{ proprieteDTO.complementAdresse }}</span>
        </NDescriptionsItem>
        <NDescriptionsItem label="Date d'acquisition">{{ proprieteDTO.dateAcquisition || 'Non spécifiée' }}</NDescriptionsItem>
        <NDescriptionsItem label="Montant d'acquisition">{{ proprieteDTO.montantAcquisition }} €</NDescriptionsItem>
        <NDescriptionsItem label="Frais de notaire">{{ proprieteDTO.fraisNotaire || 0 }} €</NDescriptionsItem>
        <NDescriptionsItem label="Frais d'agence">{{ proprieteDTO.fraisAgence || 0 }} €</NDescriptionsItem>
        <NDescriptionsItem label="Tantième">{{ proprieteDTO.tantieme || 'Non spécifié' }}</NDescriptionsItem>
      </NDescriptions>

      <NSpace class="mt-8" justify="space-between">
        <NButton @click="precedent" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
        <NButton type="primary" @click="enregistrer" :loading="chargement" title="Enregistrer la propriété">
          <template #icon>
            <NIcon :component="Save24Filled" />
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
.stepper-mobile {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 1.1rem;
  background: var(--n-color-embedded, #f5f5fa);
  border-radius: 12px;
  padding: 0.75rem 1.25rem;
  margin-bottom: 1rem;
  color: var(--n-text-color);
}
@media (max-width: 768px) {
  .mb-8 {
    margin-bottom: 1rem !important;
  }
  .steps-wrapper {
    overflow-x: auto !important;
  }
  .n-steps {
    font-size: 12px !important;
    min-width: 400px;
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
  .form-grid {
    grid-template-columns: 1fr !important;
  }
  .form-grid .n-form-item-gi {
    grid-column: 1 !important;
  }
  .mb-4,
  .mb-6,
  .mb-8 {
    margin-bottom: 1rem !important;
  }
}
</style>
