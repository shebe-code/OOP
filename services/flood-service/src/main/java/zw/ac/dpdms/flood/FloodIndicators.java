package zw.ac.dpdms.flood;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FloodIndicators(
        @NotNull
        @DecimalMin("0.0")
        Double peakWaterLevelMetres,

        @NotBlank
        String catchmentName,

        @Min(0)
        int householdsDisplaced,

        @NotNull
        @DecimalMin("0.0")
        Double areaFloodedHectares,

        @Min(0)
        int inundationDurationDays
) {
}
