package ufrn.imd.sistema_bancario.services.exceptions;

import ufrn.imd.sistema_bancario.SistemaBancarioBaseException;

public class ValorNaoInseridoException extends SistemaBancarioBaseException {

    @Override
    public String getFriendlyMessage() {
        return "O valor do saldo inicial não foi inserido.";
    }

    @Override
    public String getLogMessage() {
        return "Saldo não inserido pelo usuario.";
    }
    
}
