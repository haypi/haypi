package haypi.comm;

import haypi.model.pojo.Letter;
import haypi.model.pojo.LetterType;
import haypi.model.pojo.ScoutLetter;

import java.io.IOException;

import org.springframework.util.StringUtils;

public class ListLetterResponse extends BaseResponse {

	Letter letter = new Letter();

	public ListLetterResponse(ListLetterRequest request, String response) throws IOException, HaypiException {
		super(request, response);
		try {
			String unread = readLine();
			String letterId = readLine();
			LetterType letterType = LetterType.getByIndex(readLine());
			if ( letterType == LetterType.SCOUT ) {
				letter = new ScoutLetter();
			} else {
				log.warn("Creating new letter of type: " + letterType);
				letter = new Letter();
			}
			letter.setId(letterId);
			letter.setUnread(unread);
			letter.setType(letterType);
			letter.setSubject(readLine());
			letter.setDate(readLine());
			readLine();

			StringBuilder body = new StringBuilder();
			String line;
			while (StringUtils.hasLength((line = readLine()))) {
				body.append(line);
			}
			letter.setBody(body.toString());
			readLine();
			letter.setPlayer(readLine());
		} catch (Exception e) {
			log.error("Error " + e + ", letter=" + letter + " response=" + response);
		}
	}

	@Override
	public String toString() {
		return letter.toString();
	}

	public Letter getLetter() {
		return letter;
	}

}
