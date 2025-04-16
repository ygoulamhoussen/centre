<template>
  <div class="fr-container fr-my-4w">
    <h1 class="fr-h3">Mes propriétés</h1>

    <div v-if="errorMessage" class="fr-alert fr-alert--error">{{ errorMessage }}</div>

    <ul v-if="proprietes.length > 0" class="fr-mt-2w">
      <li v-for="propriete in proprietes" :key="propriete.id">
        <strong>{{ propriete.nom }}</strong> — {{ propriete.ville }}
      </li>
    </ul>

    <h2 class="fr-h4 fr-mt-4w">Ajouter une propriété</h2>
    <form @submit.prevent="addPropriete">
      <div class="fr-input-group">
        <label class="fr-label" for="nom">Nom</label>
        <input class="fr-input" id="nom" v-model="nouvellePropriete.nom" required />
      </div>

      <div class="fr-input-group">
        <label class="fr-label" for="ville">Ville</label>
        <input class="fr-input" id="ville" v-model="nouvellePropriete.ville" required />
      </div>

      <div class="fr-input-group">
        <label class="fr-label" for="type">Type de bien</label>
        <input class="fr-input" id="type" v-model="nouvellePropriete.typeBien" required />
      </div>

      <button class="fr-btn fr-mt-2w" type="submit">Ajouter</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'ProprieteView',
  data() {
    return {
      utilisateurId: '00000000-0000-0000-0000-000000000001', // à remplacer si besoin
      proprietes: [],
      nouvellePropriete: {
        nom: '',
        ville: '',
        typeBien: ''
      },
      errorMessage: ''
    };
  },
  async created() {
    await this.fetchProprietes();
  },
  methods: {
    async fetchProprietes() {
      try {
        const response = await fetch(`${this.$backendUrl}/api/getProprietesByUtilisateur/${this.utilisateurId}`);
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText);
        }
        this.proprietes = await response.json();
      } catch (error) {
        console.error('Erreur lors du chargement des propriétés :', error);
        this.errorMessage = `Erreur : ${error.message}`;
      }
    },

    async addPropriete() {
      try {
        const response = await fetch(`${this.$backendUrl}/api/createPropriete/${this.utilisateurId}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.nouvellePropriete)
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText);
        }

        await this.fetchProprietes();
        this.nouvellePropriete = { nom: '', ville: '', typeBien: '' };
      } catch (error) {
        console.error('Erreur lors de l\'ajout de la propriété :', error);
        this.errorMessage = `Erreur : ${error.message}`;
      }
    }
  }
};
</script>
