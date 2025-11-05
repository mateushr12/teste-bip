package com.example.ejb.test;

import com.example.ejb.BeneficioEjbService;
import com.example.entity.Beneficio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class BeneficioEjbServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbService service;

    private Beneficio fromBeneficio;
    private Beneficio toBeneficio;

    @BeforeEach
    void setUp() {
        fromBeneficio = new Beneficio();
        fromBeneficio.setId(1L);
        fromBeneficio.setValor(BigDecimal.valueOf(1000));

        toBeneficio = new Beneficio();
        toBeneficio.setId(2L);
        toBeneficio.setValor(BigDecimal.valueOf(500));
    }

    @Test
    void transferComSucesso() {
        when(em.find(Beneficio.class, 1L, LockModeType.OPTIMISTIC)).thenReturn(fromBeneficio);
        when(em.find(Beneficio.class, 2L, LockModeType.OPTIMISTIC)).thenReturn(toBeneficio);

        service.transfer(1L, 2L, BigDecimal.valueOf(30));

        assertEquals(BigDecimal.valueOf(970), fromBeneficio.getValor());
        assertEquals(BigDecimal.valueOf(530), toBeneficio.getValor());

        verify(em, times(1)).merge(fromBeneficio);
        verify(em, times(1)).merge(toBeneficio);
    }

    @Test
    void transferirValorNulo() {
        Long fromId = 1L;
        Long toId = 2L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(fromId, toId, null)
        );

        assertEquals("O valor da transferencia deve ser maior que zero", exception.getMessage());
        verify(em, never()).merge(any());
    }

    @Test
    void transferirMesmoUsuario() {

        Long mesmoId = 1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(mesmoId, mesmoId, BigDecimal.TEN)
        );

        assertEquals("Não é permitido transferir para o mesmo usuario", exception.getMessage());
        verify(em, never()).merge(any());
    }

    @Test
    void fromIdNaoEncontrado() {
        Long fromId = 1L;
        Long toId = 2L;

        when(em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC)).thenReturn(null);
        when(em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC)).thenReturn(toBeneficio);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.transfer(fromId, toId, BigDecimal.TEN)
        );

        assertEquals("Benefício de origem ou destino não encontrado.", exception.getMessage());
        verify(em, never()).merge(any());
    }

    @Test
    void toIdNaoEncontrado() {
        Long fromId = 1L;
        Long toId = 2L;

        when(em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC)).thenReturn(fromBeneficio);
        when(em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.transfer(fromId, toId, BigDecimal.TEN)
        );

        assertEquals("Benefício de origem ou destino não encontrado.", exception.getMessage());
        verify(em, never()).merge(any());
    }

    @Test
    void saldoInsuficiente() {

        Long fromId = 1L;
        Long toId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1800);

        when(em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC)).thenReturn(fromBeneficio);
        when(em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC)).thenReturn(toBeneficio);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.transfer(fromId, toId, amount)
        );

        assertEquals("Saldo deve ser maior que zero para tranferencia.", exception.getMessage());
        verify(em, never()).merge(any());
    }

}