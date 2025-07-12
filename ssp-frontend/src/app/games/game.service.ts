import { HttpClient } from '@angular/common/http';
import { environment } from '../../evironments/environment';
import { ChoiceType, Game, GameType } from './game.model';
import { Client, Stomp } from '@stomp/stompjs';
import { inject, Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import SockJS from 'sockjs-client/dist/sockjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class GameService {
  private baseUrl = environment.apiUrl;
  private gameUpdates = new Subject<Game>();
  private stompClient?: Client;
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  connectToGameUpdates(gameId: number) {
    const socket = new SockJS(`${this.baseUrl}/ws-game`);
    this.stompClient = Stomp.over(socket);

    const token = this.authService.getToken();

    this.stompClient.configure({
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      onConnect: () => {
        this.stompClient?.subscribe(`/topic/game/${gameId}`, (message) => {
          const game: Game = JSON.parse(message.body);
          this.gameUpdates.next(game);
        });
      },
    });

    this.stompClient.activate();
  }

  onGameUpdate(): Observable<Game> {
    return this.gameUpdates.asObservable();
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.forceDisconnect();
    }
  }

  startGame(type: GameType, scoreToReach = 3) {
    return this.http.post<Game>(`${this.baseUrl}/game`, { type, scoreToReach });
  }

  joinGame(id: number) {
    return this.http.post<Game>(`${this.baseUrl}/game/${id}/join`, {});
  }

  getGame(id: number) {
    return this.http.get<Game>(`${this.baseUrl}/game/${id}`);
  }

  stopGame(id: number) {
    return this.http.delete<Game>(`${this.baseUrl}/game/${id}`);
  }

  choice(gameId: number, choiceType: ChoiceType) {
    return this.http.post<Game>(`${this.baseUrl}/game/${gameId}/choice/${choiceType}`, {});
  }
}
