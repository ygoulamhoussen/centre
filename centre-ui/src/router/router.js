import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import ContactPage from '../views/ContactPage.vue';
import FormationsPage from '../views/FormationsPage.vue';
import AdminPage from '../views/AdminPage.vue';
import LoginPage from '../views/LoginPage.vue';
import FormationsManagementPage from '../views/FormationsManagementPage.vue'; // Import the new page
import FormateursPage from '../views/FormateursPage.vue'; // Import the new page
import { useAuthStore } from '../stores/auth'; // Correct the import path

const routes = [
  { path: '/', component: HomePage },
  { path: '/contact', component: ContactPage },
  { path: '/formations', component: FormationsPage },
  {
    path: '/admin',
    component: AdminPage,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/formations',
    component: FormationsManagementPage,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/formateurs',
    component: FormateursPage,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  { path: '/login', component: LoginPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation guard to check for authentication and admin access
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.isAuthenticated;
  const isAdmin = authStore.isAdmin;

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next({ path: '/login' });
    } else if (to.matched.some(record => record.meta.requiresAdmin) && !isAdmin) {
      next({ path: '/' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
