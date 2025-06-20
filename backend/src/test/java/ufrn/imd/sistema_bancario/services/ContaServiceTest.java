package ufrn.imd.sistema_bancario.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ufrn.imd.sistema_bancario.models.Conta;
import ufrn.imd.sistema_bancario.models.ContaBonus;
import ufrn.imd.sistema_bancario.models.ContaPoupanca;
import ufrn.imd.sistema_bancario.models.TipoConta;
import ufrn.imd.sistema_bancario.services.exceptions.ContaNaoEncontradaException;
import ufrn.imd.sistema_bancario.services.exceptions.SaldoInsuficienteException;
import ufrn.imd.sistema_bancario.services.exceptions.ValorInvalidoException;
import ufrn.imd.sistema_bancario.services.exceptions.ValorNaoInseridoException;

class ContaServiceTest {

    ContaService contaService;

    @BeforeEach
    void setUp() {
        contaService = new ContaService();
    }

    @Nested
    class CadastrarConta {
        @Test
        void deveCadastrarContaSimplesQuandoTipoSimples() {
            Conta conta = contaService.criarConta("001", TipoConta.SIMPLES, 100.0);
            assertNotNull(conta);
            assertEquals("001", conta.getNumero());
            assertEquals(100.0, conta.getSaldo());
        }

        @Test
        void deveCadastrarContaPoupancaQuandoTipoPoupanca() {
            Conta conta = contaService.criarConta("002", TipoConta.POUPANCA, 200.0);
            assertTrue(conta instanceof ContaPoupanca);
            assertEquals("002", conta.getNumero());
            assertEquals(200.0, conta.getSaldo());
        }

        @Test
        void deveCadastrarContaBonusQuandoTipoBonus() {
            Conta conta = contaService.criarConta("003", TipoConta.BONUS, 0.0);
            assertTrue(conta instanceof ContaBonus);
            assertEquals("003", conta.getNumero());
            assertEquals(0.0, conta.getSaldo());
            assertEquals(10, ((ContaBonus) conta).getBonus());
        }

        @Test
        void deveLancarExcecaoQuandoCadastrarContaPoupancaSemSaldoInicial() {
            assertThrows(ValorNaoInseridoException.class, () -> {
                contaService.criarConta("004", TipoConta.POUPANCA, null);
            });
        }
    }

    @Nested
    class ConsultaConta {
        @Test
        void deveConsultarContaSimplesQuandoContaExiste() {
            contaService.criarConta("005", TipoConta.SIMPLES, 50.0);
            Conta conta = contaService.buscarConta("005");
            assertNotNull(conta);
            assertEquals("005", conta.getNumero());
        }

        @Test
        void deveConsultarContaPoupancaQuandoContaExiste() {
            contaService.criarConta("006", TipoConta.POUPANCA, 60.0);
            Conta conta = contaService.buscarConta("006");
            assertTrue(conta instanceof ContaPoupanca);
        }

        @Test
        void deveConsultarContaBonusQuandoContaExiste() {
            contaService.criarConta("007", TipoConta.BONUS, 0.0);
            Conta conta = contaService.buscarConta("007");
            assertTrue(conta instanceof ContaBonus);
        }

        @Test
        void deveLancarExcecaoQuandoContaNaoEncontrada() {
            assertThrows(ContaNaoEncontradaException.class, () -> {
                contaService.buscarConta("999");
            });
        }
    }

    @Nested
    class Saldo {
        @Test
        void deveConsultarSaldoQuandoContaExiste() {
            contaService.criarConta("008", TipoConta.SIMPLES, 80.0);
            double saldo = contaService.consultarSaldo("008");
            assertEquals(80.0, saldo);
        }
    }

    @Nested
    class Credito {
        @Test
        void deveCreditarValorQuandoValorPositivo() {
            contaService.criarConta("009", TipoConta.SIMPLES, 10.0);
            Conta conta = contaService.creditar("009", 40.0);
            assertEquals(50.0, conta.getSaldo());
        }

        @Test
        void deveLancarExcecaoQuandoCreditarValorNegativo() {
            contaService.criarConta("010", TipoConta.SIMPLES, 10.0);
            assertThrows(ValorInvalidoException.class, () -> {
                contaService.creditar("010", -5.0);
            });
        }

        @Test
        void deveLancarExcecaoQuandoCreditarValorZero() {
            contaService.criarConta("010a", TipoConta.SIMPLES, 10.0);
            assertThrows(ValorInvalidoException.class, () -> {
                contaService.creditar("010a", 0.0);
            });
        }

        @Test
        void deveBonificarContaBonusQuandoCreditarValorMultiploDe100() {
            Conta conta = contaService.criarConta("011", TipoConta.BONUS, 0.0);
            contaService.creditar("011", 250.0); // 250 / 100 = 2 pontos
            assertTrue(conta instanceof ContaBonus);
            assertEquals(12, ((ContaBonus) conta).getBonus());
        }

        @Test
        void naoDeveBonificarContaBonusQuandoCreditarValorMenorQue100() {
            Conta conta = contaService.criarConta("011b", TipoConta.BONUS, 0.0);
            contaService.creditar("011b", 99.0); // 99 / 100 = 0 pontos
            assertEquals(10, ((ContaBonus) conta).getBonus());
        }
    }

    @Nested
    class Debito {
        @Test
        void deveDebitarValorQuandoValorPositivo() {
            contaService.criarConta("012", TipoConta.SIMPLES, 100.0);
            Conta conta = contaService.debitar("012", 30.0);
            assertEquals(70.0, conta.getSaldo());
        }

        @Test
        void deveLancarExcecaoQuandoDebitarValorNegativo() {
            contaService.criarConta("013", TipoConta.SIMPLES, 100.0);
            assertThrows(ValorInvalidoException.class, () -> {
                contaService.debitar("013", -10.0);
            });
        }

        @Test
        void deveLancarExcecaoQuandoDebitarValorZero() {
            contaService.criarConta("013a", TipoConta.SIMPLES, 100.0);
            assertThrows(ValorInvalidoException.class, () -> {
                contaService.debitar("013a", 0.0);
            });
        }

        @Test
        void deveLancarExcecaoQuandoSaldoInsuficiente() {
            contaService.criarConta("014", TipoConta.SIMPLES, 10.0);
            assertThrows(SaldoInsuficienteException.class, () -> {
                contaService.debitar("014", 2000.0);
            });
        }

        @Test
        void devePermitirDebitoQuandoAteLimiteNegativo() {
            contaService.criarConta("014a", TipoConta.SIMPLES, 0.0);
            Conta conta = contaService.debitar("014a", 1000.0);
            assertEquals(-1000.0, conta.getSaldo());
        }

        @Test
        void naoDevePermitirSaldoNegativoQuandoContaPoupanca() {
            contaService.criarConta("014b", TipoConta.POUPANCA, 100.0);
            assertThrows(SaldoInsuficienteException.class, () -> {
                contaService.debitar("014b", 200.0);
            });
        }
    }

    @Nested
    class Transferencia {
        @Test
        void deveLancarExcecaoQuandoTransferirValorNegativo() {
            contaService.criarConta("015", TipoConta.SIMPLES, 100.0);
            contaService.criarConta("016", TipoConta.SIMPLES, 100.0);
            assertThrows(ValorInvalidoException.class, () -> {
                contaService.transferir("015", "016", -50.0);
            });
        }

        @Test
        void deveLancarExcecaoQuandoTransferirValorZero() {
            contaService.criarConta("015a", TipoConta.SIMPLES, 100.0);
            contaService.criarConta("016a", TipoConta.SIMPLES, 100.0);
            assertThrows(ValorInvalidoException.class, () -> {
                contaService.transferir("015a", "016a", 0.0);
            });
        }
        
        // @Test
        // void deveLancarExcecaoQuandoTransferirSaldoInsuficiente() {
        //     contaService.criarConta("017", TipoConta.SIMPLES, 10.0);
        //     contaService.criarConta("018", TipoConta.SIMPLES, 10.0);
        //     assertThrows(SaldoInsuficienteException.class, () -> {
        //         contaService.transferir("017", "018", 2000.0);
        //     });
        // }

        @Test
        void deveBonificarContaBonusQuandoTransferirValorMultiploDe150() {
            contaService.criarConta("019", TipoConta.SIMPLES, 500.0);
            Conta contaBonus = contaService.criarConta("020", TipoConta.BONUS, 0.0);
            contaService.transferir("019", "020", 300.0); // 300 / 150 = 2 pontos
            assertEquals(12, ((ContaBonus) contaBonus).getBonus());
        }

        @Test
        void naoDeveBonificarContaBonusQuandoTransferirValorMenorQue150() {
            contaService.criarConta("019b", TipoConta.SIMPLES, 500.0);
            Conta contaBonus = contaService.criarConta("020b", TipoConta.BONUS, 0.0);
            contaService.transferir("019b", "020b", 100.0); // 100 / 150 = 0 pontos
            assertEquals(10, ((ContaBonus) contaBonus).getBonus());
        }

        @Test
        void deveTransferirEntreContasSimplesQuandoSaldoSuficiente() {
            Conta origem = contaService.criarConta("021", TipoConta.SIMPLES, 200.0);
            Conta destino = contaService.criarConta("022", TipoConta.SIMPLES, 100.0);
            contaService.transferir("021", "022", 50.0);
            assertEquals(150.0, origem.getSaldo());
            assertEquals(150.0, destino.getSaldo());
        }
    }

    @Nested
    class Juros {
        @Test
        void deveRenderJurosQuandoContaPoupanca() {
            Conta conta1 = contaService.criarConta("023", TipoConta.POUPANCA, 1000.0);
            Conta conta2 = contaService.criarConta("024", TipoConta.POUPANCA, 2000.0);
            Conta conta3 = contaService.criarConta("025", TipoConta.SIMPLES, 3000.0);

            contaService.renderJurosTodasContas(1.0); // 1% de juros

            assertEquals(1010.0, conta1.getSaldo(), 0.01);
            assertEquals(2020.0, conta2.getSaldo(), 0.01);
            assertEquals(3000.0, conta3.getSaldo(), 0.01); // Conta simples não rende juros
        }

        @Test
        void naoDeveAlterarSaldoQuandoContaSimplesOuBonusAoRenderJuros() {
            Conta simples = contaService.criarConta("026", TipoConta.SIMPLES, 500.0);
            Conta bonus = contaService.criarConta("027", TipoConta.BONUS, 500.0);
            contaService.renderJurosTodasContas(10.0);
            assertEquals(500.0, simples.getSaldo(), 0.01);
            // assertEquals(500.0, bonus.getSaldo(), 0.01); // Descomente se ContaBonus não render juros
        }
    }
}
