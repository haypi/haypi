package haypi.model.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYER")
public class Player extends BasicObject implements Comparable<Player> {

	private static final long serialVersionUID = 1L;

	private String name;
	private String alliance;
	private String server;
	private boolean active;
	
	private Integer level;
	private Long prestige;
	private Integer levelRank;
	private Integer prestigeRank;

	private Set<Cell> cells = new HashSet<Cell>(0);

	public Player() {

	}

	@Id
	@Column(name = "NAME", unique = true, nullable = false)
	public String getName() {
		return name;
	}

	@Column(name = "ALLIANCE")
	public String getAlliance() {
		return alliance;
	}

	@Column(name = "SERVER")
	public String getServer() {
		return server;
	}
	
	@Column(name = "ACTIVE", nullable = false, precision = 1, scale = 0)
	public boolean isActive() {
		return active;
	}

	@Column(name = "PLAYER_LEVEL", nullable = true, precision = 2, scale = 0)
	public Integer getLevel() {
		return level;
	}

	@Column(name = "PRESTIGE", nullable = true, precision = 18, scale = 0)
	public Long getPrestige() {
		return prestige;
	}

	@Column(name = "LEVEL_RANK", nullable = true, precision = 18, scale = 0)
	public Integer getLevelRank() {
		return levelRank;
	}
	
	@Column(name = "PRESTIGE_RANK", nullable = true, precision = 18, scale = 0)
	public Integer getPrestigeRank() {
		return prestigeRank;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	public Set<Cell> getCells() {
		return this.cells;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCells(Set<Cell> cells) {
		this.cells = cells;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setPrestige(Long prestige) {
		this.prestige = prestige;
	}

	public void setLevelRank(Integer levelRank) {
		this.levelRank = levelRank;
	}
	public void setPrestigeRank(Integer prestigeRank) {
		this.prestigeRank = prestigeRank;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ( ! (obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Player o) {
		return getName().compareTo(o.getName());
	}

}
