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

function precedent() {
  router.push('/locataire-etape-1')
}

function suivant() {
  router.push('/locataire-etape-3')
}
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="2" class="mb-8">
        <NStep title="Informations" description="Nom, contact, etc." />
        <NStep title="Adresse" description="Lieu de résidence" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <NH2 class="titre-principal mb-4">Étape 2: Adresse</NH2>

      <NForm>
        <NGrid :x-gap="24" :y-gap="24" :cols="2" :item-responsive="true">
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
        <NButton @click="precedent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" @click="suivant">
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
</style>
