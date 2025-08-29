import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const userLoginUserStore = defineStore('userLoginUserStore', () => {
  const loginUser = ref<any>({
    userName: '未登录',
  })

  const count = ref<number>(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }

  const setLoginUser = async (target: any) => {}

  const fetchLoginUser = async () => {
    // 后端接口为空，先保留方法
    setTimeout(() => {
      loginUser.value = {
        userName: '张三',
        id: 123,
      }
    }, 3000)
  }

  return { count, doubleCount, loginUser, increment, setLoginUser, fetchLoginUser }
})
