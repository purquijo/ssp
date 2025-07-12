import { ChoiceType } from '../games/game.model';

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  points: number;
  avatar: ChoiceType;
}

export interface User {
  username: string;
  points: number;
  avatar: ChoiceType;
}
