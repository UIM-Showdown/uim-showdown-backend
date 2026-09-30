import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getTeamStandings } from '@/services/api';

export const useTeamsStore = defineStore('teams', () => {
  const isLoading = ref(false);
  const teams = ref([]);

  async function fetchTeamUpdates() {
    isLoading.value = true;

    try {
      teams.value = await getTeamStandings();
    } catch (error) {
      console.error('Failed to load team standings!', error);
    } finally {
      isLoading.value = false;
    }
  }

  return { teams, isLoading, fetchTeamUpdates }
})
