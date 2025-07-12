import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ChoiceType, GameChoice, Player } from '../../game.model';

export interface RoundResultInfo {
  playerChoice: GameChoice;
  opponentChoice: GameChoice;
  winner: Player | undefined | null;
}

@Component({
  selector: 'app-choice-card',
  standalone: true,
  imports: [CommonModule, CardModule],
  templateUrl: './choice-card.html',
  styleUrls: ['./choice-card.css'],
})
export class ChoiceCard {
  @Input() choiceType!: ChoiceType;
  @Input() selectedChoice?: ChoiceType;
  @Input() flipped = false;
  @Input() disabled = false;

  // Nuevos Inputs para manejar el resultado
  @Input() roundResult?: RoundResultInfo;
  @Input() isPlayerRow = false; // Para saber si es una carta del jugador o del oponente
  @Input() player?: Player;
  @Input() opponent?: Player;

  @Output() onChoose = new EventEmitter<ChoiceType>();

  imagePath = '';
  label = '';

  ngOnInit() {
    this.label = this.choiceType.charAt(0).toUpperCase() + this.choiceType.slice(1).toLowerCase();
    this.imagePath = 'game-options/' + this.choiceType.toLowerCase() + '.svg';
  }

  choose() {
    this.onChoose.emit(this.choiceType);
  }

  get isSelected(): boolean {
    return this.choiceType === this.selectedChoice;
  }

  get wasChosenInRound(): boolean {
    if (!this.roundResult) return false;
    const choiceInRound = this.isPlayerRow
      ? this.roundResult.playerChoice
      : this.roundResult.opponentChoice;
    return this.choiceType === choiceInRound.type;
  }

  get shouldBeRevealed(): boolean {
    return !!this.roundResult && this.wasChosenInRound;
  }

  get shouldBeHidden(): boolean {
    return !!this.roundResult && !this.wasChosenInRound;
  }

  get isEffectivelyFlipped(): boolean {
    return this.flipped && !this.shouldBeRevealed;
  }

  get isWinner(): boolean {
    if (!this.shouldBeRevealed || !this.roundResult?.winner) return false;
    const winnerId = this.roundResult.winner.id;
    return (
      (this.isPlayerRow && winnerId === this.player?.id) ||
      (!this.isPlayerRow && winnerId === this.opponent?.id)
    );
  }

  get isLoser(): boolean {
    if (!this.shouldBeRevealed || !this.roundResult?.winner) return false;
    const winnerId = this.roundResult.winner.id;
    return (
      (this.isPlayerRow && winnerId !== this.player?.id) ||
      (!this.isPlayerRow && winnerId !== this.opponent?.id)
    );
  }

  get isDraw(): boolean {
    return this.shouldBeRevealed && this.roundResult?.winner === null;
  }
}
