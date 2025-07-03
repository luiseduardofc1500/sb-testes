package ufrn.imd.sistema_bancario.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import ufrn.imd.sistema_bancario.services.exceptions.ValorInvalidoException;

@Getter
@Setter
public class Conta {
    private String numero;
    private double saldo;
    //private String idoEUmBobao;

    public Conta(String numero) {
        this.numero = numero;
        this.saldo = 0;
    }

    public void creditar(double valor) {
        if (valor <= 0) {
            throw new ValorInvalidoException();
        }
        this.saldo += valor;
    }

    public void debitar(double valor) {
        if (valor <= 0) {
            throw new ValorInvalidoException();
        }
        this.saldo -= valor;
    }

    @JsonIgnore
    public double getLimiteNegativoMaximo() {
        return -1000;
    }
}
