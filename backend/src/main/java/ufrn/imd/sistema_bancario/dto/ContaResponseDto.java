package ufrn.imd.sistema_bancario.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import ufrn.imd.sistema_bancario.models.TipoConta;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContaResponseDto(
    String numeroConta,
    TipoConta tipoConta,
    Double saldo,
    Double bonus
) 
{}
