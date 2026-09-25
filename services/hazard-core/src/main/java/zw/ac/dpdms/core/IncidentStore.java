package zw.ac.dpdms.core;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IncidentStore {
    private final Map<UUID, Incident> incidents = new ConcurrentHashMap<>();

    public Incident create(Incident input, HazardType hazard) {
        Incident saved = new Incident(
                UUID.randomUUID(),
                input.ward(),
                input.district(),
                input.province(),
                input.occurredAt(),
                input.reporter(),
                input.severity(),
                input.latitude(),
                input.longitude(),
                hazard,
                input.indicators(),
                IncidentStatus.PENDING,
                null
        );
        incidents.put(saved.id(), saved);
        return saved;
    }

    public List<Incident> getAll(HazardType hazard) {
        return incidents.values().stream()
                .filter(incident -> incident.hazardType() == hazard)
                .toList();
    }

    public List<Incident> getApproved(HazardType hazard) {
        return incidents.values().stream()
                .filter(incident -> incident.hazardType() == hazard)
                .filter(incident -> incident.status() == IncidentStatus.APPROVED)
                .toList();
    }

    public Optional<Incident> findById(UUID id, HazardType hazard) {
        return incidents.values().stream()
                .filter(incident -> incident.id().equals(id))
                .filter(incident -> incident.hazardType() == hazard)
                .findFirst();
    }

    public Incident update(UUID id, HazardType hazard, Incident updated) {
        Incident current = incidents.get(id);
        if (current == null || current.hazardType() != hazard) {
            throw new NoSuchElementException("Incident not found for the specified hazard.");
        }

        Incident replacement = new Incident(
                id,
                updated.ward(),
                updated.district(),
                updated.province(),
                updated.occurredAt(),
                updated.reporter(),
                updated.severity(),
                updated.latitude(),
                updated.longitude(),
                hazard,
                updated.indicators(),
                IncidentStatus.PENDING,
                null
        );
        incidents.put(id, replacement);
        return replacement;
    }

    public void delete(UUID id, HazardType hazard) {
        Incident current = incidents.get(id);
        if (current == null || current.hazardType() != hazard) {
            throw new NoSuchElementException("Incident not found for the specified hazard.");
        }
        incidents.remove(id);
    }

    public Incident decide(UUID id, HazardType hazard, IncidentStatus status, String reason) {
        Incident current = incidents.get(id);
        if (current == null || current.hazardType() != hazard) {
            throw new NoSuchElementException("Incident not found for the specified hazard.");
        }

        Incident updated = current.withStatus(status, reason);
        incidents.put(id, updated);
        return updated;
    }
}
