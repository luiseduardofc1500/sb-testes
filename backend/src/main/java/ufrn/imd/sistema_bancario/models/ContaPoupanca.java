package ufrn.imd.sistema_bancario.models;

import jakarta.validation.constraints.Positive;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String numeroConta) {
        super(numeroConta);
    }

    public void renderJuros(@Positive double taxaPercentual) {
        double saldoAtual = this.getSaldo();
        double rendimento = saldoAtual * (taxaPercentual / 100.0);

        if (rendimento > 0) {
            this.creditar(rendimento);
        }
    }

    @Override
    public double getLimiteNegativoMaximo() {
        return 0;
    }
}
