<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { NButton, NDatePicker, NForm, NFormItem, NH1, NSelect, NSpace, useMessage } from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Ajouter une quittance - Étape 1',
    hideInMenu: true,
    activeMenu: '/quittance',
    roles: ['USER', 'ADMIN'],
  },
})


const router = useRouter()
const message = useMessage()
const store = useUnifiedStore()
const { quittanceDTO } = storeToRefs(store)
const locations = ref<any[]>([])
const authStore = useAuthStore()
const userId = authStore.userInfo.userId

async function fetchLocations() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocationsByUtilisateur/${userId}`)
    locations.value = await res.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement des locations')
  }
}

function suivant() {
  if (!quittanceDTO.value.locationId || !quittanceDTO.value.dateDebut || !quittanceDTO.value.dateFin) {
    message.warning('Veuillez compléter tous les champs')
    return
  }
  router.push('/quittance-etape-2')
}

onMounted(() => fetchLocations())
</script>

<template>
  <NSpace vertical :size="24">
    <NH1>Nouvelle quittance - Étape 1</NH1>
    <NForm label-placement="top">
      <NFormItem label="Location">
        <NSelect
          v-model:value="quittanceDTO.locationId"
          :options="locations.map(l => ({ label: l.proprieteNom + ' - ' + l.locataireNom, value: l.id }))"
          placeholder="Choisir une location"
        />
      </NFormItem>
      <NFormItem label="Période début">
        <NDatePicker v-model:formatted-value="quittanceDTO.dateDebut" value-format="yyyy-MM-dd" type="date" clearable />
      </NFormItem>
      <NFormItem label="Période fin">
        <NDatePicker v-model:formatted-value="quittanceDTO.dateFin" value-format="yyyy-MM-dd" type="date" clearable />
      </NFormItem>
    </NForm>
    <div class="flex justify-end gap-2">
      <NButton type="primary" @click="suivant">Suivant</NButton>
    </div>
  </NSpace>
</template>

<style scoped></style>