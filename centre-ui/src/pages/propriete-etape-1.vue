<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  useMessage,
  NSpace,
  NForm,
  NInput,
  NGrid,
  NGi,
  NButton,
  NCard,
  NSteps,
  NStep,
  NIcon,
  NFormItemGi,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import {
  Tag24Filled,
  BuildingHome24Filled,
  Home24Filled,
  Box24Filled,
  VehicleCar24Filled,
  ArrowRight24Filled,
} from '@vicons/fluent'

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
  { value: 'BOX', label: 'Box', icon: Box24Filled },
  { value: 'PARKING', label: 'Parking', icon: VehicleCar24Filled },
]

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
      <NSteps :current="1" class="mb-8">
        <NStep title="Type et Nom" description="Identification du bien" />
        <NStep title="Adresse" description="Localisation du bien" />
        <NStep title="Détails" description="Informations techniques" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-6">Étape 1: Donnez un nom à votre propriété</h2>
      <NForm>
        <NGrid :cols="1">
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

      <h2 class="text-xl font-semibold my-6">Quel est le type de bien ?</h2>
      <NGrid :cols="2" :x-gap="12" :y-gap="12" responsive="screen" :item-responsive="true">
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
        >
          Suivant
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
          </template>
        </NButton>
      </div>
    </NCard>
  </div>
</template>
