<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { useMessage, NForm, NFormItem, NInput, NInputNumber, NSelect, NButton, NCard, NGrid, NGi } from 'naive-ui'
import { createCharge } from '@/service/api/charges-recettes'
import { naturesCharges } from '@/constants/compta'

definePage({
  meta: {
    title: 'Nouvelle charge',
    hideInMenu: true,
    activeMenu: '/comptabilite',
  },
})

const router = useRouter()
const route = useRoute()
const message = useMessage()
const authStore = useAuthStore()

const proprieteId = (route.params.id as string) || (route.query.proprieteId as string) || ''
const charge = ref({
  intitule: '',
  montant: '',
  dateCharge: new Date().toISOString().split('T')[0],
  proprieteId,
  nature: '',
  commentaire: '',
})
const loading = ref(false)

const proprietes = ref<{ label: string; value: string }[]>([])
onMounted(async () => {
  if (!charge.value.proprieteId) {
    // Charger la liste des propriétés de l'utilisateur
    const userId = authStore.userInfo.userId
    const resp = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprietesByUtilisateur/${userId}`)
    if (resp.ok) {
      const data = await resp.json()
      proprietes.value = data.map((p: any) => ({ label: p.nom, value: p.id }))
    }
  }
})

async function submit() {
  if (!charge.value.intitule || !charge.value.montant || !charge.value.dateCharge || !charge.value.nature || !charge.value.proprieteId) {
    message.error('Veuillez remplir tous les champs obligatoires (y compris la propriété)')
    return
  }
  loading.value = true
  try {
    await createCharge({
      ...charge.value,
      utilisateurId: authStore.userInfo.userId,
    })
    message.success('Charge créée avec succès')
    router.back()
  } catch (e: any) {
    message.error(e.message || 'Erreur lors de la création')
  } finally {
    loading.value = false
  }
}
</script>
<template>
  <div class="p-4">
    <NCard :bordered="false">
      <h2 class="text-xl font-bold mb-4">Nouvelle charge</h2>
      <NForm @submit.prevent="submit">
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
          <NGi v-if="!charge.proprieteId" :span="2">
            <NFormItem label="Propriété" required>
              <NSelect v-model:value="charge.proprieteId" :options="proprietes" placeholder="Sélectionner une propriété" />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Commentaire">
              <NInput v-model:value="charge.commentaire" type="textarea" :rows="3" placeholder="Commentaire optionnel" />
            </NFormItem>
          </NGi>
        </NGrid>
        <div class="flex justify-end mt-6">
          <NButton type="primary" :loading="loading" @click="submit">Créer</NButton>
        </div>
      </NForm>
    </NCard>
  </div>
</template> 