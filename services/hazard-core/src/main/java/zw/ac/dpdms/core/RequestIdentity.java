package zw.ac.dpdms.core;

import java.util.Locale;

public record RequestIdentity(String role, String hazard, String ward) {
    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    public boolean is(String expectedRole) {
        return expectedRole.equalsIgnoreCase(role == null ? "" : role.trim());
    }

    public boolean hasHazard(HazardType expected) {
        return expected.name().equalsIgnoreCase(hazard == null ? "" : hazard.trim());
    }

    public String normalizedWard() {
        return normalize(ward);
    }

    public boolean isWardRecorder() {
        return is("WARD_RECORDER");
    }

    public boolean isProvincialSupervisor() {
        return is("PROVINCIAL_SUPERVISOR");
    }

    public boolean isNationalUser() {
        return is("NATIONAL_USER");
    }

    public boolean isProvincialAdmin() {
        return is("PROVINCIAL_ADMIN");
    }
}
