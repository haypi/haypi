package haypi.security;

import haypi.comm.SessionController;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class HaypiUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	String username;
	String password;
	String deviceId;
	boolean pvp;

	private SessionController sessionController;

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getDeviceId() {
		return deviceId;
	}
	
	public boolean getPVP() {
		return pvp;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public void setPVP(boolean pvp) {
		this.pvp = pvp;
	}

	public void setSessionController(SessionController sessionController) {
		this.sessionController = sessionController;
	}

	public SessionController getSessionController() {
		return sessionController;
	}

}
