package haypi.security;

import java.lang.reflect.InvocationTargetException;

import haypi.comm.SessionController;
import haypi.service.LoginService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

public class HaypiAuthenticationProvider implements AuthenticationProvider {
	private static final Log logger = LogFactory.getLog(HaypiAuthenticationProvider.class);

	LoginService loginService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HaypiAuthenticationToken userToken = (HaypiAuthenticationToken) authentication;

		String username = userToken.getName();
		String password = (String) authentication.getCredentials();
		String deviceId = userToken.getDeviceId();
		boolean pvp = userToken.getPVP();

		if (logger.isDebugEnabled()) {
			logger.debug("Processing authentication request for user: " + username);
		}

		if (!(StringUtils.hasLength(username))) {
			throw new BadCredentialsException("Empty Username");
		}

		try {
			SessionController sessionController = loginService.login(username, password, pvp, deviceId);

			if (sessionController == null) {
				throw new BadCredentialsException("Error login");
			} else {
				HaypiUserDetails user = new HaypiUserDetails();
				user.setUsername(username);
				user.setPassword(password);
				user.setDeviceId(deviceId);
				user.setSessionController(sessionController);
				return createSuccessfulAuthentication(userToken, user);
			}
		} catch (Exception e) {
			logger.warn("Login error: ", e);
			if ( e instanceof InvocationTargetException ) {
				String message = ((InvocationTargetException)e).getTargetException().getMessage();
				throw new AuthenticationServiceException("Error login: " + message, e);
			} else {
				throw new AuthenticationServiceException("Error login", e);
			}
		}
	}

	protected Authentication createSuccessfulAuthentication(HaypiAuthenticationToken authentication, HaypiUserDetails user) {
		Object password = user.getPassword();
		String deviceId = user.getDeviceId();
		boolean pvp = user.getPVP();

		HaypiAuthenticationToken result = new HaypiAuthenticationToken(user, password, deviceId, pvp, user.getAuthorities());
		HaypiAuthenticationDetails details = (HaypiAuthenticationDetails) authentication.getDetails();
		result.setDetails(details);

		return result;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Autowired
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

}
