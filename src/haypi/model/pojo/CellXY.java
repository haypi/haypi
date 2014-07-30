package haypi.model.pojo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CellXY extends Point {

	private static final long serialVersionUID = 1L;

	String server;

	public CellXY() {

	}

	public CellXY(String server, int x, int y) {
		this.x = x;
		this.y = y;
		this.server = server;
	}

	@Column(name = "X", nullable = false, precision = 3, scale = 0)
	public Integer getX() {
		return x;
	}

	@Column(name = "Y", nullable = false, precision = 3, scale = 0)
	public Integer getY() {
		return y;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Column(name = "SERVER", nullable = false)
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}
