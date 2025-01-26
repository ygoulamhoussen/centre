<template>
  <header role="banner" class="fr-header">
    <div class="fr-header__body">
      <div class="fr-container">
        <div class="fr-header__body-row">
          <div class="fr-header__brand fr-enlarge-link">
            <!-- ...existing code... -->
          </div>
          <div class="fr-header__service">
            <!-- ...existing code... -->
          </div>
          <div class="fr-header__tools">
            <div v-if="isAuthenticated">
              <span>{{ userName }}</span><br>
              <button @click="handleLogout" class="fr-btn fr-btn--secondary">Déconnexion</button>
            </div>
            <div v-else>
              <a href="/login" class="fr-btn fr-btn--secondary">Connexion</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="fr-header__menu">
      <div class="fr-container">
        <nav
          id="main-navigation"
          class="fr-nav"
          role="navigation"
          aria-label="Menu principal"
        >
          <ul class="fr-nav__list">
            <li>
              <a class="fr-nav__link" href="/">Accueil</a>
            </li>
            <li>
              <a class="fr-nav__link" href="/formations">Formations</a>
            </li>
            <li>
              <a class="fr-nav__link" href="/contact">Contact</a>
            </li>
            <li v-if="isAuthenticated">
              <a class="fr-nav__link" href="/profile">Profil</a>
            </li>
            <li v-if="isAuthenticated && isAdmin">
              <a class="fr-nav__link" href="/admin">Admin</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </header>
</template>

<script>
import { ref, onMounted } from 'vue';

export default {
  name: 'AppNavbar',
  setup() {
    const isAuthenticated = ref(false);
    const isAdmin = ref(false);
    const userName = ref('');

    const initializeAuth = () => {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        isAuthenticated.value = true;
        isAdmin.value = userProfile.role === 'ADMINISTRATEUR';
        userName.value = userProfile.nom;
      }
    };

    const handleLogout = () => {
      isAuthenticated.value = false;
      isAdmin.value = false;
      userName.value = '';
      sessionStorage.removeItem('userProfile');
      window.location.href = '/'; // Redirect to homepage
    };

    onMounted(() => {
      initializeAuth();
    });

    return {
      isAuthenticated,
      isAdmin,
      userName,
      handleLogout
    };
  }
};
</script>

<style scoped>
/* Ajoutez vos styles personnalisés si nécessaire */
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
