<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useAuthStore } from '@/store/modules/auth'
import { useRouter } from 'vue-router'
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
  useMessage,
} from 'naive-ui'
import { h, onMounted, ref } from 'vue'
// import { useRouter } from 'vue-router'  // Will be used later


definePage({
  meta: {
    title: 'Details propriete',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})
const store = useUnifiedStore()
const message = useMessage()
const router = useRouter()

// États
const loading = ref(true)
const saving = ref(false)
const editingInfos = ref(false)
const showCompositionModal = ref(false)
const proprieteDetail = ref<any | null>(null)

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
    const id = store.selectedProprieteId
    if (!id) {
      throw new Error('Aucune propriété sélectionnée')
    }

    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprieteDetails/${id}`)
    if (!res.ok) {
      throw new Error('Propriété introuvable')
    }
    proprieteDetail.value = await res.json()
  }
  catch (error) {
    message.error('Erreur de chargement de la propriété')
    console.error(error)
  }
  finally {
    loading.value = false
  }
}

function startEditing(section?: string) {
  if (section === 'infos' && proprieteDetail.value?.propriete) {
    editForm.value = { ...proprieteDetail.value.propriete }
    editingInfos.value = true
  }
  // Ajouter d'autres sections si nécessaire
}

async function savePropriete() {
  try {
    saving.value = true
    const userId = 'user-id' // À remplacer par l'ID de l'utilisateur connecté

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/savePropriete`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-User-Id': userId,
      },
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
    <NH1>Détails de la propriété</NH1>

    <NSpin :show="loading">
      <NTabs v-if="proprieteDetail" type="line" animated>
        <!-- Onglet Informations -->
        <NTabPane name="infos" tab="Informations">
          <div v-if="!editingInfos" class="action-buttons">
            <NButton type="primary" @click="startEditing('infos')" class="mr-2">
              Modifier
            </NButton>
            <NPopconfirm
              @positive-click="() => supprimerPropriete(proprieteDetail.propriete.id)"
              positive-text="Supprimer"
              negative-text="Annuler"
            >
              <template #trigger>
                <NButton type="error" ghost>
                  Supprimer la propriété
                </NButton>
              </template>
              Êtes-vous sûr de vouloir supprimer cette propriété ? Cette action est irréversible.
            </NPopconfirm>
          </div>
          <div v-else class="action-buttons">
            <NButton type="primary" :loading="saving" @click="savePropriete">
              Enregistrer
            </NButton>
            <NButton class="ml-2" @click="cancelEditing">
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
            <NFormItem label="Nom">
              <NInput v-model:value="editForm.nom" />
            </NFormItem>

            <NFormItem label="Adresse">
              <NInput v-model:value="editForm.adresse" />
            </NFormItem>

            <NFormItem label="Ville">
              <NInput v-model:value="editForm.ville" />
            </NFormItem>

            <NFormItem label="Code postal">
              <NInput v-model:value="editForm.codePostal" />
            </NFormItem>

            <NFormItem label="Type de bien">
              <NSelect
                v-model:value="editForm.typeBien"
                :options="[
                  { label: 'Appartement', value: 'APPARTEMENT' },
                  { label: 'Maison', value: 'MAISON' },
                  { label: 'Local commercial', value: 'LOCAL_COMMERCIAL' },
                ]"
              />
            </NFormItem>

            <NFormItem label="Montant d'acquisition">
              <NInputNumber v-model:value="editForm.montantAcquisition" />
            </NFormItem>

            <NFormItem label="Date d'acquisition">
              <NDatePicker v-model:formatted-value="editForm.dateAcquisition" value-format="yyyy-MM-dd" />
            </NFormItem>

            <NFormItem label="Tantième">
              <NInputNumber v-model:value="editForm.tantieme" />
            </NFormItem>
          </NForm>
        </NTabPane>

        <!-- Onglet Compositions -->
        <NTabPane name="compositions" tab="Compositions">
          <div class="action-buttons">
            <NButton type="primary" @click="addComposition">
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
.propriete-detail {
  padding: 20px;
  width: 100%;
  margin: 0;
}

.page-content {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.action-buttons {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
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
