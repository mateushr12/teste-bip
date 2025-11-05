package com.example.backend.service.test;

import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.service.BeneficioService;
import com.example.ejb.BeneficioEjbServiceRemote;
import com.example.entity.Beneficio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository repository;

    @Mock
    private BeneficioEjbServiceRemote ejbService;

    @InjectMocks
    private BeneficioService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new BeneficioService(repository);
        service.ejbService = ejbService;
    }

    @Test
    void salvar() {
        Beneficio beneficio = new Beneficio();
        beneficio.setNome("beneficio 1");

        when(repository.save(Mockito.any(Beneficio.class))).thenReturn(beneficio);

        Beneficio salvo = service.salvar(beneficio);

        assertEquals("beneficio 1", salvo.getNome());
        verify(repository, times(1)).save(beneficio);
    }

    @Test
    void deletar() {
        Beneficio b = new Beneficio();
        b.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(b));

        service.deletar(1L);

        verify(repository).delete(b);
    }

    @Test
    void listar() {
        List<Beneficio> lista = Arrays.asList(new Beneficio(), new Beneficio());
        when(repository.findAll()).thenReturn(lista);

        List<Beneficio> result = service.listar();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void tranfer() {
        TransferenciaRequest req = new TransferenciaRequest();
        req.setFromId(1L);
        req.setToId(2L);
        req.setAmount(BigDecimal.valueOf(333));

        doNothing().when(ejbService).transfer(1L, 2L, BigDecimal.valueOf(333));
        service.tranfer(req);
        verify(ejbService).transfer(1L, 2L, BigDecimal.valueOf(333));
    }

    @Test
    void atualizar() {
        Beneficio existente = new Beneficio();
        existente.setId(1L);
        existente.setNome("anterior");

        Beneficio dto = new Beneficio();
        dto.setNome("novo");
        dto.setDescricao("atualizado");
        dto.setValor(BigDecimal.valueOf(12));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Beneficio.class))).thenReturn(dto);

        Beneficio atualizado = service.atualizar(1L, dto);

        assertEquals("novo", atualizado.getNome());
        assertEquals("atualizado", atualizado.getDescricao());
        assertEquals(BigDecimal.valueOf(12), atualizado.getValor());
    }

    @Test
    void listar_beneficio() {
        Beneficio b = new Beneficio();
        b.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(b));

        Beneficio result = service.listar_beneficio(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

}