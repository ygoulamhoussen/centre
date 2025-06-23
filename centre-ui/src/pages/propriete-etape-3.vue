<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NDatePicker,
  NForm,
  NFormItemGi,
  NInputNumber,
  NSpace,
  NGrid,
  useMessage,
  NCard,
  NSteps,
  NStep,
  NIcon,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import {
  CalendarLtr24Filled,
  Money24Filled,
  DocumentHeader24Filled,
  PeopleCommunity24Filled,
  ChartMultiple24Filled,
  ArrowLeft24Filled,
  ArrowRight24Filled,
} from '@vicons/fluent'

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
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="3" class="mb-8">
        <NStep title="Type et Nom" description="Identification du bien" />
        <NStep title="Adresse" description="Localisation du bien" />
        <NStep title="Détails" description="Informations financières" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-4">Étape 3: Détails de l'acquisition</h2>

      <NForm>
        <NGrid :x-gap="24" :y-gap="16" :cols="2" :item-responsive="true">
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
