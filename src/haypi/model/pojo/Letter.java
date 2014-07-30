package haypi.model.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Letter {
	String id;
	LetterType type;
	String subject;
	Date date;
	boolean unread;
	String player;
	String body;

	public String getId() {
		return id;
	}

	public LetterType getType() {
		return type;
	}

	public String getSubject() {
		return subject;
	}

	public Date getDate() {
		return date;
	}

	public boolean isUnread() {
		return unread;
	}

	public String getPlayer() {
		return player;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setType(LetterType type) {
		this.type = type;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public void setDate(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ" );
		this.date = dateFormat.parse(date+"GMT");
	}

	public void setUnread(String unread) {
		if (unread != null && unread.equals("0")) {
			this.unread = true;
		} else {
			this.unread = false;
		}
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Letter [id=" + id + ", type=" + type + ", subject=" + subject + ", date=" + date + ", unread=" + unread + "]";
	}

}
