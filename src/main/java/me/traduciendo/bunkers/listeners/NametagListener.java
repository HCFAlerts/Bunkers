package me.traduciendo.bunkers.listeners;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.nametag.Nametag;
import com.lunarclient.apollo.module.nametag.NametagModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.chat.CC;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NametagListener implements Listener {
    private final Bunkers plugin;
    private final NametagModule nametagModule;

    public NametagListener(Bunkers plugin) {
        this.plugin = plugin;
        this.nametagModule = Apollo.getModuleManager().getModule(NametagModule.class);
    }

    public void updateNametag(Player player) {
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
        PlayerState state = playerData.getPlayerState();

        String line1 = "";
        String line2 = "";

        switch (state) {
            case LOBBY:
            case WAITING:
                int playerPing = getPing(player);
                line1 = "&f" + player.getName();
                line2 = "&7Ping: &f" + playerPing + "ms";
                break;

            case FIGHTING:
                Match match = plugin.getMatchManager().getMatch(player);
                Team team = plugin.getTeamManager().getByPlayer(player, match);

                if (match != null && team != null) {
                    line1 = TeamColor.getChatColor(playerData.getCurrentTeamColor()) + player.getName();

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
                    line1 = "&c" + player.getName();
                    line2 = "&6[&cNone &7┃ &e0.0&6]";
                }
                break;

            case SPECTATING:
                line2 = "&7" + player.getName();
                line1 = "&7[Spectator]";
                break;

            default:
                playerPing = getPing(player);
                line1 = "&f" + player.getName();
                line2 = "&7Ping: &f" + playerPing + "ms";
                break;
        }

        applyNametag(player, CC.translate(line1), CC.translate(line2));
    }

    private void applyNametag(Player player, String line1, String line2) {
        List<Component> lines = new ArrayList<>();

        if (!line1.isEmpty()) {
            lines.add(Component.text(line1));
        }
        if (!line2.isEmpty()) {
            lines.add(Component.text(line2));
        }

        Nametag nametag = Nametag.builder()
                .lines(lines)
                .build();

        nametagModule.overrideNametag(Recipients.ofEveryone(), player.getUniqueId(), nametag);
    }

    public void resetNametag(Player player) {
        nametagModule.resetNametag(Recipients.ofEveryone(), player.getUniqueId());
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

    public void resetAllNametags(Player viewer) {
        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(viewer.getUniqueId());
        apolloPlayerOpt.ifPresent(nametagModule::resetNametags);
    }

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
