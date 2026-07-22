package com.kaua.gateway_viacep_cosignado.infrastructure.client;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Objects;

@Component
public class ViaCepClient implements BuscarCepGateway {

    private final RestClient restClient;

    public ViaCepClient(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public ViaCepReturnDto buscarCep(String cep) throws NotFoundException, GatewayException {

        try {
            ViaCepResponseDto respostaApi = restClient
                    .get()
                    .uri("/{cep}/json", cep)
                    .retrieve()
                    .body(ViaCepResponseDto.class);


            if (respostaApi == null) {
                throw new GatewayException("O ViaCEP retornou uma resposta vazia");
            } else if (Objects.equals(respostaApi.getErro(), "true")) {
                throw new NotFoundException("Não foi possível encontrar um endereço válido");
            }
            return new ViaCepReturnDto(respostaApi);

        }catch (NotFoundException exception){
            throw exception;
        }
        catch (RestClientException exception){
            throw new GatewayException("Não foi possível consultar o serviço ViaCEP");
        }
    }
}
