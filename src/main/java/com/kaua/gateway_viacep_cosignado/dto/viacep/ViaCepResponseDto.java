package com.kaua.gateway_viacep_cosignado.dto.viacep;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ViaCepResponseDto {
    private final String cep;
    private final String logradouro;
    private final String complemento;
    private final String bairro;
    private final String localidade;
    private final String uf;
    private final String ibge;
    private final String gia;
    private final String ddd;
    private final String siafi;
    private final String erro;
}
