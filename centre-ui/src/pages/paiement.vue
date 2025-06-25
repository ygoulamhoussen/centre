<script setup lang="ts">
import type { PaiementDTO } from '@/types/dto'
import type { DataTableColumn } from 'naive-ui'
import { 
  deletePaiement as apiDeletePaiement, 
  fetchPaiements, 
  createPaiement, 
  updatePaiement 
} from '@/service/api/paiement'
import { useAuthStore } from '@/store/modules/auth'
import { DeleteOutlined, EditOutlined, EyeOutlined, PlusOutlined, SearchOutlined } from '@vicons/antd'
import { NButton, NIcon, NSpin, NTag, useMessage } from 'naive-ui'
import { h, onMounted, ref, computed, nextTick } from 'vue'
import PaiementDetailsDialog from './PaiementDetailsDialog.vue'
import PaiementForm from './PaiementForm.vue'

declare function definePage(options: {
  meta: {
    title: string
    icon: string
    order: number
  }
}): void

definePage({
  meta: {
    title: 'Mes paiements',
    icon: 'mdi:credit-card-outline',
    order: 7,
  },
})

const authStore = useAuthStore()
const loading = ref(false)
const search = ref('')
const paiements = ref<PaiementDTO[]>([])
const selectedPaiement = ref<PaiementDTO | null>(null)
const editedPaiement = ref<Partial<PaiementDTO>>({})
const showForm = ref(false)
const showDetailsDialog = ref(false)
const showDeleteDialog = ref(false)
const isEdit = ref(false)

const message = useMessage()

const pagination = {
  pageSize: 10,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  showQuickJumper: true,
}

const filteredPaiements = computed(() => {
  if (!search.value) return paiements.value
  
  const searchTerm = search.value.toLowerCase()
  return paiements.value.filter(paiement =>
    Object.values(paiement).some(
      value => value && String(value).toLowerCase().includes(searchTerm),
    ),
  )
})

function createColumns(): DataTableColumn[] {
  return [
    {
      title: 'ID',
      key: 'id',
      width: 80,
      align: 'center',
    },
    {
      title: 'Date',
      key: 'datePaiement',
      align: 'center',
      render: (rowData: any, _rowIndex: number) => formatDate(rowData.datePaiement),
    },
    {
      title: 'Montant',
      key: 'montant',
      align: 'center',
      render: (rowData: any, _rowIndex: number) => formatCurrency(rowData.montant),
    },
    {
      title: 'Moyen de paiement',
      key: 'moyenPaiement',
      align: 'center',
    },
    {
      title: 'Référence',
      key: 'reference',
      align: 'center',
    },
    {
      title: 'Statut',
      key: 'estValide',
      align: 'center',
      render: (rowData: any, _rowIndex: number) =>
        h(
          NTag,
          {
            type: rowData.estValide === 'true' ? 'success' : 'warning',
            bordered: false,
          },
          { default: () => (rowData.estValide === 'true' ? 'Validé' : 'En attente') },
        ),
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 150,
      align: 'center',
      render: (rowData: any, _rowIndex: number) => h('div', { class: 'flex gap-2 justify-center' }, [
        h(
          NButton,
          {
            text: true,
            type: 'primary',
            onClick: () => viewDetails(rowData),
          },
          { default: () => h(NIcon, { component: EyeOutlined }) },
        ),
        h(
          NButton,
          {
            text: true,
            type: 'info',
            onClick: () => editPaiement(rowData),
          },
          { default: () => h(NIcon, { component: EditOutlined }) },
        ),
        h(
          NButton,
          {
            text: true,
            type: 'error',
            onClick: () => confirmDelete(rowData),
          },
          { default: () => h(NIcon, { component: DeleteOutlined }) },
        ),
      ]),
    },
  ]
}

const columns: DataTableColumn[] = createColumns()

// Formater la date
function formatDate(dateString: string) {
  return new Date(dateString).toLocaleDateString('fr-FR')
}

// Formater la monnaie
function formatCurrency(amount: string) {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(Number(amount))
}

// Charger les paiements
async function loadPaiements() {
  try {
    console.log('Début du chargement des paiements...')
    loading.value = true
    
    // S'assurer que les informations de l'utilisateur sont chargées
    console.log('Vérification des informations utilisateur...')
    console.log('userInfo actuel:', authStore.userInfo)
    
    if (!authStore.userInfo.userId) {
      console.log('Récupération des informations utilisateur...')
      await authStore.initUserInfo()
      console.log('Nouvelles informations utilisateur:', authStore.userInfo)
    }
    
    const userId = authStore.userInfo.userId
    console.log('ID utilisateur:', userId)
    
    if (userId) {
      console.log('Appel de l\'API pour récupérer les paiements...')
      const response = await fetchPaiements(userId)
      console.log('Réponse de l\'API:', response)
      
      if (Array.isArray(response)) {
        console.log(`${response.length} paiements récupérés`)
        paiements.value = response
      } else {
        console.error('La réponse de l\'API n\'est pas un tableau:', response)
        message.error('Format de données invalide reçu du serveur')
        paiements.value = []
      }
    } else {
      console.error('Aucun ID utilisateur trouvé')
      message.error('Impossible d\'identifier l\'utilisateur')
      paiements.value = []
    }
  } catch (error) {
    console.error('Erreur lors du chargement des paiements:', error)
    const errorMessage = error instanceof Error ? error.message : 'Erreur inconnue'
    message.error(`Erreur lors du chargement des paiements: ${errorMessage}`)
    paiements.value = []
  } finally {
    console.log('Fin du chargement des paiements')
    loading.value = false
  }
}

// Ouvrir le formulaire de création
function openCreateDialog() {
  closeAllDialogs()
  editedPaiement.value = {}
  isEdit.value = false
  showForm.value = true
}

// Éditer un paiement
function editPaiement(paiement: PaiementDTO) {
  // S'assurer que les autres dialogues sont fermés
  closeAllDialogs()
  editedPaiement.value = { ...paiement }
  isEdit.value = true
  showForm.value = true
}

// Afficher les détails
function viewDetails(paiement: PaiementDTO) {
  // S'assurer que les autres dialogues sont fermés
  closeAllDialogs()
  selectedPaiement.value = { ...paiement }
  // Attendre le prochain tick pour s'assurer que le composant est monté
  nextTick(() => {
    showDetailsDialog.value = true
  })
}

// Fermer tous les dialogues
function closeAllDialogs() {
  showForm.value = false
  showDetailsDialog.value = false
  showDeleteDialog.value = false
}

// Confirmer la suppression
function confirmDelete(paiement: PaiementDTO) {
  selectedPaiement.value = paiement
  showDeleteDialog.value = true
}

// Supprimer un paiement
async function deletePaiement() {
  if (!selectedPaiement.value?.id)
    return

  try {
    await apiDeletePaiement(selectedPaiement.value.id)
    await loadPaiements()
    showDeleteDialog.value = false
  }
  catch (error) {
    console.error('Erreur lors de la suppression du paiement:', error)
  }
}

// Fermer le formulaire et réinitialiser les données
function closeForm() {
  showForm.value = false
  editedPaiement.value = {}
  selectedPaiement.value = null
}

// Gérer l'événement de sauvegarde
const isSaving = ref(false)

async function handleSaved(paiementData: PaiementDTO) {
  try {
    isSaving.value = true
    const authStore = useAuthStore()
    const userId = authStore.userInfo?.userId
    
    if (!userId) {
      message.error('Utilisateur non connecté')
      isSaving.value = false
      return
    }
    
    // Vérifier que les champs requis sont présents
    if (!paiementData.quittanceId || !paiementData.datePaiement || !paiementData.montant || !paiementData.moyenPaiement) {
      message.error('Veuillez remplir tous les champs obligatoires')
      isSaving.value = false
      return
    }
    
    try {
      if (isEdit.value && editedPaiement.value.id) {
        // Mise à jour d'un paiement existant
        await updatePaiement(editedPaiement.value.id, {
          ...paiementData,
          utilisateurId: userId
        })
        message.success('Paiement mis à jour avec succès')
      } else {
        // Création d'un nouveau paiement
        await createPaiement({
          ...paiementData,
          utilisateurId: userId
        })
        message.success('Paiement créé avec succès')
      }
      
      // Recharger les données et fermer le formulaire
      await loadPaiements()
      closeForm()
    } catch (error) {
      console.error('Erreur lors de la sauvegarde du paiement:', error)
      const errorMessage = error instanceof Error ? error.message : 'Une erreur est survenue lors de la sauvegarde du paiement'
      message.error(errorMessage)
    }
  } catch (error) {
    console.error('Erreur inattendue:', error)
    message.error('Une erreur inattendue est survenue')
  } finally {
    isSaving.value = false
  }
}

// Charger les données au montage du composant
onMounted(async () => {
  try {
    // S'assurer que l'utilisateur est connecté
    if (!authStore.isLogin) {
      await authStore.initUserInfo()
    }
    await loadPaiements()
  } catch (error) {
    console.error('Erreur lors de l\'initialisation:', error)
    message.error('Erreur lors du chargement des données')
  }
})
</script>

<template>
  <n-layout>
    <n-card class="w-full">
      <template #header>
        <div class="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-2">
          <div>
            <span class="text-lg font-medium">Gestion des Paiements</span>
            <span class="block text-gray-500 text-sm mt-1">Consultez, ajoutez ou modifiez vos paiements</span>
          </div>
          <n-button type="primary" size="large" @click="openCreateDialog">
            <template #icon>
              <n-icon><PlusOutlined /></n-icon>
            </template>
            Nouveau Paiement
          </n-button>
        </div>
      </template>

      <n-space vertical>
        <n-input
          v-model:value="search"
          placeholder="Rechercher"
          clearable
          class="mb-4"
        >
          <template #prefix>
            <n-icon :component="SearchOutlined" />
          </template>
        </n-input>

        <div class="w-full overflow-x-auto">
          <n-spin :show="loading" size="large" class="flex justify-center py-10 w-full">
            <div class="paiement-cards">
              <n-card
                v-for="paiement in filteredPaiements"
                :key="paiement.id"
                class="paiement-card"
                :bordered="true"
                size="medium"
              >
                <div class="flex justify-between items-center mb-2">
                  <div class="font-bold">Date : {{ formatDate(paiement.datePaiement) }}</div>
                  <div class="flex gap-2">
                    <n-button text type="primary" @click="() => viewDetails(paiement)">
                      <n-icon :component="EyeOutlined" />
                    </n-button>
                    <n-button text type="info" @click="() => editPaiement(paiement)">
                      <n-icon :component="EditOutlined" />
                    </n-button>
                    <n-button text type="error" @click="() => confirmDelete(paiement)">
                      <n-icon :component="DeleteOutlined" />
                    </n-button>
                  </div>
                </div>
                <div class="mb-1"><span class="label">Montant :</span> {{ formatCurrency(paiement.montant) }}</div>
                <div class="mb-1"><span class="label">Moyen :</span> {{ paiement.moyenPaiement }}</div>
                <div class="mb-1"><span class="label">Référence :</span> {{ paiement.reference }}</div>
                <div class="mb-1"><span class="label">Statut :</span> <n-tag :type="paiement.estValide === 'true' ? 'success' : 'warning'" size="small">{{ paiement.estValide === 'true' ? 'Validé' : 'En attente' }}</n-tag></div>
              </n-card>
              <n-empty v-if="!filteredPaiements.length" class="py-8">Aucun paiement trouvé</n-empty>
            </div>
          </n-spin>
        </div>
      </n-space>
    </n-card>

    <!-- Modale de création/édition -->
    <n-modal
      v-model:show="showForm"
      :title="isEdit ? 'Modifier le paiement' : 'Nouveau paiement'"
      preset="card"
      style="width: 90%; max-width: 600px;"
      :bordered="false"
      :segmented="{
        content: 'soft',
        footer: 'soft'
      }"
    >
      <paiement-form
        ref="paiementFormRef"
        :paiement="editedPaiement"
        :is-edit="isEdit"
        @saved="handleSaved"
        @cancel="closeForm"
      />
    </n-modal>

    <!-- Détails du paiement -->
    <PaiementDetailsDialog
      v-if="showDetailsDialog"
      v-model:modelValue="showDetailsDialog"
      :paiement="selectedPaiement"
    />

    <!-- Confirmation de suppression -->
    <n-modal v-if="showDeleteDialog" v-model:show="showDeleteDialog">
      <n-card
        style="width: 90%; max-width: 500px"
        title="Confirmer la suppression"
        :bordered="false"
        size="huge"
        role="dialog"
        aria-modal="true"
      >
        <template #header>
          <div class="text-lg font-medium">
            Confirmer la suppression
          </div>
        </template>
        <div class="py-4">
          Êtes-vous sûr de vouloir supprimer ce paiement ?
        </div>
        <template #footer>
          <div class="flex justify-end gap-2">
            <n-button @click="showDeleteDialog = false">
              Annuler
            </n-button>
            <n-button type="error" @click="deletePaiement">
              Supprimer
            </n-button>
          </div>
        </template>
      </n-card>
    </n-modal>
  </n-layout>
</template>

<style scoped>
.paiement-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
.paiement-card {
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
  .paiement-cards {
    grid-template-columns: 1fr;
    gap: 12px;
  }
}
</style>
