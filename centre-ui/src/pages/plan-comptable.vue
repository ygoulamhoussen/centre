<script setup lang="ts">
import { onMounted, computed, ref, h } from 'vue'
import { NCard, NDataTable, NInput, NH1, NSpin } from 'naive-ui'

definePage({
  meta: {
    title: 'Plan comptable',
    icon: 'mdi:book-open-variant',
    order: 10,
    hideInMenu:true,
  },
})

const comptes = ref<any[]>([])
const loading = ref(true)
const search = ref('')

const isMobile = ref(false)
onMounted(() => {
  isMobile.value = window.innerWidth < 768
  window.addEventListener('resize', () => {
    isMobile.value = window.innerWidth < 768
  })
})

const columns = computed(() => {
  const base = [
    { title: 'Code', key: 'code', ellipsis: false, className: 'code-cell' },
    { title: 'Libellé', key: 'libelle', ellipsis: false },
  ]
  if (!isMobile.value) {
    base.push(
      { title: 'Type', key: 'type', ellipsis: false },
      {
        title: 'Description',
        key: 'description',
        minWidth: 180,
        ellipsis: false,
        render(row: any) {
          return h(
            'div',
            { class: 'description-cell' },
            row.description
          )
        },
      }
    )
  }
  return base
})

async function fetchComptes() {
  loading.value = true
  try {
    const baseUrl = import.meta.env.VITE_SERVICE_BASE_URL
    const res = await fetch(`${baseUrl}/api/plan-comptable`)
    const data = await res.json()
    comptes.value = data
  } catch {
    comptes.value = []
  } finally {
    loading.value = false
  }
}

const filteredComptes = computed(() =>
  comptes.value.filter(c =>
    c.code.includes(search.value) ||
    c.libelle.toLowerCase().includes(search.value.toLowerCase()),
  ),
)

onMounted(fetchComptes)
</script>

<template>
  <div class="p-4">
    <NCard>
      <div class="flex items-center mb-4">
        <NH1>Plan comptable</NH1>
      </div>
      <NInput v-model:value="search" placeholder="Recherche code ou libellé..." class="mb-4" />
      <NSpin :show="loading">
        <div class="table-responsive">
          <NDataTable :columns="columns" :data="filteredComptes" size="small" />
        </div>
      </NSpin>
    </NCard>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
  align-items: center;
}
.table-responsive {
  width: 100%;
  overflow-x: auto;
}
.code-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 120px;
}
.description-cell {
  white-space: pre-line;
  word-break: break-word;
  max-width: 320px;
}
@media (max-width: 768px) {
  .n-data-table {
    font-size: 13px;
  }
}
</style> 