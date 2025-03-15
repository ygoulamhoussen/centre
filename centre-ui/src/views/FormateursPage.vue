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
    </section>

    <!-- Inline Modal replaced external FormateurFormModal -->
    <div v-if="showAddModal" class="modal-overlay">
      <div class="modal-content">
        <h2>{{ modalTitle }}</h2>
        <form @submit.prevent="submitModal">
          <div class="form-group">
            <label for="nom">Nom</label>
            <input type="text" id="nom" v-model="currentForm.nom" required />
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" v-model="currentForm.email" required />
          </div>
          <div class="form-actions">
            <button type="submit" class="fr-btn fr-btn--primary">Enregistrer</button>
            <button type="button" @click="closeModal" class="fr-btn fr-btn--secondary">Annuler</button>
          </div>
        </form>
      </div>
    </div>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="back-button">
      <a href="/" class="fr-btn fr-btn--primary">Retour à l'accueil</a>
    </p>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "FormateursPage",
  components: {
    HeaderSection,
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
    // New method to handle inline modal submission
    submitModal() {
      this.handleFormSubmit({ ...this.currentForm });
      // Réinitialisation du formulaire après soumission
      this.currentForm = { nom: '', email: '' };
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999; 
}
.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  width: 90%;
  max-width: 500px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
}
.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}
</style>
