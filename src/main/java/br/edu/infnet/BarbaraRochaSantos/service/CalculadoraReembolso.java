package br.edu.infnet.BarbaraRochaSantos.service;

/*
- Etapa 3: Classe dedicada ao cálculo de reembolso de consultas médicas
- Etapa 4: aceita Paciente na assinatura para evolução futura das regras,
- mantendo o cálculo sem depender de dados do paciente.
- Etapa 6: permite calcular reembolso obtendo o percentual de cobertura a partir de PlanoSaude
- (permitindo isolar o teste com um Stub de PlanoSaude).
- Etapa 7: suporta auditoria de reembolso (permitindo testar interações com Spy).
 */

import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

public class CalculadoraReembolso {

    private final AuditoriaReembolso auditoria;

    public CalculadoraReembolso() {
        this.auditoria = null;
    }

    public CalculadoraReembolso(AuditoriaReembolso auditoria) {
        this.auditoria = auditoria;
    }

    public double calcularReembolso(double valorConsulta, double percentualCobertura, Paciente paciente) {
        // 'paciente' ainda não altera o cálculo (atuação como Dummy nos testes desta etapa)
        double valorReembolso = valorConsulta * (percentualCobertura / 100.0);

        if (auditoria != null) {
            auditoria.registrarReembolso(paciente, valorReembolso);
        }

        return valorReembolso;
    }

    public double calcularReembolso(double valorConsulta, PlanoSaude planoSaude, Paciente paciente) {
        // Obtém o percentual fixo fornecido pelo PlanoSaude (ou Stub nos testes) e reaproveita o cálculo base
        double percentualCobertura = (planoSaude != null) ? planoSaude.obterPercentualCobertura() : 0.0;
        return calcularReembolso(valorConsulta, percentualCobertura, paciente);
    }
}