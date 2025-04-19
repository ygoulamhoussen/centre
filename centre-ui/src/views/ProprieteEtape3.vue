<template>
  <n-card class="fr-my-4w">
    <h1>Ajouter une propriété - Étape 3</h1>
    <p>Détails de l'acquisition</p>
    <n-form @submit.prevent="suivant" :model="proprieteDTO">
      <n-form-item label="Date d'acquisition" path="dateAcquisition">
        <n-date-picker v-model:value="dateAcquisitionModel" type="date" clearable />
      </n-form-item>
      <n-form-item label="Montant d'acquisition (€)" path="montantAcquisition">
        <n-input-number v-model:value="proprieteDTO.montantAcquisition" min="0" step="0.01" required />
      </n-form-item>
      <n-form-item label="Frais de notaire (€)" path="fraisNotaire">
        <n-input-number v-model:value="proprieteDTO.fraisNotaire" min="0" step="0.01" />
      </n-form-item>
      <n-form-item label="Frais d'agence (€)" path="fraisAgence">
        <n-input-number v-model:value="proprieteDTO.fraisAgence" min="0" step="0.01" />
      </n-form-item>
      <n-form-item label="Tantième" path="tantieme">
        <n-input-number v-model:value="proprieteDTO.tantieme" min="0" step="1" />
      </n-form-item>
      <div style="margin-top: 2rem;">
        <n-button @click="precedent" secondary style="margin-right: 1rem;">
          Précédent
        </n-button>
        <n-button type="primary" attr-type="submit">
          Suivant
        </n-button>
      </div>
    </n-form>
    <p v-if="erreur" class="error">{{ erreur }}</p>
  </n-card>
</template>

<script setup>
import { useUnifiedStore } from '@/stores/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, computed } from 'vue'

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()

const erreur = ref('')

function precedent() {
  router.push('/propriete/etape-2')
}

function suivant() {
  router.push('/propriete/etape-4')
}

// Computed pour gérer la conversion string <-> timestamp pour le date-picker
const dateAcquisitionModel = computed({
  get() {
    const val = proprieteDTO.value.dateAcquisition
    if (typeof val === 'number') return val
    if (typeof val === 'string') {
      const d = new Date(val)
      return isNaN(d.getTime()) ? null : d.getTime()
    }
    if (val instanceof Date) return isNaN(val.getTime()) ? null : val.getTime()
    return null
  },
  set(ts) {
    // Stocker en ISO string ou null dans le store (ou adaptez selon votre backend)
    proprieteDTO.value.dateAcquisition = ts ? new Date(ts).toISOString().slice(0, 10) : null
  }
})

</script>

<style scoped>
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
