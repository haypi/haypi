package haypi.model.pojo;

public class ScoutLetter extends Letter {

	ScoutInfo scoutInfo;

	@Override
	public void setBody(String body) {
		this.body = body;
		this.scoutInfo = new ScoutInfo();
		scoutInfo.parse(body);
	}

	public ScoutInfo getScoutInfo() {
		return scoutInfo;
	}

	@Override
	public String toString() {
		return scoutInfo == null ? super.toString() : scoutInfo.toString();
	}

}
