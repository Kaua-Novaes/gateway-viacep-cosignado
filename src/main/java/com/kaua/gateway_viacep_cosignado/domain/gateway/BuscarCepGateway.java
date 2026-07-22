package com.kaua.gateway_viacep_cosignado.domain.gateway;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;

public interface BuscarCepGateway {
    ViaCepReturnDto buscarCep(String cep) throws NotFoundException, GatewayException;
}
