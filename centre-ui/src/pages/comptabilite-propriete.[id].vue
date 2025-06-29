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
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import {
  Add24Filled,
  ArrowLeft24Filled,
  Calculator24Filled,
  Delete24Filled,
  Edit24Filled,
  Money24Filled,
} from '@vicons/fluent'
import { computed, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { ChargeDTO, RecetteDTO, EcritureComptableDTO } from '@/types/dto'
import {
  createCharge,
  createRecette,
  deleteCharge,
  deleteRecette,
  fetchChargesByPropriete,
  fetchRecettesByPropriete,
  fetchEcrituresComptables,
  updateCharge,
  updateRecette,
} from '@/service/api/charges-recettes'

definePage({
  meta: {
    title: 'Comptabilité - Détail Propriété',
    icon: 'material-symbols:account-balance',
    order: 8,
    hideInMenu: true,
  },
})

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()
const message = useMessage()

const proprieteId = route.params.id as string

// États réactifs
const charges = ref<ChargeDTO[]>([])
const recettes = ref<RecetteDTO[]>([])
const ecrituresComptables = ref<EcritureComptableDTO[]>([])
const propriete = ref<any>(null)
const anneeFiscale = ref(new Date().getFullYear())
const chargement = ref(false)

// États pour les modales
const modalChargeVisible = ref(false)
const modalRecetteVisible = ref(false)
const modeEdition = ref(false)
const chargeEnCours = ref<Partial<ChargeDTO>>({})
const recetteEnCours = ref<Partial<RecetteDTO>>({})

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
  { title: 'Intitulé', key: 'intitule', width: 200 },
  { title: 'Nature', key: 'nature', width: 150 },
  { title: 'Montant', key: 'montant', width: 120, render: (row: ChargeDTO) => `${row.montant} €` },
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
  { title: 'Intitulé', key: 'intitule', width: 200 },
  { title: 'Type', key: 'type', width: 120 },
  { title: 'Montant', key: 'montant', width: 120, render: (row: RecetteDTO) => `${row.montant} €` },
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
  if (!proprieteId) return

  chargement.value = true
  try {
    const [chargesData, recettesData, ecrituresData] = await Promise.all([
      fetchChargesByPropriete(proprieteId),
      fetchRecettesByPropriete(proprieteId),
      fetchEcrituresComptables(proprieteId, anneeFiscale.value),
    ])

    charges.value = chargesData
    recettes.value = recettesData
    ecrituresComptables.value = ecrituresData

    // Charger les informations de la propriété
    await chargerPropriete()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors du chargement des données')
  }
  finally {
    chargement.value = false
  }
}

async function chargerPropriete() {
  // Ici vous devriez charger les informations de la propriété
  // propriete.value = await fetchPropriete(proprieteId)
}

function retour() {
  router.push('/comptabilite')
}

function nouvelleCharge() {
  modeEdition.value = false
  chargeEnCours.value = {
    intitule: '',
    montant: '',
    dateCharge: new Date().toISOString().split('T')[0],
    proprieteId: proprieteId,
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
  if (!chargeEnCours.value.intitule || !chargeEnCours.value.montant || !chargeEnCours.value.dateCharge || !chargeEnCours.value.nature) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  try {
    if (modeEdition.value && chargeEnCours.value.id) {
      await updateCharge(chargeEnCours.value.id, chargeEnCours.value)
      message.success('Charge mise à jour avec succès')
    }
    else {
      await createCharge({
        ...chargeEnCours.value,
        proprieteId: proprieteId,
        utilisateurId: authStore.userInfo.userId,
      })
      message.success('Charge créée avec succès')
    }

    modalChargeVisible.value = false
    await chargerDonnees()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors de la sauvegarde')
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
    montant: '',
    dateRecette: new Date().toISOString().split('T')[0],
    proprieteId: proprieteId,
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
  if (!recetteEnCours.value.intitule || !recetteEnCours.value.montant || !recetteEnCours.value.dateRecette || !recetteEnCours.value.type) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  try {
    if (modeEdition.value && recetteEnCours.value.id) {
      await updateRecette(recetteEnCours.value.id, recetteEnCours.value)
      message.success('Recette mise à jour avec succès')
    }
    else {
      await createRecette({
        ...recetteEnCours.value,
        proprieteId: proprieteId,
        utilisateurId: authStore.userInfo.userId,
      })
      message.success('Recette créée avec succès')
    }

    modalRecetteVisible.value = false
    await chargerDonnees()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors de la sauvegarde')
  }
}

async function supprimerRecette(id: string) {
  try {
    await deleteRecette(id)
    message.success('Recette supprimée avec succès')
    await chargerDonnees()
  }
  catch (error: any) {
    message.error(error.message || 'Erreur lors de la suppression')
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
      <!-- En-tête avec bouton retour -->
      <div class="flex justify-between items-center mb-6">
        <div class="flex items-center">
          <NButton @click="retour" class="mr-4">
            <template #icon>
              <NIcon :component="ArrowLeft24Filled" />
            </template>
            Retour
          </NButton>
          <div>
            <h1 class="text-2xl font-bold mb-2">Comptabilité - Détail Propriété</h1>
            <p class="text-gray-600">Gestion des charges et recettes pour cette propriété</p>
          </div>
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

      <!-- Informations de la propriété -->
      <NCard title="Informations de la propriété" class="mb-6" v-if="propriete">
        <NDescriptions :columns="2" bordered>
          <NDescriptionsItem label="Nom">{{ propriete.nom }}</NDescriptionsItem>
          <NDescriptionsItem label="Type">{{ propriete.typeBien }}</NDescriptionsItem>
          <NDescriptionsItem label="Adresse" :span="2">
            {{ propriete.adresse }}, {{ propriete.codePostal }} {{ propriete.ville }}
          </NDescriptionsItem>
        </NDescriptions>
      </NCard>

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
            <NInputNumber
              v-model:value="anneeFiscale"
              placeholder="Année fiscale"
              :min="2020"
              :max="2030"
              @update:value="chargerDonnees"
            />
          </div>

          <NDataTable
            :columns="[
              { title: 'Date', key: 'dateEcriture', width: 120 },
              { title: 'Type', key: 'type', width: 100 },
              { title: 'Montant', key: 'montant', width: 120, render: (row) => `${row.montant} €` },
              { title: 'Commentaire', key: 'commentaire', width: 200 },
            ]"
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
              <NInputNumber
                v-model:value="chargeEnCours.montant"
                placeholder="0.00"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Date" required>
              <NInput v-model:value="chargeEnCours.dateCharge" type="date" />
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
          <NButton type="primary" @click="sauvegarderCharge">
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
              <NInputNumber
                v-model:value="recetteEnCours.montant"
                placeholder="0.00"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Date" required>
              <NInput v-model:value="recetteEnCours.dateRecette" type="date" />
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
          <NButton type="primary" @click="sauvegarderRecette">
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

.mr-4 {
  margin-right: 1rem;
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