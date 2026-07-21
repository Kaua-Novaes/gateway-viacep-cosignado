package com.kaua.gateway_viacep_cosignado.dto.viacep;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ReturnDto {

    private final String cep;
    private final String logradouro;
    private final String complemento;
    private final String bairro;
    private final String localidade;

    public ReturnDto(ResponseDto responseDto){
        this.cep = responseDto.getCep();
        this.logradouro = responseDto.getLogradouro();
        this.complemento = responseDto.getComplemento();
        this.bairro = responseDto.getBairro();
        this.localidade = responseDto.getLocalidade();
    }
}
