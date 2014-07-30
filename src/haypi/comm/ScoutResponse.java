package haypi.comm;

import java.io.IOException;

public class ScoutResponse extends BaseResponse {

	public ScoutResponse(ScoutRequest request, String response) throws IOException, HaypiException {
		super(request, response);
	}

}
