<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { Checkmark24Filled, Call24Filled, Mail24Filled, Person24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItemGi,
  NGrid,
  NIcon,
  NInput,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from '@/store/modules/auth'

definePage({
  meta: {
    title: 'Nouveau locataire - Informations',
    hideInMenu: true,
    activeMenu: '/locataire',
  },
})

const store = useUnifiedStore()
const { locataireDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()
// Plus de stepper ni d'étape récapitulative
const isMobile = ref(window.innerWidth < 768)

function handleResize() {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

async function valider() {
  if (!locataireDTO.value.nom || !locataireDTO.value.telephone || !locataireDTO.value.email) {
    message.warning('Veuillez remplir tous les champs obligatoires.')
    return
  }
  const utilisateurId = authStore.userInfo?.userId
  if (!utilisateurId) {
    message.error('Utilisateur non authentifié. Veuillez vous reconnecter.')
    return
  }
  const payload = {
    nom: locataireDTO.value.nom,
    telephone: locataireDTO.value.telephone,
    email: locataireDTO.value.email,
    utilisateurId
  }
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/createLocataire`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!response.ok) throw new Error('Erreur lors de la création du locataire')
    message.success('Locataire ajouté avec succès !')
    router.push('/locataire')
  } catch (e) {
    message.error(e.message || 'Erreur lors de la création du locataire')
  }
}
</script>

<template>
  <div class="page-container">
    <div class="p-4">
      <NCard :bordered="false" class="recap-card">
        <!-- Plus de stepper ni d'étape mobile -->
        <div class="content-area">
          <NForm>
            <NGrid :x-gap="24" :y-gap="24" :cols="1">
              <NFormItemGi label="Nom complet *">
                <NInput v-model:value="locataireDTO.nom" placeholder="Saisir le nom complet" size="large">
                  <template #prefix>
                    <NIcon :component="Person24Filled" />
                  </template>
                </NInput>
              </NFormItemGi>
              <NFormItemGi label="Téléphone *">
                <NInput v-model:value="locataireDTO.telephone" placeholder="Saisir le numéro de téléphone" size="large">
                  <template #prefix>
                    <NIcon :component="Call24Filled" />
                  </template>
                </NInput>
              </NFormItemGi>
              <NFormItemGi label="Email *">
                <NInput v-model:value="locataireDTO.email" placeholder="Saisir l'adresse email" size="large">
                  <template #prefix>
                    <NIcon :component="Mail24Filled" />
                  </template>
                </NInput>
              </NFormItemGi>
            </NGrid>
          </NForm>
        </div>

        <div class="button-container">
          <div class="flex justify-end">
            <NButton type="primary" @click="valider" title="Valider">
              <template #icon>
                <NIcon :component="Checkmark24Filled" />
              </template>
            </NButton>
          </div>
        </div>
      </NCard>
    </div>
  </div>
</template>

<style scoped>
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

/* Styles existants */
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}

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

  .mb-8 {
    margin-bottom: 1rem !important;
  }

  .n-steps {
    font-size: 12px !important;
    min-width: 400px;
  }

  .n-steps,
  .n-steps .n-steps-main {
    overflow-x: auto !important;
    white-space: nowrap !important;
    display: block !important;
  }

  .n-step {
    min-width: 120px !important;
  }

  .n-step__description {
    display: none !important;
  }

  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }

  .p-4 {
    padding: 1rem !important;
  }

  .mb-4,
  .mb-6,
  .mb-8 {
    margin-bottom: 1rem !important;
  }
}
</style>
