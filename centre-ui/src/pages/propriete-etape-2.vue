<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  AddSquare24Filled,
  ArrowLeft24Filled,
  ArrowRight24Filled,
  City24Filled,
  Home24Filled,
  Location24Filled,
  Tag24Filled,
  Edit24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItemGi,
  NGrid,
  NIcon,
  NInput,
  NSpace,
  NSteps,
  NStep,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'

definePage({
  meta: {
    title: 'Nouvelle propriété - Adresse',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()

const stepTitles = ['Type et Nom', 'Adresse', 'Détails', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function precedent() {
  router.push('/propriete-etape-1')
}

function valider() {
  router.push('/propriete-etape-3')
}

const currentStep = 1
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="1" size="small">
          <NStep title="Type et Nom" status="finish" />
          <NStep title="Adresse" status="process" />
          <NStep title="Détails" />
          <NStep title="Récapitulatif" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 2/4</div>
        <div class="step-mobile-label">{{ stepTitles[1] }}</div>
      </div>
      <NForm>
        <NGrid :x-gap="24" :y-gap="24" :cols="isMobile ? 1 : 2" :item-responsive="true" class="form-grid">
          <NFormItemGi :span="2" label="Adresse">
            <NInput v-model:value="proprieteDTO.adresse" placeholder="Saisir l'adresse" size="large">
              <template #prefix>
                <NIcon :component="Home24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
          <NFormItemGi :span="2" label="Complément d'adresse">
            <NInput
              v-model:value="proprieteDTO.complementAdresse"
              placeholder="Appartement, étage, etc."
              size="large"
            >
              <template #prefix>
                <NIcon :component="AddSquare24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
          <NFormItemGi :span="1" label="Code postal">
            <NInput v-model:value="proprieteDTO.codePostal" placeholder="Saisir le code postal" size="large">
              <template #prefix>
                <NIcon :component="Location24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
          <NFormItemGi :span="1" label="Ville">
            <NInput v-model:value="proprieteDTO.ville" placeholder="Saisir la ville" size="large">
              <template #prefix>
                <NIcon :component="City24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
        </NGrid>
      </NForm>

      <NSpace class="mt-8" :class="[isMobile ? 'flex-center' : 'flex-end']" justify="space-between">
        <NButton @click="precedent" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
        <NButton type="primary" @click="valider" title="Suivant">
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
