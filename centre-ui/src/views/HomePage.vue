<template>
  <div>
    <HeaderSection
      title="Bienvenue au Centre de Formation"
      subtitle="Explorez nos formations pour développer vos compétences et réussir votre avenir."
      link="/formations"
      linkText="Découvrir nos formations"
    />
    <section class="fr-container fr-my-6w">
      <h2 class="fr-h3">Qui sommes-nous ?</h2>
      <p class="fr-text">
        Notre centre de formation propose des programmes diversifiés pour répondre aux besoins
        des étudiants et des professionnels. Rejoignez une communauté dynamique et bénéficiez de l'expertise de nos formateurs.
      </p>
    </section>
    <section class="fr-container fr-my-6w">
      <h2 class="fr-h3">Nos Formations</h2>
      <div class="fr-grid-row fr-grid-row--gutters">
        <div
          class="fr-col-12 fr-col-md-4"
          v-for="course in courses"
          :key="course.id"
        >
          <ContentCard :title="course.title" :description="course.description" />
        </div>
      </div>
    </section>
    <TestimonialsSection :testimonials="testimonials" />
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <footer class="fr-footer" role="contentinfo">
      <div class="fr-container">
        <div class="fr-footer__bottom">
          <p class="fr-footer__bottom-title">&copy; 2024 Centre de Formation</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import ContentCard from '../components/ContentCard.vue';
import TestimonialsSection from '../components/TestimonialsSection.vue';

export default {
  name: "HomePage",
  components: {
    HeaderSection,
    ContentCard,
    TestimonialsSection
  },
  data() {
    return {
      courses: [
        {
          id: 1,
          title: "Développement Web",
          description:
            "Apprenez à créer des sites web modernes et interactifs.",
        },
        {
          id: 2,
          title: "Marketing Digital",
          description:
            "Maîtrisez les techniques pour promouvoir des produits et services en ligne.",
        },
        {
          id: 3,
          title: "Gestion de Projet",
          description:
            "Développez vos compétences pour gérer des projets efficacement.",
        },
      ],
      testimonials: [
        {
          id: 1,
          name: "Jean Dupont",
          message: "Une formation de qualité qui m'a permis de progresser rapidement.",
        },
        {
          id: 2,
          name: "Marie Curie",
          message: "Les formateurs sont très compétents et à l'écoute.",
        },
        {
          id: 3,
          name: "Albert Einstein",
          message: "J'ai beaucoup appris et je recommande vivement ce centre de formation.",
        },
      ],
      errorMessage: "" // Add error message data property
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        // Simulate fetching data for courses and testimonials
        this.errorMessage = ""; // Clear error message on success
      } catch (error) {
        console.error('Error fetching data:', error);
        this.errorMessage = 'Erreur lors de la récupération des données. Veuillez réessayer.'; // Set error message
      }
    }
  }
};
</script>

<style scoped>
/* ...existing code... */
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>

