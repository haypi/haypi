package haypi.comm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SessionController {

	protected final Log log = LogFactory.getLog(getClass());

	// String loginServerUrl =
	// "http://192.168.0.4:8080/passport/port_device.php";
	String loginServerUrl = "http://www.haypipassport.com/passport/port_device.php";
	String serverURL = null;
	String sessionKey = null;
	String deviceId = null;
	String serverName = null;

	private int sequenceNumber = 1;

	public synchronized LoginResponse login(LoginRequest request) throws Exception {
		this.deviceId = request.deviceId;
		LoginResponse response = (LoginResponse) sendCommand(request);
		this.serverURL = response.getServerURL();
		int index1 = this.serverURL.indexOf("/kingdom");
		int index2 = this.serverURL.lastIndexOf("/");
		this.serverName = this.serverURL.substring(index1 + "/kingdom".length(), index2);
		this.sessionKey = response.getSessionKey();
		return response;
	}

	public BaseResponse sendCommand(BaseRequest request) throws Exception {
		RestTemplate template = new RestTemplate();
		
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		StringHttpUtf8MessageConverter converter = new StringHttpUtf8MessageConverter();
		messageConverters.add(converter);
		template.setMessageConverters(messageConverters);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "multipart/related; boundary=\"HAYPI-NET-API-1234567890-afasdfkasdfuyasdfnlqpaccpa-0\"");
		headers.set("User-Agent", "Kingdom/3.0 CFNetwork/485.13.8 Darwin/11.0.0");
		headers.set("Clan", "en");
		headers.set("Udid", deviceId );
		headers.set("Accept", "*/*");
		headers.set("Accept-Language", "en-us");
		headers.set("Accept-Encoding", "gzip, deflate");

		synchronized (this) {
			if (request.getSequence() == 0) {
				request.setSequence(sequenceNumber++);
			}
		}
		if (request.getSessionKey() == null) {
			request.setSessionKey(sessionKey);
		}

		request.setServerName(serverName);
		
		String httpRequestStr = request.toString();
		if ( log.isDebugEnabled() ) {
			log.debug("REQUEST, sequence=" + request.getSequence() + ":\n" + httpRequestStr);
		}

		HttpEntity<String> httpRequest = new HttpEntity<String>(httpRequestStr, headers);
		String uri = (this.serverURL == null ? this.loginServerUrl : this.serverURL);
		ResponseEntity<String> httpResponse = template.exchange(uri, HttpMethod.POST, httpRequest, String.class);
		String httpResponseStr = httpResponse.getBody();

		// HttpClient httpclient = new DefaultHttpClient();
		// HttpPost post = new HttpPost(uri);
		// httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		// HttpVersion.HTTP_1_1);
		// HttpResponse httpResponse = httpclient.execute(post);
		// org.apache.http.HttpEntity httpEntity = httpResponse.getEntity();
		// EntityUtils.consume(httpEntity);

		if ( log.isDebugEnabled() ) {
			log.debug("RESPONSE, sequence=" + request.getSequence() + ":\n" + httpResponseStr);
		}
		return ResponseFactory.getResponseObject(request, httpResponseStr, request.getResponseClass());
	}
	
	public String getServerName() {
		return serverName;
	}
}
