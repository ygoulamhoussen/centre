<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  ArrowRight24Filled,
  CalendarLtr24Filled,
  ChartMultiple24Filled,
  DocumentHeader24Filled,
  Money24Filled,
  PeopleCommunity24Filled,
  Tag24Filled,
  Home24Filled,
  Edit24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDatePicker,
  NForm,
  NFormItemGi,
  NGrid,
  NIcon,
  NInputNumber,
  NSpace,
  useMessage,
  NSteps,
  NStep
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'

definePage({
  meta: {
    title: 'Nouvelle propriété - Détails',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const stepTitles = ['Type et Nom', 'Adresse', 'Détails', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function precedent() {
  router.push('/propriete-etape-2')
}

function suivant() {
  if (proprieteDTO.value.dateAcquisition === null || proprieteDTO.value.montantAcquisition === null) {
    message.warning('Veuillez remplir tous les champs obligatoires.')
    return
  }
  router.push('/propriete-etape-4')
}

const currentStep = 2
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="2" size="small">
          <NStep title="Type et Nom" status="finish" />
          <NStep title="Adresse" status="finish" />
          <NStep title="Détails" status="process" />
          <NStep title="Récapitulatif" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 3/4</div>
        <div class="step-mobile-label">{{ stepTitles[2] }}</div>
      </div>
      <NForm>
        <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2" :item-responsive="true" class="form-grid">
          <NFormItemGi label="Date d'acquisition *">
            <NDatePicker
              v-model:formatted-value="proprieteDTO.dateAcquisition"
              value-format="yyyy-MM-dd"
              type="date"
              clearable
              style="width: 100%"
              size="large"
            >
              <template #prefix>
                <NIcon :component="CalendarLtr24Filled" />
              </template>
            </NDatePicker>
          </NFormItemGi>

          <NFormItemGi label="Montant d'acquisition (€) *">
            <NInputNumber
              v-model:value="proprieteDTO.montantAcquisition"
              min="0"
              placeholder="0.00"
              style="width: 100%"
              size="large"
            >
              <template #prefix>
                <NIcon :component="Money24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>

          <NFormItemGi label="Frais de notaire (€)">
            <NInputNumber
              v-model:value="proprieteDTO.fraisNotaire"
              min="0"
              placeholder="0.00"
              style="width: 100%"
              size="large"
            >
              <template #prefix>
                <NIcon :component="DocumentHeader24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>

          <NFormItemGi label="Frais d'agence (€)">
            <NInputNumber
              v-model:value="proprieteDTO.fraisAgence"
              min="0"
              placeholder="0.00"
              style="width: 100%"
              size="large"
            >
              <template #prefix>
                <NIcon :component="PeopleCommunity24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>

          <NFormItemGi :span="2" label="Tantième">
            <NInputNumber
              v-model:value="proprieteDTO.tantieme"
              min="0"
              step="1"
              placeholder="ex: 1000"
              style="width: 100%"
              size="large"
            >
              <template #prefix>
                <NIcon :component="ChartMultiple24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>
        </NGrid>
      </NForm>

      <NSpace class="mt-8" :class="[isMobile ? 'flex-center' : 'flex-end']" justify="space-between">
        <NButton @click="precedent" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
        <NButton type="primary" @click="suivant" title="Suivant">
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
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
.form-grid {
  grid-template-columns: 1fr 1fr !important;
}
.form-grid .n-form-item-gi {
  grid-column: 1 !important;
}
.flex-center, .flex-end {
  justify-content: center !important;
  margin-top: 1.5rem !important;
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
