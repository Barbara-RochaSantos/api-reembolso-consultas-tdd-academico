package br.edu.infnet.BarbaraRochaSantos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculadora De Reembolso")
class CalculadoraReembolsoTest {
    @Test
    @DisplayName("Deve calcular reembolso básico de R$140 para consulta de R$200 com 70% de cobertura")
    void deveCalcularReembolsoBasico() {
        CalculadoraReembolso calculadora = new CalculadoraReembolso();
        double reembolso = calculadora.calcularReembolso(200.00, 70.0);
        assertEquals(140.00, reembolso);
    }
}