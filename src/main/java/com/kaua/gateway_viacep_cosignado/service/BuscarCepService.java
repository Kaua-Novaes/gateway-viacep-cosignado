package com.kaua.gateway_viacep_cosignado.service;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BuscarCepService {

    private final BuscarCepGateway buscarCepGateway;

    public BuscarCepService(BuscarCepGateway buscarCepGateway){
        this.buscarCepGateway = buscarCepGateway;
    }

    public ReturnDto buscarCep(String cep) throws NotFoundException, GatewayException {
      return buscarCepGateway.buscarCep(cep);
    };

}
