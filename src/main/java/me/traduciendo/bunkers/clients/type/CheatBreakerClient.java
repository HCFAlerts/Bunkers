package me.traduciendo.bunkers.clients.type;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import com.cheatbreaker.nethandler.server.CBPacketTeammates;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.clients.Client;
import me.traduciendo.bunkers.clients.ClientHook;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.waypoints.Waypoint;
import me.traduciendo.bunkers.waypoints.WaypointType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.UnaryOperator;

public class CheatBreakerClient implements Client {

    public CheatBreakerClient(ClientHook manager) {
    }

    @Override
    public void overrideNametags(Player target, Player viewer, List<String> tag) {
        CheatBreakerAPI.getInstance().overrideNametag(target, tag, viewer);
    }

    @Override
    public void sendWaypoint(Player player, Location location, Waypoint waypoint, UnaryOperator<String> replacer) {
        CheatBreakerAPI.getInstance().sendWaypoint(player, new CBWaypoint(
                replacer.apply(waypoint.getName()),
                (waypoint.getWaypointType() == WaypointType.KOTH ? location.subtract(0, 1, 0) : location),
                Color.decode(replacer.apply(waypoint.getColor())).getRGB(),
                true,
                true
        ));
    }

    @Override
    public void removeWaypoint(Player player, Location location, Waypoint waypoint, UnaryOperator<String> replacer) {
        CheatBreakerAPI.getInstance().removeWaypoint(player, new CBWaypoint(
                replacer.apply(waypoint.getName()),
                (waypoint.getWaypointType() == WaypointType.KOTH  ? location.subtract(0, 1, 0) : location),
                Color.decode(replacer.apply(waypoint.getColor())).getRGB(),
                true,
                true
        ));
    }

    @Override
    public void handleJoin(Player player) {
    }

    @Override
    public void sendTeamViewer(Player player, List<Player> allPlayers, Match match) {
        Team playerTeam = Bunkers.getInstance().getTeamManager().getByPlayer(player, match);

        if (playerTeam == null) {
            return;
        }

        List<Player> teamMembers = new ArrayList<>();
        for (Player p : allPlayers) {
            if (Bunkers.getInstance().getTeamManager().getByPlayer(p, match) == playerTeam) {
                teamMembers.add(p);
            }
        }

        Map<UUID, Map<String, Double>> teamMap = new HashMap<>();

        for (Player member : teamMembers) {
            Map<String, Double> playerInfo = new HashMap<>();
            playerInfo.put("name", (double) member.getName().hashCode());
            playerInfo.put("posX", member.getLocation().getX());
            playerInfo.put("posY", member.getLocation().getY());
            playerInfo.put("posZ", member.getLocation().getZ());

            teamMap.put(member.getUniqueId(), playerInfo);
        }

        CBPacketTeammates packet = new CBPacketTeammates(player.getUniqueId(), 1L, teamMap);
        CheatBreakerAPI.getInstance().sendTeammates(player, packet);
    }

    @Override
    public void clearTeamViewer(Player player) {
    }

    @Override
    public void giveStaffModules(Player player) {
        CheatBreakerAPI.getInstance().giveAllStaffModules(player);
    }

    @Override
    public void disableStaffModules(Player player) {
        CheatBreakerAPI.getInstance().disableAllStaffModules(player);
    }
}