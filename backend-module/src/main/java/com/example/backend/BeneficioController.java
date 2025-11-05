package com.example.backend;

import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.service.BeneficioService;
import com.example.entity.Beneficio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficio")
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios")
public class BeneficioController {

    @Autowired
    private BeneficioService service;

    @Operation(
            summary = "Listar todos os benefícios",
            description = "Retorna uma lista com todos os benefícios cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de benefícios retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Beneficio.class)
                    )
            )
    })
    @GetMapping("listar_todos")
    public ResponseEntity<List<Beneficio>> listar() {
        return ResponseEntity.ok(service.listar());
    }


    @PostMapping
    @Operation(
            summary = "Criar novo benefício",
            description = "Cadastra um novo benefício no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Benefício criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Beneficio.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<Beneficio> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do benefício a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Beneficio.class))
            )
            @RequestBody Beneficio beneficio
    ) {
        return ResponseEntity.ok(service.salvar(beneficio));
    }


    @PostMapping("transferir")
    @Operation(
            summary = "Transferir benefício",
            description = "Realiza a transferência de benefício entre usuários"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transferência realizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício ou usuário não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<String> transferirBeneficio(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da transferência (id do benefício origem e destino, valor a ser transferido)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransferenciaRequest.class))
            )
            @RequestBody TransferenciaRequest transferenciaRequest
    ) {
         service.tranfer(transferenciaRequest);
         return ResponseEntity.ok("Operação realizada com sucesso.");
    }

    @DeleteMapping("deletar/{id}")
    @Operation(
            summary = "Deletar benefício",
            description = "Remove benefício pelo id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Benefício deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<String> deletar(
            @Parameter(description = "id do benefício a ser deletado", required = true)
            @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.ok("Operação realizada com sucesso.");
    }

    @GetMapping("/listar/{id}")
    @Operation(
            summary = "Buscar benefício por id",
            description = "Retorna um benefício específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Benefício encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Beneficio.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<Beneficio> listarBeneficio(
            @Parameter(description = "id do benefício", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.listar_beneficio(id));
    }

    @PutMapping("/atualizar/{id}")
    @Operation(
            summary = "Atualizar benefício",
            description = "Atualiza os dados de um benefício"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Benefício atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Beneficio.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            )
    })
    public ResponseEntity<Beneficio> atualizar(
            @Parameter(description = "id do benefício a ser atualizado", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Beneficio.class))
            )
            @RequestBody Beneficio novosDados
    ) {
        return ResponseEntity.ok(service.atualizar(id, novosDados));
    }

    @GetMapping
    public List<String> list() {
        return Arrays.asList("Beneficio A", "Beneficio B");
    }
}
