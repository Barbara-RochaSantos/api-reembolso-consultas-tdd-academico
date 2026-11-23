package br.edu.infnet.BarbaraRochaSantos.service;

/*
- Etapa 3: Classe dedicada ao cálculo de reembolso de consultas médicas
- Etapa 4: aceita Paciente na assinatura para evolução futura das regras,
- mantendo o cálculo sem depender de dados do paciente.
- Etapa 6: permite calcular reembolso obtendo o percentual de cobertura a partir de PlanoSaude
- (permitindo isolar o teste com um Stub de PlanoSaude).
- Etapa 7: suporta auditoria de reembolso (permitindo testar interações com Spy).
- Etapa 8: evita registrar auditoria quando o valor do reembolso for zero.
- Etapa 9: aplica teto máximo de R$ 150,00 por consulta antes de retornar e auditar.
- Etapa 10: registra a consulta no HistoricoConsultas quando disponível (permitindo testar com Fake).
- Etapa 11: concede bônus de +10% na cobertura (limitado a 100%) para pacientes com 2+ consultas anteriores.
 */

import br.edu.infnet.BarbaraRochaSantos.model.Consulta;
import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

import java.time.LocalDate;

public class CalculadoraReembolso {

    private static final double TETO_REEMBOLSO_CONSULTA = 150.00;
    private static final int MINIMO_CONSULTAS_FIDELIDADE = 2;
    private static final double BONUS_FIDELIDADE_PERCENTUAL = 10.0;

    private final AuditoriaReembolso auditoria;
    private final HistoricoConsultas historicoConsultas;

    public CalculadoraReembolso() {
        this.auditoria = null;
        this.historicoConsultas = null;
    }

    public CalculadoraReembolso(AuditoriaReembolso auditoria) {
        this.auditoria = auditoria;
        this.historicoConsultas = null;
    }

    public CalculadoraReembolso(AuditoriaReembolso auditoria, HistoricoConsultas historicoConsultas) {
        this.auditoria = auditoria;
        this.historicoConsultas = historicoConsultas;
    }

    public double calcularReembolso(double valorConsulta, double percentualCobertura, Paciente paciente) {
        double percentualEfetivo = calcularPercentualComFidelidade(percentualCobertura, paciente);
        double valorCalculado = valorConsulta * (percentualEfetivo / 100.0);
        double valorReembolso = Math.min(valorCalculado, TETO_REEMBOLSO_CONSULTA);

        if (historicoConsultas != null && paciente != null) {
            Consulta consultaRegistrada = new Consulta(
                    paciente,
                    valorConsulta,
                    percentualEfetivo,
                    LocalDate.now()
            );
            historicoConsultas.registrarConsulta(consultaRegistrada);
        }

        if (auditoria != null && valorReembolso > 0.0) {
            auditoria.registrarReembolso(paciente, valorReembolso);
        }

        return valorReembolso;
    }

    public double calcularReembolso(double valorConsulta, PlanoSaude planoSaude, Paciente paciente) {
        // Obtém o percentual fixo fornecido pelo PlanoSaude (ou Stub nos testes) e reaproveita o cálculo base
        double percentualCobertura = (planoSaude != null) ? planoSaude.obterPercentualCobertura() : 0.0;
        return calcularReembolso(valorConsulta, percentualCobertura, paciente);
    }

    private double calcularPercentualComFidelidade(double percentualBase, Paciente paciente) {
        if (historicoConsultas == null || paciente == null || percentualBase <= 0.0) {
            return percentualBase;
        }

        int consultasAnteriores = historicoConsultas.buscarPorPaciente(paciente).size();
        if (consultasAnteriores >= MINIMO_CONSULTAS_FIDELIDADE) {
            return Math.min(percentualBase + BONUS_FIDELIDADE_PERCENTUAL, 100.0);
        }

        return percentualBase;
    }
}