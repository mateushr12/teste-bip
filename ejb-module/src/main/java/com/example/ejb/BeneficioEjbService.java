package com.example.ejb;

import com.example.entity.Beneficio;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;


@Stateless
public class BeneficioEjbService implements BeneficioEjbServiceRemote{

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param fromId
     * @param toId
     * @param amount
     */
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC);
        Beneficio to   = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferencia deve ser maior que zero");
        }

        if (fromId.equals(toId)){
            throw new IllegalArgumentException("Não é permitido transferir para o mesmo usuario");
        }

         if (from == null || to == null) {
            throw new EntityNotFoundException("Benefício de origem ou destino não encontrado.");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo deve ser maior que zero para tranferencia.");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
