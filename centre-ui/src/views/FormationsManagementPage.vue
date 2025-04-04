<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Gestion des Formations"
      subtitle="Ajoutez, mettez à jour ou supprimez des formations."
    />
    <div class="button-container">
      <button @click="openAddForm" class="fr-btn fr-btn--primary">Ajouter</button>
    </div>
    <div class="fr-table fr-table--bordered">
      <div class="fr-table__wrapper">
        <div class="fr-table__container">
          <div class="fr-table__content">
            <table>
              <caption>Liste des Formations</caption>
              <thead>
                <tr>
                  <th>Titre</th>
                  <th>Description</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(formation, index) in formations" :key="formation.id">
                  <td>{{ formation.titre }}</td>
                  <td>{{ formation.description }}</td>
                  <td>
                    <button @click="openEditForm(formation)" class="fr-btn fr-btn--secondary">Modifier</button>
                    <button @click="deleteFormation(index)" class="fr-btn fr-btn--tertiary">Supprimer</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showForm" class="modal-overlay">
      <div class="modal-content">
        <h2>{{ formTitle }}</h2>
        <form ref="formationForm" @submit.prevent="submitForm">
          <div class="form-group">
            <label for="titre">Titre</label>
            <input type="text" id="titre" v-model="form.titre" required />
          </div>
          <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" v-model="form.description" required></textarea>
          </div>
          <div class="form-actions">
            <button type="button" @click="closeForm" class="fr-btn fr-btn--secondary">Annuler</button>
          </div>
        </form>
        <div class="form-submit-outside">
          <button type="button" @click="submitForm" class="fr-btn fr-btn--primary">Enregistrer</button>
        </div>
      </div>
    </div>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="back-button">
      <a href="/admin" class="fr-btn fr-btn--primary">Retour à l'administration</a>
    </p>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  name: "FormationsManagementPage",
  components: {
    HeaderSection,
    DefaultLayout
  },
  data() {
    return {
      formations: [],
      errorMessage: "",
      showForm: false,
      formTitle: "",
      form: {},
      breadcrumbSegments: [
        { name: 'Accueil', link: '/' },
        { name: 'Admin', link: '/admin' },
        { name: 'Gestion des Formations', link: '/admin/formations' }
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
        this.errorMessage = "";
      } catch (error) {
        console.error('Error fetching formations:', error);
        this.errorMessage = 'Erreur lors de la récupération des formations. Veuillez réessayer.';
      }
    },
    async addFormation(formation) {
      try {
        const response = await fetch('http://localhost:8080/api/formations', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formation)
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.fetchFormations();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error adding formation:', error);
        this.errorMessage = 'Erreur lors de l\'ajout de la formation. Veuillez réessayer.';
      }
    },
    async editFormation(id, formation) {
      try {
        const response = await fetch(`http://localhost:8080/api/formations/${id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formation)
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.fetchFormations();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error editing formation:', error);
        this.errorMessage = 'Erreur lors de la modification de la formation. Veuillez réessayer.';
      }
    },
    async deleteFormation(index) {
      const id = this.formations[index].id;
      try {
        const response = await fetch(`http://localhost:8080/api/formations/${id}`, {
          method: 'DELETE'
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.fetchFormations();
        this.errorMessage = "";
      } catch (error) {
        console.error('Error deleting formation:', error);
        this.errorMessage = 'Erreur lors de la suppression de la formation. Veuillez réessayer.';
      }
    },
    openAddForm() {
      this.formTitle = 'Ajouter';
      this.form = { titre: '', description: '' };
      this.showForm = true;
    },
    openEditForm(formation) {
      this.formTitle = 'Modifier';
      this.form = { ...formation };
      this.showForm = true;
    },
    closeForm() {
      this.showForm = false;
    },
    submitForm() {
      // Check HTML5 form validity before submission and trigger browser messages
      if (!this.$refs.formationForm.checkValidity()) {
        this.errorMessage = "Veuillez remplir tous les champs requis.";
        this.$refs.formationForm.reportValidity();
        return;
      } else {
        this.errorMessage = "";
      }
      if (this.form.id === undefined) {
        this.addFormation(this.form);
      } else {
        this.editFormation(this.form.id, this.form);
      }
      // Nettoyage du formulaire après soumission
      this.form = {};
      this.closeForm();
    }
  }
};
</script>

<style scoped>
.button-container {
  text-align: right;
  margin-bottom: 10px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-actions {
  text-align: left;
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

.form-group input,
.form-group textarea {
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

.form-submit-outside {
  text-align: right;
  margin-top: 10px;
}
</style>
