<script setup lang="ts">
import { transformColorWithOpacity } from '@sa/color'
import { SimpleScrollbar } from '@sa/materials'

defineOptions({
  name: 'FirstLevelMenu',
})

const props = defineProps<Props>()

const emit = defineEmits<Emits>()

interface Props {
  menus: App.Global.Menu[]
  activeMenuKey?: string
  siderCollapse?: boolean
  darkMode?: boolean
  themeColor: string
  isMobile?: boolean // Ajout de la prop
}

interface Emits {
  (e: 'select', menu: App.Global.Menu): boolean
  (e: 'toggleSiderCollapse'): void
}

interface MixMenuItemProps {
  /** Menu item label */
  label: App.Global.Menu['label']
  /** Menu item icon */
  icon: App.Global.Menu['icon']
  /** Active menu item */
  active: boolean
  /** Mini size */
  isMini?: boolean
}
const [DefineMixMenuItem, MixMenuItem] = createReusableTemplate<MixMenuItemProps>()

const selectedBgColor = computed(() => {
  const { darkMode, themeColor } = props

  const light = transformColorWithOpacity(themeColor, 0.1, '#ffffff')
  const dark = transformColorWithOpacity(themeColor, 0.3, '#000000')

  return darkMode ? dark : light
})

function handleClickMixMenu(menu: App.Global.Menu) {
  emit('select', menu)
}

function toggleSiderCollapse() {
  emit('toggleSiderCollapse')
}
</script>

<template>
  <!-- define component: MixMenuItem -->
  <DefineMixMenuItem v-slot="{ label, icon, active, isMini }">
    <div
      class="mx-4px mb-6px flex-col-center cursor-pointer rounded-8px bg-transparent px-4px py-8px transition-300 hover:bg-[rgb(0,0,0,0.08)]"
      :class="{
        'text-primary selected-mix-menu': active,
      }"
    >
      <component :is="icon" :class="[isMini ? 'text-icon-small' : 'text-icon-large']" />
      <p
        class="w-full ellipsis-text text-center text-12px transition-height-300"
        :class="[isMini ? 'h-0 pt-0' : 'h-20px pt-4px']"
      >
        {{ label }}
      </p>
    </div>
  </DefineMixMenuItem>
  <!-- define component end: MixMenuItem -->

  <div class="h-full flex-col-stretch flex-1-hidden">
    <slot />
    <SimpleScrollbar>
      <MixMenuItem
        v-for="menu in menus"
        :key="menu.key"
        :label="menu.label"
        :icon="menu.icon"
        :active="menu.key === activeMenuKey"
        :is-mini="siderCollapse"
        @click="handleClickMixMenu(menu)"
      />
    </SimpleScrollbar>
    <MenuToggler
      v-if="!isMobile"
      arrow-icon
      :collapsed="siderCollapse"
      :z-index="99"
      @click="toggleSiderCollapse"
    />
  </div>
</template>

<style scoped>
.selected-mix-menu {
  background-color: v-bind(selectedBgColor);
}
</style>
