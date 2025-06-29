<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import {
  NAlert,
  NButton,
  NCard,
  NDataTable,
  NDescriptions,
  NDescriptionsItem,
  NForm,
  NFormItem,
  NGi,
  NGrid,
  NIcon,
  NInput,
  NInputNumber,
  NModal,
  NSelect,
  NSpace,
  NTabPane,
  NTabs,
  NText,
  useMessage,
  NTag,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import {
  Add24Filled,
  Calculator24Filled,
  Delete24Filled,
  Edit24Filled,
  Money24Filled,
} from '@vicons/fluent'
import { computed, onMounted, ref } from 'vue'
import type { ChargeDTO, RecetteDTO, EcritureComptableDTO } from '@/types/dto'
import {
  createCharge,
  createRecette,
  deleteCharge,
  deleteRecette,
  fetchCharges,
  fetchDocuments,
  fetchEcrituresComptables,
  fetchEcrituresComptablesByUtilisateurAndAnnee,
  fetchRecettes,
  uploadDocument,
  updateCharge,
  updateRecette,
  getEcrituresComptablesWithProprieteNom,
  getChargesWithProprieteNom,
  getRecettesWithProprieteNom,
  downloadDocument,
  updateEcritureComptable,
  deleteEcritureComptable,
} from '@/service/api/charges-recettes'

definePage({
  meta: {
    title: 'Comptabilité',
    icon: 'material-symbols:account-balance',
    order: 6,
  },
})

const authStore = useAuthStore()
const message = useMessage()

// États réactifs
const charges = ref<ChargeDTO[]>([])
const recettes = ref<RecetteDTO[]>([])
const ecrituresComptables = ref<EcritureComptableDTO[]>([])
const documents = ref<Array<{ label: string; value: string }>>([])
const proprietes = ref<Array<{ label: string; value: string }>>([])
const anneeFiscale = ref(new Date().getFullYear())
const proprieteSelectionnee = ref<string>('')
const chargement = ref(false)

// États pour les modales
const modalChargeVisible = ref(false)
const modalRecetteVisible = ref(false)
const modalEcritureVisible = ref(false)
const modeEdition = ref(false)
const chargeEnCours = ref<Partial<ChargeDTO>>({})
const recetteEnCours = ref<Partial<RecetteDTO>>({})
const ecritureEnCours = ref<Partial<EcritureComptableDTO>>({})

// États pour l'upload de fichiers
const fichierCharge = ref<File | null>(null)
const fichierRecette = ref<File | null>(null)
const uploadEnCours = ref(false)
const maxFileSize = 10 * 1024 * 1024 // 10MB

// Options pour les natures de charges
const naturesCharges = [
  { label: 'Travaux et réparations', value: 'TRAVAUX' },
  { label: 'Charges de copropriété', value: 'COPROPRIETE' },
  { label: 'Taxes foncières', value: 'TAXES' },
  { label: 'Assurance', value: 'ASSURANCE' },
  { label: 'Gestion locative', value: 'GESTION' },
  { label: 'Électricité', value: 'ELECTRICITE' },
  { label: 'Eau', value: 'EAU' },
  { label: 'Chauffage', value: 'CHAUFFAGE' },
  { label: 'Entretien', value: 'ENTRETIEN' },
  { label: 'Autres', value: 'AUTRES' },
]

// Options pour les types de recettes
const typesRecettes = [
  { label: 'Loyers', value: 'LOCATION' },
  { label: 'Exceptionnelle', value: 'EXCEPTIONNELLE' },
  { label: 'Quittance', value: 'QUITTANCE' },
]

// Colonnes pour les tableaux
const colonnesCharges = [
  { title: 'Date', key: 'dateCharge', width: 120 },
  { title: 'Propriété', key: 'proprieteNom', width: 200 },
  { title: 'Intitulé', key: 'intitule', width: 200 },
  { title: 'Nature', key: 'nature', width: 150 },
  { title: 'Montant', key: 'montant', width: 120, render: (row: ChargeDTO) => `${row.montant} €` },
  {
    title: 'Document',
    key: 'documentNom',
    width: 150,
    render: (row: ChargeDTO) => {
      if (row.documentId && row.documentNom) {
        return h(NButton, {
          size: 'small',
          type: 'primary',
          onClick: () => handleDownloadDocument(row.documentId!, row.documentNom!),
        }, { default: () => row.documentNom })
      }
      return h('span', { style: { color: '#999' } }, 'Aucun document')
    },
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 120,
    render: (row: ChargeDTO) => h(NSpace, { size: 'small' }, {
      default: () => [
        h(NButton, {
          size: 'small',
          type: 'primary',
          onClick: () => editerCharge(row),
        }, { default: () => h(NIcon, { component: Edit24Filled }) }),
        h(NButton, {
          size: 'small',
          type: 'error',
          onClick: () => supprimerCharge(row.id!),
        }, { default: () => h(NIcon, { component: Delete24Filled }) }),
      ],
    }),
  },
]

const colonnesRecettes = [
  { title: 'Date', key: 'dateRecette', width: 120 },
  { title: 'Propriété', key: 'proprieteNom', width: 200 },
  { title: 'Intitulé', key: 'intitule', width: 200 },
  { title: 'Type', key: 'type', width: 120 },
  { title: 'Montant', key: 'montant', width: 120, render: (row: RecetteDTO) => `${row.montant} €` },
  {
    title: 'Document',
    key: 'documentNom',
    width: 150,
    render: (row: RecetteDTO) => {
      if (row.documentId && row.documentNom) {
        return h(NButton, {
          size: 'small',
          type: 'primary',
          onClick: () => handleDownloadDocument(row.documentId!, row.documentNom!),
        }, { default: () => row.documentNom })
      }
      return h('span', { style: { color: '#999' } }, 'Aucun document')
    },
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 120,
    render: (row: RecetteDTO) => h(NSpace, { size: 'small' }, {
      default: () => [
        h(NButton, {
          size: 'small',
          type: 'primary',
          onClick: () => editerRecette(row),
        }, { default: () => h(NIcon, { component: Edit24Filled }) }),
        h(NButton, {
          size: 'small',
          type: 'error',
          onClick: () => supprimerRecette(row.id!),
        }, { default: () => h(NIcon, { component: Delete24Filled }) }),
      ],
    }),
  },
]

const colonnesEcritures = [
  { title: 'Date', key: 'dateEcriture', width: 120 },
  { title: 'Propriété', key: 'proprieteNom', width: 200 },
  {
    title: 'Type',
    key: 'type',
    width: 100,
    render: (row: EcritureComptableDTO) => {
      const type = row.type
      const color = type === 'CHARGE' ? 'error' : 'success'
      return h(NTag, { color }, { default: () => type })
    },
  },
  {
    title: 'Montant',
    key: 'montant',
    width: 120,
    render: (row: EcritureComptableDTO) => {
      const montant = parseFloat(row.montant)
      const color = row.type === 'CHARGE' ? '#ff4d4f' : '#52c41a'
      return h('span', { style: { color, fontWeight: 'bold' } }, `${montant.toFixed(2)} €`)
    },
  },
  { title: 'Description', key: 'commentaire', width: 300 },
  {
    title: 'Document',
    key: 'documentNom',
    width: 150,
    render: (row: EcritureComptableDTO) => {
      if (row.documentId && row.documentNom) {
        return h(NButton, {
          size: 'small',
          type: 'primary',
          onClick: () => handleDownloadDocument(row.documentId!, row.documentNom!),
        }, { default: () => row.documentNom })
      }
      return h('span', { style: { color: '#999' } }, 'Aucun document')
    },
  },
]

// Calculs
const totalCharges = computed(() => {
  return charges.value.reduce((sum, charge) => sum + Number.parseFloat(charge.montant), 0)
})

const totalRecettes = computed(() => {
  return recettes.value.reduce((sum, recette) => sum + Number.parseFloat(recette.montant), 0)
})

const solde = computed(() => totalRecettes.value - totalCharges.value)

// Méthodes
async function chargerDonnees() {
  if (!authStore.userInfo.userId) return

  chargement.value = true
  try {
    const [chargesData, recettesData, documentsData] = await Promise.all([
      getChargesWithProprieteNom(authStore.userInfo.userId),
      getRecettesWithProprieteNom(authStore.userInfo.userId),
      fetchDocuments(authStore.userInfo.userId),
    ])

    charges.value = chargesData
    recettes.value = recettesData

    // Charger les propriétés
    const propResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${authStore.userInfo.userId}`)
    if (propResponse.ok) {
      const proprietesData = await propResponse.json()
      proprietes.value = proprietesData.map((prop: any) => ({
        label: prop.nom,
        value: prop.id,
      }))
    }

    // Charger les documents
    documents.value = documentsData.map((doc: any) => ({
      label: doc.titre || doc.nomFichier,
      value: doc.id,
    }))

    // Charger les écritures comptables (toutes par défaut)
    await chargerEcrituresComptables()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors du chargement des données')
  }
  finally {
    chargement.value = false
  }
}

async function chargerEcrituresComptables() {
  if (!authStore.userInfo.userId) return

  try {
    if (proprieteSelectionnee.value && proprieteSelectionnee.value !== 'all') {
      // Si une propriété est sélectionnée, charger les écritures de cette propriété
      const ecritures = await fetchEcrituresComptables(proprieteSelectionnee.value, anneeFiscale.value)
      ecrituresComptables.value = ecritures
    }
    else {
      // Si aucune propriété n'est sélectionnée, charger toutes les écritures de l'utilisateur
      const ecritures = await getEcrituresComptablesWithProprieteNom(authStore.userInfo.userId)
      ecrituresComptables.value = ecritures
    }
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors du chargement des écritures comptables')
  }
}

function nouvelleCharge() {
  modeEdition.value = false
  chargeEnCours.value = {
    intitule: '',
    montant: '0',
    dateCharge: new Date().toISOString().split('T')[0],
    proprieteId: proprieteSelectionnee.value || '',
    nature: '',
    commentaire: '',
  }
  modalChargeVisible.value = true
}

function editerCharge(charge: ChargeDTO) {
  modeEdition.value = true
  chargeEnCours.value = { ...charge }
  modalChargeVisible.value = true
}

async function sauvegarderCharge() {
  if (!chargeEnCours.value.intitule || !chargeEnCours.value.montant || !chargeEnCours.value.dateCharge || !chargeEnCours.value.proprieteId || !chargeEnCours.value.nature) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  try {
    uploadEnCours.value = true
    
    // Upload du document si un fichier est sélectionné
    if (fichierCharge.value) {
      const documentUploaded = await uploadDocument(
        fichierCharge.value,
        `Document - ${chargeEnCours.value.intitule}`,
        authStore.userInfo.userId,
        chargeEnCours.value.proprieteId,
        'JUSTIFICATIF'
      )
      chargeEnCours.value.documentId = documentUploaded.id
    }

    if (modeEdition.value && chargeEnCours.value.id) {
      await updateCharge(chargeEnCours.value.id, chargeEnCours.value)
      message.success('Charge mise à jour avec succès')
    }
    else {
      await createCharge({
        ...chargeEnCours.value,
        utilisateurId: authStore.userInfo.userId,
      })
      message.success('Charge créée avec succès')
    }

    modalChargeVisible.value = false
    fichierCharge.value = null
    await chargerDonnees()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors de la sauvegarde')
  }
  finally {
    uploadEnCours.value = false
  }
}

async function supprimerCharge(id: string) {
  try {
    await deleteCharge(id)
    message.success('Charge supprimée avec succès')
    await chargerDonnees()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors de la suppression')
  }
}

function nouvelleRecette() {
  modeEdition.value = false
  recetteEnCours.value = {
    intitule: '',
    montant: '0',
    dateRecette: new Date().toISOString().split('T')[0],
    proprieteId: proprieteSelectionnee.value || '',
    type: 'LOCATION',
    commentaire: '',
  }
  modalRecetteVisible.value = true
}

function editerRecette(recette: RecetteDTO) {
  modeEdition.value = true
  recetteEnCours.value = { ...recette }
  modalRecetteVisible.value = true
}

async function sauvegarderRecette() {
  if (!recetteEnCours.value.intitule || !recetteEnCours.value.montant || !recetteEnCours.value.dateRecette || !recetteEnCours.value.proprieteId || !recetteEnCours.value.type) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  try {
    uploadEnCours.value = true
    
    // Upload du document si un fichier est sélectionné
    if (fichierRecette.value) {
      const documentUploaded = await uploadDocument(
        fichierRecette.value,
        `Document - ${recetteEnCours.value.intitule}`,
        authStore.userInfo.userId,
        recetteEnCours.value.proprieteId,
        'JUSTIFICATIF'
      )
      recetteEnCours.value.documentId = documentUploaded.id
    }

    if (modeEdition.value && recetteEnCours.value.id) {
      await updateRecette(recetteEnCours.value.id, recetteEnCours.value)
      message.success('Recette mise à jour avec succès')
    }
    else {
      await createRecette({
        ...recetteEnCours.value,
        utilisateurId: authStore.userInfo.userId,
      })
      message.success('Recette créée avec succès')
    }

    modalRecetteVisible.value = false
    fichierRecette.value = null
    await chargerDonnees()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors de la sauvegarde')
  }
  finally {
    uploadEnCours.value = false
  }
}

async function supprimerRecette(id: string) {
  try {
    await deleteRecette(id)
    message.success('Recette supprimée')
    await chargerDonnees()
  } catch (error: any) {
    message.error(error.message || 'Erreur lors de la suppression')
  }
}

function editerEcriture(ecriture: EcritureComptableDTO) {
  modeEdition.value = true
  ecritureEnCours.value = { ...ecriture }
  modalEcritureVisible.value = true
}

async function supprimerEcriture(id: string) {
  try {
    await deleteEcritureComptable(id)
    message.success('Écriture comptable supprimée')
    await chargerDonnees()
  } catch (error: any) {
    message.error(error.message || 'Erreur lors de la suppression')
  }
}

async function sauvegarderEcriture() {
  if (!ecritureEnCours.value.dateEcriture || !ecritureEnCours.value.montant) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  try {
    if (modeEdition.value) {
      await updateEcritureComptable(ecritureEnCours.value)
      message.success('Écriture comptable modifiée')
    }
    modalEcritureVisible.value = false
    await chargerDonnees()
  } catch (error: any) {
    message.error(error.message || 'Erreur lors de la sauvegarde')
  }
}

const loadEcrituresComptables = async () => {
  try {
    chargement.value = true
    if (proprieteSelectionnee.value && proprieteSelectionnee.value !== 'all') {
      // Charger les écritures pour une propriété spécifique
      const anneeCourante = new Date().getFullYear()
      ecrituresComptables.value = await getEcrituresComptablesWithProprieteNom(authStore.userInfo.userId)
    } else {
      // Charger toutes les écritures avec les noms de propriétés
      ecrituresComptables.value = await getEcrituresComptablesWithProprieteNom(authStore.userInfo.userId)
    }
  } catch (error) {
    console.error('Erreur lors du chargement des écritures comptables:', error)
    message.error('Erreur lors du chargement des écritures comptables')
  } finally {
    chargement.value = false
  }
}

const loadCharges = async () => {
  try {
    chargement.value = true
    if (proprieteSelectionnee.value && proprieteSelectionnee.value !== 'all') {
      charges.value = await getChargesWithProprieteNom(authStore.userInfo.userId)
    } else {
      charges.value = await getChargesWithProprieteNom(authStore.userInfo.userId)
    }
  } catch (error) {
    console.error('Erreur lors du chargement des charges:', error)
    message.error('Erreur lors du chargement des charges')
  } finally {
    chargement.value = false
  }
}

const loadRecettes = async () => {
  try {
    chargement.value = true
    if (proprieteSelectionnee.value && proprieteSelectionnee.value !== 'all') {
      recettes.value = await getRecettesWithProprieteNom(authStore.userInfo.userId)
    } else {
      recettes.value = await getRecettesWithProprieteNom(authStore.userInfo.userId)
    }
  } catch (error) {
    console.error('Erreur lors du chargement des recettes:', error)
    message.error('Erreur lors du chargement des recettes')
  } finally {
    chargement.value = false
  }
}

async function handleDownloadDocument(documentId: string, documentNom: string) {
  try {
    // Récupérer les métadonnées du document pour avoir le nom de fichier correct
    const metadataResponse = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/documents/${documentId}`,
      {
        method: 'GET',
        credentials: 'include',
      }
    )

    if (!metadataResponse.ok) {
      throw new Error('Erreur lors de la récupération des métadonnées du document')
    }

    const metadata = await metadataResponse.json()
    const fileName = metadata.nomFichier || documentNom || 'document'

    const blob = await downloadDocument(documentId)
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = fileName
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(url)
    document.body.removeChild(a)
    message.success('Document téléchargé avec succès')
  } catch (error) {
    console.error('Erreur lors du téléchargement du document:', error)
    message.error('Erreur lors du téléchargement du document')
  }
}

// Fonction pour valider la taille du fichier
function validateFileSize(file: File): boolean {
  if (file.size > maxFileSize) {
    message.error(`Le fichier est trop volumineux. Taille maximum autorisée : 10MB. Taille actuelle : ${formatFileSize(file.size)}`)
    return false
  }
  return true
}

// Fonction pour formater la taille du fichier
function formatFileSize(bytes: number): string {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
  return (bytes / (1024 * 1024 * 1024)).toFixed(1) + ' GB'
}

// Fonction pour gérer la sélection de fichier pour les charges
function handleFileSelectCharge(event: Event) {
  const target = event.target as HTMLInputElement
  if (target.files && target.files[0]) {
    const file = target.files[0]
    if (validateFileSize(file)) {
      fichierCharge.value = file
    } else {
      // Réinitialiser l'input
      target.value = ''
    }
  }
}

// Fonction pour gérer la sélection de fichier pour les recettes
function handleFileSelectRecette(event: Event) {
  const target = event.target as HTMLInputElement
  if (target.files && target.files[0]) {
    const file = target.files[0]
    if (validateFileSize(file)) {
      fichierRecette.value = file
    } else {
      // Réinitialiser l'input
      target.value = ''
    }
  }
}

// Chargement initial
onMounted(() => {
  chargerDonnees()
})
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-2xl font-bold mb-2">Comptabilité - Charges et Recettes</h1>
          <p class="text-gray-600">Gestion des charges déductibles et des recettes locatives</p>
        </div>
        <NSpace>
          <NButton type="primary" @click="nouvelleCharge">
            <template #icon>
              <NIcon :component="Add24Filled" />
            </template>
            Nouvelle Charge
          </NButton>
          <NButton type="success" @click="nouvelleRecette">
            <template #icon>
              <NIcon :component="Money24Filled" />
            </template>
            Nouvelle Recette
          </NButton>
        </NSpace>
      </div>

      <!-- Résumé financier -->
      <NCard title="Résumé financier" class="mb-6">
        <NGrid :cols="3" :x-gap="16">
          <NGi>
            <div class="text-center p-4 bg-red-50 rounded-lg">
              <div class="text-2xl font-bold text-red-600">{{ totalCharges.toFixed(2) }} €</div>
              <div class="text-sm text-gray-600">Total Charges</div>
            </div>
          </NGi>
          <NGi>
            <div class="text-center p-4 bg-green-50 rounded-lg">
              <div class="text-2xl font-bold text-green-600">{{ totalRecettes.toFixed(2) }} €</div>
              <div class="text-sm text-gray-600">Total Recettes</div>
            </div>
          </NGi>
          <NGi>
            <div class="text-center p-4 rounded-lg" :class="solde >= 0 ? 'bg-blue-50' : 'bg-orange-50'">
              <div class="text-2xl font-bold" :class="solde >= 0 ? 'text-blue-600' : 'text-orange-600'">
                {{ solde.toFixed(2) }} €
              </div>
              <div class="text-sm text-gray-600">Solde</div>
            </div>
          </NGi>
        </NGrid>
      </NCard>

      <!-- Onglets -->
      <NTabs type="line" animated>
        <NTabPane name="charges" tab="Charges">
          <NDataTable
            :columns="colonnesCharges"
            :data="charges"
            :loading="chargement"
            :pagination="{ pageSize: 10 }"
            striped
          />
        </NTabPane>

        <NTabPane name="recettes" tab="Recettes">
          <NDataTable
            :columns="colonnesRecettes"
            :data="recettes"
            :loading="chargement"
            :pagination="{ pageSize: 10 }"
            striped
          />
        </NTabPane>

        <NTabPane name="ecritures" tab="Écritures Comptables">
          <div class="mb-4">
            <NSpace>
              <NSelect
                v-model:value="proprieteSelectionnee"
                placeholder="Sélectionner une propriété"
                :options="[
                  { label: 'Toutes les propriétés', value: '' },
                  ...proprietes
                ]"
                style="width: 250px"
                @update:value="loadEcrituresComptables"
              />
              <NInputNumber
                v-model:value="anneeFiscale"
                placeholder="Année fiscale"
                :min="2020"
                :max="2030"
                @update:value="loadEcrituresComptables"
              />
            </NSpace>
          </div>

          <NDataTable
            :columns="colonnesEcritures"
            :data="ecrituresComptables"
            :loading="chargement"
            :pagination="{ pageSize: 10 }"
            striped
          />
        </NTabPane>
      </NTabs>
    </NCard>

    <!-- Modal Charge -->
    <NModal v-model:show="modalChargeVisible" preset="card" title="Charge" style="width: 600px">
      <NForm :model="chargeEnCours" label-placement="top">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Intitulé" required>
              <NInput v-model:value="chargeEnCours.intitule" placeholder="Ex: Réparation chaudière" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant (€)" required>
              <NInput
                v-model:value="chargeEnCours.montant"
                placeholder="0.00"
                type="text"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Date" required>
              <NInput v-model:value="chargeEnCours.dateCharge" type="text" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Nature" required>
              <NSelect
                v-model:value="chargeEnCours.nature"
                :options="naturesCharges"
                placeholder="Sélectionner une nature"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Propriété" required>
              <NSelect
                v-model:value="chargeEnCours.proprieteId"
                :options="proprietes"
                placeholder="Sélectionner une propriété"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Document associé">
              <NSelect
                v-model:value="chargeEnCours.documentId"
                :options="[
                  { label: 'Aucun document', value: '' },
                  ...documents
                ]"
                placeholder="Sélectionner un document (optionnel)"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Document (optionnel)">
              <input
                type="file"
                @change="handleFileSelectCharge"
                accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                class="w-full p-2 border border-gray-300 rounded"
              />
              <div class="text-sm text-gray-500 mt-1">
                Formats acceptés : PDF, JPG, PNG, DOC, DOCX. Taille maximum : 10MB
              </div>
              <div v-if="fichierCharge" class="text-sm text-green-600 mt-1">
                Fichier sélectionné : {{ fichierCharge.name }} ({{ formatFileSize(fichierCharge.size) }})
              </div>
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Commentaire">
              <NInput
                v-model:value="chargeEnCours.commentaire"
                type="textarea"
                placeholder="Commentaire optionnel"
                :rows="3"
              />
            </NFormItem>
          </NGi>
        </NGrid>
      </NForm>

      <template #footer>
        <NSpace justify="end">
          <NButton @click="modalChargeVisible = false">Annuler</NButton>
          <NButton type="primary" :loading="uploadEnCours" @click="sauvegarderCharge">
            {{ modeEdition ? 'Mettre à jour' : 'Créer' }}
          </NButton>
        </NSpace>
      </template>
    </NModal>

    <!-- Modal Recette -->
    <NModal v-model:show="modalRecetteVisible" preset="card" title="Recette" style="width: 600px">
      <NForm :model="recetteEnCours" label-placement="top">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Intitulé" required>
              <NInput v-model:value="recetteEnCours.intitule" placeholder="Ex: Loyer janvier 2024" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant (€)" required>
              <NInput
                v-model:value="recetteEnCours.montant"
                placeholder="0.00"
                type="text"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Date" required>
              <NInput v-model:value="recetteEnCours.dateRecette" type="text" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Type" required>
              <NSelect
                v-model:value="recetteEnCours.type"
                :options="typesRecettes"
                placeholder="Sélectionner un type"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Propriété" required>
              <NSelect
                v-model:value="recetteEnCours.proprieteId"
                :options="proprietes"
                placeholder="Sélectionner une propriété"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Document associé">
              <NSelect
                v-model:value="recetteEnCours.documentId"
                :options="[
                  { label: 'Aucun document', value: '' },
                  ...documents
                ]"
                placeholder="Sélectionner un document (optionnel)"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Document (optionnel)">
              <input
                type="file"
                @change="handleFileSelectRecette"
                accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                class="w-full p-2 border border-gray-300 rounded"
              />
              <div class="text-sm text-gray-500 mt-1">
                Formats acceptés : PDF, JPG, PNG, DOC, DOCX. Taille maximum : 10MB
              </div>
              <div v-if="fichierRecette" class="text-sm text-green-600 mt-1">
                Fichier sélectionné : {{ fichierRecette.name }} ({{ formatFileSize(fichierRecette.size) }})
              </div>
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Commentaire">
              <NInput
                v-model:value="recetteEnCours.commentaire"
                type="textarea"
                placeholder="Commentaire optionnel"
                :rows="3"
              />
            </NFormItem>
          </NGi>
        </NGrid>
      </NForm>

      <template #footer>
        <NSpace justify="end">
          <NButton @click="modalRecetteVisible = false">Annuler</NButton>
          <NButton type="primary" :loading="uploadEnCours" @click="sauvegarderRecette">
            {{ modeEdition ? 'Mettre à jour' : 'Créer' }}
          </NButton>
        </NSpace>
      </template>
    </NModal>

    <!-- Modal Écriture -->
    <NModal v-model:show="modalEcritureVisible" preset="card" title="Écriture Comptable" style="width: 600px">
      <NForm :model="ecritureEnCours" label-placement="top">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Date" required>
              <NInput v-model:value="ecritureEnCours.dateEcriture" type="text" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant" required>
              <NInput
                v-model:value="ecritureEnCours.montant"
                placeholder="0.00"
                type="text"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Type" required>
              <NSelect
                v-model:value="ecritureEnCours.type"
                :options="[
                  { label: 'Charge', value: 'CHARGE' },
                  { label: 'Recette', value: 'RECETTE' }
                ]"
                placeholder="Sélectionner un type"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Propriété" required>
              <NSelect
                v-model:value="ecritureEnCours.proprieteId"
                :options="proprietes"
                placeholder="Sélectionner une propriété"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Description">
              <NInput
                v-model:value="ecritureEnCours.commentaire"
                type="textarea"
                placeholder="Description de l'écriture"
                :rows="3"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Document associé">
              <NSelect
                v-model:value="ecritureEnCours.documentId"
                :options="[
                  { label: 'Aucun document', value: '' },
                  ...documents
                ]"
                placeholder="Sélectionner un document (optionnel)"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Document (optionnel)">
              <input
                type="file"
                @change="handleFileSelectCharge"
                accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                class="w-full p-2 border border-gray-300 rounded"
              />
              <div class="text-sm text-gray-500 mt-1">
                Formats acceptés : PDF, JPG, PNG, DOC, DOCX. Taille maximum : 10MB
              </div>
              <div v-if="fichierCharge" class="text-sm text-green-600 mt-1">
                Fichier sélectionné : {{ fichierCharge.name }} ({{ formatFileSize(fichierCharge.size) }})
              </div>
            </NFormItem>
          </NGi>
        </NGrid>
      </NForm>

      <template #footer>
        <NSpace justify="end">
          <NButton @click="modalEcritureVisible = false">Annuler</NButton>
          <NButton type="primary" :loading="uploadEnCours" @click="sauvegarderEcriture">
            {{ modeEdition ? 'Mettre à jour' : 'Créer' }}
          </NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.items-center {
  align-items: center;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.mb-4 {
  margin-bottom: 1rem;
}

.mb-6 {
  margin-bottom: 1.5rem;
}

.text-2xl {
  font-size: 1.5rem;
}

.font-bold {
  font-weight: bold;
}

.text-gray-600 {
  color: #6b7280;
}

.text-center {
  text-align: center;
}

.p-4 {
  padding: 1rem;
}

.bg-red-50 {
  background-color: #fef2f2;
}

.bg-green-50 {
  background-color: #f0fdf4;
}

.bg-blue-50 {
  background-color: #eff6ff;
}

.bg-orange-50 {
  background-color: #fff7ed;
}

.text-red-600 {
  color: #dc2626;
}

.text-green-600 {
  color: #16a34a;
}

.text-blue-600 {
  color: #2563eb;
}

.text-orange-600 {
  color: #ea580c;
}

.rounded-lg {
  border-radius: 0.5rem;
}

.text-sm {
  font-size: 0.875rem;
}
</style> 