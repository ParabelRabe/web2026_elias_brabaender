import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/LoginView.vue'
import List from '@/views/AnzeigeListeView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: Login },
    {path: '/anzeige', component: List },
    { path: '/:pathMatch(.*)*', redirect: '/anzeige' }
  ],
})

export default router
