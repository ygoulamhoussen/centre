<script setup lang="ts">
import { GLOBAL_SIDER_MENU_ID } from '@/constants/app'
import { useRouterPush } from '@/hooks/common/router'
import { $t } from '@/locales'
import { useAppStore } from '@/store/modules/app'
import { useRouteStore } from '@/store/modules/route'
import { useThemeStore } from '@/store/modules/theme'
import { useBoolean } from '@sa/hooks'
import { SimpleScrollbar } from '@sa/materials'
import { useMenu, useMixMenuContext } from '../../../context'
import GlobalLogo from '../../global-logo/index.vue'
import FirstLevelMenu from '../components/first-level-menu.vue'

defineOptions({
  name: 'VerticalMixMenu',
})

const route = useRoute()
const appStore = useAppStore()
const themeStore = useThemeStore()
const routeStore = useRouteStore()
const { routerPushByKeyWithMetaQuery } = useRouterPush()
const { bool: drawerVisible, setBool: setDrawerVisible } = useBoolean()
const {
  allMenus,
  childLevelMenus,
  activeFirstLevelMenuKey,
  setActiveFirstLevelMenuKey,
  getActiveFirstLevelMenuKey,
  //
} = useMixMenuContext()
const { selectedKey } = useMenu()

const hasChildMenus = computed(() => childLevelMenus.value.length > 0)

const showDrawer = computed(() => hasChildMenus.value && (drawerVisible.value || appStore.mixSiderFixed))

function handleSelectMixMenu(menu: App.Global.Menu) {
  setActiveFirstLevelMenuKey(menu.key)

  if (menu.children?.length) {
    setDrawerVisible(true)
  }
  else {
    routerPushByKeyWithMetaQuery(menu.routeKey)
  }
}

function handleResetActiveMenu() {
  setDrawerVisible(false)

  if (!appStore.mixSiderFixed) {
    getActiveFirstLevelMenuKey()
  }
}

const expandedKeys = ref<string[]>([])

function updateExpandedKeys() {
  if (appStore.siderCollapse || !selectedKey.value) {
    expandedKeys.value = []
    return
  }
  expandedKeys.value = routeStore.getSelectedMenuKeyPath(selectedKey.value)
}

watch(
  () => route.name,
  () => {
    updateExpandedKeys()
  },
  { immediate: true },
)
</script>

<template>
  <Teleport :to="`#${GLOBAL_SIDER_MENU_ID}`">
    <div class="h-full flex" @mouseleave="handleResetActiveMenu">
      <FirstLevelMenu
        :menus="allMenus"
        :active-menu-key="activeFirstLevelMenuKey"
        :sider-collapse="appStore.siderCollapse"
        :dark-mode="themeStore.darkMode"
        :theme-color="themeStore.themeColor"
        :is-mobile="appStore.isMobile"
        @select="handleSelectMixMenu"
        @toggle-sider-collapse="appStore.toggleSiderCollapse"
      >
        <GlobalLogo :show-title="false" :style="{ height: `${themeStore.header.height}px` }" />
      </FirstLevelMenu>
      <div
        class="relative h-full transition-width-300"
        :style="{ width: appStore.mixSiderFixed && hasChildMenus ? `${themeStore.sider.mixChildMenuWidth}px` : '0px' }"
      >
        <DarkModeContainer
          class="absolute-lt h-full flex-col-stretch nowrap-hidden shadow-sm transition-all-300"
          :style="{ width: showDrawer ? `${themeStore.sider.mixChildMenuWidth}px` : '0px' }"
        >
          <header class="flex-y-center justify-between px-12px" :style="{ height: `${themeStore.header.height}px` }">
            <h2 class="text-16px text-primary font-bold">
              {{ $t('system.title') }}
            </h2>
            <PinToggler
              :pin="appStore.mixSiderFixed"
              @click="appStore.toggleMixSiderFixed"
            />
          </header>
          <SimpleScrollbar>
            <NMenu
              v-model:expanded-keys="expandedKeys"
              mode="vertical"
              :value="selectedKey"
              :options="childLevelMenus"
              :indent="18"
              @update:value="routerPushByKeyWithMetaQuery"
            />
          </SimpleScrollbar>
        </DarkModeContainer>
      </div>
    </div>
  </Teleport>
</template>

<style scoped></style>
