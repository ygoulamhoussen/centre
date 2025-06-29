<template>
  <div class="credits-create-page">
    <div class="page-header">
      <h1>Nouveau Crédit</h1>
      <NButton @click="goBack">
        <template #icon>
          <Icon icon="material-symbols:arrow-back" />
        </template>
        Retour
      </NButton>
    </div>
    <NCard class="progress-card">
      <div class="progress-steps">
        <div v-for="(step, index) in steps" :key="index" class="step" :class="{ active: currentStep === index, completed: currentStep > index }">
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-label">{{ step.label }}</div>
        </div>
      </div>
    </NCard>
    <!-- Étape 1 : Saisie des caractéristiques -->
    <NCard v-if="currentStep === 0" title="Étape 1 : Caractéristiques du crédit" class="step-card">
      <NForm label-placement="left" label-width="auto">
        <NFormItem label="Intitulé" required>
          <NInput v-model:value="formData.intitule" placeholder="Intitulé du crédit" />
        </NFormItem>
        <NFormItem label="Montant emprunté (€)" required>
          <NInputNumber v-model:value="formData.montant" min="0" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Durée (mois)" required>
          <NInputNumber v-model:value="formData.duree" min="1" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Taux d'intérêt (%)" required>
          <NInputNumber v-model:value="formData.taux" min="0" max="100" precision="2" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Type de crédit" required>
          <NSelect v-model:value="formData.type" :options="typeOptions" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Date de début" required>
          <NDatePicker v-model:value="formData.dateDebut" type="date" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Périodicité" required>
          <NSelect v-model:value="formData.periodicite" :options="periodiciteOptions" style="width: 100%" />
        </NFormItem>
        <NFormItem label="Propriété associée" required>
          <NSelect
            v-model:value="formData.proprieteId"
            :options="proprieteOptions"
            placeholder="Sélectionner une propriété"
            style="width: 100%"
            :loading="loadingProprietes"
          />
          <div v-if="!loadingProprietes && proprieteOptions.length === 0" style="color: #dc2626; margin-top: 4px;">Aucune propriété disponible</div>
        </NFormItem>
        <NFormItem label="Banque" required>
          <NInput v-model:value="formData.banque" placeholder="Nom de la banque" />
        </NFormItem>
      </NForm>
      <div class="step-actions">
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
    <NCard v-if="currentStep === 1" title="Étape 2 : Échéancier" class="step-card">
      <div class="step-actions" style="margin-bottom: 16px;">
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
        <NDataTable :columns="echeancierColumns" :data="echeancier" striped />
      </div>
      <div class="step-actions">
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
    <NCard v-if="currentStep === 2" title="Étape 3 : Confirmation" class="step-card">
      <div class="step-actions" style="margin-bottom: 16px;">
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
          <li><strong>Intitulé :</strong> {{ formData.intitule }}</li>
          <li><strong>Montant :</strong> {{ formData.montant }} €</li>
          <li><strong>Durée :</strong> {{ formData.duree }} mois</li>
          <li><strong>Taux :</strong> {{ formData.taux }} %</li>
          <li><strong>Type :</strong> {{ formData.type }}</li>
          <li><strong>Date de début :</strong> {{ formatDate(formData.dateDebut) }}</li>
          <li><strong>Périodicité :</strong> {{ formData.periodicite }}</li>
        </ul>
        <h3>Échéancier</h3>
        <NDataTable :columns="echeancierColumns" :data="echeancier" striped />
      </div>
      <div class="step-actions">
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

<script setup lang="ts">
import { definePage } from 'vue-router'
import { computed, h, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { NButton, NCard, NDatePicker, NForm, NFormItem, NInput, NInputNumber, NSelect, NDataTable, useMessage } from 'naive-ui'

const router = useRouter()
const message = useMessage()
const currentStep = ref(0)
const saving = ref(false)

const steps = [
  { label: 'Caractéristiques' },
  { label: 'Échéancier' },
  { label: 'Confirmation' },
]

const formData = ref({
  intitule: '',
  montant: 0 as number,
  duree: 12 as number,
  taux: 1.5 as number,
  type: '',
  dateDebut: null as number | null,
  periodicite: 'MENSUELLE',
  proprieteId: '' as string,
  banque: '' as string,
})

const typeOptions = [
  { label: 'Amortissable', value: 'AMORTISSABLE' },
  { label: 'In fine', value: 'IN_FINE' },
]
const periodiciteOptions = [
  { label: 'Mensuelle', value: 'MENSUELLE' },
  { label: 'Trimestrielle', value: 'TRIMESTRIELLE' },
]
const proprieteOptions = ref<{ label: string; value: string }[]>([])

const echeancier = ref<any[]>([])

const echeancierColumns = [
  { title: 'N°', key: 'numero', width: 60 },
  { title: 'Date', key: 'date', width: 120, render: (row: any) => formatDate(row.date) },
  { title: 'Capital remboursé', key: 'capital', width: 120 },
  { title: 'Intérêts', key: 'interets', width: 100 },
  { title: 'Total échéance', key: 'total', width: 120 },
  { title: 'Solde restant dû', key: 'solde', width: 120 },
  { title: 'Actions', key: 'actions', width: 80, render: (_row: any, index: number) => h(NButton, { size: 'small', onClick: () => removeEcheance(index) }, { default: () => 'Supprimer' }) },
]

const loadingProprietes = ref(false)

function goBack() {
  router.push('/credits')
}
function nextStep() {
  if (currentStep.value === 0 && !isStep1Valid.value) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }
  if (currentStep.value < steps.length - 1) currentStep.value++
}
function previousStep() {
  if (currentStep.value > 0) currentStep.value--
}
const isStep1Valid = computed(() => {
  const f = formData.value
  return !!(f.intitule && f.montant > 0 && f.duree > 0 && f.taux >= 0 && f.type && f.dateDebut && f.periodicite && f.proprieteId && f.banque)
})
function formatDate(date: number | string | null) {
  if (!date) return ''
  const d = typeof date === 'number' ? new Date(date) : new Date(date)
  return d.toLocaleDateString('fr-FR')
}
function generateEcheancier() {
  const f = formData.value
  if (!isStep1Valid.value) {
    message.error("Veuillez d'abord remplir toutes les caractéristiques du crédit")
    return
  }
  // Génération simple d'un échéancier amortissable (français)
  const n = Number(f.duree)
  const tauxMensuel = Number(f.taux) / 100 / (f.periodicite === 'MENSUELLE' ? 12 : 4)
  const montant = Number(f.montant)
  let capitalRestant = montant
  echeancier.value = []
  if (f.type === 'AMORTISSABLE') {
    // Formule de mensualité
    const mensualite = tauxMensuel > 0 ? montant * tauxMensuel / (1 - Math.pow(1 + tauxMensuel, -n)) : montant / n
    for (let i = 1; i <= n; i++) {
      const interets = capitalRestant * tauxMensuel
      const capital = mensualite - interets
      capitalRestant -= capital
      echeancier.value.push({
        numero: i,
        date: f.dateDebut ? new Date(new Date(f.dateDebut).setMonth(new Date(f.dateDebut).getMonth() + (f.periodicite === 'MENSUELLE' ? i - 1 : (i - 1) * 3)) ) : '',
        capital: Math.round(capital * 100) / 100,
        interets: Math.round(interets * 100) / 100,
        total: Math.round(mensualite * 100) / 100,
        solde: Math.max(0, Math.round(capitalRestant * 100) / 100),
      })
    }
  } else if (f.type === 'IN_FINE') {
    // Intérêts constants, capital remboursé à la fin
    const interets = montant * tauxMensuel
    for (let i = 1; i <= n; i++) {
      echeancier.value.push({
        numero: i,
        date: f.dateDebut ? new Date(new Date(f.dateDebut).setMonth(new Date(f.dateDebut).getMonth() + (f.periodicite === 'MENSUELLE' ? i - 1 : (i - 1) * 3)) ) : '',
        capital: i === n ? montant : 0,
        interets: Math.round(interets * 100) / 100,
        total: Math.round((i === n ? montant + interets : interets) * 100) / 100,
        solde: i === n ? 0 : montant,
      })
    }
  }
}
function addEcheanceManuelle() {
  echeancier.value.push({
    numero: echeancier.value.length + 1,
    date: null,
    capital: 0,
    interets: 0,
    total: 0,
    solde: 0,
  })
}
function removeEcheance(index: number) {
  echeancier.value.splice(index, 1)
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
    } else {
      // Recalcul simple
      const montant = Number(formData.value.montant)
      const tauxMensuel = Number(formData.value.taux) / 100 / 12
      const n = Number(formData.value.duree)
      mensualite = tauxMensuel > 0 ? String(Math.round((montant * tauxMensuel / (1 - Math.pow(1 + tauxMensuel, -n))) * 100) / 100) : String(Math.round((montant / n) * 100) / 100)
    }
    // Construction du DTO
    const creditDTO = {
      proprieteId: formData.value.proprieteId,
      banque: formData.value.banque,
      montantEmprunte: String(formData.value.montant),
      dateDebut: dateDebutStr,
      dateFin: dateFinStr,
      dureeMois: String(formData.value.duree),
      tauxInteretAnnuel: String(formData.value.taux),
      mensualite: mensualite,
      assuranceMensuelle: '0', // à ajouter si tu veux gérer l'assurance
      fraisDossier: '0',
      fraisGarantie: '0',
    }
    const response = await fetch('http://localhost:8080/api/createCredit', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(creditDTO)
    })
    if (!response.ok) throw new Error('Erreur lors de la création du crédit')
    const creditCreated = await response.json()
    // Enregistrement des échéances
    let echeanceError = false
    for (const e of echeancier.value) {
      const echeanceDTO = {
        creditId: creditCreated.id,
        dateEcheance: e.date ? new Date(e.date).toISOString().split('T')[0] : '',
        interet: String(e.interets),
        capitalRembourse: String(e.capital),
        assurance: '0',
        totalEcheance: String(e.total),
      }
      const res = await fetch('http://localhost:8080/api/createEcheanceCredit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(echeanceDTO)
      })
      if (!res.ok) echeanceError = true
    }
    if (echeanceError) {
      message.error('Certaines échéances n\'ont pas pu être enregistrées')
    } else {
      message.success('Crédit et échéancier créés avec succès !')
    }
    router.push('/credits')
  } catch (e) {
    message.error('Erreur lors de la création du crédit')
  } finally {
    saving.value = false
  }
}
onMounted(async () => {
  loadingProprietes.value = true
  try {
    // Appel API réel pour charger les propriétés de l'utilisateur Yussouf
    const response = await fetch('http://localhost:8080/api/getProprietesByUtilisateur/00000000-0000-0000-0000-000000000003')
    if (!response.ok) throw new Error('Erreur lors de la récupération des propriétés')
    const proprietes = await response.json()
    proprieteOptions.value = proprietes.map((p: any) => ({ label: p.nom, value: p.id }))
  } catch (e) {
    message.error('Erreur lors du chargement des propriétés')
  } finally {
    loadingProprietes.value = false
  }
})

definePage({
  meta: {
    title: "Création d'un Crédit",
    icon: 'material-symbols:edit-square-outline',
    hideInMenu: true,
  },
})
</script>

<style scoped>
.credits-create-page {
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.progress-card {
  margin-bottom: 20px;
}
.progress-steps {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  position: relative;
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
  background-color: #e5e7eb;
  color: #6b7280;
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
  color: #6b7280;
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
</style> 