package haypi.comm;

import java.io.IOException;

public class DeleteLetterResponse extends BaseResponse {

	public DeleteLetterResponse(DeleteLetterRequest request, String response) throws IOException, HaypiException {
		super(request, response);
	}

}
