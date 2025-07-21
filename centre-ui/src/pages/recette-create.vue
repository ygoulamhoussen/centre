<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { NFormItem, NSelect, NDatePicker, NInput, NSpace, NButton, useMessage, NSteps, NStep, NH2, NGrid, NGi, NCard, NIcon } from 'naive-ui'
import { typesRecettes } from '@/constants/compta'
import { createRecette } from '@/service/api/charges-recettes'
import { useAuthStore } from '@/store/modules/auth'
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
const proprietes = ref<any[]>([])
const selectedPropriete = ref<string | null>(null)
const step = ref(1)
const recette = ref<any>({})

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
  recette.value.proprieteId = propriete.id
  step.value = 2
}
function selectType() {
  if (!recette.value.type) {
    message.error('Veuillez sélectionner un type')
    return
  }
  step.value = 3
}
function selectTypeCard(type: string) {
  recette.value.type = type
  step.value = 3
}
function precedent() {
  if (step.value > 1) step.value--
}
const selectedProprieteInfo = computed(() =>
  proprietes.value.find((p: any) => p.id === selectedPropriete.value)
)

async function valider() {
  if (!recette.value.dateRecette || !recette.value.montant || !recette.value.type || !recette.value.intitule) {
    message.error('Tous les champs sont obligatoires')
    return
  }
  try {
    const payload = { ...recette.value, utilisateurId: authStore.userInfo.userId }
    if (payload.dateRecette && typeof payload.dateRecette === 'number') {
      payload.dateRecette = new Date(payload.dateRecette).toISOString().slice(0, 10)
    }
    await createRecette(payload)
    message.success('Recette créée avec succès')
    router.push('/comptabilite')
  } catch (e: any) {
    message.error(e.message || 'Erreur lors de la création de la recette')
  }
}
</script>
<template>
  <div class="recette-create-page">
    <div class="stepper-center">
      <NSteps :current="step" size="small">
        <NStep title="Propriété" description="Choix du bien" />
        <NStep title="Type" description="Type de recette" />
        <NStep title="Détails" description="Saisie des infos" />
      </NSteps>
    </div>
    <NH2 class="mb-4 titre-principal">
      {{ step === 1 ? 'Étape 1 : Sélection de la propriété' : step === 2 ? 'Étape 2 : Type de recette' : 'Étape 3 : Détails de la recette' }}
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
          <NGi v-for="type in typesRecettes" :key="type.value">
            <NCard hoverable class="nature-card" :class="{ selected: recette.type === type.value }" @click="selectTypeCard(type.value)">
              <div class="font-bold">{{ type.label }}</div>
              <div class="text-xs text-gray-500 mt-1">{{ type.value }}</div>
            </NCard>
          </NGi>
        </NGrid>
      </div>
      <NSpace justify="space-between" class="mt-6 btn-row">
        <NButton @click="precedent" class="btn-responsive">Précédent</NButton>
      </NSpace>
    </div>
    <div v-else class="form-section">
      <NGrid :x-gap="16" :y-gap="0" cols="1 m:2">
        <NGi>
          <NFormItem label="Date" required>
            <NDatePicker v-model:value="recette.dateRecette" type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%" />
          </NFormItem>
        </NGi>
        <NGi>
          <NFormItem label="Montant" required>
            <NInput v-model:value="recette.montant" placeholder="0.00" type="text" style="width: 100%" />
          </NFormItem>
        </NGi>
        <NGi :span="2">
          <NFormItem label="Intitulé" required>
            <NInput v-model:value="recette.intitule" placeholder="Intitulé de la recette" style="width: 100%" />
          </NFormItem>
        </NGi>
        <NGi :span="2">
          <NFormItem label="Description">
            <NInput v-model:value="recette.commentaire" type="textarea" placeholder="Description de la recette" :rows="3" style="width: 100%" />
          </NFormItem>
        </NGi>
        <NGi :span="2">
          <NSpace justify="space-between" class="mt-6 btn-row">
            <NButton @click="precedent" class="btn-responsive">Précédent</NButton>
            <NButton type="primary" @click="valider" class="btn-responsive">Valider</NButton>
          </NSpace>
        </NGi>
      </NGrid>
    </div>
  </div>
</template>
<style scoped>
.recette-create-page {
  width: 100%;
  min-height: 100vh;
  background: var(--n-body-color);
  padding: 32px 24px;
  box-sizing: border-box;
  overflow-x: hidden;
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
.btn-row {
  width: 100%;
}
.btn-responsive {
  min-width: 120px;
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
  .recette-create-page {
    padding: 16px 8px;
  }
  .form-section {
    padding: 16px 4px;
  }
  .btn-row {
    flex-direction: column !important;
    gap: 12px !important;
  }
  .btn-responsive {
    width: 100%;
    min-width: 0;
  }
}
</style> 