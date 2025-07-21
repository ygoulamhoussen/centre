<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NFormItem, NSelect, NDatePicker, NInput, NSpace, NButton, useMessage, NSteps, NStep, NH2, NGrid, NGi, NCard, NIcon } from 'naive-ui'
import { typesRecettes } from '@/constants/compta'
import { updateRecette } from '@/service/api/charges-recettes'
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
const route = useRoute()
const message = useMessage()
const authStore = useAuthStore()
const proprietes = ref<any[]>([])
const selectedPropriete = ref<string | null>(null)
const step = ref(2)
const recette = ref<any>({})

async function fetchProprietes() {
  const resp = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${authStore.userInfo.userId}`)
  if (resp.ok) {
    const data = await resp.json()
    proprietes.value = data
  }
}

async function fetchRecette() {
  const id = route.params.id
  const resp = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/recettes/${id}`)
  if (resp.ok) {
    const data = await resp.json()
    recette.value = data
    selectedPropriete.value = data.proprieteId
  }
}

onMounted(() => {
  fetchProprietes()
  fetchRecette()
})

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
    await updateRecette(payload)
    message.success('Recette modifiée avec succès')
    router.push('/comptabilite')
  } catch (e: any) {
    message.error(e.message || 'Erreur lors de la modification de la recette')
  }
}
</script>
<template>
  <div class="recette-create-page">
    <div class="stepper-center">
      <NSteps :current="2" size="small">
        <NStep title="Propriété" description="Choix du bien" />
        <NStep title="Détails" description="Modification de la recette" />
      </NSteps>
    </div>
    <NH2 class="mb-4 titre-principal">Édition de la recette</NH2>
    <div class="form-section">
      <div class="resume-propriete mb-6" v-if="selectedProprieteInfo">
        <NCard size="small" class="resume-propriete-card">
          <div class="flex items-center gap-4">
            <NIcon :component="BuildingHome24Filled" size="32" />
            <div>
              <div class="font-bold">{{ selectedProprieteInfo.nom }}</div>
              <div class="text-xs text-gray-500" v-if="selectedProprieteInfo.adresse">{{ selectedProprieteInfo.adresse }}</div>
              <div class="text-xs text-gray-500" v-if="selectedProprieteInfo.surface">Surface : {{ selectedProprieteInfo.surface }} m²</div>
              <div class="text-xs text-gray-500" v-if="selectedProprieteInfo.type">Type : {{ selectedProprieteInfo.type }}</div>
            </div>
          </div>
        </NCard>
      </div>
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
          <NFormItem label="Type" required>
            <NSelect v-model:value="recette.type" :options="typesRecettes" placeholder="Sélectionner un type" style="width: 100%" />
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
            <NButton @click="router.push('/comptabilite')" class="btn-responsive">Annuler</NButton>
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