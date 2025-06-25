<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  AddSquare24Filled,
  ArrowLeft24Filled,
  ArrowRight24Filled,
  City24Filled,
  Home24Filled,
  Location24Filled
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
  NSteps
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
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="2" class="mb-8">
          <NStep title="Type et Nom" description="Identification du bien" />
          <NStep title="Adresse" description="Localisation du bien" />
          <NStep title="Détails" description="Informations techniques" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>

      <NH2 class="titre-principal mb-4">Étape 2: Adresse de la propriété</NH2>

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

      <NSpace class="mt-8" justify="space-between">
        <NButton @click="precedent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" @click="valider">
          Suivant
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
  .form-grid {
    grid-template-columns: 1fr !important;
  }
  .form-grid .n-form-item-gi {
    grid-column: 1 !important;
  }
}
</style>
