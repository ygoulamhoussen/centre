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
const erreurSomme = ref<string | null>(null)

const decompositions = ref([
  { 
    categorie: 'Terrains', 
    duree: null as number|null, 
    quotePart: null as number|null,
    description: 'Non amortissable',
    pourcentageMin: 10,
    pourcentageMax: 20
  },
  { 
    categorie: 'Bâti (gros œuvre)', 
    duree: 35 as number|null, 
    quotePart: 55 as number|null,
    description: 'Amortissement sur 35 ans',
    pourcentageMin: 50,
    pourcentageMax: 60
  },
  { 
    categorie: 'Agencements et second œuvre', 
    duree: 12 as number|null, 
    quotePart: 25 as number|null,
    description: 'Amortissement sur 12 ans',
    pourcentageMin: 20,
    pourcentageMax: 30
  },
  { 
    categorie: 'Mobilier et électroménager', 
    duree: 7 as number|null, 
    quotePart: null as number|null,
    description: 'Amortissement sur 7 ans',
    pourcentageMin: 0,
    pourcentageMax: 100
  },
  { 
    categorie: 'Frais d\'acquisition', 
    duree: 5 as number|null, 
    quotePart: null as number|null,
    description: 'Amortissement sur 5 ans',
    pourcentageMin: 0,
    pourcentageMax: 100
  }
])

function precedent() {
  router.push('/propriete-etape-3')
}

function calculerSommePourcentages() {
  return decompositions.value.reduce((sum, item) => sum + (item.quotePart || 0), 0)
}

function validerPourcentages() {
  const total = calculerSommePourcentages()
  if (total !== 100) {
    erreurSomme.value = `La somme des pourcentages doit être de 100% (actuellement ${total}%)`
    return false
  }
  erreurSomme.value = null
  return true
}

async function enregistrer() {
  if (immobilisation.value === 'oui' && !validerPourcentages()) {
    return
  }
  
  chargement.value = true
  try {
    const utilisateurId = authStore.userInfo.userId
    
    // Créer l'objet propriété avec les bonnes propriétés
    const proprieteData = {
      ...proprieteDTO.value,
      dateAcquisition: proprieteDTO.value.dateAcquisition || null,
      dateLivraison: proprieteDTO.value.dateLivraison || null,
      // S'assurer que les compositions sont dans le bon format
      compositions: immobilisation.value === 'oui'
        ? decompositions.value
            .filter(r => r.quotePart != null && (r.categorie === 'Terrains' || r.duree != null))
            .map(r => ({
              categorie: r.categorie,
              montant: r.quotePart!.toString(),
              description: r.description
            }))
        : []
    }

    console.log('Envoi des données au serveur:', JSON.stringify(proprieteData, null, 2))

    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createPropriete/${utilisateurId}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(proprieteData)
      }
    )
    
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || 'Erreur lors de la sauvegarde de la propriété')
    }

    const result = await response.json()
    console.log('Réponse du serveur:', result)

    store.resetProprieteDTO()
    message.success('Propriété créée avec succès')
    router.push('/propriete')
  } catch (e: any) {
    console.error('Erreur détaillée:', e)
    message.error(e.message || 'Erreur inconnue lors de la création de la propriété')
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
        <NText depth="3">Répartition par composantes 
          <span v-if="immobilisation === 'oui'" class="ml-2 text-sm font-medium" :class="{ 'text-green-600': calculerSommePourcentages() === 100, 'text-red-600': calculerSommePourcentages() !== 100 }">
            (Total: {{ calculerSommePourcentages() }}%)
          </span>
        </NText>
        <NText v-if="erreurSomme" type="error" class="block mt-2">
          {{ erreurSomme }}
        </NText>
        <NH1>Décomposition du bien pour amortissement</NH1>
        <NText depth="3" class="block mb-6">
          Décomposez le coût d'acquisition selon les catégories d'amortissement standard. Les pourcentages sont donnés à titre indicatif et peuvent être ajustés selon votre situation.
        </NText>
        <div class="bg-blue-50 p-4 rounded-lg mb-6 text-gray-800">
          <h3 class="font-bold mb-2 text-gray-900">Guide des catégories d'amortissement :</h3>
          <ul class="list-disc pl-5 space-y-1 text-gray-700">
            <li><strong class="text-gray-900">Terrains :</strong> Non amortissable (10-20% du prix d'achat)</li>
            <li><strong class="text-gray-900">Bâti (gros œuvre) :</strong> 30-40 ans (50-60% du prix)</li>
            <li><strong class="text-gray-900">Agencements :</strong> 10-15 ans (20-30% du prix)</li>
            <li><strong class="text-gray-900">Mobilier :</strong> 5-10 ans (selon équipement)</li>
            <li><strong class="text-gray-900">Frais d'acquisition :</strong> 5 ans (frais de notaire, agence, etc.)</li>
          </ul>
        </div>
        <NGrid cols="1 s:1 m:2 l:3" x-gap="16" y-gap="16">
          <NGi v-for="item in decompositions" :key="item.categorie">
            <NSpace vertical class="p-4 border rounded-md">
              <NText strong>{{ item.categorie }}</NText>
              <div class="flex items-center gap-2">
                <NInputNumber
                  v-model:value="item.quotePart"
                  :min="0"
                  :max="100"
                  :show-button="false"
                  placeholder="%"
                  class="w-24"
                  :disabled="immobilisation !== 'oui'"
                />
                <span v-if="item.pourcentageMin !== null" class="text-gray-600 text-sm">
                  ({{ item.pourcentageMin }}% - {{ item.pourcentageMax }}%)
                </span>
              </div>
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
