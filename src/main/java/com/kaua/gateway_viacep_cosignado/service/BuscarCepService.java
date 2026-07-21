package com.kaua.gateway_viacep_cosignado.service;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;
import org.springframework.stereotype.Service;

@Service
public class BuscarCepService {

    private final BuscarCepGateway buscarCepGateway;

    public BuscarCepService(BuscarCepGateway buscarCepGateway){
        this.buscarCepGateway = buscarCepGateway;
    }

    public ReturnDto buscarCep(String cep){
      return buscarCepGateway.buscarCep(cep);
    };

}
