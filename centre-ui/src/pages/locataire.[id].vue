<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
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
  NTabPanels
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
  Download24Filled,
  ArrowLeft24Filled
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

// Gestion des documents
const showDocumentModal = ref(false)
const documentFile = ref<File | null>(null)
const documentBase64 = ref<string | null>(null)

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
    title: 'Nom',
    key: 'titre',
    render: (row: any) => row.titre || row.nomFichier || 'Sans nom'
  },
  {
    title: 'Type',
    key: 'typeDocument',
    render: (row: any) => row.typeDocument || 'Inconnu'
  },
  {
    title: 'Actions',
    key: 'actions',
    render: (row: any) => h(
      'div',
      { class: 'flex space-x-2' },
      [
        h(
          NButton,
          {
            text: true,
            onClick: () => downloadDocument(row)
          },
          { default: () => 'Télécharger' }
        ),
        h(
          NPopconfirm,
          {
            onPositiveClick: () => deleteDocument(row.id)
          },
          {
            trigger: () => h(
              NButton,
              { text: true, type: 'error' },
              { default: () => 'Supprimer' }
            ),
            default: () => 'Êtes-vous sûr de vouloir supprimer ce document ?'
          }
        )
      ]
    )
  }
]

function handleFileUpload(event: Event) {
  const input = event.target as HTMLInputElement
  if (!input.files || input.files.length === 0) return
  
  const file = input.files[0]
  documentFile.value = file
  
  // Vérifier la taille du fichier (max 10MB)
  if (file.size > 10 * 1024 * 1024) {
    message.error('Le fichier est trop volumineux. La taille maximale est de 10MB.')
    input.value = ''
    documentFile.value = null
    return
  }
  
  const reader = new FileReader()
  reader.onload = () => {
    const result = reader.result as string
    if (result.startsWith('data:')) {
      // Stocker le contenu en base64 (sans le préfixe data:...)
      documentBase64.value = result.split(',')[1]
    } else {
      console.error('Format inattendu pour le fichier')
      message.error('Format de fichier non supporté')
      input.value = ''
      documentFile.value = null
    }
  }
  reader.onerror = () => {
    console.error('Erreur lors de la lecture du fichier')
    message.error('Erreur lors de la lecture du fichier')
    input.value = ''
    documentFile.value = null
  }
  reader.readAsDataURL(file)
}

async function uploadDocument() {
  if (!documentFile.value || !documentBase64.value) {
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
    
    const documentData = {
      contenu: documentBase64.value,
      utilisateurId: userId,
      locataireId: locataireId,
      typeDocument: 'AUTRE', // Vous pouvez ajouter un sélecteur de type si nécessaire
      titre: documentFile.value.name,
      nomFichier: documentFile.value.name,
      dateDocument: new Date().toISOString().split('T')[0]
    }
    
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
    documentFile.value = null
    documentBase64.value = null
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
      <NH1>Détails du locataire</NH1>
    </div>

    <NSpin :show="loading">
      <NCard v-if="!locataire && !loading">
        <NEmpty description="Locataire non trouvé" />
      </NCard>

      <template v-else-if="locataire">
        <NTabs type="line" animated>
          <!-- Onglet Informations -->
          <NTabPane name="informations" tab="Informations">
            <div v-if="!editing" class="action-buttons mb-4">
              <NButton type="primary" @click="startEditing" class="action-button" ghost>
                <template #icon>
                  <NIcon :component="Edit24Filled" />
                </template>
                Modifier
              </NButton>
              <NPopconfirm
                @positive-click="confirmDelete"
              >
                <template #trigger>
                  <NButton type="error" ghost class="action-button">
                    <template #icon>
                      <NIcon :component="Delete24Filled" />
                    </template>
                    Supprimer
                  </NButton>
                </template>
                Êtes-vous sûr de vouloir supprimer ce locataire ?
              </NPopconfirm>
            </div>
            <div v-else class="action-buttons mb-4">
              <NButton type="primary" :loading="saving" @click="saveLocataire" class="action-button">
                <template #icon>
                  <NIcon :component="Save24Filled" />
                </template>
                Enregistrer
              </NButton>
              <NButton class="action-button ml-2" @click="cancelEditing">
                <template #icon>
                  <NIcon :component="Dismiss24Filled" />
                </template>
                Annuler
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
                  <h3 class="text-lg font-semibold mb-4">Informations personnelles</h3>
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
                  <h3 class="text-lg font-semibold mb-4">Adresse</h3>
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
          <NTabPane name="documents" tab="Documents">
            <NCard :bordered="false">
              <div class="mb-4 flex justify-between items-center">
                <h3 class="text-lg font-semibold">Documents</h3>
                <NButton type="primary" @click="showDocumentModal = true">
                  <template #icon>
                    <NIcon :component="Add24Filled" />
                  </template>
                  Ajouter un document
                </NButton>
              </div>

              <NDataTable
                v-if="locataire.documents && locataire.documents.length > 0"
                :columns="documentColumns"
                :data="locataire.documents"
                :bordered="false"
                :single-line="false"
              />
              <NEmpty v-else description="Aucun document pour le moment" />
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
          
          <div v-if="documentFile" class="mt-4 p-3 border rounded">
            <div class="flex justify-between items-center">
              <div class="flex items-center">
                <NIcon :component="getDocumentIcon(documentFile.name)" size="24" class="mr-2" />
                <span>{{ documentFile.name }}</span>
              </div>
              <NButton text @click="documentFile = null">
                <NIcon :component="Dismiss24Filled" />
              </NButton>
            </div>
          </div>
          
          <div class="mt-6 flex justify-end space-x-2">
            <NButton @click="showDocumentModal = false">Annuler</NButton>
            <NButton 
              type="primary" 
              :disabled="!documentFile"
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

.document-list {
  margin-top: 1rem;
}

.document-item {
  transition: background-color 0.2s;
}

.document-item:hover {
  background-color: var(--n-color-embedded);
}

.edit-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 1.5rem;
  background: var(--n-color-embedded);
  border-radius: var(--n-border-radius);
}
</style>
