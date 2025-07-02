<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  AddSquare24Filled,
  ArrowLeft24Filled,
  ArrowRight24Filled,
  City24Filled,
  Home24Filled,
  Location24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItemGi,
  NGrid,
  NH2,
  NIcon,
  NInput,
  NSpace,
  NStep,
  NSteps,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'

definePage({
  meta: {
    title: 'Nouveau locataire - Adresse',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)
const router = useRouter()

const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function precedent() {
  router.push('/locataire-etape-1')
}

function suivant() {
  router.push('/locataire-etape-3')
}

const currentStep = 1 // étape 2 (0-based)
const steps = [
  { label: 'Informations personnelles' },
  { label: 'Adresse' },
  { label: 'Récapitulatif' },
]
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <!-- Stepper custom -->
      <div v-if="!isMobile" class="progress-steps mb-8">
        <div 
          v-for="(step, index) in steps" 
          :key="index"
          class="step"
          :class="{ 'active': currentStep === index, 'completed': currentStep > index, 'disabled': currentStep < index }"
        >
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-label">{{ step.label }}</div>
        </div>
      </div>
      <div v-else class="progress-steps-mobile-simple mb-8">
        <span class="step-mobile-number">Étape 2/3</span>
        <span class="step-mobile-label">Adresse</span>
      </div>

      <NForm>
        <NGrid :x-gap="24" :y-gap="24" :cols="isMobile ? 1 : 2" :item-responsive="true">
          <NFormItemGi :span="2" label="Adresse">
            <NInput v-model:value="locataireDTO.adresse" placeholder="Saisir l'adresse" size="large">
              <template #prefix>
                <NIcon :component="Home24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
          <NFormItemGi :span="2" label="Complément d'adresse">
            <NInput
              v-model:value="locataireDTO.complementAdresse"
              placeholder="Appartement, étage, etc."
              size="large"
            >
              <template #prefix>
                <NIcon :component="AddSquare24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
          <NFormItemGi :span="1" label="Code postal">
            <NInput v-model:value="locataireDTO.codePostal" placeholder="Saisir le code postal" size="large">
              <template #prefix>
                <NIcon :component="Location24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
          <NFormItemGi :span="1" label="Ville">
            <NInput v-model:value="locataireDTO.ville" placeholder="Saisir la ville" size="large">
              <template #prefix>
                <NIcon :component="City24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
        </NGrid>
      </NForm>

      <NSpace class="mt-8" justify="space-between">
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
  .mb-6,
  .mb-8 {
    margin-bottom: 1rem !important;
  }
}
.progress-steps {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0 8px 0;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  position: relative;
}
.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 20px;
  left: 50%;
  width: 100%;
  height: 2px;
  background-color: #e5e7eb;
  z-index: 1;
}
.step.completed:not(:last-child)::after {
  background-color: #9C27B0;
}
.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  background-color: #e5e7eb;
  color: #6b7280;
  z-index: 2;
  position: relative;
}
.step.active .step-number {
  background-color: #9C27B0;
  color: white;
}
.step.completed .step-number {
  background-color: #10b981;
  color: white;
}
.step-label {
  font-size: 14px;
  color: #6b7280;
  text-align: center;
}
.step.active .step-label {
  color: #9C27B0;
  font-weight: 500;
}
.step.completed .step-label {
  color: #10b981;
  font-weight: 500;
}
.progress-steps-mobile-simple {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
  gap: 4px;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #9C27B0;
}
.step-mobile-label {
  font-size: 1rem;
  color: #757575;
  text-align: center;
}
@media (max-width: 768px) {
  .progress-steps {
    display: none !important;
  }
  .progress-steps-mobile-simple {
    margin-bottom: 1rem !important;
  }
}
</style>
