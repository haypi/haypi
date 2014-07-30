package haypi.service;

import haypi.comm.SessionController;
import haypi.model.pojo.Player;
import haypi.model.pojo.ScoutInfo;

import java.util.List;

public interface ScoutService {
	public void scout(SessionController controller, int x, int y) throws Exception;

	public void scout(SessionController controller, int x, int y, int radius, boolean onlyCities, boolean onlyTroops, int minPlayerLevel, List<ScoutInfo> results) throws Exception;

	public void scout(SessionController controller, Player player, boolean onlyCities, boolean onlyTroops, List<ScoutInfo> results) throws Exception;

	public ScoutInfo getScoutInfo(SessionController controller, int x, int y) throws Exception;

	public Integer getProgress();
}
