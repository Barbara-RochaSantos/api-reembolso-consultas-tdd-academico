package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Consulta;
import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

import java.util.List;

/**
 * Contrato para armazenamento e consulta do histórico de consultas médicas.
 */

public interface HistoricoConsultas {

    void registrarConsulta(Consulta consulta);

    List<Consulta> buscarPorPaciente(Paciente paciente);
}