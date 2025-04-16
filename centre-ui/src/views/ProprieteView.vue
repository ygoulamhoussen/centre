<template>
  <div class="fr-container fr-mt-5w">
    <h1 class="fr-h4">Mes propriétés</h1>

    <div v-if="proprietes.length === 0" class="fr-alert fr-alert--info fr-mt-3w">
      <p>Aucune propriété enregistrée.</p>
    </div>

    <div class="fr-grid-row fr-grid-row--gutters fr-mt-3w">
      <div class="fr-col-12 fr-col-md-6" v-for="p in proprietes" :key="p.id">
        <div class="fr-tile fr-tile--shadow fr-tile--horizontal">
          <div class="fr-tile__body">
            <h2 class="fr-tile__title">{{ p.nom }}</h2>
            <p class="fr-tile__desc">
              <strong>Type :</strong> {{ p.typeBien }}<br />
              <strong>Ville :</strong> {{ p.ville }}<br />
              <strong>Montant :</strong> {{ p.montantAcquisition }} €
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ProprietesView',
  data() {
    return {
      proprietes: [],
      utilisateurId: '00000000-0000-0000-0000-000000000001'
    }
  },
  async mounted() {
    try {
      const res = await axios.get(`/api/getProprietesByUtilisateur/${this.utilisateurId}`)
      this.proprietes = res.data
    } catch (error) {
      console.error('Erreur chargement propriétés :', error)
    }
  }
}
</script>
