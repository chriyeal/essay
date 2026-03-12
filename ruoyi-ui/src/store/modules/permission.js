import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_DEFAULT_ROUTES: (state, routes) => {
      state.defaultRoutes = constantRoutes.concat(routes)
    },
    SET_TOPBAR_ROUTES: (state, routes) => {
      state.topbarRouters = routes
    },
    SET_SIDEBAR_ROUTERS: (state, routes) => {
      state.sidebarRouters = routes
    },
  },
  actions: {
    // 生成路由
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        // 使用固定的学习计划系统路由
        const studyRoutes = [
          {
            path: '/index',
            component: 'Layout',
            redirect: '/index',
            children: [
              {
                path: 'index',
                component: 'index',
                name: 'Index',
                meta: { title: '首页', icon: 'dashboard' }
              }
            ]
          },
          {
            path: '/study',
            component: 'Layout',
            redirect: '/study/plan',
            name: 'Study',
            meta: { title: '学习管理', icon: 'education' },
            children: [
              {
                path: 'plan',
                component: 'study/plan/index',
                name: 'StudyPlan',
                meta: { title: '学习计划', icon: 'notebook-2', permissions: ['study:plan:list'] }
              },
              {
                path: 'tomato',
                component: 'study/tomato/index',
                name: 'TomatoClock',
                meta: { title: '番茄钟', icon: 'timer', permissions: ['study:tomato:list'] }
              },
              {
                path: 'statistics',
                component: 'study/statistics/index',
                name: 'StudyStatistics',
                meta: { title: '学习统计', icon: 'data-analysis', permissions: ['study:statistics:list'] }
              },
              {
                path: 'profile',
                component: 'study/profile/index',
                name: 'StudyProfile',
                meta: { title: '个人中心', icon: 'user', permissions: ['study:profile:list'] }
              }
            ]
          }
        ];
        
        const sidebarRoutes = filterAsyncRouter(JSON.parse(JSON.stringify(studyRoutes)))
        const rewriteRoutes = filterAsyncRouter(JSON.parse(JSON.stringify(studyRoutes)), false, true)
        const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
        rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
        router.addRoutes(asyncRoutes)
        commit('SET_ROUTES', rewriteRoutes)
        commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
        commit('SET_DEFAULT_ROUTES', sidebarRoutes)
        commit('SET_TOPBAR_ROUTES', sidebarRoutes)
        resolve(rewriteRoutes)
      })
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach(el => {
    el.path = lastRouter ? lastRouter.path + '/' + el.path : el.path
    if (el.children && el.children.length && el.component === 'ParentView') {
      children = children.concat(filterChildren(el.children, el))
    } else {
      children.push(el)
    }
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/${view}`], resolve)
  } else {
    // 使用 import 实现生产环境的路由懒加载
    return () => import(`@/views/${view}`)
  }
}

export default permission
