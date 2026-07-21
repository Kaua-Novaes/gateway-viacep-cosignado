package com.kaua.gateway_viacep_cosignado.domain.validator;

import com.kaua.gateway_viacep_cosignado.exception.CepInvalidoException;
import org.springframework.stereotype.Component;

@Component
public class CepValidator {

    private static final String CEP_REGEX = "\\d{8}";

    public void validar(String cep) throws CepInvalidoException {
        if (cep == null || cep.isBlank()) {
            throw new CepInvalidoException("O CEP é obrigatório");
        }

        String cepNormalizado = removerFormatacao(cep);

        if (!cepNormalizado.matches(CEP_REGEX)) {
            throw new CepInvalidoException(
                    "O CEP deve possuir exatamente 8 números"
            );
        }
    }

    public String normalizar(String cep) throws CepInvalidoException {
        validar(cep);
        return removerFormatacao(cep);
    }

    private String removerFormatacao(String cep) {
        return cep.replaceAll("\\D", "");
    }
}