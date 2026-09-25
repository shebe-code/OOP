package zw.ac.dpdms.core;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
  private final IncidentStore store;
  private final HazardType hazard;
  public IncidentController(IncidentStore store, @Value("${dpdms.hazard-type}") String type) { this.store = store; this.hazard = HazardType.valueOf(type); }
  private RequestIdentity user(String role, String userHazard, String ward) { return new RequestIdentity(role,userHazard,ward); }
  @PostMapping public Incident create(@Valid @RequestBody Incident input, @RequestHeader("X-Role") String role, @RequestHeader("X-Hazard") String userHazard, @RequestHeader("X-Ward") String ward) { AccessPolicy.canCreate(user(role,userHazard,ward),hazard,input.ward()); return store.create(input); }
  @GetMapping public List<Incident> list(@RequestHeader("X-Role") String role, @RequestHeader("X-Hazard") String userHazard, @RequestHeader(value="X-Ward",required=false) String ward) { RequestIdentity identity=user(role,userHazard,ward); boolean privileged=identity.isNational() || identity.isAdmin() || (identity.is("PROVINCIAL_SUPERVISOR") && identity.hasHazard(hazard)); if (!privileged && !identity.hasHazard(hazard)) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN); return store.all(privileged); }
  @GetMapping("/{id}") public Incident get(@PathVariable UUID id, @RequestHeader("X-Role") String role, @RequestHeader("X-Hazard") String userHazard, @RequestHeader(value="X-Ward",required=false) String ward) { List<Incident> visible=list(role,userHazard,ward); return visible.stream().filter(i->i.id().equals(id)).findFirst().orElseThrow(()->new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND)); }
  @PutMapping("/{id}") public Incident update(@PathVariable UUID id,@Valid @RequestBody Incident input,@RequestHeader("X-Role") String role,@RequestHeader("X-Hazard") String userHazard,@RequestHeader(value="X-Ward",required=false) String ward) { AccessPolicy.canWrite(user(role,userHazard,ward),hazard); return store.update(id,input); }
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable UUID id,@RequestHeader("X-Role") String role,@RequestHeader("X-Hazard") String userHazard,@RequestHeader(value="X-Ward",required=false) String ward) { AccessPolicy.canWrite(user(role,userHazard,ward),hazard); store.delete(id); }
  @PostMapping("/{id}/decision") public Incident decision(@PathVariable UUID id,@RequestParam IncidentStatus status,@RequestParam(required=false,defaultValue="") String reason,@RequestHeader("X-Role") String role,@RequestHeader("X-Hazard") String userHazard,@RequestHeader(value="X-Ward",required=false) String ward) { AccessPolicy.canApprove(user(role,userHazard,ward),hazard); if (status == IncidentStatus.PENDING) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid decision"); return store.decide(id,status,reason); }
}
