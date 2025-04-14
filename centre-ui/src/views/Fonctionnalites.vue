<script lang="ts" setup>
import { ref } from 'vue'

const tabListName = 'Liste d’onglet'
const tabTitles = [
  { title: 'Tableau de bord', icon: 'ri-dashboard-line', tabId: 'tab-0', panelId: 'tab-content-0' },
  { title: 'Propriétés', icon: 'ri-home-2-line', tabId: 'tab-1', panelId: 'tab-content-1' },
  { title: 'Locataires', icon: 'ri-user-3-line', tabId: 'tab-2', panelId: 'tab-content-2' },
  { title: 'Locations', icon: 'ri-key-line', tabId: 'tab-3', panelId: 'tab-content-3' },
  { title: 'Quittances', icon: 'ri-file-text-line', tabId: 'tab-4', panelId: 'tab-content-4' },
  { title: 'Immobilisations', icon: 'ri-building-line', tabId: 'tab-5', panelId: 'tab-content-5' },
  { title: 'Crédits', icon: 'ri-bank-line', tabId: 'tab-6', panelId: 'tab-content-6' },
  { title: 'Comptabilité', icon: 'ri-calculator-line', tabId: 'tab-7', panelId: 'tab-content-7' },
  { title: 'Documents', icon: 'ri-attachment-line', tabId: 'tab-8', panelId: 'tab-content-8' }
]

const activeTab = ref(0)
const selectPrevious = async () => {
  const newIndex = activeTab.value === 0 ? tabTitles.length - 1 : activeTab.value - 1
  activeTab.value = newIndex
}
const selectNext = async () => {
  const newIndex = activeTab.value === tabTitles.length - 1 ? 0 : activeTab.value + 1
  activeTab.value = newIndex
}
const selectFirst = async () => {
  activeTab.value = 0
}
const selectLast = async () => {
  activeTab.value = tabTitles.length - 1
}
</script>

<template>
  <div class="fr-container fr-my-2w">
    <DsfrTabs
      v-model="activeTab"
      :tab-list-name="tabListName"
      :tabTitles="tabTitles"
    >
      <template #tab-items>
        <DsfrTabItem
          v-for="(tab, index) of tabTitles"
          :key="tab.tabId"
          :tab-id="tab.tabId"
          :panel-id="tab.panelId"
          :icon="tab.icon"
          @click="activeTab = index"
          @next="selectNext()"
          @previous="selectPrevious()"
          @first="selectFirst()"
          @last="selectLast()"
        >
          {{ tab.title }}
        </DsfrTabItem>
      </template>

      <DsfrTabContent panel-id="tab-content-0" tab-id="tab-0">
        <div>Vue d'ensemble du tableau de bord</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-1" tab-id="tab-1">
        <div>Gestion des propriétés : ajout, détails, acquisition</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-2" tab-id="tab-2">
        <div>Suivi des locataires et de leurs informations</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-3" tab-id="tab-3">
        <div>Suivi des baux, loyers, fréquence et échéances</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-4" tab-id="tab-4">
        <div>Création des quittances et suivi des paiements</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-5" tab-id="tab-5">
        <div>Décomposition des biens en immobilisations amortissables</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-6" tab-id="tab-6">
        <div>Suivi des crédits immobiliers et échéanciers</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-7" tab-id="tab-7">
        <div>Journal comptable, écritures, clôture et bilan fiscal LMNP</div>
      </DsfrTabContent>

      <DsfrTabContent panel-id="tab-content-8" tab-id="tab-8">
        <div>Centralisation et gestion des documents liés</div>
      </DsfrTabContent>

    </DsfrTabs>
  </div>
</template>
