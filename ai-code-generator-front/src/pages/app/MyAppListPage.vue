<template>
  <div id="myAppListPage">
    <a-card title="我的应用">
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
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'actions'">
            <a-space>
              <a-button type="link" @click="goEdit(record.id)">编辑</a-button>
              <a-popconfirm title="确定删除该应用？" @confirm="() => doDelete(record.id)">
                <a-button type="link" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { listMyAppVoByPage, deleteApp } from '@/api/appController'
import router from '@/router'

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
  { title: '操作', key: 'actions', width: 180 },
]

const fetchList = async () => {
  loading.value = true
  const res = await listMyAppVoByPage({
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

const goEdit = (id: number) => {
  router.push(`/app/edit/${id}`)
}

const doDelete = async (id: number) => {
  const res = await deleteApp({ id })
  if (res.data.code === 0) {
    fetchList()
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped></style>
