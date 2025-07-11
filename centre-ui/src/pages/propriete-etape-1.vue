<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import {
  ArrowRight24Filled,
  BuildingHome24Filled,
  Edit24Filled,
  Home24Filled,
  Tag24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItem,
  NFormItemGi,
  NGi,
  NGrid,
  NIcon,
  NInput,
  NSelect,
  NSpace,
  useMessage,
  NSteps,
  NStep,
} from 'naive-ui'
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle propriété - Type et Nom',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const typesBien = [
  { value: 'APPARTEMENT', label: 'Appartement', icon: BuildingHome24Filled },
  { value: 'MAISON', label: 'Maison', icon: Home24Filled },
  { value: 'BOX', label: 'Box', icon: Edit24Filled },
  { value: 'PARKING', label: 'Parking', icon: BuildingHome24Filled },
]

const stepTitles = ['Type et Nom', 'Adresse', 'Détails', 'Récapitulatif']
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function choisirType(type: string) {
  store.updateProprieteField('typeBien', type)
}

function valider() {
  if (!proprieteDTO.value.typeBien || !proprieteDTO.value.nom) {
    message.warning('Veuillez donner un nom à votre propriété et choisir un type.')
    return
  }
  router.push('/propriete-etape-2')
}

const steps = [
  { label: 'Type et Nom' },
  { label: 'Adresse' },
  { label: 'Détails' },
  { label: 'Récapitulatif' },
]
const currentStep = 0
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="mb-8" v-if="!isMobile">
        <NSteps :current="0" size="small">
          <NStep title="Type et Nom" status="process" />
          <NStep title="Adresse" />
          <NStep title="Détails" />
          <NStep title="Récapitulatif" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape 1/4</div>
        <div class="step-mobile-label">{{ stepTitles[0] }}</div>
      </div>
      <NForm>
        <NGrid :cols="isMobile ? 1 : 2" :x-gap="24" :y-gap="24" class="form-grid">
          <NFormItemGi>
            <NInput
              v-model:value="proprieteDTO.nom"
              placeholder="Ex : Résidence Les Lilas, Parking centre-ville..."
              size="large"
            >
              <template #prefix>
                <NIcon :component="Tag24Filled" />
              </template>
            </NInput>
          </NFormItemGi>
        </NGrid>
      </NForm>
      <div class="type-titre mb-4">Quel est le type de bien ?</div>
      <NGrid :cols="isMobile ? 1 : 2" :x-gap="12" :y-gap="12" responsive="screen" :item-responsive="true" class="type-grid">
        <NGi v-for="type in typesBien" :key="type.value">
          <NButton
            block
            size="large"
            :type="proprieteDTO.typeBien === type.value ? 'primary' : 'default'"
            @click="choisirType(type.value)"
          >
            <template #icon>
              <NIcon :component="type.icon" />
            </template>
            {{ type.label }}
          </NButton>
        </NGi>
      </NGrid>
      <div class="mt-8" :class="[isMobile ? 'flex-center' : 'flex-end']">
        <NButton
          type="primary"
          @click="valider"
          :disabled="!proprieteDTO.typeBien || !proprieteDTO.nom"
          title="Suivant"
        >
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
          </template>
        </NButton>
      </div>
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
  .progress-steps {
    display: none !important;
  }
  .progress-steps-mobile-simple {
    font-size: 1.15rem !important;
    padding: 1rem 1.25rem !important;
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
