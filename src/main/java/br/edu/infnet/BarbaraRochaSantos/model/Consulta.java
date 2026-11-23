package br.edu.infnet.BarbaraRochaSantos.model;

import java.time.LocalDate;

/**
 * Entidade de domínio que representa uma consulta médica realizada por um paciente.
 */
public class Consulta {

    private final Paciente paciente;
    private final double valor;
    private final double percentualCobertura;
    private final LocalDate data;

    public Consulta(Paciente paciente, double valor, double percentualCobertura, LocalDate data) {
        this.paciente = paciente;
        this.valor = valor;
        this.percentualCobertura = percentualCobertura;
        this.data = data;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public double getValor() {
        return valor;
    }

    public double getPercentualCobertura() {
        return percentualCobertura;
    }

    public LocalDate getData() {
        return data;
    }
}