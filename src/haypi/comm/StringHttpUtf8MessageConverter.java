package haypi.comm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

public class StringHttpUtf8MessageConverter extends StringHttpMessageConverter {

	protected static final Log log = LogFactory.getLog(StringHttpUtf8MessageConverter.class);

	@SuppressWarnings("rawtypes")
	@Override
	protected String readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

		if (log.isDebugEnabled()) {
			log.debug("inputMessage=" + inputMessage);
		}

		// MediaType contentType = inputMessage.getHeaders().getContentType();
		Charset charset = Charset.forName("UTF-8");
		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
	}

}
