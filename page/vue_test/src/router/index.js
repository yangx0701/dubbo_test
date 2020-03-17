import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    meta: {
      requireAuth: false
    },
    component: () => import('../views/Index.vue')
  },
  {
    path: '/mine',
    name: 'mine',
    meta: {
      requireAuth: true
    },
    component: () => import('../views/Mine.vue')
  }
]

const router = new VueRouter({
  routes
})
router.beforeEach((from, to, next) => {
  if (from.path === '/') {
    next()
  } else if ((localStorage.getItem('token') === null) & (from.meta.requireAuth == true)) {
    next({
      path: '/'
    })
  } else {
    let Base64 = require('js-base64').Base64
    let token = JSON.parse(Base64.decode(localStorage.getItem('token')))
    if (to.meta.requireAuth) {
      if (token.rem === 'true') {
        next()
        return
      }
      if (parseInt(token.exp) > new Date().getTime()) {
        next()
      } else {
        next({
          path: '/'
        })
      }
    } else {
      next()
    }
  }
})

export default router
