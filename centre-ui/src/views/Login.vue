<template>
  <div class="fr-container fr-mt-10w">
    <h1 class="fr-h1">Connexion</h1>
    <form @submit.prevent="handleLogin" class="fr-form-group">
      <div class="fr-input-group">
        <label for="email" class="fr-label">Email</label>
        <input
          id="email"
          type="email"
          v-model="email"
          class="fr-input"
          placeholder="Votre email"
          required
        />
      </div>
      <div class="fr-input-group fr-mt-3w">
        <label for="password" class="fr-label">Mot de passe</label>
        <input
          id="password"
          type="password"
          v-model="password"
          class="fr-input"
          placeholder="Votre mot de passe"
          required
        />
      </div>
      <div class="fr-mt-3w">
        <DsfrButton type="submit" variant="primary">
          Se connecter
        </DsfrButton>
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const email = ref('')
const password = ref('')

function handleLogin() {
  // Implémentez ici la logique de connexion
  console.log("Email:", email.value, "Mot de passe:", password.value)
}
</script>

<style scoped>
/* Styles supplémentaires pour la page de login */
</style>
