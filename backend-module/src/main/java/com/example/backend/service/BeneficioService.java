package com.example.backend.service;

import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.BeneficioEjbServiceRemote;
import com.example.entity.Beneficio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficioService {

        public BeneficioEjbServiceRemote ejbService;

        @Autowired
        public BeneficioRepository repository;

    /**
     *
     * @param repository
     */
    public BeneficioService(BeneficioRepository repository) {
            try {
                InitialContext ctx = new InitialContext();
                ejbService = (BeneficioEjbServiceRemote) ctx.lookup("java:global/ejb-module/BeneficioEjbService!com.example.ejb.BeneficioEjbServiceRemote");
            } catch (NamingException e) {
                throw new RuntimeException("Não foi possivel conectar com o EJB", e);
            }
        }

    /**
     *
     * @param beneficio
     * @return
     */
    public Beneficio salvar(Beneficio beneficio) {
            return repository.save(beneficio);
        }

    /**
     *
     * @param id
     */
    public void deletar(Long id) {
            Optional<Beneficio> beneficio = repository.findById(id);
            if (beneficio.isEmpty()){
                throw new EntityNotFoundException("Beneficio não encontrado");
            }
            repository.delete(beneficio.get());
        }

    /**
     *
     * @return
     */
    public List<Beneficio> listar() {
            return repository.findAll();
        }

    /**
     *
     * @param transferenciaRequest
     */
    public void tranfer(TransferenciaRequest transferenciaRequest){
            ejbService.transfer(transferenciaRequest.getFromId(), transferenciaRequest.getToId(), transferenciaRequest.getAmount());
        }

        public Beneficio atualizar (Long id, Beneficio beneficioDto){
            Optional<Beneficio> beneficio = repository.findById(id);
            if (beneficio.isEmpty()) {
                throw new EntityNotFoundException("Beneficio não encontrado.");
            }
            Beneficio beneficioAchado = beneficio.get();

            beneficioAchado.setNome(beneficioDto.getNome());
            beneficioAchado.setDescricao(beneficioDto.getDescricao());
            beneficioAchado.setValor(beneficioDto.getValor());

            return repository.save(beneficioAchado);

        }

    /**
     *
     * @param id
     * @return
     */
    public Beneficio listar_beneficio(Long id){
            return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Beneficio não encontrado"));
        }

}
