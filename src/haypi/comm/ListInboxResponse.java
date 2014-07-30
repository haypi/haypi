package haypi.comm;

import haypi.model.pojo.Letter;
import haypi.model.pojo.LetterType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class ListInboxResponse extends BaseResponse {

	public int mailboxSize;
	List<Letter> letters = new ArrayList<Letter>();

	public ListInboxResponse(ListInboxRequest request, String response) throws IOException, HaypiException {
		super(request, response);
		String line = readLine();
		mailboxSize = Integer.parseInt(line);
		Letter letter = null;
		try {
			while (StringUtils.hasLength((line = readLine()))) {
				letter = new Letter();
				letter.setId(line);
				letter.setType(LetterType.getByIndex(readLine()));
				letter.setSubject(readLine());
				letter.setDate(readLine());
				letter.setUnread(readLine());
				letter.setPlayer(readLine());
				letters.add(letter);
			}
		} catch (Exception e) {
			log.error("Error " + e + ", letter=" + letter + " response=" + response);
		}
	}

	@Override
	public String toString() {
		return letters.toString();
	}

	public List<Letter> getLetters() {
		return letters;
	}

}
