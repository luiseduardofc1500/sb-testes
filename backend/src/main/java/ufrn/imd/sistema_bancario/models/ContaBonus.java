package ufrn.imd.sistema_bancario.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaBonus  extends Conta{
    private int bonus;

    public ContaBonus(String numero) {
        super(numero);
        this.bonus = 10;
    }

    public void adicionarPontuacao(int pontosGanhos){
        this.bonus += pontosGanhos;
    }
}