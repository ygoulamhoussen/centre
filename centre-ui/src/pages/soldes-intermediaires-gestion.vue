<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useAppStore } from '@/store/modules/app'
import { useWindowSize } from '@vueuse/core'
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
  NTooltip,
  NStatistic,
  NDivider,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import {
  Calculator24Filled,
  ArrowDownload24Filled,
  Money24Filled,
  DataTrending24Filled,
  ArrowTrending24Filled,
  ArrowClockwise24Filled,
} from '@vicons/fluent'
import { computed, onMounted, ref, watch } from 'vue'
import type { SoldesIntermediairesGestionDTO } from '@/types/dto'
import { getSoldesIntermediairesGestion } from '@/service/api/charges-recettes'

definePage({
  meta: {
    title: 'Soldes Intermédiaires de Gestion',
    icon: 'material-symbols:calculate',
    hideInMenu: true,
    order: 10,
  },
})



const authStore = useAuthStore()
const message = useMessage()
const appStore = useAppStore()
const { width } = useWindowSize()
const isMobile = computed(() => width.value <= 640)

// États réactifs
const soldes = ref<SoldesIntermediairesGestionDTO | null>(null)
const anneeFiscale = ref(new Date().getFullYear())
const chargement = ref(false)
const afficherDetails = ref(false)

// Colonnes pour les tableaux de détails
const colonnesProduction = [
  { title: 'Libellé', key: 'libelle', width: 300 },
  { title: 'Date', key: 'date', width: 120 },
  { title: 'Compte', key: 'compteNum', width: 100 },
  { 
    title: 'Montant', 
    key: 'montant', 
    width: 150,
    render: (row: any) => `${row.montant.toFixed(2)} €`
  },
  { title: 'Commentaire', key: 'commentaire', width: 200 },
]

const colonnesConsommation = [
  { title: 'Libellé', key: 'libelle', width: 300 },
  { title: 'Date', key: 'date', width: 120 },
  { title: 'Compte', key: 'compteNum', width: 100 },
  { 
    title: 'Montant', 
    key: 'montant', 
    width: 150,
    render: (row: any) => `${row.montant.toFixed(2)} €`
  },
  { title: 'Commentaire', key: 'commentaire', width: 200 },
]

const colonnesChargesPersonnel = [
  { title: 'Libellé', key: 'libelle', width: 300 },
  { title: 'Date', key: 'date', width: 120 },
  { title: 'Compte', key: 'compteNum', width: 100 },
  { 
    title: 'Montant', 
    key: 'montant', 
    width: 150,
    render: (row: any) => `${row.montant.toFixed(2)} €`
  },
  { title: 'Commentaire', key: 'commentaire', width: 200 },
]

const colonnesChargesFinancieres = [
  { title: 'Libellé', key: 'libelle', width: 300 },
  { title: 'Date', key: 'date', width: 120 },
  { title: 'Compte', key: 'compteNum', width: 100 },
  { 
    title: 'Montant', 
    key: 'montant', 
    width: 150,
    render: (row: any) => `${row.montant.toFixed(2)} €`
  },
  { title: 'Commentaire', key: 'commentaire', width: 200 },
]

const colonnesExceptionnel = [
  { title: 'Libellé', key: 'libelle', width: 300 },
  { title: 'Date', key: 'date', width: 120 },
  { title: 'Compte', key: 'compteNum', width: 100 },
  { 
    title: 'Montant', 
    key: 'montant', 
    width: 150,
    render: (row: any) => `${row.montant.toFixed(2)} €`
  },
  { title: 'Commentaire', key: 'commentaire', width: 200 },
]

// Fonctions
async function chargerSoldes() {
  if (!authStore.userInfo.userId) return

  chargement.value = true
  try {
    const data = await getSoldesIntermediairesGestion(anneeFiscale.value, authStore.userInfo.userId)
    soldes.value = data
    message.success('Soldes intermédiaires de gestion calculés avec succès')
  } catch (error: any) {
    console.error('Erreur lors du calcul des soldes:', error)
    message.error(`Erreur lors du calcul des soldes: ${error.message}`)
  } finally {
    chargement.value = false
  }
}

// Calculs dérivés
const totalDotations = computed(() => {
  if (!soldes.value) return 0
  return soldes.value.dotationsAmortissements + soldes.value.dotationsProvisions
})

const resultatFinancier = computed(() => {
  if (!soldes.value) return 0
  return soldes.value.produitsFinanciers - soldes.value.chargesFinancieres
})

// Watchers
watch(anneeFiscale, () => {
  chargerSoldes()
})

// Initialisation
onMounted(() => {
  chargerSoldes()
})
</script>

<template>
  <div class="soldes-intermediaires-gestion">
    <NH1 class="page-title">
      <NIcon size="24" class="mr-2">
        <Calculator24Filled />
      </NIcon>
      📄 2033-G — Soldes Intermédiaires de Gestion (SIG)
    </NH1>

    <NAlert type="info" class="mb-4">
      <template #header>
        <strong>Informations</strong>
      </template>
      Ce formulaire calcule automatiquement les soldes intermédiaires de gestion à partir des données comptables.
      Il ne contient aucune donnée saisie directement par l'utilisateur.
    </NAlert>

    <!-- Sélecteur d'année -->
    <NCard title="Paramètres" class="mb-4">
      <NGrid :cols="isMobile ? 1 : 3" :x-gap="12">
        <NGi>
          <NFormItem label="Année fiscale">
            <NInputNumber
              v-model:value="anneeFiscale"
              :min="2020"
              :max="2030"
              placeholder="Année"
              clearable
            />
          </NFormItem>
        </NGi>
        <NGi>
          <NButton
            type="primary"
            :loading="chargement"
            @click="chargerSoldes"
            class="mt-6"
          >
            <template #icon>
              <NIcon>
                <ArrowClockwise24Filled />
              </NIcon>
            </template>
            Recalculer
          </NButton>
        </NGi>
        <NGi>
          <NButton
            type="info"
            @click="afficherDetails = !afficherDetails"
            class="mt-6"
          >
            <template #icon>
              <NIcon>
                <Money24Filled />
              </NIcon>
            </template>
            {{ afficherDetails ? 'Masquer' : 'Afficher' }} les détails
          </NButton>
        </NGi>
      </NGrid>
    </NCard>

    <!-- Résultats -->
    <div v-if="soldes" class="results-section">
      <!-- Tableau principal des soldes -->
      <div v-if="!isMobile">
        <NCard title="Soldes Intermédiaires de Gestion" class="mb-4">
          <NGrid :cols="2" :x-gap="24" :y-gap="16">
            <!-- Production de l'exercice -->
            <NGi>
              <NStatistic
                label="Production de l'exercice"
                :value="soldes.productionExercice"
                :precision="2"
                suffix="€"
                color="#52c41a"
              >
                <template #prefix>
                  <NIcon color="#52c41a">
                    <DataTrending24Filled />
                  </NIcon>
                </template>
              </NStatistic>
            </NGi>

            <!-- Consommation en provenance des tiers -->
            <NGi>
              <NStatistic
                label="Consommation en provenance des tiers"
                :value="soldes.consommationTiers"
                :precision="2"
                suffix="€"
                color="#ff4d4f"
              >
                <template #prefix>
                  <NIcon color="#ff4d4f">
                    <ArrowTrending24Filled />
                  </NIcon>
                </template>
              </NStatistic>
            </NGi>

            <!-- Valeur ajoutée -->
            <NGi>
              <NStatistic
                label="Valeur ajoutée"
                :value="soldes.valeurAjoutee"
                :precision="2"
                suffix="€"
                :color="soldes.valeurAjoutee >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>

            <!-- Charges de personnel -->
            <NGi>
              <NStatistic
                label="Charges de personnel"
                :value="soldes.chargesPersonnel"
                :precision="2"
                suffix="€"
                color="#ff4d4f"
              />
            </NGi>

            <!-- Excédent brut d'exploitation -->
            <NGi>
              <NStatistic
                label="Excédent brut d'exploitation (EBE)"
                :value="soldes.excedentBrutExploitation"
                :precision="2"
                suffix="€"
                :color="soldes.excedentBrutExploitation >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>

            <!-- Dotations -->
            <NGi>
              <NStatistic
                label="Dotations (amortissements + provisions)"
                :value="totalDotations"
                :precision="2"
                suffix="€"
                color="#ff4d4f"
              />
            </NGi>

            <!-- Résultat d'exploitation -->
            <NGi>
              <NStatistic
                label="Résultat d'exploitation"
                :value="soldes.resultatExploitation"
                :precision="2"
                suffix="€"
                :color="soldes.resultatExploitation >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>

            <!-- Résultat financier -->
            <NGi>
              <NStatistic
                label="Résultat financier"
                :value="resultatFinancier"
                :precision="2"
                suffix="€"
                :color="resultatFinancier >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>

            <!-- Résultat courant avant impôts -->
            <NGi>
              <NStatistic
                label="Résultat courant avant impôts"
                :value="soldes.resultatCourantAvantImpot"
                :precision="2"
                suffix="€"
                :color="soldes.resultatCourantAvantImpot >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>

            <!-- Résultat exceptionnel -->
            <NGi>
              <NStatistic
                label="Résultat exceptionnel"
                :value="soldes.resultatExceptionnel"
                :precision="2"
                suffix="€"
                :color="soldes.resultatExceptionnel >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>

            <!-- Impôts sur bénéfices -->
            <NGi>
              <NStatistic
                label="Impôts sur bénéfices"
                :value="soldes.impotsSurBenefices"
                :precision="2"
                suffix="€"
                color="#ff4d4f"
              />
            </NGi>

            <!-- Résultat net -->
            <NGi>
              <NStatistic
                label="Résultat net"
                :value="soldes.resultatNet"
                :precision="2"
                suffix="€"
                :color="soldes.resultatNet >= 0 ? '#52c41a' : '#ff4d4f'"
              />
            </NGi>
          </NGrid>
        </NCard>
      </div>
      <div v-else class="summary-cards-mobile">
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Production de l'exercice" :value="soldes.productionExercice" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Consommation en provenance des tiers" :value="soldes.consommationTiers" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Valeur ajoutée" :value="soldes.valeurAjoutee" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Charges de personnel" :value="soldes.chargesPersonnel" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Excédent brut d'exploitation (EBE)" :value="soldes.excedentBrutExploitation" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Dotations (amortissements + provisions)" :value="totalDotations" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Résultat d'exploitation" :value="soldes.resultatExploitation" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Résultat financier" :value="resultatFinancier" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Résultat courant avant impôts" :value="soldes.resultatCourantAvantImpot" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Résultat exceptionnel" :value="soldes.resultatExceptionnel" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Impôts sur bénéfices" :value="soldes.impotsSurBenefices" :precision="2" suffix="€" /></NCard>
        <NCard class="summary-mobile-card mb-2"><NStatistic label="Résultat net" :value="soldes.resultatNet" :precision="2" suffix="€" /></NCard>
      </div>

      <!-- Détails (optionnels) -->
      <div v-if="afficherDetails">
        <NTabs type="line" animated>
          <!-- Détails Production -->
          <NTabPane name="production" tab="Production de l'exercice">
            <NCard>
              <NDataTable
                :columns="colonnesProduction"
                :data="soldes.detailsProduction || []"
                :pagination="{ pageSize: 10 }"
                :bordered="false"
                striped
              />
            </NCard>
          </NTabPane>

          <!-- Détails Consommation -->
          <NTabPane name="consommation" tab="Consommation en provenance des tiers">
            <NCard>
              <NDataTable
                :columns="colonnesConsommation"
                :data="soldes.detailsConsommation || []"
                :pagination="{ pageSize: 10 }"
                :bordered="false"
                striped
              />
            </NCard>
          </NTabPane>

          <!-- Détails Charges Personnel -->
          <NTabPane name="personnel" tab="Charges de personnel">
            <NCard>
              <NDataTable
                :columns="colonnesChargesPersonnel"
                :data="soldes.detailsChargesPersonnel || []"
                :pagination="{ pageSize: 10 }"
                :bordered="false"
                striped
              />
            </NCard>
          </NTabPane>

          <!-- Détails Charges Financières -->
          <NTabPane name="financieres" tab="Charges et Produits Financiers">
            <NCard>
              <NDataTable
                :columns="colonnesChargesFinancieres"
                :data="soldes.detailsChargesFinancieres || []"
                :pagination="{ pageSize: 10 }"
                :bordered="false"
                striped
              />
            </NCard>
          </NTabPane>

          <!-- Détails Exceptionnel -->
          <NTabPane name="exceptionnel" tab="Opérations Exceptionnelles">
            <NCard>
              <NDataTable
                :columns="colonnesExceptionnel"
                :data="soldes.detailsExceptionnel || []"
                :pagination="{ pageSize: 10 }"
                :bordered="false"
                striped
              />
            </NCard>
          </NTabPane>
        </NTabs>
      </div>

      <!-- Informations complémentaires -->
      <NCard title="Informations complémentaires" class="mt-4">
        <NDescriptions :column="isMobile ? 1 : 2" bordered>
          <NDescriptionsItem label="Charges de personnel">
            {{ soldes.chargesPersonnel.toFixed(2) }} €
            <NTag v-if="soldes.chargesPersonnel === 0" type="success" size="small" class="ml-2">
              Typique en LMNP
            </NTag>
          </NDescriptionsItem>
          <NDescriptionsItem label="Dotations aux amortissements">
            {{ soldes.dotationsAmortissements.toFixed(2) }} €
          </NDescriptionsItem>
          <NDescriptionsItem label="Dotations aux provisions">
            {{ soldes.dotationsProvisions.toFixed(2) }} €
          </NDescriptionsItem>
          <NDescriptionsItem label="Charges financières">
            {{ soldes.chargesFinancieres.toFixed(2) }} €
          </NDescriptionsItem>
          <NDescriptionsItem label="Produits financiers">
            {{ soldes.produitsFinanciers.toFixed(2) }} €
          </NDescriptionsItem>
          <NDescriptionsItem label="Produits exceptionnels">
            {{ soldes.produitsExceptionnels.toFixed(2) }} €
          </NDescriptionsItem>
          <NDescriptionsItem label="Charges exceptionnelles">
            {{ soldes.chargesExceptionnelles.toFixed(2) }} €
          </NDescriptionsItem>
          <NDescriptionsItem label="Impôts sur bénéfices">
            {{ soldes.impotsSurBenefices.toFixed(2) }} €
          </NDescriptionsItem>
        </NDescriptions>
      </NCard>
    </div>

    <!-- État de chargement -->
    <div v-else-if="chargement" class="loading-state">
      <NCard>
        <div class="text-center py-8">
          <NIcon size="48" class="mb-4">
            <Calculator24Filled />
          </NIcon>
          <NText>Calcul des soldes intermédiaires de gestion en cours...</NText>
        </div>
      </NCard>
    </div>

    <!-- État vide -->
    <div v-else class="empty-state">
      <NCard>
        <div class="text-center py-8">
          <NIcon size="48" class="mb-4">
            <Calculator24Filled />
          </NIcon>
          <NText>Aucune donnée disponible pour l'année {{ anneeFiscale }}</NText>
          <br>
          <NText depth="3">Vérifiez que vous avez des écritures comptables pour cette année</NText>
        </div>
      </NCard>
    </div>
  </div>
</template>

<style scoped>
.soldes-intermediaires-gestion {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.results-section {
  margin-top: 20px;
}

.loading-state,
.empty-state {
  margin-top: 20px;
}

.mr-2 {
  margin-right: 8px;
}

.ml-2 {
  margin-left: 8px;
}

.mb-4 {
  margin-bottom: 16px;
}

.mt-4 {
  margin-top: 16px;
}

.mt-6 {
  margin-top: 24px;
}

.text-center {
  text-align: center;
}

.py-8 {
  padding-top: 32px;
  padding-bottom: 32px;
}

.summary-cards-mobile {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 18px;
}
.summary-mobile-card {
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  border-radius: 10px;
  padding: 10px 8px;
}
@media (max-width: 640px) {
  .summary-mobile-card {
    font-size: 0.97rem;
    padding: 10px 6px;
  }
}

@media (max-width: 768px) {
  .soldes-intermediaires-gestion {
    padding: 10px;
  }
}
</style> 