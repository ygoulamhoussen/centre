<template>
  <div>
    <h2>{{ titleGestion }}</h2>
    <n-grid :cols="2" x-gap="16" y-gap="16">
      <n-grid-item v-for="tile in tiles" :key="tile.title">
        <n-card :title="tile.title" hoverable>
          <template #cover>
            <n-icon size="48">
              <component :is="tile.icon" />
            </n-icon>
          </template>
          <div>{{ tile.description }}</div>

        </n-card>
      </n-grid-item>
    </n-grid>
    <h2 class="mt-6">{{ titleCompta }}</h2>
    <n-grid :cols="2" x-gap="16" y-gap="16">
      <n-grid-item v-for="tile in tilesCompta" :key="tile.title">
        <n-card :title="tile.title" hoverable>
          <template #cover>
            <n-icon size="48">
              <component :is="tile.icon" />
            </n-icon>
          </template>
          <div>{{ tile.description }}</div>
        </n-card>
      </n-grid-item>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { NGrid, NGridItem, NCard, NButton, NIcon } from 'naive-ui'
// Import d'icônes Naive UI adaptées
import { HomeOutline, PeopleOutline, DocumentTextOutline, ReceiptOutline, CashOutline, BriefcaseOutline, FileTrayFullOutline, FolderOpenOutline } from '@vicons/ionicons5'

const activeAccordion = ref(['gestion'])
const titleGestion = 'Gestion locative'
const titleCompta = 'Comptabilité'
const tiles = [
  {
    to: "ProprieteListView",
    title: "Propriétés",
    icon: HomeOutline,
    description: "Ajoutez et gérez vos biens immobiliers"
  },
  {
    title: "Locataires",
    icon: PeopleOutline,
    description: "Suivez les informations de vos locataires"
  },
  {
    title: "Locations",
    icon: DocumentTextOutline,
    description: "Gérez les baux, loyers et échéances"
  },
  {
    title: "Quittances",
    icon: ReceiptOutline,
    description: "Créez et suivez les quittances de loyer"
  }
]
const tilesCompta = [
  {
    title: "Immobilisations",
    icon: BriefcaseOutline,
    description: "Décomposez et amortissez vos biens"
  },
  {
    title: "Crédits immobiliers",
    icon: CashOutline,
    description: "Suivez vos emprunts et échéanciers"
  },
  {
    title: "Comptabilité",
    icon: FileTrayFullOutline,
    description: "Journal, écritures et clôture de l’exercice"
  },
  {
    title: "Documents",
    icon: FolderOpenOutline,
    description: "Stockez et classez vos justificatifs"
  }
]
</script>