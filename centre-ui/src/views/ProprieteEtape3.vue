<template>
  <section class="fr-container fr-my-4w">
    <h1 class="fr-h3">Ajouter une propriété - Étape 3</h1>

    <p>Détails de l'acquisition</p>
    <form @submit.prevent="suivant">
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="dateAcquisition">Date d'acquisition</label>
        <input class="fr-input" id="dateAcquisition" type="date" v-model="proprieteDTO.dateAcquisition" required />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="montantAcquisition">Montant d'acquisition (€)</label>
        <input class="fr-input" id="montantAcquisition" type="number" min="0" step="0.01" v-model="proprieteDTO.montantAcquisition" required />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="fraisNotaire">Frais de notaire (€)</label>
        <input class="fr-input" id="fraisNotaire" type="number" min="0" step="0.01" v-model="proprieteDTO.fraisNotaire" />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="fraisAgence">Frais d'agence (€)</label>
        <input class="fr-input" id="fraisAgence" type="number" min="0" step="0.01" v-model="proprieteDTO.fraisAgence" />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="tantieme">Tantième</label>
        <input class="fr-input" id="tantieme" type="number" min="0" step="1" v-model="proprieteDTO.tantieme" />
      </div>
      <div class="fr-mt-4w">
        <DsfrButton type="button" @click="precedent" secondary class="fr-mr-2w">
          Précédent
        </DsfrButton>
        <DsfrButton type="submit">
          Suivant
        </DsfrButton>
      </div>
    </form>
    <p v-if="erreur" class="error">{{ erreur }}</p>
  </section>
</template>

<script setup>
import { useUnifiedStore } from '@/stores/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

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
</script>

<style scoped>
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
