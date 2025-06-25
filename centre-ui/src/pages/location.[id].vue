<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { Add24Filled, ArrowLeft24Filled, Delete24Filled, Edit24Filled, Eye24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDataTable,
  NDatePicker,
  NEmpty,
  NForm,
  NFormItem,
  NH2,
  NH3,
  NIcon,
  NInput,
  NInputNumber,
  NModal,
  NPopconfirm,
  NSelect,
  NSpin,
  NTabPane,
  NTabs,
  NTag,
  NText,
  NUpload,
  NUploadDragger,
  useMessage
} from 'naive-ui'
import { computed, h, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

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

const documentTypes = [
  { value: 'BAIL', label: 'Bail de location' },
  { value: 'QUITTANCE', label: 'Quittance de loyer' },
  { value: 'FACTURE', label: 'Facture' },
  { value: 'JUSTIFICATIF', label: 'Justificatif (revenus, identité...)' },
  { value: 'CONTRAT_DE_CREDIT', label: 'Contrat de crédit' },
  { value: 'AUTRE', label: 'Autre document' },
]

const initialDocumentForm = {
  id: '',
  typeDocument: 'AUTRE',
  fichier: null as File | null,
  contenu: '',
  proprieteId: '',
  locataireId: '',
  titre: '',
  nomFichier: '',
  dateDocument: new Date().getTime(),
}

const documentForm = ref({ ...initialDocumentForm })

function resetDocumentForm() {
  documentForm.value = {
    ...initialDocumentForm,
    dateDocument: new Date().getTime(),
  }
}

function openDocumentModal() {
  resetDocumentForm()
  showDocumentModal.value = true
}

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

function handleFileUpload({ file }: { file: any }) {
  if (!file || !file.file) return

  const uploadedFile = file.file as File

  if (uploadedFile.size > 10 * 1024 * 1024) {
    message.error('Le fichier est trop volumineux. La taille maximale est de 10MB.')
    return
  }

  const reader = new FileReader()
  reader.onload = () => {
    const result = reader.result as string
    if (result.startsWith('data:')) {
      documentForm.value.fichier = uploadedFile
      documentForm.value.contenu = result.split(',')[1]
      documentForm.value.nomFichier = uploadedFile.name
      if (!documentForm.value.titre) {
        documentForm.value.titre =
          uploadedFile.name.split('.').slice(0, -1).join('.') || uploadedFile.name
      }
    } else {
      message.error('Format de fichier non supporté')
    }
  }
  reader.onerror = () => {
    message.error('Erreur lors de la lecture du fichier')
  }
  reader.readAsDataURL(uploadedFile)
}

async function uploadDocument() {
  if (!documentForm.value.contenu || !documentForm.value.fichier) {
    message.error('Veuillez sélectionner un fichier')
    return
  }
  if (!documentForm.value.titre) {
    message.error('Veuillez saisir un titre pour le document')
    return
  }

  try {
    saving.value = true
    const dateDoc = documentForm.value.dateDocument
      ? new Date(documentForm.value.dateDocument).toISOString().split('T')[0]
      : new Date().toISOString().split('T')[0]

    const payload = {
      contenu: documentForm.value.contenu,
      nomFichier: documentForm.value.nomFichier,
      titre: documentForm.value.titre,
      typeDocument: documentForm.value.typeDocument,
      dateDocument: dateDoc,
      proprieteId: location.value.propriete.id,
      locataireId: location.value.locataire.id,
      utilisateurId: authStore.userInfo.userId,
    }
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/uploadDocument`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      }
    )

    if (!res.ok) throw new Error("Échec de l'envoi du document")

    message.success('Document ajouté avec succès')
    showDocumentModal.value = false
    await loadDocuments()
  } catch (e: any) {
    console.error(e)
    message.error(e.message || "Erreur lors de l'envoi du document")
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

const quittances = ref<any[]>([])
const quittanceLoading = ref(false)

async function loadQuittances() {
  quittanceLoading.value = true
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getQuittancesByLocation/${locationId}`)
    if (!res.ok) throw new Error('Erreur de chargement des quittances')
    quittances.value = await res.json()
  } catch (e) {
    console.error(e)
    quittances.value = []
  } finally {
    quittanceLoading.value = false
  }
}

function getTotalQuittance(q: any) {
  const loyer = Number(q.montantLoyer) || 0
  const charges = Number(q.montantCharges) || 0
  const caution = Number(q.depotGarantie) || 0
  return (loyer + charges + caution).toFixed(2)
}

const quittanceColumns = [
  { title: 'Période', key: 'periode', render: (row: any) => `${row.dateDebut} - ${row.dateFin}` },
  { title: 'Loyer', key: 'montantLoyer', render: (row: any) => `${row.montantLoyer} €` },
  { title: 'Charges', key: 'montantCharges', render: (row: any) => `${row.montantCharges} €` },
  { title: 'Caution', key: 'depotGarantie', render: (row: any) => row.depotGarantie ? `${Number(row.depotGarantie).toFixed(2)} €` : '-' },
  { title: 'Total', key: 'total', render: (row: any) => `${getTotalQuittance(row)} €` },
  { title: 'Statut', key: 'statut', render: (row: any) => h(NTag, { type: row.statut === 'PAYEE' ? 'success' : (row.statut === 'PARTIELLE' ? 'warning' : 'error'), size: 'small' }, { default: () => row.statut === 'PAYEE' ? 'Payée' : (row.statut === 'PARTIELLE' ? 'Partielle' : 'Impayée') }) },
  { title: 'Actions', key: 'actions', render: (row: any) => h('div', { class: 'flex gap-2' }, [
    h(NButton, { size: 'small', ghost: true, onClick: () => router.push(`/quittance-detail/${row.id}`) }, { default: () => 'Modifier' }),
    row.statut === 'PAYEE'
      ? h(NButton, { size: 'small', type: 'primary', ghost: true, onClick: () => telechargerQuittance(row.id) }, { default: () => 'Editer la quittance' })
      : null
  ]) },
]

async function telechargerQuittance(id: string) {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/generateQuittancePdf/${id}`, { method: 'GET' })
    const blob = await res.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `quittance-${id}.pdf`
    link.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    console.error(e)
    message.error('Erreur lors du téléchargement du PDF')
  }
}

onMounted(() => {
  loadLocation()
  loadQuittances()
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
        <NH2 class="titre-principal mb-4">Location – {{ location.propriete?.nom }}</NH2>

        <NTabs type="line" animated @update:value="(tab) => { if(tab==='quittances'){loadQuittances()} }">
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
                <NH3 class="sous-titre">Documents associés</NH3>
                <NButton type="primary" size="small" @click="openDocumentModal">
                  <template #icon><NIcon :component="Add24Filled" /></template>
                  Ajouter un document
                </NButton>
              </div>
              <NDataTable :columns="documentColumns" :data="documents" />
            </NCard>
          </NTabPane>

          <NTabPane name="quittances" tab="Quittances">
            <NCard class="mt-4">
              <NH3 class="sous-titre mb-4">Quittances de cette location</NH3>
              <NDataTable :columns="quittanceColumns" :data="quittances" :loading="quittanceLoading" />
            </NCard>
          </NTabPane>
        </NTabs>

        <NModal
          v-model:show="showDocumentModal"
          preset="card"
          style="width: 600px"
          title="Ajouter un nouveau document"
          :on-after-leave="resetDocumentForm"
        >
          <NForm :model="documentForm" label-placement="top">
            <NFormItem label="Titre du document" required>
              <NInput
                v-model:value="documentForm.titre"
                placeholder="Ex: Bail de location signé"
              />
            </NFormItem>
            <NFormItem label="Type de document" required>
              <NSelect
                v-model:value="documentForm.typeDocument"
                :options="documentTypes"
              />
            </NFormItem>
            <NFormItem label="Date du document" required>
              <NDatePicker
                v-model:value="documentForm.dateDocument"
                type="date"
                class="w-full"
              />
            </NFormItem>
            <NFormItem label="Fichier" required>
              <NUpload :max="1" :default-upload="false" @change="handleFileUpload">
                <NUploadDragger>
                  <div
                    class="flex flex-col items-center justify-center gap-2 p-4"
                  >
                    <NIcon
                      :component="Document24Filled"
                      size="48"
                      class="text-gray-400"
                    />
                    <NText>Cliquez ou glissez-déposez un fichier ici</NText>
                  </div>
                </NUploadDragger>
              </NUpload>
            </NFormItem>
            <div class="flex justify-end gap-2">
              <NButton @click="showDocumentModal = false">Annuler</NButton>
              <NButton
                type="primary"
                :loading="saving"
                :disabled="!documentForm.fichier"
                @click="uploadDocument"
              >
                Enregistrer le document
              </NButton>
            </div>
          </NForm>
        </NModal>
      </div>

      <div v-else class="text-center py-12">
        <NEmpty description="Location non trouvée ou en cours de chargement..." />
      </div>
    </NSpin>
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
.sous-titre {
  color: var(--n-text-color) !important;
  font-weight: 600;
  font-size: 1.1rem;
}
.location-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}
.location-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
@media (max-width: 768px) {
  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }
  .sous-titre {
    font-size: 1rem;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-4 {
    margin-bottom: 1rem !important;
  }
}
</style>
