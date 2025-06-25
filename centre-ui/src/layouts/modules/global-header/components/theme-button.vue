<script setup lang="ts">
import { $t } from '@/locales'
import { useAppStore } from '@/store/modules/app'
import { useAuthStore } from '@/store/modules/auth'

defineOptions({
  name: 'ThemeButton',
})

const appStore = useAppStore()
const authStore = useAuthStore()
</script>

<template>
  <ButtonIcon
    v-if="authStore.userInfo.roles.includes('ADMIN')"
    icon="majesticons:color-swatch-line"
    :tooltip-content="$t('icon.themeConfig')"
    @click="appStore.openThemeDrawer"
  />
</template>

<style scoped></style>
