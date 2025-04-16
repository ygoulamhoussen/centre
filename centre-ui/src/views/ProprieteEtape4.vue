<template>
  <section class="fr-container fr-my-4w">
    <h1 class="fr-h3">Ajouter une propriété - Étape 4</h1>
    <form @submit.prevent="enregistrer">
      <div class="fr-fieldset__element">
        <fieldset class="fr-fieldset">
          <legend class="fr-label">Créer une immobilisation comptable ?</legend>
          <div class="fr-radio-group">
            <input type="radio" id="immobilisationOui" value="oui" v-model="immobilisation" />
            <label for="immobilisationOui">Oui</label>
          </div>
          <div class="fr-radio-group">
            <input type="radio" id="immobilisationNon" value="non" v-model="immobilisation" />
            <label for="immobilisationNon">Non</label>
          </div>
        </fieldset>
      </div>
      <div v-if="immobilisation === 'oui'" class="fr-mt-4w">
        <table class="fr-table">
          <thead>
            <tr>
              <th>Catégorie</th>
              <th>Durée d'amortissement (ans)</th>
              <th>Quote-part (€)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, idx) in decompositions" :key="row.categorie">
              <td>{{ row.categorie }}</td>
              <td>
                <template v-if="row.categorie === 'Terrains'">
                  <span>Non amortissable</span>
                </template>
                <template v-else>
                  <input
                    class="fr-input"
                    type="number"
                    min="0"
                    v-model="row.duree"
                    :placeholder="'Durée (ans)'"
                    required
                  />
                </template>
              </td>
              <td>
                <input
                  class="fr-input"
                  type="number"
                  min="0"
                  step="0.01"
                  v-model="row.quotePart"
                  :placeholder="'Quote-part (€)'"
                  required
                />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="fr-mt-4w">
        <DsfrButton type="button" @click="precedent" secondary class="fr-mr-2w">
          Précédent
        </DsfrButton>
        <DsfrButton type="submit" :disabled="chargement">
          Enregistrer
        </DsfrButton>
      </div>
    </form>
    <p v-if="erreur" class="error">{{ erreur }}</p>
  </section>
</template>

<script setup>
import { useUnifiedStore } from '@/stores/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, getCurrentInstance } from 'vue'

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const { proxy } = getCurrentInstance()

const immobilisation = ref('')
const chargement = ref(false)
const erreur = ref('')

// Catégories fixes pour la décomposition
const decompositions = ref([
  { categorie: 'Terrains', duree: '', quotePart: '' },
  { categorie: 'Gros œuvre', duree: '', quotePart: '' },
  { categorie: 'Façades', duree: '', quotePart: '' },
  { categorie: 'Étanchéité', duree: '', quotePart: '' },
  { categorie: 'IGT et agencement', duree: '', quotePart: '' }
])

function precedent() {
  router.push('/propriete/etape-3')
}

async function enregistrer() {
  chargement.value = true
  erreur.value = ''
  try {
    // Utilise ici la valeur attendue pour l'utilisateur (exemple : valeur fixe ou depuis un autre store)
    const utilisateurId = '00000000-0000-0000-0000-000000000001'
    const dtoToSend = {
      ...proprieteDTO.value,
      dateAcquisition: proprieteDTO.value.dateAcquisition ? proprieteDTO.value.dateAcquisition : null,
      dateLivraison: proprieteDTO.value.dateLivraison ? proprieteDTO.value.dateLivraison : null
    }
    let compositions = []
    if (immobilisation.value === 'oui') {
      compositions = decompositions.value
        .filter(row => row.quotePart && (row.categorie === 'Terrains' || row.duree))
        .map(row => ({
          categorie: row.categorie,
          montant: row.quotePart,
          description: row.categorie === 'Terrains'
            ? 'Non amortissable'
            : `Amortissement sur ${row.duree} ans`
        }))
    }
    const payload = {
      propriete: dtoToSend,
      compositions
    }
    const response = await fetch(`${proxy.$backendUrl}/api/createProprieteWithCompositions/${utilisateurId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
    if (!response.ok) {
      let message = `Erreur lors de la création (code ${response.status})`
      try {
        const errorData = await response.json()
        if (errorData && errorData.message) {
          message += ` : ${errorData.message}`
        }
      } catch {
        // ignore JSON parse error
      }
      throw new Error(message)
    }
    store.resetProprieteDTO()
    router.push('/ProprieteListView')
  } catch (e) {
    erreur.value = e.message || 'Erreur lors de l\'enregistrement de la propriété.'
  } finally {
    chargement.value = false
  }
}
</script>

<style scoped>
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
