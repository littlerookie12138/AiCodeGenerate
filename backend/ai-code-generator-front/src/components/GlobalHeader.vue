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
            <a-dropdown>
              <a-space>
                <a-avatar icon="user" :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="logout" @click="doLogout">
                    <LogoutOutlined style="margin-right: 5px !important" />登出
                  </a-menu-item>
                  <a-menu-item
                    @click="doManageUser"
                    v-if="loginUserStore.loginUser.userRole === 'admin'"
                  >
                    <ContactsOutlined style="margin-right: 5px !important" /> 管理用户
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
      <a-col>
        <div class="user-register-button" v-if="!loginUserStore.loginUser.id">
          <a-button style="margin-left: 8px" @click="doRegister">注册</a-button>
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
import { logout } from '@/api/userController'
import { LogoutOutlined, ContactsOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

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

const router = useRouter()

const clickMenu = ({ key }: any) => {
  router.push({ path: key })
}

const doLogout = async () => {
  try {
    const res = await logout()
    if (res.data.code !== 0) {
      message.error('登出失败：' + res.data.message)
      return
    } else {
      message.success('登出成功')
    }
    loginUserStore.setCurrentUser({})
    router.push({ path: '/' })
  } catch (e) {
    warn('登出异常', e)
  }
}

const doManageUser = () => {
  router.push({ path: '/admin/userManage', replace: true })
}

const doRegister = () => {
  console.log('doRegister')
  router.push({ path: '/user/register', replace: true })
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
