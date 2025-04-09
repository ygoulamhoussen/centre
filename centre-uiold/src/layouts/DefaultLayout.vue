<template>
  <div>
    <AppNavbar />
    <section class="fr-container fr-my-6w">
      <Breadcrumb :segments="breadcrumbSegments" />
      <main>
        <slot></slot>
      </main>
    </section>
    <AppFooter />
  </div>
</template>

<script>
import AppNavbar from '../components/DsfrHeader.vue';
import Breadcrumb from '../components/Breadcrumb.vue';
import AppFooter from '../components/AppFooter.vue';

export default {
  name: 'DefaultLayout',
  components: {
    AppNavbar,
    AppFooter,
    Breadcrumb
  },
  props: {
    breadcrumbSegments: {
      type: Array,
      required: true
    }
  }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
