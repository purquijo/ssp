import { Injectable } from '@angular/core';
import { environment } from '../../evironments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { UserStats } from './user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  updateUserPoints() {
    this.http.get<number>(`${this.baseUrl}/user/points`).subscribe({
      next: (res) => this.authService.updatePoints(res),
    });
  }

  getUserStats() {
    return this.http.get<UserStats>(`${this.baseUrl}/user/stats`);
  }
}
