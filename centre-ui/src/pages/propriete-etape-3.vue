<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  ArrowRight24Filled,
  CalendarLtr24Filled,
  ChartMultiple24Filled,
  DocumentHeader24Filled,
  Edit24Filled,
  Home24Filled,
  PeopleCommunity24Filled,
  Tag24Filled,
  Money24Filled,
  QuestionCircle24Filled,
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
  NStep,
  NSteps,
  useMessage,
  NTooltip,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

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
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
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
        
        <div class="content-area">
          <NForm>
            <NGrid :x-gap="24" :y-gap="16" :cols="isMobile ? 1 : 2" :item-responsive="true" class="form-grid">
              <NFormItemGi label="Date d'acquisition ou date de démarrage de l'activité*">
                <NDatePicker
                  v-model:formatted-value="proprieteDTO.dateAcquisition"
                  value-format="yyyy-MM-dd"
                  type="date"
                  clearable
                  style="width: 100%"
                  size="large"
                />
              </NFormItemGi>

              <NFormItemGi label="Montant d'acquisition (€) *">
                <NInputNumber
                  v-model:value="proprieteDTO.montantAcquisition"
                  :min="0"
                  placeholder="0.00"
                  style="width: 100%"
                  size="large"
                  inputmode="numeric"
                  pattern="[0-9]*"
                >
                  <template #prefix>
                    <NIcon :component="Money24Filled" />
                  </template>
                </NInputNumber>
              </NFormItemGi>

              <NFormItemGi>
                <template #label>
                  Frais d'acquisition (€)
                  <NTooltip placement="top">
                    <template #trigger>
                      <NIcon :component="QuestionCircle24Filled" size="18" style="color: #1976d2; margin-left: 6px; cursor: pointer; vertical-align: middle;" />
                    </template>
                    <div style="max-width: 320px;">
                      <b>Que comprennent les frais d'acquisition&nbsp;?</b><br>
                      <span>
                        Ils incluent généralement&nbsp;:
                        <ul style="margin: 0.5em 0 0 1.2em; padding: 0; font-size: 0.95em;">
                          <li>Droits d'enregistrement (ou TVA si bien neuf)</li>
                          <li>Émoluments du notaire</li>
                          <li>Frais de formalités</li>
                          <li>Frais d'agence (si payés par l'acquéreur)</li>
                          <li>Frais de dossier de prêt (si inclus dans l'acte)</li>
                          <li>Honoraires de conseils liés à l'achat</li>
                        </ul>
                      </span>
                    </div>
                  </NTooltip>
                </template>
                <NInputNumber
                  v-model:value="proprieteDTO.fraisNotaire"
                  :min="0"
                  placeholder="0.00"
                  style="width: 100%;"
                  size="large"
                  inputmode="numeric"
                  pattern="[0-9]*"
                >
                  <template #prefix>
                    <NIcon :component="DocumentHeader24Filled" />
                  </template>
                </NInputNumber>
              </NFormItemGi>
            </NGrid>
          </NForm>
        </div>

        <div class="button-container">
          <NSpace :class="[isMobile ? 'flex-center' : 'flex-end']" justify="space-between">
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
        </div>
      </NCard>
    </div>
  </div>
</template>

<style>
/* Layout principal */
.page-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.recap-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: calc(100vh - 2rem);
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem;
}

.button-container {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
  background: white;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

/* Styles existants */
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

.step.active,
.step.completed {
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

.flex-center,
.flex-end {
  justify-content: center !important;
  margin-top: 1.5rem !important;
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

@media (max-width: 768px) {
  .page-container {
    min-height: 100vh;
  }

  .recap-card {
    min-height: calc(100vh - 2rem);
  }

  .content-area {
    max-height: calc(100vh - 200px);
    overflow-y: auto;
  }

  .button-container {
    position: sticky;
    bottom: 0;
    background: white;
    padding: 1rem 0;
    margin-top: 1rem;
    border-top: 1px solid #f0f0f0;
  }

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

  .flex-center,
  .flex-end {
    justify-content: center !important;
    margin-top: 1.5rem !important;
  }
}
</style>
