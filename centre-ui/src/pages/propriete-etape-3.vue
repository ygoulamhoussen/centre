<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  CalendarLtr24Filled,
  Money24Filled,
  Tag24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDatePicker,
  NForm,
  NFormItemGi,
  NGrid,
  NIcon,
  NInput,
  NInputNumber,
  NSpace,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle propriété - Infos essentielles',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()

const chargement = ref(false)
const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function precedent() {
  router.push('/propriete-etape-2')
}

async function valider() {
  // Validation des champs obligatoires
  if (!proprieteDTO.value.nom || !proprieteDTO.value.dateAcquisition || !proprieteDTO.value.montantAcquisition) {
    message.warning('Veuillez remplir tous les champs obligatoires.')
    return
  }

  // Validation du prix d'acquisition
  if (proprieteDTO.value.montantAcquisition <= 0) {
    message.warning('Le prix d\'acquisition doit être supérieur à 0.')
    return
  }

  // Validation de la date (≤ aujourd'hui)
  const today = new Date()
  today.setHours(23, 59, 59, 999) // Fin de la journée
  const acquisitionDate = new Date(proprieteDTO.value.dateAcquisition)
  
  if (acquisitionDate > today) {
    message.warning('La date d\'acquisition ne peut pas être dans le futur.')
    return
  }

  chargement.value = true
  try {
    const utilisateurId = authStore.userInfo.userId
    const finalDTO = {
      typeBien: proprieteDTO.value.typeBien,
      nom: proprieteDTO.value.nom,
      dateAcquisition: proprieteDTO.value.dateAcquisition,
      montantAcquisition: proprieteDTO.value.montantAcquisition,
      adresse: proprieteDTO.value.adresse || null,
      codePostal: proprieteDTO.value.codePostal || null,
      ville: proprieteDTO.value.ville || null,
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

    const result = await response.json()
    store.resetProprieteDTO()
    message.success('Propriété créée avec succès 👍')
    
    // Redirection vers la liste des propriétés
    router.push('/propriete')
  }
  catch (e: any) {
    message.error(e.message || 'Erreur inconnue lors de la création de la propriété')
  }
  finally {
    chargement.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <div class="mb-8" v-if="!isMobile">
          <NSteps :current="2" size="small">
            <NStep title="Type de bien" status="finish" />
            <NStep title="Adresse" status="finish" />
            <NStep title="Infos essentielles" status="process" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 3/3</div>
          <div class="step-mobile-label">Infos essentielles</div>
        </div>

        <div class="content-area">
          <div class="section-title mb-6">
            <h2>Informations essentielles</h2>
            <p class="section-subtitle">Tous les champs sont obligatoires</p>
          </div>

          <NForm>
            <NGrid :x-gap="24" :y-gap="24" :cols="isMobile ? 1 : 2" :item-responsive="true" class="form-grid">
              <NFormItemGi :span="2" label="Nom du bien *">
                <NInput
                  v-model:value="proprieteDTO.nom"
                  placeholder="Ex : Résidence Les Lilas, Parking centre-ville..."
                  size="large"
                >
                  <template #prefix>
                    <NIcon :component="Tag24Filled" />
                  </template>
                </NInput>
                <template #feedback>
                  <span class="field-help">Libellé court pour identifier la propriété dans l'app</span>
                </template>
              </NFormItemGi>

              <NFormItemGi label="Date d'acquisition *">
                <NDatePicker
                  v-model:formatted-value="proprieteDTO.dateAcquisition"
                  value-format="yyyy-MM-dd"
                  type="date"
                  clearable
                  style="width: 100%"
                  size="large"
                  :is-date-disabled="(timestamp) => timestamp > Date.now()"
                >
                  <template #prefix>
                    <NIcon :component="CalendarLtr24Filled" />
                  </template>
                </NDatePicker>
              </NFormItemGi>

              <NFormItemGi label="Prix d'acquisition TTC (€) *">
                <NInputNumber
                  v-model:value="proprieteDTO.montantAcquisition"
                  :min="0"
                  placeholder="0.00"
                  style="width: 100%"
                  size="large"
                  inputmode="numeric"
                  pattern="[0-9]*"
                >
                  <template #prefix>
                    <NIcon :component="Money24Filled" />
                  </template>
                </NInputNumber>
              </NFormItemGi>
            </NGrid>
          </NForm>
        </div>

        <div class="button-container">
          <NSpace :class="[isMobile ? 'flex-center' : 'flex-end']" justify="space-between">
            <NButton @click="precedent" title="Précédent">
              <template #icon>
                <NIcon :component="ArrowLeft24Filled" />
              </template>
              Précédent
            </NButton>
            <NButton 
              type="primary" 
              @click="valider" 
              :loading="chargement"
              title="Créer la propriété"
              size="large"
            >
              Créer la propriété
            </NButton>
          </NSpace>
        </div>
      </NCard>
    </div>
  </div>
</template>

<style>
/* Layout principal */
.page-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.recap-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: calc(100vh - 2rem);
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem;
}

.button-container {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
  background: white;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

/* Styles pour les sections */
.section-title {
  text-align: center;
  margin-bottom: 2rem;
}

.section-title h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.section-subtitle {
  font-size: 1rem;
  color: #666;
  margin: 0;
}

.field-help {
  font-size: 0.85rem;
  color: #666;
  font-style: italic;
}

/* Mobile stepper */
.mobile-stepper {
  text-align: center;
  margin-bottom: 1.5rem;
}

.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1976d2;
}

.step-mobile-label {
  font-size: 1.2rem;
  color: #222;
  margin-bottom: 1rem;
}

.flex-center {
  display: flex;
  justify-content: center;
}

.flex-end {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .page-container {
    min-height: 100vh;
  }

  .recap-card {
    min-height: calc(100vh - 2rem);
  }

  .content-area {
    max-height: calc(100vh - 200px);
    overflow-y: auto;
  }

  .button-container {
    position: sticky;
    bottom: 0;
    background: white;
    padding: 1rem 0;
    margin-top: 1rem;
    border-top: 1px solid #f0f0f0;
  }

  .section-title h2 {
    font-size: 1.3rem;
  }

  .section-subtitle {
    font-size: 0.9rem;
  }

  .field-help {
    font-size: 0.8rem;
  }
}
</style>
