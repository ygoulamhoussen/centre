import { reactive, computed } from 'vue';

const state = reactive({
  isAuthenticated: false,
  isAdmin: false,
  userIdentifier: '',
  userName: '' // Add userName to the state
});

export const useStore = () => {
  const login = (userData) => {
    console.log('Logging in:', userData); // Debugging line
    state.isAuthenticated = true;
    state.isAdmin = userData.isAdmin;
    state.userIdentifier = userData.identifier;
    state.userName = userData.name; // Set userName from userData
    console.log('State after login:', state); // Debugging line
  };

  const logout = () => {
    console.log('Logging out'); // Debugging line
    state.isAuthenticated = false;
    state.isAdmin = false;
    state.userIdentifier = '';
    state.userName = '';
    sessionStorage.removeItem('userProfile');
    console.log('State after logout:', state); // Debugging line
  };

  const initializeStore = () => {
    const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
    if (userProfile) {
      login(userProfile);
    }
  };

  initializeStore();

  return {
    isAuthenticated: computed(() => state.isAuthenticated),
    isAdmin: computed(() => state.isAdmin),
    userIdentifier: computed(() => state.userIdentifier),
    userName: computed(() => state.userName), // Add computed property for userName
    login,
    logout
  };
};
