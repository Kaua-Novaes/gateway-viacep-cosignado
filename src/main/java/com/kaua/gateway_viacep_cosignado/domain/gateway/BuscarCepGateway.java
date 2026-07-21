package com.kaua.gateway_viacep_cosignado.domain.gateway;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;

public interface BuscarCepGateway {
    ReturnDto buscarCep(String cep) throws NotFoundException, GatewayException;
}
