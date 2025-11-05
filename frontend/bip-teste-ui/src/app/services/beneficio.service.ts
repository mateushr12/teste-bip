import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Beneficio {
  id?: number;
  nome?: string;
  descricao?: string;
  valor?: number;
}

export interface TransferenciaRequest {
  beneficioId?: number;
  destinatarioId?: number;
  valor?: number;
}

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {

  private apiUrl = 'http://localhost:8080/api/v1/beneficios';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(`${this.apiUrl}/listar_todos`);
  }

  listarPorId(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.apiUrl}/listar/${id}`);
  }

  salvar(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiUrl, beneficio);
  }

  atualizar(id: number, beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.apiUrl}/atualizar/${id}`, beneficio);
  }

  deletar(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/deletar/${id}`, { responseType: 'text' });
  }

  transferir(request: TransferenciaRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/transferir`, request, { responseType: 'text' });
  }


}
