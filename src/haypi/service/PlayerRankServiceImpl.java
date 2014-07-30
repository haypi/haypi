package haypi.service;

import haypi.comm.RankRequest;
import haypi.comm.RankResponse;
import haypi.comm.RankType;
import haypi.comm.SessionController;
import haypi.model.pojo.Player;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("playerRankService")
public class PlayerRankServiceImpl implements PlayerRankService {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	protected PlayerService playerService;

	protected Integer progress;

	@Override
	public List<Player> listRankPage(SessionController controller, RankType rankType, int page) throws Exception {
		log.debug("PlayerRankService request: page=" + page + ", rankType=" + rankType);

		RankRequest playerRankRequest = new RankRequest(page, rankType);
		RankResponse rankResponse = (RankResponse) controller.sendCommand(playerRankRequest);
		List<Player> result = rankResponse.getPlayers();
		log.debug("PlayerRankService result:" + result);
		return result;
	}

	@Override
	public List<Player> listRank(SessionController controller, RankType rankType, int fromPage, int toPage, boolean store) throws Exception {
		List<Player> result = new ArrayList<Player>();
		log.debug("PlayerRankService request: fromPage=" + fromPage + ", toPage=" + toPage + ", rankType=" + rankType);

		setProgress(new Integer(0));
		for (int page = fromPage; page <= toPage; page++) {
			result.addAll(listRankPage(controller, rankType, page));
			if (store) {
				// do not return objects, but write them to database instead
				try {
					switch (rankType) {
					case PRESTIGE:
						playerService.updatePrestige(result);
						break;
					case LEVEL:
						playerService.updateLevel(result);
						break;
					}
				} catch (Exception e) {
					log.error("Error persisting player", e);
				}
				result.clear();
			}

			setProgress(100 * (page - fromPage + 1) / (toPage - fromPage + 1));
		}

		log.debug("PlayerRankService result:" + result);
		return result;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public Integer getProgress() {
		return this.progress;
	}

	public synchronized void setProgress(Integer progress) {
		this.progress = progress;
	}

}
