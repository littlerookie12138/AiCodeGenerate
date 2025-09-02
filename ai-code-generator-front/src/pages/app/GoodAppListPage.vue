<template>
  <div id="goodAppListPage">
    <a-card title="精选应用">
      <a-space style="margin-bottom: 12px">
        <a-input
          v-model:value="searchName"
          placeholder="按名称搜索"
          allow-clear
          style="width: 240px"
        />
        <a-button type="primary" @click="doSearch">搜索</a-button>
      </a-space>
      <a-table
        :data-source="list"
        :columns="columns"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="onTableChange"
      />
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { listGoodAppVoByPage } from '@/api/appController'

const searchName = ref<string>('')
const loading = ref<boolean>(false)
const list = ref<API.AppVO[]>([])

const pagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
  showTotal: (t: number) => `共 ${t} 条`,
})

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: '名称', dataIndex: 'appName', key: 'appName' },
  { title: '优先级', dataIndex: 'priority', key: 'priority', width: 100 },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 180 },
]

const fetchList = async () => {
  loading.value = true
  const res = await listGoodAppVoByPage({
    pageNum: pagination.current,
    pageSize: pagination.pageSize,
    appName: searchName.value || undefined,
  })
  if (res.data.code === 0 && res.data.data) {
    list.value = res.data.data.records || []
    pagination.total = Number(res.data.data.totalRow || 0)
  }
  loading.value = false
}

const onTableChange = (p: any) => {
  pagination.current = p.current
  pagination.pageSize = p.pageSize
  fetchList()
}

const doSearch = () => {
  pagination.current = 1
  fetchList()
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped></style>
