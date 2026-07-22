package com.kaua.gateway_viacep_cosignado.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private Integer status;

    public ErrorResponse(int value, String rotaNãoEncontrada, String s) {
    }
}
