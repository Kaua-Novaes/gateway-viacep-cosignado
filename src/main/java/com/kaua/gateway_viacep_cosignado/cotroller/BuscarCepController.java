package com.kaua.gateway_viacep_cosignado.cotroller;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;
import com.kaua.gateway_viacep_cosignado.service.BuscarCepService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuscarCepController {

    private final BuscarCepService buscarCepService;

    public BuscarCepController(BuscarCepService buscarCepService){
        this.buscarCepService = buscarCepService;
    }

    @GetMapping("/buscarcep/{cep}")
    public ResponseEntity<?> buscarCep(@PathVariable String cep){
        ReturnDto respostaCep = buscarCepService.buscarCep(cep);
        return ResponseEntity.ok(respostaCep);
    };
}
