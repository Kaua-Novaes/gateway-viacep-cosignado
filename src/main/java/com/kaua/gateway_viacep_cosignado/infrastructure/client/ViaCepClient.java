package com.kaua.gateway_viacep_cosignado.infrastructure.client;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient implements BuscarCepGateway {

    private final RestClient restClient;

    public ViaCepClient(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public ResponseDto buscarCep(String cep) {
        return restClient
                .get()
                .uri("/{cep}/json", cep)
                .retrieve()
                .body(ResponseDto.class);
    }
}
