<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NForm,
  NFormItemGi,
  NInput,
  NSpace,
  NCard,
  NSteps,
  NStep,
  NIcon,
  NGrid,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import {
  Home24Filled,
  AddSquare24Filled,
  Location24Filled,
  City24Filled,
  ArrowLeft24Filled,
  ArrowRight24Filled,
} from '@vicons/fluent'

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
      <NSteps :current="2" class="mb-8">
        <NStep title="Type et Nom" description="Identification du bien" />
        <NStep title="Adresse" description="Localisation du bien" />
        <NStep title="Détails" description="Informations techniques" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-4">Étape 2: Adresse de la propriété</h2>

      <NForm>
        <NGrid :x-gap="24" :y-gap="24" :cols="2" :item-responsive="true">
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
