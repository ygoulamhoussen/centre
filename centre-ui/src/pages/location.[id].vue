<script setup lang="ts">
import { onMounted, ref, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'
import {
  NButton,
  NCard,
  NForm,
  NFormItem,
  NInput,
  NDatePicker,
  NSelect,
  NPopconfirm,
  NIcon,
  NDataTable,
  NModal,
  NSpace,
  NSpin,
  NEmpty,
  useMessage,
  NTabs,
  NTabPane,
} from 'naive-ui'
import {
  ArrowLeft24Filled,
  Edit24Filled,
  Save24Filled,
  Dismiss24Filled,
  Add24Filled,
  Delete24Filled,
  ArrowDownload24Filled,
  DocumentPdf24Filled,
  Document24Filled,
  Image24Filled,
} from '@vicons/fluent'

// Utilitaires pour choisir la bonne icône suivant l'extension
function getDocumentIcon(name: string) {
  const ext = name.split('.').pop()?.toLowerCase()
  if (ext === 'pdf') return DocumentPdf24Filled
  if (['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'].includes(ext || ''))
    return Document24Filled
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp'].includes(ext || ''))
    return Image24Filled
  return Document24Filled
}

definePage({
  meta: {
    title: 'Détail location',
    hideInMenu: true,
    activeMenu: '/location',
  },
})

const route = useRoute()
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()

const loading = ref(true)
const saving = ref(false)
const editing = ref(false)

const locationId = Array.isArray(route.params.id)
  ? route.params.id[0]
  : (route.params.id as string)

const location = ref<any>(null)
const formData = ref({
  id: '',
  dateDebut: null as number | null,
  dateFin: null as number | null,
  frequenceLoyer: 'MENSUEL',
  jourEcheance: '',
  loyerMensuel: '',
  chargesMensuelles: '',
  depotGarantie: '',
})

// Documents
const documents = ref<any[]>([])
const showDocumentModal = ref(false)
const documentForm = ref({
  id: '',
  type: 'BAIL',
  fichier: null as File | null,
  contenu: '',
  util: authStore.userInfo.userId,
  proprieteId: '',
  locataireId: '',
  titre: '',
  nomFichier: '',
  dateDocument: new Date().toISOString().split('T')[0],
})

const fileInput = ref<HTMLInputElement | null>(null)

function formatDate(str: string) {
  if (!str) return 'Non défini'
  return new Date(str).toLocaleDateString('fr-FR')
}

async function loadLocation() {
  try {
    loading.value = true
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocationDetail/${locationId}`
    )
    if (!res.ok) throw new Error('Erreur de chargement')
    const data = await res.json()
    location.value = data
    formData.value = {
      id: data.id,
      dateDebut: data.dateDebut ? new Date(data.dateDebut).getTime() : null,
      dateFin: data.dateFin ? new Date(data.dateFin).getTime() : null,
      frequenceLoyer: data.frequenceLoyer,
      jourEcheance: data.jourEcheance,
      loyerMensuel: data.loyerMensuel,
      chargesMensuelles: data.chargesMensuelles,
      depotGarantie: data.depotGarantie,
    }
    await loadDocuments()
  } catch (err) {
    console.error(err)
    message.error('Impossible de charger la location')
    location.value = null // S'assurer que la page affiche 'non trouvé'
  } finally {
    loading.value = false
  }
}

async function loadDocuments() {
  if (!location.value) return
  try {
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getDocumentsByUtilisateur/${authStore.userInfo.userId}`
    )
    if (!res.ok) {
      documents.value = []
      return
    }
    const allDocs = await res.json()
    documents.value = allDocs.filter(
      (d: any) =>
        d.locataireId === location.value?.locataire?.id ||
        d.proprieteId === location.value?.propriete?.id
    )
  } catch (e) {
    console.error(e)
    documents.value = []
  }
}

function startEditing() {
  editing.value = true
}

function cancelEditing() {
  editing.value = false
  if (location.value) {
    formData.value = {
      id: location.value.id,
      dateDebut: location.value.dateDebut
        ? new Date(location.value.dateDebut).getTime()
        : null,
      dateFin: location.value.dateFin
        ? new Date(location.value.dateFin).getTime()
        : null,
      frequenceLoyer: location.value.frequenceLoyer,
      jourEcheance: location.value.jourEcheance,
      loyerMensuel: location.value.loyerMensuel,
      chargesMensuelles: location.value.chargesMensuelles,
      depotGarantie: location.value.depotGarantie,
    }
  }
}

async function saveLocation() {
  try {
    saving.value = true

    const toISODateString = (timestamp: number | null) => {
      if (!timestamp) return null
      return new Date(timestamp).toISOString().split('T')[0]
    }

    const body = {
      ...formData.value,
      dateDebut: toISODateString(formData.value.dateDebut),
      dateFin: toISODateString(formData.value.dateFin),
      proprieteId: location.value.propriete.id,
      locataireId: location.value.locataire.id,
    }
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/updateLocation`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      }
    )
    if (!res.ok) throw new Error(await res.text())
    message.success('Location mise à jour')
    editing.value = false
    await loadLocation()
  } catch (err: any) {
    console.error(err)
    message.error(`Erreur: ${err.message}`)
  } finally {
    saving.value = false
  }
}

async function deleteLocation() {
  try {
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteLocation/${locationId}`,
      { method: 'DELETE' }
    )

    if (!res.ok) {
      const errorText = await res.text()
      if (errorText.includes('quittance_location_id_fkey')) {
        message.error(
          'Impossible de supprimer : des quittances sont liées à cette location.'
        )
      } else {
        message.error('La suppression a échoué. Le serveur a retourné une erreur.')
      }
      return
    }

    message.success('Location supprimée avec succès')
    router.push('/location')
  } catch (error: any) {
    console.error('Delete location fetch error:', error)
    message.error('Erreur de communication avec le serveur lors de la suppression.')
  }
}

function handleFileUpload(event: Event) {
  const input = event.target as HTMLInputElement
  if (!input.files || !input.files.length) return
  const file = input.files[0]
  const reader = new FileReader()
  reader.onload = () => {
    const result = reader.result as string
    if (!result.startsWith('data:')) return
    documentForm.value.fichier = file
    documentForm.value.contenu = result.split(',')[1]
    documentForm.value.nomFichier = file.name
    documentForm.value.titre = file.name
  }
  reader.readAsDataURL(file)
}

async function uploadDocument() {
  if (!documentForm.value.contenu) {
    message.error('Sélectionnez un fichier')
    return
  }
  try {
    saving.value = true
    const payload = {
      ...documentForm.value,
      proprieteId: location.value.propriete.id,
      locataireId: location.value.locataire.id,
      utilisateurId: authStore.userInfo.userId,
    }
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/uploadDocument`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    message.success('Document ajouté')
    showDocumentModal.value = false
    await loadDocuments()
  } catch (e) {
    console.error(e)
    message.error("Erreur lors de l'upload")
  } finally {
    saving.value = false
  }
}

async function deleteDocument(id: string) {
  try {
    await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/documents/${id}`, {
      method: 'DELETE',
    })
    message.success('Document supprimé')
    await loadDocuments()
  } catch (e) {
    console.error(e)
    message.error('Erreur suppression')
  }
}

function downloadDocument(row: any) {
  window.open(
    `${import.meta.env.VITE_SERVICE_BASE_URL}/api/downloadDocument/${row.id}`,
    '_blank'
  )
}

const documentColumns = [
  {
    title: 'Type',
    key: 'typeDocument',
    render: (row: any) =>
      h('div', { class: 'flex items-center' }, [
        h(NIcon, { component: getDocumentIcon(row.nomFichier || ''), size: 20 }),
        h('span', { class: 'ml-2' }, row.typeDocument || 'Inconnu'),
      ]),
  },
  { title: 'Nom', key: 'titre' },
  {
    title: 'Date',
    key: 'dateDocument',
    render: (row: any) => formatDate(row.dateDocument),
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 100,
    render: (row: any) =>
      h('div', { class: 'flex space-x-2' }, [
        h(
          NButton,
          { size: 'small', text: true, onClick: () => downloadDocument(row) },
          () => h(NIcon, { component: ArrowDownload24Filled, size: 18 })
        ),
        h(
          NPopconfirm,
          { onPositiveClick: () => deleteDocument(row.id) },
          {
            trigger: () =>
              h(
                NButton,
                { size: 'small', text: true, type: 'error' },
                () => h(NIcon, { component: Delete24Filled, size: 18 })
              ),
            default: () => 'Confirmer la suppression ?',
          }
        ),
      ]),
  },
]

onMounted(() => {
  loadLocation()
})
</script>

<template>
  <div class="p-4">
    <NButton text @click="router.back()" class="mb-4">
      <template #icon><NIcon :component="ArrowLeft24Filled" /></template>
      Retour
    </NButton>

    <NSpin :show="loading">
      <div v-if="location">
        <h2 class="text-xl font-bold mb-4">Location – {{ location.propriete?.nom }}</h2>

        <NTabs type="line" animated>
          <NTabPane name="details" tab="Détails">
            <NCard class="mt-4">
              <NForm label-placement="top" :disabled="!editing" class="max-w-xl">
                <NFormItem label="Date début">
                  <NDatePicker v-model:value="formData.dateDebut" type="date" class="w-full" />
                </NFormItem>
                <NFormItem label="Date fin">
                  <NDatePicker v-model:value="formData.dateFin" type="date" class="w-full" />
                </NFormItem>
                <NFormItem label="Loyer mensuel (€)">
                  <NInput v-model:value="formData.loyerMensuel" />
                </NFormItem>
                <NFormItem label="Charges mensuelles (€)">
                  <NInput v-model:value="formData.chargesMensuelles" />
                </NFormItem>
                <NFormItem label="Dépôt garantie (€)">
                  <NInput v-model:value="formData.depotGarantie" />
                </NFormItem>
                <NFormItem label="Fréquence loyer">
                  <NSelect
                    v-model:value="formData.frequenceLoyer"
                    :options="[
                      { label: 'Mensuel', value: 'MENSUEL' },
                      { label: 'Trimestriel', value: 'TRIMESTRIEL' },
                    ]"
                  />
                </NFormItem>
                <NFormItem label="Jour échéance (1-28)">
                  <NInput v-model:value="formData.jourEcheance" />
                </NFormItem>
              </NForm>

              <div class="flex justify-between items-center mt-6">
                <div>
                  <NPopconfirm
                    v-if="!editing"
                    @positive-click="deleteLocation"
                    positive-text="Oui, supprimer"
                    negative-text="Annuler"
                  >
                    <template #trigger>
                      <NButton type="error" ghost>
                        <template #icon><NIcon :component="Delete24Filled" /></template>
                        Supprimer la location
                      </NButton>
                    </template>
                    Êtes-vous sûr de vouloir supprimer cette location ? Cette action est irréversible.
                  </NPopconfirm>
                </div>

                <div class="flex items-center">
                  <NButton v-if="editing" @click="cancelEditing" class="mr-2">
                    <template #icon><NIcon :component="Dismiss24Filled" /></template>
                    Annuler
                  </NButton>
                  <NButton v-if="editing" type="primary" :loading="saving" @click="saveLocation">
                    <template #icon><NIcon :component="Save24Filled" /></template>
                    Enregistrer
                  </NButton>
                  <NButton v-if="!editing" type="primary" @click="startEditing">
                    <template #icon><NIcon :component="Edit24Filled" /></template>
                    Modifier
                  </NButton>
                </div>
              </div>
            </NCard>
          </NTabPane>

          <NTabPane name="documents" tab="Documents">
            <NCard class="mt-4">
              <div class="flex justify-between items-center mb-4">
                <h3 class="text-lg font-semibold">Documents associés</h3>
                <NButton type="primary" size="small" @click="showDocumentModal = true">
                  <template #icon><NIcon :component="Add24Filled" /></template>
                  Ajouter un document
                </NButton>
              </div>
              <NDataTable :columns="documentColumns" :data="documents" />
            </NCard>
          </NTabPane>
        </NTabs>

        <NModal v-model:show="showDocumentModal" preset="card" style="width: 600px;" title="Ajouter un document">
          <NSpace vertical :size="16">
            <p>Sélectionnez un fichier. Le titre sera automatiquement défini sur le nom du fichier.</p>
            <input ref="fileInput" type="file" @change="handleFileUpload" />
            <NButton type="primary" :loading="saving" @click="uploadDocument" :disabled="!documentForm.fichier">
              Téléverser et lier
            </NButton>
          </NSpace>
        </NModal>
      </div>

      <div v-else class="text-center py-12">
        <NEmpty description="Location non trouvée ou en cours de chargement..." />
      </div>
    </NSpin>
  </div>
</template>

<style scoped>
.location-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}
.location-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>
