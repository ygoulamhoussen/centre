<template>
  <header role="banner" class="fr-header">
    <!-- Header body -->
    <div class="fr-header__body">
      <div class="fr-container">
        <div class="fr-header__menu-links">
          <ul class="fr-btns-group">
            <!-- Liens supplémentaires ici si nécessaire -->
          </ul>
        </div>
        <div class="fr-header__body-row">
          <div class="fr-header__brand fr-enlarge-link">
            <div class="fr-header__brand-top">
              <div class="fr-header__logo">
                <div class="company-name">
                  Ocean Pro Formation
                </div>
                <div class="company-slogan">
                  Des compétences pour un monde en mouvement
                </div>
              </div>
              <div class="fr-header__navbar">
                <button
                  class="fr-btn--menu fr-btn"
                  data-fr-opened="false"
                  aria-controls="modal-header__menu"
                  aria-haspopup="menu"
                  aria-expanded="false"
                >
                  Menu
                </button>
              </div>
            </div>
<!--             <div class="fr-header__service">
              <a href="/" title="Accueil - [Nom du site]">
                <p class="fr-header__service-title">
                  Ocean Pro Formation
                </p>
              </a>
            </div> -->
          </div>
          
          <!-- Tools section -->
          <div class="fr-header__tools">
            <div class="fr-header__tools-links">
              <ul class="fr-btns-group">
  
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Menu -->
    <div class="fr-header__menu fr-modal" id="modal-header__menu">
      <div class="fr-container">
        <nav class="fr-nav" role="navigation" aria-label="Menu principal">
          <ul class="fr-nav__list">
            <li class="fr-nav__item">
              <a class="fr-nav__link" href="/" target="_self">Accueil</a>
            </li>
            <li class="fr-nav__item">
              <a class="fr-nav__link" href="/formations" target="_self">Formations</a>
            </li>
            <li class="fr-nav__item">
              <a class="fr-nav__link" href="/contact" target="_self">Contact</a>
            </li>
            <li class="fr-nav__item" v-if="authStore.isAuthenticated">
              <a class="fr-nav__link" href="/profile" target="_self">Profil</a>
            </li>
            <li class="fr-nav__item" v-if="authStore.isAuthenticated && authStore.isAdmin">
              <a class="fr-nav__link" href="/admin" target="_self">Admin</a>
            </li>
            <li class="fr-nav__item">
              <a class="fr-nav__link" v-if="!authStore.isAuthenticated" href="/login">Connexion</a>
              <a class="fr-nav__link" v-else @click="authStore.logout">Déconnexion</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </header>
</template>

<script>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth'; // Correct the import path

export default {
  name: 'AppNavbar',
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();

    onMounted(() => {
      authStore.initializeStore();

      // Initialisation du DSFR
      if (window.dsfr && typeof window.dsfr.start === 'function') {
        window.dsfr.start();
      } else {
        console.warn('DSFR non chargé ou non initialisé.');
      }
    });

    return {
      authStore
    };
  }
};
</script>

<style scoped>
.company-name {
  font-size: 1.5rem;
  font-weight: bold;
  color: #000091;
}

.company-slogan {
  font-size: 1rem;
  color: #000091;
}
</style>
