package com.devestudo.projeto_financas.enums;

public enum PaymentMethod {
    PIX("pix"),
    CREDITO ("credito"),
    DEBITO ("debito"),
    DINHEIRO ("dinheiro");


    private final String payment;

    //Construtor
    PaymentMethod(String payment) {
        this.payment = payment;
    }

    //Getter
    public String getPayment() {
        return payment;
    }
}
