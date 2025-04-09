import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
    isAdmin: false,
    userName: ''
  }),
  actions: {
    login(userData) {
      this.isAuthenticated = true;
      this.isAdmin = userData.role === 'ADMINISTRATEUR';
      this.userName = userData.nom;
      sessionStorage.setItem('userProfile', JSON.stringify(userData));
    },
    logout() {
      this.isAuthenticated = false;
      this.isAdmin = false;
      this.userName = '';
      sessionStorage.removeItem('userProfile');
    },
    initializeStore() {
      const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
      if (userProfile) {
        this.login(userProfile);
      }
    }
  }
});
