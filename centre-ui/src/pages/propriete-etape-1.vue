<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowRight24Filled,
  Building24Filled,
  BuildingHome24Filled,
  Home24Filled,
  Location24Filled,
  VehicleCar24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NGi,
  NGrid,
  NIcon,
  NStep,
  NSteps,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle propriété - Type de bien',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()

const typesBien = [
  { value: 'APPARTEMENT', label: 'Appartement', icon: BuildingHome24Filled, emoji: '🏠' },
  { value: 'MAISON', label: 'Maison', icon: Home24Filled, emoji: '🏡' },
  { value: 'PARKING', label: 'Parking/Box', icon: VehicleCar24Filled, emoji: '🚗' },
  { value: 'COMMERCIAL', label: 'Local commercial', icon: Building24Filled, emoji: '🏢' },
  { value: 'TERRAIN', label: 'Terrain', icon: Location24Filled, emoji: '🌿' },
]

const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function choisirType(type: string) {
  store.updateProprieteField('typeBien', type)
}

function valider() {
  if (!proprieteDTO.value.typeBien) {
    return
  }
  router.push('/propriete-etape-2')
}
</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <div class="mb-8" v-if="!isMobile">
          <NSteps :current="0" size="small">
            <NStep title="Type de bien" status="process" />
            <NStep title="Adresse" />
            <NStep title="Infos essentielles" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 1/3</div>
          <div class="step-mobile-label">Type de bien</div>
        </div>

        <div class="content-area">
          <div class="type-titre mb-6">Quel est le type de votre bien ?</div>
          <NGrid :cols="isMobile ? 2 : 3" :x-gap="16" :y-gap="16" responsive="screen" :item-responsive="true" class="type-grid">
            <NGi v-for="type in typesBien" :key="type.value">
              <NCard
                class="type-card"
                :class="{ selected: proprieteDTO.typeBien === type.value }"
                @click="choisirType(type.value)"
                hoverable
              >
                <div class="type-card-content">
                  <div class="type-emoji">{{ type.emoji }}</div>
                  <div class="type-label">{{ type.label }}</div>
                </div>
              </NCard>
            </NGi>
          </NGrid>
        </div>

        <div class="button-container">
          <div :class="[isMobile ? 'flex-center' : 'flex-end']">
            <NButton
              type="primary"
              @click="valider"
              :disabled="!proprieteDTO.typeBien"
              title="Suivant"
              size="large"
            >
              <template #icon>
                <NIcon :component="ArrowRight24Filled" />
              </template>
              Suivant
            </NButton>
          </div>
        </div>
      </NCard>
    </div>
  </div>
</template>

<style>
/* Layout principal */
.page-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.recap-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: calc(100vh - 2rem);
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem;
}

.button-container {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
  background: white;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

/* Styles pour les cartes de type */
.type-titre {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  text-align: center;
  margin-bottom: 2rem;
}

.type-grid {
  margin-bottom: 2rem;
}

.type-card {
  cursor: pointer;
  transition: all 0.2s ease;
  border: 2px solid transparent;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.type-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.type-card.selected {
  border-color: #1976d2;
  background-color: #f3f8ff;
}

.type-card-content {
  text-align: center;
  padding: 1rem;
}

.type-emoji {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.type-label {
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}

/* Mobile stepper */
.mobile-stepper {
  text-align: center;
  margin-bottom: 1.5rem;
}

.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1976d2;
}

.step-mobile-label {
  font-size: 1.2rem;
  color: #222;
  margin-bottom: 1rem;
}

.flex-center {
  display: flex;
  justify-content: center;
}

.flex-end {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .page-container {
    min-height: 100vh;
  }

  .recap-card {
    min-height: calc(100vh - 2rem);
  }

  .content-area {
    max-height: calc(100vh - 200px);
    overflow-y: auto;
  }

  .button-container {
    position: sticky;
    bottom: 0;
    background: white;
    padding: 1rem 0;
    margin-top: 1rem;
    border-top: 1px solid #f0f0f0;
  }

  .type-titre {
    font-size: 1.3rem;
    margin-bottom: 1.5rem;
  }

  .type-card {
    height: 100px;
  }

  .type-emoji {
    font-size: 2rem;
  }

  .type-label {
    font-size: 0.9rem;
  }
}
</style>
