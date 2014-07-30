package haypi.model.pojo;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "CELL")
public class Cell extends BasicObject {

	private static final long serialVersionUID = 1L;

	CellXY cellXY;
	int type;
	int level;
	String name;
	String owner;
	String alliance;
	Date lastUpdate;
	
	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "X", nullable = false, precision = 3, scale = 0)),
			@AttributeOverride(name = "y", column = @Column(name = "Y", nullable = false, precision = 3, scale = 0)) })
	public CellXY getCellXY() {
		return cellXY;
	}

	@Column(name = "TYPE", nullable = false, precision = 1, scale = 0)
	public int getType() {
		return type;
	}

	@Column(name = "CELL_LEVEL", nullable = false, precision = 2, scale = 0)
	public int getLevel() {
		return level;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	@Column(name = "ALLIANCE")
	public String getAlliance() {
		return alliance;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATE", length = 7)
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	@Transient
	public int getX() {
		return getCellXY().getX();
	}

	@Transient
	public int getY() {
		return getCellXY().getY();
	}
	
	@Transient
	public String getDistance(int x, int y) {
		int seconds = (int)Math.sqrt((cellXY.x-x)*(cellXY.x-x)*100 + (cellXY.y-y)*(cellXY.y-y)*100);
		int hours = seconds/3600;
		int minutes = (seconds-hours*3600)/60;
		int secs = seconds-hours*3600-minutes*60;
		return String.format("%2d:%02d:%02d", hours, minutes, secs);
	}
	
	@Transient
	public String getTypeName() {
		return CellType.getByIndex(type).name();
	}

	public void setCellXY(CellXY cellXY) {
		this.cellXY = cellXY;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellXY == null) ? 0 : cellXY.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cell))
			return false;
		Cell other = (Cell) obj;
		if (cellXY == null) {
			if (other.cellXY != null)
				return false;
		} else if (!cellXY.equals(other.cellXY))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cell [x=" + (cellXY == null ? "" : cellXY.x) + ", y=" + (cellXY == null ? "" : cellXY.y) + ", type=" + CellType.getByIndex(type) + ", name=" + name
				+ ", level=" + level + ", owner=" + owner + ", alliance=" + alliance + ", lastUpdate=" + lastUpdate + "]";
	}

}
