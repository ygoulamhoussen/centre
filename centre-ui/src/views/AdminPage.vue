<template>
  <div class="admin-page">
    <HeaderSection
      title="Administration des Référentiels"
      subtitle="Gérez les formations, les formateurs et les sessions."
      link="/"
      linkText="Retour à l'accueil"
    />
    <section v-if="isAdmin" class="fr-container fr-my-6w">
      <h2 class="fr-h3">Formations</h2>
      <button @click="addFormation" class="fr-btn fr-btn--primary">Ajouter une Formation</button>
      <!-- Liste des formations -->
    </section>

    <section v-if="isAdmin" class="fr-container fr-my-6w">
      <h2 class="fr-h3">Formateurs</h2>
      <button @click="addFormateur" class="fr-btn fr-btn--primary">Ajouter un Formateur</button>
      <!-- Liste des formateurs -->
    </section>

    <section v-if="isAdmin" class="fr-container fr-my-6w">
      <h2 class="fr-h3">Sessions</h2>
      <button @click="addSession" class="fr-btn fr-btn--primary">Ajouter une Session</button>
      <!-- Liste des sessions -->
    </section>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import { ref, onMounted } from 'vue';

export default {
  name: "AdminPage",
  components: {
    HeaderSection
  },
  setup() {
    const isAdmin = ref(false);
    const errorMessage = ref("");

    const initializeAuth = () => {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        isAdmin.value = userProfile.role === 'ADMINISTRATEUR';
      }
    };

    const addFormation = () => {
      try {
        // Logique pour ajouter une formation
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error adding formation:', error);
        errorMessage.value = 'Erreur lors de l\'ajout de la formation. Veuillez réessayer.'; // Set error message
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
      addFormation,
      addFormateur,
      addSession
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
</style>
