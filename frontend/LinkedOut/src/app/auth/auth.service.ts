import { Injectable } from '@angular/core';
import { User } from './user';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  async login(email: string, password: string): Promise<boolean> {
    try {
      const response = await fetch(`${this.apiUrl}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) throw new Error('Login falhou');

      const data = await response.json();
      localStorage.setItem('token', data.token);
      return true;
    } catch (err) {
      console.error('Erro ao fazer login:', err);
      return false;
    }
  }

  async register(newUser: User): Promise<User | null> {
    try {
      const { role, ...userPayload } = newUser; // Garante que a role não seja enviada
      const response = await fetch(`${this.apiUrl}/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userPayload)
      });
      return await response.json();
    } catch (error) {
      console.error('Erro ao cadastrar usuário:', error);
      return null;
    }
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      const exp = decoded.exp;
      if (!exp) return true;
      const now = Math.floor(Date.now() / 1000);
      return exp > now;
    } catch (error) {
      console.warn('Token inválido ou malformado:', error);
      this.logout();
      return false;
    }
  }

  /**
   * CORREÇÃO DEFINITIVA: Lê a lista de "authorities" do token.
   */
  hasRole(requiredRole: string): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded: any = this.decodeToken(token);
      const authorities: string[] = decoded.authorities || [];
      // O Spring Security envia as roles com o prefixo "ROLE_", então verificamos com ele.
      return authorities.includes(`ROLE_${requiredRole.toUpperCase()}`);
    } catch {
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserId(): number {
    const token = this.getToken();
    if (!token) return 0;
    const payload = this.decodeToken(token);
    return payload?.id ?? 0;
  }

  private decodeToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch (e) {
      console.error('Erro ao decodificar token:', e);
      return null;
    }
  }
}
