<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowRight24Filled, BuildingHome24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItem,
  NH2,
  NIcon,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle location - Étape 1',
    hideInMenu: true,
    activeMenu: '/location',
  },
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId

const router = useRouter()
const message = useMessage()

const proprietes = ref<any[]>([])
const stepTitles = ['Propriété', 'Locataire', 'Détails du bail', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}

async function fetchData() {
  try {
    const propResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${utilisateurId}`)
    proprietes.value = await propResponse.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement')
  }
}

function handleProprieteChange(value: string) {
  const selected = proprietes.value.find(p => p.id === value)
  if (selected) {
    locationDTO.value.proprieteId = selected.id
    locationDTO.value.proprieteNom = selected.nom
  }
}

function suivant() {
  if (!locationDTO.value.proprieteId) {
    message.warning('Merci de choisir une propriété')
    return
  }
  router.push('/location-etape-2')
}

onMounted(() => {
  fetchData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="0" size="small">
          <NStep title="Propriété" status="process" description="Choix du bien" />
          <NStep title="Locataire" description="Choix du locataire" />
          <NStep title="Détails du bail" description="Loyer, dates, etc." />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 1/4</div>
        <div class="step-mobile-label">{{ stepTitles[0] }}</div>
      </div>

      <NH2 class="titre-principal mb-4">Étape 1: Sélection de la propriété</NH2>

      <NForm label-placement="top">
        <NFormItem label="Sélectionner une propriété">
          <NSelect
            v-model:value="locationDTO.proprieteId"
            :options="proprietes.map(p => ({ label: p.nom, value: p.id }))"
            placeholder="Choisir une propriété"
            @update:value="handleProprieteChange"
            size="large"
          />
        </NFormItem>
      </NForm>

      <NSpace justify="end" class="mt-8">
        <NButton type="primary" @click="suivant" size="large" title="Suivant">
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
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
