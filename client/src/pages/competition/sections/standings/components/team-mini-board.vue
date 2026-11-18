<template>
  <article class="team-information__mini-board" :style="gridStyle">
    <div
      v-for="tile in teamMiniBoard"
      :key="tile.id"
      class="team-information__mini-board__tile"
      :style="getTileStyle(tile)"
    >
      <span class="team-information__mini-board__tile__value">{{ tile.value }}</span>
    </div>
  </article>
</template>

<script setup>
import { getTeamMiniBoard, getTileColorPalette } from '@/services/board';
import { computed } from 'vue';

const props = defineProps({
  columns: {
    type: Number,
    required: true
  },
  rows: {
    type: Number,
    required: true
  },
  height: {
    type: Number,
    required: true
  },
  width: {
    type: Number,
    required: true
  },
  teamTileProgress: {
    type: Array,
    required: true
  },
});

const gridStyle = computed(() => ({
  gridTemplateColumns: `repeat(${props.columns} ,1fr)`,
  gridTemplateRows: `repeat(${props.rows}, 1fr)`,
  height: `${props.height}px`,
  width: `${props.width}px`
}));

/** @param {import('@/services/board').MiniBoardTile} tile */
const getTileStyle = (tile) => {
  let tileColor = getTileColorPalette(tile.value);

  return {
    backgroundColor: tileColor.bg,
    borderColor: tileColor.text,
    color: tileColor.text,
    gridColumnStart: tile.x,
    gridColumnEnd: `span ${tile.w || 1}`,
    gridRowStart: tile.y,
    gridRowEnd: `span ${tile.h || 1}`
  };
};

const teamMiniBoard = computed(() => getTeamMiniBoard(props.teamTileProgress));
</script>
