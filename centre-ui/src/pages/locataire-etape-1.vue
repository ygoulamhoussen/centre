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
  <div class="p-4">
    <NCard :bordered="false">
      <!-- Plus de stepper ni d'étape mobile -->
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

      <div class="flex justify-end mt-8">
        <NButton type="primary" @click="valider" title="Valider">
          <template #icon>
            <NIcon :component="Checkmark24Filled" />
          </template>
        </NButton>
      </div>
    </NCard>
  </div>
</template>

<style scoped>
.titre-principal,
h1,
h2,
h3 {
  color: var(--n-text-color) !important;
  font-weight: bold;
}
.progress-steps {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0 8px 0;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  position: relative;
}
.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 20px;
  left: 50%;
  width: 100%;
  height: 2px;
  background-color: #e5e7eb;
  z-index: 1;
}
.step.completed:not(:last-child)::after {
  background-color: #9C27B0;
}
.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  background-color: #e5e7eb;
  color: #6b7280;
  z-index: 2;
  position: relative;
}
.step.active .step-number {
  background-color: #9C27B0;
  color: white;
}
.step.completed .step-number {
  background-color: #10b981;
  color: white;
}
.step-label {
  font-size: 14px;
  color: #6b7280;
  text-align: center;
}
.step.active .step-label {
  color: #9C27B0;
  font-weight: 500;
}
.step.completed .step-label {
  color: #10b981;
  font-weight: 500;
}
.progress-steps-mobile-simple {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
  gap: 4px;
}
.step-mobile-number {
  font-size: 1.1rem;
  font-weight: 700;
  color: #9C27B0;
}
.step-mobile-label {
  font-size: 1rem;
  color: #757575;
  text-align: center;
}
@media (max-width: 768px) {
  .progress-steps {
    display: none !important;
  }
  .progress-steps-mobile-simple {
    margin-bottom: 1rem !important;
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
</style>
