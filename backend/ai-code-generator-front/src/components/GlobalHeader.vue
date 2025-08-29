<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="300px">
        <RouterLink to="/" class="logoLink">
          <div class="titleBar">
            <img src="../assets/logo.png" alt="logo" class="logo" />
            <div class="title">AI Code Generator</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="clickMenu"
        />
      </a-col>
      <a-col flex="120px">
        <div class="user-login-button">
          <div v-if="loginUserStore.loginUser.id">
            {{ loginUserStore.loginUser.username }}
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
      <a-col>
        <div class="user-register-button">
          <a-button style="margin-left: 8px">注册</a-button>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { h, ref, warn } from 'vue'
import { AppstoreOutlined, TwitterOutlined } from '@ant-design/icons-vue'
import { type MenuProps } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { userLoginUserStore } from '@/stores/userLoginUserStore'

const loginUserStore = userLoginUserStore()

const current = ref<string[]>(['/'])
const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(TwitterOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/welcome',
    icon: () => h(AppstoreOutlined),
    label: '欢迎',
    title: '欢迎',
  },
  {
    key: 'alipay',
    label: h('a', { href: 'https://antdv.com', target: '_blank' }, 'zwy'),
    title: 'zwy',
  },
])

loginUserStore.fetchLoginUser()

const router = useRouter()

const clickMenu = ({ key }: any) => {
  router.push({ path: key })
}

router.afterEach((to, from, next) => {
  current.value = [to.path]
})
</script>

<style scoped>
#globalHeader .titleBar {
  display: flex;
  align-items: center;
}

.title {
  font-size: 20px;
  font-weight: bold;
  margin-left: 10px;
}

.logo {
  height: 48px;
}
</style>
