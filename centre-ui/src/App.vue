<template>
  <div id="app">
    <router-view v-slot="{ Component }">
      <component :is="Component" v-if="$route.path === '/login'"></component>
      <div v-else>
        <AppNavbar />
        <component :is="Component"></component>
      </div>
    </router-view>
  </div>
</template>

<script>
import AppNavbar from './components/AppNavbar.vue';  // Assurez-vous d'importer le bon fichier

export default {
  name: 'App',
  components: {
    AppNavbar
  }
};
</script>

<style>
/* Si vous avez des styles globaux, vous pouvez les ajouter ici */
</style>
