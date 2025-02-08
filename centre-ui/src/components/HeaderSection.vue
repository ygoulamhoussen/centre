<template>
  <section class="fr-hero fr-hero--lg">
    <div >
      <div class="fr-grid-row fr-grid-row--center">
        <div class="fr-col-12">
          <h1 class="fr-display-lg">{{ title }}</h1>
          <p class="fr-text--lead">{{ subtitle }}</p>

        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'HeaderSection',
  props: {
    title: {
      type: String,
      required: true
    },
    subtitle: {
      type: String,
      required: true
    },
    link: {
      type: String,
      required: true
    },
    linkText: {
      type: String,
      required: true
    }
  }
};
</script>

<style scoped>
/* Ajoutez vos styles personnalisés si nécessaire */
</style>
