package haypi.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class BaseResponse {

	protected final Log log = LogFactory.getLog(getClass());

	public static int STATUS_OK = 0;
	public static int STATUS_UNKNOWN = -1;
	public static int STATUS_LOGIN_ERROR = 1010;

	private BufferedReader _reader;
	
	protected String sequence;
	protected int status = STATUS_UNKNOWN;
	
	protected BaseRequest request;

	protected void init(BaseRequest request, String response) throws IOException {
		this.request = request;
		_reader = new BufferedReader(new StringReader(response));
		sequence = _reader.readLine();
		String statusLine = _reader.readLine();
		if (StringUtils.hasLength(statusLine)) {
			try {
				status = Integer.parseInt(statusLine);
			} catch (NumberFormatException nfe) {
				log.warn("Invalid response status: " + statusLine);
			}
		}
	}
	
	protected BaseResponse() {
	}
	
	public BaseResponse(BaseRequest request, String response) throws HaypiException, IOException {
		init(request, response);
		if (status != STATUS_OK) {
			log.warn("RESPONSE: " + response);
			if ( status == 2001 || status == 2000 ) {
				throw new SessionTimeoutException();
			} else {
				throw new HaypiException(getStatus());
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		_reader.close();
	}

	protected String readLine() throws IOException {
		return _reader.readLine();
	}

	public int getStatus() {
		return status;
	}

}
