<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { ref } from 'vue'

definePage({
  meta: {
    title: 'Ajouter une propriété - Étape 1',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})

const store = useUnifiedStore()
const { proprieteDTO } = storeToRefs(store)
const router = useRouter()
const message = useMessage()

const typesBien = [
  { value: 'APPARTEMENT', label: 'Appartement' },
  { value: 'MAISON', label: 'Maison' },
  { value: 'BOX', label: 'Box' },
  { value: 'PARKING', label: 'Parking' }
]

function choisirType(type: string) {
  store.updateProprieteField('typeBien', type)
}

function valider() {
  if (!proprieteDTO.value.typeBien || !proprieteDTO.value.nom) {
    message.warning('Veuillez remplir tous les champs requis.')
    return
  }

  router.push('/propriete-etape-2')
}
</script>

<template>
<n-space vertical :size="24">
    <n-h1>Ajouter une propriété - Étape 1</n-h1>
    <n-form :model="proprieteDTO" label-placement="top">
      <n-form-item label="Nom de la propriété" path="nom">
        <n-input
          v-model:value="proprieteDTO.nom"
          placeholder="Ex : Résidence Les Lilas, Parking centre-ville..."
        />
      </n-form-item>
    </n-form>
    <n-text strong class="block">Quel est le type de bien&nbsp;?</n-text>
    <n-grid cols="1 s:1 m:2 l:4" x-gap="12" y-gap="12">
      <n-gi v-for="type in typesBien" :key="type.value">
        <n-button
          block
          :type="proprieteDTO.typeBien === type.value ? 'primary' : 'default'"
          @click="choisirType(type.value)"
        >
          {{ type.label }}
        </n-button>
      </n-gi>
    </n-grid>



    <n-button
      type="primary"
      @click="valider"
      :disabled="!proprieteDTO.typeBien || !proprieteDTO.nom"
    >
      Suivant
    </n-button>
  </n-space>
</template>
