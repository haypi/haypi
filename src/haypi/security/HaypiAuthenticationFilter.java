package haypi.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.TextEscapeUtils;

public class HaypiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_DEVICE_ID_KEY = "j_deviceId";
    public static final String SPRING_SECURITY_FORM_PVP_KEY = "j_pvp";

    private String deviceIdParameter = SPRING_SECURITY_FORM_DEVICE_ID_KEY;
    private String pvpParameter = SPRING_SECURITY_FORM_PVP_KEY;
    
    public HaypiAuthenticationFilter() {
    	super();
    	// configure custom user details
    	((WebAuthenticationDetailsSource)authenticationDetailsSource).setClazz(HaypiAuthenticationDetails.class);
    }

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);
		String deviceId = obtainDeviceId(request);
		String pvpString = obtainPVP(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		
		if ( deviceId == null ) {
			deviceId = "";
		}
		
		boolean pvp;
		if ( pvpString == null || pvpString.length() == 0 ) {
			pvp = false;
		} else {
			pvp = true;
		}
		
		username = username.trim();

		HaypiAuthenticationToken authRequest = new HaypiAuthenticationToken(username, password, pvp, deviceId);

		// Place the last username attempted into HttpSession for views
		HttpSession session = request.getSession(false);

		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
		}

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
    protected String obtainDeviceId(HttpServletRequest request) {
        return request.getParameter(deviceIdParameter);
    }
    protected String obtainPVP(HttpServletRequest request) {
    	return request.getParameter(pvpParameter);
    }

}
