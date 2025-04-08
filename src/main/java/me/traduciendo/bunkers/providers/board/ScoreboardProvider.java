package me.traduciendo.bunkers.providers.board;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.utils.scoreboard.scoreboard.Board;
import me.traduciendo.bunkers.utils.scoreboard.scoreboard.BoardAdapter;
import me.traduciendo.bunkers.utils.scoreboard.scoreboard.cooldown.BoardCooldown;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.utils.Formatter;
import me.traduciendo.bunkers.utils.TimeUtils;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class ScoreboardProvider implements BoardAdapter, Listener {

  private final Bunkers instance;

  public ScoreboardProvider(Bunkers instance) {
    this.instance = instance;
    Bukkit.getPluginManager().registerEvents(this, instance);
  }

  @Override
  public String getTitle(Player player) {
    return CC.translate("&6&lBunkers");
  }

  @Override
  public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
    PlayerData playerData = instance.getPlayerManager().getPlayerData(player);
    if (playerData == null) {
      instance.getLogger().warning(
              player.getName() + "'s player data is null (" + this.getClass().getName() + ")"
      );
      return Collections.emptyList();
    }

    switch (playerData.getPlayerState()) {
      case LOBBY:
        return inLobbyScoreboard(player);
      case WAITING:
        return inArenaWaiting(player);
      case SPECTATING:
        return inSpectatorMode(player);
      case FIGHTING:
        return inActiveMatch(player);
      default:
        return Collections.emptyList();
    }
  }

  private List<String> inLobbyScoreboard(Player player) {
    List<String> strings = new LinkedList<>();
    Match match = instance.getMatchManager().getMatch("Classic");
    String players = match == null ? String.valueOf(0) : String.valueOf(match.getPlayerList().size());
    strings.add(CC.translate(CC.LINE));
    strings.add(CC.translate("&fIn Lobby: &6" + Bukkit.getServer().getOnlinePlayers().size()));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6UnRanked"));
    strings.add(CC.translate(" &fWaiting: &60"));
    strings.add(CC.translate(" &fPlaying: &6" + players));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6Ranked"));
    strings.add(CC.translate(" &fWaiting: &60"));
    strings.add(CC.translate(" &fPlaying: &60"));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6fladepvp.cc"));
    strings.add(CC.translate(CC.LINE));
    return strings;
  }

  private List<String> inArenaWaiting(Player player) {
    List<String> strings = new LinkedList<>();
    PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

    if (playerData == null) {
      instance.getLogger().warning("PlayerData is null for player: " + player.getName());
      return strings;
    }

    Match match = instance.getMatchManager().getMatch(playerData.getCurrentMatchName());
    if (match == null) {
      instance.getLogger().warning("Match is null for player: " + player.getName());
      return strings;
    }

    strings.add(CC.translate(CC.LINE));
    strings.add(CC.translate("&6Game State:"));
    strings.add(CC.translate(" &fPre-Game"));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6Status:"));
    strings.add(CC.translate(" &fStarts in &6" + match.getStartTime() + "s"));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&fTeam: &r" + TeamColor.getChatColor(playerData.getCurrentTeamColor()) + playerData.getCurrentTeamColor().name()));
    strings.add(CC.translate("&fPlayers: &6" + match.getPlayerList().size() + "&f/&620"));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6fladepvp.cc"));
    strings.add(CC.translate(CC.LINE));

    return strings;
  }

  private List<String> inSpectatorMode(Player player) {
    List<String> strings = new LinkedList<>();
    PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

    if (playerData == null) {
      instance.getLogger().warning("PlayerData is null for player: " + player.getName());
      return strings;
    }

    Match match = instance.getMatchManager().getMatch(playerData.getCurrentMatchName());
    if (match == null) {
      instance.getLogger().warning("Match is null for player: " + player.getName());
      return strings;
    }

    strings.add(CC.translate(CC.LINE));
    strings.add(CC.translate("&fGame Time: &6" + TimeUtils.formatIntoMMSS(match.getGameTime())));
    strings.add(CC.translate("&fKoTH: &6" + TimeUtils.formatIntoMMSS(match.getKothTime())));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6fladepvp.cc"));
    strings.add(CC.translate(CC.LINE));

    return strings;
  }

  private List<String> inActiveMatch(Player player) {
    List<String> strings = new LinkedList<>();
    PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

    if (playerData == null) {
      instance.getLogger().warning("PlayerData is null for player: " + player.getName());
      return strings;
    }

    Match match = instance.getMatchManager().getMatch(playerData.getCurrentMatchName());
    if (match == null) {
      instance.getLogger().warning("Match is null for player: " + player.getName());
      return strings;
    }

    Team team = instance.getTeamManager().getByPlayer(player, match);
    if (team == null) {
      instance.getLogger().warning("Team is null for player: " + player.getName());
      return strings;
    }

    if (instance.getTimerManager() == null || instance.getTimerManager().getTeleportTimer() == null) {
      instance.getLogger().warning("TimerManager or TeleportTimer is null.");
      return strings;
    }

    double dtr = team.getDTR();

    String dtrColor;
    if (dtr >= 6.0) {
      dtrColor = "&a";
    } else if (dtr < 6.0 && dtr >= 1.0) {
      dtrColor = "&a";
    } else if (dtr == 0.0) {
      dtrColor = "&e";
    } else if (dtr < -1.0 && dtr > -3.0) {
      dtrColor = "&c";
    } else {
      dtrColor = "&4";
    }

    strings.add(CC.translate(CC.LINE));
    strings.add(CC.translate("&fGame Time: &6" + TimeUtils.formatIntoMMSS(match.getGameTime())));
    strings.add(CC.translate("&fKoTH: &6" + TimeUtils.formatIntoMMSS(match.getKothTime())));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&fTeam: &r") + TeamColor.getChatColor(playerData.getCurrentTeamColor()) + playerData.getCurrentTeamColor().name());
    strings.add(CC.translate("&fDTR: &r" + dtrColor + team.getDTR()));
    strings.add(CC.translate(""));
    if (instance.getTimerManager().getTeleportTimer().getRemaining(player) > 0) {
      strings.add(CC.translate("&fHome: &6" + Formatter.getRemaining(instance.getTimerManager().getTeleportTimer().getRemaining(player), true)));
    }
    strings.add(CC.translate("&fKills: &6" + playerData.getKills()));
    strings.add(CC.translate("&fBalance: &6$" + playerData.getBalance()));
    strings.add(CC.translate(""));
    strings.add(CC.translate("&6fladepvp.cc"));
    strings.add(CC.translate(CC.LINE));

    return strings;
  }
}