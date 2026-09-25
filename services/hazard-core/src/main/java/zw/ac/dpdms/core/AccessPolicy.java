package zw.ac.dpdms.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class AccessPolicy {
    private AccessPolicy() {
    }

    public static void canCreate(RequestIdentity user, HazardType hazard, String ward) {
        if (user == null) {
            throw forbidden();
        }
        if (user.isNationalUser() || !user.isWardRecorder()) {
            throw forbidden();
        }
        if (!user.hasHazard(hazard)) {
            throw forbidden();
        }
        if (!user.normalizedWard().equals(normalize(ward))) {
            throw forbidden();
        }
    }

    public static void canRead(RequestIdentity user, HazardType hazard, String ward) {
        if (user == null) {
            throw forbidden();
        }
        if (user.isNationalUser()) {
            return;
        }
        if (user.isWardRecorder()) {
            if (!user.hasHazard(hazard)) {
                throw forbidden();
            }
            if (ward != null && !user.normalizedWard().equals(normalize(ward))) {
                throw forbidden();
            }
            return;
        }
        if (user.isProvincialSupervisor() || user.isProvincialAdmin()) {
            if (!user.hasHazard(hazard)) {
                throw forbidden();
            }
            return;
        }
        throw forbidden();
    }

    public static void canWrite(RequestIdentity user, HazardType hazard) {
        if (user == null || user.isNationalUser()) {
            throw forbidden();
        }
        if (user.isWardRecorder() && user.hasHazard(hazard)) {
            return;
        }
        if ((user.isProvincialSupervisor() || user.isProvincialAdmin()) && user.hasHazard(hazard)) {
            return;
        }
        throw forbidden();
    }

    public static void canApprove(RequestIdentity user, HazardType hazard) {
        if (user == null) {
            throw forbidden();
        }
        if (user.isNationalUser()) {
            throw forbidden();
        }
        if ((user.isProvincialSupervisor() || user.isProvincialAdmin()) && user.hasHazard(hazard)) {
            return;
        }
        throw forbidden();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private static ResponseStatusException forbidden() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorised for this hazard or operation.");
    }
}
