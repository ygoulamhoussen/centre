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
  useMessage,
  NCard,
  NSteps,
  NGrid,
  NFormItemGi
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Ajouter une quittance - Étape 2',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})

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
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="2" class="mb-8">
        <NStep title="Sélection" description="Location et période" />
        <NStep title="Détails" description="Montants et statut" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>
      <h2 class="text-xl font-semibold mb-4">Étape 2 : Détails de la quittance</h2>
      <NForm label-placement="top">
        <NGrid :x-gap="24" :y-gap="16" :cols="2">
          <NFormItemGi label="Date émission *">
            <NDatePicker v-model:formatted-value="quittanceDTO.dateEmission" type="date" value-format="yyyy-MM-dd" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Date échéance">
            <NDatePicker v-model:formatted-value="quittanceDTO.dateEcheance" type="date" value-format="yyyy-MM-dd" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Montant loyer (€) *">
            <NInputNumber v-model:value="quittanceDTO.montantLoyer" min="0" placeholder="0.00" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Montant charges (€) *">
            <NInputNumber v-model:value="quittanceDTO.montantCharges" min="0" placeholder="0.00" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Inclure caution ?">
            <NRadioGroup v-model:value="quittanceDTO.inclureCaution">
              <NRadio :value="true">Oui</NRadio>
              <NRadio :value="false">Non</NRadio>
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="quittanceDTO.inclureCaution" label="Montant caution (€)">
            <NInputNumber v-model:value="quittanceDTO.depotGarantie" min="0" placeholder="0.00" size="large" />
          </NFormItemGi>
          <NFormItemGi label="Montant total (€)">
            <NInputNumber :value="computedTotal" disabled size="large" />
          </NFormItemGi>
          <NFormItemGi label="Statut *">
            <NSelect
              v-model:value="quittanceDTO.statut"
              :options="[
                { label: 'PAYÉE', value: 'PAYEE' },
                { label: 'PARTIELLE', value: 'PARTIELLE' },
                { label: 'IMPAYÉE', value: 'IMPAYEE' }
              ]"
              size="large"
            />
          </NFormItemGi>
        </NGrid>
      </NForm>
      <div class="flex justify-between mt-8">
        <NButton @click="$router.back()" size="large">Précédent</NButton>
        <NButton type="primary" @click="suivant" size="large">Suivant</NButton>
      </div>
    </NCard>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
}
.justify-between {
  justify-content: space-between;
}
.mt-8 {
  margin-top: 2rem;
}
</style>
