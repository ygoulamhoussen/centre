<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { NSpace, NButton, useMessage } from 'naive-ui'

definePage({
  meta: {
    title: 'Ajouter un locataire - Étape 3',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const chargement = ref(false)

async function enregistrer() {
  try {
    chargement.value = true

    const utilisateurId = locataireDTO.value.utilisateurId || '00000000-0000-0000-0000-000000000001' // À adapter

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/createLocataire`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        ...locataireDTO.value,
        utilisateurId
      })
    })

    if (!response.ok) {
      const err = await response.text()
      throw new Error(err)
    }

    store.resetLocataireDTO()
    message.success('Locataire enregistré avec succès.')
    router.push('/locataire') // Redirige vers la liste des locataires

  } catch (e: any) {
    message.error(e.message || 'Erreur lors de l’enregistrement.')
  } finally {
    chargement.value = false
  }
}

function precedent() {
  router.push('/locataire_workflow/etape-2')
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter un locataire - Étape 3</h1>

    <p class="text-gray-500">Valider les informations saisies avant l’enregistrement.</p>

    <ul class="pl-4 list-disc text-sm">
      <li><strong>Nom :</strong> {{ locataireDTO.nom }}</li>
      <li><strong>Téléphone :</strong> {{ locataireDTO.telephone }}</li>
      <li><strong>Email :</strong> {{ locataireDTO.email }}</li>
      <li><strong>Adresse :</strong> {{ locataireDTO.adresse }}</li>
      <li><strong>Complément :</strong> {{ locataireDTO.complementAdresse }}</li>
      <li><strong>Code Postal :</strong> {{ locataireDTO.codePostal }}</li>
      <li><strong>Ville :</strong> {{ locataireDTO.ville }}</li>
    </ul>

    <div class="flex justify-between pt-4">
      <NButton @click="precedent">Précédent</NButton>
      <NButton type="primary" :loading="chargement" @click="enregistrer">Enregistrer</NButton>
    </div>
  </NSpace>
</template>
