<template>
  <main class="standings">
    <aside class="standings__teams">
      <article
        v-if="isLoadingTeams && teams.length === 0"
        aria-busy="true"
      >
      </article>
      <team-information
        v-else
        v-for="(team, index) in orderedTeams"
        :key="index"
        :captains="team.captains"
        :position="index + 1"
        :team-abbreviation="team.abbreviation"
        :team-mvp="team.mvp.rsn || 'N/A'"
        :team-name="team.name"
        :tile-points="team.tilePoints"
      />
    </aside>
  </main>
</template>

<script setup>
import { computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useTeamsStore } from '@/stores/teams';

import teamInformation from './components/team-information.vue';

const teamsStore = useTeamsStore();
const { isLoading: isLoadingTeams, teams } = storeToRefs(teamsStore);

/** @todo The sorting of teams and players will more than likely be done on the backend */
const orderedTeams = computed(() => {
  return teams.value.sort((teamA, teamB) => teamB.tilePoints - teamA.tilePoints);
});
</script>
