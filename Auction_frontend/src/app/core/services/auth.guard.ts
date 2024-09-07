import { CanActivateFn, ActivatedRouteSnapshot,Router } from '@angular/router';
import { RouterStateSnapshot } from '@angular/router';
import { LoginServiceService } from './login-service/login-service.service';
import { Observable } from 'rxjs';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const authService = inject(LoginServiceService);
  const router = inject(Router);

  const isAuthenticated = authService.isAuthenticated();

  if (!isAuthenticated) {
    router.navigate(['/login']);
    return false;
  }
  return true;
};
