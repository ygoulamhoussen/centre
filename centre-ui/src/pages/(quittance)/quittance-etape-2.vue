<template>
  <NSpace vertical :size="24">
    <NH1>Nouvelle quittance - Étape 2</NH1>
    <NForm label-placement="top">
      <NFormItem label="Montant loyer (€)">
        <NInputNumber v-model:value="quittanceDTO.montantLoyer" min="0" placeholder="0.00" />
      </NFormItem>
      <NFormItem label="Montant charges (€)">
        <NInputNumber v-model:value="quittanceDTO.montantCharges" min="0" placeholder="0.00" />
      </NFormItem>
      <NFormItem label="Statut">
        <NSelect
          v-model:value="quittanceDTO.statut"
          :options="[
            { label: 'Payée', value: 'PAYEE' },
            { label: 'Partielle', value: 'PARTIELLE' },
            { label: 'Impayée', value: 'IMPAYEE' }
          ]"
          placeholder="Choisir un statut"
        />
      </NFormItem>
    </NForm>
    <div class="flex justify-between gap-2">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { NSpace, NH1, NForm, NFormItem, NInputNumber, NSelect, NButton, useMessage } from 'naive-ui'
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'

const router = useRouter()
const message = useMessage()
const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)

function precedent() {
  router.push('/quittance-etape-1')
}

function suivant() {
  if (!quittanceDTO.value.montantLoyer || !quittanceDTO.value.statut) {
    message.warning('Veuillez compléter les champs obligatoires')
    return
  }
  router.push('/quittance-etape-3')
}
</script>

<style scoped></style>