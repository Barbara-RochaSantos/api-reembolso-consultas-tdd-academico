package br.edu.infnet.BarbaraRochaSantos.service;

/*
 * Classe dedicada ao cálculo de reembolso de consultas médicas (Ex:3)
 * É isolada de dependências externas para facilitar os testes unitários. */

public class CalculadoraReembolso {
    public double calcularReembolso(double valorConsulta, double percentualCobertura) {
        return valorConsulta * (percentualCobertura / 100.0);
    }
}
