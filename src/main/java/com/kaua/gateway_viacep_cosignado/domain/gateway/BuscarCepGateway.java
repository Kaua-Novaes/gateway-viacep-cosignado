package com.kaua.gateway_viacep_cosignado.domain.gateway;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;

public interface BuscarCepGateway {
    ViaCepReturnDto buscarCep(String cep);
}
