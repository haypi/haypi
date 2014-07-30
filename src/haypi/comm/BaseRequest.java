package haypi.comm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public abstract class BaseRequest {

	public static final String CRLF = "\r\n";

	protected final Log log = LogFactory.getLog(getClass());

	private String command;
	private int sequence;
	private String sessionKey;
	public String server;

	protected BaseRequest(String command) {
		this.command = command;
	}

	public abstract Object[] getDecoded();

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(CRLF);
		buffer.append("--HAYPI-NET-API-1234567890-afasdfkasdfuyasdfnlqpaccpa-0");
		buffer.append(CRLF);
		buffer.append("Content-Type: text/plain; charset=\"UTF-8\"");
		buffer.append(CRLF);
		buffer.append("Content-Transfer-Encoding: base64");
		buffer.append(CRLF);
		buffer.append(CRLF);

		StringBuilder decoded = new StringBuilder();
		// request code, set in constructor
		decoded.append(command);
		decoded.append(CRLF);
		// sequence number
		decoded.append(getSequence());
		decoded.append(CRLF);

		if (sessionKey != null) {
			decoded.append(sessionKey);
			decoded.append(CRLF);
		}

		// specific payload
		Object[] decodedLines = getDecoded();
		for (Object line : decodedLines) {
			if (line != null) {
				decoded.append(line.toString());
			}
			decoded.append(CRLF);
		}
		log.debug("Decoded payload: " + decoded);
		buffer.append(Base64.encode(decoded.toString().getBytes()));

		buffer.append(CRLF);
		buffer.append("--HAYPI-NET-API-1234567890-afasdfkasdfuyasdfnlqpaccpa-0--");
		buffer.append(CRLF);
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	public Class<? extends BaseResponse> getResponseClass() throws ClassNotFoundException {
		String requestClassName = getClass().getName();
		return (Class<? extends BaseResponse>) Class.forName(requestClassName.replace("Request", "Response"));
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public int getSequence() {
		return sequence;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public void setServerName(String server) {
		this.server = server;
	}
	public String getServerName() {
		return server;
	}

}
