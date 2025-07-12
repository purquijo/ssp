import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { AuthGuard } from './auth/auth.guard';
import { MenuComponent } from './menu/menu-component';
import { SspGame } from './games/ssp-game/ssp-game';
import { UserStadistics } from './user/user-stadistics/user-stadistics';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'menu', component: MenuComponent, canActivate: [AuthGuard] },
  { path: 'user/stats', component: UserStadistics, canActivate: [AuthGuard] },
  { path: 'game/ssp/:gameId', component: SspGame, canActivate: [AuthGuard] },
];
