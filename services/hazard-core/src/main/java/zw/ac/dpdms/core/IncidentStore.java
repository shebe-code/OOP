package zw.ac.dpdms.core;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IncidentStore {
  private final Map<UUID, Incident> incidents = new ConcurrentHashMap<>();
  public Incident create(Incident input) {
    Incident saved = new Incident(UUID.randomUUID(), input.ward(), input.district(), input.province(), input.occurredAt(), input.reporter(), input.severity(), input.latitude(), input.longitude(), input.indicators(), IncidentStatus.PENDING, null);
    incidents.put(saved.id(), saved); return saved;
  }
  public List<Incident> all(boolean includePending) { return incidents.values().stream().filter(i -> includePending || i.status() == IncidentStatus.APPROVED).toList(); }
  public Optional<Incident> find(UUID id) { return Optional.ofNullable(incidents.get(id)); }
  public Incident update(UUID id, Incident input) { if (!incidents.containsKey(id)) throw new NoSuchElementException("Incident not found"); Incident saved = new Incident(id,input.ward(),input.district(),input.province(),input.occurredAt(),input.reporter(),input.severity(),input.latitude(),input.longitude(),input.indicators(),IncidentStatus.PENDING,null); incidents.put(id,saved); return saved; }
  public void delete(UUID id) { if (incidents.remove(id) == null) throw new NoSuchElementException("Incident not found"); }
  public Incident decide(UUID id, IncidentStatus status, String reason) { Incident old = incidents.get(id); if (old == null) throw new NoSuchElementException("Incident not found"); Incident changed = new Incident(old.id(),old.ward(),old.district(),old.province(),old.occurredAt(),old.reporter(),old.severity(),old.latitude(),old.longitude(),old.indicators(),status,reason); incidents.put(id,changed); return changed; }
}
