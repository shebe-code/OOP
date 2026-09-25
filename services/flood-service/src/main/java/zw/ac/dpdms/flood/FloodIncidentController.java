package zw.ac.dpdms.flood;

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
@RequestMapping("/api/floods")
public class FloodIncidentController {

    private final IncidentStore store;

    public FloodIncidentController(IncidentStore store) {
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
        AccessPolicy.canCreate(identity, HazardType.FLOOD, input.ward());
        return store.create(input, HazardType.FLOOD);
    }

    @GetMapping
    public List<Incident> list(
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canRead(identity, HazardType.FLOOD, ward);
        return store.getAll(HazardType.FLOOD);
    }

    @GetMapping("/{id}")
    public Incident getById(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canRead(identity, HazardType.FLOOD, ward);
        return store.findById(id, HazardType.FLOOD)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flood incident not found"));
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
        AccessPolicy.canWrite(identity, HazardType.FLOOD);
        return store.update(id, HazardType.FLOOD, input);
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
        AccessPolicy.canWrite(identity, HazardType.FLOOD);
        store.delete(id, HazardType.FLOOD);
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
        AccessPolicy.canApprove(identity, HazardType.FLOOD);
        return store.decide(id, HazardType.FLOOD, status, reason);
    }
}
