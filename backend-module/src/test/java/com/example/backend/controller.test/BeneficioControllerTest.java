package com.example.backend.controller.test;

import com.example.backend.BeneficioController;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.service.BeneficioService;
import com.example.entity.Beneficio;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest
class BeneficioControllerTest {

    @InjectMocks
    BeneficioController controller;

    @Mock
    BeneficioService service;

    @Test
    void listar() {
        List<Beneficio> listaBeneficio =new ArrayList<>();
        when(service.listar()).thenReturn(listaBeneficio);

        ResponseEntity<List<Beneficio>> listaGerada = controller.listar();

        assertNotNull(listaBeneficio);
        assertEquals(listaBeneficio.size(), Objects.requireNonNull(listaGerada.getBody()).size());
        assertEquals(listaGerada.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void salvar() {
        Beneficio b = new Beneficio();
        b.setNome("teste");
        b.setValor(BigDecimal.valueOf(100));

        when(service.salvar(Mockito.any(Beneficio.class))).thenReturn(b);

        ResponseEntity<Beneficio> beeficioSalvo = controller.salvar(b);

        assertEquals(beeficioSalvo.getBody().getNome(), "teste");
        assertEquals(beeficioSalvo.getStatusCode(), HttpStatus.OK);
        assertNotNull(beeficioSalvo);
    }

    @Test
    void transferirBeneficio() {
        TransferenciaRequest t = new TransferenciaRequest();
        t.setAmount(BigDecimal.valueOf(100));
        t.setFromId(1L);
        t.setToId(2L);

        doNothing().when(service).tranfer(t);
        ResponseEntity<String> msg = controller.transferirBeneficio(t);

        assertEquals(msg.getBody(), "Operação realizada com sucesso.");
        verify(service, times(1)).tranfer(t);
    }

    @Test
    void deletar() {
        Long id = 1L;
        doNothing().when(service).deletar(id);

        ResponseEntity<String> msg = controller.deletar(id);

        assertEquals(msg.getBody(), "Operação realizada com sucesso.");
        verify(service, times(1)).deletar(id);
    }

    @Test
    void listarBeneficio() {
        Long id = 1L;
        Beneficio beneficio = new Beneficio();
        beneficio.setNome("teste");
        beneficio.setId(1L);

        when(service.listar_beneficio(Mockito.anyLong())).thenReturn(beneficio);
        ResponseEntity<Beneficio> beneficioAchado = controller.listarBeneficio(id);

        assertEquals(beneficioAchado.getBody(), beneficio);
        assertEquals(beneficioAchado.getStatusCode(), HttpStatus.OK);
        assertNotNull(beneficioAchado);
    }

    @Test
    void atualizar() {
        Long id = 1L;
        Beneficio beneficio = new Beneficio();
        beneficio.setNome("novo");
        beneficio.setId(1L);

        when(service.atualizar(Mockito.anyLong(), Mockito.any(Beneficio.class))).thenReturn(beneficio);
        ResponseEntity<Beneficio> beneficioAtualizado = controller.atualizar(id, beneficio);

        assertEquals(beneficioAtualizado.getBody(), beneficio);
        assertEquals(beneficioAtualizado.getStatusCode(), HttpStatus.OK);
        assertNotNull(beneficioAtualizado);
    }

}