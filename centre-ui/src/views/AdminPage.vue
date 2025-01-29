<template>
  <div class="admin-page">
    <HeaderSection
      title="Administration des Référentiels"
      subtitle="Gérez les formations, les formateurs et les sessions."
      link="/"
      linkText="Retour à l'accueil"
    />
    <section v-if="isAdmin" class="fr-container fr-my-6w">
      <div class="fr-grid-row fr-grid-row--gutters">
        <div class="fr-col-12 fr-col-md-4">
          <DSFRCard
            title="Formations"
            description="Gérez les formations proposées par notre centre."
            link="/admin/formations"
            imageSrc="/img/placeholder.16x9.png"
            imageAlt="Image de formations"
          />
        </div>
        <div class="fr-col-12 fr-col-md-4">
          <DSFRCard
            title="Formateurs"
            description="Gérez les formateurs de notre centre."
            link="#"
            @click="addFormateur"
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
  </div>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DSFRCard from '../components/DSFRCard.vue';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: "AdminPage",
  components: {
    HeaderSection,
    DSFRCard
  },
  setup() {
    const isAdmin = ref(false);
    const errorMessage = ref("");
    const router = useRouter();

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
