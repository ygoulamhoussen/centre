<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
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
  NH2,
  NIcon,
  NInput,
  NSelect,
  NSpace,
  NStep,
  NSteps,
  useMessage
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'

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
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="1" class="mb-8">
          <NStep title="Type et Nom" description="Identification du bien" />
          <NStep title="Adresse" description="Localisation du bien" />
          <NStep title="Détails" description="Informations techniques" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>

      <NH2 class="titre-principal mb-6">Étape 1: Donnez un nom à votre propriété</NH2>
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

      <NH2 class="titre-principal my-6">Quel est le type de bien ?</NH2>
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

      <div class="flex justify-end mt-8">
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
  .form-grid {
    grid-template-columns: 1fr !important;
  }
  .form-grid .n-form-item-gi {
    grid-column: 1 !important;
  }
  .type-grid {
    grid-template-columns: 1fr !important;
  }
}
</style>
