<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Administration des Référentiels"
      subtitle="Gérez les formations, les formateurs et les sessions."
    />
    <section v-if="isAdmin" >
      <div class="fr-grid-row fr-grid-row--gutters">
        <div class="fr-col-12 fr-col-md-4">
          <DSFRCard
            title="Formations"
            description="Gérez les formations proposées par notre centre."
            link="/admin/formations"

          />
        </div>
        <div class="fr-col-12 fr-col-md-4">
          <DSFRCard
            title="Formateurs"
            description="Gérez les formateurs de notre centre."
            link="/admin/formateurs"
            imageSrc="/img/placeholder.16x9.png"
            imageAlt="Image de formateurs"
          />
        </div>
        <div class="fr-col-12 fr-col-md-4">
          <DSFRCard
            title="Sessions"
            description="Gérez les sessions de formation."
            link="#"
            @click="addSession"
            imageSrc="/img/placeholder.16x9.png"
            imageAlt="Image de sessions"
          />
        </div>
      </div>
    </section>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="back-button">
      <a href="/" class="fr-btn fr-btn--primary">Retour à l'accueil</a>
    </p>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DSFRCard from '../components/DSFRCard.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "AdminPage",
  components: {
    HeaderSection,
    DSFRCard,
    DefaultLayout
  },
  data() {
    return {
      isAdmin: false,
      errorMessage: "",
      breadcrumbSegments: [
        { name: 'Accueil', link: '/' },
        { name: 'Admin', link: '/admin' }
      ]
    };
  },
  created() {
    this.initializeAuth();
  },
  methods: {
    initializeAuth() {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        this.isAdmin = userProfile.role === 'ADMINISTRATEUR';
      }
    },
    addFormateur() {
      try {
        // Logique pour ajouter un formateur
        this.errorMessage = "";
      } catch (error) {
        console.error('Error adding formateur:', error);
        this.errorMessage = 'Erreur lors de l\'ajout du formateur. Veuillez réessayer.';
      }
    },
    addSession() {
      try {
        // Fonctionnalité non implémentée, informer l'utilisateur
        alert("La fonctionnalité d'ajout de session n'est pas encore disponible.");
        this.errorMessage = "";
      } catch (error) {
        console.error('Error adding session:', error);
        this.errorMessage = 'Erreur lors de l\'ajout de la session. Veuillez réessayer.';
      }
    }
  }
};
</script>

<style scoped>
.admin-page {
  padding: 20px;
}

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
