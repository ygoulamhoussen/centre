<script setup lang="ts">
definePage({
  meta: {
    hideInMenu: true,
    roles: ['ADMIN'],
    activeMenu: '/manage/user',
  },
  props: true,
})
</script>

<template>
  <LookForward />
</template>

<style scoped></style>
