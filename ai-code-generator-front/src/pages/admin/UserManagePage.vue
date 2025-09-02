<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.dataIndex === 'updateTime'">
          {{ dayjs(record.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts" setup>
import { listUserVoByPage } from '@/api/userController'
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { computed, onMounted, ref } from 'vue'
import dayjs from 'dayjs'
import { deleteUser } from '../../api/userController'

const columns = [
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

const data = ref<API.UserVO[]>([])
const total = ref(0)

const searchParams = ref<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 2,
})

const queryData = async () => {
  const res = await listUserVoByPage({ ...searchParams.value })
  if (res.data.code === 0 && res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('查询失败' + res.data.message)
  }
}

const pagination = computed(() => ({
  current: searchParams.value.pageNum ?? 1,
  pageSize: searchParams.value.pageSize ?? 2,
  total: Number(total.value),
  showTotal: (total: number) => `共 ${total} 条`,
  showSizeChanger: true,
  pageSizeOptions: ['5', '10', '20', '50'],
}))

const doTableChange = (pager: any) => {
  searchParams.value.pageNum = pager.current
  searchParams.value.pageSize = pager.pageSize
  queryData()
}

const doSearch = () => {
  searchParams.value.pageNum = 1
  queryData()
}

const doDelete = async (id: number) => {
  const target: any = data.value.find((item) => item.id === id)
  const delRes = await deleteUser({ id: id })
  if (delRes.data.code !== 0) {
    message.error('删除用户：' + target.userName + '失败，' + delRes.data.message)
    return
  } else {
    message.success('删除用户：' + target.userName + '成功')
  }
  doSearch()
}

onMounted(() => {
  queryData()
})
</script>

<style scoped>
#userManagePage {
  overflow-x: auto;
}
</style>
