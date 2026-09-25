package zw.ac.dpdms.drought;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DroughtIndicators(
        @NotNull
        @DecimalMin("0.0")
        Double rainfallDeficitMm,

        @Min(0)
        int consecutiveDryDays,

        @NotNull
        @DecimalMin("0.0")
        Double cropFailurePercentage,

        @Min(0)
        int peopleFacingWaterShortages,

        @Min(0)
        int livestockMortalityCount
) {
}
