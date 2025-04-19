<script setup>
import { ref } from 'vue'

const tabTitles = [
  { label: 'Tableau de bord', slot: 'dashboard' },
  { label: 'Propriétés', slot: 'proprietes' },
  { label: 'Locataires', slot: 'locataires' },
  { label: 'Locations', slot: 'locations' },
  { label: 'Quittances', slot: 'quittances' },
  { label: 'Immobilisations', slot: 'immobilisations' },
  { label: 'Crédits', slot: 'credits' },
  { label: 'Comptabilité', slot: 'comptabilite' },
  { label: 'Documents', slot: 'documents' }
]

const activeTab = ref(0)
</script>

<template>
  <n-card class="fr-my-2w">
    <n-tabs v-model:value="activeTab" type="line">
      <n-tab-pane
        v-for="(tab, idx) in tabTitles"
        :key="tab.slot"
        :name="idx"
        :tab="tab.label"
      >
        <template v-if="tab.slot === 'dashboard'">
          <div>Vue d'ensemble du tableau de bord</div>
        </template>
        <template v-else-if="tab.slot === 'proprietes'">
          <div>Gestion des propriétés : ajout, détails, acquisition</div>
        </template>
        <template v-else-if="tab.slot === 'locataires'">
          <div>Suivi des locataires et de leurs informations</div>
        </template>
        <template v-else-if="tab.slot === 'locations'">
          <div>Suivi des baux, loyers, fréquence et échéances</div>
        </template>
        <template v-else-if="tab.slot === 'quittances'">
          <div>Création des quittances et suivi des paiements</div>
        </template>
        <template v-else-if="tab.slot === 'immobilisations'">
          <div>Décomposition des biens en immobilisations amortissables</div>
        </template>
        <template v-else-if="tab.slot === 'credits'">
          <div>Suivi des crédits immobiliers et échéanciers</div>
        </template>
        <template v-else-if="tab.slot === 'comptabilite'">
          <div>Journal comptable, écritures, clôture et bilan fiscal LMNP</div>
        </template>
        <template v-else-if="tab.slot === 'documents'">
          <div>Centralisation et gestion des documents liés</div>
        </template>
      </n-tab-pane>
    </n-tabs>
  </n-card>
</template>
