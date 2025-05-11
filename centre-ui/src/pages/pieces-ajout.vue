<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { NButton, NText, useMessage } from 'naive-ui'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Mes documents - ajout',
    activeMenu: '/pieces',
    hideInMenu: true,
    order: 6,
  },
})

const authStore = useAuthStore()
const router = useRouter()
const message = useMessage()

const form = ref({
  typeDocument: '',
  dateDocument: null,
  contenu: null as string | null,
  titre: '',
  proprieteId: null as string | null,
  locataireId: null as string | null,
})

const typeOptions = [
  { label: 'Facture', value: 'Facture' },
  { label: 'Quittance', value: 'Quittance' },
  { label: 'Bail', value: 'Bail' },
  { label: 'Justificatif', value: 'Justificatif' },
  { label: 'ContratDeCredit', value: 'ContratDeCredit' },
  { label: 'Autre', value: 'Autre' },
]

const proprietes = ref<any[]>([])
const locataires = ref<any[]>([])

async function fetchOptions() {
  const userId = authStore.userInfo.userId
  try {
    const propRes = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${userId}`)
    proprietes.value = await propRes.json()

    const locRes = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocatairesByUtilisateur/${userId}`)
    locataires.value = await locRes.json()
  } catch (e) {
    message.error('Erreur lors du chargement des données.')
  }
}

function handleFileUpload(event: Event) {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (!file) return

  const reader = new FileReader()
  reader.onload = () => {
    const result = reader.result as string
    if (result.startsWith('data:')) {
      const base64String = result.split(',')[1]
      form.value.contenu = base64String
      form.value.titre = file.name
    } else {
      console.error('Format inattendu pour le fichier')
    }
  }
  reader.readAsDataURL(file)
}

async function submit() {
  if (!form.value.typeDocument) {
    message.error('Veuillez sélectionner un type de document.')
    return
  }

    if (!form.value.dateDocument) {
    message.error('Veuillez saisir une date.')
    return
  }

  if (!form.value.contenu) {
    message.error('Veuillez sélectionner un fichier.')
    return
  }

  if (!form.value.proprieteId && !form.value.locataireId) {
    message.error('Veuillez associer le document à une propriété ou un locataire.')
    return
  }

  const payload = {
    ...form.value,
    utilisateurId: authStore.userInfo.userId,
  }

  const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/uploadDocument`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  if (!res.ok) {
    message.error('Erreur lors de l\'enregistrement')
    return
  }

  message.success('Document enregistré !')
  router.push('/pieces')
}

onMounted(fetchOptions)
</script>

<template>
  <n-form label-placement="top">
    <n-form-item label="Type de document">
      <n-select v-model:value="form.typeDocument" :options="typeOptions" />
    </n-form-item>

    <n-form-item label="Date du document">
      <n-date-picker v-model:formatted-value="form.dateDocument" type="date" value-format="yyyy-MM-dd" />
    </n-form-item>

    <n-form-item label="Propriété associée (obligatoire si pas de locataire)">
      <n-select
        v-model:value="form.proprieteId"
        :options="proprietes.map(p => ({ label: p.nom, value: p.id }))"
        clearable
      />
    </n-form-item>

    <n-form-item label="Locataire associé (obligatoire si pas de propriété)">
      <n-select
        v-model:value="form.locataireId"
        :options="locataires.map(l => ({ label: l.nom, value: l.id }))"
        clearable
      />
    </n-form-item>

    <n-form-item label="Fichier">
      <input type="file" @change="handleFileUpload" />
      <div v-if="form.titre" class="mt-2 text-sm">
        <NText type="info">Fichier sélectionné : {{ form.titre }}</NText>
      </div>
    </n-form-item>

    <NButton type="primary" @click="submit">Enregistrer</NButton>
  </n-form>
</template>
