<template>
  <header class="event-banner">
    <h1 class="event-banner__name">🏖️ UIM Summer Showdown 2026 🏄</h1>
  </header>
  <Standings />
  <footer>

  </footer>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue';

import { useTeamsStore } from '@/stores/teams';
import Standings from './sections/standings/index.vue';

const { fetchTeamUpdates } = useTeamsStore();
const timerId = ref(null);

onMounted(() => {
  fetchTeamUpdates();
  timerId.value = setInterval(fetchTeamUpdates, 60000);
});
onBeforeUnmount(() => {
  clearInterval(timerId.value);
})
</script>
