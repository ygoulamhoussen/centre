<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  NH1,
  NText,
  NSpace,
  NForm,
  NFormItem,
  NButton,
  NRadioGroup,
  NRadio,
  NInputNumber,
  NGrid,
  NGi,
  useMessage
} from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter une propriété - Étape 4',
    hideInMenu: true,
    activeMenu: '/propriete'
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const immobilisation = ref('')
const chargement = ref(false)

const decompositions = ref([
  { categorie: 'Terrains', duree: null, quotePart: null },
  { categorie: 'Gros œuvre', duree: null, quotePart: null },
  { categorie: 'Façades', duree: null, quotePart: null },
  { categorie: 'Étanchéité', duree: null, quotePart: null },
  { categorie: 'IGT et agencement', duree: null, quotePart: null }
])

function precedent() {
  router.push('/propriete-etape-3')
}

async function enregistrer() {
  chargement.value = true
  try {
    const utilisateurId = '00000000-0000-0000-0000-000000000001'

    const dtoToSend = {
      ...proprieteDTO.value,
      dateAcquisition: proprieteDTO.value.dateAcquisition || null,
      dateLivraison: proprieteDTO.value.dateLivraison || null
    }

    const compositions = immobilisation.value === 'oui'
      ? decompositions.value
          .filter(row => row.quotePart && (row.categorie === 'Terrains' || row.duree))
          .map(row => ({
            categorie: row.categorie,
            montant: row.quotePart,
            description: row.categorie === 'Terrains'
              ? 'Non amortissable'
              : `Amortissement sur ${row.duree} ans`
          }))
      : []

    const payload = {
      propriete: dtoToSend,
      compositions
    }

    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createProprieteWithCompositions/${utilisateurId}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }
    )

    if (!response.ok) throw new Error('Erreur lors de la création de la propriété.')

    store.resetProprieteDTO()
    router.push('/propriete')
  } catch (e: any) {
    message.error(e.message || 'Erreur inconnue')
  } finally {
    chargement.value = false
  }
}
</script>

<template>
  <n-space vertical :size="24">
    <n-h1>Ajouter une propriété - Étape 4</n-h1>

    <n-form label-placement="top">
      <n-form-item label="Créer une immobilisation comptable ?">
        <n-radio-group v-model:value="immobilisation" name="immobilisation">
          <n-radio value="oui">Oui</n-radio>
          <n-radio value="non">Non</n-radio>
        </n-radio-group>
      </n-form-item>

      <template v-if="immobilisation === 'oui'">
        <n-text depth="3">Répartition par composantes</n-text>
        <n-grid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16">
          <n-gi v-for="row in decompositions" :key="row.categorie">
            <n-space vertical class="p-4 border rounded-md">
              <n-text strong>{{ row.categorie }}</n-text>

              <n-form-item v-if="row.categorie !== 'Terrains'" label="Durée (ans)">
                <n-input-number v-model:value="row.duree" min="0" />
              </n-form-item>

              <n-form-item label="Quote-part (€)">
                <n-input-number v-model:value="row.quotePart" min="0" step="0.01" />
              </n-form-item>
            </n-space>
          </n-gi>
        </n-grid>
      </template>

      <n-space justify="end" :size="16">
        <n-button @click="precedent">Précédent</n-button>
        <n-button type="primary" @click="enregistrer" :loading="chargement">Enregistrer</n-button>
      </n-space>
    </n-form>
  </n-space>
</template>

<style scoped>
.border {
  border: 1px solid #dcdcdc;
  border-radius: 8px;
}
</style>
