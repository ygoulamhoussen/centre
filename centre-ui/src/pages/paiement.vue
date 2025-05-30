<script setup lang="ts">
import type { PaiementDTO } from '@/types/dto'
import { 
  deletePaiement as apiDeletePaiement, 
  fetchPaiements, 
  createPaiement, 
  updatePaiement 
} from '@/service/api/paiement'
import { useAuthStore } from '@/store/modules/auth'
import { h, onMounted, ref, computed, nextTick } from 'vue'
import { NButton, NIcon, useMessage } from 'naive-ui'
import { SearchOutlined, PlusOutlined, EyeOutlined, EditOutlined, DeleteOutlined } from '@vicons/antd'
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
    icon: 'mdi:account-group',
    order: 3,
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
      value => value && String(value).toLowerCase().includes(searchTerm)
    )
  )
})

const createColumns = () => [
  {
    title: 'ID',
    key: 'id',
    width: 80,
  },
  {
    title: 'Date',
    key: 'datePaiement',
    render: (row: PaiementDTO) => formatDate(row.datePaiement)
  },
  {
    title: 'Montant',
    key: 'montant',
    render: (row: PaiementDTO) => formatCurrency(row.montant)
  },
  {
    title: 'Moyen de paiement',
    key: 'moyenPaiement'
  },
  {
    title: 'Référence',
    key: 'reference'
  },
  {
    title: 'Statut',
    key: 'estValide',
    render: (row: PaiementDTO) => row.estValide === 'true' ? 'Validé' : 'En attente'
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 150,
    render: (row: PaiementDTO) => h('div', { class: 'flex gap-2' }, [
      h(
        NButton,
        {
          text: true,
          type: 'primary',
          onClick: () => viewDetails(row)
        },
        { default: () => h(NIcon, { component: EyeOutlined }) }
      ),
      h(
        NButton,
        {
          text: true,
          type: 'info',
          onClick: () => editPaiement(row)
        },
        { default: () => h(NIcon, { component: EditOutlined }) }
      ),
      h(
        NButton,
        {
          text: true,
          type: 'error',
          onClick: () => confirmDelete(row)
        },
        { default: () => h(NIcon, { component: DeleteOutlined }) }
      )
    ])
  }
]

const columns = createColumns()

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
      await authStore.getUserInfo()
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
    <n-card>
      <template #header>
        <div class="flex justify-between items-center">
          <span class="text-lg font-medium">Gestion des Paiements</span>
          <n-button type="primary" @click="openCreateDialog">
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

        <n-data-table
          :columns="columns"
          :data="filteredPaiements"
          :loading="loading"
          :pagination="pagination"
          :bordered="false"
          class="min-h-[400px]"
        >
          <template #empty>
            <n-empty description="Aucun paiement trouvé" class="py-8">
              <template #description>
                <span>Aucun paiement trouvé</span>
              </template>
            </n-empty>
          </template>
        </n-data-table>
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
      v-model:show="showDetailsDialog"
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
