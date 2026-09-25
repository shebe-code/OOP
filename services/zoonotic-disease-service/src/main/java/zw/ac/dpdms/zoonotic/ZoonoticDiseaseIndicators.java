package zw.ac.dpdms.zoonotic;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ZoonoticDiseaseIndicators(
        @NotBlank
        String diseaseName,

        @Min(0)
        int suspectedCases,

        @Min(0)
        int confirmedCases,

        @Min(0)
        int deaths,

        @Min(0)
        int animalsAffected
) {
}
