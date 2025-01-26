<template>
  <section class="fr-container fr-my-6w">
    <h2 class="fr-h3">Témoignages</h2>
    <div class="fr-grid-row fr-grid-row--gutters">
      <div
        class="fr-col-12 fr-col-md-4"
        v-for="testimonial in testimonials"
        :key="testimonial.id"
      >
        <div class="fr-card">
          <div class="fr-card__body">
            <h3 class="fr-card__title">{{ testimonial.name }}</h3>
            <p class="fr-card__desc">"{{ testimonial.message }}"</p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'TestimonialsSection',
  props: {
    testimonials: {
      type: Array,
      required: true
    }
  }
};
</script>

<style scoped>
/* Ajoutez vos styles personnalisés si nécessaire */
</style>
