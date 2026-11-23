package br.edu.infnet.BarbaraRochaSantos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*Testes unitários da classe CalculadoraReembolso.
* Valida o cálculo básico e os cenários de borda (0 e 100) sem depender de outra classe ou serviço externo. */

@DisplayName("Calculadora De Reembolso")
class CalculadoraReembolsoTest {
    @Test
    @DisplayName("Deve calcular reembolso básico de R$140 para consulta de R$200 com 70% de cobertura")
    void deveCalcularReembolsoBasico() {
        CalculadoraReembolso calculadora = new CalculadoraReembolso();
        double reembolso = calculadora.calcularReembolso(200.00, 70.0);
        assertEquals(140.00, reembolso);
    }

    // CONSULTA GRATUITA
    @Test
    @DisplayName("Deve retornar R$0 de reembolso quando a consulta for R$0.")
    void deveRetornarZeroParaConsultaGratuita(){
            CalculadoraReembolso calculadora = new CalculadoraReembolso();
            double reembolso = calculadora.calcularReembolso(0.0, 70.0);
            assertEquals(0.0, reembolso);
    }

    // COBERTURA ZERO
    @Test
    @DisplayName("Deve retornar R$ 0 quando a cobertura do plano for 0%")
    void deveRetornarZeroParaCoberturaZero() {
        CalculadoraReembolso calculadora = new CalculadoraReembolso();
        double reembolso = calculadora.calcularReembolso(200.0, 0.0);
        assertEquals(0.0, reembolso);
    }

    // COBERTURA TOTAL
    @Test
    @DisplayName("Deve reembolsar o valor integral quando a cobertura for 100%")
    void deveReembolsarTotalParaCoberturaCem() {
        CalculadoraReembolso calculadora = new CalculadoraReembolso();
        double reembolso = calculadora.calcularReembolso(100.0, 100.0);
        assertEquals(100.0, reembolso);
    }
}