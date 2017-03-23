package ro.upb.researchmgr.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    
    public static final String SEC = "ROLE_SEC";
    
    public static final String PHD = "ROLE_PHD";
    
    public static final String COORD = "ROLE_COORD";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
