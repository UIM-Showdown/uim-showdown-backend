/**
 * @typedef {Object} Player
 * @property {String} rsn
 * @property {Number} tileContribution
 */

/**
 * @typedef {Object} Team
 * @property {String} abbreviation
 * @property {String} name
 * @property {Array<String>} captains
 * @property {Array<Player>} players
 * @property {Number} tilePoints
 */

/**
 * @typedef {Array<Team>} Standings
 */

/**
 * @todo The data is currently mocked, a data endpoint will need to be implemented
 * @returns Promise<Standings>
 */
function getTeamStandings() {
  return Promise.resolve([
    {
      abbreviation: 'GSG',
      name: 'Gwenith Sugar Gliders',
      captains: ['Chxwy', 'finance king'],
      players: [
        { rsn: 'Chxwy', tileContribution: 154.13 }
      ],
      tilePoints: 2218
    },
    {
      abbreviation: 'CRC',
      name: 'Chichilihui Rosé Chuggers',
      captains: ['RinkoLover', 'llerblore'],
      players: [
        { rsn: 'Lemoniuim', tileContribution: 246.29 }
      ],
      tilePoints: 2043
    },
    {
      abbreviation: 'GULL',
      name: "Gillie's Unbelievably Large Livestock",
      captains: ['Pink Mareep', 'what switchs'],
      players: [
        { rsn: 'what switchs', tileContribution: 143.24 }
      ],
      tilePoints: 2020
    },
    {
      abbreviation: 'RATS',
      name: 'Da Varrock Sewer Rats',
      captains: ['Kochininako', 'Pathardo'],
      players: [
        { rsn: 'azula', tileContribution: 154.11 }
      ],
      tilePoints: 1750
    },
    {
      abbreviation: 'GOOGLE',
      name: 'Guild of Overworked Goblin Lamplighters & Electricians',
      captains: ['Luna Lanaa', 'DoctorKebbit'],
      players: [
        { rsn: 'The Weemler', tileContribution: 137.06 }
      ],
      tilePoints: 1564
    },
    {
      abbreviation: 'SNORB',
      name: 'Senntisten Noobs Overtly Rigging Bingo',
      captains: ['Zalc CEO', 'Freddo Cake'],
      players: [
        { rsn: 'Tiwi', tileContribution: 82.58 }
      ],
      tilePoints: 1300
    },
    {
      abbreviation: 'RAID',
      name: 'Rellekkian Army: Invasion Division',
      captains: ['Bag Account', 'FACESlTTER'],
      players: [
        { rsn: 'RxViagraPill', tileContribution: 83.69 }
      ],
      tilePoints: 1053
    },
  ]);
}

export { getTeamStandings };
