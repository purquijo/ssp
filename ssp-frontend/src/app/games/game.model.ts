export interface GameRequest {
  gameType: GameType;
  scoreToReach: number;
}

export interface Game {
  id: number;
  status: GameStatus;
  players: Player[];
  rounds?: GameRound[];
  ongoingRound?: GameRound;
  scoreToReach: number;
  type: GameType;
  startedAt: string;
  endedAt: string | null;
  winnerUsername?: string;
}

export interface GameRound {
  id: number;
  round: number;
  status: GameRoundStatus;
  choices: GameChoice[];
  winner?: Player;
}

export interface Player {
  id: number;
  score: number;
  username: string;
  avatar: ChoiceType;
}

export interface GameChoice {
  id: number;
  type: ChoiceType;
  result: ChoiceResult;
  player: Player;
}

export enum ChoiceResult {
  WIN = 'WIN',
  LOSE = 'LOSE',
  DRAW = 'DRAW',
}

export enum GameRoundStatus {
  ONGOING = 'ONGOING',
  FINISHED = 'FINISHED',
}

export enum GameStatus {
  WAITING_FOR_PLAYERS = 'WAITING_FOR_PLAYERS',
  ONGOING = 'ONGOING',
  FINISHED = 'FINISHED',
  STOPPED = 'STOPPED',
}

export enum GameType {
  TTS_BOT = 'TTS_BOT',
  TTS_RIVAL = 'TTS_RIVAL',
}

export enum ChoiceType {
  STONE = 'STONE',
  SCISSORS = 'SCISSORS',
  PAPER = 'PAPER',
}
