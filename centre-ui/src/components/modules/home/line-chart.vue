<script setup lang="ts">
import { useEcharts } from '@/hooks/common/echarts'
import { useAppStore } from '@/store/modules/app'
import { useThemeStore } from '@/store/modules/theme'
import { getPaletteColorByNumber } from '@sa/color'
import { useAuthStore } from '@/store/modules/auth'

const themeStore = useThemeStore()
const appStore = useAppStore()
const authStore = useAuthStore()
const userId = authStore.userInfo.userId

const lightColor = computed(() => getPaletteColorByNumber(themeStore.themeColor, 300))

const { domRef, updateOptions } = useEcharts(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      label: { backgroundColor: '#6a7985' }
    }
  },
  legend: { data: ['Loyers perçus'] },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [] as string[]
  },
  yAxis: { type: 'value' },
  series: [
    {
      color: themeStore.themeColor,
      name: 'Loyers perçus',
      type: 'line',
      smooth: true,
      stack: 'Total',
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0.25, color: themeStore.themeColor },
            { offset: 1, color: '#fff' }
          ]
        }
      },
      emphasis: { focus: 'series' },
      data: [] as number[]
    }
  ]
}))

async function fetchData() {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/dashboard/${userId}`)
    const dashboard = await res.json()

    const mois = Object.keys(dashboard.loyersMensuels)
    const montants = Object.values(dashboard.loyersMensuels)

    updateOptions((opts) => {
      opts.xAxis.data = mois
      opts.series[0].data = (montants as unknown[]).map(Number)
      return opts
    })
  } catch (e) {
    console.error('Erreur lors du chargement du dashboard', e)
  }
}

watch(() => appStore.locale, fetchData)
watch(() => themeStore.themeColor, fetchData)

onMounted(fetchData)
</script>

<template>

  <NCard :bordered="false" class="card-wrapper">
    <div ref="domRef" class="h-360px overflow-hidden" />
  </NCard>
</template>

<style scoped>
.card-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.h-360px {
  flex: 1;
  min-height: 0;
  width: 100%;
}
</style>
