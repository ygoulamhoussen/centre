<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'
import { useUnifiedStore } from '@/store/unifiedStore'
import { DocumentPdf24Filled } from '@vicons/fluent'
import {
  NButton,
  NCard,
  NEmpty,
  NGi,
  NGrid,
  NH1,
  NH3,
  NIcon,
  NSpin,
  NTag,
  NText,
  NTooltip,
  useMessage
} from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

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
      <NH1 class="titre-principal">Mes quittances</NH1>
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
          <NCard hoverable class="quittance-card cursor-pointer" @click="router.push(`/quittance-detail/${q.id}`)">
            <div class="flex items-start gap-4">
              <div class="quittance-avatar">
                <NIcon :component="DocumentPdf24Filled" size="32" />
              </div>
              <div class="flex-1">
                <NH3 class="quittance-titre mb-2">
                  {{ q.proprieteNom }} – {{ q.locataireNom || 'Aucun locataire' }}
                </NH3>
                <div class="text-sm space-y-1">
                  <div><NText strong>Période :</NText> <NText depth="3">{{ q.dateDebut }} - {{ q.dateFin }}</NText></div>
                  <div><NText strong>Loyer :</NText> <NText depth="3">{{ q.montantLoyer }} €</NText></div>
                  <div><NText strong>Charges :</NText> <NText depth="3">{{ q.montantCharges }} €</NText></div>
                  <div v-if="q.depotGarantie && Number(q.depotGarantie) > 0"><NText strong>Caution :</NText> <NText depth="3">{{ Number(q.depotGarantie).toFixed(2) }} €</NText></div>
                  <div><NText strong>Total :</NText> <NText depth="3">{{ getTotalQuittance(q) }} €</NText></div>
                  <div class="flex items-center gap-2">
                    <NText strong>Statut :</NText>
                    <NTag :type="q.statut === 'PAYEE' ? 'success' : (q.statut === 'PARTIELLE' ? 'warning' : 'error')" size="small">
                      {{ q.statut === 'PAYEE' ? 'Payée' : (q.statut === 'PARTIELLE' ? 'Partielle' : 'Impayée') }}
                    </NTag>
                  </div>
                </div>
              </div>
            </div>
            <template #footer>
              <div class="flex justify-end gap-2">
                <template v-if="q.statut !== 'PAYEE'">
                  <NTooltip>
                    <template #trigger>
                      <NButton size="small" type="primary" ghost :disabled="q.statut !== 'PAYEE'" @click.stop>
                        Editer la quittance
                      </NButton>
                    </template>
                    La quittance n'est téléchargeable que si elle est payée.
                  </NTooltip>
                </template>
                <template v-else>
                  <NButton size="small" type="primary" ghost @click.stop="telechargerQuittance(q.id)">
                    Editer la quittance
                  </NButton>
                </template>
              </div>
            </template>
          </NCard>
        </NGi>
      </NGrid>
    </NSpin>
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
.quittance-titre {
  color: var(--n-text-color) !important;
  font-size: 1.1rem;
  font-weight: 600;
}
.quittance-card {
  cursor: pointer;
  transition: 
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.quittance-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.quittance-avatar {
  background-color: var(--n-color-embedded);
  color: var(--n-color-target);
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
@media (max-width: 768px) {
  .titre-principal,
  h1,
  h2,
  h3 {
    font-size: 1.25rem !important;
  }
  .quittance-titre {
    font-size: 1rem;
  }
  .p-4 {
    padding: 1rem !important;
  }
  .mb-6 {
    margin-bottom: 1rem !important;
  }
}
</style>
