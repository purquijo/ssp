import { Component } from '@angular/core';
import { MenuItem, PrimeIcons } from 'primeng/api';
import { MenuModule } from 'primeng/menu';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-component',
  imports: [MenuModule, SelectButtonModule, FormsModule, CommonModule],
  templateUrl: './navbar-component.html',
  styleUrl: './navbar-component.css',
})
export class NavbarComponent {
  isDarkTheme: boolean = false;

  userMenuItems: MenuItem[] = [];

  constructor(
    public auth: AuthService,
    private router: Router,
  ) {
    this.userMenuItems = [
      {
        label: 'User Stats',
        icon: 'pi pi-chart-bar',
        command: () => this.router.navigate(['user/stats']),
      },
      {
        label: 'Logout',
        icon: PrimeIcons.SIGN_OUT,
        command: () => this.logout(),
      },
    ];
  }

  logout() {
    this.auth.logout();
  }
}
