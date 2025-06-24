<script setup lang="ts">
import { NButton, NCard, NGi, NGrid, NH1, NPopconfirm, NSpace, NEmpty, NSpin, NIcon, useMessage, NTag, NTooltip } from 'naive-ui'
import { DocumentPdf24Filled } from '@vicons/fluent'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'

const router = useRouter()
const message = useMessage()
const quittances = ref<any[]>([])
const authStore = useAuthStore()
const userId = authStore.userInfo.userId
const loading = ref(false)

definePage({
  meta: {
    title: 'Mes quittances',
    icon: 'mdi:receipt-text',
    order: 5,
  },
})

async function fetchQuittances() {
  try {
    loading.value = true
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/getQuittancesByUtilisateur/${userId}`)
    quittances.value = await res.json()
  } catch (e) {
    console.error(e)
    message.error('Erreur de chargement des quittances')
  } finally {
    loading.value = false
  }
}

async function supprimerQuittance(id: string) {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deleteQuittance/${id}`, { method: 'DELETE' })
    if (!res.ok) {
      const errorText = await res.text()
      const q = quittances.value.find(q => q.id === id)
      const proprieteNom = q?.proprieteNom || 'propriété inconnue'
      const locataireNom = q?.locataireNom || 'locataire inconnu'
      if (
        errorText.includes('paiement_quittance_id_fkey')
        || errorText.includes('DataIntegrityViolationException')
        || errorText.includes('violates foreign key constraint')
      ) {
        message.error(`Suppression impossible : la quittance pour la propriété "${proprieteNom}" et le locataire "${locataireNom}" est liée à un ou plusieurs paiements. Veuillez d'abord supprimer les paiements associés.`)
      } else {
        message.error('Erreur lors de la suppression')
      }
      return
    }
    message.success('Quittance supprimée')
    fetchQuittances()
  } catch (e) {
    console.error(e)
    message.error('Erreur lors de la suppression')
  }
}

function demarrerCreation() {
  router.push('/quittance-etape-1')
}

async function telechargerQuittance(id: string) {
  try {
    const res = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/generateQuittancePdf/${id}`, {
      method: 'GET'
    })
    const blob = await res.blob()
    const url = window.URL.createObjectURL(blob)

    const link = document.createElement('a')
    link.href = url
    link.download = `quittance-${id}.pdf`
    link.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    console.error(e)
    message.error("Erreur lors du téléchargement du PDF")
  }
}

function getTotalQuittance(q: Record<string, any>) {
  const loyer = Number(q.montantLoyer) || 0
  const charges = Number(q.montantCharges) || 0
  const caution = Number(q.depotGarantie) || 0
  return (loyer + charges + caution).toFixed(2)
}

onMounted(() => fetchQuittances())
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Mes quittances</h1>
      <NButton type="primary" @click="demarrerCreation">
        Créer une quittance
      </NButton>
    </div>
    <NSpin :show="loading">
      <div v-if="quittances.length === 0" class="text-center py-12">
        <NEmpty description="Aucune quittance enregistrée.">
          <template #extra>
            <NButton type="primary" @click="demarrerCreation">
              Créer une quittance
            </NButton>
          </template>
        </NEmpty>
      </div>
      <NGrid v-else :x-gap="16" :y-gap="16" cols="1 s:1 m:2 l:3 xl:4">
        <NGi v-for="q in quittances" :key="q.id">
          <NCard hoverable class="quittance-card">
            <div class="flex items-start">
              <div class="quittance-avatar">
                <NIcon :component="DocumentPdf24Filled" size="32" />
              </div>
              <div class="ml-4 flex-1">
                <h3 class="text-lg font-semibold mb-1">
                  {{ q.proprieteNom }} – {{ q.locataireNom || 'Aucun locataire' }}
                </h3>
                <div class="text-gray-600 text-sm space-y-1">
                  <div><strong>Période :</strong> {{ q.dateDebut }} - {{ q.dateFin }}</div>
                  <div><strong>Loyer :</strong> {{ q.montantLoyer }} €</div>
                  <div><strong>Charges :</strong> {{ q.montantCharges }} €</div>
                  <div v-if="q.depotGarantie && Number(q.depotGarantie) > 0"><strong>Caution :</strong> {{ Number(q.depotGarantie).toFixed(2) }} €</div>
                  <div><strong>Total (loyer + charges<span v-if="q.depotGarantie && Number(q.depotGarantie) > 0"> + caution</span>) :</strong> {{ getTotalQuittance(q) }} €</div>
                  <div>
                    <strong>Statut :</strong>
                    <NTag :type="q.statut === 'PAYEE' ? 'success' : (q.statut === 'PARTIELLE' ? 'warning' : 'error')" size="small">
                      {{ q.statut === 'PAYEE' ? 'Payée' : (q.statut === 'PARTIELLE' ? 'Partielle' : 'Impayée') }}
                    </NTag>
                  </div>
                </div>
              </div>
            </div>
            <template #footer>
              <div class="flex justify-end gap-2">
                <NPopconfirm
                  @positive-click="() => supprimerQuittance(q.id)"
                  positive-text="Supprimer"
                  negative-text="Annuler"
                >
                  <template #trigger>
                    <NButton size="small" type="info" ghost @click="router.push(`/quittance-detail/${q.id}`)">
                      Modifier
                    </NButton>
                  </template>
                </NPopconfirm>
                <NTooltip v-if="q.statut !== 'PAYEE'">
                  <template #trigger>
                    <NButton size="small" type="primary" ghost :disabled="q.statut !== 'PAYEE'">
                      Editer la quittance
                    </NButton>
                  </template>
                  La quittance n'est téléchargeable que si elle est payée.
                </NTooltip>
                <NButton v-else size="small" type="primary" ghost @click="telechargerQuittance(q.id)">
                  Editer la quittance
                </NButton>
              </div>
            </template>
          </NCard>
        </NGi>
      </NGrid>
    </NSpin>
  </div>
</template>

<style scoped>
.quittance-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.quittance-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.quittance-avatar {
  background-color: #f6f6f6;
  color: #409eff;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.ml-4 {
  margin-left: 1rem;
}
.flex {
  display: flex;
}
.justify-end {
  justify-content: flex-end;
}
.gap-2 {
  gap: 0.5rem;
}
.mb-1 {
  margin-bottom: 0.25rem;
}
.mb-6 {
  margin-bottom: 1.5rem;
}
.py-12 {
  padding-top: 3rem;
  padding-bottom: 3rem;
}
.text-center {
  text-align: center;
}
</style>
