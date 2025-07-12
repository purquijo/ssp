import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { Card, CardModule } from 'primeng/card';
import { Message } from 'primeng/message';
import { FloatLabel } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { FluidModule } from 'primeng/fluid';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { ToastService } from '../../shared/service/toast-service';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login.component',
  standalone: true,
  imports: [
    Card,
    Message,
    FloatLabel,
    CardModule,
    InputTextModule,
    FluidModule,
    ButtonModule,
    FormsModule,
    CommonModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  error = '';

  private auth = inject(AuthService);
  private router = inject(Router);
  private toastService = inject(ToastService);

  ngOnInit(): void {
    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/menu']);
    }
  }

  login() {
    if (!this.username || !this.password) return;

    this.auth.login(this.username, this.password).subscribe({
      next: (res) => {
        this.auth.saveAuthInfo(res);
        this.router.navigate(['/menu']);
      },
      error: (err: HttpErrorResponse) => this.toastService.showError(err.error),
    });
  }

  register() {
    if (!this.username || !this.password) return;

    this.auth.register(this.username, this.password).subscribe({
      next: (res) => {
        this.auth.saveAuthInfo(res);
        this.router.navigate(['/menu']);
      },
      error: (err: HttpErrorResponse) => this.toastService.showError(err.error),
    });
  }
}
