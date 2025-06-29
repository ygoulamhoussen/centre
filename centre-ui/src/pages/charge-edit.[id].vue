<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { useMessage, NForm, NFormItem, NInput, NInputNumber, NSelect, NButton, NCard, NGrid, NGi } from 'naive-ui'
import { updateCharge, fetchChargeById } from '@/service/api/charges-recettes'
import { naturesCharges } from '@/constants/compta'

definePage({
  meta: {
    title: 'Modifier une charge',
    hideInMenu: true,
    activeMenu: '/comptabilite',
  },
})

const router = useRouter()
const route = useRoute()
const message = useMessage()
const authStore = useAuthStore()

const proprieteId = route.params.id as string || ''
const chargeId = route.params.id as string
const charge = ref<any>(null)
const loading = ref(false)

onMounted(async () => {
  charge.value = await fetchChargeById(chargeId)
  if (charge.value && charge.value.montant) {
    charge.value.montant = Number(charge.value.montant)
  }
})

async function submit() {
  if (!charge.value.intitule || !charge.value.montant || !charge.value.dateCharge || !charge.value.nature) {
    message.error('Veuillez remplir tous les champs obligatoires')
    return
  }
  loading.value = true
  try {
    await updateCharge(chargeId, charge.value)
    message.success('Charge modifiée avec succès')
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
      <h2 class="text-xl font-bold mb-4">Modifier la charge</h2>
      <NForm v-if="charge" @submit.prevent="submit">
        <NGrid :cols="2" :x-gap="16">
          <NGi>
            <NFormItem label="Intitulé" required>
              <NInput v-model:value="charge.intitule" placeholder="Ex: Réparation chaudière" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Montant (€)" required>
              <NInputNumber v-model:value="charge.montant" placeholder="0.00" :precision="2" :min="0" style="width: 100%" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Date" required>
              <NInput v-model:value="charge.dateCharge" type="text" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Nature" required>
              <NSelect v-model:value="charge.nature" :options="naturesCharges" placeholder="Sélectionner une nature" />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Commentaire">
              <NInput v-model:value="charge.commentaire" type="textarea" :rows="3" placeholder="Commentaire optionnel" />
            </NFormItem>
          </NGi>
        </NGrid>
        <div class="flex justify-end mt-6">
          <NButton type="primary" :loading="loading" @click="submit">Enregistrer</NButton>
        </div>
      </NForm>
      <div v-else class="text-center py-8 text-gray-500">Chargement de la charge...</div>
    </NCard>
  </div>
</template> 