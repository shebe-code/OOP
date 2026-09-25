package zw.ac.dpdms.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class AccessPolicy {
  private AccessPolicy() {}
  public static void canCreate(RequestIdentity user, HazardType hazard, String ward) {
    if (!user.is("WARD_RECORDER") || !user.hasHazard(hazard) || !user.normalisedWard().equals(ward.trim().toLowerCase())) deny();
  }
  public static void canApprove(RequestIdentity user, HazardType hazard) {
    if (!(user.is("PROVINCIAL_SUPERVISOR") || user.isAdmin()) || (!user.isAdmin() && !user.hasHazard(hazard))) deny();
  }
  public static void canWrite(RequestIdentity user, HazardType hazard) {
    if (user.isNational()) deny();
    if (!user.isAdmin() && !user.hasHazard(hazard)) deny();
  }
  private static void deny() { throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorised for this hazard or operation"); }
}
