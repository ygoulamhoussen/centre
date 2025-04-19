<template>
  <n-card class="fr-my-4w">
    <h1>Ajouter une propriété - Étape 1</h1>
    <p>Quel est le type de bien&nbsp;?</p>
    <div class="fr-grid-row fr-grid-row--gutters">
      <div
        v-for="type in typesBien"
        :key="type.value"
        class="fr-col-12 fr-col-md-4 fr-mb-2w"
      >
        <n-button
          :type="proprieteDTO.typeBien === type.value ? 'primary' : 'default'"
          @click="choisirType(type.value)"
          style="margin-right: 1rem; margin-bottom: 1rem; width: 100%;"
        >
          {{ type.label }}
        </n-button>
      </div>
    </div>
    <n-form :model="proprieteDTO" style="margin-top: 2rem;">
      <n-form-item label="Nom de la propriété" path="nom">
        <n-input
          v-model:value="proprieteDTO.nom"
          required
          placeholder="Ex : Résidence Les Lilas, Parking centre-ville..."
        />
      </n-form-item>
      <div style="margin-top: 2rem;">
        <n-button
          type="primary"
          :disabled="!proprieteDTO.typeBien || !proprieteDTO.nom"
          @click="valider"
        >
          Suivant
        </n-button>
      </div>
    </n-form>
  </n-card>
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
  store.updateProprieteField('typeBien', type)
}

function valider() {
  router.push('/propriete/etape-2')
}
</script>
