<template>
  <div>
    <section class="fr-container fr-my-6w">
    <Breadcrumb :segments="breadcrumbSegments" />
    <main>
      <slot></slot>
    </main>
</section>

  </div>

</template>

<script>
import AppNavbar from '../components/AppNavbar.vue';

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
