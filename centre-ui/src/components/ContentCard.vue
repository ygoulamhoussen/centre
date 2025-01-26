<template>
  <div class="fr-card">
    <div class="fr-card__body">
      <h3 class="fr-card__title">{{ title }}</h3>
      <p class="fr-card__desc">{{ description }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ContentCard',
  props: {
    title: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: true
    }
  }
};
</script>

<style scoped>
/* Ajoutez vos styles personnalisés si nécessaire */
</style>
