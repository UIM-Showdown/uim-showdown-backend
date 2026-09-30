import { createRouter, createWebHistory } from 'vue-router'

import CompetitionView from '@/pages/competition/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: CompetitionView }
  ],
})

export default router
