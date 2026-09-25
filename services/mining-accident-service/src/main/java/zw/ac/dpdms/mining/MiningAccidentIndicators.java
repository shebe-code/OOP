package zw.ac.dpdms.mining;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MiningAccidentIndicators(
        @NotBlank
        String mineName,

        @NotBlank
        String accidentType,          // e.g., collapse, explosion, flooding

        @Min(0)
        int workersAffected,

        @Min(0)
        int fatalities,

        @NotNull
        @DecimalMin("0.0")
        Double estimatedEconomicLossUSD
) {
}
