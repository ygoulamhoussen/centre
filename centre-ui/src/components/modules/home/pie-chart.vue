<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useEcharts } from '@/hooks/common/echarts'
import { useAppStore } from '@/store/modules/app'
import { useThemeStore } from '@/store/modules/theme'
import { getPaletteColorByNumber } from '@sa/color'
import { $t } from '@/locales'

defineOptions({ name: 'RepartitionParTypePieChart' })

const themeStore = useThemeStore()
const appStore = useAppStore()
const authStore = useAuthStore()

const colors = computed(() => {
  const lightColor = getPaletteColorByNumber(themeStore.themeColor, 300)
  const darkColor = getPaletteColorByNumber(themeStore.themeColor, 700)
  const darkestColor = getPaletteColorByNumber(themeStore.themeColor, 900)

  return [lightColor, themeStore.themeColor, darkColor, darkestColor]
})

const { domRef, updateOptions } = useEcharts(() => ({
  tooltip: { trigger: 'item' },
  legend: {
    bottom: '1%',
    left: 'center',
    itemStyle: { borderWidth: 0 },
  },
  series: [
    {
      color: colors.value,
      name: 'Répartition par type',
      type: 'pie',
      radius: ['45%', '75%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 1,
      },
      label: {
        show: false,
        position: 'center',
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '12',
        },
      },
      labelLine: { show: false },
      data: [] as { name: string; value: number }[],
    },
  ],
}))

async function fetchRepartitionData() {
  try {
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/dashboard/${authStore.userInfo.userId}`
    )
    if (!res.ok) throw new Error('Erreur lors du chargement de la répartition')

    const data = await res.json()
    const repartition = data.repartitionParType || {}

    const pieData = Object.entries(repartition).map(([type, count]) => ({
      name: type,
      value: count as number,
    }))

    updateOptions((opts) => {
      opts.series[0].data = pieData
      return opts
    })
  } catch (e) {
    console.error(e)
  }
}

watch(() => appStore.locale, fetchRepartitionData)

watch(
  () => themeStore.themeColor,
  () => {
    updateOptions((opts) => {
      opts.series[0].color = colors.value
      return opts
    })
  }
)

onMounted(fetchRepartitionData)
</script>

<template>

  <NCard :bordered="false" class="card-wrapper">
    
    <div ref="domRef" class="h-360px overflow-hidden" />
  </NCard>
</template>

<style scoped></style>
