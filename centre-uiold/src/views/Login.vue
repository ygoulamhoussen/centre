<template>
  <n-card class="fr-mt-10w" style="max-width: 400px; margin: 0 auto;">
    <h1>Connexion</h1>
    <n-form @submit.prevent="handleLogin">
      <n-form-item label="Email" path="email">
        <n-input
          v-model:value="email"
          type="email"
          placeholder="Votre email"
          required
        />
      </n-form-item>
      <n-form-item label="Mot de passe" path="password">
        <n-input
          v-model:value="password"
          type="password"
          placeholder="Votre mot de passe"
          required
        />
      </n-form-item>
      <div style="margin-top: 2rem;">
        <n-button type="primary" attr-type="submit">
          Se connecter
        </n-button>
      </div>
    </n-form>
  </n-card>
</template>

<script setup>
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
