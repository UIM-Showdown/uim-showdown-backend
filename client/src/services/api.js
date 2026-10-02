/**
 * @typedef {Object} MVP
 * @property {String} rsn
 * @property {Number} tileContribution
 */

/**
 * @typedef {Object} TilePoints
 * @property {Number} total
 * @property {Number} tileCompletion
 * @property {Number} rowBonuses
 * @property {Number} recordsChallenges
 * @property {Number} collectionLog
 */

/**
 * @typedef {Object} Team
 * @property {String} abbreviation
 * @property {String} name
 * @property {Array<String>} captains
 * @property {MVP} mvp
 * @property {TilePoints} tilePoints
 */

/**
 * @todo The data is currently mocked, a data endpoint will need to be implemented
 * @returns {Promise<Array<Team>>}
 */
function getTeamStandings() {
  return Promise.resolve([
    {
      abbreviation: 'GSG',
      name: 'Gwenith Sugar Gliders',
      captains: ['Chxwy', 'finance king'],
      mvp: {
        rsn: 'Chxwy',
        tileContribution: 154.13
      },
      tilePoints: {
        total: 2218,
        tileCompletion: 1350,
        rowBonuses: 540,
        recordsChallenges: 92,
        collectionLog: 236
      }
    },
    {
      abbreviation: 'CRC',
      name: 'Chichilihui Rosé Chuggers',
      captains: ['RinkoLover', 'llerblore'],
      mvp: {
        rsn: 'Lemoniuim',
        tileContribution: 246.29
      },
      tilePoints: {
        total: 2043,
        tileCompletion: 1260,
        rowBonuses: 480,
        recordsChallenges: 105,
        collectionLog: 198
      }
    },
    {
      abbreviation: 'GULL',
      name: "Gillie's Unbelievably Large Livestock",
      captains: ['Pink Mareep', 'what switchs'],
      mvp: {
        rsn: 'what switchs',
        tileContribution: 143.24
      },
      tilePoints: {
        total: 2020,
        tileCompletion: 1240,
        rowBonuses: 490,
        recordsChallenges: 86,
        collectionLog: 204
      }
    },
    {
      abbreviation: 'RATS',
      name: 'Da Varrock Sewer Rats',
      captains: ['Kochininako', 'Pathardo'],
      mvp: {
        rsn: 'azula',
        tileContribution: 154.11
      },
      tilePoints: {
        total: 1750,
        tileCompletion: 1076,
        rowBonuses: 408,
        recordsChallenges: 100,
        collectionLog: 166
      }
    },
    {
      abbreviation: 'GOOGLE',
      name: 'Guild of Overworked Goblin Lamplighters & Electricians',
      captains: ['Luna Lanaa', 'DoctorKebbit'],
      mvp: {
        rsn: 'The Weemler',
        tileContribution: 137.06
      },
      tilePoints: {
        total: 1564,
        tileCompletion: 984,
        rowBonuses: 364,
        recordsChallenges: 74,
        collectionLog: 142
      }
    },
    {
      abbreviation: 'SNORB',
      name: 'Senntisten Noobs Overtly Rigging Bingo',
      captains: ['Zalc CEO', 'Freddo Cake'],
      mvp: {
        rsn: 'Tiwi',
        tileContribution: 82.58
      },
      tilePoints: {
        total: 1300,
        tileCompletion: 786,
        rowBonuses: 260,
        recordsChallenges: 92,
        collectionLog: 162
      }
    },
    {
      abbreviation: 'RAID',
      name: 'Rellekkian Army: Invasion Division',
      captains: ['Bag Account', 'FACESlTTER'],
      mvp: {
        rsn: 'RxViagraPill',
        tileContribution: 83.69
      },
      tilePoints: {
        total: 1053,
        tileCompletion: 644,
        rowBonuses: 138,
        recordsChallenges: 146,
        collectionLog: 125
      }
    },
  ]);
}

export { getTeamStandings };
