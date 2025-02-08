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
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: "AdminPage",
  components: {
    HeaderSection,
    DSFRCard,
    DefaultLayout
  },
  setup() {
    const isAdmin = ref(false);
    const errorMessage = ref("");
    const router = useRouter();

    const breadcrumbSegments = [
      { name: 'Accueil', link: '/' },
      { name: 'Admin', link: '/admin' }
    ];

    const initializeAuth = () => {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        isAdmin.value = userProfile.role === 'ADMINISTRATEUR';
      }
    };

    const addFormateur = () => {
      try {
        // Logique pour ajouter un formateur
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error adding formateur:', error);
        errorMessage.value = 'Erreur lors de l\'ajout du formateur. Veuillez réessayer.'; // Set error message
      }
    };

    const addSession = () => {
      try {
        // Logique pour ajouter une session
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error adding session:', error);
        errorMessage.value = 'Erreur lors de l\'ajout de la session. Veuillez réessayer.'; // Set error message
      }
    };

    onMounted(() => {
      initializeAuth();
    });

    return {
      isAdmin,
      errorMessage,
      addFormateur,
      addSession,
      breadcrumbSegments
    };
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
