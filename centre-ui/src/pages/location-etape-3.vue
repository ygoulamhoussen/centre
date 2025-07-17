<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  ArrowRight24Filled,
  ArrowSync20Filled,
  CalendarClock24Filled,
  CalendarDay24Filled,
  CalendarLtr24Filled,
  Key24Filled,
  Money24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDatePicker,
  NForm,
  NFormItem,
  NFormItemGi,
  NGrid,
  NH2,
  NIcon,
  NInputNumber,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle location - Étape 3',
    hideInMenu: true,
    activeMenu: '/location',
  }
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const router = useRouter()
const message = useMessage()

const frequences = [
  { label: 'Mensuel', value: 'MENSUEL' },
  { label: 'Trimestriel', value: 'TRIMESTRIEL' },
  { label: 'Annuel', value: 'ANNUEL' }
]

const loyerMensuel = computed({
  get: () => (locationDTO.value.loyerMensuel ? Number(locationDTO.value.loyerMensuel) : null),
  set: (val: number | null) => {
    locationDTO.value.loyerMensuel = val === null ? '' : String(val)
  },
})

const chargesMensuelles = computed({
  get: () => (locationDTO.value.chargesMensuelles ? Number(locationDTO.value.chargesMensuelles) : null),
  set: (val: number | null) => {
    locationDTO.value.chargesMensuelles = val === null ? '' : String(val)
  },
})

const depotGarantie = computed({
  get: () => (locationDTO.value.depotGarantie ? Number(locationDTO.value.depotGarantie) : null),
  set: (val: number | null) => {
    locationDTO.value.depotGarantie = val === null ? '' : String(val)
  },
})

const jourEcheance = computed({
  get: () => (locationDTO.value.jourEcheance ? Number(locationDTO.value.jourEcheance) : null),
  set: (val: number | null) => {
    locationDTO.value.jourEcheance = val === null ? '' : String(val)
  },
})

const stepTitles = ['Propriété', 'Locataire', 'Détails du bail', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function suivant() {
  if (!locationDTO.value.loyerMensuel || !locationDTO.value.dateDebut) {
    message.warning('Veuillez saisir les informations obligatoires')
    return
  }
  router.push('/location-etape-4')
}

function precedent() {
  router.push('/location-etape-2')
}
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="2" size="small">
          <NStep title="Propriété" status="finish" description="Choix du bien" />
          <NStep title="Locataire" status="finish" description="Choix du locataire" />
          <NStep title="Détails du bail" status="process" description="Loyer, dates, etc." />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 3/4</div>
        <div class="step-mobile-label">{{ stepTitles[2] }}</div>
      </div>

      <NH2 class="titre-principal mb-4">Étape 3: Détails du bail</NH2>

      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2">
          <NFormItemGi label="Date de début *">
            <NDatePicker
              v-model:formatted-value="locationDTO.dateDebut"
              value-format="yyyy-MM-dd"
              format="dd/MM/yyyy"
              type="date"
              clearable
              class="w-full"
              size="large"
            />
          </NFormItemGi>
          <NFormItemGi label="Date de fin (optionnel)">
            <NDatePicker
              v-model:formatted-value="locationDTO.dateFin"
              value-format="yyyy-MM-dd"
              format="dd/MM/yyyy"
              type="date"
              clearable
              class="w-full"
              size="large"
            />
          </NFormItemGi>
          <NFormItemGi label="Loyer mensuel (€) *">
            <NInputNumber v-model:value="loyerMensuel" min="0" placeholder="0.00" class="w-full" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Charges mensuelles (€)">
            <NInputNumber v-model:value="chargesMensuelles" min="0" placeholder="0.00" class="w-full" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Dépôt de garantie (€)">
            <NInputNumber v-model:value="depotGarantie" min="0" placeholder="0.00" class="w-full" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Fréquence de paiement">
            <NSelect v-model:value="locationDTO.frequenceLoyer" :options="frequences" placeholder="Choisir une fréquence" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Jour d'échéance du loyer">
            <NInputNumber v-model:value="jourEcheance" min="1" max="31" placeholder="ex: 5" class="w-full" size="large" />
          </NFormItemGi>
        </NGrid>
      </NForm>

      <NSpace class="flex justify-between mt-8">
        <NButton @click="precedent" size="large" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
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
