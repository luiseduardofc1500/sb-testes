package ufrn.imd.sistema_bancario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreditarRequest(
    @Positive
    @NotNull
    double valor
) {}