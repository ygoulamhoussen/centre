<script setup lang="ts">
import CardData from '@/components/modules/home/card-data.vue'
import HeaderBanner from '@/components/modules/home/header-banner.vue'
import LineChart from '@/components/modules/home/line-chart.vue'
import PieChart from '@/components/modules/home/pie-chart.vue'
import { useAppStore } from '@/store/modules/app'
import { ref, onMounted } from 'vue'

definePage({
  meta: {
    icon: 'mdi:monitor-dashboard',
    order: 1,
  },
})

const appStore = useAppStore()

const gap = computed(() => (appStore.isMobile ? 0 : 16))

const showInstallHelp = ref(false)
const deferredPrompt = ref<any>(null)
const isIos = /iphone|ipad|ipod/i.test(navigator.userAgent)

onMounted(() => {
  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault()
    deferredPrompt.value = e
  })
})

function tryInstallApp() {
  if (deferredPrompt.value) {
    deferredPrompt.value.prompt()
    deferredPrompt.value.userChoice.then(() => {
      deferredPrompt.value = null
    })
  } else {
    showInstallHelp.value = true
  }
}
</script>

<template>
  <NSpace vertical :size="16">

    <HeaderBanner />
    <CardData />
    <NGrid :x-gap="gap" :y-gap="16" responsive="screen" item-responsive>
      <NGi span="24 s:24 m:14">
        <NCard :bordered="false" class="card-wrapper">
          <LineChart />
        </NCard>
      </NGi>
      <NGi span="24 s:24 m:10">
        <NCard :bordered="false" class="card-wrapper">
          <PieChart />
        </NCard>
      </NGi>
    </NGrid>
    <NGrid :x-gap="gap" :y-gap="16" responsive="screen" item-responsive>
<!--       <NGi span="24 s:24 m:14">
        <ProjectNews />
      </NGi> -->
<!--       <NGi span="24 s:24 m:10">
        <CreativityBanner />
      </NGi> -->
    </NGrid>
    <div v-if="appStore.isMobile" class="mt-4 flex-center">
      <NButton type="primary" @click="tryInstallApp">
        Installer l’application sur mon mobile
      </NButton>
    </div>
    <NModal v-model:show="showInstallHelp">
      <NCard>
        <template #header>Installer l’application</template>
        <div>
          <div v-if="isIos">
            Sur iPhone, ouvrez le menu <b>Partager</b> puis « Ajouter à l’écran d’accueil  ».
          </div>
          <div v-else>
            Sur Android, ouvrez le menu du navigateur puis « Ajouter à l'écran d'accueil  ».
          </div>
        </div>
      </NCard>
    </NModal>
  </NSpace>
</template>

<style scoped>
.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
