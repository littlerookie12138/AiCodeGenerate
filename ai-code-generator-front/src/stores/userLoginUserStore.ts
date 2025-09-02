import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { getLoginUser, login } from '@/api/userController.ts'

export const userLoginUserStore = defineStore('loginUser', () => {
  // 默认值
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  const count = ref<number>(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }

  const fetchLoginUser = async () => {
    // 后端接口为空，先保留方法
    const res = await getLoginUser()

    if (res.data.code === 0 && res.data.data) {
      setCurrentUser(res.data.data)
    }
  }

  const setCurrentUser = (user: API.LoginUserVO) => {
    loginUser.value = user
  }

  return { loginUser, increment, setCurrentUser, fetchLoginUser }
})
