<template>
  <DefaultLayout :breadcrumbSegments="breadcrumbSegments">
    <HeaderSection
      title="Gestion des Formations"
      subtitle="Ajoutez, mettez à jour ou supprimez des formations."
    />
    <DSFRTable
      title="Liste des Formations"
      :headers="['Titre', 'Description', 'Actions']"
      :rows="tableRows"
      @add="addFormation"
      @edit="editFormation"
      @delete="deleteFormation"
    />
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="back-button">
      <a href="/admin" class="fr-btn fr-btn--primary">Retour à l'administration</a>
    </p>
  </DefaultLayout>
</template>

<script>
import HeaderSection from '../components/HeaderSection.vue';
import DSFRTable from '../components/DSFRTable.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import { ref, onMounted, computed } from 'vue';

export default {
  name: "FormationsManagementPage",
  components: {
    HeaderSection,
    DSFRTable,
    DefaultLayout
  },
  setup() {
    const formations = ref([]);
    const errorMessage = ref("");
    const currentFormation = ref(null);

    const breadcrumbSegments = [
      { name: 'Accueil', link: '/' },
      { name: 'Admin', link: '/admin' },
      { name: 'Gestion des Formations', link: '/admin/formations' }
    ];

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

    const addFormation = async (formation) => {
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
        fetchFormations();
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error adding formation:', error);
        errorMessage.value = 'Erreur lors de l\'ajout de la formation. Veuillez réessayer.'; // Set error message
      }
    };

    const editFormation = async (index, formation) => {
      const id = formations.value[index].id;
      currentFormation.value = { ...formations.value[index] }; // Store current formation data
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
        fetchFormations();
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error editing formation:', error);
        errorMessage.value = 'Erreur lors de la modification de la formation. Veuillez réessayer.'; // Set error message
      }
    };

    const deleteFormation = async (index) => {
      const id = formations.value[index].id;
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

    const tableRows = computed(() => {
      return formations.value.map(formation => [
        formation.titre,
        formation.description,
        '' // Placeholder for action buttons
      ]);
    });

    onMounted(() => {
      fetchFormations();
    });

    return {
      formations,
      errorMessage,
      addFormation,
      editFormation,
      deleteFormation,
      tableRows,
      breadcrumbSegments,
      currentFormation
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

.back-button {
  margin-top: 20px;
  text-align: left;
}
</style>
