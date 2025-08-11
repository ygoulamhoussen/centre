<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { NButton, NCard, NDatePicker, NFormItem, NGrid, NGi, NH2, NIcon, NInput, NSelect, NStep, NSteps, NSpace, useMessage } from 'naive-ui'
import { naturesCharges } from '@/constants/compta'
import { createCharge } from '@/service/api/charges-recettes'
import { useAuthStore } from '@/store/modules/auth'
import { useAppStore } from '@/store/modules/app'
import { BuildingHome24Filled } from '@vicons/fluent'
definePage({
  meta: {
    title: 'Comptabilité',
    icon: 'material-symbols:calculate',
    order: 9,
    hideInMenu:true,
  },
})
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()
const appStore = useAppStore()
const isMobile = appStore.isMobile
const proprietes = ref<any[]>([])
const selectedPropriete = ref<string | null>(null)
const step = ref(1)
const charge = ref<any>({})

async function fetchProprietes() {
  const resp = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${authStore.userInfo.userId}`)
  if (resp.ok) {
    const data = await resp.json()
    proprietes.value = data
  }
}
fetchProprietes()

function selectPropriete(propriete: any) {
  selectedPropriete.value = propriete.id
  charge.value.proprieteId = propriete.id
  step.value = 2
}
function selectNature() {
  if (!charge.value.nature) {
    message.error('Veuillez sélectionner une nature')
    return
  }
  step.value = 3
}
function precedent() {
  if (step.value > 1) step.value--
}
function selectNatureCard(nature) {
  charge.value.nature = nature
  // Préremplir la date et l'intitulé à l'étape 3
  if (!charge.value.dateCharge) {
    charge.value.dateCharge = Date.now()
  }
  if (!charge.value.intitule) {
    const prop = selectedProprieteInfo.value
    const propNom = prop ? prop.nom || prop.label : ''
    charge.value.intitule = `${propNom} - ${nature}`
  }
  step.value = 3
}
const selectedProprieteInfo = computed(() =>
  proprietes.value.find((p: any) => p.id === selectedPropriete.value)
)

async function valider() {
  if (!charge.value.dateCharge || !charge.value.montant || !charge.value.nature || !charge.value.intitule) {
    message.error('Tous les champs sont obligatoires')
    return
  }
  try {
    const payload = { ...charge.value, utilisateurId: authStore.userInfo.userId }
    if (payload.dateCharge && typeof payload.dateCharge === 'number') {
      payload.dateCharge = new Date(payload.dateCharge).toISOString().slice(0, 10)
    }
    await createCharge(payload)
    message.success('Charge créée avec succès')
    router.push('/comptabilite')
  } catch (e: any) {
    message.error(e.message || 'Erreur lors de la création de la charge')
  }
}
</script>
<template>
  <div class="page-container">
    <div class="p-4 charge-create-page">
      <NCard :bordered="false" class="recap-card">
        <div v-if="!isMobile" class="stepper-center">
          <NSteps :current="step" size="small">
            <NStep title="Propriété" description="Choix du bien" />
            <NStep title="Nature" description="Type de charge" />
            <NStep title="Détails" description="Saisie des infos" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape {{ step }}/3</div>
          <div class="step-mobile-label">
            {{ step === 1 ? 'Sélection de la propriété' : step === 2 ? 'Nature de la charge' : 'Détails de la charge' }}
          </div>
        </div>
        
        <div class="content-area">
          <NH2 class="mb-4 titre-principal">
            {{ step === 1 ? 'Étape 1 : Sélection de la propriété' : step === 2 ? 'Étape 2 : Nature de la charge' : 'Étape 3 : Détails de la charge' }}
          </NH2>
          
          <div v-if="step === 1">
            <NGrid :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
              <NGi v-for="propriete in proprietes" :key="propriete.id">
                <NCard hoverable class="propriete-card" :class="{ selected: selectedPropriete === propriete.id }" @click="selectPropriete(propriete)">
                  <div class="flex items-start gap-4">
                    <div class="propriete-avatar">
                      <NIcon :component="BuildingHome24Filled" size="32" />
                    </div>
                    <div class="flex-1">
                      <div class="propriete-nom mb-2">{{ propriete.nom }}</div>
                      <div class="text-sm space-y-1">
                        <div v-if="propriete.adresse">{{ propriete.adresse }}</div>
                        <div v-if="propriete.surface">Surface : {{ propriete.surface }} m²</div>
                        <div v-if="propriete.type">Type : {{ propriete.type }}</div>
                      </div>
                    </div>
                  </div>
                </NCard>
              </NGi>
            </NGrid>
          </div>
          
          <div v-else-if="step === 2" class="form-section">
            <div class="nature-grid">
              <NGrid :x-gap="16" :y-gap="16" cols="1 s:2 m:3 l:4">
                <NGi v-for="nature in naturesCharges" :key="nature.value">
                  <NCard hoverable class="nature-card" :class="{ selected: charge.nature === nature.value }" @click="selectNatureCard(nature.value)">
                    <div class="font-bold">{{ nature.label }}</div>
                    <div class="text-xs text-gray-500 mt-1">{{ nature.value }}</div>
                  </NCard>
                </NGi>
              </NGrid>
            </div>
          </div>
          
          <div v-else class="form-section">
            <NGrid :x-gap="16" :y-gap="0" cols="1 m:2">
              <NGi>
                <NFormItem label="Date" required>
                  <NDatePicker v-model:value="charge.dateCharge" type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%" />
                </NFormItem>
              </NGi>
              <NGi>
                <NFormItem label="Montant" required>
                  <NInput v-model:value="charge.montant" placeholder="0.00" type="text" style="width: 100%" />
                </NFormItem>
              </NGi>
              <NGi :span="2">
                <NFormItem label="Intitulé" required>
                  <NInput v-model:value="charge.intitule" placeholder="Intitulé de la charge" style="width: 100%" />
                </NFormItem>
              </NGi>
              <NGi :span="2">
                <NFormItem label="Description">
                  <NInput v-model:value="charge.commentaire" type="textarea" placeholder="Description de la charge" :rows="3" style="width: 100%" />
                </NFormItem>
              </NGi>
            </NGrid>
          </div>
        </div>

        <!-- Boutons de navigation -->
        <div class="button-container">
          <div v-if="step === 2" class="step-actions">
            <NButton @click="precedent">
              <template #icon>
                <Icon icon="material-symbols:arrow-back" />
              </template>
              Précédent
            </NButton>
          </div>
          <div v-if="step === 3" class="step-actions">
            <NButton @click="precedent">
              <template #icon>
                <Icon icon="material-symbols:arrow-back" />
              </template>
              Précédent
            </NButton>
            <NButton type="primary" @click="valider">
              Valider
              <template #icon>
                <Icon icon="material-symbols:check" />
              </template>
            </NButton>
          </div>
        </div>
      </NCard>
    </div>
  </div>
</template>
<style scoped>
/* Layout principal */
.page-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.recap-card {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* Assure que le contenu interne de NCard s'étire et permet le scroll dans .content-area */
.recap-card :deep(.n-card__content) {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0; /* essentiel pour autoriser le scroll d'un enfant flex */
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem;
  -webkit-overflow-scrolling: touch;
  min-height: 0; /* évite les contraintes de hauteur */
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

/* Styles existants */
.charge-create-page {
  width: 100%;
  background: var(--n-body-color);
  padding: 32px 24px;
  box-sizing: border-box;
  overflow-x: hidden;
  max-width: 100%;
}
.stepper-center {
  display: flex;
  justify-content: center;
  margin-bottom: 32px;
}
.form-section {
  width: 100%;
  padding: 32px 24px;
  box-sizing: border-box;
  background: transparent;
  overflow-x: hidden;
  max-width: 100%;
}
.titre-principal { font-size: 1.2rem; font-weight: bold; }
.propriete-card {
  cursor: pointer;
  transition: box-shadow 0.2s, border-color 0.2s;
  border: 2px solid transparent;
}
.propriete-card.selected {
  border-color: #2563eb;
  box-shadow: 0 0 0 2px #2563eb33;
}
.propriete-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f4f6;
  border-radius: 8px;
  width: 48px;
  height: 48px;
}
.resume-propriete-card {
  background: #f8fafc;
  border: 1px solid #e5e7eb;
}
.step-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.step-actions-mobile {
  flex-direction: column !important;
  gap: 10px !important;
  align-items: stretch !important;
}

.step-actions-mobile .n-button {
  width: 100%;
  max-width: 320px;
  align-self: center;
}
.nature-grid {
  margin-bottom: 24px;
}
.nature-card {
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.2s, box-shadow 0.2s;
  text-align: center;
  min-height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.nature-card.selected {
  border-color: #2563eb;
  box-shadow: 0 0 0 2px #2563eb33;
  background: #f0f6ff;
}
@media (max-width: 900px) {
  .page-container { height: 100vh; }
  .recap-card { height: 100%; }
  .content-area { padding-bottom: 1rem; }

  .button-container {
    position: sticky;
    bottom: 0;
    background: white;
    padding: 1rem 0;
    margin-top: 1rem;
    border-top: 1px solid #f0f0f0;
  }

  .charge-create-page {
    padding: 16px 8px;
  }
  .form-section {
    padding: 16px 4px;
  }
  .step-actions {
    flex-direction: column !important;
    gap: 12px !important;
  }
}
</style> 