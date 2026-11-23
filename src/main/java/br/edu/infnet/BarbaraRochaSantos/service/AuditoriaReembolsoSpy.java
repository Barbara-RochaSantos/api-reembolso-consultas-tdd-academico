package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

/**
 * Dublê do tipo SPY: registra chamadas realizadas em registrarReembolso
 * para permitir verificar interações nos testes unitários.
 */
public class AuditoriaReembolsoSpy implements AuditoriaReembolso {

    private int quantidadeChamadas = 0;
    private Paciente ultimoPaciente;
    private double ultimoValorReembolso;

    @Override
    public void registrarReembolso(Paciente paciente, double valorReembolso) {
        this.quantidadeChamadas++;
        this.ultimoPaciente = paciente;
        this.ultimoValorReembolso = valorReembolso;
    }

    public int getQuantidadeChamadas() {
        return quantidadeChamadas;
    }

    public Paciente getUltimoPaciente() {
        return ultimoPaciente;
    }

    public double getUltimoValorReembolso() {
        return ultimoValorReembolso;
    }
}