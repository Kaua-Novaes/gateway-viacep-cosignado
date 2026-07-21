package com.kaua.gateway_viacep_cosignado.service;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.domain.validator.CepValidator;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.CepInvalidoException;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BuscarCepService {

    private final BuscarCepGateway buscarCepGateway;
    private final CepValidator cepValidator;

    public BuscarCepService(BuscarCepGateway buscarCepGateway, CepValidator cepValidator){
        this.buscarCepGateway = buscarCepGateway;
        this.cepValidator = cepValidator;

    }

    public ReturnDto buscarCep(String cep) throws NotFoundException, GatewayException, CepInvalidoException {
        cepValidator.validar(cep);
        return buscarCepGateway.buscarCep(cep);
    };

}
