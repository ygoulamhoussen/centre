<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth' // ajout pour récupérer l'utilisateur connecté
import { useUnifiedStore } from '@/store/unifiedStore'
import { NButton, NForm, NFormItem, NSelect, NSpace, useMessage, NCard, NSteps, NStep, NIcon } from 'naive-ui'
import { BuildingHome24Filled, Person24Filled, ArrowRight24Filled } from '@vicons/fluent'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle location - Étape 1',
    hideInMenu: true,
    activeMenu: '/location',
  }
})

const store = useUnifiedStore()
const { locationDTO } = storeToRefs(store)

const authStore = useAuthStore()
const utilisateurId = authStore.userInfo.userId // 👈 récupération de l'ID connecté

const router = useRouter()
const message = useMessage()

const proprietes = ref<any[]>([])
const locataires = ref<any[]>([])

async function fetchData() {
  try {
    const propResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${utilisateurId}`)
    proprietes.value = await propResponse.json()

    const locResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/${utilisateurId}`)
    locataires.value = await locResponse.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement')
  }
}

function handleProprieteChange(value: string) {
  const selected = proprietes.value.find(p => p.id === value)
  if (selected) {
    locationDTO.value.proprieteId = selected.id
    locationDTO.value.proprieteNom = selected.nom
  }
}

function handleLocataireChange(value: string) {
  const selected = locataires.value.find(l => l.id === value)
  if (selected) {
    locationDTO.value.locataireId = selected.id
    locationDTO.value.locataireNom = selected.nom
  }
}

function suivant() {
  if (!locationDTO.value.proprieteId || !locationDTO.value.locataireId) {
    message.warning('Merci de choisir une propriété et un locataire')
    return
  }
  router.push('/location-etape-2')
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <NSteps :current="1" class="mb-8">
        <NStep title="Sélection" description="Propriété et locataire" />
        <NStep title="Détails du bail" description="Loyer, dates, etc." />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-4">Étape 1: Sélection de la propriété et du locataire</h2>

      <NForm label-placement="top">
        <NFormItem label="Propriété">
          <NSelect
            :value="locationDTO.proprieteId"
            :options="proprietes.map(p => ({ label: p.nom, value: p.id }))"
            placeholder="Choisir une propriété"
            @update:value="handleProprieteChange"
            size="large"
          >
            <template #prefix>
              <NIcon :component="BuildingHome24Filled" />
            </template>
          </NSelect>
        </NFormItem>

        <NFormItem label="Locataire">
          <NSelect
            :value="locationDTO.locataireId"
            :options="locataires.map(l => ({ label: l.nom, value: l.id }))"
            placeholder="Choisir un locataire"
            @update:value="handleLocataireChange"
            size="large"
          >
            <template #prefix>
              <NIcon :component="Person24Filled" />
            </template>
          </NSelect>
        </NFormItem>
      </NForm>

      <div class="flex justify-end mt-8">
        <NButton type="primary" @click="suivant" size="large">
          Suivant
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
          </template>
        </NButton>
      </div>
    </NCard>
  </div>
</template>
