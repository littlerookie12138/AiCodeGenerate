<template>
  <BasicLayout></BasicLayout>
</template>

<script lang="ts" setup>
import { QuestionCircleOutlined } from '@ant-design/icons-vue'
import BasicLayout from '@/layouts/BasicLayout.vue'
import { hello } from '@/api/helloController.ts'
import { userLoginUserStore } from './stores/userLoginUserStore'

const loginUserStore = userLoginUserStore()

hello()
  .then((res: any) => {
    console.log('hello', res)
  })
  .catch((err: any) => {
    console.error('hello error', err)
  })
</script>

<style scoped></style>
