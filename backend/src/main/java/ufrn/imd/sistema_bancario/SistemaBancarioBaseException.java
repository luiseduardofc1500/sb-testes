package ufrn.imd.sistema_bancario;

import lombok.Getter;

@Getter
public abstract class SistemaBancarioBaseException extends RuntimeException {

    protected SistemaBancarioBaseException() {
    }

    protected SistemaBancarioBaseException(Throwable cause) {
        super(cause);
    }

    public abstract String getFriendlyMessage();

    public abstract String getLogMessage();

    @Override
    public String getMessage() {
        return getFriendlyMessage();
    }

}
