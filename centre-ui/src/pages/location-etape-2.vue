<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useRouter } from 'vue-router'
import { NSpace, NButton, NForm, NFormItem, NInputNumber, NSelect, NDatePicker, useMessage, NCard, NSteps, NStep, NIcon, NGrid, NFormItemGi } from 'naive-ui'
import { ArrowLeft24Filled, ArrowRight24Filled, CalendarLtr24Filled, Money24Filled, CalendarClock24Filled, Key24Filled, ArrowSync20Filled, CalendarDay24Filled } from '@vicons/fluent'
import { storeToRefs } from 'pinia'

definePage({
  meta: {
    title: 'Nouvelle location - Étape 2',
    hideInMenu: true,
    activeMenu: '/location',
  }
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const router = useRouter()
const message = useMessage()

const frequences = [
  { label: 'Mensuel', value: 'MENSUEL' },
  { label: 'Trimestriel', value: 'TRIMESTRIEL' },
  { label: 'Annuel', value: 'ANNUEL' }
]

function suivant() {
  if (!locationDTO.value.loyerMensuel || !locationDTO.value.dateDebut) {
    message.warning('Veuillez saisir les informations obligatoires')
    return
  }
  router.push('/location-etape-3')
}

function precedent() {
  router.push('/location-etape-1')
}
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="2" class="mb-8">
        <NStep title="Sélection" description="Propriété et locataire" />
        <NStep title="Détails du bail" description="Loyer, dates, etc." />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-4">Étape 2: Détails du bail</h2>

      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="2">
          <NFormItemGi label="Date de début *">
            <NDatePicker
              v-model:formatted-value="locationDTO.dateDebut"
              value-format="yyyy-MM-dd"
              type="date"
              clearable
              class="w-full"
              size="large"
            >
              <template #prefix>
                <NIcon :component="CalendarLtr24Filled" />
              </template>
            </NDatePicker>
          </NFormItemGi>
          <NFormItemGi label="Date de fin (optionnel)">
            <NDatePicker
              v-model:formatted-value="locationDTO.dateFin"
              value-format="yyyy-MM-dd"
              type="date"
              clearable
              class="w-full"
              size="large"
            >
              <template #prefix>
                <NIcon :component="CalendarClock24Filled" />
              </template>
            </NDatePicker>
          </NFormItemGi>
          <NFormItemGi label="Loyer mensuel (€) *">
            <NInputNumber v-model:value="locationDTO.loyerMensuel" min="0" placeholder="0.00" class="w-full" size="large">
              <template #prefix>
                <NIcon :component="Money24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>
          <NFormItemGi label="Charges mensuelles (€)">
            <NInputNumber v-model:value="locationDTO.chargesMensuelles" min="0" placeholder="0.00" class="w-full" size="large">
              <template #prefix>
                <NIcon :component="Money24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>
          <NFormItemGi label="Dépôt de garantie (€)">
            <NInputNumber v-model:value="locationDTO.depotGarantie" min="0" placeholder="0.00" class="w-full" size="large">
              <template #prefix>
                <NIcon :component="Key24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>
          <NFormItemGi label="Fréquence de paiement">
            <NSelect v-model:value="locationDTO.frequenceLoyer" :options="frequences" placeholder="Choisir une fréquence" size="large">
              <template #prefix>
                <NIcon :component="ArrowSync20Filled" />
              </template>
            </NSelect>
          </NFormItemGi>
          <NFormItemGi label="Jour d'échéance du loyer">
            <NInputNumber v-model:value="locationDTO.jourEcheance" min="1" max="31" placeholder="ex: 5" class="w-full" size="large">
              <template #prefix>
                <NIcon :component="CalendarDay24Filled" />
              </template>
            </NInputNumber>
          </NFormItemGi>
        </NGrid>
      </NForm>

      <div class="flex justify-between mt-8">
        <NButton @click="precedent" size="large">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" @click="suivant" size="large">
          Suivant
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
          </template>
        </NButton>
      </div>
    </NCard>
  </div>
</template>
