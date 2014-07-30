package haypi.service;

import haypi.comm.DeleteLetterRequest;
import haypi.comm.ListInboxRequest;
import haypi.comm.ListInboxResponse;
import haypi.comm.ListLetterRequest;
import haypi.comm.ListLetterResponse;
import haypi.comm.ScoutRequest;
import haypi.comm.SessionController;
import haypi.comm.SessionTimeoutException;
import haypi.model.pojo.Cell;
import haypi.model.pojo.CellType;
import haypi.model.pojo.CellXY;
import haypi.model.pojo.Letter;
import haypi.model.pojo.LetterType;
import haypi.model.pojo.Player;
import haypi.model.pojo.ScoutInfo;
import haypi.model.pojo.ScoutLetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("scoutService")
public class ScoutServiceImpl implements ScoutService {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	protected CellService cellService;
	@Autowired
	protected PlayerService playerService;

	protected Integer progress;

	@Override
	public void scout(SessionController controller, int x, int y) throws Exception {
		log.debug("ScoutService request: x=" + x + " y=" + y);

		ScoutRequest request = new ScoutRequest(x, y);
		controller.sendCommand(request);

	}

	public ScoutInfo getScoutInfo(SessionController controller, int x, int y) throws Exception {
		ListInboxRequest request1 = new ListInboxRequest();
		ListInboxResponse response1 = (ListInboxResponse) controller.sendCommand(request1);
		for (Letter letter : response1.getLetters()) {
			if (letter.isUnread() && letter.getType() == LetterType.SCOUT) {
				ListLetterRequest request2 = new ListLetterRequest(letter.getId());
				ListLetterResponse response2 = (ListLetterResponse) controller.sendCommand(request2);
				ScoutLetter scoutLetter = (ScoutLetter) response2.getLetter();
				ScoutInfo scoutInfo = scoutLetter.getScoutInfo();
				if (scoutInfo != null) {
					log.debug(scoutInfo.getArmy());
					if (scoutInfo.x == x && scoutInfo.y == y) {
						DeleteLetterRequest request3 = new DeleteLetterRequest(letter.getId());
						controller.sendCommand(request3);
						return scoutInfo;
					}
				}
			}
		}
		return null;
	}

	private List<ScoutInfo> getUnreadScoutInfo(SessionController controller) throws Exception {
		List<ScoutInfo> results = new ArrayList<ScoutInfo>();

		ListInboxRequest request1 = new ListInboxRequest();
		ListInboxResponse response1 = (ListInboxResponse) controller.sendCommand(request1);
		log.debug(response1.getLetters().size() + " unread letters");
		for (Letter letter : response1.getLetters()) {
			if (letter.isUnread() && letter.getType() == LetterType.SCOUT) {
				ListLetterRequest request2 = new ListLetterRequest(letter.getId());
				ListLetterResponse response2 = (ListLetterResponse) controller.sendCommand(request2);
				try {
					ScoutLetter scoutLetter = (ScoutLetter) response2.getLetter();
					ScoutInfo scoutInfo = scoutLetter.getScoutInfo();
					if (scoutInfo != null) {
						log.debug(scoutInfo.getArmy());
						results.add(scoutInfo);
						DeleteLetterRequest request3 = new DeleteLetterRequest(letter.getId());
						controller.sendCommand(request3);
					}
				} catch (Exception e) {
					log.error("Error parsing letter", e);
				}
			}
		}
		return results;
	}

	@Override
	public void scout(SessionController controller, int x, int y, int radius, boolean onlyCities, boolean onlyTroops, int minPlayerLevel,
			List<ScoutInfo> results) throws Exception {
		setProgress(new Integer(0));
		List<Cell> cells = cellService.getCellByXYAndRangeAndLevel(controller.getServerName(), x, y, radius, minPlayerLevel);
		scout(controller, cells, results, onlyCities, onlyTroops);
	}

	@Override
	public void scout(SessionController controller, Player player, boolean onlyCities, boolean onlyTroops, List<ScoutInfo> results) throws Exception {
		setProgress(new Integer(0));
		List<Cell> cells = cellService.getCellByOwner(controller.getServerName(), player.getName());
		scout(controller, cells, results, onlyCities, onlyTroops);
	}

	public void scout(SessionController controller, List<Cell> cells, List<ScoutInfo> results, boolean onlyCities, boolean onlyTroops)
			throws Exception {
		Map<CellXY, Cell> recentCells = new HashMap<CellXY, Cell>();
		int scoutCounter = 0;
		for (int i = 0; i < cells.size(); i++) {
			Cell cell = cells.get(i);
			if (!onlyCities || (cell.getType() == CellType.MAIN_CITY.ordinal() || cell.getType() == CellType.BRANCH_CITY.ordinal())) {
				try {
					scout(controller, cell.getX(), cell.getY());
					scoutCounter++;
					recentCells.put(cell.getCellXY(), cell);
					if (scoutCounter >= 10) {
						List<ScoutInfo> recentScoutInfoList = getUnreadScoutInfo(controller);
						for (ScoutInfo recentInfo : recentScoutInfoList) {
							Cell recentCell = recentCells.get(new CellXY(controller.getServerName(), recentInfo.x, recentInfo.y));
							recentInfo.setCell(recentCell);
						}
						if (onlyTroops) {
							for (ScoutInfo scoutInfo : recentScoutInfoList) {
								if (scoutInfo.getMinPrestigeValue() > 0) {
									// setAlliance(scoutInfo);
									results.add(scoutInfo);
								}
							}
						} else {
							// setAlliance(recentScoutInfoList);
							results.addAll(recentScoutInfoList);
						}
						Collections.sort(results);
						recentCells.clear();
						scoutCounter = 0;
					}
				} catch (SessionTimeoutException ste) {
					log.warn("Session timeout");
					throw ste;
				} catch (Exception e) {
					if (e.getCause() != null && e.getCause() instanceof SessionTimeoutException) {
						log.warn("Session timeout");
						throw e;
					}
					log.warn("Error scanning " + cell.getX() + ":" + cell.getY(), e);
				}
			}
			setProgress(100 * (i + 1) / cells.size() - 1);
		}
		if (recentCells.size() > 0) {
			List<ScoutInfo> recentScoutInfoList = getUnreadScoutInfo(controller);
			for (ScoutInfo recentInfo : recentScoutInfoList) {
				Cell recentCell = recentCells.get(new CellXY(controller.getServerName(), recentInfo.x, recentInfo.y));
				recentInfo.setCell(recentCell);
			}
			if (onlyTroops) {
				for (ScoutInfo scoutInfo : recentScoutInfoList) {
					if (scoutInfo.getMinPrestigeValue() > 0) {
						// setAlliance(scoutInfo);
						results.add(scoutInfo);
					}
				}
			} else {
				// setAlliance(recentScoutInfoList);
				results.addAll(recentScoutInfoList);
			}

			Collections.sort(results);
			recentCells.clear();
		}
		setProgress(100);
	}

	/*
	private void setAlliance(ScoutInfo scoutInfo) {
		// default alliance: from cell
		scoutInfo.setAlliance(scoutInfo.getCell().getAlliance());
		String playerName = scoutInfo.getPlayer();
		if (playerName != null && playerName.length() > 0) {
			Player player = playerService.getPlayer(playerName);
			if (player != null) {
				// alliance from player might be more recent
				scoutInfo.setAlliance(player.getAlliance());
			}
		}
	}

	private void setAlliance(List<ScoutInfo> scoutInfoList) {
		for (ScoutInfo scoutInfo : scoutInfoList) {
			setAlliance(scoutInfo);
		}
	}*/

	public void setCellService(CellService cellService) {
		this.cellService = cellService;
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
