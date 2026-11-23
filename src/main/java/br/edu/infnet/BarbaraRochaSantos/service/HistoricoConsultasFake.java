package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Consulta;
import br.edu.infnet.BarbaraRochaSantos.model.Paciente;

import java.util.ArrayList;
import java.util.List;

/**
 * Dublê do tipo FAKE --> implementação funcional em memória de HistoricoConsultas.
 * Armazena as consultas em uma lista local para permitir testes rápidos de escrita e leitura
 * sem necessidade de banco de dados.
 */
public class HistoricoConsultasFake implements HistoricoConsultas {

    private final List<Consulta> consultas = new ArrayList<>();

    @Override
    public void registrarConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    @Override
    public List<Consulta> buscarPorPaciente(Paciente paciente) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getPaciente().getId().equals(paciente.getId())) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }
}