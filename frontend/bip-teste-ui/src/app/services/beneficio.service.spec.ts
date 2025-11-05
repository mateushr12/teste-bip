import { TestBed } from '@angular/core/testing';

import { Beneficio, BeneficioService, TransferenciaRequest } from './beneficio.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('BeneficioService', () => {
  let service: BeneficioService;
  let http: HttpTestingController

    const apiUrl = 'http://localhost:8080/api/v1/beneficios';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BeneficioService]
    });
    service = TestBed.inject(BeneficioService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  //--------teste -----------
  it('deve listar todos os benefícios', () => {
    const mockBeneficios: Beneficio[] = [
      { id: 1, nome: 'Benefício 111' },
      { id: 2, nome: 'Benefício 3333' }
    ];

    service.listarTodos().subscribe((beneficios) => {
      expect(beneficios.length).toBe(2);
      expect(beneficios).toEqual(mockBeneficios);
    });

    const req = http.expectOne(`${apiUrl}/listar_todos`);
    expect(req.request.method).toBe('GET');
    req.flush(mockBeneficios);
  });

  it('deve listar beneficios por id', () => {
    const mockBeneficio: Beneficio = { id: 1111, nome: 'Benefício 1111' };

    service.listarPorId(1111).subscribe((beneficio) => {
      expect(beneficio).toEqual(mockBeneficio);
    });

    const req = http.expectOne(`${apiUrl}/listar/1111`);
    expect(req.request.method).toBe('GET');
    
  });

  it('deve salvar um novo benefício', () => {
    const newBene: Beneficio = { id: 3, nome: 'Novo Benefício' };

    service.salvar(newBene).subscribe((res) => {
      expect(res).toEqual(newBene);
    });

    const req = http.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newBene);
    
  });

  it('deve atualizar um beneficio', () => {
    const beneficioAtualizado: Beneficio = { id: 1, nome: 'Benefício atualizado' };

    service.atualizar(1, beneficioAtualizado).subscribe((res) => {
      expect(res).toEqual(beneficioAtualizado);
    });

    const req = http.expectOne(`${apiUrl}/atualizar/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(beneficioAtualizado);
    
  });

  it('deve deletar um benefício', () => {
    const mockResponse = 'Benefício deletado com sucesso';

    service.deletar(1).subscribe((res) => {
      expect(res).toBe(mockResponse);
    });

    const req = http.expectOne(`${apiUrl}/deletar/1`);
    expect(req.request.method).toBe('DELETE');
    
  });

  it('deve transferir benefício', () => {
    const mockRequest: TransferenciaRequest = { beneficioId: 1, destinatarioId: 2, valor: 100 };
    const mockResponse = 'Transferência realizada com sucesso';

    service.transferir(mockRequest).subscribe((res) => {
      expect(res).toBe(mockResponse);
    });

    const req = http.expectOne(`${apiUrl}/transferir`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
   
  });
  //--------------
});
