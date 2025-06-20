package ufrn.imd.sistema_bancario.services.exceptions;

import ufrn.imd.sistema_bancario.SistemaBancarioBaseException;

public class ContaNaoEncontradaException extends SistemaBancarioBaseException {
    private final String numeroConta;

    public ContaNaoEncontradaException(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    @Override
    public String getFriendlyMessage() {
        return "Conta com número " + numeroConta + " não foi encontrada.";
    }

    @Override
    public String getLogMessage() {
        return "ContaNaoEncontradaException: conta " + numeroConta + " não encontrada.";
    }
}
