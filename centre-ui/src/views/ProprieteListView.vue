<template>
  <section class="fr-container fr-my-4w">
    <h1 class="fr-h3">Mes propriétés</h1>

    <div class="fr-mb-4w">
      <DsfrButton icon="ri-add-line" @click="demarrerCreation">
        Ajouter une propriété
      </DsfrButton>
    </div>

    <ul v-if="proprietes.length > 0" class="fr-list">
      <li v-for="propriete in proprietes" :key="propriete.id" class="fr-card fr-card--sm fr-mb-2w">
        <div class="fr-card__body">
          <h4 class="fr-card__title">{{ propriete.nom }}</h4>
          <p>{{ propriete.adresse }}, {{ propriete.ville }}</p>
          <p><strong>Type:</strong> {{ propriete.typeBien }}</p>
        </div>
      </li>
    </ul>

    <p v-else>Aucune propriété enregistrée.</p>
  </section>
</template>

<script>
import { mapState } from 'pinia'
import { useUnifiedStore } from '@/stores/unifiedStore'

export default {
  name: 'ProprietesView',
  data() {
    return {
      proprietes: [],
    }
  },
  computed: {
    ...mapState(useUnifiedStore, ['proprieteDTO']),
  },
  methods: {
    async fetchProprietes() {
      try {
        const response = await fetch(`${this.$backendUrl}/api/getProprietesByUtilisateur/00000000-0000-0000-0000-000000000001`)
        const data = await response.json()
        this.proprietes = data
      } catch (error) {
        console.error('Erreur lors du chargement des propriétés :', error)
      }
    },
    demarrerCreation() {
      this.$router.push('/propriete/etape-1')
    }
  },
  mounted() {
    this.fetchProprietes()
  }
}
</script>
