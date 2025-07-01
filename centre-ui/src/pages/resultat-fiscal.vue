<script setup lang="ts">
import type { ResultatFiscal } from '@/service/api/resultat-fiscal'
import { Icon } from '@iconify/vue'
import { computed, onMounted, ref } from 'vue'
import {
  NButton,
  NCard,
  NCollapse,
  NCollapseItem,
  NDataTable,
  NEmpty,
  NGi,
  NGrid,
  NSelect,
  NSpin,
  NStatistic,
  useMessage,
} from 'naive-ui'
import { resultatFiscalApi } from '@/service/api/resultat-fiscal'
import type { DataTableColumns } from 'naive-ui'

definePage({
  meta: {
    title: 'Résultat Fiscal',
    icon: 'material-symbols:account-balance-outline',
    order: 10,
  },
});

const message = useMessage();

// State
const loading = ref(false);
const loadingData = ref(false);
const selectedYear = ref<number | null>(new Date().getFullYear());
const selectedProprieteIds = ref<string[] | null>(null);
const proprietes = ref<any[]>([]);
const resultat = ref<ResultatFiscal | null>(null);
const utilisateurId = '00000000-0000-0000-0000-000000000003'; // Hardcoded for now
const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL

// Options for selects
const yearOptions = computed(() => {
  const currentYear = new Date().getFullYear();
  return Array.from({ length: 5 }, (_, i) => ({
    label: (currentYear - i).toString(),
    value: currentYear - i,
  }));
});

const proprieteOptions = computed(() =>
  proprietes.value.map(p => ({
    label: p.nom,
    value: p.id,
  })),
);

const allProprieteIds = computed(() => proprietes.value.map(p => p.id));

// Functions
async function loadProprietes() {
  loadingData.value = true;
  try {
    // Assuming a generic API to fetch properties, similar to other pages
    const response = await fetch(`${baseUrl}/api/getProprietesByUtilisateur/${utilisateurId}`);
    proprietes.value = await response.json();
    // Select all properties by default
    selectedProprieteIds.value = allProprieteIds.value;
  } catch (error) {
    message.error('Erreur lors du chargement des propriétés');
    console.error(error);
  } finally {
    loadingData.value = false;
  }
}

async function calculerResultat() {
  if (!selectedYear.value || !selectedProprieteIds.value || selectedProprieteIds.value.length === 0) {
    message.warning('Veuillez sélectionner une année et au moins une propriété.');
    return;
  }

  loading.value = true;
  resultat.value = null;
  try {
    resultat.value = await resultatFiscalApi.calculerResultatFiscal(
      selectedYear.value,
      selectedProprieteIds.value,
      utilisateurId,
    );
  } catch (error) {
    message.error('Erreur lors du calcul du résultat fiscal.');
    console.error(error);
  } finally {
    loading.value = false;
  }
}

function formatCurrency(value?: number) {
  if (value === undefined || value === null) return 'N/A';
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(value);
}

function formatDate(dateString: string) {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('fr-FR');
}

// Columns for detail tables
const recetteColumns: DataTableColumns<RecetteDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Date', key: 'dateRecette', render: row => formatDate(row.dateRecette) },
  { title: 'Montant', key: 'montant', render: row => formatCurrency(row.montant), align: 'right' },
]

const chargeColumns: DataTableColumns<ChargeDetail> = [
  { title: 'Intitulé', key: 'intitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Nature', key: 'nature' },
  { title: 'Date', key: 'dateCharge', render: row => formatDate(row.dateCharge) },
  { title: 'Montant', key: 'montant', render: row => formatCurrency(row.montant), align: 'right' },
]

const amortissementColumns: DataTableColumns<AmortissementDetail> = [
  { title: 'Immobilisation', key: 'immobilisationIntitule' },
  { title: 'Propriété', key: 'proprieteNom' },
  { title: 'Année', key: 'annee' },
  { title: 'Montant', key: 'montantAmortissement', render: row => formatCurrency(row.montantAmortissement), align: 'right' },
]

function getResultatClass(value?: number) {
  if (value === undefined || value === null) return '';
  return value > 0 ? 'result-positive' : 'result-negative';
}

onMounted(async () => {
  await loadProprietes();
  if (proprietes.value.length > 0) {
    await calculerResultat();
  }
});
</script>

<template>
  <div class="resultat-fiscal-page">
    <div class="page-header">
      <h1>Résultat Fiscal Annuel</h1>
    </div>

    <NCard class="filters-card">
      <div class="filters">
        <NSelect
          v-model:value="selectedYear"
          :options="yearOptions"
          placeholder="Année"
          style="width: 150px"
        />
        <NSelect
          v-model:value="selectedProprieteIds"
          :options="proprieteOptions"
          placeholder="Toutes les propriétés"
          multiple
          clearable
          style="flex-grow: 1; min-width: 250px;"
        />
        <NButton type="primary" :loading="loading" @click="calculerResultat">
          <template #icon>
            <Icon icon="material-symbols:calculate-outline" />
          </template>
          Calculer
        </NButton>
      </div>
    </NCard>

    <div v-if="loading" class="loading-container">
      <NSpin size="large" />
    </div>

    <div v-if="!loading && resultat" class="result-container">
      <NGrid :x-gap="20" :y-gap="20" cols="1 s:2 l:4" responsive="screen">
        <NGi>
          <NCard>
            <NStatistic label="Recettes locatives" :value="formatCurrency(resultat.recettesLocatives)" />
          </NCard>
        </NGi>
        <NGi>
          <NCard>
            <NStatistic label="Charges déductibles" :value="formatCurrency(resultat.chargesDeductibles)" />
          </NCard>
        </NGi>
        <NGi>
          <NCard>
            <NStatistic label="Amortissements" :value="formatCurrency(resultat.amortissements)" />
          </NCard>
        </NGi>
        <NGi>
          <NCard>
            <NStatistic label="Résultat Fiscal" :class="getResultatClass(resultat.resultatFiscal)">
              {{ formatCurrency(resultat.resultatFiscal) }}
            </NStatistic>
          </NCard>
        </NGi>
      </NGrid>
      <div class="actions-footer">
         <p v-if="resultat.resultatFiscal <= 0">
           Votre résultat fiscal est négatif ou nul. Vous n'êtes probablement pas imposable sur ces revenus au régime réel.
         </p>
         <p v-else>
           Votre résultat fiscal est positif. Vous êtes probablement imposable sur ces revenus.
         </p>
         <NButton type="default">
           <template #icon>
             <Icon icon="material-symbols:download" />
           </template>
           Générer la liasse 2042-C PRO (à venir)
         </NButton>
      </div>
    </div>
    
    <div v-if="!loading && !resultat" class="empty-state">
       <NEmpty description="Aucun résultat à afficher. Veuillez lancer un calcul." />
    </div>

    <NCollapse arrow-placement="right" class="details-collapse">
      <NCollapseItem title="Détail des recettes" :name="1">
        <NDataTable :columns="recetteColumns" :data="resultat?.recettesDetail ?? []" :bordered="false" size="small" />
      </NCollapseItem>
      <NCollapseItem title="Détail des charges" :name="2">
        <NDataTable :columns="chargeColumns" :data="resultat?.chargesDetail ?? []" :bordered="false" size="small" />
      </NCollapseItem>
      <NCollapseItem title="Détail des amortissements" :name="3">
        <NDataTable :columns="amortissementColumns" :data="resultat?.amortissementsDetail ?? []" :bordered="false" size="small" />
      </NCollapseItem>
    </NCollapse>

  </div>
</template>

<style scoped>
.resultat-fiscal-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
}

.filters-card {
  margin-bottom: 20px;
}

.filters {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.loading-container {
  display: flex;
  justify-content: center;
  padding: 40px;
}

.result-container {
  margin-top: 20px;
}

.details-collapse {
  margin-top: 20px;
}

.actions-footer {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
  text-align: center;
}

.empty-state {
  margin-top: 40px;
}

:deep(.result-positive .n-statistic-value) {
  color: #d03050; /* naive-ui error color */
}

:deep(.result-negative .n-statistic-value) {
  color: #18a058; /* naive-ui success color */
}
</style> 