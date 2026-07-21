package com.kaua.gateway_viacep_cosignado.domain.gateway;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;

public interface BuscarCepGateway {
    ReturnDto buscarCep(String cep);
}
