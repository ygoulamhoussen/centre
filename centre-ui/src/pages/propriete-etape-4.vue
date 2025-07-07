<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  Save24Filled,
  Tag24Filled,
  Home24Filled,
  Edit24Filled,
  ArrowRight24Filled,
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
  NText,
  useMessage,
  NSteps,
  NStep,
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

const stepTitles = ['Type et Nom', 'Adresse', 'Détails', 'Récapitulatif']
const currentStep = 3

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
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="3" size="small">
          <NStep title="Type et Nom" status="finish" />
          <NStep title="Adresse" status="finish" />
          <NStep title="Détails" status="finish" />
          <NStep title="Récapitulatif" status="process" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 4/4</div>
        <div class="step-mobile-label">{{ stepTitles[3] }}</div>
      </div>
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

      <NSpace class="mt-8" :class="[isMobile ? 'flex-center' : 'flex-end']" justify="space-between">
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

<style>
.progress-steps {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 32px;
  padding: 16px 0;
  background: transparent;
  border-radius: 16px;
  margin-bottom: 2rem;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 90px;
  opacity: 0.5;
  transition: opacity 0.2s;
}
.step.active, .step.completed {
  opacity: 1;
}
.step-number {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #1976d2;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.2rem;
  margin-bottom: 6px;
  border: 2px solid #1565c0;
}
.step.completed .step-number {
  background: #1565c0;
}
.step-label {
  font-size: 1rem;
  color: #1565c0;
  text-align: center;
  font-weight: 500;
}
.progress-steps-mobile-simple {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
  gap: 4px;
  background: transparent;
  border-radius: 12px;
  margin-bottom: 1rem;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1976d2;
}
.step-mobile-label {
  font-size: 1rem;
  color: #1565c0;
  text-align: center;
}
@media (max-width: 768px) {
  .mb-8 {
    margin-bottom: 1rem !important;
  }
  .progress-steps {
    display: none !important;
  }
  .progress-steps-mobile-simple {
    font-size: 1.15rem !important;
    padding: 1rem 1.25rem !important;
    margin-bottom: 1.5rem !important;
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
  .flex-center, .flex-end {
    justify-content: center !important;
    margin-top: 1.5rem !important;
  }
}
.mobile-stepper {
  text-align: center;
  margin-bottom: 1.5rem;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1976d2;
}
.step-mobile-label {
  font-size: 1.2rem;
  color: #222;
  margin-bottom: 1rem;
}
</style>
