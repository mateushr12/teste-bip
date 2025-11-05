import { Component, OnInit } from '@angular/core';
import { Beneficio, BeneficioService } from '../../services/beneficio.service';

@Component({
  selector: 'app-beneficio',
  templateUrl: './beneficio.component.html',
  styleUrl: './beneficio.component.css'
})
export class BeneficioComponent implements OnInit {

  beneficios: Beneficio[] = [];
  selecionado: Beneficio = {};
  edicao: boolean = false;

  constructor(private beneficioService: BeneficioService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar() {
    this.beneficioService.listarTodos().subscribe(data => this.beneficios = data);
  }

  selecionar(b: Beneficio) {
    this.edicao = true;
    this.selecionado = { ...b };
  }

  novo(){
    this.edicao = false;
    this.selecionado = {};
  }

  deletar(id?: number) {
    if (!id) return;
    this.beneficioService.deletar(id).subscribe(() => this.carregar());
  }

  salvar() {
    if (this.edicao && this.selecionado?.id) {
      this.beneficioService.atualizar(this.selecionado.id, this.selecionado).subscribe(
        () => alert("Atualizado com sucesso")
      );
    } else {
      this.beneficioService.salvar(this.selecionado).subscribe(
        () => alert("Salvo com sucesso")
      );
    }
  }

  transferir() {
    const origemId = prompt('ID de origem?');
    const destinoId = prompt('ID de destino?');
    const valor = prompt('Valor da transferência?');
    if (origemId && destinoId && valor) {
      this.beneficioService.transferir({
        beneficioId: +origemId,
        destinatarioId: +destinoId,
        valor: +parseFloat(valor)
      }).subscribe(msg => alert(msg));
    }
  }

}
