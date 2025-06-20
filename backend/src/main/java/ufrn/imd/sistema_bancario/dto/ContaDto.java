package ufrn.imd.sistema_bancario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import ufrn.imd.sistema_bancario.models.TipoConta;

public record ContaDto(
    @NotBlank
    String numeroConta,
    TipoConta tipoConta,
    @PositiveOrZero
    Double saldoInicial
) {} 
