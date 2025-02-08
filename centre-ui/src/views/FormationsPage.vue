<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Nos Formations"
      subtitle="Découvrez les formations proposées par notre centre pour développer vos compétences dans le domaine informatique."
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
      <p class="back-button">
        <a href="/" class="fr-btn fr-btn--primary">Retour à l'accueil</a>
      </p>
    </section>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import ContentCard from '../components/ContentCard.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "FormationsPage",
  components: {
    HeaderSection,
    ContentCard,
    DefaultLayout
  },
  data() {
    return {
      formations: [],
      errorMessage: "", // Add error message data property
      breadcrumbSegments: [
        { name: 'Accueil', link: '/' },
        { name: 'Formations', link: '/formations' }
      ]
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
.back-button {
  margin-top: 20px;
  text-align: left;
}
</style>
