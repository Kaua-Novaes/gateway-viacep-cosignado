package com.kaua.gateway_viacep_cosignado.controller;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.CepInvalidoException;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import com.kaua.gateway_viacep_cosignado.service.BuscarCepService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuscarCepController.class)
class BuscarCepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuscarCepService buscarCepService;

    @Test
    void deveRetornarOkEEnderecoQuandoCepForValido() throws Exception {
        String cep = "01001000";
        ViaCepResponseDto responseDto = new ViaCepResponseDto(
                "01001-000", "Praça da Sé", "lado ímpar", "Sé", "São Paulo", "SP", "3550308", "1004", "11", "7107", "false"
        );
        ViaCepReturnDto returnDto = new ViaCepReturnDto(responseDto);

        when(buscarCepService.buscarCep(cep)).thenReturn(returnDto);

        mockMvc.perform(get("/api/v1/buscarcep/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01001-000"))
                .andExpect(jsonPath("$.logradouro").value("Praça da Sé"))
                .andExpect(jsonPath("$.complemento").value("lado ímpar"))
                .andExpect(jsonPath("$.bairro").value("Sé"))
                .andExpect(jsonPath("$.localidade").value("São Paulo"));
    }

    @Test
    void deveRetornarNotFoundQuandoCepForInvalido() throws Exception {
        String cep = "123";
        when(buscarCepService.buscarCep(cep)).thenThrow(new CepInvalidoException("O CEP deve possuir exatamente 8 números"));

        mockMvc.perform(get("/api/v1/buscarcep/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("O CEP deve possuir exatamente 8 números"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornarNotFoundQuandoEnderecoNaoForEncontrado() throws Exception {
        String cep = "99999999";
        when(buscarCepService.buscarCep(cep)).thenThrow(new NotFoundException("Não foi possível encontrar um endereço válido"));

        mockMvc.perform(get("/api/v1/buscarcep/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Não foi possível encontrar um endereço válido"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornarServiceUnavailableQuandoGatewayFalhar() throws Exception {
        String cep = "01001000";
        when(buscarCepService.buscarCep(cep)).thenThrow(new GatewayException("Não foi possível consultar o serviço ViaCEP"));

        mockMvc.perform(get("/api/v1/buscarcep/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Não foi possível consultar o serviço ViaCEP"))
                .andExpect(jsonPath("$.status").value(503));
    }
}
