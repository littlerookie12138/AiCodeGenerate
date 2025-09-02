<template>
  <div id="userRegisterPage">
    <h2 class="registerPageTitle">zwy AI 应用生成 - 用户注册</h2>
    <div class="desc">注册后即可体验智能应用生成</div>
    <a-form
      :model="formState"
      name="register"
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
        label="用户名"
        name="userName"
        :rules="[
          { required: true, message: '请输入用户名!' },
          { max: 20, message: '用户名不能超过20个字符' }
        ]"
      >
        <a-input v-model:value="formState.userName" placeholder="请输入用户名" />
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

      <a-form-item
        label="确认密码"
        name="checkPassword"
        :rules="[{ required: true, message: '请确认密码!' }, { validator: validateCheckPassword }]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请再次输入密码" />
      </a-form-item>

      <div class="tips">
        <router-link to="/user/login">已有账号？点击登录</router-link>
      </div>

      <div class="submit">
        <a-form-item>
          <a-button type="primary" html-type="submit">注册</a-button>
        </a-form-item>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { register } from '@/api/userController'
import router from '@/router'
import { message } from 'ant-design-vue'
import { reactive } from 'vue'

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  userName: '',
})

const validateCheckPassword = (_: any, value: string) => {
  if (value !== formState.userPassword) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const handleSubmit = async (values: any) => {
  const res = await register(values)
  if (res.data.code === 0) {
    message.success('注册成功，请登录')
    router.replace({ path: '/user/login', replace: true })
  } else {
    message.error('注册失败：' + res.data.message)
  }
}
</script>

<style scoped>
#userRegisterPage {
  max-width: 480px;
  margin: 0px auto;
  display: absolute;
}

.registerPageTitle {
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
