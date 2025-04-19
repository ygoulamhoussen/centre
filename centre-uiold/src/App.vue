<script setup lang="ts">
import { ref, computed, h } from 'vue'
import { useRegisterSW } from 'virtual:pwa-register/vue'
import useToaster from './composables/use-toaster'
import { useRoute } from 'vue-router'

//backend
const backendUrl = import.meta.env.VUE_APP_BACKEND_URL || "http://localhost:8080"; // Pour Vite

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

const route = useRoute()


const navItems = [
  {
    "to": "/",
    "text": "Accueil" // Correction ici
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

const menuOptions = computed(() =>
  navItems.map(item => ({
    label: item.text,
    key: item.to,
    to: item.to
  }))
)

// Ajout des imports Naive UI
import { NLayoutHeader, NLayoutFooter, NButton, NIcon, NLayout, NMenu, NLayoutSider, NLayoutContent } from 'naive-ui'
import { LogoGithub, MenuOutline, CloseOutline } from '@vicons/ionicons5'

// État pour le menu responsive
const menuVisible = ref(false)
function toggleMenu() {
  menuVisible.value = !menuVisible.value
}
function closeMenu() {
  menuVisible.value = false
}

// Ajout : détection du layout custom-menu
const isCustomMenuLayout = computed(() => route.meta.layout === 'custom-menu')

// Ajout : détection du layout gestion (corrige la page blanche)
const isGestionLayout = computed(() => route.meta.layout === 'gestion')

// Menu vertical pour le layout custom-menu
const verticalMenuItems = [
  { to: '/ProprieteListView', label: 'Propriété' },
  { to: '/locataires', label: 'Locataires' },
  { to: '/gestion', label: 'Gestion' },
  // Ajoutez d'autres entrées ici si besoin
]

</script>

<template>
  <!-- Ajout : Layout custom-menu -->
  <template v-if="isCustomMenuLayout">
    <div class="vertical-menu-layout">
      <aside class="vertical-menu">
        <ul>
          <li v-for="item in verticalMenuItems" :key="item.to">
            <router-link :to="item.to" class="vertical-menu-link" active-class="active">
              {{ item.label }}
            </router-link>
          </li>
        </ul>
        <!-- Bouton de déconnexion qui renvoie à l'accueil -->
        <div class="vertical-menu-logout">
          <router-link to="/">
            <n-button type="error" block>Déconnexion</n-button>
          </router-link>
        </div>
      </aside>
      <main class="vertical-main">
        <router-view />
      </main>
    </div>
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
  </template>

  <!-- Layout normal -->
  <template v-else>

      <n-layout style="min-height: 100vh;">
        <n-layout-header bordered>
          <div class="naive-container">
            <div class="header-row">
              <n-icon size="32"><LogoGithub /></n-icon>
              <span class="service-title">{{ serviceTitle }}</span>
              <nav class="nav-desktop">
                <router-link v-for="item in navItems" :key="item.to" :to="item.to" class="nav-link" @click="closeMenu">
                  {{ item.text }}
                </router-link>
              </nav>
              <div class="quick-links-desktop">
                <router-link v-for="link in quickLinks" :key="link.to" :to="link.to">
                  <n-button text size="small">
                    <template #icon>
                      <n-icon :component="link.icon" />
                    </template>
                    {{ link.label }}
                  </n-button>
                </router-link>
              </div>
              <n-button class="hamburger" text @click="toggleMenu" aria-label="Ouvrir le menu">
                <n-icon size="28">
                  <MenuOutline v-if="!menuVisible" />
                  <CloseOutline v-else />
                </n-icon>
              </n-button>
            </div>
            <div class="nav-mobile" v-if="menuVisible">
              <div class="quick-links-mobile">
                <router-link v-for="link in quickLinks" :key="link.to" :to="link.to" @click="closeMenu">
                  <n-button text block>
                    <template #icon>
                      <n-icon :component="link.icon" />
                    </template>
                    {{ link.label }}
                  </n-button>
                </router-link>
              </div>
              <nav>
                <router-link v-for="item in navItems" :key="item.to" :to="item.to" class="nav-link" @click="closeMenu">
                  {{ item.text }}
                  <br>
                </router-link>
              </nav>
            </div>
          </div>
        </n-layout-header>
        <div class="naive-container">
          <router-view />
        </div>
        <n-layout-footer class="naive-footer" bordered>
          <div class="footer-content">
            © {{ new Date().getFullYear() }} Tikaz - Tous droits réservés
          </div>
        </n-layout-footer>
      </n-layout>
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
    </template>
</template>

<style scoped>
.vertical-menu-layout {
  display: flex;
  min-height: 100vh;
}
.vertical-menu {
  width: 220px;
  background: #f6f6f6;
  padding: 2rem 1rem;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  height: 100vh;
}
.vertical-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1 1 auto;
}
.vertical-menu li {
  margin-bottom: 1.5rem;
}
.vertical-menu-logout {
  margin-top: auto;
  padding-top: 2rem;
}
@media (max-width: 900px) {
  .vertical-menu-layout {
    flex-direction: column;
  }
  .vertical-menu {
    width: 100vw;
    height: auto;
    min-height: unset;
    flex-direction: row;
    align-items: center;
    border-right: none;
    border-bottom: 1px solid #ddd;
    padding: 0.5rem 0.5rem 0.5rem 0.5rem;
    overflow-x: auto;
    gap: 1rem;
  }
  .vertical-menu ul {
    display: flex;
    flex-direction: row;
    gap: 1rem;
    margin: 0;
    padding: 0;
    flex: none;
  }
  .vertical-menu li {
    margin-bottom: 0;
    margin-right: 0.5rem;
    white-space: nowrap;
  }
  .vertical-menu-logout {
    margin-top: 0;
    padding-top: 0;
    margin-left: auto;
    flex: none;
    min-width: 120px;
  }
  .vertical-main {
    padding: 1rem 0.5rem;
  }
}
.naive-header {
  background: #fff;
  /* padding: 1rem 2rem;  <-- retire ce padding pour éviter le décalage */
  display: flex;
  flex-direction: column;
  border-bottom: 1px solid #eee;
  position: relative;
  padding: 0; /* Ajouté pour enlever le padding horizontal */
}
.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}
.logo {
  height: 40px;
  margin-right: 1rem;
}
.header-titles {
  flex: 1;
}
.service-title {
  font-size: 1.5rem;
  font-weight: bold;
}
.service-description {
  font-size: 1rem;
  color: #888;
}
.quick-links {
  display: flex;
  gap: 1rem;
}
.hamburger {
  display: none;
}
.responsive-nav {
  transition: max-height 0.3s;
}
.naive-footer {
  background: #f6f6f6;
  padding: 1rem 2rem;
  text-align: center;
  border-top: 1px solid #eee;
}
.footer-content {
  color: #888;
  font-size: 0.95rem;
}
.responsive-quick-links {
  display: none;
  flex-direction: column;
  gap: 0.5rem;
  margin: 1rem 2rem 0 2rem;
}
.main-sider {
  background: #f6f6f6;
}
.naive-container {
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  padding: 2rem 1rem 2rem 1rem;
}
@media (max-width: 900px) {
  .naive-container {
    padding: 1rem 0.5rem 1rem 0.5rem;
  }
}
@media (max-width: 900px) {
  .header-content {
    flex-wrap: wrap;
  }
  .quick-links {
    display: none;
  }
  .hamburger {
    display: inline-flex;
    margin-left: auto;
  }
  .responsive-nav {
    max-height: 0;
    overflow: hidden;
    background: #fff;
    border-bottom: 1px solid #eee;
    margin-left: -2rem;
    margin-right: -2rem;
    margin-bottom: 1rem;
  }
  .responsive-nav.open {
    max-height: 300px;
    padding-bottom: 1rem;
  }
  .responsive-nav .fr-nav__list {
    flex-direction: column;
    gap: 0.5rem;
    padding: 1rem 2rem 0 2rem;
  }
  .responsive-quick-links {
    display: flex;
  }
  .main-sider {
    display: none !important;
  }
}
@media (min-width: 901px) {
  .responsive-nav {
    max-height: none !important;
    overflow: visible !important;
    background: none;
    border: none;
    margin: 0;
    padding: 0;
  }
  .responsive-nav .fr-nav__list {
    display: flex;
    flex-direction: row;
    gap: 2rem;
    padding: 0;
  }
  .hamburger {
    display: none !important;
  }
  .quick-links {
    display: flex;
  }
  .responsive-quick-links {
    display: none !important;
  }
}
.naive-header {
  background: #fff;
  padding: 0;
  border-bottom: 1px solid #eee;
}
.header-row {
  display: flex;
  align-items: center;
  gap: 1rem;
  min-height: 56px;
  position: relative;
}
.service-title {
  font-size: 1.3rem;
  font-weight: bold;
  margin-right: 2rem;
}
.nav-desktop {
  display: flex;
  gap: 1.5rem;
  flex: 1;
}
.nav-link {
  color: #222;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
}
.nav-link.router-link-exact-active {
  color: #2563eb;
}
.quick-links-desktop {
  display: flex;
  gap: 0.5rem;
}
.hamburger {
  display: none;
  margin-left: 1rem;
}
.nav-mobile {
  display: none;
}
.quick-links-mobile {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 1rem;
}
.naive-container {
  max-width: 1200px;
  margin: 2rem auto 2rem auto;
  padding: 0 1rem;
}
.naive-footer {
  background: #f6f6f6;
  padding: 1rem 2rem;
  text-align: center;
  border-top: 1px solid #eee;
}
.footer-content {
  color: #888;
  font-size: 0.95rem;
}
@media (max-width: 900px) {
  .nav-desktop,
  .quick-links-desktop {
    display: none !important;
  }
  .hamburger {
    display: inline-flex !important;
    margin-left: auto;
  }
  .nav-mobile {
    display: block;
    background: #fff;
    border-bottom: 1px solid #eee;
    padding: 1rem 0;
  }
  .naive-container {
    padding: 0 0.5rem;
    margin: 1rem 0 1rem 0;
  }
}
.gestion-tabs {
  display: flex;
  gap: 2rem;
  background: #f8f8f8;
  border-bottom: 1px solid #eee;
  padding: 0.5rem 1rem 0.5rem 1rem;
  margin-bottom: 1rem;
}
.gestion-tab {
  color: #222;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 4px 4px 0 0;
  transition: background 0.2s, color 0.2s;
}
.gestion-tab.active,
.gestion-tab.router-link-exact-active {
  background: #fff;
  color: #2563eb;
  border-bottom: 2px solid #2563eb;
}
@media (max-width: 900px) {
  .gestion-tabs {
    gap: 1rem;
    padding: 0.5rem 0.5rem;
    overflow-x: auto;
    font-size: 0.98rem;
  }
  .gestion-tab {
    padding: 0.5rem 0.7rem;
    white-space: nowrap;
  }
}
.vertical-menu-layout {
  display: flex;
  min-height: 100vh;
}
.vertical-menu {
  width: 220px;
  background: #f6f6f6;
  padding: 2rem 1rem;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  height: 100vh;
}
.vertical-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1 1 auto;
}
.vertical-menu li {
  margin-bottom: 1.5rem;
}
.vertical-menu-link {
  color: #222;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
}
.vertical-menu-link.active {
  color: #2563eb;
}
.vertical-main {
  flex: 1;
  padding: 2rem 2rem;
  background: #fff;
  min-height: 100vh;
}
@media (max-width: 900px) {
  .vertical-menu-layout {
    flex-direction: column;
  }
  .vertical-menu {
    width: 100vw;
    height: auto;
    min-height: unset;
    flex-direction: row;
    align-items: center;
    border-right: none;
    border-bottom: 1px solid #ddd;
    padding: 0.5rem 0.5rem 0.5rem 0.5rem;
    overflow-x: auto;
    gap: 1rem;
  }
  .vertical-menu ul {
    display: flex;
    flex-direction: row;
    gap: 1rem;
    margin: 0;
    padding: 0;
    flex: none;
  }
  .vertical-menu li {
    margin-bottom: 0;
    margin-right: 0.5rem;
    white-space: nowrap;
  }
  .vertical-main {
    padding: 1rem 0.5rem;
  }
}
</style>
