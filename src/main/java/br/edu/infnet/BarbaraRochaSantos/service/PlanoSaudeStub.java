package br.edu.infnet.BarbaraRochaSantos.service;

/**
 * Dublê STUB: retorna um percentual de cobertura fixo pré-configurado para
 * isolar o teste da CalculadoraReembolso de regras externas de plano de saúde.
 */
public class PlanoSaudeStub implements PlanoSaude {

    private final double percentualCobertura;

    public PlanoSaudeStub(double percentualCobertura) {
        this.percentualCobertura = percentualCobertura;
    }

    @Override
    public double obterPercentualCobertura() {
        return percentualCobertura;
    }
}