package me.traduciendo.bunkers.nametags;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.nethandler.server.CBPacketOverrideNametags;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheatBreakerNametag implements Listener {
    private final Bunkers plugin;

    public CheatBreakerNametag(Bunkers plugin) {
        this.plugin = plugin;
    }

//    @EventHandler
//    public void onPlayerQuit(PlayerQuitEvent event) {
//        Player player = event.getPlayer();
//        resetNametag(player);
//    }

    public void updateNametag(Player player) {
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
        PlayerState state = playerData.getPlayerState();

        //String line1 = "";
        String line2 = "";

        switch (state) {
            case LOBBY:
            case WAITING:
                int playerPing = getPing(player);
                //line1 = "&f" + player.getName();
                line2 = "&7Ping: &f" + playerPing + "ms";
                break;

            case FIGHTING:
                Match match = plugin.getMatchManager().getMatch(player);
                Team team = plugin.getTeamManager().getByPlayer(player, match);

                if (match != null && team != null) {
                    //line1 = TeamColor.getChatColor(playerData.getCurrentTeamColor()) + player.getName();

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

                    line2 = "&6[" + TeamColor.getChatColor(playerData.getCurrentTeamColor()) + playerData.getCurrentTeamColor().name() + " &7┃ " + dtrColor + team.getDTR() + "&6]";
                } else {
                    //line1 = "&c" + player.getName();
                    line2 = "&6[&cNone &7┃ &e0.0&6]";
                }
                break;

            case SPECTATING:
                //line1 = "&7" + player.getName();
                line2 = "&7[Spectator]";
                break;

            default:
                playerPing = getPing(player);
                //line1 = "&f" + player.getName();
                line2 = "&7Ping: &f" + playerPing + "ms";
                break;
        }

        // CC.translate(line1)
        applyNametag(player, CC.translate(line2));
    }

    private void applyNametag(Player player, String line2) {
        UUID playerId = player.getUniqueId();

        List<String> lines = new ArrayList<>();

//        if (!line1.isEmpty()) {
//            lines.add(line1);
//        }
        if (!line2.isEmpty()) {
            lines.add(line2);
        }

//        Nametag nametag = Nametag.builder()
//                .lines(lines)
//                .build();

//        lines.add(CC.translate(line1));
        lines.add(CC.translate(line2));

        CBPacketOverrideNametags packet = new CBPacketOverrideNametags(playerId, lines);

        CheatBreakerAPI.getInstance().sendPacket(player, packet);
    }

    private void resetNametag(Player player) {
        List<String> emptyLines = new ArrayList<>();
        CBPacketOverrideNametags packet = new CBPacketOverrideNametags(player.getUniqueId(), emptyLines);

        CheatBreakerAPI.getInstance().sendPacket(player, packet);
    }

    private int getPing(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            return (int) handle.getClass().getField("ping").get(handle);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

//    public void resetAllNametags(Player viewer) {
//        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(viewer.getUniqueId());
//        apolloPlayerOpt.ifPresent(nametagModule::resetNametags);
//    }

    public void startNametagUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    updateNametag(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 0L);
    }
}

