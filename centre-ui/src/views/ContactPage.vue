<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Contactez-nous"
      subtitle="Nous sommes là pour répondre à toutes vos questions et vous aider à trouver la formation qui vous convient."
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
      <p class="back-button">
        <a href="/" class="fr-btn fr-btn--primary">Retour à l'accueil</a>
      </p>
    </div>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import ContentCard from '../components/ContentCard.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "ContactPage",
  components: {
    HeaderSection,
    ContentCard,
    DefaultLayout
  },
  data() {
    return {
      contacts: [
        {
          id: 1,
          title: "Service Client",
          description: "Pour toute question relative à nos formations, contactez notre service client."
        },
        {
          id: 2,
          title: "Support Technique",
          description: "Pour toute assistance technique, contactez notre support technique."
        },
        {
          id: 3,
          title: "Service Administratif",
          description: "Pour toute question administrative, contactez notre service administratif."
        }
      ],
      errorMessage: "", // Ajouter une propriété pour les messages d'erreur
      breadcrumbSegments: [
        { name: 'Accueil', link: '/' },
        { name: 'Contact', link: '/contact' }
      ]
    };
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

.back-button {
  margin-top: 20px;
  text-align: left;
}
</style>
