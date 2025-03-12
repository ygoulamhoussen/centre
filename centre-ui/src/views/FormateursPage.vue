<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Gestion des Formateurs"
      subtitle="Ajoutez, modifiez ou supprimez des formateurs."
    />
    <section v-if="isAdmin">
      <!-- Mise en forme identique à DSFRTable -->
      <div class="button-container">
        <button @click="addFormateurPrompt" class="fr-btn fr-btn--primary">Ajouter</button>
      </div>
      <div class="fr-table fr-table--bordered">
        <div class="fr-table__wrapper">
          <div class="fr-table__container">
            <div class="fr-table__content">
              <table>
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="formateur in formateurs" :key="formateur.id">
                    <td>{{ formateur.nom }}</td>
                    <td>{{ formateur.email }}</td>
                    <td>
                      <button @click="openEditForm(formateur)" class="fr-btn fr-btn--secondary">Modifier</button>
                      <button @click="deleteFormateur(formateur.id)" class="fr-btn fr-btn--tertiary">Supprimer</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <!-- Modale utilisée pour ajouter ET modifier -->
      <FormateurFormModal 
        v-if="showAddModal" 
        :title="modalTitle" 
        :form="currentForm" 
        @submit="handleFormSubmit" 
        @cancel="closeModal" />
    </section>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="back-button">
      <a href="/" class="fr-btn fr-btn--primary">Retour à l'accueil</a>
    </p>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import FormateurFormModal from '../components/FormateurFormModal.vue';

export default {
  name: "FormateursPage",
  components: {
    HeaderSection,
    DefaultLayout,
    FormateurFormModal
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
      ],
      showAddModal: false,
      currentForm: {
        nom: '',
        email: ''
      },
      modalTitle: 'Ajouter un formateur'
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
        const response = await fetch('http://localhost:8080/api/formateurs/creer', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(formateur)
        });
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText);
        }
        await this.fetchFormateurs();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error adding formateur:', error);
        this.errorMessage = `Erreur lors de l'ajout du formateur: ${error.message}`;
      }
    },
    async editFormateur(id, formateur) {
      try {
        const response = await fetch(`http://localhost:8080/api/formateurs/update/${id}`, {
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
        const response = await fetch(`http://localhost:8080/api/formateurs/delete/${id}`, {
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
    addFormateurPrompt() {
      this.currentForm = { nom: '', email: '' };
      this.modalTitle = 'Ajouter un formateur';
      this.showAddModal = true;
    },
    openEditForm(formateur) {
      this.currentForm = { ...formateur };
      this.modalTitle = 'Modifier le formateur';
      this.showAddModal = true;
    },
    handleFormSubmit(formData) {
      if (formData.id) {
        this.editFormateur(formData.id, formData);
      } else {
        this.addFormateur(formData);
      }
      this.closeModal();
    },
    closeModal() {
      this.showAddModal = false;
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
