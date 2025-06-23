<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import {
  NButton,
  NForm,
  NFormItemGi,
  NInput,
  useMessage,
  NCard,
  NSteps,
  NStep,
  NIcon,
  NGrid,
} from 'naive-ui'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { Person24Filled, Call24Filled, Mail24Filled, ArrowRight24Filled } from '@vicons/fluent'

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
      <NSteps :current="1" class="mb-8">
        <NStep title="Informations" description="Nom, contact, etc." />
        <NStep title="Adresse" description="Lieu de résidence" />
        <NStep title="Récapitulatif" description="Vérification finale" />
      </NSteps>

      <h2 class="text-xl font-semibold mb-4">Étape 1: Informations personnelles</h2>

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
