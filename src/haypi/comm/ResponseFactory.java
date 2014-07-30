package haypi.comm;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseFactory {

	protected static final Log log = LogFactory.getLog(ResponseFactory.class);

	public static BaseResponse getResponseObject(BaseRequest request, String serverResponse, Class<? extends BaseResponse> responseClass) throws Exception {
		log.debug("Response class: " + responseClass);
		Constructor<? extends BaseResponse> constructor = responseClass.getConstructor(request.getClass(), String.class);
		return constructor.newInstance(request, serverResponse);
	}
}
