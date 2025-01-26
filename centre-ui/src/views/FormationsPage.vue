<template>
  <div class="fr-container fr-mt-6w">
    <HeaderSection
      title="Nos Formations"
      subtitle="Découvrez les formations proposées par notre centre pour développer vos compétences dans le domaine informatique."
      link="/"
      linkText="Retour à l'accueil"
    />
    <section>
      <div class="fr-grid-row fr-grid-row--gutters">
        <div
          v-for="formation in formations"
          :key="formation.id"
          class="fr-col-12 fr-col-md-6 fr-col-lg-4"
        >
          <ContentCard :title="formation.titre" :description="formation.description" />
        </div>
      </div>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </section>
  </div>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import ContentCard from '../components/ContentCard.vue';

export default {
  name: "FormationsPage",
  components: {
    HeaderSection,
    ContentCard
  },
  data() {
    return {
      formations: [],
      errorMessage: "" // Add error message data property
    };
  },
  created() {
    this.fetchFormations();
  },
  methods: {
    async fetchFormations() {
      try {
        const response = await fetch('http://localhost:8080/api/formations');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.formations = await response.json();
        this.errorMessage = ""; // Clear error message on success
      } catch (error) {
        console.error('Error fetching formations:', error);
        this.errorMessage = 'Erreur lors de la récupération des formations. Veuillez réessayer.'; // Set error message
      }
    }
  }
};
</script>

<style scoped>
/* Aucun style personnalisé pour garantir l'utilisation des classes DSFR uniquement */
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
