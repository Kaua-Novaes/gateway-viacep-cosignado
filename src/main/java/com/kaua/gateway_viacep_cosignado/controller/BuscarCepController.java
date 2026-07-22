package com.kaua.gateway_viacep_cosignado.controller;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.CepInvalidoException;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import com.kaua.gateway_viacep_cosignado.service.BuscarCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BuscarCepController {

    private final BuscarCepService buscarCepService;

    public BuscarCepController(BuscarCepService buscarCepService){
        this.buscarCepService = buscarCepService;
    }

    @Operation(summary = "Normzaliza e faz a busca do cep no gateway de busca")
    @ApiResponse(responseCode = "200", description = "Endereço encontrado")
    @ApiResponse(responseCode = "400", description = "Endereço não encontrado")
    @ApiResponse(responseCode = "503", description = "Erro no gateway de busca")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    @GetMapping("/buscarcep/{cep}")
    public ResponseEntity<?> buscarCep(@PathVariable String cep)  {
        ViaCepReturnDto respostaCep = buscarCepService.buscarCep(cep);
        return ResponseEntity.ok(respostaCep);
    }
}
