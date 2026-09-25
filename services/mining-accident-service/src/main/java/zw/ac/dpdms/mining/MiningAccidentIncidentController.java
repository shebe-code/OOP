package zw.ac.dpdms.mining;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zw.ac.dpdms.core.AccessPolicy;
import zw.ac.dpdms.core.HazardType;
import zw.ac.dpdms.core.Incident;
import zw.ac.dpdms.core.IncidentStatus;
import zw.ac.dpdms.core.IncidentStore;
import zw.ac.dpdms.core.RequestIdentity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mining-accidents")
public class MiningAccidentIncidentController {

    private final IncidentStore store;

    public MiningAccidentIncidentController(IncidentStore store) {
        this.store = store;
    }

    @PostMapping
    public Incident create(
            @Valid @RequestBody Incident input,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canCreate(identity, HazardType.MINING_ACCIDENT, input.ward());
        return store.create(input, HazardType.MINING_ACCIDENT);
    }

    @GetMapping
    public List<Incident> list(
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canRead(identity, HazardType.MINING_ACCIDENT, ward);
        return store.getAll(HazardType.MINING_ACCIDENT);
    }

    @GetMapping("/{id}")
    public Incident getById(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canRead(identity, HazardType.MINING_ACCIDENT, ward);

        return store.findById(id, HazardType.MINING_ACCIDENT)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Mining accident incident not found"
                        )
                );
    }

    @PutMapping("/{id}")
    public Incident update(
            @PathVariable UUID id,
            @Valid @RequestBody Incident input,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canWrite(identity, HazardType.MINING_ACCIDENT);
        return store.update(id, HazardType.MINING_ACCIDENT, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canWrite(identity, HazardType.MINING_ACCIDENT);
        store.delete(id, HazardType.MINING_ACCIDENT);
    }

    @PostMapping("/{id}/decision")
    public Incident decide(
            @PathVariable UUID id,
            @RequestParam IncidentStatus status,
            @RequestParam(required = false, defaultValue = "") String reason,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canApprove(identity, HazardType.MINING_ACCIDENT);
        return store.decide(id, HazardType.MINING_ACCIDENT, status, reason);
    }
}
