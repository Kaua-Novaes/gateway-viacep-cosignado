package com.kaua.gateway_viacep_cosignado.infrastructure.client;

import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.GatewayException;
import com.kaua.gateway_viacep_cosignado.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class ViaCepClientTest {

    private ViaCepClient viaCepClient;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://viacep.com.br/ws");
        server = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();
        viaCepClient = new ViaCepClient(restClient);
    }

    @Test
    void deveBuscarCepComSucesso() {
        String cep = "01001000";
        String jsonResponse = """
                {
                    "cep": "01001-000",
                    "logradouro": "Praça da Sé",
                    "complemento": "lado ímpar",
                    "bairro": "Sé",
                    "localidade": "São Paulo",
                    "uf": "SP",
                    "ibge": "3550308",
                    "gia": "1004",
                    "ddd": "11",
                    "siafi": "7107"
                }
                """;

        this.server.expect(requestTo("https://viacep.com.br/ws/01001000/json"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ViaCepReturnDto returnDto = viaCepClient.buscarCep(cep);

        assertNotNull(returnDto);
        assertEquals("01001-000", returnDto.getCep());
        assertEquals("Praça da Sé", returnDto.getLogradouro());
        assertEquals("lado ímpar", returnDto.getComplemento());
        assertEquals("Sé", returnDto.getBairro());
        assertEquals("São Paulo", returnDto.getLocalidade());
        this.server.verify();
    }

    @Test
    void deveLancarNotFoundExceptionQuandoViaCepRetornarErroTrue() {
        String cep = "99999999";
        String jsonResponse = """
                {
                    "erro": "true"
                }
                """;

        this.server.expect(requestTo("https://viacep.com.br/ws/99999999/json"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> viaCepClient.buscarCep(cep));
        assertEquals("Não foi possível encontrar um endereço válido", exception.getMessage());
        this.server.verify();
    }

    @Test
    void deveLancarGatewayExceptionQuandoViaCepRetornarNullOuVazio() {
        String cep = "01001000";

        this.server.expect(requestTo("https://viacep.com.br/ws/01001000/json"))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        GatewayException exception = assertThrows(GatewayException.class, () -> viaCepClient.buscarCep(cep));
        assertEquals("O ViaCEP retornou uma resposta vazia", exception.getMessage());
        this.server.verify();
    }

    @Test
    void deveLancarGatewayExceptionQuandoOcorrerErroNoServico() {
        String cep = "01001000";

        this.server.expect(requestTo("https://viacep.com.br/ws/01001000/json"))
                .andRespond(withServerError());

        GatewayException exception = assertThrows(GatewayException.class, () -> viaCepClient.buscarCep(cep));
        assertEquals("Não foi possível consultar o serviço ViaCEP", exception.getMessage());
        this.server.verify();
    }
}
