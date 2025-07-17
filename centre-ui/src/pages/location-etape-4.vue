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
    title: 'Nouvelle location - Étape 4',
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
const stepTitles = ['Propriété', 'Locataire', 'Détails du bail', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

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
    message.error(error.message || "Erreur lors de l'enregistrement")
  } finally {
    chargement.value = false
  }
}

function precedent() {
  router.push('/location-etape-3')
}

function formatDateFr(dateStr: string | null | undefined) {
  if (!dateStr) return 'Non définie'
  const [y, m, d] = dateStr.split('-')
  if (!y || !m || !d) return dateStr
  return `${d}/${m}/${y}`
}
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="3" size="small">
          <NStep title="Propriété" status="finish" description="Choix du bien" />
          <NStep title="Locataire" status="finish" description="Choix du locataire" />
          <NStep title="Détails du bail" status="finish" description="Loyer, dates, etc." />
          <NStep title="Récapitulatif" status="process" description="Vérification finale" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 4/4</div>
        <div class="step-mobile-label">{{ stepTitles[3] }}</div>
      </div>

      <NH2 class="titre-principal mb-4">Étape 4: Récapitulatif</NH2>

      <NDescriptions label-placement="top" bordered :column="isMobile ? 1 : 2">
        <NDescriptionsItem label="Propriété">
          {{ locationDTO.proprieteNom || 'Non défini' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Locataire">
          {{ locationDTO.locataireNom || 'Non défini' }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date de début">
          {{ formatDateFr(locationDTO.dateDebut) }}
        </NDescriptionsItem>
        <NDescriptionsItem label="Date de fin">
          {{ formatDateFr(locationDTO.dateFin) }}
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
        <NButton @click="precedent" size="large" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
        <NButton type="primary" :loading="chargement" @click="enregistrer" size="large" title="Enregistrer la location">
          <template #icon>
            <NIcon :component="Checkmark24Filled" />
          </template>
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