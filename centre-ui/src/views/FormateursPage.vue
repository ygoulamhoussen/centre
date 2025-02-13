<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Gestion des Formateurs"
      subtitle="Ajoutez, modifiez ou supprimez des formateurs."
    />
    <section v-if="isAdmin">
      <DSFRTable
        title="Liste des Formateurs"
        :headers="['Nom', 'Email', 'Actions']"
        :rows="formateursTableRows()"
        @add="addFormateur"
        @edit="editFormateur"
        @delete="deleteFormateur"
      />
    </section>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="back-button">
      <a href="/" class="fr-btn fr-btn--primary">Retour à l'accueil</a>
    </p>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DSFRTable from '../components/DSFRTable.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "FormateursPage",
  components: {
    HeaderSection,
    DSFRTable,
    DefaultLayout
  },
  data() {
    return {
      isAdmin: false,
      errorMessage: "",
      formateurs: [],
      breadcrumbSegments: [
        { name: 'Accueil', link: '/' },
        { name: 'Admin', link: '/admin' },
        { name: 'Formateurs', link: '/admin/formateurs' }
      ]
    };
  },
  created() {
    this.initializeAuth();
    this.fetchFormateurs();
  },
  methods: {
    initializeAuth() {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        this.isAdmin = userProfile.role === 'ADMINISTRATEUR';
      }
    },
    async fetchFormateurs() {
      try {
        const response = await fetch('http://localhost:8080/api/formateurs');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.formateurs = await response.json();
      } catch (error) {
        console.error('Error fetching formateurs:', error);
        this.errorMessage = 'Erreur lors de la récupération des formateurs. Veuillez réessayer.';
      }
    },
    async addFormateur(formateur) {
      try {
        const response = await fetch('http://localhost:8080/api/formateurs', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formateur)
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        await this.fetchFormateurs();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error adding formateur:', error);
        this.errorMessage = 'Erreur lors de l\'ajout du formateur. Veuillez réessayer.';
      }
    },
    async editFormateur(id, formateur) {
      try {
        const response = await fetch(`http://localhost:8080/api/formateurs/${id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formateur)
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        await this.fetchFormateurs();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error editing formateur:', error);
        this.errorMessage = 'Erreur lors de la modification du formateur. Veuillez réessayer.';
      }
    },
    async deleteFormateur(id) {
      try {
        const response = await fetch(`http://localhost:8080/api/formateurs/${id}`, {
          method: 'DELETE'
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        await this.fetchFormateurs();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error deleting formateur:', error);
        this.errorMessage = 'Erreur lors de la suppression du formateur. Veuillez réessayer.';
      }
    },
    formateursTableRows() {
      return this.formateurs.map(formateur => ({
        Nom: formateur.nom,
        Email: formateur.email,
        Actions: {
          edit: () => this.openEditForm(formateur),
          delete: () => this.deleteFormateur(formateur.id)
        }
      }));
    },
    openEditForm(formateur) {
      const form = {
        id: formateur.id,
        Nom: formateur.nom,
        Email: formateur.email
      };
      this.editFormateur(formateur.id, form);
    }
  }
};
</script>

<style scoped>
.formateurs-page {
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
