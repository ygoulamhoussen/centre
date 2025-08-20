<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  ArrowLeft24Filled,
  ArrowRight24Filled,
  City24Filled,
  Home24Filled,
  Location24Filled,
} from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItemGi,
  NGrid,
  NIcon,
  NInput,
  NSpace,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Nouvelle propriété - Adresse',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

function precedent() {
  router.push('/propriete-etape-1')
}

function valider() {
  // Validation des champs si renseignés
  if (proprieteDTO.value.adresse || proprieteDTO.value.codePostal || proprieteDTO.value.ville) {
    // Si au moins un champ est rempli, valider tous les champs
    if (!proprieteDTO.value.adresse || !proprieteDTO.value.codePostal || !proprieteDTO.value.ville) {
      message.warning('Si vous renseignez une adresse, tous les champs doivent être complétés.')
      return
    }
    
    // Validation du code postal (5 chiffres)
    if (!/^\d{5}$/.test(proprieteDTO.value.codePostal)) {
      message.warning('Le code postal doit contenir exactement 5 chiffres.')
      return
    }
  }
  
  router.push('/propriete-etape-3')
}
</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <div class="mb-8" v-if="!isMobile">
          <NSteps :current="1" size="small">
            <NStep title="Type de bien" status="finish" />
            <NStep title="Adresse" status="process" />
            <NStep title="Infos essentielles" />
          </NSteps>
        </div>
        <div v-else class="mobile-stepper mb-8">
          <div class="step-mobile-number">Étape 2/3</div>
          <div class="step-mobile-label">Adresse</div>
        </div>

        <div class="content-area">
          <div class="section-title mb-6">
            <h2>Adresse du bien</h2>
            <p class="section-subtitle">Tous les champs sont optionnels</p>
          </div>

          <NForm>
            <NGrid :x-gap="24" :y-gap="24" :cols="isMobile ? 1 : 2" :item-responsive="true" class="form-grid">
              <NFormItemGi :span="2" label="Adresse">
                <NInput 
                  v-model:value="proprieteDTO.adresse" 
                  placeholder="Numéro et nom de rue" 
                  size="large"
                >
                  <template #prefix>
                    <NIcon :component="Home24Filled" />
                  </template>
                </NInput>
              </NFormItemGi>
              
              <NFormItemGi label="Code postal">
                <NInput 
                  v-model:value="proprieteDTO.codePostal" 
                  placeholder="75001" 
                  size="large" 
                  inputmode="numeric" 
                  pattern="[0-9]*"
                  maxlength="5"
                >
                  <template #prefix>
                    <NIcon :component="Location24Filled" />
                  </template>
                </NInput>
              </NFormItemGi>
              
              <NFormItemGi label="Ville">
                <NInput 
                  v-model:value="proprieteDTO.ville" 
                  placeholder="Paris" 
                  size="large"
                >
                  <template #prefix>
                    <NIcon :component="City24Filled" />
                  </template>
                </NInput>
              </NFormItemGi>
            </NGrid>
          </NForm>

          <div class="info-box">
            <p>💡 Vous pouvez laisser ces champs vides et continuer directement.</p>
          </div>
        </div>

        <div class="button-container">
          <NSpace :class="[isMobile ? 'flex-center' : 'flex-end']" justify="space-between">
            <NButton @click="precedent" title="Précédent">
              <template #icon>
                <NIcon :component="ArrowLeft24Filled" />
              </template>
              Précédent
            </NButton>
            <NButton type="primary" @click="valider" title="Suivant" size="large">
              <template #icon>
                <NIcon :component="ArrowRight24Filled" />
              </template>
              Suivant
            </NButton>
          </NSpace>
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

/* Styles pour les sections */
.section-title {
  text-align: center;
  margin-bottom: 2rem;
}

.section-title h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.section-subtitle {
  font-size: 1rem;
  color: #666;
  margin: 0;
}

.info-box {
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 1rem;
  margin-top: 2rem;
  text-align: center;
}

.info-box p {
  margin: 0;
  color: #495057;
  font-size: 0.95rem;
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

  .section-title h2 {
    font-size: 1.3rem;
  }

  .section-subtitle {
    font-size: 0.9rem;
  }

  .info-box {
    margin-top: 1.5rem;
    padding: 0.75rem;
  }

  .info-box p {
    font-size: 0.9rem;
  }
}
</style>
