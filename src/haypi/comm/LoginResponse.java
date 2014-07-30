package haypi.comm;

import java.io.IOException;

public class LoginResponse extends BaseResponse {
	protected String sessionKey;
	protected String serverURL = "http://216.227.221.68/kingdom110/index_device.php";
	
	protected String line1;
	protected String line2;
	protected String line3;

	public LoginResponse(LoginRequest request, String response) throws IOException, HaypiException {
		super.init(request, response);
		sessionKey = readLine();
		serverURL = readLine();
		line1 = readLine();
		line2 = readLine();
		line3 = readLine();
		if (status != STATUS_OK) {
			log.warn("RESPONSE: " + response);
			if ( status == 1010 ) {
				throw new HaypiException("Incorrect username/password");
			} else {
				// session key contains error message in this case
				throw new HaypiException(sessionKey);
			}
		}
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public String getServerURL() {
		return serverURL;
	}
	
	
}
