<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { NButton, NInput, NGrid, NGi, useMessage } from 'naive-ui'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

definePage({
  meta: {
    title: 'Ajouter une propriété - Étape 1',
    hideInMenu: true,
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

  router.push('/propriete_workflow/etape-2')
}
</script>

<template>
  <NSpace vertical :size="24">
    <h1 class="text-xl font-bold">Ajouter une propriété - Étape 1</h1>

    <div>
      <p class="mb-2">Quel est le type de bien&nbsp;?</p>
      <NGrid cols="1 s:1 m:2 l:4" x-gap="12" y-gap="12">
        <NGi v-for="type in typesBien" :key="type.value">
          <NButton
            block
            :type="proprieteDTO.typeBien === type.value ? 'primary' : 'default'"
            @click="choisirType(type.value)"
          >
            {{ type.label }}
          </NButton>
        </NGi>
      </NGrid>
    </div>

    <div>
      <label class="block mb-1">Nom de la propriété</label>
      <NInput
        v-model:value="proprieteDTO.nom"
        placeholder="Ex : Résidence Les Lilas, Parking centre-ville..."
      />
    </div>

    <div>
      <NButton
        type="primary"
        @click="valider"
        :disabled="!proprieteDTO.typeBien || !proprieteDTO.nom"
      >
        Suivant
      </NButton>
    </div>
  </NSpace>
</template>
