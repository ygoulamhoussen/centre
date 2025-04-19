<template>
  <n-card class="fr-my-4w">
    <h1>Ajouter une propriété - Étape 2</h1>
    <p>Adresse du bien</p>
    <n-form @submit.prevent="valider" :model="proprieteDTO">
      <n-form-item label="Adresse" path="adresse">
        <n-input v-model:value="proprieteDTO.adresse" required />
      </n-form-item>
      <n-form-item label="Complément d'adresse" path="complementAdresse">
        <n-input v-model:value="proprieteDTO.complementAdresse" />
      </n-form-item>
      <n-form-item label="Code postal" path="codePostal">
        <n-input v-model:value="proprieteDTO.codePostal" required />
      </n-form-item>
      <n-form-item label="Ville" path="ville">
        <n-input v-model:value="proprieteDTO.ville" required />
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
  </n-card>
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
