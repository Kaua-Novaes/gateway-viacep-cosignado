package com.kaua.gateway_viacep_cosignado.dto.viacep;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "cep",
        "logradouro",
        "complemento",
        "bairro",
        "localidade",
})
public class ReturnDto {

    private final String cep;
    private String logradouro;
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

    public void logradouroToLowerCase(){
        logradouro = logradouro.toLowerCase();
    }


}
