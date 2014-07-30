package haypi.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class HaypiAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private String deviceId;
	private boolean pvp = false;

	public HaypiAuthenticationToken(String username, String password, boolean pvp, String deviceId) {
		super(username, password);
		this.deviceId = deviceId;
		this.pvp = pvp;
	}

	/**
	 * This constructor should only be used by
	 * <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
	 * implementations that are satisfied with producing a trusted (i.e.
	 * {@link #isAuthenticated()} = <code>true</code>) authentication token.
	 * 
	 * @param principal
	 * @param credentials
	 * @param authorities
	 */
	public HaypiAuthenticationToken(Object principal, Object credentials, String deviceId, boolean pvp,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.deviceId = deviceId;
		this.pvp = pvp;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public boolean getPVP() {
		return this.pvp;
	}

}
