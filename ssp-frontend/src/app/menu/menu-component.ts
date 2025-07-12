import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Card } from 'primeng/card';
import { GameService } from '../games/game.service';
import { GameType } from '../games/game.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastService } from '../shared/service/toast-service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-menu-component',
  imports: [Card, CommonModule, FormsModule, InputTextModule, ButtonModule],
  templateUrl: './menu-component.html',
  styleUrl: './menu-component.css',
})
export class MenuComponent {
  GameType = GameType;
  joinCode?: number;

  showMultiplayerOptions = false;

  private router = inject(Router);
  private gameService = inject(GameService);
  private toastService = inject(ToastService);

  startGame(gameType: GameType) {
    this.gameService.startGame(gameType, 3).subscribe({
      next: (res) => this.router.navigate([`/game/ssp/${res.id}`]),
      error: (err: HttpErrorResponse) => this.toastService.showError(err.error),
    });
  }

  joinGame() {
    if (!this.joinCode) {
      return;
    }
    this.gameService.joinGame(this.joinCode).subscribe({
      next: (res) => this.router.navigate([`/game/ssp/${res.id}`]),
      error: (err: HttpErrorResponse) => this.toastService.showError(err.error),
    });
  }

  onMultiplayerClick(event: Event) {
    event.stopPropagation();
    this.showMultiplayerOptions = true;
  }

  onBackgroundClick() {
    if (this.showMultiplayerOptions) {
      this.showMultiplayerOptions = false;
    }
  }

  navigateTo(path: string) {
    this.router.navigate([`/game/${path}`]);
  }
}
