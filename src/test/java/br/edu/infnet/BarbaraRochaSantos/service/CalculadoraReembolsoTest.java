package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Paciente;
import br.edu.infnet.BarbaraRochaSantos.model.Consulta;
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

    // STUB DE PLANO DE SAÚDE
    @Test
    @DisplayName("Deve calcular reembolso usando percentual retornado por Stub de Plano de Saúde")
    void deveCalcularReembolsoComStub() {
        PlanoSaude planoStub = new PlanoSaudeStub(70.0);
        Paciente pacienteDummy = criarPacienteDummy();

        double reembolso = calculadora.calcularReembolso(200.00, planoStub, pacienteDummy);

        assertEquals(140.00, reembolso);
    }

    // SPY DE AUDITORIA DE REEMBOLSO
    @Test
    @DisplayName("Deve registrar chamada na auditoria ao calcular reembolso usando Spy")
    void deveRegistrarAuditoriaComSpy() {
        AuditoriaReembolsoSpy auditoriaSpy = new AuditoriaReembolsoSpy();
        CalculadoraReembolso calculadoraComAuditoria = new CalculadoraReembolso(auditoriaSpy);
        Paciente pacienteDummy = criarPacienteDummy();
        PlanoSaude planoStub = new PlanoSaudeStub(70.0);

        double reembolso = calculadoraComAuditoria.calcularReembolso(200.00, planoStub, pacienteDummy);

        assertEquals(140.00, reembolso);
        assertEquals(1, auditoriaSpy.getQuantidadeChamadas());
        assertEquals(pacienteDummy, auditoriaSpy.getUltimoPaciente());
        assertEquals(140.00, auditoriaSpy.getUltimoValorReembolso());
    }

    // MOCK DE AUDITORIA COM MOCKITO
    @Test
    @DisplayName("Deve verificar chamada na auditoria ao calcular reembolso usando Mock")
    void deveVerificarAuditoriaComMock() {
        AuditoriaReembolso auditoriaMock = org.mockito.Mockito.mock(AuditoriaReembolso.class);
        CalculadoraReembolso calculadoraComMock = new CalculadoraReembolso(auditoriaMock);
        Paciente pacienteDummy = criarPacienteDummy();
        PlanoSaude planoStub = new PlanoSaudeStub(70.0);

        double reembolso = calculadoraComMock.calcularReembolso(200.00, planoStub, pacienteDummy);

        assertEquals(140.00, reembolso);
        org.mockito.Mockito.verify(auditoriaMock, org.mockito.Mockito.times(1))
                .registrarReembolso(pacienteDummy, 140.00);
    }

    @Test
    @DisplayName("Não deve interagir com auditoria quando valor da consulta for zero usando Mock")
    void naoDeveAuditarConsultaZeroComMock() {
        AuditoriaReembolso auditoriaMock = org.mockito.Mockito.mock(AuditoriaReembolso.class);
        CalculadoraReembolso calculadoraComMock = new CalculadoraReembolso(auditoriaMock);
        Paciente pacienteDummy = criarPacienteDummy();
        PlanoSaude planoStub = new PlanoSaudeStub(70.0);

        double reembolso = calculadoraComMock.calcularReembolso(0.0, planoStub, pacienteDummy);

        assertEquals(0.0, reembolso);
        org.mockito.Mockito.verifyNoInteractions(auditoriaMock);
    }

    // TETO DE REEMBOLSO POR CONSULTA (ETAPA 9)
    @Test
    @DisplayName("Deve limitar o reembolso ao teto máximo de R$ 150 por consulta")
    void deveAplicarTetoMaximoReembolso() {
        AuditoriaReembolso auditoriaMock = org.mockito.Mockito.mock(AuditoriaReembolso.class);
        CalculadoraReembolso calculadoraComTeto = new CalculadoraReembolso(auditoriaMock);
        Paciente pacienteDummy = criarPacienteDummy();
        PlanoSaude planoStub = new PlanoSaudeStub(80.0);

        // 80% de R$ 300,00 seria R$ 240,00, mas deve respeitar o teto de R$ 150,00
        double reembolso = calculadoraComTeto.calcularReembolso(300.00, planoStub, pacienteDummy);

        assertEquals(150.00, reembolso);
        org.mockito.Mockito.verify(auditoriaMock, org.mockito.Mockito.times(1))
                .registrarReembolso(pacienteDummy, 150.00);
    }

    // HISTÓRICO DE CONSULTAS COM FAKE (ETAPA 10)
    @Test
    @DisplayName("Deve registrar consulta no histórico Fake ao calcular reembolso")
    void deveRegistrarConsultaNoHistoricoFake() {
        HistoricoConsultas historicoFake = new HistoricoConsultasFake();
        AuditoriaReembolso auditoriaMock = org.mockito.Mockito.mock(AuditoriaReembolso.class);
        CalculadoraReembolso calculadoraCompleta = new CalculadoraReembolso(auditoriaMock, historicoFake);
        Paciente pacienteDummy = criarPacienteDummy();
        PlanoSaude planoStub = new PlanoSaudeStub(70.0);

        double reembolso = calculadoraCompleta.calcularReembolso(200.00, planoStub, pacienteDummy);

        assertEquals(140.00, reembolso);
        assertEquals(1, historicoFake.buscarPorPaciente(pacienteDummy).size());
        assertEquals(200.00, historicoFake.buscarPorPaciente(pacienteDummy).get(0).getValor());
    }

    // BÔNUS DE FIDELIDADE COM HISTÓRICO FAKE (ETAPA 11)
    @Test
    @DisplayName("Deve aplicar bônus de 10% na cobertura quando paciente possuir 2 ou mais consultas anteriores")
    void deveAplicarBonusFidelidadeComHistorico() {
        HistoricoConsultas historicoFake = new HistoricoConsultasFake();
        AuditoriaReembolso auditoriaMock = org.mockito.Mockito.mock(AuditoriaReembolso.class);
        Paciente pacienteFiel = new Paciente("pac-fiel", "Ana Paula");
        PlanoSaude planoStub = new PlanoSaudeStub(50.0);

        // Pré-popula o Fake com 2 consultas anteriores do mesmo paciente
        historicoFake.registrarConsulta(new Consulta(pacienteFiel, 100.00, 50.0, java.time.LocalDate.now().minusDays(20)));
        historicoFake.registrarConsulta(new Consulta(pacienteFiel, 120.00, 50.0, java.time.LocalDate.now().minusDays(10)));

        CalculadoraReembolso calculadoraFidelidade = new CalculadoraReembolso(auditoriaMock, historicoFake);

        // Cobertura base = 50% + 10% bônus = 60% de R$ 200,00 = R$ 120,00
        double reembolso = calculadoraFidelidade.calcularReembolso(200.00, planoStub, pacienteFiel);

        assertEquals(120.00, reembolso);
        org.mockito.Mockito.verify(auditoriaMock, org.mockito.Mockito.times(1))
                .registrarReembolso(pacienteFiel, 120.00);
        // Também deve ter registrado a 3ª consulta no histórico
        assertEquals(3, historicoFake.buscarPorPaciente(pacienteFiel).size());
    }

    // VALIDAÇÕES DE ENTRADA E EXCEÇÕES (ETAPA 12)
    @Test
    @DisplayName("Deve lançar exceção quando o valor da consulta for negativo")
    void deveLancarExcecaoValorConsultaNegativo() {
        Paciente pacienteDummy = criarPacienteDummy();

        assertThrows(IllegalArgumentException.class, () ->
                calculadora.calcularReembolso(-50.00, 70.0, pacienteDummy)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o percentual de cobertura for menor que 0 ou maior que 100")
    void deveLancarExcecaoPercentualInvalido() {
        Paciente pacienteDummy = criarPacienteDummy();

        assertThrows(IllegalArgumentException.class, () ->
                calculadora.calcularReembolso(200.00, -10.0, pacienteDummy)
        );
        assertThrows(IllegalArgumentException.class, () ->
                calculadora.calcularReembolso(200.00, 120.0, pacienteDummy)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o paciente for nulo")
    void deveLancarExcecaoPacienteNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                calculadora.calcularReembolso(200.00, 70.0, null)
        );
    }

}