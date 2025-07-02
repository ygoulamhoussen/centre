<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  Add24Filled,
  ArrowLeft24Filled,
  ChartMultiple24Filled,
  CheckmarkCircle24Filled,
  Delete24Filled,
  Dismiss24Filled as DismissIcon,
  Document24Filled,
  Edit24Filled,
  ErrorCircle24Filled,
  Home24Filled,
  Info24Filled,
  Money24Filled,
  Save24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NDataTable,
  NDatePicker,
  NEmpty,
  NForm,
  NFormItem,
  NH1,
  NH3,
  NIcon,
  NInput,
  NInputNumber,
  NModal,
  NPopconfirm,
  NSelect,
  NSpace,
  NSpin,
  NTabPane,
  NTabs,
  NTooltip,
  useMessage,
} from 'naive-ui'
import { computed, h, nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Details propriete',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})
const route = useRoute()
const router = useRouter()
const message = useMessage()

// États
const loading = ref(true)
const saving = ref(false)
const editingInfos = ref(false)
const showDocumentModal = ref(false)
const uploading = ref(false)
const proprieteDetail = ref<any | null>(null)

// Récupérer l'ID de la propriété depuis les paramètres de route
const proprieteId = (route.params && (route.params as any).id) ? String((route.params as any).id) : ''

// Formulaires
const editForm = ref({
  id: '',
  nom: '',
  adresse: '',
  ville: '',
  codePostal: '',
  typeBien: '',
  montantAcquisition: 0,
  dateAcquisition: '',
  tantieme: 0,
  fraisNotaire: 0,
  fraisAgence: 0,
})

// Formulaire de document
const documentForm = ref({
  id: '',
  type: 'AUTRE',
  fichier: null as File | null,
  contenu: '',
  proprieteId,
  locataireId: '', // Laisser vide pour les documents de propriété
  titre: '',
  nomFichier: '',
  mimeType: '',
  taille: 0,
  dateDocument: new Date().toISOString().split('T')[0],
})

const fileInput = ref<HTMLInputElement | null>(null)

const documentTypes = [
  { value: 'BAIL', label: 'Bail de location' },
  { value: 'QUITTANCE', label: 'Quittance de loyer' },
  { value: 'FACTURE', label: 'Facture' },
  { value: 'JUSTIFICATIF', label: 'Justificatif (revenus, identité...)' },
  { value: 'CONTRAT_DE_CREDIT', label: 'Contrat de crédit' },
  { value: 'AUTRE', label: 'Autre document' },
]

// Ajouter la ref pour le champ nom
const nomInputRef = ref<HTMLInputElement | null>(null)

// Méthodes
async function supprimerPropriete(id: string) {
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deletePropriete/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include'
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      if (response.status === 400 && errorData.message) {
        // Afficher le message d'erreur spécifique du serveur
        message.error(errorData.message)
        if (errorData.locations) {
          // Afficher la liste des locations liées si disponible
          const locationsList = errorData.locations.join('\n')
          message.error(`Locations liées :\n${locationsList}`, { duration: 10000 })
        }
      } else {
        throw new Error(errorData.message || 'Erreur lors de la suppression')
      }
      return
    }

    message.success('Propriété supprimée avec succès')
    router.push('/propriete')
  } catch (error) {
    console.error('Erreur lors de la suppression :', error)
    message.error(error instanceof Error ? error.message : 'Erreur lors de la suppression de la propriété')
  }
}

async function fetchProprieteDetails() {
  try {
    loading.value = true
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprieteDetails/${proprieteId}`
    const response = await fetch(url, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    const responseText = await response.text()
    if (!response.ok) {
      console.error('Erreur de réponse. Contenu:', responseText)
      throw new Error(`Erreur HTTP: ${response.status}`)
    }
    // Essayer de parser le JSON
    try {
      const data = JSON.parse(responseText)
      proprieteDetail.value = data
    } catch (parseError) {
      console.error('Erreur lors de l\'analyse JSON:', parseError)
      throw new Error(`Réponse du serveur invalide (pas du JSON): ${responseText.substring(0, 200)}...`)
    }
  } catch (error) {
    console.error('Erreur lors du chargement de la propriété:', error instanceof Error ? error.message : String(error))
    message.error(`Erreur de chargement de la propriété : ${error instanceof Error ? error.message : String(error)}`)
  } finally {
    loading.value = false
  }
}

function startEditing(section?: string) {
  if (section === 'infos' && proprieteDetail.value?.propriete) {
    const propriete = proprieteDetail.value.propriete
    // S'assurer que les champs numériques sont correctement typés
    editForm.value = {
      ...propriete,
      montantAcquisition: Number(propriete.montantAcquisition) || 0,
      tantieme: Number(propriete.tantieme) || 0,
      fraisNotaire: Number(propriete.fraisNotaire) || 0,
      fraisAgence: Number(propriete.fraisAgence) || 0,
    }
    editingInfos.value = true
    nextTick(() => {
      // Scroll sur le signet
      document.getElementById('focus-nom')?.scrollIntoView({ behavior: 'smooth', block: 'center' })
      // Focus sur l'input si possible
      nomInputRef.value?.focus?.()
    })
  }
}

async function savePropriete() {
  try {
    saving.value = true
    const authStore = useAuthStore()
    const userId = authStore.userInfo?.userId || ''
    
    if (!userId) {
      throw new Error('Utilisateur non connecté')
    }

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePropriete/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(editForm.value),
    })

    if (!response.ok) {
      const error = await response.text()
      throw new Error(error || 'Erreur lors de la mise à jour')
    }

    const updatedPropriete = await response.json()

    // Mettre à jour les données locales
    if (proprieteDetail.value) {
      proprieteDetail.value.propriete = updatedPropriete
    }

    message.success('Modifications enregistrées avec succès')
    editingInfos.value = false
  }
  catch (error) {
    const errorMessage = error instanceof Error ? error.message : 'Erreur inconnue'
    message.error(errorMessage || 'Erreur lors de la mise à jour de la propriété')
    console.error('Erreur:', error)
  }
  finally {
    saving.value = false
  }
}

function cancelEditing() {
  editingInfos.value = false
}

// Utilitaires
function formatCurrency(value: string | number | null | undefined) {
  if (value === null || value === undefined)
    return '-'
  const num = typeof value === 'string' ? Number.parseFloat(value) : value
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(num)
}

function formatDate(dateString: string | null | undefined) {
  if (!dateString)
    return '-'
  return new Date(dateString).toLocaleDateString('fr-FR')
}

// Gestion des documents
function getDocumentIcon() {
  // Utilisez Document24Filled pour tous les types de documents
  return Document24Filled
}

function handleDrop(event: DragEvent) {
  const file = event.dataTransfer?.files[0]
  if (!file) {
    message.error('Aucun fichier déposé.')
    return
  }

  if (file.size > 10 * 1024 * 1024) { // 10 MB
    message.error('Le fichier est trop volumineux (max 10MB).')
    return
  }

  documentForm.value.fichier = file
  documentForm.value.nomFichier = file.name
  documentForm.value.mimeType = file.type
  documentForm.value.taille = file.size

  const reader = new FileReader()
  reader.onload = e => {
    documentForm.value.contenu = e.target?.result as string
  }
  reader.onerror = () => {
    message.error('Erreur lors de la lecture du fichier.')
  }
  reader.readAsDataURL(file)
}

function handleFileUpload(event: Event) {
  const target = event.target as HTMLInputElement
  if (!target.files || target.files.length === 0) {
    console.log('Aucun fichier sélectionné.')
    return
  }

  const file = target.files[0]
  console.log('Fichier sélectionné:', file)

  if (file.size > 10 * 1024 * 1024) { // 10 MB
    message.error('Le fichier est trop volumineux. La taille maximale est de 10 Mo.')
    target.value = ''
    return
  }

  documentForm.value.fichier = file
  documentForm.value.nomFichier = file.name
  documentForm.value.mimeType = file.type
  documentForm.value.taille = file.size
  documentForm.value.titre = file.name.split('.').slice(0, -1).join('.')

  const reader = new FileReader()
  reader.onload = (e) => {
    const result = e.target?.result as string
    documentForm.value.contenu = result.split(',')[1]
  }
  reader.onerror = (error) => {
    console.error('Erreur de lecture du fichier:', error)
    message.error('Erreur lors de la lecture du fichier.')
  }
  reader.readAsDataURL(file)
}

async function ajouterDocument() {
  if (!documentForm.value.fichier || !documentForm.value.contenu) {
    message.error('Veuillez sélectionner un fichier.')
    return
  }

  uploading.value = true
  try {
    const authStore = useAuthStore()
    const token = authStore.token || localStorage.getItem('token')
    if (!token) {
      throw new Error('Utilisateur non authentifié')
    }

    const documentData = {
      contenu: documentForm.value.contenu,
      mimeType: documentForm.value.mimeType,
      nomFichier: documentForm.value.nomFichier,
      taille: documentForm.value.taille,
      utilisateurId: authStore.userInfo?.userId,
      proprieteId: documentForm.value.proprieteId,
      locataireId: documentForm.value.locataireId,
      typeDocument: documentForm.value.type,
      titre: documentForm.value.nomFichier, // Utiliser le nom du fichier comme titre
      dateDocument: new Date().toISOString().split('T')[0] // Utiliser la date du jour
    }

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/uploadDocument`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(documentData)
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Erreur ${response.status}: ${errorText}`)
    }

    message.success('Document téléversé avec succès !')
    showDocumentModal.value = false
    resetDocumentForm()
    await fetchProprieteDetails() // Recharger les documents
  } catch (error) {
    console.error('Erreur lors du téléversement du document:', error)
    message.error(error instanceof Error ? error.message : 'Une erreur est survenue.')
  } finally {
    uploading.value = false
  }
}

function resetDocumentForm() {
  documentForm.value = {
    id: '',
    type: 'AUTRE',
    fichier: null,
    contenu: '',
    proprieteId,
    locataireId: '',
    titre: '',
    nomFichier: '',
    mimeType: '',
    taille: 0,
    dateDocument: new Date().toISOString().split('T')[0]
  }
  // Reset file input if possible
  const fileInput = document.getElementById('file-upload') as HTMLInputElement
  if (fileInput) {
    fileInput.value = ''
  }
}

async function supprimerDocument(id: string) {
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/documents/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('Erreur lors de la suppression du document')
    }

    message.success('Document supprimé avec succès')
    fetchProprieteDetails()
  } catch (error) {
    console.error('Erreur lors de la suppression du document:', error)
    message.error('Erreur lors de la suppression du document: ' + (error instanceof Error ? error.message : String(error)))
  }
}

const telechargerDocument = async (doc: any) => {
  if (typeof window === 'undefined') return;
  
  let a: HTMLAnchorElement | null = null;
  
  try {
    const authStore = useAuthStore()
    const token = authStore.token || localStorage.getItem('token') || ''
    
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL || ''}/api/documents/${doc.id}/content`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Erreur ${response.status}: ${errorText || 'Erreur inconnue'}`)
    }

    // Récupérer le contenu en base64
    const base64Content = await response.text()
    
    // Récupérer le nom de fichier depuis les en-têtes ou utiliser un nom par défaut
    const contentDisposition = response.headers.get('Content-Disposition')
    const filename = contentDisposition 
      ? contentDisposition.split('filename=')[1].replace(/"/g, '')
      : doc.nomFichier || `document_${doc.id}.bin`
    
    // Créer un élément <a> invisible
    a = document.createElement('a')
    a.style.display = 'none'
    a.href = `data:${doc.mimeType || 'application/octet-stream'};base64,${base64Content}`
    a.download = filename
    
    // Ajouter l'élément au DOM, déclencher le clic
    document.body.appendChild(a)
    a.click()
    message.success('Téléchargement démarré')
  } catch (error) {
    console.error('Erreur lors du téléchargement:', error)
    message.error('Erreur lors du téléchargement du document: ' + (error instanceof Error ? error.message : String(error)))
  } finally {
    // Nettoyer après un court délai
    setTimeout(() => {
      if (a && document.body.contains(a)) {
        document.body.removeChild(a)
      }
    }, 1000)
  }
};

function nouveauDocument() {
  documentForm.value = {
    id: '',
    type: 'AUTRE',
    fichier: null,
    contenu: '',
    proprieteId,
    locataireId: '',
    titre: '',
    nomFichier: '',
    mimeType: '',
    taille: 0,
    dateDocument: new Date().toISOString().split('T')[0],
  }
  showDocumentModal.value = true
}

// Cycle de vie
onMounted(() => {
  fetchProprieteDetails()
})

function definePage(arg0: { meta: { title: string; hideInMenu: boolean; activeMenu: string } }) {
  throw new Error('Function not implemented.')
}

// Suppression de toute la logique d'amortissement et de centrage d'onglet (plus d'onglet composant)
</script>

<template>
  <div class="propriete-detail">
    <div class="page-header">
      <NButton text @click="$router.push('/propriete')" class="back-button" title="Retour">
        <NIcon :component="ArrowLeft24Filled" size="20" />
      </NButton>
      <NH1 class="titre-principal">Détails de la propriété</NH1>
    </div>

    <NSpin :show="loading">
      <div ref="tabsWrapperRef" class="tabs-scrollable">
        <NTabs v-if="proprieteDetail" v-model:value="activeTab" type="line" animated>
          <!-- Onglet Informations -->
          <NTabPane name="infos" :tab="() => h('span', [h(NIcon, { component: Info24Filled, size: 20, class: 'mr-1' }), ' Informations'])" title="Informations">
            <div v-if="!editingInfos" class="action-buttons">
              <NButton type="primary" @click="startEditing('infos')" class="action-button" ghost title="Modifier">
                <template #icon>
                  <NIcon :component="Edit24Filled" />
                </template>
              </NButton>
              <NPopconfirm
                @positive-click="() => proprieteDetail.propriete && proprieteDetail.propriete.id && supprimerPropriete(proprieteDetail.propriete.id)"
              >
                <template #trigger>
                  <NButton type="error" ghost class="action-button" title="Supprimer">
                    <template #icon>
                      <NIcon :component="Delete24Filled" />
                    </template>
                  </NButton>
                </template>
                Êtes-vous sûr de vouloir supprimer cette propriété ?
              </NPopconfirm>
            </div>
            <div v-else class="action-buttons">
              <NButton type="primary" :loading="saving" @click="savePropriete" class="action-button" title="Enregistrer">
                <template #icon>
                  <NIcon :component="Save24Filled" />
                </template>
              </NButton>
              <NButton class="action-button ml-2" @click="cancelEditing" title="Annuler">
                <template #icon>
                  <NIcon :component="DismissIcon" />
                </template>
              </NButton>
            </div>

            <NCard class="propriete-info-card" :bordered="false">
              <div class="propriete-info-grid">
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Home24Filled" class="mr-1" />Nom :</span>
                  <span class="propriete-info-value">{{ proprieteDetail.propriete.nom }}</span>
                </div>
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Document24Filled" class="mr-1" />Type :</span>
                  <span class="propriete-info-value">{{ proprieteDetail.propriete.typeBien }}</span>
                </div>
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Document24Filled" class="mr-1" />Adresse :</span>
                  <span class="propriete-info-value">{{ proprieteDetail.propriete.adresse }}</span>
                </div>
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Document24Filled" class="mr-1" />Ville :</span>
                  <span class="propriete-info-value">{{ proprieteDetail.propriete.ville }}</span>
                </div>
                <div id="focus-nom"></div>
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Document24Filled" class="mr-1" />Montant acquisition :</span>
                  <span class="propriete-info-value">{{ formatCurrency(proprieteDetail.propriete.montantAcquisition) }}</span>
                </div>
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Document24Filled" class="mr-1" />Date acquisition :</span>
                  <span class="propriete-info-value">{{ formatDate(proprieteDetail.propriete.dateAcquisition) }}</span>
                </div>
                <div class="propriete-info-item">
                  <span class="propriete-info-label"><NIcon :component="Document24Filled" class="mr-1" />Tantième :</span>
                  <span class="propriete-info-value">{{ proprieteDetail.propriete.tantieme }}</span>
                </div>
              </div>
            </NCard>

            <NForm v-if="editingInfos" class="edit-form">
              <div class="info-grid">
                <div class="info-label">
                  Nom :
                </div>
                <div class="info-value-edit">
                  <NInput v-model:value="editForm.nom" size="large" style="width: 100%" ref="nomInputRef" />
                </div>

                <div class="info-label">
                  Adresse :
                </div>
                <div class="info-value-edit">
                  <NInput v-model:value="editForm.adresse" size="large" style="width: 100%" />
                </div>

                <div class="info-label">
                  Code postal :
                </div>
                <div class="info-value-edit">
                  <NInput v-model:value="editForm.codePostal" size="large" style="width: 100%" />
                </div>

                <div class="info-label">
                  Ville :
                </div>
                <div class="info-value-edit">
                  <NInput v-model:value="editForm.ville" size="large" style="width: 100%" />
                </div>

                <div class="info-label">
                  Type :
                </div>
                <div class="info-value-edit">
                  <NSelect
                    v-model:value="editForm.typeBien"
                    :options="[
                      { label: 'Appartement', value: 'APPARTEMENT' },
                      { label: 'Maison', value: 'MAISON' },
                      { label: 'Local commercial', value: 'LOCAL_COMMERCIAL' },
                    ]"
                    size="large"
                    style="width: 100%"
                  />
                </div>

                <div class="info-label">
                  Montant acquisition :
                </div>
                <div class="info-value-edit">
                  <NInputNumber v-model:value="editForm.montantAcquisition" size="large" style="width: 100%" />
                </div>

                <div class="info-label">
                  Date acquisition :
                </div>
                <div class="info-value-edit">
                  <NDatePicker v-model:formatted-value="editForm.dateAcquisition" value-format="yyyy-MM-dd" size="large" style="width: 100%" />
                </div>

                <div class="info-label">
                  Tantième :
                </div>
                <div class="info-value-edit">
                  <NInputNumber v-model:value="editForm.tantieme" size="large" style="width: 100%" />
                </div>
              </div>
            </NForm>
          </NTabPane>

          <!-- Onglet Documents -->
          <NTabPane name="documents" :tab="() => h('span', [h(NIcon, { component: Document24Filled, size: 20, class: 'mr-1' }), ' Documents'])" title="Documents">
            <div class="action-buttons">
              <NButton type="primary" @click="nouveauDocument" class="action-button" title="Ajouter un document">
                <template #icon>
                  <NIcon :component="Add24Filled" />
                </template>
              </NButton>
            </div>

            <NH3 class="sous-titre mb-4">Documents de la propriété</NH3>
            <div class="document-cards">
              <NCard
                v-for="doc in proprieteDetail?.documents || []"
                :key="doc.id"
                class="document-card"
                :bordered="true"
                size="medium"
              >
                <div class="flex justify-between items-center mb-2">
                  <div class="flex items-center gap-2">
                    <NIcon :component="getDocumentIcon()" size="22" />
                    <span class="font-bold">{{ doc.titre || doc.nomFichier || 'Sans nom' }}</span>
                  </div>
                  <div class="flex gap-2">
                    <NButton size="small" text @click="telechargerDocument(doc)" title="Télécharger">
                      <NIcon :component="Document24Filled" />
                    </NButton>
                    <NPopconfirm @positive-click="() => supprimerDocument(doc.id)">
                      <template #trigger>
                        <NButton size="small" text type="error" title="Supprimer">
                          <NIcon :component="Delete24Filled" />
                        </NButton>
                      </template>
                      Êtes-vous sûr de vouloir supprimer ce document ?
                    </NPopconfirm>
                  </div>
                </div>
                <div class="mb-1"><span class="label">Type :</span> {{ (documentTypes.find(t => t.value === doc.typeDocument)?.label) || doc.typeDocument || 'Non spécifié' }}</div>
                <div class="mb-1"><span class="label">Date :</span> {{ formatDate(doc.dateDocument) }}</div>
              </NCard>
              <NEmpty v-if="!proprieteDetail?.documents || proprieteDetail.documents.length === 0" description="Aucun document pour le moment" />
            </div>
          </NTabPane>
        </NTabs>
      </div>

      <!-- Modal d'ajout de document -->
      <NModal v-model:show="showDocumentModal" :mask-closable="false">
        <NCard
          style="width: 600px; max-width: 90vw;"
          title="Ajouter un document"
          :bordered="false"
          size="huge"
          role="dialog"
          aria-modal="true"
        >
          <NForm @submit.prevent="ajouterDocument">
            <NFormItem label="Type de document" required>
              <NSelect
                v-model:value="documentForm.type"
                :options="documentTypes"
                placeholder="Sélectionnez un type"
              />
            </NFormItem>

            <NFormItem label="Fichier" required>
              <div class="w-full">
                <div 
                  class="border-2 border-dashed rounded-lg p-6 text-center cursor-pointer hover:bg-gray-50 transition-colors"
                  @dragover.prevent
                  @drop.prevent="handleDrop"
                  @click="() => fileInput?.click()"
                >
                  <input 
                    type="file" 
                    ref="fileInput"
                    class="hidden" 
                    accept=".pdf,.doc,.docx,.jpg,.jpeg,.png,image/*"
                    @change="handleFileUpload"
                  >
                  <div class="flex flex-col items-center">
                    <NIcon size="48" :depth="3" class="mb-3">
                      <Document24Filled />
                    </NIcon>
                    <NText style="font-size: 16px" class="block mb-1">
                      Cliquez pour sélectionner un fichier
                    </NText>
                    <NText depth="3" class="block mb-3">
                      ou glissez-déposez un fichier ici
                    </NText>
                    <NButton type="primary" ghost @click.stop="() => fileInput?.click()">
                      Sélectionner un fichier
                    </NButton>
                    <NText depth="3" class="block mt-3 text-xs">
                      Formats supportés: PDF, DOC, DOCX, JPG, PNG (max 10MB)
                    </NText>
                  </div>
                </div>
                
                <div v-if="documentForm.fichier" class="mt-4 p-3 border rounded">
                  <div class="flex justify-between items-center">
                    <div class="flex items-center">
                      <NIcon :component="getDocumentIcon()" size="24" class="mr-2" />
                      <span>{{ documentForm.fichier.name }}</span>
                    </div>
                    <NButton text @click="() => { documentForm.fichier = null; documentForm.contenu = ''; if (fileInput) fileInput.value = ''; }">
                      <NIcon :component="DismissIcon" />
                    </NButton>
                  </div>
                </div>
              </div>
            </NFormItem>
          </NForm>
          <template #footer>
            <div class="flex justify-end gap-2">
              <NButton @click="showDocumentModal = false">Annuler</NButton>
              <NButton 
                type="primary" 
                :loading="uploading"
                :disabled="!documentForm.fichier"
                @click="ajouterDocument"
              >
                Téléverser
              </NButton>
            </div>
          </template>
        </NCard>
      </NModal>
    </NSpin>
  </div>
</template>

<style scoped>
.titre-principal, .sous-titre, h1, h2, h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.sous-titre {
  font-size: 1.1rem;
  font-weight: 600;
}
.propriete-detail {
  padding: 20px;
  width: 100%;
  margin: 0;
}
.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}
.back-button {
  margin-right: 12px;
  color: var(--n-text-color);
  transition: color 0.2s;
}
.back-button:hover {
  color: var(--n-text-color-hover);
}
.action-buttons {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}
.action-buttons .n-button {
  flex: 0 0 auto;
  width: auto;
  min-width: 0;
  max-width: none;
}
.table-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}
.action-icon {
  font-size: 18px;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
  color: var(--n-text-color);
}
.action-icon:hover {
  background-color: var(--n-color-hover);
}
.action-icon.error {
  color: var(--n-color-error);
}
.action-icon.error:hover {
  background-color: var(--n-color-error-hover);
  color: white;
}
.info-label {
  font-weight: bold;
  padding: 12px;
  background-color: var(--n-color-embedded);
  border-radius: 4px;
}
.info-value-edit {
  padding: 0;
  border: none;
  background: none;
  width: 100%;
  max-width: 100%;
  display: flex;
  align-items: center;
}
.info-value-edit > * {
  flex: 1 1 0%;
  min-width: 0;
}
.edit-form {
  width: 100%;
  max-width: 100%;
  margin-top: 20px;
}
.n-tabs {
  width: auto;
  min-width: 0;
  max-width: 100%;
}
.n-tab-pane {
  padding: 0;
  width: 100%;
}
.n-form {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  width: 100%;
}
.n-form-item {
  margin-bottom: 0;
}
.composition-cards,
.document-cards,
.amortissement-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
.composition-card,
.document-card,
.amortissement-card {
  min-width: 0;
  width: 100%;
  box-sizing: border-box;
}
.label {
  font-weight: 600;
  color: var(--n-text-color);
  margin-right: 4px;
}
.n-tabs-tab {
  min-width: unset;
  max-width: unset;
  justify-content: flex-start;
  text-align: left;
  padding: 0 16px !important;
  position: relative;
}
.n-tabs-tab .n-icon {
  margin: 0 8px 0 0;
  display: inline-block;
}
.tab-label-hover {
  display: inline;
  margin-left: 0;
  vertical-align: middle;
  font-size: 15px;
  font-weight: 500;
  position: static;
  background: none;
  color: inherit;
  padding: 0;
  border-radius: 0;
  white-space: normal;
  box-shadow: none;
}
.tabs-scrollable {
  overflow-x: auto;
  scrollbar-width: thin;
  -webkit-overflow-scrolling: touch;
  margin-bottom: 16px;
  width: auto;
  max-width: 100%;
}
.propriete-info-card {
  margin-bottom: 24px;
  background: var(--n-color-embedded);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.propriete-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px 32px;
}
.propriete-info-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 0;
}
.propriete-info-label {
  color: var(--n-text-color-disabled);
  font-size: 0.95rem;
  margin-bottom: 2px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 600;
}
.propriete-info-value {
  color: var(--n-text-color);
  font-size: 1.15rem;
  font-weight: 500;
  word-break: break-word;
  line-height: 1.4;
}

/* --- Responsive --- */
@media (max-width: 768px) {
  .propriete-detail {
    padding: 8px;
  }
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    margin-bottom: 16px;
  }
  .titre-principal {
    font-size: 1.3rem !important;
    text-align: center;
    width: 100%;
    margin: 0.5rem 0 1rem 0;
  }
  .back-button {
    margin-bottom: 8px;
    margin-right: 0;
    font-size: 1.2rem;
  }
  .n-tabs {
    font-size: 0.98rem;
    padding: 0 2px;
    overflow-x: auto !important;
    white-space: nowrap;
    min-width: 0;
    max-width: 100vw;
  }
  .tabs-scrollable {
    margin-bottom: 8px;
    padding-bottom: 2px;
    overflow-x: auto !important;
    -webkit-overflow-scrolling: touch;
  }
  .n-tabs-tab {
    padding: 0 8px !important;
    font-size: 0.98rem;
  }
  .action-buttons {
    flex-direction: column;
    gap: 10px;
    margin-bottom: 1rem;
  }
  .action-buttons .n-button {
    width: 100%;
    max-width: 100%;
  }
  .propriete-info-card {
    margin-bottom: 16px;
    padding: 12px 8px;
  }
  .propriete-info-grid {
    grid-template-columns: 1fr !important;
    gap: 22px 0 !important;
  }
  .propriete-info-item {
    margin-bottom: 0;
    padding-bottom: 8px;
    border-bottom: 1px solid #ececec;
  }
  .propriete-info-item:last-child {
    border-bottom: none;
  }
  .propriete-info-label {
    font-size: 1.13rem;
    margin-bottom: 2px;
    font-weight: 700;
  }
  .propriete-info-value {
    font-size: 1.15rem;
    font-weight: 600;
    word-break: break-word;
    line-height: 1.5;
    margin-bottom: 0.2rem;
  }
  .composition-cards,
  .document-cards,
  .amortissement-cards {
    grid-template-columns: 1fr !important;
    gap: 10px;
    margin-bottom: 12px;
  }
  .composition-card,
  .document-card,
  .amortissement-card {
    padding: 10px 6px;
    font-size: 1rem;
  }
  .label {
    font-size: 1.05rem;
  }
  .n-form {
    grid-template-columns: 1fr !important;
    gap: 12px;
  }
  .n-form-item {
    margin-bottom: 0;
  }
}
</style>
