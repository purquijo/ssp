import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { UserService } from '../user-service';
import { UserStats } from '../user.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-user-stadistics',
  imports: [CommonModule, ButtonModule],
  templateUrl: './user-stadistics.html',
  styleUrl: './user-stadistics.css',
})
export class UserStadistics implements OnInit {
  stats?: UserStats;
  private readonly userService = inject(UserService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.userService.getUserStats().subscribe({
      next: (res) => {
        this.stats = res;
        this.cdr.detectChanges();
      },
    });
  }

  goToMenu(): void {
    this.router.navigate(['/menu']);
  }
}
