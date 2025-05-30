<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NCard, NEmpty, NSpace, useMessage } from 'naive-ui'

definePage({
  meta: {
    title: 'Détails locataire',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const route = useRoute()
const router = useRouter()
const message = useMessage()
const loading = ref(true)
const locataire = ref<any>(null)

// Récupérer l'ID du locataire depuis les paramètres de route
const locataireId = route.params.id

// Charger les données du locataire
async function loadLocataire() {
  try {
    loading.value = true
    console.log('Chargement des détails du locataire ID:', locataireId)
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocataireDetails/${locataireId}`
    console.log('URL de l\'API:', url)
    
    const response = await fetch(url)
    console.log('Réponse reçue. Statut:', response.status)
    
    // Récupérer la réponse en tant que texte d'abord
    const responseText = await response.text()
    console.log('Réponse brute:', responseText)
    
    if (!response.ok) {
      console.error('Erreur de réponse. Contenu:', responseText)
      throw new Error(`Erreur HTTP: ${response.status}`)
    }
    
    // Essayer de parser le JSON
    try {
      const data = JSON.parse(responseText)
      console.log('Données JSON parsées:', data)
      locataire.value = data
    } catch (parseError) {
      console.error('Erreur lors de l\'analyse JSON:', parseError)
      throw new Error(`Réponse du serveur invalide (pas du JSON): ${responseText.substring(0, 200)}...`)
    }
  } catch (error) {
    console.error('Erreur lors du chargement du locataire:', error)
    message.error('Impossible de charger les détails du locataire: ' + error.message)
    // On ne redirige plus automatiquement pour pouvoir voir les logs
    // router.push('/locataire')
  } finally {
    loading.value = false
  }
}

// Charger les données au montage du composant
onMounted(() => {
  loadLocataire()
})

// Fonction pour formater la date
function formatDate(dateString: string) {
  if (!dateString) return 'Non spécifié'
  return new Date(dateString).toLocaleDateString('fr-FR')
}
</script>

<template>
  <div class="p-4">
    <NSpace vertical :size="16">
      <div class="flex items-center justify-between">
        <h1 class="text-2xl font-bold">
          {{ locataire ? `${locataire.prenom} ${locataire.nom}` : 'Détails du locataire' }}
        </h1>
        <NButton type="primary" ghost @click="router.push('/locataire')">
          Retour à la liste
        </NButton>
      </div>

      <NCard v-if="loading" :bordered="false">
        <div class="flex justify-center py-8">
          <NSpin size="large" />
        </div>
      </NCard>

      <NCard v-else-if="!locataire" :bordered="false">
        <NEmpty description="Locataire non trouvé" />
      </NCard>

      <template v-else>
        <NCard title="Informations personnelles" :bordered="false">
          <NSpace vertical :size="12">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <h3 class="font-medium text-gray-500">Nom</h3>
                <p>{{ locataire.nom }}</p>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">Prénom</h3>
                <p>{{ locataire.prenom }}</p>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">Email</h3>
                <p>{{ locataire.email || 'Non spécifié' }}</p>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">Téléphone</h3>
                <p>{{ locataire.telephone || 'Non spécifié' }}</p>
              </div>
              <div v-if="locataire.dateNaissance">
                <h3 class="font-medium text-gray-500">Date de naissance</h3>
                <p>{{ formatDate(locataire.dateNaissance) }}</p>
              </div>
            </div>
          </NSpace>
        </NCard>

        <NCard title="Adresse" :bordered="false">
          <NSpace vertical :size="12">
            <div v-if="locataire.adresse || locataire.ville || locataire.codePostal">
              <p v-if="locataire.adresse">{{ locataire.adresse }}</p>
              <p v-if="locataire.codePostal || locataire.ville">
                {{ locataire.codePostal }} {{ locataire.ville }}
              </p>
              <p v-if="locataire.pays">{{ locataire.pays }}</p>
            </div>
            <NEmpty v-else description="Aucune adresse enregistrée" />
          </NSpace>
        </NCard>
      </template>
    </NSpace>
  </div>
</template>

<style scoped>
/* Styles spécifiques à la page si nécessaire */
</style>
