<script setup lang="ts">
definePage({
  meta: {
    order: 7,
    icon: 'clarity:plugin-line',
    roles: ['ADMIN', 'USER'],
  },
})
</script>

<template>
  <RouterView />
</template>
