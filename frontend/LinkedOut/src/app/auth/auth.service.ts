import { Injectable } from '@angular/core';
import { User } from './user';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/auth';

  async login(email: string, password: string): Promise<boolean> {
    try {
      const response = await fetch(this.apiUrl, {
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
      const response = await fetch(this.apiUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newUser)
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

      if (!exp) return true; // Se não tiver expiração definida no token, assume-se válido

      const now = Math.floor(Date.now() / 1000);
      return exp > now;
    } catch (error) {
      console.warn('Token inválido ou malformado:', error);
      this.logout(); // Remove token inválido
      return false;
    }
  }

  hasRole(requiredRole: string): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.role === requiredRole;
    } catch {
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
