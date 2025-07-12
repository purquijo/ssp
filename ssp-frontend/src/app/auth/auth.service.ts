import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../evironments/environment';
import { AuthResponse, User } from './auth.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private token: string | null = null;
  private baseUrl = environment.apiUrl;
  private user: User | null = null;

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  login(username: string, password: string) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, { username, password });
  }

  register(username: string, password: string) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/register`, { username, password });
  }

  saveToken(token: string) {
    this.token = token;
    localStorage.setItem('auth_token', token);
  }

  saveAuthInfo(authResponse: AuthResponse) {
    this.token = authResponse.token;
    const user = {
      username: authResponse.username,
      points: authResponse.points,
      avatar: authResponse.avatar,
    };
    this.user = user;
    localStorage.setItem('auth_token', authResponse.token);
    localStorage.setItem('user', JSON.stringify(user));
  }

  getToken() {
    return this.token || localStorage.getItem('auth_token');
  }

  getUser(): User {
    return this.user || JSON.parse(localStorage.getItem('user') || '{}');
  }

  updatePoints(points: number) {
    const oldUser = this.getUser();
    oldUser.points = points;
    localStorage.setItem('user', JSON.stringify(oldUser));
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout() {
    this.token = null;
    this.user = null;
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }
}
