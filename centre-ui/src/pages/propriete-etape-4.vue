<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useAuthStore } from '@/store/modules/auth'
import {
  NButton,
  NForm,
  NFormItem,
  NInputNumber,
  NSpace,
  NGrid,
  NGi,
  useMessage,
  NCard,
  NSteps,
  NStep,
  NIcon,
  NRadioGroup,
  NRadio,
  NDescriptions,
  NDescriptionsItem,
  NText,
  NAlert,
  NH2,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  ArrowLeft24Filled,
  Save24Filled,
} from '@vicons/fluent'

definePage({
  meta: {
    title: 'Nouvelle propriété - Récapitulatif',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()

const immobilisation = ref<'oui' | 'non'>('non')
const chargement = ref(false)

const decompositions = ref([
  { categorie: 'Terrains', duree: null as number | null, quotePart: 15 as number | null, description: 'Non amortissable', pourcentageMin: 10, pourcentageMax: 20 },
  { categorie: 'Bâti (gros œuvre)', duree: 35 as number | null, quotePart: 55 as number | null, description: 'Amortissement sur 35 ans', pourcentageMin: 50, pourcentageMax: 60 },
  { categorie: 'Agencements et second œuvre', duree: 12 as number | null, quotePart: 20 as number | null, description: 'Amortissement sur 12 ans', pourcentageMin: 20, pourcentageMax: 30 },
  { categorie: 'Mobilier et électroménager', duree: 7 as number | null, quotePart: 10 as number | null, description: 'Amortissement sur 7 ans', pourcentageMin: 0, pourcentageMax: 100 },
  { categorie: 'Frais d\'acquisition', duree: 5 as number | null, quotePart: null as number | null, description: 'Amortissement sur 5 ans', pourcentageMin: 0, pourcentageMax: 100 },
])

const sommePourcentages = computed(() => {
  return decompositions.value.reduce((sum, item) => sum + (item.quotePart || 0), 0)
})

function validerPourcentages() {
  if (sommePourcentages.value !== 100) {
    message.error(`La somme des pourcentages doit être de 100% (actuellement ${sommePourcentages.value}%)`)
    return false
  }
  return true
}

function precedent() {
  router.push('/propriete-etape-3')
}

async function enregistrer() {
  if (immobilisation.value === 'oui' && !validerPourcentages()) {
    return
  }

  chargement.value = true
  try {
    const utilisateurId = authStore.userInfo.userId

    const finalDTO = {
      ...proprieteDTO.value,
      dateAcquisition: proprieteDTO.value.dateAcquisition || null,
      montantAcquisition: proprieteDTO.value.montantAcquisition || '0',
      fraisNotaire: proprieteDTO.value.fraisNotaire || '0',
      fraisAgence: proprieteDTO.value.fraisAgence || '0',
      tantieme: proprieteDTO.value.tantieme || '0',
      compositions: immobilisation.value === 'oui'
        ? decompositions.value
            .filter(r => r.quotePart != null && r.quotePart > 0)
            .map(r => ({
              categorie: r.categorie,
              montant: r.quotePart!.toString(),
              description: r.description,
              duree: r.duree
            }))
        : [],
    }

    const response = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/createPropriete/${utilisateurId}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(finalDTO),
      },
    )

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || 'Erreur lors de la sauvegarde de la propriété')
    }

    store.resetProprieteDTO()
    message.success('Propriété créée avec succès')
    router.push('/propriete')
  }
  catch (e: any) {
    message.error(e.message || 'Erreur inconnue lors de la création de la propriété')
  }
  finally {
    chargement.value = false
  }
}

const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="4" class="mb-8">
          <NStep title="Type et Nom" description="Identification du bien" />
          <NStep title="Adresse" description="Localisation du bien" />
          <NStep title="Détails" description="Informations financières" />
          <NStep title="Récapitulatif" description="Vérification et sauvegarde" />
        </NSteps>
      </div>

      <NH2 class="titre-principal mb-6">Étape 4: Récapitulatif et Amortissement</NH2>

      <NDescriptions label-placement="top" bordered :columns="2" title="Récapitulatif de la propriété">
        <NDescriptionsItem label="Nom de la propriété" :label-style="{ fontWeight: 'bold' }">{{
          proprieteDTO.nom
        }}</NDescriptionsItem>
        <NDescriptionsItem label="Type de bien" :label-style="{ fontWeight: 'bold' }">{{
          proprieteDTO.typeBien
        }}</NDescriptionsItem>
        <NDescriptionsItem label="Adresse" :label-style="{ fontWeight: 'bold' }" :span="2">
          {{ proprieteDTO.adresse }}, {{ proprieteDTO.codePostal }} {{ proprieteDTO.ville }}
          <br>
          <span v-if="proprieteDTO.complementAdresse">{{ proprieteDTO.complementAdresse }}</span>
        </NDescriptionsItem>
        <NDescriptionsItem label="Date d'acquisition">{{ proprieteDTO.dateAcquisition || 'Non spécifiée' }}</NDescriptionsItem>
        <NDescriptionsItem label="Montant d'acquisition">{{ proprieteDTO.montantAcquisition }} €</NDescriptionsItem>
        <NDescriptionsItem label="Frais de notaire">{{ proprieteDTO.fraisNotaire || 0 }} €</NDescriptionsItem>
        <NDescriptionsItem label="Frais d'agence">{{ proprieteDTO.fraisAgence || 0 }} €</NDescriptionsItem>
        <NDescriptionsItem label="Tantième">{{ proprieteDTO.tantieme || 'Non spécifié' }}</NDescriptionsItem>
      </NDescriptions>

      <div class="mt-8">
        <NCard title="Amortissement comptable" :bordered="true">
          <NFormItem label="Souhaitez-vous créer une immobilisation comptable pour ce bien ?">
            <NRadioGroup v-model:value="immobilisation" name="immobilisation-group">
              <NRadio value="oui">Oui</NRadio>
              <NRadio value="non">Non</NRadio>
            </NRadioGroup>
          </NFormItem>

          <template v-if="immobilisation === 'oui'">
            <NAlert title="Répartition par composantes" type="info" class="mb-4">
              Décomposez le coût d'acquisition selon les catégories d'amortissement standard. La somme des pourcentages doit être égale à 100%.
            </NAlert>
            <NGrid :x-gap="16" :y-gap="16" :cols="isMobile ? 1 : 3" :item-responsive="true" class="form-grid">
              <NGi v-for="item in decompositions" :key="item.categorie">
                <NCard :title="item.categorie" size="small">
                  <NFormItem label="Quote-part (%)">
                    <NInputNumber
                      v-model:value="item.quotePart"
                      :min="0"
                      :max="100"
                      placeholder="%"
                      style="width: 100%;"
                    >
                      <template #suffix>%</template>
                    </NInputNumber>
                  </NFormItem>
                  <NFormItem label="Durée (années)">
                    <NInputNumber
                      v-model:value="item.duree"
                      :min="1"
                      :max="100"
                      placeholder="Durée"
                      style="width: 100%;"
                    />
                  </NFormItem>
                  <template #footer>
                    <NText depth="3" class="text-xs">{{ item.description }}</NText>
                  </template>
                </NCard>
              </NGi>
            </NGrid>
             <NAlert
              title="Validation"
              :type="sommePourcentages === 100 ? 'success' : 'error'"
              class="mt-4"
            >
              Total des pourcentages : {{ sommePourcentages }}%
            </NAlert>
          </template>
        </NCard>
      </div>

      <NSpace class="mt-8" justify="space-between">
        <NButton @click="precedent" title="Précédent">
          <template #icon>
            <NIcon :component="ArrowLeft24Filled" />
          </template>
        </NButton>
        <NButton type="primary" @click="enregistrer" :loading="chargement" title="Enregistrer la propriété">
          <template #icon>
            <NIcon :component="Save24Filled" />
          </template>
        </NButton>
      </NSpace>
    </NCard>
  </div>
</template>

<style scoped>
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
@media (max-width: 768px) {
  .mb-8 {
    margin-bottom: 1rem !important;
  }
  .n-steps {
    font-size: 12px !important;
    min-width: 400px;
  }
  .n-steps,
  .n-steps .n-steps-main {
    overflow-x: auto !important;
    white-space: nowrap !important;
    display: block !important;
  }
  .n-step {
    min-width: 120px !important;
  }
  .n-step__description {
    display: none !important;
  }
  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .form-grid {
    grid-template-columns: 1fr !important;
  }
  .form-grid .n-form-item-gi {
    grid-column: 1 !important;
  }
}
</style>
