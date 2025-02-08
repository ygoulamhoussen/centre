<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Bienvenue au Centre de Formation"
      subtitle="Explorez nos formations pour développer vos compétences et réussir votre avenir."
      link="/formations"
      linkText="Découvrir nos formations"
    />

      <h2 class="fr-h3">Qui sommes-nous ?</h2>
      <p class="fr-text">
        Notre centre de formation propose des programmes diversifiés pour répondre aux besoins
        des étudiants et des professionnels. Rejoignez une communauté dynamique et bénéficiez de l'expertise de nos formateurs.
      </p>




    <TestimonialsSection :testimonials="testimonials" />
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import ContentCard from '../components/ContentCard.vue';
import TestimonialsSection from '../components/TestimonialsSection.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "HomePage",
  components: {
    HeaderSection,
    ContentCard,
    TestimonialsSection,
    DefaultLayout
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
      errorMessage: "", // Add error message data property
      breadcrumbSegments: [
        { name: 'Accueil', link: '/' }
      ]
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

