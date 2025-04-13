<script setup lang="ts">
import { ref } from 'vue'
import { useRegisterSW } from 'virtual:pwa-register/vue'
import useToaster from './composables/use-toaster'
import { DsfrFooter } from '@gouvminint/vue-dsfr'

// Configuration de l'en-tête DSFR (qui se rapproche du menu de Tomappart)
const serviceTitle = 'Tikaz'
const serviceDescription = ''
const logoText = ['Ministère', 'de l’intérieur']
const quickLinks = [
  {
    label: 'Se connecter',
    to: '/',
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
</script>

<template>
  <!-- En-tête DSFR avec recherche, logo et liens rapides -->
  <DsfrHeader
    v-model="searchQuery"
    :service-title="serviceTitle"
    :service-description="serviceDescription"
    :logo-text="logoText"
    :quick-links="quickLinks"
    operatorImgSrc ="/centre-ui/assets/logo-tikaz.svg"
  >



    <!-- Barre de navigation DSFR recréant le menu de Tomappart -->
    <nav class="fr-nav">
    <div class="fr-container">
      <ul class="fr-nav__list">
        <!-- Exemple de liens de navigation principal -->
        <li class="fr-nav__item">
          <a href="/" class="fr-nav__link">Accueil</a>
        </li>
        <li class="fr-nav__item">
          <a href="/fonctionnalites" class="fr-nav__link">Fonctionnalités</a>
        </li>
        <li class="fr-nav__item">
          <a href="/tarifs" class="fr-nav__link">Tarifs</a>
        </li>
        <!-- Vous pouvez ajouter d'autres liens ou des sous-menus ici si besoin -->
      </ul>
    </div>
  </nav>
<!-- <DsfrHeaderMenuLink label="Accueil" to="/" >
  <DsfrHeaderMenuLinkList navAriaLabel="test"/> 
</DsfrHeaderMenuLink>



<DsfrHeaderMenuLink label="Fonctionnalités" to="/fonctionnalites"/>
<DsfrHeaderMenuLink label="Tarifs" to="/tarifs"/>
<DsfrHeaderMenuLink label="Mon Compte" to="/"/> -->
</DsfrHeader>





  <!-- Contenu principal de l'application -->
  <div class="fr-container fr-mt-3w fr-mt-md-5w fr-mb-5w">
    <router-view />
  </div>

  <!-- Composant de notification pour la gestion du service worker -->
  <ReloadPrompt
    :offline-ready="offlineReady"
    :need-refresh="needRefresh"
    @close="close()"
    @update-service-worker="updateServiceWorker()"
  />

  <!-- Composant pour afficher les messages / toasts -->
  <AppToaster
    :messages="toaster.messages"
    @close-message="toaster.removeMessage($event)"
  />

  <DsfrFooter>
    
  </DsfrFooter>

</template>
