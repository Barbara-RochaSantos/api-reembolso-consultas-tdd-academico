package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*Testes unitários da classe CalculadoraReembolso.
- Valida o cálculo básico e os cenários de borda (0 e 100) sem depender de outra classe ou serviço externo.
- Neste momento será introduzido um Dummy de Paciente para satisfazer a assinatura do metodo de cálculo
- sem adicionar regras de negócio dependentes do paciente nesta etapa. */

@DisplayName("Calculadora De Reembolso")
class CalculadoraReembolsoTest {

    private CalculadoraReembolso calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraReembolso();
    }

    private Paciente criarPacienteDummy() {
        return new Paciente("dummy-id", "Paciente Dummy");
    }

    @Test
    @DisplayName("Deve calcular reembolso básico de R$140 usando Dummy de Paciente")
    void deveCalcularReembolsoComDummy() {
        Paciente pacienteDummy = criarPacienteDummy();
        double reembolso = calculadora.calcularReembolso(200.00, 70.0, pacienteDummy);
        assertEquals(140.00, reembolso);
    }

    // CONSULTA GRATUITA
    @Test
    @DisplayName("Deve retornar R$ 0 quando o valor da consulta for R$ 0 com Dummy de Paciente")
    void deveRetornarZeroConsultaComDummy() {
        Paciente pacienteDummy = criarPacienteDummy();
        double reembolso = calculadora.calcularReembolso(0.0, 70.0, pacienteDummy);
        assertEquals(0.0, reembolso);
    }

    // COBERTURA ZERO
    @Test
    @DisplayName("Deve retornar R$ 0 quando a cobertura for 0% com Dummy de Paciente")
    void deveRetornarZeroCoberturaComDummy() {
        Paciente pacienteDummy = criarPacienteDummy();
        double reembolso = calculadora.calcularReembolso(200.0, 0.0, pacienteDummy);
        assertEquals(0.0, reembolso);
    }

    // COBERTURA TOTAL
    @Test
    @DisplayName("Deve reembolsar valor integral quando a cobertura for 100% com Dummy de Paciente")
    void deveReembolsarTotalComDummy() {
        Paciente pacienteDummy = criarPacienteDummy();
        double reembolso = calculadora.calcularReembolso(100.0, 100.0, pacienteDummy);
        assertEquals(100.0, reembolso);
    }
}