package com.kaua.gateway_viacep_cosignado.domain.validator;

import com.kaua.gateway_viacep_cosignado.exception.CepInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CepValidatorTest {

    private final CepValidator cepValidator = new CepValidator();

    @Test
    void deveNormalizarCepComMascara() {
        String cepComMascara = "01001-000";
        String cepNormalizado = cepValidator.normalizar(cepComMascara);

        assertEquals("01001000", cepNormalizado);
    }

    @Test
    void deveValidarCepValido() {
        assertDoesNotThrow(() -> cepValidator.validar("01001000"));
        assertDoesNotThrow(() -> cepValidator.validar("01001-000"));
    }

    @Test
    void deveLancarExcecaoQuandoCepForNuloOuVazio() {
        CepInvalidoException exceptionNulo = assertThrows(CepInvalidoException.class, () -> cepValidator.validar(null));
        assertEquals("O CEP é obrigatório", exceptionNulo.getMessage());

        CepInvalidoException exceptionVazio = assertThrows(CepInvalidoException.class, () -> cepValidator.validar("   "));
        assertEquals("O CEP é obrigatório", exceptionVazio.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCepTiverTamanhoInvalido() {
        CepInvalidoException exceptionCurto = assertThrows(CepInvalidoException.class, () -> cepValidator.validar("1234567"));
        assertEquals("O CEP deve possuir exatamente 8 números", exceptionCurto.getMessage());

        CepInvalidoException exceptionLongo = assertThrows(CepInvalidoException.class, () -> cepValidator.validar("123456789"));
        assertEquals("O CEP deve possuir exatamente 8 números", exceptionLongo.getMessage());
    }
}
