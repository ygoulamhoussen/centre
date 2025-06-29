<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useMessage, NForm, NFormItem, NInput, NInputNumber, NSelect, NButton, NCard, NGrid, NGi, NDatePicker } from 'naive-ui'
import { useRoute, useRouter } from 'vue-router'
import { fetchRecetteById, updateRecette } from '@/service/api/charges-recettes'
import { typesRecettes } from '@/constants/compta'

definePage({
  meta: {
    title: 'Modifier une recette',
    hideInMenu: true,
    activeMenu: '/comptabilite',
  },
})

const router = useRouter()
const route = useRoute()
const message = useMessage()

const recetteId = route.params.id as string
const recette = ref<any>(null)
const loading = ref(false)
const proprietesOptions = ref<any[]>([])

onMounted(async () => {
  recette.value = await fetchRecetteById(recetteId)
  if (recette.value && recette.value.montant) {
    recette.value.montant = Number(recette.value.montant)
  }
  if (recette.value && recette.value.proprieteId && recette.value.proprieteNom) {
    proprietesOptions.value = [{
      label: recette.value.proprieteNom,
      value: recette.value.proprieteId,
    }]
  }
})

async function submit() {
  if (!recette.value.intitule || !recette.value.montant || !recette.value.dateRecette || !recette.value.type) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }
  loading.value = true
  try {
    await updateRecette(recetteId, recette.value)
    message.success('Recette modifiée avec succès')
    router.back()
  } catch (e: any) {
    message.error(e.message || 'Erreur lors de la modification')
  } finally {
    loading.value = false
  }
}
</script>
<template>
  <div class="p-4">
    <NCard :bordered="false">
      <h2 class="text-xl font-bold mb-4">Modifier la recette</h2>
      <NForm v-if="recette" @submit.prevent="submit">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Intitulé" required>
              <NInput v-model:value="recette.intitule" placeholder="Ex: Loyer janvier 2024" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant (€)" required>
              <NInputNumber v-model:value="recette.montant" placeholder="0.00" :precision="2" :min="0" style="width: 100%" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Date" required>
              <NDatePicker
                v-model:formatted-value="recette.dateRecette"
                value-format="yyyy-MM-dd"
                type="date"
                clearable
                style="width: 100%"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Type de recette" required>
              <NSelect
                v-model:value="recette.type"
                :options="typesRecettes"
                placeholder="Type de recette"
                style="width: 100%"
              />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Propriété associée" required>
              <NSelect
                v-model:value="recette.proprieteId"
                :options="proprietesOptions"
                placeholder="Sélectionner la propriété"
                style="width: 100%"
                :disabled="true"
              />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Commentaire">
              <NInput v-model:value="recette.commentaire" type="textarea" :rows="3" placeholder="Commentaire optionnel" />
            </NFormItem>
          </NGi>
        </NGrid>
        <div class="flex justify-end mt-6">
          <NButton type="primary" :loading="loading" @click="submit">Enregistrer</NButton>
        </div>
      </NForm>
      <div v-else class="text-center py-8 text-gray-500">Chargement de la recette...</div>
    </NCard>
  </div>
</template> 