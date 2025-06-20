package ufrn.imd.sistema_bancario.services.exceptions;

import ufrn.imd.sistema_bancario.SistemaBancarioBaseException;

public class SaldoInsuficienteException extends SistemaBancarioBaseException {
    private final String numeroConta;

    public SaldoInsuficienteException(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    @Override
    public String getFriendlyMessage() {
        return "Saldo insuficiente na conta de número " + numeroConta + ".";
    }

    @Override
    public String getLogMessage() {
        return "SaldoInsuficienteException: saldo insuficiente na conta " + numeroConta + ".";
    }
}
