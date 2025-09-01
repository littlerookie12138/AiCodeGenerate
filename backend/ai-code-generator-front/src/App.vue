<template>
  <BasicLayout></BasicLayout>
</template>

<script lang="ts" setup>
import { QuestionCircleOutlined } from '@ant-design/icons-vue'
import BasicLayout from '@/layouts/BasicLayout.vue'
import { hello } from '@/api/helloController.ts'
import { userLoginUserStore } from './stores/userLoginUserStore'
import { onMounted } from 'vue'

const loginUserStore = userLoginUserStore()
onMounted(() => {
  loginUserStore.fetchLoginUser()
})
</script>

<style scoped></style>
