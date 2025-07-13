package ufrn.imd.sistema_bancario.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import ufrn.imd.sistema_bancario.dto.ContaResponseDto;
import ufrn.imd.sistema_bancario.models.Conta;
import ufrn.imd.sistema_bancario.models.ContaBonus;
import ufrn.imd.sistema_bancario.models.ContaPoupanca;
import ufrn.imd.sistema_bancario.models.TipoConta;
import ufrn.imd.sistema_bancario.services.exceptions.ContaNaoEncontradaException;
import ufrn.imd.sistema_bancario.services.exceptions.SaldoInsuficienteException;
import ufrn.imd.sistema_bancario.services.exceptions.ValorInvalidoException;
import ufrn.imd.sistema_bancario.services.exceptions.ValorNaoInseridoException;

@Service
public class ContaService {

    private final Map<String, Conta> contas = new HashMap<>();

    public Conta criarConta(String numeroConta, TipoConta tipoConta, Double saldoInicial) {
        Conta novaConta;

        if (tipoConta == TipoConta.BONUS) {
            novaConta = criarContaBonus(numeroConta);
        } else if (tipoConta == TipoConta.POUPANCA) {
            novaConta = criarContaPoupanca(numeroConta, saldoInicial);
        } else {
            novaConta = criarContaSimples(numeroConta, saldoInicial);
        }

        contas.put(numeroConta, novaConta);

        return novaConta;
    }

    public Conta buscarConta(String numeroConta) {
        Conta conta = contas.get(numeroConta);
        if (conta == null) {
            throw new ContaNaoEncontradaException(numeroConta);
        }
        return conta;
    }

    public ContaResponseDto consultarDadosConta(String numeroConta) {
        Conta conta = buscarConta(numeroConta);
        TipoConta tipoConta = obterTipoConta(conta);
        Double bonus = (conta instanceof ContaBonus contaBonus) ? (double) contaBonus.getBonus() : null;

        return new ContaResponseDto(
            conta.getNumero(),
            tipoConta,
            conta.getSaldo(),
            bonus
        );
    }

    public double consultarSaldo(String numeroConta) {
        Conta conta = buscarConta(numeroConta);
        return conta.getSaldo();
    }

    public Conta creditar(String numeroConta, double valor) {

        if (valor <= 0) {
            throw new ValorInvalidoException();
        }

        Conta conta = buscarConta(numeroConta);
        // Erro de lógica: não modifica o saldo
        // conta.creditar(valor);

        adicionarPontuacaoBonusSeContaBonus(conta, valor, 100);

        return conta;
    }

    public Conta debitar(String numeroConta, double valor) {

        if (valor <= 0) {
            throw new ValorInvalidoException();
        }
        Conta conta = buscarConta(numeroConta);
        verificarSaldoSuficiente(conta, valor);
        conta.debitar(valor);

        return conta;
    }

    public Conta transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        Conta contaOrigem = buscarConta(numeroContaOrigem);
        Conta contaDestino = buscarConta(numeroContaDestino);
        contaOrigem.debitar(valor);
        contaDestino.creditar(valor);

        adicionarPontuacaoBonusSeContaBonus(contaDestino, valor, 150);

        return contaOrigem;
    }

    public void renderJurosTodasContas(double taxa) {
        for (Conta conta : contas.values()) {
            renderJurosConta(conta.getNumero(), taxa);
        }
    }

    private void verificarSaldoSuficiente(Conta conta, Double valorRequerido) {
        double novoSaldo = conta.getSaldo() - valorRequerido;
        if (novoSaldo < conta.getLimiteNegativoMaximo()) {
            throw new SaldoInsuficienteException(conta.getNumero());
        }
    }

    private void adicionarPontuacaoBonusSeContaBonus(Conta contaDestino, double valor, double referencia) {
        if (contaDestino instanceof ContaBonus contaBonus) {
            int pontos = (int) (valor / referencia);
            contaBonus.adicionarPontuacao(pontos);
        }
    }

    private void renderJurosConta(String numeroConta, double taxa) {
        Conta conta = buscarConta(numeroConta);
        if (conta instanceof ContaPoupanca contaPoupanca) {
            contaPoupanca.renderJuros(taxa);
        }
    }

    private ContaPoupanca criarContaPoupanca(String numeroConta, Double saldoInicial) {
        if (saldoInicial == null) {
            throw new ValorNaoInseridoException();
        }
        ContaPoupanca novaContaPoupanca = new ContaPoupanca(numeroConta);
        novaContaPoupanca.setSaldo(saldoInicial);

        return novaContaPoupanca;
    }

    private ContaBonus criarContaBonus(String numeroConta) {
        return new ContaBonus(numeroConta);
    }
    
    private Conta criarContaSimples(String numeroConta, Double saldoInicial) {
        Conta novaConta = new Conta(numeroConta);
        novaConta.setSaldo(saldoInicial);

        contas.put(numeroConta, novaConta);
        return novaConta;
    }
    
    public TipoConta obterTipoConta(Conta conta) {
        if (conta instanceof ContaBonus) {
            return TipoConta.BONUS;
        } else if (conta instanceof ContaPoupanca) {
            return TipoConta.POUPANCA;
        } else {
            return TipoConta.SIMPLES;
        }
    }
}
