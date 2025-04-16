<template>
  <section class="fr-container fr-my-4w">
    <h1 class="fr-h3">Ajouter une propriété - Étape 1</h1>
    <p>Quel est le type de bien&nbsp;?</p>
    <div class="fr-grid-row fr-grid-row--gutters">
      <div
        v-for="type in typesBien"
        :key="type.value"
        class="fr-col-12 fr-col-md-4 fr-mb-2w"
      >
        <DsfrButton
          :secondary="proprieteDTO.typeBien !== type.value"
          :primary="proprieteDTO.typeBien === type.value"
          @click="choisirType(type.value)"
          class="fr-mr-1w"
        >
          {{ type.label }}
        </DsfrButton>
      </div>
    </div>
    <div class="fr-input-group fr-mt-4w">
      <label class="fr-label" for="nomPropriete">Nom de la propriété</label>
      <input
        class="fr-input"
        id="nomPropriete"
        v-model="proprieteDTO.nom"
        required
        placeholder="Ex : Résidence Les Lilas, Parking centre-ville..."
      />
    </div>
    <div class="fr-mt-4w">
      <DsfrButton
        :disabled="!proprieteDTO.typeBien || !proprieteDTO.nom"
        @click="valider"
      >
        Suivant
      </DsfrButton>
    </div>
  </section>
</template>

<script setup>
import { useUnifiedStore } from '@/stores/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

const typesBien = [
  { value: 'APPARTEMENT', label: 'Appartement' },
  { value: 'MAISON', label: 'Maison' },
  { value: 'BOX', label: 'Box' },
  { value: 'PARKING', label: 'Parking' }
]

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()

function choisirType(type) {
  // On stocke bien la valeur enum attendue par le backend
  store.updateProprieteField('typeBien', type)
}

function valider() {
  router.push('/propriete/etape-2')
}
</script>
