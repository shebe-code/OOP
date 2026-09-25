package zw.ac.dpdms.fire;

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
@RequestMapping("/api/fires")
public class FireIncidentController {

    private final IncidentStore store;

    public FireIncidentController(IncidentStore store) {
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
        AccessPolicy.canCreate(identity, HazardType.FIRE, input.ward());
        return store.create(input, HazardType.FIRE);
    }

    @GetMapping
    public List<Incident> list(
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canRead(identity, HazardType.FIRE, ward);
        return store.getAll(HazardType.FIRE);
    }

    @GetMapping("/{id}")
    public Incident getById(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Role", required = false) String role,
            @RequestHeader(value = "X-Hazard", required = false) String hazard,
            @RequestHeader(value = "X-Ward", required = false) String ward
    ) {
        RequestIdentity identity = new RequestIdentity(role, hazard, ward);
        AccessPolicy.canRead(identity, HazardType.FIRE, ward);

        return store.findById(id, HazardType.FIRE)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Fire incident not found"
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
        AccessPolicy.canWrite(identity, HazardType.FIRE);
        return store.update(id, HazardType.FIRE, input);
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
        AccessPolicy.canWrite(identity, HazardType.FIRE);
        store.delete(id, HazardType.FIRE);
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
        AccessPolicy.canApprove(identity, HazardType.FIRE);
        return store.decide(id, HazardType.FIRE, status, reason);
    }
}
