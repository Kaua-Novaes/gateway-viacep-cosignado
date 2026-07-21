package com.kaua.gateway_viacep_cosignado.infrastructure.client;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ViaCepClient implements BuscarCepGateway {

    private final RestClient restClient;

    public ViaCepClient(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public ReturnDto buscarCep(String cep) throws NotFoundException, GatewayException {

        try {
            ResponseDto respostaApi = restClient
                    .get()
                    .uri("/{cep}/json", cep)
                    .retrieve()
                    .body(ResponseDto.class);


            if (respostaApi == null) {
                throw new GatewayException("O ViaCEP retornou uma resposta vazia");
            } else if (respostaApi.getBairro() == null) {
                throw new NotFoundException("Não foi possível encontrar um endereço válido");
            }
            return new ReturnDto(respostaApi);

        }catch (NotFoundException exception){
            throw exception;
        }
        catch (RestClientException exception){
            throw new GatewayException("Não foi possível consultar o serviço ViaCEP");
        }
    }
}
