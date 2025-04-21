<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { NButton, NRadioGroup, NRadio, NInputNumber, NInput, NGrid, NGi, useMessage } from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter une propriété - Étape 4',
    hideInMenu: true,
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
  router.push('/propriete_workflow/etape-3')
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

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/createProprieteWithCompositions/${utilisateurId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })

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
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter une propriété - Étape 4</h1>

    <div>
      <span class="block font-medium mb-2">Créer une immobilisation comptable ?</span>
      <NRadioGroup v-model:value="immobilisation" name="immobilisation">
        <NRadio value="oui">Oui</NRadio>
        <NRadio value="non">Non</NRadio>
      </NRadioGroup>
    </div>

    <div v-if="immobilisation === 'oui'">
      <NGrid cols="3" x-gap="16" y-gap="12">
        <NGi v-for="(row, idx) in decompositions" :key="row.categorie">
          <div class="p-2 border rounded-md">
            <p class="font-semibold">{{ row.categorie }}</p>
            <div v-if="row.categorie !== 'Terrains'">
              <label class="block text-sm mb-1">Durée (ans)</label>
              <NInputNumber v-model:value="row.duree" min="0" />
            </div>
            <label class="block text-sm mt-2 mb-1">Quote-part (€)</label>
            <NInputNumber v-model:value="row.quotePart" min="0" step="0.01" />
          </div>
        </NGi>
      </NGrid>
    </div>

    <div class="flex justify-end gap-3">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" @click="enregistrer" :loading="chargement">
        Enregistrer
      </NButton>
    </div>
  </NSpace>
</template>

<style scoped>
p {
  margin-bottom: 0.5rem;
}
</style>
