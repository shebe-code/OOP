package zw.ac.dpdms.core;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Incident(
      UUID id,
      @NotBlank String ward,
      @NotBlank String district,
      @NotBlank String province,
      @NotNull Instant occurredAt,
      @NotBlank String reporter,
      @NotBlank String severity,
      @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
      @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
      @NotNull HazardType hazardType,
      @NotNull Map<String, Object> indicators,
      @NotNull IncidentStatus status,
      String decisionReason
) {
    public Incident withStatus(IncidentStatus status, String decisionReason) {
        return new Incident(this.id, this.ward, this.district, this.province, this.occurredAt,
                this.reporter, this.severity, this.latitude, this.longitude, this.hazardType,
                this.indicators, status, decisionReason);
    }
}
