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
        :rows="formateursTableRows"
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
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: "FormateursPage",
  components: {
    HeaderSection,
    DSFRTable,
    DefaultLayout
  },
  setup() {
    const isAdmin = ref(false);
    const errorMessage = ref("");
    const formateurs = ref([]);
    const router = useRouter();

    const breadcrumbSegments = [
      { name: 'Accueil', link: '/' },
      { name: 'Admin', link: '/admin' },
      { name: 'Formateurs', link: '/admin/formateurs' }
    ];

    const initializeAuth = () => {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        isAdmin.value = userProfile.role === 'ADMINISTRATEUR';
      }
    };

    const fetchFormateurs = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/formateurs');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        formateurs.value = await response.json();
      } catch (error) {
        console.error('Error fetching formateurs:', error);
        errorMessage.value = 'Erreur lors de la récupération des formateurs. Veuillez réessayer.';
      }
    };

    const addFormateur = async (formateur) => {
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
        await fetchFormateurs(); // Refresh the list
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error adding formateur:', error);
        errorMessage.value = 'Erreur lors de l\'ajout du formateur. Veuillez réessayer.';
      }
    };

    const editFormateur = async (id, formateur) => {
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
        await fetchFormateurs(); // Refresh the list
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error editing formateur:', error);
        errorMessage.value = 'Erreur lors de la modification du formateur. Veuillez réessayer.';
      }
    };

    const deleteFormateur = async (id) => {
      try {
        const response = await fetch(`http://localhost:8080/api/formateurs/${id}`, {
          method: 'DELETE'
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        await fetchFormateurs(); // Refresh the list
        errorMessage.value = ""; // Clear error message on success
      } catch (error) {
        console.error('Error deleting formateur:', error);
        errorMessage.value = 'Erreur lors de la suppression du formateur. Veuillez réessayer.';
      }
    };

    const formateursTableRows = computed(() => {
      return formateurs.value.map(formateur => ({
        Nom: formateur.nom,
        Email: formateur.email,
        Actions: {
          edit: () => openEditForm(formateur),
          delete: () => deleteFormateur(formateur.id)
        }
      }));
    });

    const openEditForm = (formateur) => {
      const form = {
        id: formateur.id,
        Nom: formateur.nom,
        Email: formateur.email
      };
      editFormateur(formateur.id, form);
    };

    onMounted(() => {
      initializeAuth();
      fetchFormateurs();
    });

    return {
      isAdmin,
      errorMessage,
      formateursTableRows,
      addFormateur,
      editFormateur,
      deleteFormateur,
      breadcrumbSegments
    };
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
