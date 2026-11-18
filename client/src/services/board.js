/**
 * @typedef {Object} MiniBoardTile
 * @property {String} id
 * @property {Number} x
 * @property {Number} y
 * @property {Number} [w]
 * @property {Number} [h]
 * @property {Number} value
 */

const MINIBOARD_LAYOUT = Object.freeze({
  'BB': { x: 4, y: 1, w: 2, h: 2, value: 0 },
  'LL': { x: 6, y: 1, w: 2, h: 2, value: 0 },
  'CCL': { x: 2, y: 3, w: 2, h: 2, value: 0 },
  '3TM': { x: 5, y: 3, w: 2, h: 2, value: 0 },
  'DD': { x: 8, y: 3, w: 2, h: 2, value: 0 },
  'BP': { x: 1, y: 5, w: 2, h: 2, value: 0 },
  'SOS': { x: 3, y: 5, w: 2, h: 2, value: 0 },
  'TT': { x: 7, y: 5, w: 2, h: 2, value: 0 },
  'SS': { x: 9, y: 5, w: 2, h: 2, value: 0 },
  'CCH': { x: 2, y: 7, w: 2, h: 2, value: 0 },
  'ZO': { x: 4, y: 7, w: 2, h: 2, value: 0 },
  'VV': { x: 6, y: 7, w: 2, h: 2, value: 0 },
  'FD': { x: 8, y: 7, w: 2, h: 2, value: 0 },
  'MM': { x: 4, y: 9, w: 2, h: 2, value: 0 },
  '1337': { x: 6, y: 9, w: 2, h: 2, value: 0 },
});

/**
 * @typedef {Object} TileColorPalette
 * @property {String} bg Color of background represented by a hex color code
 * @property {String} text Color of text represented by a hex color code
 */

const TILE_COLOR_PALETTE = Object.freeze([
  { bg: '#f0f0f0', text: '#333333' },
  { bg: '#e0f7fa', text: '#006064' },
  { bg: '#b2ebf2', text: '#00838f' },
  { bg: '#c8e6c9', text: '#2e7d32' },
  { bg: '#aed581', text: '#33691e' },
  { bg: '#fff9c4', text: '#f57f17' },
  { bg: '#ffe0b2', text: '#e65100' },
  { bg: '#ffccbc', text: '#bf360c' },
  { bg: '#f8bbd0', text: '#880e4f' },
  { bg: '#e1bee7', text: '#4a148c' },
  { bg: '#d1c4e9', text: '#311b92' },
]);

/**
 *
 * @param {Array<import('@/services/api').TileProgress>} teamTileProgression
 * @returns {Array<MiniBoardTile>}
 * @throws {RangeError} When a given tile progress has an abbreviation that is not found in `MINIBOARD_LAYOUT`
 */
function getTeamMiniBoard(teamTileProgression) {
  return teamTileProgression.map((tileProgess) => {
    if (Object.hasOwn(MINIBOARD_LAYOUT, tileProgess.abbreviation) === false) {
      throw new RangeError(`"${tileProgess.abbreviation}" not found in miniboard layout!`);
    }

    return {
      ...MINIBOARD_LAYOUT[tileProgess.abbreviation],
      id: tileProgess.abbreviation,
      value: tileProgess.tier
    };
  });
}

/**
 * @param {Number} tileTier
 * @returns {TileColorPalette}
 * @throws {TypeError} If given `tileTier` value is not an integer
 */
function getTileColorPalette(tileTier) {
  if (Number.isInteger(tileTier) === false) {
    throw new TypeError("The given value is not an integer!");
  }

  if (tileTier >= TILE_COLOR_PALETTE.length) {
    return TILE_COLOR_PALETTE.at(-1);
  }

  return TILE_COLOR_PALETTE.at(tileTier);
}

export { getTeamMiniBoard, getTileColorPalette };
