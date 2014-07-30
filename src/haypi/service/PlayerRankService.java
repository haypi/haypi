package haypi.service;

import haypi.comm.RankType;
import haypi.comm.SessionController;
import haypi.model.pojo.Player;

import java.util.List;

public interface PlayerRankService {

	public List<Player> listRankPage(SessionController controller, RankType rankType, int page) throws Exception;

	public List<Player> listRank(SessionController controller, RankType rankType, int fromPage, int toPage, boolean store) throws Exception;

	public Integer getProgress();

}