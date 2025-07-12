<script setup lang="ts">
import type { EcritureComptableDTO, LigneEcritureDTO } from '@/types/dto'
import { downloadDocument, getEcrituresComptablesWithProprieteNom } from '@/service/api/charges-recettes'
import { useAuthStore } from '@/store/modules/auth'
import { ArrowDownload24Filled, Document24Filled } from '@vicons/fluent'
import { NButton, NCard, NDataTable, NH1, NIcon, NSelect, NSpace, NTag, NTooltip, useMessage } from 'naive-ui'
import { computed, h, onMounted, ref } from 'vue'

definePage({
  meta: {
    title: 'Fichier des Écritures Comptables (FEC)',
    icon: 'mdi:file-table-outline',
    order: 11,
  },
})

const authStore = useAuthStore()
const message = useMessage()
const lignesFEC = ref<any[]>([])
const chargement = ref(false)
const annees = ref<number[]>([])
const anneeSelectionnee = ref<number | null>(null)

const colonnesFEC = [
  { title: 'JournalCode', key: 'journalCode', width: 80 },
  { title: 'JournalLib', key: 'journalLib', width: 160 },
  { title: 'EcritureNum', key: 'ecritureNum', width: 120 },
  { title: 'DateEcriture', key: 'dateEcriture', width: 110 },
  { title: 'CompteNum', key: 'compteNum', width: 100 },
  { title: 'CompteLib', key: 'compteLibelle', width: 180 },
  { title: 'CompAuxNum', key: 'tiers', width: 120 },
  { title: 'CompAuxLib', key: 'tiers', width: 120 },
  { title: 'PieceRef', key: 'numeroPiece', width: 120 },
  { title: 'PieceDate', key: 'pieceDate', width: 110 },
  { title: 'LibelleEcriture', key: 'libelle', width: 200 },
  { title: 'MontantDebit', key: 'debit', width: 110, render: (row: any) => row.debit ? Number(row.debit).toFixed(2) : '' },
  { title: 'MontantCredit', key: 'credit', width: 110, render: (row: any) => row.credit ? Number(row.credit).toFixed(2) : '' },
  {
    title: 'Document',
    key: 'document',
    width: 120,
    render: (row: any) => {
      if (row.documentId && row.documentNom) {
        return h(
          NTooltip,
          { placement: 'top' },
          {
            trigger: () =>
              h(
                NIcon,
                {
                  component: ArrowDownload24Filled,
                  size: 20,
                  style: { cursor: 'pointer', color: '#2563eb' },
                  onClick: () => handleDownloadDocument(row.documentId, row.documentNom),
                  title: row.documentNom,
                },
              ),
            default: () => row.documentNom,
          },
        )
      }
      return h('span', { style: { color: '#999' } }, 'Aucun document')
    },
  },
]

const lignesFECFiltrees = computed(() => {
  if (!anneeSelectionnee.value) {
    return lignesFEC.value
  }
  return lignesFEC.value.filter(l => l.dateEcriture && new Date(l.dateEcriture).getFullYear() === anneeSelectionnee.value)
})

async function chargerEcritures() {
  chargement.value = true
  try {
    const data = await getEcrituresComptablesWithProprieteNom(authStore.userInfo.userId)
    // Pour chaque écriture, on a potentiellement plusieurs lignes d'écriture
    const lignes: any[] = []
    for (const e of data as any[]) {
      if (Array.isArray(e.lignes)) {
        for (const l of e.lignes as LigneEcritureDTO[]) {
          lignes.push({
            journalCode: e.journalCode,
            journalLib: e.journalLib,
            ecritureNum: e.id,
            dateEcriture: e.dateEcriture,
            compteNum: l.compteNum,
            compteLibelle: l.compteLibelle,
            tiers: l.tiers,
            numeroPiece: e.numeroPiece,
            pieceDate: e.pieceDate,
            libelle: e.libelle,
            debit: l.debit,
            credit: l.credit,
            documentId: e.documentId,
            documentNom: e.documentNom,
          })
        }
      }
    }
    lignesFEC.value = lignes
    // Calcul des années disponibles
    const anneesUniques = Array.from(new Set(
      lignes
        .map(l => l.dateEcriture ? new Date(l.dateEcriture).getFullYear() : null)
        .filter((a): a is number => a !== null),
    )).sort((a, b) => b - a)
    annees.value = anneesUniques
    if (annees.value.length > 0) {
      anneeSelectionnee.value = annees.value[0]
    }
  } catch (error: any) {
    message.error(error.message || 'Erreur lors du chargement des écritures')
  } finally {
    chargement.value = false
  }
}

onMounted(() => {
  chargerEcritures()
})

async function telechargerFEC() {
  try {
    const userId = authStore.userInfo.userId
    const annee = anneeSelectionnee.value
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/fec?utilisateurId=${userId}&annee=${annee}`
    const response = await fetch(url, { method: 'GET', credentials: 'include' })

    if (!response.ok) {
      throw new Error('Erreur lors du téléchargement du FEC')
    }

    const blob = await response.blob()
    const urlBlob = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = urlBlob
    a.download = `FEC-${annee}.txt`
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(urlBlob)
    document.body.removeChild(a)
    message.success('FEC téléchargé avec succès')
  } catch {
    message.error('Erreur lors du téléchargement du FEC')
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
      },
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
  } catch (error: any) {
    console.error('Erreur lors du téléchargement du document:', error)
    message.error(error.message || 'Erreur lors du téléchargement du document')
  }
}
</script>

<template>
  <div class="p-4 compta-bg">
    <NCard :bordered="false">
      <div class="flex items-center justify-between mb-6">
        <div>
          <NH1 class="titre-principal text-2xl mb-2">Fichier des Écritures Comptables (FEC)</NH1>
          <p class="text-gray-600">Visualisez et exportez toutes vos écritures comptables</p>
        </div>
        <NButton type="primary" @click="telechargerFEC">
          <template #icon>
            <NIcon :component="ArrowDownload24Filled" />
          </template>
          Télécharger le FEC
        </NButton>
      </div>
      <div class="annee-row-wrapper mb-4">
        <span class="label-annee">Année :</span>
        <NSelect
          v-model:value="anneeSelectionnee"
          :options="annees.map(a => ({ label: a ? a.toString() : '', value: a }))"
          placeholder="Choisir une année"
          style="width: 120px"
        />
      </div>
      <NDataTable
        :columns="colonnesFEC"
        :data="lignesFECFiltrees"
        :loading="chargement"
        :pagination="{ pageSize: 20 }"
        striped
        class="fec-table"
      />
    </NCard>
  </div>
</template>

<style scoped>
.titre-principal {
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
.text-gray-600 {
  color: #6b7280;
}
.annee-row-wrapper {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.label-annee {
  font-size: 1rem;
}
.compta-bg {
  background-color: var(--n-body-color);
  min-height: 100vh;
}
.fec-table :deep(.n-data-table-th) {
  position: sticky;
  top: 0;
  background: #f8fafc;
  z-index: 2;
  font-weight: bold;
}
.fec-table :deep(.n-data-table-td) {
  vertical-align: middle;
  font-size: 0.98rem;
  padding: 8px 6px;
}
.fec-table :deep(.n-data-table-tr) {
  transition: background 0.2s;
}
.fec-table :deep(.n-data-table-tr):hover {
  background: #e0e7ef;
}
.fec-table :deep(.n-data-table-td[data-col-key='MontantDebit']),
.fec-table :deep(.n-data-table-td[data-col-key='MontantCredit']) {
  text-align: right;
  font-variant-numeric: tabular-nums;
  font-weight: 500;
}
.fec-table :deep(.n-data-table-td[data-col-key='MontantDebit'])::after,
.fec-table :deep(.n-data-table-td[data-col-key='MontantCredit'])::after {
  content: ' €';
  color: #888;
  font-size: 0.95em;
}
.fec-table :deep(.n-data-table-th[data-col-key='LibelleEcriture']) {
  min-width: 220px;
}
.fec-table :deep(.n-data-table-th[data-col-key='CompteLib']) {
  min-width: 180px;
}
.fec-table :deep(.n-data-table-th[data-col-key='JournalLib']) {
  min-width: 160px;
}
</style> 