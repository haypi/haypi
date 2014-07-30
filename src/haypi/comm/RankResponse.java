package haypi.comm;

import haypi.model.pojo.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class RankResponse extends BaseResponse {

	public int pageSize;
	List<Player> players = new ArrayList<Player>();

	public RankResponse(RankRequest request, String response) throws IOException, HaypiException {
		super(request, response);

		String line = readLine();
		pageSize = Integer.parseInt(line);
		Player player = null;
		try {
			while (StringUtils.hasLength((line = readLine()))) {
				player = new Player();
				player.setServer(request.getServerName());
				switch (request.getRankType()) {
				case PRESTIGE:
					player.setPrestigeRank(new Integer(line));
					break;
				case LEVEL:
					player.setLevelRank(new Integer(line));
					break;
				}
				player.setName(readLine());
				switch (request.getRankType()) {
				case PRESTIGE:
					player.setPrestige(new Long(readLine()));
					break;
				case LEVEL:
					player.setLevel(new Integer(readLine()));
					break;
				}
				player.setAlliance(readLine());

				players.add(player);
			}
		} catch (Exception e) {
			log.error("Error " + e + ", player=" + player + " response=" + response);
		}
	}

	@Override
	public String toString() {
		return players.toString();
	}

	public List<Player> getPlayers() {
		return players;
	}
}
