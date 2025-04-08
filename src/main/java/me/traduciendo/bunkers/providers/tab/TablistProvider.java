package me.traduciendo.bunkers.providers.tab;

import com.google.common.collect.Lists;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.utils.TimeUtils;
import me.traduciendo.bunkers.utils.tablist.adapter.TabAdapter;
import me.traduciendo.bunkers.utils.tablist.entry.TabEntry;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class TablistProvider implements TabAdapter {
    private final Bunkers plugin = Bunkers.getInstance();
    FileConfiguration config = Bunkers.getInstance().getTabConfig().getConfig();

    @Override
    public String getHeader(Player player) {
        List<String> headerLines = config.getStringList("TABLIST.HEADER");

        if (headerLines.isEmpty()) {
            return "";
        }

        return String.join("\n", headerLines);
    }

    @Override
    public String getFooter(Player player) {
        List<String> footerLines = config.getStringList("TABLIST.FOOTER");

        if (footerLines.isEmpty()) {
            return "";
        }

        return String.join("\n", footerLines);
    }

    private String getDTRColor(double dtr) {
        if (dtr >= 6.0) {
            return "&a";
        } else if (dtr < 6.0 && dtr >= 1.0) {
            return "&a";
        } else if (dtr == 0.0) {
            return "&e";
        } else if (dtr < -1.0 && dtr > -3.0) {
            return "&c";
        } else {
            return "&4";
        }
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> lines = Lists.newArrayList();
        PlayerData practicePlayerData = plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        switch (practicePlayerData.getPlayerState()) {
            case WAITING:
            case LOBBY:
                int column = 0;
                int row = 0;
                for (Player online : Bukkit.getOnlinePlayers()) {
                    try {
                        lines.add(new TabEntry(column, row, online.getName()).setPing(
                                ((CraftPlayer) online).getHandle().ping));
                        if (column++ < 3) {
                            continue;
                        }
                        column = 0;

                        if (row++ < 19) {
                            continue;
                        }
                        row = 0;
                    } catch (Exception ignored) {
                        break;
                    }
                }
                break;
            case SPECTATING:
                Match spec = plugin.getMatchManager().getMatch(player);
                Team specLoc = plugin.getTeamManager().getByLocation(player.getLocation(), spec);

                lines.add(new TabEntry(0, 2, CC.translate("&6&lSpectating")));
                lines.add(new TabEntry(0, 3, CC.translate("&fGame Time: &6" + TimeUtils.formatIntoMMSS(spec.getGameTime()))));
                lines.add(new TabEntry(0, 4, CC.translate("&fKoTH: &6" + TimeUtils.formatIntoMMSS(spec.getKothTime()))));

                lines.add(new TabEntry(0, 6, CC.translate("&6&lLocation")));
                lines.add(new TabEntry(0, 7, specLoc != null ? TeamColor.getChatColor(specLoc.getTeamColor()) + specLoc.getTeamColor().name() : CC.translate("&4Warzone")));
                lines.add(new TabEntry(0, 8, CC.translate("&f(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ") [&c" + getCardinalDirection(player) + "&f]")));

                lines.add(new TabEntry(0, 10, CC.translate("&6&lGame Info")));
                lines.add(new TabEntry(0, 11, CC.translate("&fMap: &6" + spec.getArena().getName().replace("Ranked", " &7(Ranked)"))));
                lines.add(new TabEntry(0, 12, CC.translate("&fPlayers: &6" + spec.getPlayerList().size() + "/20")));

                lines.add(new TabEntry(0, 14, CC.translate("&6&lDTR")));
                lines.add(new TabEntry(0, 15, CC.translate("&cRed&7: " + getDTRColor(spec.getRedTeam().getDTR()) + spec.getRedTeam().getDTR())));
                lines.add(new TabEntry(0, 16, CC.translate("&9Blue&7: " + getDTRColor(spec.getBlueTeam().getDTR()) + spec.getBlueTeam().getDTR())));
                lines.add(new TabEntry(0, 17, CC.translate("&eYellow&7: " + getDTRColor(spec.getYellowTeam().getDTR()) + spec.getYellowTeam().getDTR())));
                lines.add(new TabEntry(0, 18, CC.translate("&aGreen&7: " + getDTRColor(spec.getGreenTeam().getDTR()) + spec.getGreenTeam().getDTR())));

                lines.add(new TabEntry(1, 0, CC.translate("&6&lBunkers-01")));

                lines.add(new TabEntry(1, 2, CC.translate("&c&lRed Team")));
                lines.add(new TabEntry(1, 3, CC.translate("&c" + formatPlayerSlot(0, spec.getRedTeam()))));
                lines.add(new TabEntry(1, 4, CC.translate("&c" + formatPlayerSlot(1, spec.getRedTeam()))));
                lines.add(new TabEntry(1, 5, CC.translate("&c" + formatPlayerSlot(2, spec.getRedTeam()))));
                lines.add(new TabEntry(1, 6, CC.translate("&c" + formatPlayerSlot(3, spec.getRedTeam()))));
                lines.add(new TabEntry(1, 7, CC.translate("&c" + formatPlayerSlot(4, spec.getRedTeam()))));

                lines.add(new TabEntry(1, 9, CC.translate("&a&lGreen Team")));
                lines.add(new TabEntry(1, 10, CC.translate("&a" + formatPlayerSlot(0, spec.getGreenTeam()))));
                lines.add(new TabEntry(1, 11, CC.translate("&a" + formatPlayerSlot(1, spec.getGreenTeam()))));
                lines.add(new TabEntry(1, 12, CC.translate("&a" + formatPlayerSlot(2, spec.getGreenTeam()))));
                lines.add(new TabEntry(1, 13, CC.translate("&a" + formatPlayerSlot(3, spec.getGreenTeam()))));
                lines.add(new TabEntry(1, 14, CC.translate("&a" + formatPlayerSlot(4, spec.getGreenTeam()))));

                lines.add(new TabEntry(2, 2, CC.translate("&9&lBlue Team")));
                lines.add(new TabEntry(2, 3, CC.translate("&9" + formatPlayerSlot(0, spec.getBlueTeam()))));
                lines.add(new TabEntry(2, 4, CC.translate("&9" + formatPlayerSlot(1, spec.getBlueTeam()))));
                lines.add(new TabEntry(2, 5, CC.translate("&9" + formatPlayerSlot(2, spec.getBlueTeam()))));
                lines.add(new TabEntry(2, 6, CC.translate("&9" + formatPlayerSlot(3, spec.getBlueTeam()))));
                lines.add(new TabEntry(2, 7, CC.translate("&9" + formatPlayerSlot(4, spec.getBlueTeam()))));

                lines.add(new TabEntry(2, 9, CC.translate("&e&lYellow Team")));
                lines.add(new TabEntry(2, 10, CC.translate("&e" + formatPlayerSlot(0, spec.getYellowTeam()))));
                lines.add(new TabEntry(2, 11, CC.translate("&e" + formatPlayerSlot(1, spec.getYellowTeam()))));
                lines.add(new TabEntry(2, 12, CC.translate("&e" + formatPlayerSlot(2, spec.getYellowTeam()))));
                lines.add(new TabEntry(2, 13, CC.translate("&e" + formatPlayerSlot(3, spec.getYellowTeam()))));
                lines.add(new TabEntry(2, 14, CC.translate("&e" + formatPlayerSlot(4, spec.getYellowTeam()))));
                break;
            case FIGHTING:
                Match match = plugin.getMatchManager().getMatch(player);
                Team team = plugin.getTeamManager().getByPlayer(player, match);
                Team teamAt = plugin.getTeamManager().getByLocation(player.getLocation(), match);

                lines.add(new TabEntry(0, 2, CC.translate("&6&lTeam Info")));
                lines.add(new TabEntry(0, 3, CC.translate("&fDTR: &r" + getDTRColor(team.getDTR()) + team.getDTR())));
                lines.add(new TabEntry(0, 4, CC.translate("&fOnline: &6" + team.getPlayers().size())));

                lines.add(new TabEntry(0, 6, CC.translate("&6&lLocation")));
                lines.add(new TabEntry(0, 7, teamAt != null ? TeamColor.getChatColor(teamAt.getTeamColor()) + teamAt.getTeamColor().name() : CC.translate("&4Warzone")));
                lines.add(new TabEntry(0, 8, CC.translate("&f(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ") [&c" + getCardinalDirection(player) + "&f]")));

                lines.add(new TabEntry(0, 10, CC.translate("&6&lGame Info")));
                lines.add(new TabEntry(0, 11, CC.translate("&fMap: &6" + match.getArena().getName().replace("Ranked", " &7(Ranked)"))));
                lines.add(new TabEntry(0, 12, CC.translate("&fPlayers: &6" + match.getPlayerList().size() + "/20")));

                lines.add(new TabEntry(0, 14, CC.translate("&6&lDTR")));
                lines.add(new TabEntry(0, 15, CC.translate("&cRed&7: " + getDTRColor(match.getRedTeam().getDTR()) + match.getRedTeam().getDTR())));
                lines.add(new TabEntry(0, 16, CC.translate("&9Blue&7: " + getDTRColor(match.getBlueTeam().getDTR()) + match.getBlueTeam().getDTR())));
                lines.add(new TabEntry(0, 17, CC.translate("&eYellow&7: " + getDTRColor(match.getYellowTeam().getDTR()) + match.getYellowTeam().getDTR())));
                lines.add(new TabEntry(0, 18, CC.translate("&aGreen&7: " + getDTRColor(match.getGreenTeam().getDTR()) + match.getGreenTeam().getDTR())));

                lines.add(new TabEntry(1,0,CC.translate("&6&lBunkers-01")));

                lines.add(new TabEntry(1,2,CC.translate("&c&lRed Team")));
                lines.add(new TabEntry(1,3,CC.translate("&c" + formatPlayerSlot(0,match.getRedTeam()))));
                lines.add(new TabEntry(1,4,CC.translate("&c" + formatPlayerSlot(1,match.getRedTeam()))));
                lines.add(new TabEntry(1,5,CC.translate("&c" + formatPlayerSlot(2,match.getRedTeam()))));
                lines.add(new TabEntry(1,6,CC.translate("&c" + formatPlayerSlot(3,match.getRedTeam()))));
                lines.add(new TabEntry(1,7,CC.translate("&c" + formatPlayerSlot(4,match.getRedTeam()))));

                lines.add(new TabEntry(1,9,CC.translate("&a&lGreen Team")));
                lines.add(new TabEntry(1,10,CC.translate("&a" + formatPlayerSlot(0,match.getGreenTeam()))));
                lines.add(new TabEntry(1,11,CC.translate("&a" + formatPlayerSlot(1,match.getGreenTeam()))));
                lines.add(new TabEntry(1,12,CC.translate("&a" + formatPlayerSlot(2,match.getGreenTeam()))));
                lines.add(new TabEntry(1,13,CC.translate("&a" + formatPlayerSlot(3,match.getGreenTeam()))));
                lines.add(new TabEntry(1,14,CC.translate("&a" + formatPlayerSlot(4,match.getGreenTeam()))));

                lines.add(new TabEntry(2,2,CC.translate("&9&lBlue Team")));
                lines.add(new TabEntry(2,3,CC.translate("&9" + formatPlayerSlot(0,match.getBlueTeam()))));
                lines.add(new TabEntry(2,4,CC.translate("&9" + formatPlayerSlot(1,match.getBlueTeam()))));
                lines.add(new TabEntry(2,5,CC.translate("&9" + formatPlayerSlot(2,match.getBlueTeam()))));
                lines.add(new TabEntry(2,6,CC.translate("&9" + formatPlayerSlot(3,match.getBlueTeam()))));
                lines.add(new TabEntry(2,7,CC.translate("&9" + formatPlayerSlot(4,match.getBlueTeam()))));

                lines.add(new TabEntry(2,9,CC.translate("&e&lYellow Team")));
                lines.add(new TabEntry(2,10,CC.translate("&e" + formatPlayerSlot(0,match.getYellowTeam()))));
                lines.add(new TabEntry(2,11,CC.translate("&e" + formatPlayerSlot(1,match.getYellowTeam()))));
                lines.add(new TabEntry(2,12,CC.translate("&e" + formatPlayerSlot(2,match.getYellowTeam()))));
                lines.add(new TabEntry(2,13,CC.translate("&e" + formatPlayerSlot(3,match.getYellowTeam()))));
                lines.add(new TabEntry(2,14,CC.translate("&e" + formatPlayerSlot(4,match.getYellowTeam()))));
        }
        return lines;
    }

    private String formatPlayerSlot(int slot, Team team){

        if(slot+1>team.getPlayers().size()){
            return CC.translate("");

        }else {
            Player player = team.getPlayers().get(slot);
            return CC.translate(player.getName());
        }

    }
    public static String getCardinalDirection(Player player) {
        double rot = (player.getLocation().getYaw() - 90) % 360;
        if (rot < 0) rot += 360.0;
        return getDirection(rot);
    }
    private static String getDirection(double rot) {
        if (0 <= rot && rot < 22.5) {
            return "W";
        } else if (22.5 <= rot && rot < 67.5) {
            return "NW";
        } else if (67.5 <= rot && rot < 112.5) {
            return "N";
        } else if (112.5 <= rot && rot < 157.5) {
            return "NE";
        } else if (157.5 <= rot && rot < 202.5) {
            return "E";
        } else if (202.5 <= rot && rot < 247.5) {
            return "SE";
        } else if (247.5 <= rot && rot < 292.5) {
            return "S";
        } else if (292.5 <= rot && rot < 337.5) {
            return "SW";
        } else if (337.5 <= rot && rot < 360.0) {
            return "W";
        } else {
            return null;
        }
    }
}
