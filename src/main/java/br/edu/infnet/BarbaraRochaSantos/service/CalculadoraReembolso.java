package br.edu.infnet.BarbaraRochaSantos.service;

/*
 * Classe dedicada ao cálculo de reembolso de consultas médicas (Et:3)
 * Etapa 4: aceita Paciente na assinatura para evolução futura das regras,
 * mantendo o cálculo sem depender de dados do paciente.
 */

import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

public class CalculadoraReembolso {
    public double calcularReembolso(double valorConsulta, double percentualCobertura, Paciente paciente) {
        // 'paciente' ainda não altera o cálculo (atuação como Dummy nos testes desta etapa)
        return valorConsulta * (percentualCobertura / 100.0);
    }
}
