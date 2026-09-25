package zw.ac.dpdms.core;

import jakarta.validation.constraints.*;
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
  @NotNull Map<String,Object> indicators,
  IncidentStatus status,
  String decisionReason
) {}
