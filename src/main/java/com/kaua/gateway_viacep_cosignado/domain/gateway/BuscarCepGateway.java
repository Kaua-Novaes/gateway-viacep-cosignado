package com.kaua.gateway_viacep_cosignado.domain.gateway;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;

public interface BuscarCepGateway {
    ResponseDto buscarCep(String cep);
}
