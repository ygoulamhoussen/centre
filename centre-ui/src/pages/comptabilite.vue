<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useAppStore } from '@/store/modules/app'
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
  NTag,
  NText,
  useMessage,
  NDatePicker,
  NH1,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import {
  Add24Filled,
  Calculator24Filled,
  Delete24Filled,
  Edit24Filled,
  ArrowDownload24Filled,
  Money24Filled,
} from '@vicons/fluent'
import { computed, onMounted, ref, watch } from 'vue'
import type { ChargeDTO, EcritureComptableDTO, RecetteDTO } from '@/types/dto'
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
  getChargesWithProprieteNom,
  getEcrituresComptablesWithProprieteNom,
  getRecettesWithProprieteNom,
  downloadDocument,
  updateEcritureComptable,
  deleteEcritureComptable,
} from '@/service/api/charges-recettes'
import { useRouter } from 'vue-router'
import { naturesCharges, typesRecettes } from '@/constants/compta'

definePage({
  meta: {
    title: 'Comptabilité',
    icon: 'material-symbols:calculate',
    order: 9,
  },
})

const authStore = useAuthStore()
const message = useMessage()
const router = useRouter()
const appStore = useAppStore()
const isMobile = appStore.isMobile

// États réactifs
const charges = ref<ChargeDTO[]>([])
const recettes = ref<RecetteDTO[]>([])
const ecrituresComptables = ref<EcritureComptableDTO[]>([])
const documents = ref<Array<{ label: string; value: string }>>([])
const proprietes = ref<Array<{ label: string; value: string }>>([])
const anneeFiscale = ref(new Date().getFullYear())
const proprieteSelectionnee = ref<string>('')
const chargement = ref(false)

// États pour l'upload de fichiers
const fichierCharge = ref<File | null>(null)
const fichierRecette = ref<File | null>(null)
const uploadEnCours = ref(false)
const maxFileSize = 10 * 1024 * 1024 // 10MB

// Type local pour les amortissements (à placer avec les autres types)
type AmortissementDTO = {
  id?: string
  annee: number
  proprieteNom: string
  immobilisationIntitule: string
  montantAmortissement: string
}

// Ajout pour les amortissements
const amortissements = ref<AmortissementDTO[]>([])

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
      return h(NTag, { type: color }, { default: () => type })
    },
  },
  {
    title: 'Montant',
    key: 'montant',
    width: 120,
    render: (row: EcritureComptableDTO) => {
      const montant = Number.parseFloat(row.montant)
      const color = row.type === 'CHARGE' ? 'error' : 'success'
      return h('span', { style: { color: color === 'error' ? '#ff4d4f' : '#52c41a', fontWeight: 'bold' } }, `${montant.toFixed(2)} €`)
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

// Colonnes pour les amortissements
const colonnesAmortissements = [
  { title: 'Année', key: 'annee', width: 100 },
  { title: 'Propriété', key: 'proprieteNom', width: 200 },
  { title: 'Immobilisation', key: 'immobilisationIntitule', width: 200 },
  { title: 'Montant', key: 'montantAmortissement', width: 120, render: (row: AmortissementDTO) => `${row.montantAmortissement} €` },
]

// Calculs
const totalCharges = computed(() => {
  return charges.value.reduce((sum, charge) => sum + Number.parseFloat(charge.montant), 0)
})

const totalRecettes = computed(() => {
  return recettes.value.reduce((sum, recette) => sum + Number.parseFloat(recette.montant), 0)
})

const solde = computed(() => totalRecettes.value - totalCharges.value)

// Calculs groupés par année
const anneesDisponibles = computed(() => {
  const anneesCharges = charges.value.map(c => c.dateCharge ? new Date(c.dateCharge).getFullYear() : null)
  const anneesAmortissements = amortissements.value.map(a => a.annee)
  const anneesRecettes = recettes.value.map(r => r.dateRecette ? new Date(r.dateRecette).getFullYear() : null)
  return Array.from(new Set([...anneesCharges, ...anneesAmortissements, ...anneesRecettes].filter(a => !!a))).sort((a, b) => b - a)
})

const anneeSelectionnee = ref<number | null>(null)

// Années disponibles pour les écritures (on peut réutiliser anneesDisponibles ou en calculer un spécifique si besoin)
const anneesEcrituresDisponibles = computed(() => anneesDisponibles.value.filter(a => a !== null))

onMounted(() => {
  chargerDonnees().then(() => {
    const currentYear = new Date().getFullYear()
    if (anneesDisponibles.value.includes(currentYear)) {
      anneeSelectionnee.value = currentYear
    } else if (anneesDisponibles.value.length > 0) {
      anneeSelectionnee.value = anneesDisponibles.value[0]
    }
  })
})

function totalParAnnee(type: 'charges' | 'amortissements' | 'recettes', annee: number) {
  if (type === 'charges') {
    return charges.value.filter(c => c.dateCharge && new Date(c.dateCharge).getFullYear() === annee)
      .reduce((sum, c) => sum + Number.parseFloat(c.montant), 0)
  }
  if (type === 'amortissements') {
    return amortissements.value.filter(a => a.annee === annee)
      .reduce((sum, a) => sum + Number.parseFloat(a.montantAmortissement), 0)
  }
  if (type === 'recettes') {
    return recettes.value.filter(r => r.dateRecette && new Date(r.dateRecette).getFullYear() === annee)
      .reduce((sum, r) => sum + Number.parseFloat(r.montant), 0)
  }
  return 0
}

function soldeParAnnee(annee: number) {
  return totalParAnnee('recettes', annee) - totalParAnnee('charges', annee) - totalParAnnee('amortissements', annee)
}

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

    charges.value = (chargesData as any[])
      .filter((c: any) => c.nature !== 'AMORTISSEMENT')
      .map((c: any) => ({
        id: c.id,
        intitule: c.intitule ?? '',
        montant: c.montant?.toString() ?? '',
        dateCharge: c.dateCharge ?? '',
        proprieteId: c.proprieteId ?? '',
        proprieteNom: c.proprieteNom ?? proprietes.value.find(p => p.value === c.proprieteId)?.label ?? '',
        nature: c.nature ?? '',
        commentaire: c.commentaire,
        utilisateurId: c.utilisateurId,
        documentId: c.documentId,
        documentNom: c.documentNom,
        createdAt: c.createdAt,
        updatedAt: c.updatedAt,
      }))
    recettes.value = (recettesData as any[]).map((r: any) => ({
      id: r.id,
      intitule: r.intitule ?? '',
      montant: r.montant?.toString() ?? '',
      dateRecette: r.dateRecette ?? '',
      proprieteId: r.proprieteId ?? '',
      type: r.type ?? '',
      commentaire: r.commentaire,
      utilisateurId: r.utilisateurId,
      documentId: r.documentId,
      documentNom: r.documentNom,
      createdAt: r.createdAt,
      updatedAt: r.updatedAt,
    }))

    // Charger les amortissements (API à adapter si besoin)
    const annee = anneeSelectionnee.value || new Date().getFullYear()
    const utilisateurId = authStore.userInfo.userId
    const amortissementsResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/amortissements/${utilisateurId}/${annee}`)
    if (amortissementsResponse.ok) {
      const data = await amortissementsResponse.json()
      amortissements.value = (data as any[]).map((a: any) => ({
        id: a.id,
        annee: a.annee ? parseInt(a.annee) : 0,
        proprieteNom: '', // Le DTO ne contient pas ce champ, à compléter si besoin
        immobilisationIntitule: a.immobilisationIntitule ?? '',
        montantAmortissement: a.montantAmortissement ?? '',
      }))
    } else {
      // Si pas d'API dédiée, on extrait les amortissements des charges
      amortissements.value = (chargesData as any[])
        .filter((c: any) => c.nature === 'AMORTISSEMENT')
        .map((c: any) => ({
          id: c.id,
          annee: c.dateCharge ? new Date(c.dateCharge).getFullYear() : 0,
          proprieteNom: c.proprieteNom ?? '',
          immobilisationIntitule: c.commentaire || '',
          montantAmortissement: c.montant?.toString() ?? '',
        }))
    }

    // Charger les propriétés
    const propResponse = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${authStore.userInfo.userId}`)
    let proprietesMap: Record<string, string> = {}
    if (propResponse.ok) {
      const proprietesData = await propResponse.json()
      proprietes.value = proprietesData.map((prop: any) => ({
        label: prop.nom,
        value: prop.id,
      }))
      proprietesMap = Object.fromEntries(proprietesData.map((prop: any) => [prop.id, prop.nom]))
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
  if (!authStore.userInfo.userId || !anneeSelectionnee.value) return
  try {
    if (proprieteSelectionnee.value && proprieteSelectionnee.value !== 'all') {
      const ecritures = await fetchEcrituresComptables(proprieteSelectionnee.value, anneeSelectionnee.value)
      ecrituresComptables.value = (ecritures as any[]).map((e: any) => ({
        id: e.id,
        dateEcriture: e.dateEcriture ?? '',
        montant: e.montant !== undefined && e.montant !== null ? e.montant.toString() : '',
        type: e.type ?? '',
        proprieteId: e.proprieteId ?? '',
        chargeId: e.chargeId,
        recetteId: e.recetteId,
        commentaire: e.commentaire,
        documentId: e.documentId,
        documentNom: e.documentNom,
        createdAt: e.createdAt,
        updatedAt: e.updatedAt,
        proprieteNom: e.proprieteNom,
      })) as EcritureComptableDTO[]
    }
    else {
      const ecritures = await fetch(
        `${import.meta.env.VITE_SERVICE_BASE_URL}/api/ecritures-comptables/utilisateur/${authStore.userInfo.userId}/${anneeSelectionnee.value}`
      ).then(res => res.json())
      ecrituresComptables.value = (ecritures as any[]).map((e: any) => ({
        id: e.id,
        dateEcriture: e.dateEcriture ?? '',
        montant: e.montant !== undefined && e.montant !== null ? e.montant.toString() : '',
        type: e.type ?? '',
        proprieteId: e.proprieteId ?? '',
        chargeId: e.chargeId,
        recetteId: e.recetteId,
        commentaire: e.commentaire,
        documentId: e.documentId,
        documentNom: e.documentNom,
        createdAt: e.createdAt,
        updatedAt: e.updatedAt,
        proprieteNom: e.proprieteNom,
      })) as EcritureComptableDTO[]
    }
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors du chargement des écritures comptables')
  }
}

watch([anneeSelectionnee, proprieteSelectionnee], () => {
  if (anneeSelectionnee.value) {
    chargerEcrituresComptables()
  }
})

function nouvelleCharge() {
  chargeEnCours.value = {}
  fichierCharge.value = null
  modeEdition.value = false
  modalChargeVisible.value = true
}

function editerCharge(charge: ChargeDTO) {
  chargeEnCours.value = { ...charge }
  fichierCharge.value = null
  modeEdition.value = true
  modalChargeVisible.value = true
}

async function sauvegarderCharge() {
  if (!chargeEnCours.value.intitule || !chargeEnCours.value.montant || !chargeEnCours.value.dateCharge || !chargeEnCours.value.proprieteId || !chargeEnCours.value.nature) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  // Conversion dateCharge en string yyyy-MM-dd si c'est un timestamp
  if (typeof chargeEnCours.value.dateCharge === 'number') {
    const d = new Date(chargeEnCours.value.dateCharge)
    chargeEnCours.value.dateCharge = d.toISOString().slice(0, 10)
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
  recetteEnCours.value = {}
  fichierRecette.value = null
  modeEdition.value = false
  modalRecetteVisible.value = true
}

function editerRecette(recette: RecetteDTO) {
  recetteEnCours.value = { ...recette }
  fichierRecette.value = null
  modeEdition.value = true
  modalRecetteVisible.value = true
}

async function sauvegarderRecette() {
  if (!recetteEnCours.value.intitule || !recetteEnCours.value.montant || !recetteEnCours.value.dateRecette || !recetteEnCours.value.proprieteId || !recetteEnCours.value.type) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  // Conversion dateRecette en string yyyy-MM-dd si c'est un timestamp
  if (typeof recetteEnCours.value.dateRecette === 'number') {
    const d = new Date(recetteEnCours.value.dateRecette)
    recetteEnCours.value.dateRecette = d.toISOString().slice(0, 10)
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
      const data = await getChargesWithProprieteNom(authStore.userInfo.userId)
      charges.value = (data as any[]).map((c: any) => ({
        id: c.id,
        intitule: c.intitule ?? '',
        montant: c.montant?.toString() ?? '',
        dateCharge: c.dateCharge ?? '',
        proprieteId: c.proprieteId ?? '',
        nature: c.nature ?? '',
        commentaire: c.commentaire,
        utilisateurId: c.utilisateurId,
        documentId: c.documentId,
        documentNom: c.documentNom,
        createdAt: c.createdAt,
        updatedAt: c.updatedAt,
      }))
    } else {
      const data = await getChargesWithProprieteNom(authStore.userInfo.userId)
      charges.value = (data as any[]).map((c: any) => ({
        id: c.id,
        intitule: c.intitule ?? '',
        montant: c.montant?.toString() ?? '',
        dateCharge: c.dateCharge ?? '',
        proprieteId: c.proprieteId ?? '',
        nature: c.nature ?? '',
        commentaire: c.commentaire,
        utilisateurId: c.utilisateurId,
        documentId: c.documentId,
        documentNom: c.documentNom,
        createdAt: c.createdAt,
        updatedAt: c.updatedAt,
      }))
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
      const data = await getRecettesWithProprieteNom(authStore.userInfo.userId)
      recettes.value = (data as any[]).map((r: any) => ({
        id: r.id,
        intitule: r.intitule ?? '',
        montant: r.montant?.toString() ?? '',
        dateRecette: r.dateRecette ?? '',
        proprieteId: r.proprieteId ?? '',
        type: r.type ?? '',
        commentaire: r.commentaire,
        utilisateurId: r.utilisateurId,
        documentId: r.documentId,
        documentNom: r.documentNom,
        createdAt: r.createdAt,
        updatedAt: r.updatedAt,
      }))
    } else {
      const data = await getRecettesWithProprieteNom(authStore.userInfo.userId)
      recettes.value = (data as any[]).map((r: any) => ({
        id: r.id,
        intitule: r.intitule ?? '',
        montant: r.montant?.toString() ?? '',
        dateRecette: r.dateRecette ?? '',
        proprieteId: r.proprieteId ?? '',
        type: r.type ?? '',
        commentaire: r.commentaire,
        utilisateurId: r.utilisateurId,
        documentId: r.documentId,
        documentNom: r.documentNom,
        createdAt: r.createdAt,
        updatedAt: r.updatedAt,
      }))
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
    message.error(`Le fichier est trop volumineux. Taille maximum autorisée : 10MB. Taille actuelle : ${formatFileSize(file.size) }`)
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

// Variables réactives manquantes pour les formulaires
const chargeEnCours = ref<Partial<ChargeDTO>>({})
const recetteEnCours = ref<Partial<RecetteDTO>>({})
const modeEdition = ref(false)
const modalChargeVisible = ref(false)
const modalRecetteVisible = ref(false)
const ecritureEnCours = ref<Partial<EcritureComptableDTO>>({})
const modalEcritureVisible = ref(false)

// Chargement initial
onMounted(() => {
  chargerDonnees()
})

async function editerJournalComptable() {
  try {
    const userId = authStore.userInfo.userId
    const annee = anneeSelectionnee.value
    const proprieteId = proprieteSelectionnee.value && proprieteSelectionnee.value !== 'all' ? proprieteSelectionnee.value : null
    let url = ''
    if (proprieteId) {
      url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/journal-comptable/pdf?proprieteId=${proprieteId}&annee=${annee}`
    } else {
      url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/journal-comptable/pdf?utilisateurId=${userId}&annee=${annee}`
    }
    const response = await fetch(url, { method: 'GET', credentials: 'include' })
    if (!response.ok) throw new Error('Erreur lors de la génération du PDF')
    const blob = await response.blob()
    const urlBlob = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = urlBlob
    a.download = `journal-comptable-${annee}.pdf`
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(urlBlob)
    document.body.removeChild(a)
    message.success('PDF du journal comptable généré et téléchargé')
  } catch (error) {
    message.error('Erreur lors de la génération du PDF du journal comptable')
  }
}

// Ajout des computed pour filtrer selon l'année sélectionnée
const chargesFiltrees = computed(() => {
  if (!anneeSelectionnee.value) return []
  return charges.value.filter(c => c.dateCharge && new Date(c.dateCharge).getFullYear() === anneeSelectionnee.value)
})
const recettesFiltrees = computed(() => {
  if (!anneeSelectionnee.value) return []
  return recettes.value.filter(r => r.dateRecette && new Date(r.dateRecette).getFullYear() === anneeSelectionnee.value)
})
const amortissementsFiltres = computed(() => {
  if (!anneeSelectionnee.value) return []
  return amortissements.value.filter(a => a.annee === anneeSelectionnee.value)
})

// Fonction utilitaire pour obtenir le nom de la propriété
function getProprieteNom(obj: any) {
  if (obj.proprieteNom) return obj.proprieteNom
  if (obj.proprieteId && Array.isArray(proprietes.value)) {
    const found = proprietes.value.find(p => p.value === obj.proprieteId)
    return found ? found.label : ''
  }
  return ''
}
</script>

<template>
  <div class="p-4 compta-bg">
    <NCard :bordered="false">
      <div class="page-header mb-6">
        <div v-if="!isMobile" class="flex items-center justify-between">
          <div>
            <NH1 class="titre-principal text-2xl mb-2">Comptabilité - Charges et Recettes</NH1>
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
        <div v-else class="mobile-header">
          <NH1 class="titre-principal mobile-title">Comptabilité</NH1>
          <NButton block size="small" type="primary" class="mobile-journal-btn" @click="editerJournalComptable">
            <template #icon>
              <NIcon :component="ArrowDownload24Filled" />
            </template>
            Éditer le journal comptable
          </NButton>
          <div class="mobile-actions">
            <NButton size="small" type="primary" @click="nouvelleCharge">
              <template #icon>
                <NIcon :component="Add24Filled" />
              </template>
              Charge
            </NButton>
            <NButton size="small" type="success" @click="nouvelleRecette">
              <template #icon>
                <NIcon :component="Money24Filled" />
              </template>
              Recette
            </NButton>
          </div>
        </div>
      </div>
      <div class="annee-row-wrapper">
        <div class="annee-row">
          <span class="label-annee">Année :</span>
          <NSelect
            v-model:value="anneeSelectionnee"
            :options="anneesDisponibles.map(a => ({ label: a ? a.toString() : '', value: a }))"
            placeholder="Choisir une année"
            style="width: 120px"
          />
        </div>
      </div>
      <NCard v-if="!isMobile" title="Résumé financier" class="mb-6">
        <template #header-extra>
          <NButton type="primary" @click="editerJournalComptable">
            <template #icon>
              <NIcon :component="ArrowDownload24Filled" />
            </template>
            Éditer le journal comptable
          </NButton>
        </template>
        <NGrid :cols="4" :x-gap="16">
          <NGi>
            <NCard class="resume-card charge">
              <div class="resume-label">Total Charges</div>
              <div class="resume-value">{{ totalParAnnee('charges', anneeSelectionnee ?? 0).toFixed(2) }} €</div>
            </NCard>
          </NGi>
          <NGi>
            <NCard class="resume-card amortissement">
              <div class="resume-label">Total Amortissements</div>
              <div class="resume-value">{{ totalParAnnee('amortissements', anneeSelectionnee ?? 0).toFixed(2) }} €</div>
            </NCard>
          </NGi>
          <NGi>
            <NCard class="resume-card recette">
              <div class="resume-label">Total Recettes</div>
              <div class="resume-value">{{ totalParAnnee('recettes', anneeSelectionnee ?? 0).toFixed(2) }} €</div>
            </NCard>
          </NGi>
          <NGi>
            <NCard class="resume-card solde" :class="soldeParAnnee(anneeSelectionnee ?? 0) >= 0 ? 'positif' : 'negatif'">
              <div class="resume-label">Solde</div>
              <div class="resume-value">{{ soldeParAnnee(anneeSelectionnee ?? 0).toFixed(2) }} €</div>
            </NCard>
          </NGi>
        </NGrid>
      </NCard>

      <!-- Onglets -->
      <NTabs type="line" animated>
        <NTabPane name="charges" tab="Charges">
          <NDataTable
            v-if="!isMobile"
            :columns="colonnesCharges"
            :data="chargesFiltrees"
            :loading="chargement"
            :pagination="{ pageSize: 10 }"
            striped
          />
          <div v-else>
            <NCard v-for="charge in chargesFiltrees" :key="charge.id" class="mobile-card">
              <div><b>Date :</b> {{ charge.dateCharge }}</div>
              <div><b>Propriété :</b> {{ getProprieteNom(charge) }}</div>
              <div><b>Intitulé :</b> {{ charge.intitule }}</div>
              <div><b>Nature :</b> {{ charge.nature }}</div>
              <div><b>Montant :</b> {{ charge.montant }} €</div>
              <div v-if="charge.documentNom"><b>Document :</b> {{ charge.documentNom }}</div>
              <div class="actions">
                <NButton size="small" type="primary" @click="editerCharge(charge)">Éditer</NButton>
                <NButton size="small" type="error" @click="supprimerCharge(charge.id || '')">Supprimer</NButton>
              </div>
            </NCard>
          </div>
        </NTabPane>

        <NTabPane name="recettes" tab="Recettes">
          <NDataTable
            v-if="!isMobile"
            :columns="colonnesRecettes"
            :data="recettesFiltrees"
            :loading="chargement"
            :pagination="{ pageSize: 10 }"
            striped
          />
          <div v-else>
            <NCard v-for="recette in recettesFiltrees" :key="recette.id" class="mobile-card">
              <div><b>Date :</b> {{ recette.dateRecette }}</div>
              <div><b>Propriété :</b> {{ getProprieteNom(recette) }}</div>
              <div><b>Intitulé :</b> {{ recette.intitule }}</div>
              <div><b>Type :</b> {{ recette.type }}</div>
              <div><b>Montant :</b> {{ recette.montant }} €</div>
              <div v-if="recette.documentNom"><b>Document :</b> {{ recette.documentNom }}</div>
              <div class="actions">
                <NButton size="small" type="primary" @click="editerRecette(recette)">Éditer</NButton>
                <NButton size="small" type="error" @click="supprimerRecette(recette.id || '')">Supprimer</NButton>
              </div>
            </NCard>
          </div>
        </NTabPane>

        <NTabPane name="amortissements" tab="Amortissements">
          <NDataTable
            v-if="!isMobile"
            :columns="colonnesAmortissements"
            :data="amortissementsFiltres"
            :loading="chargement"
            :pagination="{ pageSize: 10 }"
            striped
          />
          <div v-else>
            <NCard v-for="amort in amortissementsFiltres" :key="amort.id" class="mobile-card">
              <div><b>Année :</b> {{ amort.annee }}</div>
              <div><b>Propriété :</b> {{ amort.proprieteNom }}</div>
              <div><b>Immobilisation :</b> {{ amort.immobilisationIntitule }}</div>
              <div><b>Montant :</b> {{ amort.montantAmortissement }} €</div>
            </NCard>
          </div>
        </NTabPane>
      </NTabs>
    </NCard>

    <!-- Modal Écriture -->
    <NModal v-model:show="modalEcritureVisible" preset="card" title="Écriture Comptable" style="width: 600px">
      <NForm :model="ecritureEnCours" label-placement="top">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Date" required>
              <NDatePicker
                v-model:value="ecritureEnCours.dateEcriture"
                type="date"
                format="dd/MM/yyyy"
                value-format="yyyy-MM-dd"
                clearable
                style="width: 100%"
                placeholder="Choisir une date"
              />
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

    <!-- Modal Charge -->
    <NModal v-model:show="modalChargeVisible" preset="card" title="Charge" style="width: 600px">
      <NForm :model="chargeEnCours" label-placement="top">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Date" required>
              <NDatePicker
                v-model:value="chargeEnCours.dateCharge"
                type="date"
                format="dd/MM/yyyy"
                value-format="yyyy-MM-dd"
                clearable
                style="width: 100%"
                placeholder="Choisir une date"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant" required>
              <NInput
                v-model:value="chargeEnCours.montant"
                placeholder="0.00"
                type="text"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
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
            <NFormItem required label="Intitulé">
              <NInput
                v-model:value="chargeEnCours.intitule"
                type="text"
                placeholder="Intitulé de la charge"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Description">
              <NInput
                v-model:value="chargeEnCours.commentaire"
                type="textarea"
                placeholder="Description de la charge"
                :rows="3"
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
            <NFormItem label="Date" required>
              <NDatePicker
                v-model:value="recetteEnCours.dateRecette"
                type="date"
                format="dd/MM/yyyy"
                value-format="yyyy-MM-dd"
                clearable
                style="width: 100%"
                placeholder="Choisir une date"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant" required>
              <NInput
                v-model:value="recetteEnCours.montant"
                placeholder="0.00"
                type="text"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
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
            <NFormItem required label="Intitulé">
              <NInput
                v-model:value="recetteEnCours.intitule"
                type="text"
                placeholder="Intitulé de la recette"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Description">
              <NInput
                v-model:value="recetteEnCours.commentaire"
                type="textarea"
                placeholder="Description de la recette"
                :rows="3"
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
  </div>
</template>

<style scoped>
.titre-principal, h1, h2, h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.flex {
  display: flex;
  justify-content: space-between;
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
.mobile-card {
  margin-bottom: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
  background: #fff;
}
.mobile-card .actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}
.resume-card {
  margin-bottom: 12px;
  text-align: center;
  box-shadow: 0 2px 8px #0001;
  border-radius: 10px;
  padding: 16px 0;
}
.resume-label {
  font-size: 1rem;
  color: #888;
}
.resume-value {
  font-size: 1.5rem;
  font-weight: bold;
  margin-top: 4px;
}
.resume-card.charge .resume-value { color: #dc2626; }
.resume-card.amortissement .resume-value { color: #eab308; }
.resume-card.recette .resume-value { color: #16a34a; }
.resume-card.solde.positif .resume-value { color: #2563eb; }
.resume-card.solde.negatif .resume-value { color: #ea580c; }
.page-header {
  margin-bottom: 1.5rem;
}
.mobile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-bottom: 1.5rem;
}
.mobile-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 12px 0 0 0;
  text-align: center;
  color: #222;
}
.mobile-actions {
  display: flex;
  gap: 10px;
  justify-content: center;
}
.mobile-journal-btn {
  margin-bottom: 10px;
  max-width: 320px;
  width: 100%;
  align-self: center;
}
@media (max-width: 768px) {
  .titre-principal, h1, h2, h3 {
    font-size: 1.25rem !important;
  }
  .flex {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
}
.annee-row-wrapper {
  margin-bottom: 12px;
}
.annee-row {
  display: flex;
  align-items: center;
  gap: 0;
}
@media (max-width: 768px) {
  .annee-row-wrapper {
    justify-content: center;
    display: flex;
    margin-bottom: 16px;
  }
  .annee-row {
    font-size: 1rem;
    justify-content: center;
  }
  .label-annee {
    font-size: 1rem;
  }
}
.compta-bg {
  background-color: var(--n-body-color);
  min-height: 100vh;
}
</style> 