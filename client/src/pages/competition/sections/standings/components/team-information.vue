<template>
  <article class="team-information">
    <h2>{{ props.position }}</h2>
    <h3>{{ props.teamName }}</h3>
    <h3>{{ props.teamAbbreviation }}</h3>
    <h2>{{ props.tilePoints }}</h2>
    <div>
      <small>Captains</small>
      <h4>{{ props.captains.at(0) + ' & ' + props.captains.at(1) }}</h4>
    </div>
    <div>
      <small>MVP</small>
      <h4>{{ props.teamMvp }}</h4>
    </div>
  </article>
</template>

<script setup>
const props = defineProps({
  captains: {
    type: Array,
    required: true,
    validator(value) {
      return value.length >= 2 && value.every(captain => typeof captain === 'string');
    }
  },
  position: {
    type: String,
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
    type: Number,
    required: true
  }
});
</script>
