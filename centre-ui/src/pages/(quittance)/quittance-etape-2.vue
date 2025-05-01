<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NDatePicker,
  NForm,
  NFormItem,
  NH1,
  NInputNumber,
  NRadio,
  NRadioGroup,
  NSelect,
  NSpace,
  useMessage
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

// calcul du total en temps réel
const computedTotal = computed(() => {
  const loyer = Number.parseFloat(quittanceDTO.value.montantLoyer) || 0
  const charges = Number.parseFloat(quittanceDTO.value.montantCharges) || 0
  const caution = quittanceDTO.value.inclureCaution
    ? Number.parseFloat(quittanceDTO.value.depotGarantie || '0')
    : 0
  return (loyer + charges + caution).toFixed(2)
})

function suivant() {
  // validation sommaire
  if (
    !quittanceDTO.value.dateDebut ||
    !quittanceDTO.value.dateFin ||
    !quittanceDTO.value.dateEmission ||
    !quittanceDTO.value.statut
  ) {
    message.warning('Merci de renseigner tous les champs obligatoires (*)')
    return
  }
  // on stocke le total calculé
  quittanceDTO.value.montantTotal = computedTotal.value
  router.push('/quittance-etape-3')
}
</script>

<template>
  <NSpace vertical :size="24">
    <NH1>Nouvelle quittance – Détails</NH1>
    <NForm label-placement="top">
      <NFormItem label="Date émission *">
        <NDatePicker
          v-model:formatted-value="quittanceDTO.dateEmission"
          type="date"
          value-format="yyyy-MM-dd"
        />
      </NFormItem>
      <NFormItem label="Date échéance">
        <NDatePicker
          v-model:formatted-value="quittanceDTO.dateEcheance"
          type="date"
          value-format="yyyy-MM-dd"
        />
      </NFormItem>
      <NFormItem label="Montant loyer (€) *">
        <NInputNumber
          v-model:value="quittanceDTO.montantLoyer"
          min="0"
          placeholder="0.00"
        />
      </NFormItem>
      <NFormItem label="Montant charges (€) *">
        <NInputNumber
          v-model:value="quittanceDTO.montantCharges"
          min="0"
          placeholder="0.00"
        />
      </NFormItem>
      <NFormItem label="Inclure caution ?">
        <NRadioGroup v-model:value="quittanceDTO.inclureCaution">
          <NRadio :value="true">Oui</NRadio>
          <NRadio :value="false">Non</NRadio>
        </NRadioGroup>
      </NFormItem>
      <NFormItem
        v-if="quittanceDTO.inclureCaution"
        label="Montant caution (€)"
      >
        <NInputNumber
          v-model:value="quittanceDTO.depotGarantie"
          min="0"
          placeholder="0.00"
        />
      </NFormItem>
      <NFormItem label="Montant total (€)">
        <NInputNumber :value="computedTotal" disabled />
      </NFormItem>
      <NFormItem label="Statut *">
        <NSelect
          v-model:value="quittanceDTO.statut"
          :options="[
            { label: 'PAYÉE', value: 'PAYEE' },
            { label: 'PARTIELLE', value: 'PARTIELLE' },
            { label: 'IMPAYÉE', value: 'IMPAYEE' }
          ]"
        />
      </NFormItem>
    </NForm>

    <div class="flex justify-between pt-4">
      <NButton @click="$router.back()">Précédent</NButton>
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>

<style scoped></style>
