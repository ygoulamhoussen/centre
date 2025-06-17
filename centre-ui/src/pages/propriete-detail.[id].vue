<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useAuthStore } from '@/store/modules/auth'
import { useRoute, useRouter } from 'vue-router'
import {
  NButton,
  NCard,
  NDataTable,
  NDatePicker,
  NEmpty,
  NForm,
  NFormItem,
  NH1,
  NInput,
  NInputNumber,
  NModal,
  NSelect,
  NSpace,
  NSpin,
  NTabPane,
  NTabs,
  NIcon,
  useMessage,
} from 'naive-ui'
import { h, onMounted, ref } from 'vue'
import { 
  Edit24Filled,
  Delete24Filled,
  Add24Filled,
  Save24Filled,
  Dismiss24Filled,
  ArrowLeft24Filled
} from '@vicons/fluent'
// import { useRouter } from 'vue-router'  // Will be used later


definePage({
  meta: {
    title: 'Details propriete',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})
const route = useRoute()
const router = useRouter()
const store = useUnifiedStore()
const message = useMessage()

// États
const loading = ref(true)
const saving = ref(false)
const editingInfos = ref(false)
const showCompositionModal = ref(false)
const proprieteDetail = ref<any | null>(null)

// Récupérer l'ID de la propriété depuis les paramètres de route
const proprieteId = route.params.id as string

// Formulaires
const editForm = ref({
  id: '',
  nom: '',
  adresse: '',
  ville: '',
  codePostal: '',
  typeBien: '',
  montantAcquisition: 0,
  dateAcquisition: null as string | null,
  tantieme: 0,
  fraisNotaire: 0,
  fraisAgence: 0,
})

const compositionForm = ref({
  id: '',
  categorie: '',
  pourcentage: 0,
  montant: 0,
  description: '',
  proprieteId: '',
})

const categoriesAmortissement = [
  { value: 'terrain', label: 'Terrains (non amortissable, 10-20%)' },
  { value: 'bati', label: 'Bâti (gros œuvre, 50-60%, 35 ans)' },
  { value: 'agencement', label: 'Agencements et second œuvre (20-30%, 12 ans)' },
  { value: 'mobilier', label: 'Mobilier et électroménager (variable, 7 ans)' },
  { value: 'frais', label: 'Frais d\'acquisition (5 ans)' },
]

function calculerMontant() {
  if (proprieteDetail.value?.propriete && compositionForm.value.pourcentage) {
    const montantTotal = parseFloat(proprieteDetail.value.propriete.montantAcquisition) || 0
    compositionForm.value.montant = (montantTotal * compositionForm.value.pourcentage) / 100
  }
}

// Colonnes du tableau des compositions
// Tableau des colonnes pour le tableau des compositions
const _compositionColumns = [
  { title: 'Catégorie', key: 'categorie' },
  {
    title: 'Montant',
    key: 'montant',
    render: (row: any) => formatCurrency(row.montant),
  },
  { title: 'Description', key: 'description' },
  {
    title: 'Actions',
    key: 'actions',
    render: (row: any) => h('div', { class: 'actions' }, [
      h(NButton, {
        size: 'small',
        onClick: () => editComposition(row),
      }, 'Modifier'),
      h(NButton, {
        size: 'small',
        type: 'error',
        onClick: () => deleteComposition(row.id),
      }, 'Supprimer'),
    ]),
  },
]

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
    console.log('Chargement des détails de la propriété ID:', proprieteId)
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprieteDetails/${proprieteId}`
    console.log('URL de l\'API:', url)
    
    const response = await fetch(url)
    console.log('Réponse reçue. Statut:', response.status)
    
    const responseText = await response.text()
    console.log('Réponse brute:', responseText)
    
    if (!response.ok) {
      console.error('Erreur de réponse. Contenu:', responseText)
      throw new Error(`Erreur HTTP: ${response.status}`)
    }
    
    // Essayer de parser le JSON
    try {
      const data = JSON.parse(responseText)
      console.log('Données JSON parsées:', data)
      proprieteDetail.value = data
    } catch (parseError) {
      console.error('Erreur lors de l\'analyse JSON:', parseError)
      throw new Error(`Réponse du serveur invalide (pas du JSON): ${responseText.substring(0, 200)}...`)
    }
  } catch (error) {
    console.error('Erreur lors du chargement de la propriété:', error)
    message.error('Erreur de chargement de la propriété: ' + (error instanceof Error ? error.message : String(error)))
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

function addComposition() {
  compositionForm.value = {
    id: '',
    categorie: '',
    pourcentage: 0,
    montant: 0,
    description: '',
    proprieteId: store.selectedProprieteId,
  }
  showCompositionModal.value = true
}

function editComposition(composition: any) {
  compositionForm.value = { ...composition, pourcentage: 0 }
  // Si on édite une composition existante, on calcule le pourcentage
  if (proprieteDetail.value?.propriete && composition.montant) {
    const montantTotal = parseFloat(proprieteDetail.value.propriete.montantAcquisition) || 1 // Éviter la division par zéro
    compositionForm.value.pourcentage = (parseFloat(composition.montant) / montantTotal) * 100
  }
  showCompositionModal.value = true
}

async function saveComposition() {
  try {
    saving.value = true
    
    // S'assurer que le montant est à jour avant sauvegarde
    if (compositionForm.value.pourcentage) {
      calculerMontant()
    }
    
    // Préparer la composition
    const compositionToSave = {
      id: compositionForm.value.id || undefined,
      categorie: compositionForm.value.categorie,
      montant: compositionForm.value.montant.toString(),
      description: compositionForm.value.description,
    };
    
    // Récupérer la propriété actuelle
    const propriete = { ...proprieteDetail.value.propriete };
    
    // Mettre à jour ou ajouter la composition
    if (compositionForm.value.id) {
      // Mise à jour d'une composition existante
      const index = propriete.compositions.findIndex((c: any) => c.id === compositionForm.value.id);
      if (index !== -1) {
        propriete.compositions[index] = {
          ...propriete.compositions[index],
          ...compositionToSave
        };
      }
    } else {
      // Ajout d'une nouvelle composition
      if (!propriete.compositions) {
        propriete.compositions = [];
      }
      propriete.compositions.push({
        ...compositionToSave,
        id: '' // L'ID sera généré par le backend
      });
    }
    
    // Mettre à jour la propriété avec les nouvelles compositions
    const authStore = useAuthStore();
    const userId = authStore.userInfo.userId;
    
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePropriete/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(propriete),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Erreur lors de la mise à jour de la propriété');
    }

    message.success('Composition enregistrée avec succès');
    showCompositionModal.value = false;
    await fetchProprieteDetails();
  } catch (error) {
    console.error('Erreur:', error);
    message.error(error instanceof Error ? error.message : 'Erreur lors de la sauvegarde de la composition');
  } finally {
    saving.value = false;
  }
}

async function deleteComposition(id: string) {
  try {
    if (!proprieteDetail.value?.propriete) return;
    
    // Créer une copie de la propriété
    const propriete = { ...proprieteDetail.value.propriete };
    
    // Filtrer pour supprimer la composition
    if (propriete.compositions) {
      propriete.compositions = propriete.compositions.filter((c: any) => c.id !== id);
      
      // Mettre à jour la propriété
      const authStore = useAuthStore();
      const userId = authStore.userInfo.userId;
      
      const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePropriete/${userId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(propriete),
      });

      if (!response.ok) {
        const error = await response.text();
        throw new Error(error || 'Erreur lors de la suppression de la composition');
      }
      
      // Recharger les données
      await fetchProprieteDetails();
      message.success('Composition supprimée avec succès');
    }
  } catch (error) {
    console.error('Erreur lors de la suppression :', error);
    message.error(error instanceof Error ? error.message : 'Erreur lors de la suppression de la composition');
  }
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

// Cycle de vie
onMounted(() => {
  fetchProprieteDetails()
})

function definePage(arg0: { meta: { title: string; hideInMenu: boolean; activeMenu: string } }) {
  throw new Error('Function not implemented.')
}
</script>

<template>
  <div class="propriete-detail">
    <div class="page-header">
      <NButton text @click="$router.push('/propriete')" class="back-button">
        <NIcon :component="ArrowLeft24Filled" size="20" />
      </NButton>
      <NH1>Détails de la propriété</NH1>
    </div>

    <NSpin :show="loading">
      <NTabs v-if="proprieteDetail" type="line" animated>
        <!-- Onglet Informations -->
        <NTabPane name="infos" tab="Informations">
          <div v-if="!editingInfos" class="action-buttons">
            <NButton type="primary" @click="startEditing('infos')" class="action-button" ghost>
              <template #icon>
                <NIcon :component="Edit24Filled" />
              </template>
              Modifier
            </NButton>
            <NPopconfirm
              @positive-click="() => supprimerPropriete(proprieteDetail.propriete.id)"
            >
              <template #trigger>
                <NButton type="error" ghost class="action-button">
                  <template #icon>
                    <NIcon :component="Delete24Filled" />
                  </template>
                  Supprimer
                </NButton>
              </template>
              Êtes-vous sûr de vouloir supprimer cette propriété ?
            </NPopconfirm>
          </div>
          <div v-else class="action-buttons">
            <NButton type="primary" :loading="saving" @click="savePropriete" class="action-button">
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

          <div v-if="!editingInfos" class="info-grid">
            <div class="info-label">
              Nom :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.nom }}
            </div>

            <div class="info-label">
              Adresse :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.adresse }}
            </div>

            <div class="info-label">
              Ville :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.ville }}
            </div>

            <div class="info-label">
              Type :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.typeBien }}
            </div>

            <div class="info-label">
              Montant acquisition :
            </div>
            <div class="info-value">
              {{ formatCurrency(proprieteDetail.propriete.montantAcquisition) }}
            </div>

            <div class="info-label">
              Date acquisition :
            </div>
            <div class="info-value">
              {{ formatDate(proprieteDetail.propriete.dateAcquisition) }}
            </div>

            <div class="info-label">
              Tantième :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.tantieme }}
            </div>
          </div>

          <NForm v-else class="edit-form">
            <div class="info-grid">
              <div class="info-label">
                Nom :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.nom" />
              </div>

              <div class="info-label">
                Adresse :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.adresse" />
              </div>

              <div class="info-label">
                Code postal :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.codePostal" />
              </div>

              <div class="info-label">
                Ville :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.ville" />
              </div>

              <div class="info-label">
                Type :
              </div>
              <div class="info-value">
                <NSelect
                  v-model:value="editForm.typeBien"
                  :options="[
                    { label: 'Appartement', value: 'APPARTEMENT' },
                    { label: 'Maison', value: 'MAISON' },
                    { label: 'Local commercial', value: 'LOCAL_COMMERCIAL' },
                  ]"
                  style="width: 100%"
                />
              </div>

              <div class="info-label">
                Montant acquisition :
              </div>
              <div class="info-value">
                <NInputNumber v-model:value="editForm.montantAcquisition" style="width: 100%" />
              </div>

              <div class="info-label">
                Date acquisition :
              </div>
              <div class="info-value">
                <NDatePicker v-model:value="editForm.dateAcquisition" style="width: 100%" />
              </div>

              <div class="info-label">
                Tantième :
              </div>
              <div class="info-value">
                <NInputNumber v-model:value="editForm.tantieme" style="width: 100%" />
              </div>
            </div>
          </NForm>
        </NTabPane>

        <!-- Onglet Compositions -->
        <NTabPane name="compositions" tab="Compositions">
          <div class="action-buttons">
            <NButton type="primary" @click="addComposition" class="action-button">
              <template #icon>
                <NIcon :component="Add24Filled" />
              </template>
              Ajouter une composition
            </NButton>
          </div>

          <NDataTable
            :columns="[
              { key: 'categorie', title: 'Catégorie' },
              {
                key: 'montant',
                title: 'Montant',
                render: (row: any) => formatCurrency(row.montant),
              },
              {
                key: 'description',
                title: 'Description',
              },
              {
                key: 'actions',
                title: 'Actions',
                render: (row: any) => h('div', { class: 'table-actions' }, [
                  h(NButton, {
                    size: 'small',
                    text: true,
                    onClick: () => editComposition(row),
                    title: 'Modifier',
                    class: 'action-icon',
                    style: { marginRight: '8px' }
                  }, () => h(NIcon, { component: Edit24Filled, size: 18 })),
                  h(NButton, {
                    size: 'small',
                    text: true,
                    onClick: () => deleteComposition(row.id),
                    title: 'Supprimer',
                    class: 'action-icon error',
                  }, () => h(NIcon, { component: Delete24Filled, size: 18 }))
                ]),
              },
            ]"
            :data="proprieteDetail?.compositions || []"
            :loading="loading"
          >
            <template #empty>
              <NEmpty description="Aucune composition trouvée" />
            </template>
          </NDataTable>

          <!-- Modal d'édition de composition -->
          <NModal v-model:show="showCompositionModal">
            <NCard
              style="width: 600px"
              title="Édition de composition"
              :bordered="false"
              size="huge"
              role="dialog"
              aria-modal="true"
            >
              <NForm :model="compositionForm">
                <NFormItem label="Catégorie">
                  <NSelect
                    v-model:value="compositionForm.categorie"
                    :options="categoriesAmortissement"
                    placeholder="Sélectionnez une catégorie"
                    clearable
                  />
                </NFormItem>
                <NFormItem label="Pourcentage du coût total">
                  <NInputNumber 
                    v-model:value="compositionForm.pourcentage"
                    :min="0"
                    :max="100"
                    :step="0.01"
                    @update:value="calculerMontant"
                    :suffix="'%'"
                  />
                </NFormItem>
                <NFormItem label="Montant calculé">
                  <NInputNumber 
                    :value="compositionForm.montant" 
                    :disabled="true"
                    :formatter="value => formatCurrency(value)"
                  />
                </NFormItem>
                <NFormItem label="Description">
                  <NInput
                    v-model:value="compositionForm.description"
                    type="textarea"
                    :autosize="{ minRows: 3 }"
                  />
                </NFormItem>
              </NForm>
              <template #footer>
                <NSpace justify="end">
                  <NButton @click="showCompositionModal = false">
                    Annuler
                  </NButton>
                  <NButton type="primary" :loading="saving" @click="saveComposition">
                    Enregistrer
                  </NButton>
                </NSpace>
              </template>
            </NCard>
          </NModal>
        </NTabPane>

        <!-- Onglet Documents -->
        <NTabPane name="documents" tab="Documents">
          <NEmpty description="Aucun document pour le moment" />
        </NTabPane>
      </NTabs>
    </NSpin>
  </div>
</template>

<style scoped>
/* Styles de base pour les champs de formulaire */
.info-value {
  width: 100%;
}

/* Tous les champs de saisie prennent 100% de la largeur */
.info-value :deep(.n-input),
.info-value :deep(.n-input-number),
.info-value :deep(.n-date-picker),
.info-value :deep(.n-select) {
  width: 100% !important;
  max-width: 100%;
}

/* Pour les éléments internes des champs */
.info-value :deep(.n-input__input),
.info-value :deep(.n-input-number-input),
.info-value :deep(.n-base-selection) {
  width: 100% !important;
}

/* Ajustement pour le sélecteur de date */
.info-value :deep(.n-date-picker) {
  width: 100%;
}

/* Pour les petits écrans */
@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .info-label {
    text-align: left;
  }
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
  margin-bottom: 20px;
  display: flex;
  gap: 12px;
}

.action-button {
  display: inline-flex;
  align-items: center;
  gap: 6px;
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

.page-content {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.info-grid {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 10px;
  margin-top: 20px;
  width: 100%;
}

.info-label {
  font-weight: bold;
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.info-value {
  padding: 12px;
  border: 1px solid #eee;
  border-radius: 4px;
}

.edit-form {
  width: 100%;
  max-width: 100%;
  margin-top: 20px;
}

.n-tabs {
  width: 100%;
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

/* Pour les champs qui doivent prendre toute la largeur */
.full-width {
  grid-column: 1 / -1;
}

/* Ajustements pour les petits écrans */
@media (max-width: 768px) {
  .n-form {
    grid-template-columns: 1fr;
  }
  .info-grid {
    grid-template-columns: 1fr;
  }
  .info-label {
    padding-bottom: 4px;
  }
}

/* Styles pour la table des compositions */
.n-data-table {
  width: 100%;
  margin-top: 20px;
}

/* Styles pour la modale */
.n-modal {
  display: flex;
  align-items: center;
  justify-content: center;
}

.n-card {
  width: 90%;
  max-width: 800px;
  margin: 0 auto;
}

.ml-2 {
  margin-left: 8px;
}

.actions {
  display: flex;
  gap: 8px;
}
</style>
