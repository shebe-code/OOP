package zw.ac.dpdms.core;

import java.util.Locale;

public record RequestIdentity(String role, String hazard, String ward) {
  public boolean is(String expected) { return expected.equalsIgnoreCase(role == null ? "" : role); }
  public boolean hasHazard(HazardType expected) { return expected.name().equalsIgnoreCase(hazard == null ? "" : hazard); }
  public boolean isNational() { return is("NATIONAL_USER"); }
  public boolean isAdmin() { return is("PROVINCIAL_ADMIN"); }
  public String normalisedWard() { return ward == null ? "" : ward.trim().toLowerCase(Locale.ROOT); }
}
