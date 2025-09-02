import { createApp } from 'vue'
import { createPinia } from 'pinia'

import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import { message } from 'ant-design-vue'
import { ref } from 'vue'
import dayjs from 'dayjs'

import App from './App.vue'
import router from './router'
import '@/access/index.ts'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.config.globalProperties.$message = message
app.config.globalProperties.$ref = ref
app.config.globalProperties.$dayjs = dayjs
app.use(Antd).mount('#app')
