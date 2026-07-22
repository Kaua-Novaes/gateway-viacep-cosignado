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
public class ViaCepReturnDto {

    private final String cep;
    private String logradouro;
    private final String complemento;
    private final String bairro;
    private final String localidade;

    public ViaCepReturnDto(ViaCepResponseDto viaCepResponseDto){
        this.cep = viaCepResponseDto.getCep();
        this.logradouro = viaCepResponseDto.getLogradouro();
        this.complemento = viaCepResponseDto.getComplemento();
        this.bairro = viaCepResponseDto.getBairro();
        this.localidade = viaCepResponseDto.getLocalidade();
    }

    public void logradouroToLowerCase(){
        logradouro = logradouro.toLowerCase();
    }


}
