<template>
  <n-modal
    :show="modelValue"
    :title="paiement ? 'Détails du paiement' : ''"
    preset="card"
    style="width: 90%; max-width: 600px"
    :bordered="false"
    size="huge"
    @update:show="(val) => $emit('update:modelValue', val)"
  >
    <n-descriptions
      v-if="paiement"
      label-placement="left"
      bordered
      :column="1"
      label-style="width: 200px"
      class="mb-4"
    >
      <n-descriptions-item label="ID">
        {{ paiement.id || 'N/A' }}
      </n-descriptions-item>
      
      <n-descriptions-item label="Date de paiement">
        {{ formatDate(paiement.datePaiement) }}
      </n-descriptions-item>
      
      <n-descriptions-item label="Montant">
        {{ formatCurrency(paiement.montant) }}
      </n-descriptions-item>
      
      <n-descriptions-item label="Moyen de paiement">
        {{ formatMoyenPaiement(paiement.moyenPaiement) }}
      </n-descriptions-item>
      
      <n-descriptions-item label="Référence">
        {{ paiement.reference || 'N/A' }}
      </n-descriptions-item>
      
      <n-descriptions-item label="Statut">
        <n-tag :type="paiement.estValide === 'true' || paiement.estValide === true ? 'success' : 'warning'" size="small">
          {{ (paiement.estValide === 'true' || paiement.estValide === true) ? 'Validé' : 'En attente' }}
        </n-tag>
      </n-descriptions-item>
      
      <n-descriptions-item v-if="paiement.commentaire" label="Commentaire">
        {{ paiement.commentaire }}
      </n-descriptions-item>
    </n-descriptions>

    <template #footer>
      <div style="display: flex; justify-content: flex-end">
        <n-button @click="$emit('update:modelValue', false)">
          Fermer
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import type { PaiementDTO } from '@/types/dto'

defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  paiement: {
    type: Object as () => PaiementDTO | null,
    default: null,
  },
})

const emit = defineEmits(['update:modelValue'])

// Formater la date
const formatDate = (dateString: string) => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

// Formater la monnaie
const formatCurrency = (amount: string) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(Number(amount))
}

// Formater le moyen de paiement
const formatMoyenPaiement = (moyen: string) => {
  const moyens: Record<string, string> = {
    'CARTE': 'Carte bancaire',
    'VIREMENT': 'Virement',
    'CHEQUE': 'Chèque',
    'ESPECES': 'Espèces',
    'AUTRE': 'Autre',
  }
  return moyens[moyen] || moyen || 'Non spécifié'
}
</script>
