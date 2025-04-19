<template>
  <n-card class="fr-my-4w">
    <h1>Ajouter une propriété - Étape 4</h1>
    <n-form @submit.prevent="enregistrer">
      <n-form-item label="Créer une immobilisation comptable ?">
        <n-radio-group v-model:value="immobilisation">
          <n-radio value="oui">Oui</n-radio>
          <n-radio value="non">Non</n-radio>
        </n-radio-group>
      </n-form-item>
      <div v-if="immobilisation === 'oui'" class="fr-mt-4w">
        <n-data-table
          :columns="columns"
          :data="decompositions"
          :pagination="false"
          bordered
        >
          <template #duree="{ row }">
            <span v-if="row.categorie === 'Terrains'">Non amortissable</span>
            <n-input-number
              v-else
              v-model:value="row.duree"
              min="0"
              placeholder="Durée (ans)"
              style="width: 120px"
            />
          </template>
          <template #quotePart="{ row }">
            <n-input-number
              v-model:value="row.quotePart"
              min="0"
              step="0.01"
              placeholder="Quote-part (€)"
              style="width: 120px"
            />
          </template>
        </n-data-table>
      </div>
      <div style="margin-top: 2rem;">
        <n-button @click="precedent" secondary style="margin-right: 1rem;">
          Précédent
        </n-button>
        <n-button type="primary" attr-type="submit" :loading="chargement">
          Enregistrer
        </n-button>
      </div>
    </n-form>
    <p v-if="erreur" class="error">{{ erreur }}</p>
  </n-card>
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

const decompositions = ref([
  { categorie: 'Terrains', duree: '', quotePart: '' },
  { categorie: 'Gros œuvre', duree: '', quotePart: '' },
  { categorie: 'Façades', duree: '', quotePart: '' },
  { categorie: 'Étanchéité', duree: '', quotePart: '' },
  { categorie: 'IGT et agencement', duree: '', quotePart: '' }
])

const columns = [
  { title: 'Catégorie', key: 'categorie' },
  { title: "Durée d'amortissement (ans)", key: 'duree', render: 'duree' },
  { title: 'Quote-part (€)', key: 'quotePart', render: 'quotePart' }
]

function precedent() {
  router.push('/propriete/etape-3')
}

async function enregistrer() {
  chargement.value = true
  erreur.value = ''
  try {
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
