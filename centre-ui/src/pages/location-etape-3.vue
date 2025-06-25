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
import { ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle location - Récapitulatif',
    hideInMenu: true,
    activeMenu: '/location',
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

  const payload = {
    ...locationDTO.value,
    proprieteId: locationDTO.value.proprieteId || null,
    locataireId: locationDTO.value.locataireId || null,
    loyerMensuel: locationDTO.value.loyerMensuel || '0',
    chargesMensuelles: locationDTO.value.chargesMensuelles || '0',
    depotGarantie: locationDTO.value.depotGarantie || '0',
    utilisateurId
  }

  try {
    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createLocation`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
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
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="3" class="mb-8">
          <NStep title="Sélection" description="Propriété et locataire" />
          <NStep title="Détails du bail" description="Loyer, dates, etc." />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>

      <NH2 class="titre-principal mb-4">Étape 3: Récapitulatif</NH2>

      <NDescriptions label-placement="top" bordered :column="2">
        <NDescriptionsItem label="Propriété">
          {{ locationDTO.proprieteNom || 'Non défini' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Locataire">
          {{ locationDTO.locataireNom || 'Non défini' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date de début">
          {{ locationDTO.dateDebut || 'Non définie' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date de fin">
          {{ locationDTO.dateFin || 'Non définie' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Loyer mensuel">
          {{ locationDTO.loyerMensuel }} €
        </NDescriptionsItem>
        <NDescriptionsItem label="Charges mensuelles">
          {{ locationDTO.chargesMensuelles || 0 }} €
        </NDescriptionsItem>
        <NDescriptionsItem label="Dépôt de garantie">
          {{ locationDTO.depotGarantie || 0 }} €
        </NDescriptionsItem>
        <NDescriptionsItem label="Fréquence de paiement">
          {{ locationDTO.frequenceLoyer || 'Non définie' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Jour d'échéance du loyer">
          {{ locationDTO.jourEcheance || 'Non défini' }}
        </NDescriptionsItem>
      </NDescriptions>

      <div class="flex justify-between mt-8">
        <NButton @click="precedent" size="large">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" :loading="chargement" @click="enregistrer" size="large">
          <template #icon>
            <NIcon :component="Checkmark24Filled" />
          </template>
          Enregistrer la location
        </NButton>
      </div>
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
  .mb-8 {
    margin-bottom: 1rem !important;
  }
}
</style>
