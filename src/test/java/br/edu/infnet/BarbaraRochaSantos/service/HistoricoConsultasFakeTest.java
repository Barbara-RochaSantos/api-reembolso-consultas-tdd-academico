package br.edu.infnet.BarbaraRochaSantos.service;

import br.edu.infnet.BarbaraRochaSantos.model.Consulta;
import br.edu.infnet.BarbaraRochaSantos.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Testes unitários do HistoricoConsultasFake (Et 5).
- Valida o armazenamento e a consulta em memória do histórico de consultas de pacientes,
- simulando um repositório real sem depender de banco de dados. */

@DisplayName("Histórico de Consultas (Fake)")
class HistoricoConsultasFakeTest {

    private HistoricoConsultas historico;
    private Paciente pacienteFakenilson;
    private Paciente pacienteMaria;

    @BeforeEach
    void setUp() {
        historico = new HistoricoConsultasFake();
        pacienteFakenilson = new Paciente("pac-01", "Fakenilson Barbosa");
        pacienteMaria = new Paciente("pac-02", "Maria Faketínia Jésus");
    }

    @Test
    @DisplayName("Deve registrar e retornar as consultas de um paciente específico")
    void deveRegistrarEConsultarPorPaciente() {
        Consulta consulta1 = new Consulta(pacienteFakenilson, 200.00, 70.0, LocalDate.of(2026, 11, 10));
        Consulta consulta2 = new Consulta(pacienteFakenilson, 300.00, 80.0, LocalDate.of(2026, 11, 20));
        Consulta consultaOutra = new Consulta(pacienteMaria, 150.00, 50.0, LocalDate.of(2026, 11, 21));

        historico.registrarConsulta(consulta1);
        historico.registrarConsulta(consulta2);
        historico.registrarConsulta(consultaOutra);

        List<Consulta> consultasFakenilson = historico.buscarPorPaciente(pacienteFakenilson);

        assertEquals(2, consultasFakenilson.size());
        assertTrue(consultasFakenilson.contains(consulta1));
        assertTrue(consultasFakenilson.contains(consulta2));
        assertFalse(consultasFakenilson.contains(consultaOutra));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o paciente não possuir consultas")
    void deveRetornarListaVaziaSemConsultas() {
        List<Consulta> consultas = historico.buscarPorPaciente(pacienteFakenilson);

        assertNotNull(consultas);
        assertTrue(consultas.isEmpty());
    }
}