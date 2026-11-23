package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

/**
 * Contrato para registro/auditoria de cálculos de reembolso realizados.
 */
public interface AuditoriaReembolso {

    void registrarReembolso(Paciente paciente, double valorReembolso);
}