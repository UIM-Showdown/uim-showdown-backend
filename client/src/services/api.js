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
 * @typedef {Object} TileProgress
 * @property {String} abbreviation
 * @property {Number} [points] Will only appear for the current user's team.
 * @property {Number} [progressToNextTier] Will only appear for the current user's team.
 * @property {Number} tier
 */

/**
 * @typedef {Object} Team
 * @property {String} abbreviation
 * @property {String} name
 * @property {Array<String>} captains
 * @property {MVP} mvp
 * @property {TilePoints} tilePoints
 * @property {Array<TileProgress>} tileProgression
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
        tileContribution: 154.13,
      },
      tilePoints: {
        total: 2218,
        tileCompletion: 1350,
        rowBonuses: 540,
        recordsChallenges: 92,
        collectionLog: 236,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 6
        },
        {
          abbreviation: 'LL',
          tier: 6
        },
        {
          abbreviation: '3TM',
          tier: 6
        },
        {
          abbreviation: 'DD',
          tier: 6
        },
        {
          abbreviation: 'TT',
          tier: 6
        },
        {
          abbreviation: 'SS',
          tier: 6
        },
        {
          abbreviation: 'VV',
          tier: 6
        },
        {
          abbreviation: 'FD',
          tier: 6
        },
        {
          abbreviation: '1337',
          tier: 6
        },
        {
          abbreviation: 'CCH',
          tier: 6
        },
        {
          abbreviation: 'ZO',
          tier: 6
        },
        {
          abbreviation: 'MM',
          tier: 6
        },
        {
          abbreviation: 'CCL',
          tier: 6
        },
        {
          abbreviation: 'BP',
          tier: 6
        },
        {
          abbreviation: 'SOS',
          tier: 6
        },
      ],
    },
    {
      abbreviation: 'CRC',
      name: 'Chichilihui Rosé Chuggers',
      captains: ['RinkoLover', 'llerblore'],
      mvp: {
        rsn: 'Lemoniuim',
        tileContribution: 246.29,
      },
      tilePoints: {
        total: 2043,
        tileCompletion: 1260,
        rowBonuses: 480,
        recordsChallenges: 105,
        collectionLog: 198,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 5
        },
        {
          abbreviation: 'LL',
          tier: 5
        },
        {
          abbreviation: '3TM',
          tier: 5
        },
        {
          abbreviation: 'DD',
          tier: 5
        },
        {
          abbreviation: 'TT',
          tier: 5
        },
        {
          abbreviation: 'SS',
          tier: 5
        },
        {
          abbreviation: 'VV',
          tier: 5
        },
        {
          abbreviation: 'FD',
          tier: 5
        },
        {
          abbreviation: '1337',
          tier: 5
        },
        {
          abbreviation: 'CCH',
          tier: 5
        },
        {
          abbreviation: 'ZO',
          tier: 5
        },
        {
          abbreviation: 'MM',
          tier: 5
        },
        {
          abbreviation: 'CCL',
          tier: 10
        },
        {
          abbreviation: 'BP',
          tier: 5
        },
        {
          abbreviation: 'SOS',
          tier: 6
        },
      ],
    },
    {
      abbreviation: 'GULL',
      name: "Gillie's Unbelievably Large Livestock",
      captains: ['Pink Mareep', 'what switchs'],
      mvp: {
        rsn: 'what switchs',
        tileContribution: 143.24,
      },
      tilePoints: {
        total: 2020,
        tileCompletion: 1240,
        rowBonuses: 490,
        recordsChallenges: 86,
        collectionLog: 204,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 6
        },
        {
          abbreviation: 'LL',
          tier: 5
        },
        {
          abbreviation: '3TM',
          tier: 5
        },
        {
          abbreviation: 'DD',
          tier: 5
        },
        {
          abbreviation: 'TT',
          tier: 5
        },
        {
          abbreviation: 'SS',
          tier: 5
        },
        {
          abbreviation: 'VV',
          tier: 5
        },
        {
          abbreviation: 'FD',
          tier: 5
        },
        {
          abbreviation: '1337',
          tier: 5
        },
        {
          abbreviation: 'CCH',
          tier: 5
        },
        {
          abbreviation: 'ZO',
          tier: 5
        },
        {
          abbreviation: 'MM',
          tier: 5
        },
        {
          abbreviation: 'CCL',
          tier: 6
        },
        {
          abbreviation: 'BP',
          tier: 6
        },
        {
          abbreviation: 'SOS',
          tier: 6
        },
      ],
    },
    {
      abbreviation: 'RATS',
      name: 'Da Varrock Sewer Rats',
      captains: ['Kochininako', 'Pathardo'],
      mvp: {
        rsn: 'azula',
        tileContribution: 154.11,
      },
      tilePoints: {
        total: 1750,
        tileCompletion: 1076,
        rowBonuses: 408,
        recordsChallenges: 100,
        collectionLog: 166,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 4
        },
        {
          abbreviation: 'LL',
          tier: 4
        },
        {
          abbreviation: '3TM',
          tier: 4
        },
        {
          abbreviation: 'DD',
          tier: 4
        },
        {
          abbreviation: 'TT',
          tier: 7
        },
        {
          abbreviation: 'SS',
          tier: 4
        },
        {
          abbreviation: 'VV',
          tier: 5
        },
        {
          abbreviation: 'FD',
          tier: 4
        },
        {
          abbreviation: '1337',
          tier: 4
        },
        {
          abbreviation: 'CCH',
          tier: 4
        },
        {
          abbreviation: 'ZO',
          tier: 5
        },
        {
          abbreviation: 'MM',
          tier: 4
        },
        {
          abbreviation: 'CCL',
          tier: 4
        },
        {
          abbreviation: 'BP',
          tier: 4
        },
        {
          abbreviation: 'SOS',
          tier: 4
        },
      ],
    },
    {
      abbreviation: 'GOOGLE',
      name: 'Guild of Overworked Goblin Lamplighters & Electricians',
      captains: ['Luna Lanaa', 'DoctorKebbit'],
      mvp: {
        rsn: 'The Weemler',
        tileContribution: 137.06,
      },
      tilePoints: {
        total: 1564,
        tileCompletion: 984,
        rowBonuses: 364,
        recordsChallenges: 74,
        collectionLog: 142,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 3
        },
        {
          abbreviation: 'LL',
          tier: 4
        },
        {
          abbreviation: '3TM',
          tier: 3
        },
        {
          abbreviation: 'DD',
          tier: 5
        },
        {
          abbreviation: 'TT',
          tier: 4
        },
        {
          abbreviation: 'SS',
          tier: 4
        },
        {
          abbreviation: 'VV',
          tier: 4
        },
        {
          abbreviation: 'FD',
          tier: 3
        },
        {
          abbreviation: '1337',
          tier: 3
        },
        {
          abbreviation: 'CCH',
          tier: 3
        },
        {
          abbreviation: 'ZO',
          tier: 4
        },
        {
          abbreviation: 'MM',
          tier: 3
        },
        {
          abbreviation: 'CCL',
          tier: 5
        },
        {
          abbreviation: 'BP',
          tier: 5
        },
        {
          abbreviation: 'SOS',
          tier: 5
        },
      ],
    },
    {
      abbreviation: 'SNORB',
      name: 'Senntisten Noobs Overtly Rigging Bingo',
      captains: ['Zalc CEO', 'Freddo Cake'],
      mvp: {
        rsn: 'Tiwi',
        tileContribution: 82.58,
      },
      tilePoints: {
        total: 1300,
        tileCompletion: 786,
        rowBonuses: 260,
        recordsChallenges: 92,
        collectionLog: 162,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 2
        },
        {
          abbreviation: 'LL',
          tier: 2
        },
        {
          abbreviation: '3TM',
          tier: 4
        },
        {
          abbreviation: 'DD',
          tier: 3
        },
        {
          abbreviation: 'TT',
          tier: 3
        },
        {
          abbreviation: 'SS',
          tier: 2
        },
        {
          abbreviation: 'VV',
          tier: 4
        },
        {
          abbreviation: 'FD',
          tier: 4
        },
        {
          abbreviation: '1337',
          tier: 3
        },
        {
          abbreviation: 'CCH',
          tier: 2
        },
        {
          abbreviation: 'ZO',
          tier: 2
        },
        {
          abbreviation: 'MM',
          tier: 3
        },
        {
          abbreviation: 'CCL',
          tier: 3
        },
        {
          abbreviation: 'BP',
          tier: 3
        },
        {
          abbreviation: 'SOS',
          tier: 4
        },
      ],
    },
    {
      abbreviation: 'RAID',
      name: 'Rellekkian Army: Invasion Division',
      captains: ['Bag Account', 'FACESlTTER'],
      mvp: {
        rsn: 'RxViagraPill',
        tileContribution: 83.69,
      },
      tilePoints: {
        total: 1053,
        tileCompletion: 644,
        rowBonuses: 138,
        recordsChallenges: 146,
        collectionLog: 125,
      },
      tileProgression: [
        {
          abbreviation: 'BB',
          tier: 1
        },
        {
          abbreviation: 'LL',
          tier: 1
        },
        {
          abbreviation: '3TM',
          tier: 2
        },
        {
          abbreviation: 'DD',
          tier: 1
        },
        {
          abbreviation: 'TT',
          tier: 1
        },
        {
          abbreviation: 'SS',
          tier: 8
        },
        {
          abbreviation: 'VV',
          tier: 1
        },
        {
          abbreviation: 'FD',
          tier: 1
        },
        {
          abbreviation: '1337',
          tier: 1
        },
        {
          abbreviation: 'CCH',
          tier: 1
        },
        {
          abbreviation: 'ZO',
          tier: 1
        },
        {
          abbreviation: 'MM',
          tier: 8
        },
        {
          abbreviation: 'CCL',
          tier: 2
        },
        {
          abbreviation: 'BP',
          tier: 10
        },
        {
          abbreviation: 'SOS',
          tier: 2
        },
      ],
    },
  ]);
}

const CONTRIBUTION_METHOD_CATEGORIES = Object.freeze({
  OTHER: 'Other',
  PVM: 'PVM',
  SKILLING: 'Skilling',
})

const CONTRIBUTION_METHOD_TYPES = Object.freeze({
  SUBMISSION_ITEM_DROP: 'Submission_Item_Drop',
  SUBMISSION_KC: 'Submission_KC',
  SUBMISSION_OTHER: 'Submission_Other',
  TEMPLE_KC: 'Temple_KC',
})

/**
 * @template T
 * @typedef {T[keyof T]} Enum<T>
 */

/** @typedef {Enum<CONTRIBUTION_METHOD_CATEGORIES>} ContributionMethodCategory */
/** @typedef {Enum<CONTRIBUTION_METHOD_TYPES>} ContributionMethodType */

/**
 * @typedef {Object} ContributionMethod
 * @property {String} name
 * @property {ContributionMethodCategory} category
 * @property {Number} ehtRate
 * @property {ContributionMethodType} type
 * @property {Number} [diminishedMultiplier]
 * @property {Number} [diminishedThreshold]
 */

/**
 * @typedef {Object} Tile
 * @property {String} abbreviation
 * @property {String} name
 * @property {Number} pointsPerTier
 * @property {Array<ContributionMethod>} contributionMethods
 */

/**
 * @todo The data is currently mocked, a data endpoint will need to be implemented
 * @returns {Promise<Array<Tile>>}
 */
function getTiles() {
  return Promise.resolve([
    {
      name: 'Big Bad Buddies',
      abbreviation: 'BBB',
      contributionMethods: [
        {
          name: 'Corporeal Beast',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 7.5,
        },
        {
          name: 'Vardorvis',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 33,
        },
        {
          name: 'Zulrah',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 36,
        },
        {
          name: 'General Graardor',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 30,
        },
        {
          name: "Kree'Arra",
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 25,
        },
        {
          name: 'Yama',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 16,
        },
        {
          name: 'Phantom Muspah',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 25,
        },
      ],
    },
    {
      name: 'Lords and Lackeys',
      abbreviation: 'LL',
      contributionMethods: [
        {
          name: 'Dagannoth Rex',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 85,
        },
        {
          name: 'Dagannoth Supreme',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 85,
        },
        {
          name: 'Dagannoth Prime',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 85,
        },
        {
          name: 'Lizardman Shamans',
          type: 'Submission_KC',
          category: 'PVM',
          ehtRate: 150,
        },
        {
          name: 'Scurrius: Spines',
          type: 'Submission_Item_Drop',
          category: 'PVM',
          ehtRate: 1.8,
        },
        {
          name: 'Medium Clues',
          type: 'Temple_KC',
          category: 'Other',
          ehtRate: 20,
        },
        {
          name: 'Kraken',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 72,
        },
        {
          name: "MTA: Alchemist's Playground",
          type: 'Submission_Other',
          category: 'Other',
          ehtRate: 340,
        },
        {
          name: 'MTA: Creature Graveyard',
          type: 'Submission_Other',
          category: 'Other',
          ehtRate: 1080,
        },
        {
          name: 'MTA: Enchanting Chamber',
          type: 'Submission_Other',
          category: 'Other',
          ehtRate: 15000,
        },
        {
          name: 'MTA: Telekinetic Theatre',
          type: 'Submission_Other',
          category: 'Other',
          ehtRate: 285,
        },
        {
          name: 'TzTok-Jad',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 2,
        },
      ],
    },
    {
      name: '3-Tick Mistakes',
      abbreviation: '3TM',
      contributionMethods: [
        {
          name: 'Herblore',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 600000,
        },
        {
          name: 'Mixology: Resin',
          type: 'Submission_Other',
          category: 'Skilling',
          ehtRate: 12760,
        },
        {
          name: 'Woodcutting',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 220000,
        },
        {
          name: 'Hunter',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 240000,
        },
      ],
    },
    {
      name: 'Dungeons and a Dragon',
      abbreviation: 'DD',
      contributionMethods: [
        {
          name: 'The Nightmare',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 11,
        },
        {
          name: "Phosani's Nightmare",
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 8.5,
        },
        {
          name: 'Nex: Nihil Shards',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 33.33,
        },
        {
          name: 'The Whisperer',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 20,
        },
        {
          name: 'The Corrupted Gauntlet',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 7.2,
        },
        {
          name: 'Vorkath',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 28,
        },
        {
          name: 'Commander Zilyana',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 29,
        },
      ],
    },
    {
      name: 'Toiling for Tephra',
      abbreviation: 'TT',
      contributionMethods: [
        {
          name: 'Mining',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 125000,
        },
        {
          name: 'Smithing',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 330000,
        },
        {
          name: 'Runecraft',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 80000,
        },
      ],
    },
    {
      name: 'Scribbled Scriptures',
      abbreviation: 'SS',
      contributionMethods: [
        {
          name: 'Chambers of Xeric',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 3,
        },
        {
          name: 'CoX: CM',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 2.5,
        },
        {
          name: 'Master Clues',
          type: 'Temple_KC',
          category: 'Other',
          ehtRate: 4,
        },
        {
          name: 'Alchemical Hydra',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 24,
        },
      ],
    },
    {
      name: 'Vampyre Variety',
      abbreviation: 'VV',
      contributionMethods: [
        {
          name: 'Agility',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 100000,
        },
        {
          name: 'Thieving',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 300000,
        },
        {
          name: 'Prayer',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 1000000,
        },
      ],
    },
    {
      name: 'Frozen and Demonic',
      abbreviation: 'FD',
      contributionMethods: [
        {
          name: 'Cerberus',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 44,
        },
        {
          name: 'Duke Sucellus',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 35,
        },
        {
          name: 'Abyssal Sire',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 35,
        },
        {
          name: "K'ril Tsutsaroth",
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 28,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 1',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 941.2,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 2',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 470.6,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 3',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 313.8,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 4',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 235.2,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 5',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 94.2,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 6',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 47,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 7',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 37.6,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 8',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 26.8,
        },
        {
          name: 'Doom of Mokhaiotl - Delve Level 8+',
          type: 'Submission_Other',
          category: 'PVM',
          ehtRate: 26.8,
        },
      ],
    },
    {
      name: '1337 Content',
      abbreviation: '1337',
      contributionMethods: [
        {
          name: 'Barbarian Assault Points',
          type: 'Submission_Other',
          category: 'Other',
          ehtRate: 1600,
        },
        {
          name: 'Elite Clues',
          type: 'Temple_KC',
          category: 'Other',
          ehtRate: 7,
        },
        {
          name: 'Barrows: Items',
          type: 'Submission_Item_Drop',
          category: 'PVM',
          ehtRate: 1.3,
        },
        {
          name: 'Artio',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 32,
        },
        {
          name: 'Amoxliatl',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 65,
        },
        {
          name: 'King Black Dragon',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 55,
        },
        {
          name: 'Shellbane Gryphon',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 77,
        },
      ],
    },
    {
      name: 'Chasing Chores',
      abbreviation: 'CCH',
      contributionMethods: [
        {
          name: 'Hueycoatl Hides (3x)',
          type: 'Submission_Item_Drop',
          category: 'PVM',
          ehtRate: 0.33,
        },
        {
          name: 'Hard Clues',
          type: 'Temple_KC',
          category: 'Other',
          ehtRate: 10,
        },
        {
          name: 'Sarachnis',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 65,
        },
        {
          name: 'The Royal Titans',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 55,
        },
        {
          name: 'Spindel',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 30,
        },
        {
          name: 'Tormented Demons',
          type: 'Submission_KC',
          category: 'PVM',
          ehtRate: 65,
        },
        {
          name: 'Demonic Gorillas',
          type: 'Submission_KC',
          category: 'PVM',
          ehtRate: 85,
        },
      ],
    },
    {
      name: 'Zeah Only',
      abbreviation: 'ZO',
      contributionMethods: [
        {
          name: 'Firemaking',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 440000,
        },
        {
          name: 'Farming Exp',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 400000,
        },
        {
          name: 'Farming Contracts',
          type: 'Submission_Other',
          category: 'Skilling',
          ehtRate: 24,
        },
        {
          name: 'Tithe Farm Points',
          type: 'Submission_Other',
          category: 'Skilling',
          ehtRate: 154,
        },
        {
          name: 'Cooking',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 400000,
        },
      ],
    },
    {
      name: 'Morytania Menagerie',
      abbreviation: 'MM',
      contributionMethods: [
        {
          name: 'Grotesque Guardians',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 29,
        },
        {
          name: 'Theatre of Blood',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 2.9,
        },
        {
          name: 'ToB: Hard Mode',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 2.6,
        },
        {
          name: 'Araxxor',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 32,
        },
        {
          name: 'The Leviathan',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 23,
        },
      ],
    },
    {
      name: 'Cavern Collective',
      abbreviation: 'CCL',
      contributionMethods: [
        {
          name: 'Moons of Peril: Items',
          type: 'Submission_Item_Drop',
          category: 'PVM',
          ehtRate: 0.95,
        },
        {
          name: 'Easy Clues',
          type: 'Temple_KC',
          category: 'Other',
          ehtRate: 37,
        },
        {
          name: "Calvar'ion",
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 32,
        },
        {
          name: 'Thermonuclear Smoke Devil',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 80,
        },
        {
          name: 'Giant Mole',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 85,
        },
        {
          name: 'Pest Control: Games',
          type: 'Submission_Other',
          category: 'Other',
          ehtRate: 30,
        },
        {
          name: 'Deranged Archaeologist',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 85,
        },
      ],
    },
    {
      name: 'Beachfront Property',
      abbreviation: 'BP',
      contributionMethods: [
        {
          name: 'Tombs of Amascut',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 3.8,
        },
        {
          name: 'ToA: Expert',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 3,
        },
        {
          name: 'ToA: Purples',
          type: 'Submission_Item_Drop',
          category: 'PVM',
          ehtRate: 0.3,
          diminishedThreshold: 5,
          diminishedMultiplier: 0.5,
        },
        {
          name: 'Sol Heredit',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 2,
        },
        {
          name: 'Kalphite Queen',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 33,
        },
        {
          name: 'TzKal-Zuk',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 0.8,
        },
        {
          name: 'Maggot King',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 24,
        },
      ],
    },
    {
      name: 'Shipwright or Shipwreck',
      abbreviation: 'SOS',
      contributionMethods: [
        {
          name: 'Fishing',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 137500,
        },
        {
          name: 'Tempoross',
          type: 'Temple_KC',
          category: 'PVM',
          ehtRate: 22,
        },
        {
          name: 'Sailing',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 245000,
        },
        {
          name: 'Construction',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 247500,
        },
        {
          name: 'Crafting',
          type: 'Temple_XP',
          category: 'Skilling',
          ehtRate: 181500,
        },
      ],
    },
  ]);
}

/**
 * @typedef {Object} TileGroup
 * @property {String} name
 * @property {Array<String>} tileNames
 */

/**
 * @todo The data is currently mocked, a data endpoint will need to be implemented
 * @returns {Promise<Array<TileGroup>>}
 */
function getTileGroups() {
  return Promise.resolve([
    {
      name: 'Yellow Group',
      tileNames: ['Big Bad Buddies', 'Lords and Lackeys', '3-Tick Mistakes'],
    },
    {
      name: 'Blue Group',
      tileNames: ['Dungeons and a Dragon', 'Toiling for Tephra', 'Scribbled Scriptures'],
    },
    {
      name: 'Purple Group',
      tileNames: ['Vampyre Variety', 'Frozen and Demonic', '1337 Content'],
    },
    {
      name: 'Green Group',
      tileNames: ['Chasing Chores', 'Zeah Only', 'Morytania Menagerie'],
    },
    {
      name: 'Red Group',
      tileNames: ['Cavern Collective', 'Beachfront Property', 'Shipwright or Shipwreck'],
    },
  ]);
}

export { getTeamStandings, getTiles, getTileGroups }
