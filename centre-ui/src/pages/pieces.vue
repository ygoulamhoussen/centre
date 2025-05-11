<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import {
  NButton,
  NDataTable,
  NH1,
  useMessage
} from 'naive-ui'
import { h, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Mes documents',
    icon: 'mdi:file-document',
    order: 6
  }
})

const authStore = useAuthStore()
const userId = authStore.userInfo.userId
const router = useRouter()
const message = useMessage()
const documents = ref([])

async function fetchDocuments() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getDocumentsByUtilisateur/${userId}`)
    documents.value = await res.json()
  } catch (e) {
    message.error('Erreur lors du chargement des documents.')
  }
}

function ajouterDocument() {
  router.push('/pieces-ajout')
}

async function telecharger(documentId: string, nom: string) {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/downloadDocument/${documentId}`)
    if (!res.ok) throw new Error('Erreur lors du téléchargement')

    const base64 = await res.text()

    // Décode la base64 en bytes
    const binary = atob(base64)
    const bytes = new Uint8Array(binary.length)
    for (let i = 0; i < binary.length; i++) {
      bytes[i] = binary.charCodeAt(i)
    }

    // Devine le type MIME en fonction de l’extension
    const extension = nom.split('.').pop()?.toLowerCase() || ''
    const mimeTypes: Record<string, string> = {
      pdf: 'application/pdf',
      jpg: 'image/jpeg',
      jpeg: 'image/jpeg',
      png: 'image/png',
      docx: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      doc: 'application/msword',
      txt: 'text/plain',
      csv: 'text/csv'
    }

    const mimeType = mimeTypes[extension] || 'application/octet-stream'

    const blob = new Blob([bytes], { type: mimeType })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = nom
    document.body.appendChild(link)
    link.click()
    link.remove()
    URL.revokeObjectURL(link.href)
  } catch (e) {
    console.error(e)
    message.error('Impossible de télécharger le document')
  }
}



const columns = [
  { title: 'Titre', key: 'titre' },
  { title: 'Type', key: 'typeDocument' },
  { title: 'Date', key: 'dateDocument' },
  {
    title: 'Actions',
    key: 'actions',
    render(row: any) {
      return h(
        NButton,
        {
          size: 'small',
          onClick: () => telecharger(row.id, row.titre || 'document.bin')
        },
        { default: () => 'Télécharger' }
      )
    }
  }
]

onMounted(fetchDocuments)
</script>

<template>
  <div>
    <div class="flex justify-between items-center mb-4">
      <NH1>Mes documents</NH1>
      <NButton type="primary" @click="ajouterDocument">Ajouter</NButton>
    </div>

    <NDataTable :columns="columns" :data="documents" />
  </div>
</template>

<style scoped>
.flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.mb-4 {
  margin-bottom: 1rem;
}
</style>
