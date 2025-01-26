<template>
  <div class="contact-page">
    <HeaderSection
      title="Contactez-nous"
      subtitle="Nous sommes là pour répondre à toutes vos questions et vous aider à trouver la formation qui vous convient."
      link="/"
      linkText="Retour à l'accueil"
    />
    <div class="fr-container fr-my-6w">
      <div class="fr-grid-row fr-grid-row--gutters">
        <div
          v-for="contact in contacts"
          :key="contact.id"
          class="fr-col-12 fr-col-md-6 fr-col-lg-4"
        >
          <ContentCard :title="contact.title" :description="contact.description" />
        </div>
      </div>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import ContentCard from '../components/ContentCard.vue';

export default {
  name: "ContactPage",
  components: {
    HeaderSection,
    ContentCard
  },
  data() {
    return {
      contacts: [],
      errorMessage: "" // Ajouter une propriété pour les messages d'erreur
    };
  },
  created() {
    this.fetchContacts();
  },
  methods: {
    async fetchContacts() {
      try {
        const response = await fetch('http://localhost:8080/api/contacts');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.contacts = await response.json();
        this.errorMessage = ""; // Effacer le message d'erreur en cas de succès
      } catch (error) {
        console.error('Erreur lors de la récupération des contacts:', error);
        this.errorMessage = 'Erreur lors de la récupération des contacts. Veuillez réessayer.'; // Définir le message d'erreur
      }
    }
  }
};
</script>

<style scoped>
.contact-page {
  padding: 20px;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
