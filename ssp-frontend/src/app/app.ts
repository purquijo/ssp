import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './auth/auth.service';
import { NavbarComponent } from './auth/navbar-component/navbar-component';
import { Toast } from 'primeng/toast';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, Toast, CommonModule],
  providers: [],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  public auth = inject(AuthService);

  logout() {
    this.auth.logout();
  }
}
