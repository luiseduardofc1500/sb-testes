package ufrn.imd.sistema_bancario.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ufrn.imd.sistema_bancario.services.exceptions.ContaNaoEncontradaException;
import ufrn.imd.sistema_bancario.services.exceptions.SaldoInsuficienteException;
import ufrn.imd.sistema_bancario.services.exceptions.ValorNaoInseridoException;

@RestControllerAdvice
public class SistemaBancarioExceptionHandler {
    private static final String MENSAGEM_ERRO_INTERNO = "Ocorreu um erro interno no sistema. Tente novamente mais tarde.";

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleContaNaoEncontradaException(ContaNaoEncontradaException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getFriendlyMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, Object>> handleAllException(Throwable ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, MENSAGEM_ERRO_INTERNO);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getFriendlyMessage());
    }

     @ExceptionHandler(ValorNaoInseridoException.class)
    public ResponseEntity<Map<String, Object>> handleValorNaoInseridoException(ValorNaoInseridoException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getFriendlyMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        message = "Campos inválidos: " + message + ".";

        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }
    

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus httpStatus, String friendlyMessage) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", httpStatus.value());
        responseBody.put("error", httpStatus.getReasonPhrase());
        responseBody.put("message", friendlyMessage);

        return ResponseEntity.status(httpStatus).body(responseBody);
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage() != null ? ex.getMessage() : MENSAGEM_ERRO_INTERNO);
    }
}
