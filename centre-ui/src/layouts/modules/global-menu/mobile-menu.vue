<script setup lang="ts">
import { useRouterPush } from '@/hooks/common/router'
import { useRouteStore } from '@/store/modules/route'
import { NMenu } from 'naive-ui'
import { nextTick, ref, watch, onMounted } from 'vue'
import { useMenu } from '../../context'

const routeStore = useRouteStore()
const { selectedKey } = useMenu()
const { routerPushByKeyWithMetaQuery } = useRouterPush()

const menuContainer = ref<HTMLElement | null>(null)

// Fonction corrigée pour centrer l'item actif sur le parent .n-menu-item--selected
function centerActiveMenuItem() {
  console.log('centerActiveMenuItem called')
  if (!menuContainer.value) {
    console.log('menuContainer not ready')
    return
  }

  const menuList = menuContainer.value.querySelector('.n-menu--horizontal') as HTMLElement | null
  if (!menuList) {
    console.log('menuList not found')
    return
  }

  console.log('selectedKey:', selectedKey.value)
  console.log('menu options:', routeStore.menus)
  setTimeout(() => {
    console.log('setTimeout executed')
    console.log('menuList.innerHTML:', menuList.innerHTML)
    const active = menuList.querySelector('.n-menu-item-content--selected') as HTMLElement | null
    console.log('active:', active)
    if (active) {
      const scrollContainer = menuList.parentElement || menuList
      const menuWidth = scrollContainer.clientWidth
      const activeLeft = active.getBoundingClientRect().left - scrollContainer.getBoundingClientRect().left + scrollContainer.scrollLeft
      const activeWidth = active.offsetWidth
      const scrollLeft = activeLeft - (menuWidth / 2) + (activeWidth / 2)
      scrollContainer.scrollTo({ left: scrollLeft, behavior: 'smooth' })
    }
  }, 300)
}

// Handler du clic sur le menu
function handleMenuClick(key: string) {
  routerPushByKeyWithMetaQuery(key)
  nextTick(() => {
    centerActiveMenuItem()
  })
}

// Centrer aussi à l'initialisation ou quand la route change
watch(selectedKey, () => {
  nextTick(() => {
    centerActiveMenuItem()
  })
})

watch(
  () => routeStore.menus,
  (menus) => {
    if (menus && menus.length > 0) {
      setTimeout(() => {
        centerActiveMenuItem()
      }, 300)
    }
  },
  { immediate: true }
)
</script>

<template>
  <div ref="menuContainer" class="mobile-menu-scroll">
    <NMenu
      mode="horizontal"
      :options="routeStore.menus"
      :value="selectedKey"
      @update:value="handleMenuClick"
      :collapsed="false"
      :indent="18"
      :render-label="() => ''"
      style="width: 100%; min-width: max-content; justify-content: flex-start;"
    />
  </div>
</template>

<style scoped>
.mobile-menu-scroll {
  overflow-x: auto;
  width: 100%;
  -webkit-overflow-scrolling: touch;
}
:deep(.n-menu--horizontal) {
  overflow-x: auto !important;
  width: 100%;
  min-width: max-content;
  justify-content: flex-start;
  -webkit-overflow-scrolling: touch;
}
</style>