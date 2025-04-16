<template>
  <section class="fr-container fr-my-4w">
    <h1 class="fr-h3">Ajouter une propriété - Étape 2</h1>
    <p>Adresse du bien</p>
    <form @submit.prevent="valider">
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="adresse">Adresse</label>
        <input class="fr-input" id="adresse" v-model="proprieteDTO.adresse" required />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="complementAdresse">Complément d'adresse</label>
        <input class="fr-input" id="complementAdresse" v-model="proprieteDTO.complementAdresse" />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="codePostal">Code postal</label>
        <input class="fr-input" id="codePostal" v-model="proprieteDTO.codePostal" required />
      </div>
      <div class="fr-input-group fr-mb-2w">
        <label class="fr-label" for="ville">Ville</label>
        <input class="fr-input" id="ville" v-model="proprieteDTO.ville" required />
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
  </section>
</template>

<script setup>
import { useUnifiedStore } from '@/stores/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()

function valider() {
  router.push('/propriete/etape-3')
}

function precedent() {
  router.push('/propriete/etape-1')
}
</script>
