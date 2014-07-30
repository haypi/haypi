package haypi.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class HaypiAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;

	public HaypiAuthenticationDetails(HttpServletRequest request) {
		super(request);
	}

}
