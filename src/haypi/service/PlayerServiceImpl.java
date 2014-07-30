package haypi.service;

import haypi.model.dao.AllianceDao;
import haypi.model.dao.CellDao;
import haypi.model.dao.PlayerDao;
import haypi.model.pojo.Cell;
import haypi.model.pojo.Player;
import haypi.model.search.CellSearch;
import haypi.model.search.PlayerSearch;
import haypi.model.search.Search;
import haypi.model.search.Sort;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService {
	private final Log log = LogFactory.getLog(getClass());

	protected CellDao cellDao;
	protected PlayerDao playerDao;
	protected AllianceDao allianceDao;

	@Autowired
	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}

	@Autowired
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Autowired
	public void setAllianceDao(AllianceDao allianceDao) {
		this.allianceDao = allianceDao;
	}

	@Override
	public Player getPlayer(String server, String name) {
		if (log.isDebugEnabled()) {
			log.debug("PlayerService.getPlayer: " + name);
		}
		List<Player> result = playerDao.getObjects(Player.class, null, null, PlayerSearch.byNameIgnoreCase(server, name));
		return (result.isEmpty() ? null : result.get(0));
	}

	@Override
	public List<Player> getPlayersByNamePrefix(String server, String namePrefix) {
		if (log.isDebugEnabled()) {
			log.debug("PlayerService.getPlayersByNamePrefix: " + namePrefix);
		}
		return playerDao.getObjects(Player.class, null, null, PlayerSearch.byNamePrefix(server, namePrefix));
	}

	@Override
	public List<Cell> getCells(Player player) {
		Sort sort = new Sort(Order.asc("type"));
		return cellDao.getObjects(Cell.class, null, sort, CellSearch.byOwner(player.getServer(), player.getName()));
	}

	@Override
	public List<String> getAlliancesByNamePrefix(String server, String namePrefix) {
		Projection projections = Projections.distinct(Projections.projectionList().add(Projections.property("alliance").as("alliance")));
		Criterion criterion = Restrictions.ilike("alliance", namePrefix, MatchMode.START);
		criterion = Restrictions.and(criterion, Restrictions.eq("server", server));
		Search search = new Search(criterion, projections, "alliance", Transformers.aliasToBean(Player.class));
		List<Player> players = playerDao.getObjects(Player.class, null, null, search);
		List<String> result = new ArrayList<String>();
		for (Player player : players) {
			if (!result.contains(player.getAlliance())) {
				result.add(player.getAlliance());
			}
		}
		return result;
	}

	@Override
	public List<Player> getPlayersByAlliance(String server, String alliance) {
		if (log.isDebugEnabled()) {
			log.debug("PlayerService.getPlayersByAlliance: " + alliance);
		}
		return playerDao.getObjects(Player.class, null, null, PlayerSearch.byAlliance(server, alliance));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void savePlayers(List<Player> players) {
		if (log.isDebugEnabled()) {
			log.debug("PlayerService.savePlayers: " + players.toString());
		}
		playerDao.flush();

		for (Player player : players) {
			// temporary, for missing players
			playerDao.saveOrUpdate(player);
			playerDao.flush();
			playerDao.evict(player);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updatePrestige(List<Player> players) {
		if (log.isDebugEnabled()) {
			log.debug("PlayerService.updatePrestige: " + players.toString());
		}

		for (Player player : players) {
			Player playerPersisted = getPlayer(player.getServer(), player.getName());
			if (playerPersisted == null) {
				playerDao.save(player);
			} else {
				playerPersisted.setPrestigeRank(player.getPrestigeRank());
				playerPersisted.setPrestige(player.getPrestige());
				playerPersisted.setAlliance(player.getAlliance());
				playerDao.evict(player);
				playerDao.update(playerPersisted);
			}
			playerDao.flush();
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateLevel(List<Player> players) {
		if (log.isDebugEnabled()) {
			log.debug("PlayerService.updateLevel: " + players.toString());
		}

		for (Player player : players) {
			Player playerPersisted = getPlayer(player.getServer(), player.getName());
			if (playerPersisted == null) {
				playerDao.save(player);
			} else {
				playerPersisted.setLevelRank(player.getLevelRank());
				playerPersisted.setLevel(player.getLevel());
				playerPersisted.setAlliance(player.getAlliance());
				playerDao.evict(player);
				playerDao.update(playerPersisted);
			}
			playerDao.flush();
		}
	}
}
