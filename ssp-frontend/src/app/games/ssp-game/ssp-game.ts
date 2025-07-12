import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { GameService } from '../game.service';
import { ChoiceType, Game, GameRoundStatus, GameStatus, GameType, Player } from '../game.model';
import { AvatarModule } from 'primeng/avatar';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';
import { ChoiceCard, RoundResultInfo } from '../components/choice-card/choice-card';
import { ButtonModule } from 'primeng/button';
import { UserService } from '../../user/user-service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { Subscription, timer, switchMap, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ToastService } from '../../shared/service/toast-service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-ssp-game',
  templateUrl: './ssp-game.html',
  styleUrls: ['./ssp-game.css'],
  imports: [
    AvatarModule,
    DividerModule,
    ToastModule,
    CardModule,
    CommonModule,
    ChoiceCard,
    ButtonModule,
  ],
  providers: [],
})
export class SspGame implements OnInit, OnDestroy {
  choices = Object.values(ChoiceType);

  roundResult?: RoundResultInfo;

  isBotReady = false;
  opponentReady = false;
  isPlayerReady = false;
  isRoundFinished = false;
  isGameFinished = false;

  game?: Game;
  GameStatus = GameStatus;

  choiceType?: ChoiceType;

  showVs = false;
  showChoices = false;

  private gameUpdateSub?: Subscription;
  private routeSub?: Subscription;

  private gameService = inject(GameService);
  private toastService = inject(ToastService);
  private cdr = inject(ChangeDetectorRef);
  private userService = inject(UserService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);

  get player(): Player | undefined {
    return this.game?.players.find((item) => this.authService.getUser().username === item.username);
  }

  get opponent(): Player | undefined {
    return this.game?.players.find((item) => this.authService.getUser().username !== item.username);
  }

  get isOpponentReady(): boolean {
    return this.game?.type === GameType.TTS_BOT
      ? this.isBotReady
      : !!this.game?.ongoingRound?.choices.find((choice) => choice.player.id === this.opponent?.id);
  }

  ngOnInit(): void {
    this.routeSub = this.route.paramMap
      .pipe(
        map((params) => Number(params.get('gameId'))),
        switchMap((gameId) =>
          this.gameService.getGame(gameId).pipe(
            catchError((error: HttpErrorResponse) => {
              this.toastService.showError(error.error);
              this.navigateToMenu();
              return of(undefined);
            }),
          ),
        ),
      )
      .subscribe((res) => {
        if (!res) return;
        this.game = res;
        if (res.type !== GameType.TTS_BOT) {
          this.gameService.connectToGameUpdates(res.id);
          this.gameUpdateSub = this.gameService.onGameUpdate().subscribe((updatedGame) => {
            if (
              this.game!.status === GameStatus.WAITING_FOR_PLAYERS &&
              updatedGame.status === GameStatus.ONGOING
            ) {
              this.updateGameState(updatedGame);
              this.launchGame();
            } else {
              this.updateGameState(updatedGame);
              this.reflectChoice(updatedGame);
            }
          });
        }
        if (res.status === GameStatus.WAITING_FOR_PLAYERS) {
          this.cdr.detectChanges();
          return;
        }
        this.launchGame();
      });
  }

  ngOnDestroy(): void {
    this.gameUpdateSub?.unsubscribe();
    this.routeSub?.unsubscribe();
  }

  launchGame(): void {
    this.showVs = true;
    timer(3000).subscribe(() => {
      this.showVs = false;
      this.showChoices = true;
      this.cdr.detectChanges();
    });
    timer(4000).subscribe(() => {
      this.isBotReady = true;
      this.cdr.detectChanges();
    });
    this.cdr.detectChanges();
  }

  reflectChoice(res: Game): void {
    const lastRound = res.rounds![res.rounds!.length - 1];
    if (lastRound.status === GameRoundStatus.ONGOING) {
      this.cdr.detectChanges();
      return;
    }
    this.roundResult = {
      playerChoice: lastRound.choices.find((choice) => choice.player.id === this.player?.id)!,
      opponentChoice: lastRound.choices.find((choice) => choice.player.id === this.opponent?.id)!,
      winner: lastRound.winner,
    };
    this.cdr.detectChanges();

    const animationDuration = 2000;
    timer(animationDuration).subscribe(() => {
      if (this.game!.status === GameStatus.FINISHED) {
        this.isGameFinished = true;
        this.userService.updateUserPoints();
        this.cdr.detectChanges();
        return;
      }
      this.resetRound();
    });

    timer(animationDuration + 1000).subscribe(() => {
      this.isBotReady = true;
      this.cdr.detectChanges();
    });
  }

  updateGameState(newGame: Game): void {
    this.game = newGame;
  }

  resetRound(): void {
    this.isPlayerReady = false;
    this.isBotReady = false;
    this.choiceType = undefined;
    this.roundResult = undefined;
    this.cdr.detectChanges();
  }

  replayGame(): void {
    this.gameService.startGame(this.game!.type, 3).subscribe({
      next: (res) => this.router.navigate([`/game/ssp/${res.id}`]),
      error: (err: HttpErrorResponse) => this.toastService.showError(err.error),
    });
    this.resetRound();
    this.isGameFinished = false;
    this.isRoundFinished = false;
    this.game = undefined;
    this.showVs = false;
    this.showChoices = false;
  }

  navigateToMenu() {
    this.router.navigate(['/menu']);
  }

  selectChoice(choiceType: ChoiceType) {
    this.choiceType = choiceType;
  }

  confirmChoice() {
    if (!this.game || !this.choiceType) return;

    this.isPlayerReady = true;
    this.isBotReady = true;

    this.gameService.choice(this.game.id, this.choiceType).subscribe({
      next: (res) => {
        if (this.game?.type === GameType.TTS_BOT) {
          this.updateGameState(res);
          this.reflectChoice(res);
        }
      },
      error: (err: HttpErrorResponse) => this.toastService.showError(err.error),
    });
  }

  exitGame() {
    if (!this.game) return;
    this.gameService.stopGame(this.game.id).subscribe({
      next: () => {
        this.gameService.disconnect();
        this.navigateToMenu();
      },
    });
  }
}
