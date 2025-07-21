<script setup lang="ts">
import { Icon } from '@iconify/vue'
import { NButton, NCard, NDataTable, NDatePicker, NForm, NFormItem, NInput, NInputNumber, NSelect, useMessage, NSteps, NStep, NH2, NIcon } from 'naive-ui'
import { computed, h, onMounted, ref, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/store/modules/app'
import { useAuthStore } from '@/store/modules/auth'
import { BuildingHome24Filled } from '@vicons/fluent'

const router = useRouter()
const message = useMessage()
const currentStep = ref(0)
const saving = ref(false)
const generatingEcheancier = ref(false)
const editingRowsOriginalData = ref<Record<number, any>>({})
const authStore = useAuthStore()

// 1. Mets à jour les titres d'étape
const steps = [
  { label: 'Propriété' },
  { label: 'Caractéristiques' },
  { label: 'Échéancier' },
  { label: 'Confirmation' },
]
const stepTitles = ['Étape 1 : Propriété', 'Étape 2 : Caractéristiques', 'Étape 3 : Échéancier', 'Étape 4 : Confirmation']

// 2. Ajoute une ref pour la propriété sélectionnée
const proprieteIdSelected = ref('')

const formData = ref({
  montant: 0,
  duree: 12,
  taux: 1.5,
  type: '',
  dateDebut: null as number | null,
  periodicite: 'MENSUELLE',
  proprieteId: '' as string,
  banque: '',
})

const typeOptions = [
  { label: 'Amortissable', value: 'AMORTISSABLE' },
  { label: 'In fine', value: 'IN_FINE' },
]
const periodiciteOptions = [
  { label: 'Mensuelle', value: 'MENSUELLE' },
  { label: 'Trimestrielle', value: 'TRIMESTRIELLE' },
]
type ProprieteOption = { label: string; value: string; adresse?: string; surface?: number; type?: string }
const proprieteOptions = ref<ProprieteOption[]>([])

const echeancier = ref<any[]>([])

// Ajout refs pour focus sur le NDatePicker de la nouvelle échéance
const echeanceRefs = ref<Record<number, any>>({})

function formatDate(date: number | string | null) {
  if (!date) {
    return ''
  }
  const d = new Date(date)
  const options: Intl.DateTimeFormatOptions = { day: '2-digit', month: '2-digit', year: 'numeric' }
  return d.toLocaleDateString('fr-FR', options)
}

// Colonnes pour le récapitulatif (non-éditable)
const recapColumns = [
  { title: 'N°', key: 'numero', width: 60 },
  { title: 'Date', key: 'date', width: 120, render: (row: any) => formatDate(row.date) },
  { title: 'Capital remboursé', key: 'capital', width: 120 },
  { title: 'Intérêts', key: 'interets', width: 100 },
  { title: 'Total échéance', key: 'total', width: 120 },
  { title: 'Solde restant dû', key: 'solde', width: 120 },
]

function handleEdit(row: any) {
  editingRowsOriginalData.value[row.numero] = { ...row }
  const rowToEdit = echeancier.value.find(e => e.numero === row.numero)
  if (rowToEdit) {
    rowToEdit.isEditing = true
    nextTick(() => {
      if (echeanceRefs.value[row.numero] && echeanceRefs.value[row.numero].focus) {
        echeanceRefs.value[row.numero].focus()
      }
    })
  }
}

function handleSave(row: any) {
  const rowToSave = echeancier.value.find(e => e.numero === row.numero)
  if (rowToSave) {
    rowToSave.isEditing = false
  }
  delete editingRowsOriginalData.value[row.numero]
}

function handleCancel(row: any, index: number) {
  const originalData = editingRowsOriginalData.value[row.numero]
  if (originalData) {
    echeancier.value[index] = { ...originalData, isEditing: false }
    delete editingRowsOriginalData.value[row.numero]
  }
  else {
    echeancier.value.splice(index, 1)
  }
}

function removeEcheance(index: number) {
  echeancier.value.splice(index, 1)
}

// Colonnes pour l'édition de l'échéancier (éditable)
const echeancierColumns = computed(() => [
  { title: 'N°', key: 'numero', width: 60 },
  {
    title: 'Date',
    key: 'date',
    width: 150,
    render: (row: any, index: number) => {
      if (row.isEditing) {
        return h(NDatePicker, {
          ref: (el: any) => {
            if (el) echeanceRefs.value[row.numero] = el
          },
          value: row.date ? new Date(row.date).getTime() : null,
          onUpdateValue: (value: number | null) => {
            echeancier.value[index].date = value ? new Date(value) : null
          },
          type: 'date',
          format: 'dd/MM/yyyy',
          style: 'width: 100%',
        })
      }
      return formatDate(row.date)
    },
  },
  {
    title: 'Capital remboursé',
    key: 'capital',
    width: 120,
    render: (row: any, index: number) => {
      if (row.isEditing) {
        return h(NInputNumber, {
          value: row.capital,
          onUpdateValue: (value: number | null) => (echeancier.value[index].capital = value || 0),
          style: 'width: 100%',
        })
      }
      return row.capital
    },
  },
  {
    title: 'Intérêts',
    key: 'interets',
    width: 100,
    render: (row: any, index: number) => {
      if (row.isEditing) {
        return h(NInputNumber, {
          value: row.interets,
          onUpdateValue: (value: number | null) => (echeancier.value[index].interets = value || 0),
          style: 'width: 100%',
        })
      }
      return row.interets
    },
  },
  {
    title: 'Total échéance',
    key: 'total',
    width: 120,
    render: (row: any, index: number) => {
      if (row.isEditing) {
        return h(NInputNumber, {
          value: row.total,
          onUpdateValue: (value: number | null) => (echeancier.value[index].total = value || 0),
          style: 'width: 100%',
        })
      }
      return row.total
    },
  },
  {
    title: 'Solde restant dû',
    key: 'solde',
    width: 120,
    render: (row: any, index: number) => {
      if (row.isEditing) {
        return h(NInputNumber, {
          value: row.solde,
          onUpdateValue: (value: number | null) => (echeancier.value[index].solde = value || 0),
          style: 'width: 100%',
        })
      }
      return row.solde
    },
  },
  {
    title: 'Actions',
    key: 'actions',
    width: 180,
    render: (row: any, index: number) => {
      const buttons = []
      if (row.isEditing) {
        buttons.push(
          h(NButton, { size: 'small', type: 'primary', onClick: () => handleSave(row) }, { default: () => 'OK' }),
          h(NButton, { size: 'small', onClick: () => handleCancel(row, index) }, { default: () => 'Annuler' }),
        )
      }
      else {
        buttons.push(
          h(NButton, { size: 'small', onClick: () => handleEdit(row) }, { default: () => 'Modifier' }),
          h(NButton, { size: 'small', onClick: () => removeEcheance(index) }, { default: () => 'Supprimer' }),
        )
      }
      return h('div', { style: 'display: flex; gap: 6px;' }, buttons)
    },
  },
])

const loadingProprietes = ref(false)

function goBack() {
  router.push('/credits')
}

// 3. Modifie la validation des étapes
const isStep0Valid = computed(() => !!proprieteIdSelected.value)
// Corrige la validation de l'étape 2 (isStep1Valid)
const isStep1Valid = computed(() => {
  const f = formData.value
  return !!(f.montant > 0 && f.duree > 0 && f.taux >= 0 && f.type && f.dateDebut && f.periodicite && f.banque)
})

// 4. Modifie la navigation
function nextStep() {
  if (currentStep.value === 0 && !isStep0Valid.value) {
    message.error('Veuillez sélectionner une propriété')
    return
  }
  if (currentStep.value === 1 && !isStep1Valid.value) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }
  if (currentStep.value < steps.length - 1) {
    currentStep.value++
  }
}

function previousStep() {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

async function generateEcheancier() {
  const f = formData.value
  if (!isStep1Valid.value) {
    message.error('Veuillez d\'abord remplir toutes les caractéristiques du crédit')
    return
  }
  generatingEcheancier.value = true
  await new Promise(resolve => setTimeout(resolve, 300))
  // Génération simple d'un échéancier amortissable (français)
  const n = Number(f.duree)
  const tauxMensuel = Number(f.taux) / 100 / (f.periodicite === 'MENSUELLE' ? 12 : 4)
  const montant = Number(f.montant)
  let capitalRestant = montant
  echeancier.value = []
  if (f.type === 'AMORTISSABLE') {
    // Formule de mensualité
    const mensualite = tauxMensuel > 0 ? (montant * tauxMensuel) / (1 - (1 + tauxMensuel) ** -n) : montant / n
    for (let i = 1; i <= n; i++) {
      const interets = capitalRestant * tauxMensuel
      const capital = mensualite - interets
      capitalRestant -= capital
      echeancier.value.push({
        numero: i,
        date: f.dateDebut ? new Date(new Date(f.dateDebut).setMonth(new Date(f.dateDebut).getMonth() + (f.periodicite === 'MENSUELLE' ? i - 1 : (i - 1) * 3))) : '',
        capital: Math.round(capital * 100) / 100,
        interets: Math.round(interets * 100) / 100,
        total: Math.round(mensualite * 100) / 100,
        solde: Math.max(0, Math.round(capitalRestant * 100) / 100),
        isEditing: false,
      })
    }
  }
  else if (f.type === 'IN_FINE') {
    // Intérêts constants, capital remboursé à la fin
    const interets = montant * tauxMensuel
    for (let i = 1; i <= n; i++) {
      echeancier.value.push({
        numero: i,
        date: f.dateDebut ? new Date(new Date(f.dateDebut).setMonth(new Date(f.dateDebut).getMonth() + (f.periodicite === 'MENSUELLE' ? i - 1 : (i - 1) * 3))) : '',
        capital: i === n ? montant : 0,
        interets: Math.round(interets * 100) / 100,
        total: Math.round((i === n ? montant + interets : interets) * 100) / 100,
        solde: i === n ? 0 : montant,
        isEditing: false,
      })
    }
  }
  generatingEcheancier.value = false
}
function addEcheanceManuelle() {
  const maxNumero = echeancier.value.length > 0 ? Math.max(...echeancier.value.map(e => e.numero)) : 0
  echeancier.value.push({
    numero: maxNumero + 1,
    date: new Date(),
    capital: 0,
    interets: 0,
    total: 0,
    solde: 0,
    isEditing: true,
  })
  nextTick(() => {
    if (echeanceRefs.value[maxNumero + 1] && echeanceRefs.value[maxNumero + 1].focus) {
      echeanceRefs.value[maxNumero + 1].focus()
    }
  })
}

async function saveCredit() {
  saving.value = true
  try {
    // Calcul de la date de fin à partir de la date de début et de la durée (en mois)
    const dateDebut = new Date(formData.value.dateDebut!)
    const dateFin = new Date(dateDebut)
    dateFin.setMonth(dateFin.getMonth() + Number(formData.value.duree))
    const dateDebutStr = dateDebut.toISOString().split('T')[0]
    const dateFinStr = dateFin.toISOString().split('T')[0]
    // Calcul de la mensualité (prendre la première échéance si dispo, sinon recalculer)
    let mensualite = '0'
    if (echeancier.value.length > 0) {
      mensualite = String(echeancier.value[0].total)
    }
    else {
      // Recalcul simple
      const montant = Number(formData.value.montant)
      const tauxMensuel = Number(formData.value.taux) / 100 / 12
      const n = Number(formData.value.duree)
      mensualite = tauxMensuel > 0 ? String(Math.round(((montant * tauxMensuel) / (1 - (1 + tauxMensuel) ** -n)) * 100) / 100) : String(Math.round((montant / n) * 100) / 100)
    }
    // Construction du DTO
    const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL
    const creditDTO = {
      proprieteId: proprieteIdSelected.value,
      banque: formData.value.banque,
      montantEmprunte: String(formData.value.montant),
      dateDebut: dateDebutStr,
      dateFin: dateFinStr,
      dureeMois: String(formData.value.duree),
      tauxInteretAnnuel: String(formData.value.taux),
      mensualite,
      assuranceMensuelle: '0',
      fraisDossier: '0',
      fraisGarantie: '0',
      utilisateurId: authStore.userInfo.userId,
    }
    const response = await fetch(`${baseUrl}/api/createCredit`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(creditDTO),
    })
    if (!response.ok) {
      throw new Error('Erreur lors de la création du crédit')
    }
    const creditCreated = await response.json()
    // Enregistrement des échéances
    const echeancesDTO = echeancier.value.map(e => ({
      creditId: creditCreated.id,
      dateEcheance: e.date ? new Date(e.date).toISOString().split('T')[0] : '',
      interet: String(e.interets),
      capitalRembourse: String(e.capital),
      assurance: '0',
      totalEcheance: String(e.total),
    }))
    const res = await fetch(`${baseUrl}/api/createEcheancesCredit`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(echeancesDTO),
    })
    if (!res.ok) {
      message.error('Erreur lors de la création des échéances')
    } else {
      message.success('Crédit et échéancier créés avec succès !')
    }
    router.push('/credits')
  }
  catch {
    message.error('Erreur lors de la création du crédit')
  }
  finally {
    saving.value = false
  }
}
function selectProprieteCredit(p: ProprieteOption) {
  proprieteIdSelected.value = String(p.value)
  nextStep()
}
onMounted(async () => {
  loadingProprietes.value = true
  try {
    // Appel API réel pour charger les propriétés de l'utilisateur Yussouf
    const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL
    const response = await fetch(`${baseUrl}/api/getProprietesByUtilisateur/${authStore.userInfo.userId}`)
    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des propriétés')
    }
    const proprietes = await response.json()
    proprieteOptions.value = proprietes.map((p: any) => ({
      label: p.nom,
      value: String(p.id),
      adresse: p.adresse,
      surface: p.surface,
      type: p.type
    }))
  }
  catch {
    message.error('Erreur lors du chargement des propriétés')
  }
  finally {
    loadingProprietes.value = false
  }
})

const isMobileRef = ref(window.innerWidth < 768)
function handleResize() {
  isMobileRef.value = window.innerWidth < 768
}
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

definePage({
  meta: {
    title: 'Création d\'un Crédit',
    icon: 'material-symbols:edit-square-outline',
    hideInMenu: true,
  },
})
</script>

<template>
  <div class="credits-create-page">
    <NCard class="progress-card">
      <div v-if="!isMobileRef" class="mb-8">
        <NSteps :current="Number(currentStep)" size="small">
          <NStep title="Propriété" :status="currentStep === 0 ? 'process' : 'finish'" />
          <NStep title="Caractéristiques" :status="currentStep === 1 ? 'process' : (currentStep > 1 ? 'finish' : undefined)" />
          <NStep title="Échéancier" :status="currentStep === 2 ? 'process' : (currentStep > 2 ? 'finish' : undefined)" />
          <NStep title="Confirmation" :status="currentStep === 3 ? 'process' : undefined" />
        </NSteps>
      </div>
      <div v-else class="mobile-stepper mb-8">
        <div class="step-mobile-number">Étape {{ currentStep + 1 }}/4</div>
        <div class="step-mobile-label">{{ stepTitles[currentStep] }}</div>
      </div>
    </NCard>
    <!-- Étape 0 : Sélection de la propriété -->
    <div v-if="currentStep === 0">
      <NH2 class="titre-principal mb-4">Étape 1 : Sélection de la propriété</NH2>
      <div class="proprietes-grid">
        <NCard
          v-for="p in proprieteOptions"
          :key="p.value"
          :class="['propriete-card', { selected: proprieteIdSelected === String(p.value) }]"
          @click="selectProprieteCredit(p)"
          hoverable
          tabindex="0"
          @keydown.enter="selectProprieteCredit(p)"
        >
          <div class="flex items-start gap-4">
            <div class="propriete-avatar">
              <NIcon :component="BuildingHome24Filled" size="32" />
            </div>
            <div class="flex-1">
              <div class="propriete-nom mb-2">{{ p.label }}</div>
              <div class="text-sm space-y-1">
                <div v-if="p.adresse">{{ p.adresse }}</div>
                <div v-if="p.surface">Surface : {{ p.surface }} m²</div>
                <div v-if="p.type">Type : {{ p.type }}</div>
              </div>
            </div>
          </div>
        </NCard>
      </div>
      <div v-if="!loadingProprietes && proprieteOptions.length === 0" style="color: #dc2626; margin-top: 4px;">Aucune propriété disponible</div>
      <div :class="['step-actions', { 'step-actions-mobile': isMobileRef }]">
        <NButton @click="goBack">Annuler</NButton>
        <NButton type="primary" :disabled="!isStep0Valid" @click="nextStep">
          Suivant
          <template #icon>
            <Icon icon="material-symbols:arrow-forward" />
          </template>
        </NButton>
      </div>
    </div>
    <!-- Étape 1 : Saisie des caractéristiques -->
    <NCard v-if="currentStep === 1" title="Étape 2 : Caractéristiques du crédit" class="step-card">
      <NForm :class="{ 'mobile-form': isMobileRef }" :label-placement="isMobileRef ? 'top' : 'left'" label-width="auto">
        <NFormItem label="Montant emprunté (€)" required>
          <NInputNumber v-model:value="formData.montant as number" min="0" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Durée (mois)" required>
          <NInputNumber v-model:value="formData.duree as number" min="1" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Taux d'intérêt (%)" required>
          <NInputNumber v-model:value="formData.taux as number" min="0" max="100" precision="2" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Type de crédit" required>
          <NSelect v-model:value="formData.type" :options="typeOptions" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Date de début" required>
          <NDatePicker v-model:value="formData.dateDebut as number | null" type="date" style="width: 100%" format="dd/MM/yyyy" />
        </NFormItem>
        <NFormItem label="Périodicité" required>
          <NSelect v-model:value="formData.periodicite" :options="periodiciteOptions" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Banque" required>
          <NInput v-model:value="formData.banque" placeholder="Nom de la banque" />
        </NFormItem>
      </NForm>
      <div :class="['step-actions', { 'step-actions-mobile': isMobileRef }]">
        <NButton @click="goBack">Annuler</NButton>
        <NButton type="primary" :disabled="!isStep1Valid" @click="nextStep">
          Suivant
          <template #icon>
            <Icon icon="material-symbols:arrow-forward" />
          </template>
        </NButton>
      </div>
    </NCard>
    <!-- Étape 2 : Génération de l'échéancier -->
    <NCard v-if="currentStep === 2" title="Étape 3 : Échéancier" class="step-card">
      <div :class="['step-actions', { 'step-actions-mobile': isMobileRef }]" style="margin-bottom: 16px;">
        <NButton @click="previousStep">
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" :disabled="echeancier.length === 0" @click="nextStep">
          Suivant
          <template #icon>
            <Icon icon="material-symbols:arrow-forward" />
          </template>
        </NButton>
      </div>
      <div class="echeancier-section">
        <NButton @click="generateEcheancier" type="primary">Générer l'échéancier</NButton>
        <NButton @click="addEcheanceManuelle" style="margin-left: 8px;">Ajouter une échéance manuelle</NButton>
        <NDataTable v-if="!isMobileRef" :columns="echeancierColumns" :data="echeancier" :loading="generatingEcheancier" :row-key="(row: any) => row.numero" striped />
        <div v-else>
          <NCard v-for="(e, idx) in echeancier" :key="e.numero" class="mobile-card">
            <div><b>N° :</b> {{ e.numero }}</div>
            <div><b>Date :</b>
              <template v-if="e.isEditing">
                <NDatePicker v-model:value="e.date" type="date" format="dd/MM/yyyy" style="width: 100%" :ref="el => { if (el) echeanceRefs[e.numero] = el }" />
              </template>
              <template v-else>
                {{ formatDate(e.date) }}
              </template>
            </div>
            <div><b>Capital remboursé :</b>
              <template v-if="e.isEditing">
                <NInputNumber v-model:value="e.capital" style="width: 100%" />
              </template>
              <template v-else>
                {{ e.capital }}
              </template>
            </div>
            <div><b>Intérêts :</b>
              <template v-if="e.isEditing">
                <NInputNumber v-model:value="e.interets" style="width: 100%" />
              </template>
              <template v-else>
                {{ e.interets }}
              </template>
            </div>
            <div><b>Total échéance :</b>
              <template v-if="e.isEditing">
                <NInputNumber v-model:value="e.total" style="width: 100%" />
              </template>
              <template v-else>
                {{ e.total }}
              </template>
            </div>
            <div><b>Solde restant dû :</b>
              <template v-if="e.isEditing">
                <NInputNumber v-model:value="e.solde" style="width: 100%" />
              </template>
              <template v-else>
                {{ e.solde }}
              </template>
            </div>
            <div class="actions">
              <NButton size="small" v-if="!e.isEditing" @click="handleEdit(e)">Modifier</NButton>
              <NButton size="small" v-if="!e.isEditing" @click="removeEcheance(idx)">Supprimer</NButton>
              <NButton size="small" v-if="e.isEditing" type="primary" @click="handleSave(e)">OK</NButton>
              <NButton size="small" v-if="e.isEditing" @click="handleCancel(e, idx)">Annuler</NButton>
            </div>
          </NCard>
        </div>
      </div>
      <div :class="['step-actions', { 'step-actions-mobile': isMobileRef }]">
        <NButton @click="previousStep">
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" :disabled="echeancier.length === 0" @click="nextStep">
          Suivant
          <template #icon>
            <Icon icon="material-symbols:arrow-forward" />
          </template>
        </NButton>
      </div>
    </NCard>
    <!-- Étape 3 : Récapitulatif et confirmation -->
    <NCard v-if="currentStep === 3" title="Étape 4 : Confirmation" class="step-card">
      <div :class="['step-actions', { 'step-actions-mobile': isMobileRef }]" style="margin-bottom: 16px;">
        <NButton @click="previousStep">
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" @click="saveCredit" :loading="saving">
          <template #icon>
            <Icon icon="material-symbols:check" />
          </template>
          Créer le crédit
        </NButton>
      </div>
      <div class="recap-section">
        <h3>Récapitulatif du crédit</h3>
        <ul>
          <li><strong>Propriété :</strong> {{ proprieteOptions.find(p => p.value === proprieteIdSelected)?.label || 'Aucune sélection' }}</li>
          <li><strong>Montant :</strong> {{ formData.montant }} €</li>
          <li><strong>Durée :</strong> {{ formData.duree }} mois</li>
          <li><strong>Taux :</strong> {{ formData.taux }} %</li>
          <li><strong>Type :</strong> {{ formData.type }}</li>
          <li><strong>Date de début :</strong> {{ formatDate(formData.dateDebut) }}</li>
          <li><strong>Périodicité :</strong> {{ formData.periodicite }}</li>
        </ul>
        <h3>Échéancier</h3>
        <NDataTable v-if="!isMobileRef" :columns="recapColumns" :data="echeancier" striped />
        <div v-else>
          <NCard v-for="(e, idx) in echeancier" :key="e.numero" class="mobile-card">
            <div><b>N° :</b> {{ e.numero }}</div>
            <div><b>Date :</b> {{ formatDate(e.date) }}</div>
            <div><b>Capital remboursé :</b> {{ e.capital }}</div>
            <div><b>Intérêts :</b> {{ e.interets }}</div>
            <div><b>Total échéance :</b> {{ e.total }}</div>
            <div><b>Solde restant dû :</b> {{ e.solde }}</div>
          </NCard>
        </div>
      </div>
      <div :class="['step-actions', { 'step-actions-mobile': isMobileRef }]">
        <NButton @click="previousStep">
          <template #icon>
            <Icon icon="material-symbols:arrow-back" />
          </template>
          Précédent
        </NButton>
        <NButton type="primary" @click="saveCredit" :loading="saving">
          <template #icon>
            <Icon icon="material-symbols:check" />
          </template>
          Créer le crédit
        </NButton>
      </div>
    </NCard>
  </div>
</template>

<style scoped>
.credits-create-page {
  padding: 20px;
  background: #f5f6fa;
}
.progress-card, .step-card {
  background: #fafbfc;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(60,60,60,0.04);
}
.progress-steps {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}
.progress-steps-mobile {
  overflow-x: auto;
  flex-wrap: nowrap;
  gap: 10px;
  padding: 12px 0 12px 4px;
}
.progress-steps-mobile .step {
  min-width: 120px;
  padding: 8px 6px;
}
.progress-steps-mobile .step-number {
  width: 32px;
  height: 32px;
  font-size: 1rem;
}
.progress-steps-mobile .step-label {
  font-size: 12px;
  text-align: center;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  position: relative;
  color: #222;
}
.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 20px;
  left: 50%;
  width: 100%;
  height: 2px;
  background-color: #e5e7eb;
  z-index: 1;
}
.step.completed:not(:last-child)::after {
  background-color: #10b981;
}
.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  background: #3b82f6;
  color: #fff;
  z-index: 2;
  position: relative;
}
.step.active .step-number {
  background-color: #3b82f6;
  color: white;
}
.step.completed .step-number {
  background-color: #10b981;
  color: white;
}
.step-label {
  font-size: 14px;
  color: #222;
  text-align: center;
}
.step.active .step-label {
  color: #3b82f6;
  font-weight: 500;
}
.step.completed .step-label {
  color: #10b981;
  font-weight: 500;
}
.step-card {
  margin-bottom: 20px;
}
.step-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}
.echeancier-section {
  margin-bottom: 24px;
}
.recap-section {
  margin-bottom: 24px;
}
.mobile-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 12px 0 0 0;
  text-align: center;
  color: #222;
}
.mobile-journal-btn {
  margin-bottom: 10px;
  max-width: 320px;
  width: 100%;
  align-self: center;
}
.step-actions-mobile {
  flex-direction: column !important;
  gap: 10px !important;
  align-items: stretch !important;
  margin-bottom: 16px;
}
.step-actions-mobile .n-button {
  width: 100%;
  max-width: 320px;
  align-self: center;
}
.mobile-card {
  margin-bottom: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(60,60,60,0.04);
}
.mobile-card .actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}
.mobile-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.mobile-form .n-form-item {
  width: 100%;
  margin-bottom: 0;
}
.mobile-stepper {
  text-align: center;
  margin-bottom: 1.5rem;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1976d2;
}
.step-mobile-label {
  font-size: 1.2rem;
  color: #222;
  margin-bottom: 1rem;
}
.n-form-item-label {
  color: #222;
}
.proprietes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
.propriete-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.propriete-card.selected {
  border-color: #1976d2;
  box-shadow: 0 0 0 2px #1976d233;
}
.propriete-avatar {
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.propriete-nom {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--n-text-color);
}
@media (max-width: 600px) {
  .proprietes-grid {
    grid-template-columns: 1fr;
    gap: 10px;
  }
  .propriete-card {
    min-width: 0;
  }
}
@media (max-width: 768px) {
  .echeancier-section {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .echeancier-section .n-button {
    width: 100%;
    margin-left: 0 !important;
  }
}
</style>
<!-- test --> 