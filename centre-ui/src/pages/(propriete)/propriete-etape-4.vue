<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/store/modules/auth'
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
    title: 'Ajouter une propriété – Étape 4',
    hideInMenu: true,
    activeMenu: '/propriete'
  }
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()

const immobilisation = ref<'oui'|'non'>('non')
const chargement = ref(false)

const decompositions = ref([
  { categorie: 'Terrains', duree: null as number|null, quotePart: null as number|null },
  { categorie: 'Gros œuvre', duree: null as number|null, quotePart: null as number|null },
  { categorie: 'Façades', duree: null as number|null, quotePart: null as number|null },
  { categorie: 'Étanchéité', duree: null as number|null, quotePart: null as number|null },
  { categorie: 'IGT et agencement', duree: null as number|null, quotePart: null as number|null }
])

function precedent() {
  router.push('/propriete-etape-3')
}

async function enregistrer() {
  chargement.value = true
  try {
    const utilisateurId = authStore.userInfo.userId
    // On reconstruit le DTO principal
    const dtoToSend = {
      ...proprieteDTO.value,
      dateAcquisition: proprieteDTO.value.dateAcquisition || null,
      dateLivraison: proprieteDTO.value.dateLivraison || null
    }
    // On génère le tableau des compositions si besoin
    const compositions = immobilisation.value === 'oui'
      ? decompositions.value
          .filter(r => r.quotePart != null && (r.categorie === 'Terrains' || r.duree != null))
          .map(r => ({
            categorie: r.categorie,
            montant: r.quotePart!,
            description: r.categorie === 'Terrains'
              ? 'Non amortissable'
              : `Amortissement sur ${r.duree} ans`
          }))
      : []

    // On envoie tout dans un seul objet
    const payload = {
      ...dtoToSend,
      compositions
    }

    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createPropriete/${utilisateurId}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }
    )
    if (!response.ok) {
      throw new Error('Erreur lors de la sauvegarde de la propriété.')
    }

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
    <NH1>Ajouter une propriété – Étape 4</NH1>

    <NForm label-placement="top">
      <NFormItem label="Créer une immobilisation comptable ?">
        <NRadioGroup v-model:value="immobilisation">
          <NRadio value="oui">Oui</NRadio>
          <NRadio value="non">Non</NRadio>
        </NRadioGroup>
      </NFormItem>

      <template v-if="immobilisation === 'oui'">
        <NText depth="3">Répartition par composantes</NText>
        <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16">
          <NGi v-for="row in decompositions" :key="row.categorie">
            <NSpace vertical class="p-4 border rounded-md">
              <NText strong>{{ row.categorie }}</NText>
              <NFormItem
                v-if="row.categorie !== 'Terrains'"
                label="Durée (ans)"
              >
                <NInputNumber v-model:value="row.duree" min="0" />
              </NFormItem>
              <NFormItem label="Quote-part (€)">
                <NInputNumber v-model:value="row.quotePart" min="0" step="0.01" />
              </NFormItem>
            </NSpace>
          </NGi>
        </NGrid>
      </template>

      <NSpace justify="end" :size="16">
        <NButton @click="precedent">Précédent</NButton>
        <NButton type="primary" @click="enregistrer" :loading="chargement">
          Enregistrer
        </NButton>
      </NSpace>
    </NForm>
  </NSpace>
</template>

<style scoped>
.border {
  border: 1px solid #dcdcdc;
  border-radius: 8px;
}
</style>
