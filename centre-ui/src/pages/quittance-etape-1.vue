<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NCard,
  NDatePicker,
  NForm,
  NFormItem,
  NFormItemGi,
  NGrid,
  NH2,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage,
  NIcon
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight24Filled } from '@vicons/fluent'

definePage({
  meta: {
    title: 'Ajouter une quittance - Étape 1',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})


const router = useRouter()
const message = useMessage()
const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)
const locations = ref<any[]>([])
const authStore = useAuthStore()
const userId = authStore.userInfo.userId
const isMobile = ref(window.innerWidth < 768)

async function fetchLocations() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocationsByUtilisateur/${userId}`)
    locations.value = await res.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement des locations')
  }
}

function suivant() {
  if (!quittanceDTO.value.locationId || !quittanceDTO.value.dateDebut || !quittanceDTO.value.dateFin) {
    message.warning('Veuillez compléter tous les champs')
    return
  }
  router.push('/quittance-etape-2')
}

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  fetchLocations()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="1" class="mb-8">
          <NStep title="Sélection" description="Location et période" />
          <NStep title="Détails" description="Montants et statut" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <NH2 class="titre-principal mb-4">Étape 1 : Sélection de la location et de la période</NH2>
      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
          <NFormItemGi :span="2" label="Location">
            <NSelect
              v-model:value="quittanceDTO.locationId"
              :options="locations.map(l => ({ label: l.proprieteNom + ' - ' + l.locataireNom, value: l.id }))"
              placeholder="Choisir une location"
              size="large"
            />
          </NFormItemGi>
          <NFormItemGi label="Période début">
            <NDatePicker v-model:formatted-value="quittanceDTO.dateDebut" value-format="yyyy-MM-dd" type="date" clearable size="large" />
          </NFormItemGi>
          <NFormItemGi label="Période fin">
            <NDatePicker v-model:formatted-value="quittanceDTO.dateFin" value-format="yyyy-MM-dd" type="date" clearable size="large" />
          </NFormItemGi>
        </NGrid>
      </NForm>
      <div class="flex justify-end mt-8">
        <NButton type="primary" @click="suivant" :loading="chargement" title="Suivant">
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
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
.flex {
  display: flex;
}
.justify-end {
  justify-content: flex-end;
}
.mt-8 {
  margin-top: 2rem;
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
  .mb-8,
  .mt-8 {
    margin-bottom: 1rem !important;
    margin-top: 1rem !important;
  }
}
</style>