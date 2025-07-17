<script setup lang="ts">
// Cette étape est désormais supprimée : plus de formulaire d'adresse pour le locataire
import { useRouter } from 'vue-router'

const router = useRouter()

// Redirige automatiquement vers l'étape suivante si jamais cette page est atteinte
router.replace('/locataire-etape-3')
</script>

<template>
  <div class="p-4">
    <p>Redirection...</p>
  </div>
</template>

<style scoped></style>
