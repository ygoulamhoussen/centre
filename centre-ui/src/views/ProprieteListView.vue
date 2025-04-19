<template>
  <n-card class="fr-my-4w">
    <h1>Mes propriétés</h1>
    <div style="margin-bottom: 2rem;">
      <n-button type="primary" @click="demarrerCreation">
        Ajouter une propriété
      </n-button>
    </div>
    <n-list v-if="proprietes.length > 0">
      <n-list-item v-for="propriete in proprietes" :key="propriete.id">
        <n-card>
          <h4>{{ propriete.nom }}</h4>
          <p>{{ propriete.adresse }}, {{ propriete.ville }}</p>
          <p><strong>Type:</strong> {{ propriete.typeBien }}</p>
        </n-card>
      </n-list-item>
    </n-list>
    <p v-else>Aucune propriété enregistrée.</p>
  </n-card>
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
