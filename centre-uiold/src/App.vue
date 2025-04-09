<template>
  <div id="app">
    <AppNavbar v-if="showHeaderFooter" />
    <router-view v-slot="{ Component }">
      <component :is="Component" />
    </router-view>
    <AppFooter v-if="showHeaderFooter" />
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import DsfrHeader from './components/DsfrHeader.vue';
import AppFooter from './components/AppFooter.vue';

export default {
  name: 'App',
  components: {
    DsfrHeader,
    AppFooter
  },
  setup() {
    const route = useRoute();
    const showHeaderFooter = computed(() => route.path !== '/login');
    return {
      showHeaderFooter
    };
  }
};
</script>

<style>
/* Si vous avez des styles globaux, vous pouvez les ajouter ici */
</style>
