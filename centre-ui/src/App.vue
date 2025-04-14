<script setup lang="ts">
import { ref } from 'vue'
import { useRegisterSW } from 'virtual:pwa-register/vue'
import useToaster from './composables/use-toaster'
import { DsfrFooter } from '@gouvminint/vue-dsfr'

// Configuration de l'en-tête DSFR (qui se rapproche du menu de Tomappart)
const serviceTitle = 'Tikaz'
const serviceDescription = ''
const logoText = ['Application', 'de Yussouf']
const quickLinks = [
  {
    label: 'Se connecter',
    to: '/login',
    icon: 'ri-home-4-line',
    iconAttrs: { color: 'var(--red-marianne-425-625)' },
  },
  {
    label: 'Commencer',
    to: '/a-propos',
    icon: 'ri-question-mark',
    iconRight: true,
  },
]

// Variable liée à la barre de recherche intégrée dans l'en-tête DSFR
const searchQuery = ref('')

// Utilisation du service worker pour la gestion du mode hors-ligne et actualisation
const { offlineReady, needRefresh, updateServiceWorker } = useRegisterSW()

function close () {
  offlineReady.value = false
  needRefresh.value = false
}

// Pour la gestion des notifications/toasts
const toaster = useToaster()


const navItems = [
  {
    "to": "/",
    "text": "Acceuil"
  },
  {
    "to": "/fonctionnalites",
    "text": "Fonctionnalités"
  },
  {
    "to": "/tarifs",
    "text": "Tarifs"
  },
  {
    "to": "/gestion",
    "text": "Gestion"
  },

];
</script>

<template>
  <!-- En-tête DSFR -->
  <DsfrHeader
    v-model="searchQuery"
    :service-title="serviceTitle"
    :service-description="serviceDescription"
    :logo-text="logoText"
    :quick-links="quickLinks"
    operatorImgSrc="/centre-ui/assets/logo-tikaz.svg"
  >
    <!-- Barre de navigation DSFR recréant le menu -->
    <nav class="fr-nav">
      <div class="fr-container">
        <ul class="fr-nav__list">

          <DsfrNavigation :navItems="navItems" />
        </ul>
      </div>
    </nav>
  </DsfrHeader>

  <!-- Contenu principal de l'application -->
  <div class="fr-container fr-mt-3w fr-mt-md-5w fr-mb-5w">
    <router-view />
  </div>

  <!-- Autres composants -->
  <ReloadPrompt
    :offline-ready="offlineReady"
    :need-refresh="needRefresh"
    @close="close()"
    @update-service-worker="updateServiceWorker()"
  />
  <AppToaster
    :messages="toaster.messages"
    @close-message="toaster.removeMessage($event)"
  />
  <DsfrFooter></DsfrFooter>
</template>
