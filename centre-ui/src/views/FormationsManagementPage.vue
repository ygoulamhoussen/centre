<template>
  <div class="formations-management-page">
    <HeaderSection
      title="Gestion des Formations"
      subtitle="Ajoutez, mettez à jour ou supprimez des formations."
      link="/admin"
      linkText="Retour à l'administration"
    />
    <section class="fr-container fr-my-6w">
      <button @click="showAddForm" class="fr-btn fr-btn--primary">Ajouter une Formation</button>
      <DSFRTable
        title="Liste des Formations"
        :headers="['Titre', 'Description', 'Actions']"
        :rows="tableRows"
        :escapeHtml="false"
      />
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </section>

    <div v-if="showForm" class="fr-modal" id="modal-form">
      <div class="fr-container fr-my-6w">
        <h2>{{ formTitle }}</h2>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="titre">Titre</label>
            <input type="text" id="titre" v-model="form.titre" required />
          </div>
          <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" v-model="form.description" required></textarea>
          </div>
          <button type="submit" class="fr-btn fr-btn--primary">Enregistrer</button>
          <button type="button" @click="cancelForm" class="fr-btn fr-btn--secondary">Annuler</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DSFRTable from '../components/DSFRTable.vue';
import { ref, onMounted, computed } from 'vue';

export default {
  name: "FormationsManagementPage",
  components: {
    HeaderSection,
    DSFRTable
  },
  setup() {
    const formations = ref([]);
    const errorMessage = ref("");
    const showForm = ref(false);
    const formTitle = ref("");
    const form = ref({
      id: null,
      titre: "",
      description: ""
    });

    const fetchFormations = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/formations');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        formations.value = await response.json();
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error fetching formations:', error);
        errorMessage.value = 'Erreur lors de la récupération des formations. Veuillez réessayer.'; // Set error message
      }
    };

    const showAddForm = () => {
      formTitle.value = "Ajouter une Formation";
      form.value = { id: null, titre: "", description: "" };
      showForm.value = true;
    };

    const editFormation = (formation) => {
      formTitle.value = "Modifier la Formation";
      form.value = { ...formation };
      showForm.value = true;
    };

    const deleteFormation = async (id) => {
      try {
        const response = await fetch(`http://localhost:8080/api/formations/${id}`, {
          method: 'DELETE'
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        fetchFormations();
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error deleting formation:', error);
        errorMessage.value = 'Erreur lors de la suppression de la formation. Veuillez réessayer.'; // Set error message
      }
    };

    const submitForm = async () => {
      try {
        const method = form.value.id ? 'PUT' : 'POST';
        const url = form.value.id ? `http://localhost:8080/api/formations/${form.value.id}` : 'http://localhost:8080/api/formations';
        const response = await fetch(url, {
          method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(form.value)
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        fetchFormations();
        showForm.value = false;
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error saving formation:', error);
        errorMessage.value = 'Erreur lors de l\'enregistrement de la formation. Veuillez réessayer.'; // Set error message
      }
    };

    const cancelForm = () => {
      showForm.value = false;
    };

    const tableRows = computed(() => {
      return formations.value.map(formation => [
        formation.titre,
        formation.description,
        `<button @click="editFormation(${formation})" class="fr-btn fr-btn--secondary">Modifier</button>
         <button @click="deleteFormation(${formation.id})" class="fr-btn fr-btn--tertiary">Supprimer</button>`
      ]);
    });

    onMounted(() => {
      fetchFormations();
    });

    return {
      formations,
      errorMessage,
      showForm,
      formTitle,
      form,
      showAddForm,
      editFormation,
      deleteFormation,
      submitForm,
      cancelForm,
      tableRows
    };
  }
};
</script>

<style scoped>
.formations-management-page {
  padding: 20px;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
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

.fr-modal {
  display: block;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}
</style>
