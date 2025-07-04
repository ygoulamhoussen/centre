<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { NCard, NGi, NGrid, useMessage } from 'naive-ui'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

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
const router = useRouter()

interface DashboardData {
  nombreBiens: number
  repartitionTypes: Record<string, number>
  tauxOccupation: number
  totalLoyersPercus: number
  nombreImpayes: number
  alertesImpaye?: number // Ancienne propriété pour rétrocompatibilité
  resultatFiscalEstime: number
}

const dashboardData = ref<DashboardData>({
  nombreBiens: 0,
  repartitionTypes: {},
  tauxOccupation: 0,
  totalLoyersPercus: 0,
  nombreImpayes: 0,
  alertesImpaye: 0,
  resultatFiscalEstime: 0,
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
    title: 'Taux d\'occupation',
    value: Math.round(dashboardData.value.tauxOccupation * 100),
    unit: '',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:account-group-outline',
  },
  {
    key: 'resultatFiscalEstime',
    title: 'Résultat fiscal estimé',
    value: dashboardData.value.resultatFiscalEstime,
    unit: '€',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:calculator-variant',
  },
  {
    key: 'alertesImpaye',
    title: 'Alertes impayés',
    value: dashboardData.value.nombreImpayes || 0,
    unit: '',
    color: { start: 'rgb(var(--primary-color))', end: 'rgb(var(--primary-600-color))' },
    icon: 'mdi:alert-circle-outline',
  },
])

function getGradientColor(color: CardData['color']) {
  return `linear-gradient(to bottom right, ${color.start}, ${color.end})`
}

function handleCardClick(key: string) {
  switch (key) {
    case 'nombreBiens':
      router.push('/propriete')
      break
    case 'tauxOccupation':
      router.push('/location')
      break
    case 'resultatFiscalEstime':
      router.push('/resultat-fiscal')
      break
    case 'alertesImpaye':
      router.push('/quittance')
      break
  }
}

onMounted(async () => {
  try {
    const res = await fetch(
      `${import.meta.env.VITE_SERVICE_BASE_URL}/api/dashboard/${authStore.userInfo.userId}`,
    )

    if (!res.ok) {
      throw new Error('Erreur lors du chargement du tableau de bord')
    }

    dashboardData.value = await res.json()
  }
  catch (e) {
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
          class="rd-8px px-16px pb-4px pt-8px text-white card-link"
          :style="{ backgroundImage: getGradientColor(item.color) }"
          @click="handleCardClick(item.key)"
        >
          <h3 class="text-16px">
            {{ item.title }}
          </h3>
          <div class="flex justify-between pt-12px">
            <SvgIcon :icon="item.icon" class="text-32px" />
            <template v-if="item.key === 'tauxOccupation'">
              {{ item.value }}%
            </template>
            <CountTo
              v-else
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

<style scoped>
.card-wrapper {
  width: 100%;
}

.rd-8px {
  border-radius: 8px;
  height: 100%;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.flex {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  flex: 1;
}

.text-30px {
  font-size: 30px;
  line-height: 1;
  font-weight: 500;
}

.text-16px {
  font-size: 16px;
  line-height: 1.2;
  margin: 0;
  font-weight: 500;
}

.text-32px {
  font-size: 32px;
  opacity: 0.8;
}

.justify-between {
  justify-content: space-between;
  width: 100%;
}

.px-16px {
  padding-left: 16px;
  padding-right: 16px;
}

.pb-4px {
  padding-bottom: 4px;
}

.pt-8px {
  padding-top: 8px;
}

.pt-12px {
  padding-top: 12px;
}

.card-link {
  cursor: pointer;
  transition:
    box-shadow 0.2s,
    transform 0.2s;
}
.card-link:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px) scale(1.02);
}
</style>
