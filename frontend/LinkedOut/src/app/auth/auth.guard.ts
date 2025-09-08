import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  // Modificado para ler um array de 'roles' em vez de uma única 'role'
  const requiredRoles: string[] | undefined = route.data?.['roles'];

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  // Verifica se a rota exige papéis específicos
  if (requiredRoles && requiredRoles.length > 0) {
    // Verifica se o usuário possui PELO MENOS UM dos papéis necessários
    const userHasRequiredRole = requiredRoles.some(role => authService.hasRole(role));
    if (!userHasRequiredRole) {
      // Se não tiver, redireciona para a página de não autorizado
      return router.createUrlTree(['/unauthorized']);
    }
  }

  // Se o usuário está autenticado e tem o papel necessário (ou a rota não exige um), permite o acesso.
  return true;
};
