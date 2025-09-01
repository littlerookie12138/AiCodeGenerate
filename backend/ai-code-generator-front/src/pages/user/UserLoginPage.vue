<template>
  <div id="userLoginPage">
    <h2 class="loginPageTitle">zwy AI 应用生成 - 用户登录</h2>
    <div class="desc">不写一行代码生成完整应用</div>
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <a-form-item
        label="用户账号"
        name="userAccount"
        :rules="[{ required: true, message: '请输入账号!' }]"
      >
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <a-form-item
        label="密码"
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码!' },
          { min: 8, message: '密码不能少于8位' },
          { max: 20, message: '密码不能多于20位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>

      <div class="tips">
        <router-link to="/user/register">没有账号？点击注册</router-link>
      </div>

      <div class="submit">
        <a-form-item>
          <a-button type="primary" html-type="submit">登录</a-button>
        </a-form-item>
      </div>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { login } from '@/api/userController'
import router from '@/router'
import { userLoginUserStore } from '@/stores/userLoginUserStore'
import { message } from 'ant-design-vue'
import { reactive } from 'vue'

const loginUserStore = userLoginUserStore()

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const handleSubmit = async (values: any) => {
  var res = await login(values)

  // 如果登录成功
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.replace({ path: '/', replace: true })
  } else {
    message.error('登录失败' + res.data.message)
  }
}
</script>

<style scoped>
#userLoginPage {
  max-width: 480px;
  margin: 0px auto;
  display: absolute;
}

.loginPageTitle {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  margin-bottom: 12px;
  color: #888;
}

.tips {
  margin-bottom: 12px;
  text-align: right;
  color: #bbb;
  font-size: 13px;
}

.submit {
  justify-content: center;
  display: flex;
  width: 100%;
}
</style>
