package me.traduciendo.bunkers.tasks;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Koth;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.TimeUtils;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KothTask extends BukkitRunnable {

    private final Match match;
    private final Bunkers instance;
    private final Koth koth;

    public KothTask(Match match, Bunkers instace){
        this.match = match;
        this.instance = instace;
        this.koth = new Koth(match);
    }

    @Override public void run() {

        if (koth.getCapSeconds() <= 0) {
            Team team = Bunkers.getInstance().getTeamManager().getByPlayer(koth.getController(),match);
            instance.getMatchManager().setWinner(match,team);
            cancel();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            String started = Bunkers.getInstance().getLangConfig().getConfig().getString("KOTH.CONTROLLING");
            String attempting = Bunkers.getInstance().getLangConfig().getConfig().getString("KOTH.ATTEMPTING");
            String knocked = Bunkers.getInstance().getLangConfig().getConfig().getString("KOTH.KNOCKED");
            if (match.getSpectatorList().contains(player)) return;

            if (koth.isInsideArea(player.getLocation())) {
                if(instance.getPlayerManager().getPlayerData(player).getPlayerState() == PlayerState.SPECTATING || instance.getPlayerManager().getPlayerData(player).getPlayerState() == PlayerState.LOBBY || instance.getPlayerManager().getPlayerData(player).getPlayerState() == PlayerState.WAITING){
                    return;
                }
                if (koth.getController() == null) {
                    koth.setController(player);
                    String message = started
                            .replace("%player%", player.getName())
                            .replace("%time%", TimeUtils.formatIntoMMSS(match.getKothTime()));
                    Bukkit.broadcastMessage(CC.translate(message));
                }
                if (koth.getCapSeconds() % 30 == 0 && koth.getCapSeconds() != 600) {
                    String message = attempting
                            .replace("%player%", player.getName())
                            .replace("%time%", TimeUtils.formatIntoMMSS(match.getKothTime()));
                    Bukkit.broadcastMessage(CC.translate(message));
                }
            } else {
                if (koth.getController() == player) {
                    String message = knocked
                            .replace("%player%", player.getName())
                            .replace("%time%", TimeUtils.formatIntoMMSS(match.getKothTime()));
                    Bukkit.broadcastMessage(CC.translate(message));
                    koth.setController(null);
                    match.setKothTime(koth.getSeconds());
                    koth.setCapSeconds(koth.getSeconds());

                }
            }
        }

        if (koth.getController() != null) {
            match.setKothTime(match.getKothTime() -1);
            koth.setCapSeconds(koth.getCapSeconds() - 1);
        }
    }
}
