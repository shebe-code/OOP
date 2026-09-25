package zw.ac.dpdms.fire;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FireIndicators(
        @NotBlank
        String fireType,

        @NotNull
        @DecimalMin("0.0")
        Double areaBurnedHectares,

        @Min(0)
        int structuresDestroyed,

        @Min(0)
        int peopleInjured,

        @Min(0)
        int deaths
) {
}
