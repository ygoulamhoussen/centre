<script setup lang="ts">
import { onMounted, ref, computed, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'
import { 
  NButton, 
  NCard, 
  NEmpty, 
  NSpace, 
  useMessage, 
  NForm, 
  NFormItem, 
  NInput, 
  NDatePicker, 
  NSelect,
  NPopconfirm,
  NIcon,
  NUpload,
  NUploadDragger,
  NText,
  NDataTable,
  NModal,
  NH1,
  NTabs,
  NTabPane,
  NTab,
  NH3,
} from 'naive-ui'
import { 
  Save24Filled, 
  Dismiss24Filled, 
  Edit24Filled, 
  Delete24Filled,
  Add24Filled, 
  Document24Filled,
  DocumentPdf24Filled,
  Image24Filled,
  Document24Regular,
  ArrowDownload24Filled,
  ArrowLeft24Filled,
  Info24Filled,
} from '@vicons/fluent'

// Fonction pour obtenir l'icône appropriée en fonction du type de fichier
function getDocumentIcon(filename: string) {
  if (!filename) return Document24Filled
  
  const ext = filename.split('.').pop()?.toLowerCase()
  
  if (ext === 'pdf') return DocumentPdf24Filled
  if (['doc', 'docx', 'xls', 'xlsx'].includes(ext || '')) return Document24Filled
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp'].includes(ext || '')) return Image24Filled
  
  return Document24Filled
}

definePage({
  meta: {
    title: 'Détails locataire',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

// Initialisation des références réactives
const route = useRoute()
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()
const loading = ref(true)
const saving = ref(false)
const editing = ref(false)
const locataire = ref<any>(null)
const formData = ref({
  id: '',
  nom: '',
  prenom: '',
  email: '',
  telephone: '',
  dateNaissance: null as number | null,
  adresse: '',
  codePostal: '',
  ville: '',
  pays: ''
})

// Options pour les listes déroulantes
const paysOptions = [
  { label: 'France', value: 'France' },
  { label: 'Belgique', value: 'Belgique' },
  { label: 'Suisse', value: 'Suisse' },
  { label: 'Luxembourg', value: 'Luxembourg' },
  { label: 'Canada', value: 'Canada' }
]

// Récupérer l'ID du locataire depuis les paramètres de route
console.log('Paramètres de route:', route.params)
const locataireId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
console.log('ID du locataire extrait:', locataireId)

// Fonctions de gestion d'édition
function startEditing() {
  editing.value = true
}

// Charger les données du locataire
async function loadLocataire() {
  try {
    loading.value = true
    const token = localStorage.getItem('token')
    console.log('Token JWT:', token ? 'Présent' : 'Manquant')
    
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getLocataireDetails/${locataireId}`
    console.log('URL de l\'API:', url)
    
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      credentials: 'include'
    })
    
    console.log('Statut de la réponse:', response.status, response.statusText)
    
    if (!response.ok) {
      const errorText = await response.text()
      console.error('Erreur de l\'API:', errorText)
      throw new Error(`Erreur ${response.status}: ${response.statusText} - ${errorText}`)
    }
    
    const data = await response.json()
    console.log('Données reçues de l\'API:', JSON.parse(JSON.stringify(data)))
    locataire.value = data
    // Copier les données dans le formulaire
    formData.value = {
      id: data.id || '',
      nom: data.nom || '',
      prenom: data.prenom || '',
      email: data.email || '',
      telephone: data.telephone || '',
      dateNaissance: data.dateNaissance ? new Date(data.dateNaissance).getTime() : null,
      adresse: data.adresse || '',
      codePostal: data.codePostal || '',
      ville: data.ville || '',
      pays: data.pays || ''
    }
  } catch (error) {
    console.error('Erreur lors du chargement du locataire:', error)
    message.error('Impossible de charger les détails du locataire')
  } finally {
    loading.value = false
  }
}

// Annuler l'édition
function cancelEditing() {
  editing.value = false
  // Restaurer les données originales
  if (locataire.value) {
    formData.value = {
      id: locataire.value.id || '',
      nom: locataire.value.nom || '',
      prenom: locataire.value.prenom || '',
      email: locataire.value.email || '',
      telephone: locataire.value.telephone || '',
      dateNaissance: locataire.value.dateNaissance ? new Date(locataire.value.dateNaissance).getTime() : null,
      adresse: locataire.value.adresse || '',
      codePostal: locataire.value.codePostal || '',
      ville: locataire.value.ville || '',
      pays: locataire.value.pays || ''
    }
  }
}

// Enregistrer les modifications
async function saveLocataire() {
  try {
    saving.value = true
    const method = 'PUT' // Toujours utiliser PUT pour la mise à jour
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/updateLocataire`
    
    // Vérifier les informations utilisateur
    console.log('Utilisateur connecté:', authStore.userInfo)
    console.log('Token:', authStore.token)
    
    if (!authStore.userInfo.userId) {
      console.log('Chargement des informations utilisateur...')
      await authStore.initUserInfo()
      console.log('Utilisateur après chargement:', authStore.userInfo)
    }
    
    console.log('Envoi de la requête à:', url)
    console.log('Méthode:', method)
    console.log('Données du formulaire:', formData.value)
    
    const requestBody = {
      ...formData.value,
      utilisateurId: authStore.userInfo.userId
    }
    
    console.log('Corps de la requête:', requestBody)
    
    const response = await fetch(url, {
      method,
      headers: { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })
    
    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Erreur ${response.status}: ${response.statusText} - ${errorText}`)
    }
    
    const responseData = await response.json()
    locataire.value = responseData
    editing.value = false
    message.success('Locataire enregistré avec succès')
    
    // Recharger les données pour s'assurer qu'elles sont à jour
    await loadLocataire()
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
    message.error(error instanceof Error ? error.message : 'Erreur lors de la sauvegarde du locataire')
  } finally {
    saving.value = false
  }
}

// Types de documents disponibles
const documentTypes = [
  { value: 'BAIL', label: 'Bail de location' },
  { value: 'QUITTANCE', label: 'Quittance de loyer' },
  { value: 'FACTURE', label: 'Facture' },
  { value: 'JUSTIFICATIF', label: 'Justificatif (revenus, identité...)' },
  { value: 'CONTRAT_DE_CREDIT', label: 'Contrat de crédit' },
  { value: 'AUTRE', label: 'Autre document' },
]

// Gestion des documents
const showDocumentModal = ref(false)
const documentForm = ref({
  id: '',
  type: 'AUTRE',
  fichier: null,
  contenu: '',
  proprietaireId: '',
  locataireId: locataireId,
  titre: '',
  nomFichier: '',
  dateDocument: new Date().toISOString().split('T')[0]
})

// Référence vers l'input file
const fileInput = ref<HTMLInputElement | null>(null)

// Gestion du drop de fichier
function handleDrop(event: DragEvent) {
  if (!event.dataTransfer) return
  
  const files = event.dataTransfer.files
  if (files.length === 0) return
  
  // Créer un événement de changement de fichier artificiel
  const input = document.createElement('input')
  input.type = 'file'
  input.files = files
  
  // Déclencher le gestionnaire de fichier
  const changeEvent = new Event('change', { bubbles: true })
  Object.defineProperty(changeEvent, 'target', { value: { files } })
  handleFileUpload(changeEvent as unknown as Event)
}

const documentColumns = [
  {
    title: 'Type',
    key: 'type',
    render: (row: any) => h('div', { class: 'flex items-center' }, [
      h(NIcon, { component: getDocumentIcon(row.nomFichier || ''), size: 20, class: 'mr-2' }),
      h('span', row.typeDocument || 'Inconnu')
    ])
  },
  { 
    title: 'Nom',
    key: 'titre',
    render: (row: any) => row.titre || row.nomFichier || 'Sans nom'
  },
  {
    title: 'Date',
    key: 'dateDocument',
    render: (row: any) => formatDate(row.dateDocument || row.dateCreation || '')
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 100,
    render: (row: any) => h('div', { class: 'flex space-x-2' }, [
      h(NButton, {
        size: 'small',
        text: true,
        onClick: () => downloadDocument(row),
        title: 'Télécharger',
        class: 'action-icon',
        style: { marginRight: '8px' }
      }, () => h(NIcon, { component: ArrowDownload24Filled, size: 18 })),
      h(NPopconfirm, {
        onPositiveClick: () => deleteDocument(row.id)
      }, {
        trigger: () => h(NButton, {
          size: 'small',
          text: true,
          title: 'Supprimer',
          class: 'action-icon error',
        }, () => h(NIcon, { component: Delete24Filled, size: 18 })),
        default: 'Êtes-vous sûr de vouloir supprimer ce document ?'
      })
    ])
  }
]

function handleFileUpload(event: Event) {
  console.log('handleFileUpload triggered');
  const input = event.target as HTMLInputElement
  if (!input.files || input.files.length === 0) {
    console.log('No files selected');
    return
  }
  
  const file = input.files[0]
  console.log('File selected:', file.name);
  
  // Vérifier la taille du fichier (max 10MB)
  if (file.size > 10 * 1024 * 1024) {
    message.error('Le fichier est trop volumineux. La taille maximale est de 10MB.')
    input.value = ''
    return
  }
  
  const reader = new FileReader()
  reader.onload = () => {
    console.log('FileReader onload');
    const result = reader.result as string
    if (result.startsWith('data:')) {
      // Mettre à jour le formulaire avec les informations du fichier
      documentForm.value = {
        ...documentForm.value,
        fichier: file,
        contenu: result.split(',')[1],
        nomFichier: file.name,
        titre: file.name
      }
      console.log('documentForm updated:', documentForm.value);
    } else {
      console.error('Format inattendu pour le fichier')
      message.error('Format de fichier non supporté')
      input.value = ''
    }
  }
  reader.onerror = () => {
    console.error('Erreur lors de la lecture du fichier')
    message.error('Erreur lors de la lecture du fichier')
    input.value = ''
  }
  
  reader.readAsDataURL(file)
}

async function uploadDocument() {
  if (!documentForm.value.fichier || !documentForm.value.contenu) {
    message.error('Veuillez sélectionner un fichier valide')
    return
  }
  
  try {
    saving.value = true
    
    const token = localStorage.getItem('token')
    const userId = authStore.userInfo?.userId || ''
    
    if (!userId) {
      throw new Error('Utilisateur non connecté')
    }
    
        // S'assurer que la date est au format YYYY-MM-DD
    const dateDoc = documentForm.value.dateDocument
      ? new Date(documentForm.value.dateDocument).toISOString().split('T')[0]
      : new Date().toISOString().split('T')[0]

    const documentData = {
      contenu: documentForm.value.contenu,
      mimeType: documentForm.value.mimeType,
      nomFichier: documentForm.value.nomFichier,
      taille: documentForm.value.taille,
      utilisateurId: userId,
      locataireId: locataireId,
      typeDocument: documentForm.value.type,
      titre: documentForm.value.nomFichier, // Utiliser le nom du fichier comme titre
      dateDocument: new Date().toISOString().split('T')[0] // Utiliser la date du jour
    }
    
    console.log('Données du document à envoyer:', documentData)
    
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/uploadDocument`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      },
      body: JSON.stringify(documentData),
      credentials: 'include'
    })
    
    if (!response.ok) {
      let errorMessage = `Erreur ${response.status} lors de l'upload`
      try {
        const errorData = await response.json()
        errorMessage = errorData.message || errorMessage
      } catch (e) {
        const errorText = await response.text()
        errorMessage = errorText || errorMessage
      }
      throw new Error(errorMessage)
    }
    
    const result = await response.json()
    message.success('Document téléversé avec succès')
    showDocumentModal.value = false
    resetDocumentForm()
    await loadLocataire()
    return result
  } catch (error) {
    console.error('Erreur lors de l\'upload:', error)
    message.error(error instanceof Error ? error.message : 'Erreur lors du téléversement du document')
    throw error
  } finally {
    saving.value = false
  }
}

async function deleteDocument(id: string) {
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
    await loadLocataire()
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    message.error('Erreur lors de la suppression du document: ' + (error instanceof Error ? error.message : String(error)))
  }
}

async function downloadDocument(doc: any) {
  if (typeof window === 'undefined') return;
  
  let a: HTMLAnchorElement | null = null;
  
  try {
    const token = localStorage.getItem('token') || ''
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
    console.error('Erreur lors du téléchargement du document:', error)
    message.error('Erreur lors du téléchargement du document: ' + (error instanceof Error ? error.message : String(error)))
  } finally {
    // Nettoyer après un court délai
    setTimeout(() => {
      if (a && document.body.contains(a)) {
        document.body.removeChild(a)
      }
    }, 1000)
  }
}

// Réinitialiser le formulaire d'ajout de document
function resetDocumentForm() {
  documentForm.value = {
    id: '',
    type: 'AUTRE',
    fichier: null,
    contenu: '',
    proprietaireId: '',
    locataireId: locataireId,
    titre: '',
    nomFichier: '',
    dateDocument: new Date().toISOString().split('T')[0]
  }
}

async function confirmDelete() {
  window.$dialog.warning({
    title: 'Confirmer la suppression',
    content: 'Êtes-vous sûr de vouloir supprimer ce locataire ? Cette action est irréversible.',
    positiveText: 'Supprimer',
    negativeText: 'Annuler',
    onPositiveClick: async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteLocataire/${locataireId}`, {
          method: 'DELETE'
        })
        
        if (!response.ok) {
          const errorData = await response.json()
          throw new Error(errorData.message || 'Erreur lors de la suppression du locataire')
        }
        
        message.success('Locataire supprimé avec succès')
        router.push('/locataire')
      } catch (error) {
        console.error('Erreur lors de la suppression:', error)
        
        if (error.message.includes('contrainte de clé étrangère') || error.message.includes('location_locataire_id_fkey')) {
          message.error('Impossible de supprimer ce locataire car il est associé à une ou plusieurs locations.')
        } else {
          message.error(error.message || 'Erreur lors de la suppression du locataire')
        }
      }
    }
  })
}

// Charger les données au montage du composant
onMounted(() => {
  loadLocataire()
})

// Fonction pour formater la date
function formatDate(dateString: string) {
  if (!dateString) return 'Non spécifié'
  return new Date(dateString).toLocaleDateString('fr-FR')
}
</script>

<template>
  <div class="p-4">
    <div class="page-header">
      <NButton text @click="router.push('/locataire')" class="back-button">
        <NIcon :component="ArrowLeft24Filled" size="20" />
      </NButton>
      <NH1 class="titre-principal">Détails du locataire</NH1>
    </div>

    <NSpin :show="loading">
      <NCard v-if="!locataire && !loading">
        <NEmpty description="Locataire non trouvé" />
      </NCard>

      <template v-else-if="locataire">
        <NTabs type="line" animated>
          <!-- Onglet Informations -->
          <NTabPane name="informations" :tab="[
            h(NIcon, { component: Info24Filled, size: 20 }),
            ' Informations'
          ]">
            <div v-if="!editing" class="action-buttons mb-4">
              <NButton type="primary" @click="startEditing" class="action-button" ghost title="Modifier">
                <template #icon>
                  <NIcon :component="Edit24Filled" />
                </template>
              </NButton>
              <NPopconfirm
                @positive-click="confirmDelete"
              >
                <template #trigger>
                  <NButton type="error" ghost class="action-button" title="Supprimer">
                    <template #icon>
                      <NIcon :component="Delete24Filled" />
                    </template>
                  </NButton>
                </template>
                Êtes-vous sûr de vouloir supprimer ce locataire ?
              </NPopconfirm>
            </div>
            <div v-else class="action-buttons mb-4">
              <NButton type="primary" :loading="saving" @click="saveLocataire" class="action-button" title="Enregistrer">
                <template #icon>
                  <NIcon :component="Save24Filled" />
                </template>
              </NButton>
              <NButton class="action-button ml-2" @click="cancelEditing" title="Annuler">
                <template #icon>
                  <NIcon :component="Dismiss24Filled" />
                </template>
              </NButton>
            </div>
            <NCard :bordered="false">
              <NForm v-if="editing">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <NFormItem label="Nom" required>
                    <NInput v-model:value="formData.nom" placeholder="Nom" />
                  </NFormItem>
                  <NFormItem label="Prénom" required>
                    <NInput v-model:value="formData.prenom" placeholder="Prénom" />
                  </NFormItem>
                  <NFormItem label="Email">
                    <NInput v-model:value="formData.email" type="email" placeholder="email@exemple.com" />
                  </NFormItem>
                  <NFormItem label="Téléphone">
                    <NInput v-model:value="formData.telephone" placeholder="+33 6 12 34 56 78" />
                  </NFormItem>
                  <NFormItem label="Date de naissance">
                    <NDatePicker v-model:formatted-value="formData.dateNaissance" value-format="yyyy-MM-dd" />
                  </NFormItem>
                  <NFormItem label="Adresse">
                    <NInput v-model:value="formData.adresse" placeholder="Adresse" />
                  </NFormItem>
                  <NFormItem label="Code postal">
                    <NInput v-model:value="formData.codePostal" placeholder="Code postal" />
                  </NFormItem>
                  <NFormItem label="Ville">
                    <NInput v-model:value="formData.ville" placeholder="Ville" />
                  </NFormItem>
                  <NFormItem label="Pays">
                    <NSelect v-model:value="formData.pays" :options="paysOptions" placeholder="Sélectionner un pays" />
                  </NFormItem>
                </div>
              </NForm>
              <div v-else class="space-y-6">
                <div>
                  <NH3 class="sous-titre mb-4">Informations personnelles</NH3>
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <div class="text-sm text-gray-500">Nom</div>
                      <div class="text-base">{{ locataire.nom || 'Non spécifié' }}</div>
                    </div>
                    <div>
                      <div class="text-sm text-gray-500">Prénom</div>
                      <div class="text-base">{{ locataire.prenom || 'Non spécifié' }}</div>
                    </div>
                    <div>
                      <div class="text-sm text-gray-500">Email</div>
                      <div class="text-base">{{ locataire.email || 'Non spécifié' }}</div>
                    </div>
                    <div>
                      <div class="text-sm text-gray-500">Téléphone</div>
                      <div class="text-base">{{ locataire.telephone || 'Non spécifié' }}</div>
                    </div>
                    <div v-if="locataire.dateNaissance">
                      <div class="text-sm text-gray-500">Date de naissance</div>
                      <div class="text-base">{{ formatDate(locataire.dateNaissance) }}</div>
                    </div>
                  </div>
                </div>

                <div v-if="locataire.adresse || locataire.ville || locataire.codePostal || locataire.pays">
                  <NH3 class="sous-titre mb-4">Adresse</NH3>
                  <div class="space-y-1">
                    <div v-if="locataire.adresse" class="text-base">{{ locataire.adresse }}</div>
                    <div v-if="locataire.codePostal || locataire.ville" class="text-base">
                      {{ locataire.codePostal }} {{ locataire.ville }}
                    </div>
                    <div v-if="locataire.pays" class="text-base">{{ locataire.pays }}</div>
                  </div>
                </div>
                <NEmpty v-else description="Aucune adresse enregistrée" />
              </div>
            </NCard>
          </NTabPane>

          <!-- Onglet Documents -->
          <NTabPane name="documents" :tab="[
            h(NIcon, { component: Document24Filled, size: 20 }),
            ' Documents'
          ]">
            <NCard :bordered="false">
              <div class="mb-4 flex justify-between items-center">
                <NH3 class="sous-titre">Documents</NH3>
                <NButton type="primary" @click="showDocumentModal = true" title="Ajouter un document">
                  <template #icon>
                    <NIcon :component="Add24Filled" />
                  </template>
                </NButton>
              </div>

              <div class="document-cards">
                <NCard
                  v-for="doc in locataire.documents || []"
                  :key="doc.id"
                  class="document-card"
                  :bordered="true"
                  size="medium"
                >
                  <div class="flex justify-between items-center mb-2">
                    <div class="flex items-center gap-2">
                      <NIcon :component="getDocumentIcon(doc.nomFichier || '')" size="22" />
                      <span class="font-bold">{{ doc.titre || doc.nomFichier || 'Sans nom' }}</span>
                    </div>
                    <div class="flex gap-2">
                      <NButton size="small" text @click="downloadDocument(doc)" title="Télécharger">
                        <NIcon :component="ArrowDownload24Filled" />
                      </NButton>
                      <NPopconfirm @positive-click="() => deleteDocument(doc.id)">
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
                <NEmpty v-if="!locataire.documents || locataire.documents.length === 0" description="Aucun document pour le moment" />
              </div>
            </NCard>
          </NTabPane>
        </NTabs>
      </template>
    </NSpin>

    <!-- Modal d'ajout de document -->
    <NModal v-model:show="showDocumentModal">
      <NCard
        style="width: 600px; max-width: 90vw;"
        title="Ajouter un document"
        :bordered="false"
        size="huge"
        role="dialog"
        aria-modal="true"
      >
        <template #header-extra>
          <NButton text @click="showDocumentModal = false">
            <NIcon :component="Dismiss24Filled" />
          </NButton>
        </template>

        <div class="space-y-4">
          <NFormItem label="Type de document" required>
            <NSelect
              v-model:value="documentForm.type"
              :options="documentTypes"
              placeholder="Sélectionnez un type de document"
              :consistent-menu-width="false"
            />
          </NFormItem>
          <div class="border-2 border-dashed rounded-lg p-6 text-center cursor-pointer hover:bg-gray-50 transition-colors"
               @dragover.prevent
               @drop.prevent="handleDrop">
            <input 
              type="file" 
              ref="fileInput"
              class="hidden" 
              accept=".pdf,.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png"
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
              <NButton type="primary" ghost @click="$refs.fileInput.click()">
                Sélectionner un fichier
              </NButton>
              <NText depth="3" class="block mt-3 text-xs">
                Formats supportés: PDF, DOC, DOCX, XLS, XLSX, JPG, PNG (max 10MB)
              </NText>
            </div>
          </div>
          
          <div v-if="documentForm.fichier" class="mt-4 p-3 border rounded">
            <div class="flex justify-between items-center">
              <div class="flex items-center">
                <NIcon :component="getDocumentIcon(documentForm.fichier.name)" size="24" class="mr-2" />
                <span>{{ documentForm.fichier.name }}</span>
              </div>
              <NButton text @click="() => { documentForm.fichier = null; documentForm.contenu = ''; if (fileInput) fileInput.value = ''; }">
                <NIcon :component="Dismiss24Filled" />
              </NButton>
            </div>
          </div>
          
          <div class="mt-6 flex justify-end space-x-2">
            <NButton @click="showDocumentModal = false">Annuler</NButton>
            <NButton 
              type="primary" 
              :disabled="!documentForm.fichier"
              :loading="saving"
              @click="uploadDocument"
            >
              Téléverser
            </NButton>
          </div>
        </div>
      </NCard>
    </NModal>
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
.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
}
.page-header .back-button {
  margin-right: 1rem;
}
.action-buttons {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}
.action-button {
  margin-right: 0.5rem;
}
.info-grid {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 1rem;
  margin-top: 1rem;
}
.info-label {
  font-weight: 500;
  color: var(--n-text-color);
}
.document-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
.document-card {
  min-width: 0;
  width: 100%;
  box-sizing: border-box;
}
.label {
  font-weight: 600;
  color: var(--n-text-color);
  margin-right: 4px;
}
@media (max-width: 768px) {
  .titre-principal, h1, h2, h3 {
    font-size: 1.25rem !important;
  }
  .sous-titre {
    font-size: 1rem;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .page-header, .action-buttons {
    margin-bottom: 1rem;
  }
  .document-cards {
    grid-template-columns: 1fr;
    gap: 12px;
  }
}
.tab-label-hover {
  display: none;
  margin-left: 6px;
  vertical-align: middle;
  font-size: 15px;
  font-weight: 500;
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
.n-tabs-tab .tab-label-hover {
  display: inline;
  position: static;
  background: none;
  color: inherit;
  padding: 0;
  border-radius: 0;
  white-space: normal;
  box-shadow: none;
}
</style>
