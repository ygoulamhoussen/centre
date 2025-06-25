<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { ArrowRight24Filled, Call24Filled, Mail24Filled, Person24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NForm,
  NFormItemGi,
  NGrid,
  NH2,
  NIcon,
  NInput,
  NStep,
  NSteps,
  useMessage,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

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

function suivant() {
  if (!locataireDTO.value.nom || !locataireDTO.value.telephone || !locataireDTO.value.email) {
    message.warning('Veuillez remplir tous les champs obligatoires.')
    return
  }
  router.push('/locataire-etape-2')
}
</script>

<template>
  <div class="p-4">
    <NCard :bordered="false">
      <div class="steps-wrapper" style="overflow-x:auto;">
        <NSteps :current="1" class="mb-8">
          <NStep title="Informations" description="Nom, contact, etc." />
          <NStep title="Adresse" description="Lieu de résidence" />
          <NStep title="Récapitulatif" description="Vérification finale" />
        </NSteps>
      </div>

      <NH2 class="titre-principal mb-4">Étape 1: Informations personnelles</NH2>

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
        <NButton type="primary" @click="suivant">
          Suivant
          <template #icon>
            <NIcon :component="ArrowRight24Filled" />
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
@media (max-width: 768px) {
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
