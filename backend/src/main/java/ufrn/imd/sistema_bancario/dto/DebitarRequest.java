package ufrn.imd.sistema_bancario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DebitarRequest(
    @Positive
    @NotNull
    double valor
) {}
