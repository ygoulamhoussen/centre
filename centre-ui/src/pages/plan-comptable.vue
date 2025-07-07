<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { NCard, NDataTable, NInput, NSpin, NH1 } from 'naive-ui'

const comptes = ref<any[]>([])
const loading = ref(true)
const search = ref('')

const columns = [
  { title: 'Code', key: 'code' },
  { title: 'Libellé', key: 'libelle' },
  { title: 'Type', key: 'type' },
]

async function fetchComptes() {
  loading.value = true
  try {
    const res = await fetch('http://localhost:8080/api/plan-comptable')
    const data = await res.json()
    comptes.value = data
  } catch (e) {
    comptes.value = []
  } finally {
    loading.value = false
  }
}

const filteredComptes = computed(() =>
  comptes.value.filter(c =>
    c.code.includes(search.value) ||
    c.libelle.toLowerCase().includes(search.value.toLowerCase())
  )
)

onMounted(fetchComptes)
</script>

<template>
  <div class="p-4">
    <NCard>
      <NH1 class="mb-4">Plan comptable</NH1>
      <NInput v-model:value="search" placeholder="Recherche code ou libellé..." class="mb-4" />
      <NSpin :show="loading">
        <NDataTable :columns="columns" :data="filteredComptes" />
      </NSpin>
    </NCard>
  </div>
</template> 