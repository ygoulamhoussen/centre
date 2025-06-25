<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowLeft24Filled, Checkmark24Filled } from '@vicons/fluent'
import {
  NButton,
  NDescriptions,
  NDescriptionsItem,
  NSpace,
  useMessage,
  NCard,
  NSteps,
  NStep,
  NH2
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle quittance – Récapitulatif',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})

const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)
const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()
const loading = ref(false)

const pdfSrc = ref<string | undefined>()

async function genererPdfPreview() {
  try {
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/generateQuittancePdf/${quittanceDTO.value.id}`,
      { method: 'GET' },
    )

    if (!res.ok) throw new Error('Erreur lors de la génération du PDF')

    const blob = await res.blob()
    pdfSrc.value = URL.createObjectURL(blob)
  }
  catch (err) {
    message.error('Impossible de charger la prévisualisation PDF')
    console.error(err)
  }
}

function precedent() {
  router.back()
}

async function enregistrer() {
  loading.value = true
  try {
    const payload = {
      ...quittanceDTO.value,
      montantTotal: Number(quittanceDTO.value.montantTotal) || 0,
      locationId: quittanceDTO.value.locationId,
      utilisateurId: authStore.userInfo.userId,
      inclure_caution: quittanceDTO.value.inclureCaution ?? false
    }
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createQuittance`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }
    )
    if (!res.ok) throw new Error('Erreur serveur')
    message.success('Quittance créée !')
    store.resetQuittanceDTO()
    router.push('/quittance')
  } catch (e: any) {
    message.error(e.message || 'Erreur inconnue')
  } finally {
    loading.value = false
  }
}

</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="3" class="mb-8">
        <NStep title="Sélection" description="Location et période" />
        <NStep title="Détails" description="Montants et statut" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>
      <NH2 class="titre-principal mb-4">Étape 3 : Récapitulatif</NH2>
      <NDescriptions label-placement="top" bordered :column="2">
        <NDescriptionsItem label="Location ID">
          {{ quittanceDTO.locationId }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date début">
          {{ quittanceDTO.dateDebut }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date fin">
          {{ quittanceDTO.dateFin || '—' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date émission">
          {{ quittanceDTO.dateEmission }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Loyer (€)">
          {{ quittanceDTO.montantLoyer }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Charges (€)">
          {{ quittanceDTO.montantCharges }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Inclure caution">
          {{ quittanceDTO.inclureCaution ? 'Oui' : 'Non' }}
        </NDescriptionsItem>
        <NDescriptionsItem v-if="quittanceDTO.inclureCaution" label="Montant caution (€)">
          {{ quittanceDTO.depot_garantie }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Montant total (€)">
          {{ quittanceDTO.montantTotal }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Statut">
          {{ quittanceDTO.statut }}
        </NDescriptionsItem>
      </NDescriptions>
      <div class="flex justify-between mt-8">
        <NButton @click="precedent" size="large">Précédent</NButton>
        <NButton type="primary" @click="enregistrer" :loading="loading" size="large">Enregistrer</NButton>
      </div>
    </NCard>
  </div>
</template>

<style scoped>
.titre-principal, h1, h2, h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.flex {
  display: flex;
}
.justify-between {
  justify-content: space-between;
}
.mt-8 {
  margin-top: 2rem;
}
@media (max-width: 768px) {
  .titre-principal, h1, h2, h3 {
    font-size: 1.25rem !important;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-8, .mt-8 {
    margin-bottom: 1rem !important;
    margin-top: 1rem !important;
  }
}
</style>
