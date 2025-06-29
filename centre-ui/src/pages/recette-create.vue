<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { useMessage, NForm, NFormItem, NInput, NInputNumber, NSelect, NButton, NCard, NGrid, NGi } from 'naive-ui'
import { createRecette } from '@/service/api/charges-recettes'
import { typesRecettes } from '@/constants/compta'

definePage({
  meta: {
    title: 'Nouvelle recette',
    hideInMenu: true,
    activeMenu: '/comptabilite',
  },
})

const router = useRouter()
const route = useRoute()
const message = useMessage()
const authStore = useAuthStore()

const proprieteId = (route.params.id as string) || (route.query.proprieteId as string) || ''
const recette = ref({
  intitule: '',
  montant: '',
  dateRecette: new Date().toISOString().split('T')[0],
  proprieteId,
  type: '',
  commentaire: '',
})
const loading = ref(false)

const proprietes = ref<{ label: string; value: string }[]>([])
onMounted(async () => {
  if (!recette.value.proprieteId) {
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
  if (!recette.value.intitule || !recette.value.montant || !recette.value.dateRecette || !recette.value.type || !recette.value.proprieteId) {
    message.error('Veuillez remplir tous les champs obligatoires (y compris la propriété)')
    return
  }
  loading.value = true
  try {
    await createRecette({
      ...recette.value,
      utilisateurId: authStore.userInfo.userId,
    })
    message.success('Recette créée avec succès')
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
      <h2 class="text-xl font-bold mb-4">Nouvelle recette</h2>
      <NForm @submit.prevent="submit">
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
              <NInput v-model:value="recette.dateRecette" type="text" />
            </NFormItem>
          </NGi>
          <NGi>
            <NFormItem label="Type" required>
              <NSelect v-model:value="recette.type" :options="typesRecettes" placeholder="Sélectionner un type" />
            </NFormItem>
          </NGi>
          <NGi v-if="!recette.proprieteId" :span="2">
            <NFormItem label="Propriété" required>
              <NSelect v-model:value="recette.proprieteId" :options="proprietes" placeholder="Sélectionner une propriété" />
            </NFormItem>
          </NGi>
          <NGi :span="2">
            <NFormItem label="Commentaire">
              <NInput v-model:value="recette.commentaire" type="textarea" :rows="3" placeholder="Commentaire optionnel" />
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