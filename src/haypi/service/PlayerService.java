package haypi.service;

import haypi.model.pojo.Cell;
import haypi.model.pojo.Player;

import java.util.List;

public interface PlayerService {

	public Player getPlayer(String server, String name);

	public List<Player> getPlayersByNamePrefix(String server, String namePrefix);

	public List<Cell> getCells(Player player);

	public List<String> getAlliancesByNamePrefix(String server, String namePrefix);

	public List<Player> getPlayersByAlliance(String server, String alliance);

	public void savePlayers(List<Player> players);

	public void updatePrestige(List<Player> players);

	public void updateLevel(List<Player> players);

}