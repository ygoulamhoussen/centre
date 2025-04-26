<script setup lang="ts">
definePage({
  meta: {
    constant: true,
    hideInMenu: true,
    title: 'test',
  },
  redirect: import.meta.env.VITE_ROUTE_HOME || '/home',
})
</script>

<template>
  <RouterView />
</template>
