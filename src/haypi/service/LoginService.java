package haypi.service;

import haypi.comm.BaseResponse;
import haypi.comm.LoginRequest;
import haypi.comm.LoginResponse;
import haypi.comm.SessionController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginService {

	protected final Log log = LogFactory.getLog(getClass());
	
	public SessionController login(String username, String password, boolean pvp, String deviceId) throws Exception {
		SessionController controller = new SessionController();
		LoginRequest request = new LoginRequest(username, password, pvp, deviceId);
		LoginResponse response = controller.login(request);
		if ( response.getStatus() == BaseResponse.STATUS_OK ) {
			return controller;
		} else {
			log.warn("Login response: " + response);
			return null;
		}
	}
}
