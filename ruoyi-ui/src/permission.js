import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isPathMatch } from '@/utils/validate'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register']

const isWhiteList = (path) => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  
  // 检查是否在白名单中
  if (isWhiteList(to.path)) {
    next()
    NProgress.done()
    return
  }
  
  // 检查是否有token
  const hasToken = getToken()
  
  if (hasToken) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    
    if (to.path === '/login') {
      // 有token但访问登录页，重定向到首页
      next({ path: '/' })
      NProgress.done()
    } else {
      // 有token且访问非登录页
      if (store.getters.roles && store.getters.roles.length > 0) {
        // 已获取用户信息，直接放行
        next()
      } else {
        // 未获取用户信息，先获取
        isRelogin.show = true
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            router.addRoutes(accessRoutes)
            next({ ...to, replace: true })
          })
        }).catch(err => {
          isRelogin.show = false
          store.dispatch('LogOut').then(() => {
            Message.error('登录状态已过期，请重新登录')
            next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
          })
        })
      }
    }
  } else {
    // 无token，重定向到登录页
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done()
})
