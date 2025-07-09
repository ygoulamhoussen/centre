<script setup lang="ts">
import { useAppStore } from '@/store/modules/app'
import pkg from '~/package.json'
import { ref, onMounted } from 'vue'

definePage({
  meta: {
    icon: 'fluent:book-information-24-regular',
    order: 10,
    roles: ['ADMIN'],
  },
})

const appStore = useAppStore()

const column = computed(() => (appStore.isMobile ? 1 : 2))

interface PkgJson {
  name: string
  version: string
  dependencies: PkgVersionInfo[]
  devDependencies: PkgVersionInfo[]
  engines?: {
    pnpm?: string
    node?: string
  }
}

interface PkgVersionInfo {
  name: string
  version: string
}

const { name, version, dependencies, devDependencies } = pkg

function transformVersionData(tuple: [string, string]): PkgVersionInfo {
  const [$name, $version] = tuple
  return {
    name: $name,
    version: $version,
  }
}

const pkgJson: PkgJson = {
  name,
  version,
  engines: {
    pnpm: pkg.engines?.pnpm,
    node: pkg.engines?.node,
  },
  dependencies: Object.entries(dependencies).map(item => transformVersionData(item)),
  devDependencies: Object.entries(devDependencies).map(item => transformVersionData(item)),
}

const latestBuildTime = BUILD_TIME

const website = 'https://v-naive-admin.vercel.app'

// --- Bouton d'installation toujours visible ---
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
    <NCard
      :title="$t('page.about.title')"
      :bordered="false"
      size="small"
      segmented
      class="card-wrapper"
    >
      <p>{{ $t('page.about.introduction') }}</p>
      <!-- Bouton d'installation toujours visible -->
      <div class="mt-4">
        <NButton type="primary" @click="tryInstallApp">
          Installer l’application sur mon mobile
        </NButton>
      </div>
      <!-- Modale d'aide si le prompt n'est pas disponible -->
      <NModal v-model:show="showInstallHelp">
        <NCard>
          <template #header>Installer l’application</template>
          <div>
            <div v-if="isIos">
              Sur iPhone, ouvrez le menu <b>Partager</b> puis « Ajouter à l’écran d’accueil  ».
            </div>
            <div v-else>
              Sur Android, ouvrez le menu du navigateur puis « Installer l’application ».
            </div>
          </div>
        </NCard>
      </NModal>
    </NCard>
    <NCard
      :title="$t('page.about.projectInfo.title')"
      :bordered="false"
      size="small"
      segmented
      class="card-wrapper"
    >
      <NDescriptions label-placement="left" bordered size="small" :column="column">
        <NDescriptionsItem :label="$t('page.about.projectInfo.version')">
          <NTag type="primary">
            {{ pkgJson.version }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.about.projectInfo.latestBuildTime')">
          <NTag type="primary">
            {{ latestBuildTime }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.about.projectInfo.node')">
          <NTag type="primary">
            {{ pkgJson?.engines?.node }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.about.projectInfo.pnpm')">
          <NTag type="primary">
            {{ pkgJson?.engines?.pnpm }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.about.projectInfo.githubLink')">
          <a class="text-primary" :href="pkg.homepage" target="_blank" rel="noopener noreferrer">
            {{ $t('page.about.projectInfo.githubLink') }}
          </a>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.about.projectInfo.previewLink')">
          <a class="text-primary" :href="website" target="_blank" rel="noopener noreferrer">
            {{ $t('page.about.projectInfo.previewLink') }}
          </a>
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
    <NCard :bordered="false" size="small" segmented class="card-wrapper">
      <template #header>
        <span class="mr-2">{{ $t('page.about.prdDep') }}</span>
        <NTag type="info" round :bordered="false" size="small">
          {{ pkgJson.dependencies.length }}
        </NTag>
      </template>
      <NDescriptions label-placement="left" bordered size="small" :column="column">
        <NDescriptionsItem v-for="item in pkgJson.dependencies" :key="item.name" :label="item.name">
          <a
            v-if="!item.version.includes('workspace')"
            :href="`https://www.npmjs.com/package/${item.name}`"
            target="_blank"
            rel="noopener noreferrer"
          >
            {{ item.version }}
          </a>
          <template v-else>
            {{ item.version }}
          </template>
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
    <NCard :bordered="false" size="small" segmented class="card-wrapper">
      <template #header>
        <span class="mr-2">{{ $t('page.about.devDep') }}</span>
        <NTag type="info" round :bordered="false" size="small">
          {{ pkgJson.devDependencies.length }}
        </NTag>
      </template>
      <NDescriptions label-placement="left" bordered size="small" :column="column">
        <NDescriptionsItem
          v-for="item in pkgJson.devDependencies"
          :key="item.name"
          :label="item.name"
        >
          <a
            v-if="!item.version.includes('workspace')"
            :href="`https://www.npmjs.com/package/${item.name}`"
            target="_blank"
            rel="noopener noreferrer"
          >
            {{ item.version }}
          </a>
          <template v-else>
            {{ item.version }}
          </template>
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
  </NSpace>
</template>

<style scoped></style>
