package com.kaua.gateway_viacep_cosignado.service;

import com.kaua.gateway_viacep_cosignado.domain.gateway.BuscarCepGateway;
import com.kaua.gateway_viacep_cosignado.domain.validator.CepValidator;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepResponseDto;
import com.kaua.gateway_viacep_cosignado.dto.viacep.ViaCepReturnDto;
import com.kaua.gateway_viacep_cosignado.exception.CepInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarCepServiceTest {

    @Mock
    private BuscarCepGateway buscarCepGateway;

    @Mock
    private CepValidator cepValidator;

    @InjectMocks
    private BuscarCepService buscarCepService;

    @Test
    void deveBuscarCepComSucessoELogradouroEmMinusculo() {
        String cep = "01001-000";
        String cepNormalizado = "01001000";

        ViaCepResponseDto responseDto = new ViaCepResponseDto(
                "01001-000", "Praça da Sé", "lado ímpar", "Sé", "São Paulo", "SP", "3550308", "1004", "11", "7107", "false"
        );
        ViaCepReturnDto returnDto = new ViaCepReturnDto(responseDto);

        when(cepValidator.normalizar(cep)).thenReturn(cepNormalizado);
        when(buscarCepGateway.buscarCep(cepNormalizado)).thenReturn(returnDto);

        ViaCepReturnDto resultado = buscarCepService.buscarCep(cep);

        assertNotNull(resultado);
        assertEquals("praça da sé", resultado.getLogradouro());
        verify(cepValidator).normalizar(cep);
        verify(cepValidator).validar(cepNormalizado);
        verify(buscarCepGateway).buscarCep(cepNormalizado);
    }

    @Test
    void deveBuscarCepComSucessoQuandoLogradouroForNulo() {
        String cep = "01001-000";
        String cepNormalizado = "01001000";

        ViaCepResponseDto responseDto = new ViaCepResponseDto(
                "01001-000", null, "lado ímpar", "Sé", "São Paulo", "SP", "3550308", "1004", "11", "7107", "false"
        );
        ViaCepReturnDto returnDto = new ViaCepReturnDto(responseDto);

        when(cepValidator.normalizar(cep)).thenReturn(cepNormalizado);
        when(buscarCepGateway.buscarCep(cepNormalizado)).thenReturn(returnDto);

        ViaCepReturnDto resultado = buscarCepService.buscarCep(cep);

        assertNotNull(resultado);
        assertNull(resultado.getLogradouro());
        verify(cepValidator).normalizar(cep);
        verify(cepValidator).validar(cepNormalizado);
        verify(buscarCepGateway).buscarCep(cepNormalizado);
    }

    @Test
    void devePropagarExcecaoQuandoCepForInvalido() {
        String cep = "invalido";

        doThrow(new CepInvalidoException("O CEP deve possuir exatamente 8 números"))
                .when(cepValidator).normalizar(cep);

        assertThrows(CepInvalidoException.class, () -> buscarCepService.buscarCep(cep));

        verify(buscarCepGateway, never()).buscarCep(anyString());
    }
}
