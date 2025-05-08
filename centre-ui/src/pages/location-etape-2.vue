<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useRouter } from 'vue-router'
import { NSpace, NH1, NButton, NForm, NFormItem, NInputNumber, NSelect, NDatePicker, useMessage } from 'naive-ui'
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
  <NSpace vertical :size="24">
    <NH1>Nouvelle location - Étape 2</NH1>

    <NForm label-placement="top">
      <NFormItem label="Date de début *">
        <NDatePicker
          v-model:formatted-value="locationDTO.dateDebut"
          value-format="yyyy-MM-dd"
          type="date"
          clearable
        />
      </NFormItem>

      <NFormItem label="Date de fin (optionnel)">
        <NDatePicker
          v-model:formatted-value="locationDTO.dateFin"
          value-format="yyyy-MM-dd"
          type="date"
          clearable
        />
      </NFormItem>

      <NFormItem label="Loyer mensuel (€) *">
        <NInputNumber v-model:value="locationDTO.loyerMensuel" min="0" placeholder="0.00" />
      </NFormItem>

      <NFormItem label="Charges mensuelles (€)">
        <NInputNumber v-model:value="locationDTO.chargesMensuelles" min="0" placeholder="0.00" />
      </NFormItem>

      <NFormItem label="Dépôt de garantie (€)">
        <NInputNumber v-model:value="locationDTO.depotGarantie" min="0" placeholder="0.00" />
      </NFormItem>

      <NFormItem label="Fréquence de paiement">
        <NSelect v-model:value="locationDTO.frequenceLoyer" :options="frequences" placeholder="Choisir une fréquence" />
      </NFormItem>

      <NFormItem label="Jour d'échéance du loyer">
        <NInputNumber v-model:value="locationDTO.jourEcheance" min="1" max="31" placeholder="ex: 5" />
      </NFormItem>
    </NForm>

    <div class="flex justify-between">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>
