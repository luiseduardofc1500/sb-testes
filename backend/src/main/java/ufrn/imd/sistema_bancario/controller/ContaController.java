package ufrn.imd.sistema_bancario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ufrn.imd.sistema_bancario.dto.ContaDto;
import ufrn.imd.sistema_bancario.dto.ContaResponseDto;
import ufrn.imd.sistema_bancario.dto.CreditarRequest;
import ufrn.imd.sistema_bancario.dto.DebitarRequest;
import ufrn.imd.sistema_bancario.dto.RenderJurosRequest;
import ufrn.imd.sistema_bancario.dto.RenderJurosResponse;
import ufrn.imd.sistema_bancario.dto.SaldoDto;
import ufrn.imd.sistema_bancario.dto.TransferirRequest;
import ufrn.imd.sistema_bancario.models.Conta;
import ufrn.imd.sistema_bancario.services.ContaService;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Conta> cadastrarConta(@Valid @RequestBody ContaDto contaCreate) {
        Conta novaConta = contaService.criarConta(contaCreate.numeroConta(), contaCreate.tipoConta(), contaCreate.saldoInicial());
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<Conta> buscarConta(@PathVariable String numeroConta) {
        Conta conta = contaService.buscarConta(numeroConta);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/{numeroConta}/saldo")
    public ResponseEntity<SaldoDto> consultarSaldo(@PathVariable String numeroConta) {
        double saldo = contaService.consultarSaldo(numeroConta);
        SaldoDto saldoDto = new SaldoDto(saldo);

        return ResponseEntity.ok(saldoDto);
    }

    @PostMapping("/{numeroConta}/creditar")
    public ResponseEntity<Conta> creditarValor(@PathVariable String numeroConta, @Valid @RequestBody CreditarRequest creditar) {
        Conta contaAtualizada = contaService.creditar(numeroConta, creditar.valor());
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/{numeroConta}/debitar")
    public ResponseEntity<Conta> debitarValor(@PathVariable String numeroConta, @Valid @RequestBody DebitarRequest debitar) {
        Conta contaAtualizada = contaService.debitar(numeroConta, debitar.valor());
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/{numeroContaOrigem}/transferir/{numeroContaDestino}")
    public ResponseEntity<Conta> transferirValor(@PathVariable String numeroContaOrigem, @PathVariable String numeroContaDestino, @Valid @RequestBody TransferirRequest transferir) {
        Conta contaAtualizada = contaService.transferir(numeroContaOrigem, numeroContaDestino, transferir.valor());
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/render-juros-todas-as-contas")
    public ResponseEntity<RenderJurosResponse> renderJurosTodasContas(@Valid @RequestBody RenderJurosRequest renderJuros) {
        contaService.renderJurosTodasContas(renderJuros.taxaDeJuros());
        RenderJurosResponse renderJurosResponse = new RenderJurosResponse("Juros aplicados a todas as contas poupança com sucesso.");
        return ResponseEntity.ok(renderJurosResponse);
    }

    @GetMapping("/{numeroConta}/dados")
    public ResponseEntity<ContaResponseDto> consultarDadosConta(@PathVariable String numeroConta) {
        ContaResponseDto response = contaService.consultarDadosConta(numeroConta);
        return ResponseEntity.ok(response);
    }
}
