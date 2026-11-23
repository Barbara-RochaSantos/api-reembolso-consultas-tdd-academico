package br.edu.infnet.BarbaraRochaSantos.model;

/* Entidade de domínio que representa um paciente.
* Nos testes atuais, atua como Dummy para satisfazer a assinatura do cálculo.
* */

public class Paciente {
    private final String id;
    private final String nome;

    public Paciente (String id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public String getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

}
