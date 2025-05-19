<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { NCard, NGrid, NGi, useMessage } from 'naive-ui'
import { ref, onMounted, computed } from 'vue'

interface CardData {
  key: string
  title: string
  value: number
  unit: string
  color: {
    start: string
    end: string
  }
  icon: string
}

const authStore = useAuthStore()
const message = useMessage()

const dashboardData = ref({
  nombreBiens: 0,
  repartitionTypes: {} as Record<string, number>,
  tauxOccupation: 0,
  totalLoyersPercus: 0,
  alertesImpaye: 0,
})

const cardData = computed<CardData[]>(() => [
  {
    key: 'nombreBiens',
    title: 'Nombre de biens',
    value: dashboardData.value.nombreBiens,
    unit: '',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:home-city-outline',
  },
  {
    key: 'tauxOccupation',
    title: 'Taux d’occupation',
    value: dashboardData.value.tauxOccupation,
    unit: '%',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:account-group-outline',
  },
  {
    key: 'totalLoyersPercus',
    title: 'Loyers perçus (mois)',
    value: dashboardData.value.totalLoyersPercus,
    unit: '€',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:cash-multiple',
  },
  {
    key: 'alertesImpaye',
    title: 'Alertes impayés',
    value: dashboardData.value.alertesImpaye,
    unit: '',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:alert-circle-outline',
  },
])

function getGradientColor(color: CardData['color']) {
  return `linear-gradient(to bottom right, ${color.start}, ${color.end})`
}

onMounted(async () => {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/dashboard/${authStore.userInfo.userId}`)
    if (!res.ok) throw new Error('Erreur lors du chargement du tableau de bord')
    dashboardData.value = await res.json()
  } catch (e) {
    console.error(e)
    message.error('Échec du chargement du tableau de bord.')
  }
})
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NGrid cols="s:1 m:2 l:4" responsive="screen" :x-gap="16" :y-gap="16">
      <NGi v-for="item in cardData" :key="item.key">
        <div
          class="rd-8px px-16px pb-4px pt-8px text-white"
          :style="{ backgroundImage: getGradientColor(item.color) }"
        >
          <h3 class="text-16px">
            {{ item.title }}
          </h3>
          <div class="flex justify-between pt-12px">
            <SvgIcon :icon="item.icon" class="text-32px" />
            <CountTo
              :prefix="item.unit"
              :start-value="0"
              :end-value="item.value"
              class="text-30px text-white dark:text-dark"
            />
          </div>
        </div>
      </NGi>
    </NGrid>
  </NCard>
</template>

<style scoped></style>
