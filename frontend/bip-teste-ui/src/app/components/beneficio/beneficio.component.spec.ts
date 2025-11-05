import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficioComponent } from './beneficio.component';
import { Beneficio, BeneficioService } from '../../services/beneficio.service';
import { of } from 'rxjs';

describe('BeneficioComponent', () => {
  let component: BeneficioComponent;
  let fixture: ComponentFixture<BeneficioComponent>;
  let beneficioServiceSpy: jasmine.SpyObj<BeneficioService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('BeneficioService', [
      'listarTodos',
      'salvar',
      'transferir',
      'atualizar',
      'deletar',
    ]);

    await TestBed.configureTestingModule({
      declarations: [BeneficioComponent],
      providers: [{ provide: BeneficioService, useValue: spy }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BeneficioComponent);
    component = fixture.componentInstance;
    beneficioServiceSpy = TestBed.inject(BeneficioService) as jasmine.SpyObj<BeneficioService>;
    fixture.detectChanges();
  });

  it('deve chamar ngOnInit', () => {
    const mockBeneficios: Beneficio[] = [{ id: 1, nome: 'Benefício 1' }];
    beneficioServiceSpy.listarTodos.and.returnValue(of(mockBeneficios));

    component.ngOnInit();

    expect(beneficioServiceSpy.listarTodos).toHaveBeenCalled();
    expect(component.beneficios).toEqual(mockBeneficios);
  });

  it('deve carregar todos os benefícios', () => {
    const mockBeneficios: Beneficio[] = [{ id: 1, nome: 'Benefício A' }];
    beneficioServiceSpy.listarTodos.and.returnValue(of(mockBeneficios));

    component.carregar();

    expect(beneficioServiceSpy.listarTodos).toHaveBeenCalled();
    expect(component.beneficios).toEqual(mockBeneficios);
  });

  it('deve selecionar um benefício ,ativar modo de edição', () => {
    const beneficio: Beneficio = { id: 1, nome: 'Teste' };

    component.selecionar(beneficio);

    expect(component.edicao).toBeTrue();
    expect(component.selecionado).toEqual(beneficio);
  });

  it('deve criar um novo benefício ,desativar modo de edição', () => {
    component.edicao = true;
    component.selecionado = { id: 1, nome: 'Anterior' };

    component.novo();

    expect(component.edicao).toBeFalse();
    expect(component.selecionado).toEqual({});
  });

  it('deve deletar um benefício', () => {
    const mockBeneficios: Beneficio[] = [{ id: 1, nome: 'Teste' }];
    beneficioServiceSpy.deletar;
    beneficioServiceSpy.listarTodos.and.returnValue(of(mockBeneficios));

    component.deletar(1);

    expect(beneficioServiceSpy.deletar).toHaveBeenCalledWith(1);
  });

  it('deve atualizar um benefício ', () => {
    
    const beneficio: Beneficio = { id: 111, nome: 'Atualizado' };
    beneficioServiceSpy.atualizar.and.returnValue(of(beneficio));
    component.edicao = true;
    component.selecionado = beneficio;

    component.salvar();

    expect(beneficioServiceSpy.atualizar).toHaveBeenCalledWith(111, beneficio);
    
  });

  it('deve salvar um novo benefício ', () => {

    const beneficio: Beneficio = { nome: 'Novo Benefício' };
    beneficioServiceSpy.salvar.and.returnValue(of(beneficio));
    component.edicao = false;
    component.selecionado = beneficio;

    component.salvar();

    expect(beneficioServiceSpy.salvar).toHaveBeenCalledWith(beneficio);
  });

  it('deve transferir um benefício quando todos os prompts forem informados', () => {
    spyOn(window, 'prompt').and.returnValues('11', '222', '50');
    spyOn(window, 'alert');
    
    beneficioServiceSpy.transferir.and.returnValue(of('Transferência realizada com sucesso'));

    component.transferir();

    expect(beneficioServiceSpy.transferir).toHaveBeenCalledWith({
      beneficioId: 11,
      destinatarioId: 222,
      valor: 50
    });
    expect(window.alert).toHaveBeenCalledWith('Transferência realizada com sucesso');
  });

  
});
