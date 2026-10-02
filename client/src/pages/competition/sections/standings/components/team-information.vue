<template>
  <article class="team-information">
    <div class="team-information__summary">
      <div class="team-information__summary__data-point">
        <small>Position</small>
        <h2>{{ props.position }}</h2>
      </div>
      <div class="team-information__summary__data-point">
        <small>Team Name</small>
        <h3>{{ props.teamName }}</h3>
      </div>
      <div class="team-information__summary__data-point">
        <small>TAG</small>
        <h3>{{ props.teamAbbreviation }}</h3>
      </div>
      <div class="team-information__summary__data-point">
        <small>Tile Points</small>
        <h2>{{ props.tilePoints.total }}</h2>
      </div>
      <div class="team-information__summary__data-point">
        <small>Captains</small>
        <h4>{{ props.captains.at(0) + ' & ' + props.captains.at(1) }}</h4>
      </div>
      <div class="team-information__summary__data-point">
        <small>MVP</small>
        <h4>{{ props.teamMvp }}</h4>
      </div>
    </div>
    <table class="team-information__points">
      <tbody>
        <tr>
          <th scope="row"><strong>TILE COMPLETION:</strong></th>
          <td>{{ props.tilePoints.tileCompletion }}</td>
        </tr>
        <tr>
          <th scope="row"><strong>ROW BONUSES:</strong></th>
          <td>{{ props.tilePoints.rowBonuses }}</td>
        </tr>
        <tr>
          <th scope="row"><strong>REC/CHALLENGES:</strong></th>
          <td>{{ props.tilePoints.recordsChallenges }}</td>
        </tr>
        <tr>
          <th scope="row"><strong>COLLECTION LOG:</strong></th>
          <td>{{ props.tilePoints.collectionLog }}</td>
        </tr>
      </tbody>
    </table>
  </article>
</template>

<script setup>
/**
 * @typedef {Object} TeamInformation
 * @property {Array<String>} captains
 * @property {Number} position
 * @property {String} teamAbbreviation
 * @property {String} teamMvp
 * @property {String} teamName
 * @property {import('@/services/api').TilePoints} tilePoints
 */

/** @type {TeamInformation} props */
const props = defineProps({
  captains: {
    type: Array,
    required: true,
    validator(value) {
      return value.length >= 2 && value.every(captain => typeof captain === 'string');
    }
  },
  position: {
    type: Number,
    required: true
  },
  teamAbbreviation: {
    type: String,
    required: true
  },
  teamMvp: {
    type: String,
    required: true
  },
  teamName: {
    type: String,
    required: true
  },
  tilePoints: {
    type: Object,
    required: true
  }
});
</script>
