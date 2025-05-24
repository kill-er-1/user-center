import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/user/login',
      name: 'login',
      component: HomeView,
    },
    {
      path: '/user/register',
      name: 'register',
      component: HomeView,
    },
    {
      path: '/admin/userManage',
      name: 'userManage',
      component: HomeView,
    },
  ],
})

export default router
