package me.traduciendo.bunkers.clients.type;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates;
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.lunarclient.bukkitapi.serverrule.LunarClientAPIServerRule;
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

public class LunarClient implements Client {

    public LunarClient(ClientHook manager) {
    }

    @Override
    public void overrideNametags(Player target, Player viewer, List<String> tag) {
        LunarClientAPI.getInstance().overrideNametag(target, tag, viewer);
    }

    @Override
    public void sendWaypoint(Player player, Location location, Waypoint waypoint, UnaryOperator<String> replacer) {
        LunarClientAPI.getInstance().sendWaypoint(player, new LCWaypoint(
                replacer.apply(waypoint.getName()),
                (waypoint.getWaypointType() == WaypointType.KOTH  ? location.subtract(0, 1, 0) : location),
                Color.decode(replacer.apply(waypoint.getColor())).getRGB(),
                true,
                true
        ));
    }

    @Override
    public void removeWaypoint(Player player, Location location, Waypoint waypoint, UnaryOperator<String> replacer) {
        LunarClientAPI.getInstance().removeWaypoint(player, new LCWaypoint(
                replacer.apply(waypoint.getName()),
                (waypoint.getWaypointType() == WaypointType.KOTH  ? location.subtract(0, 1, 0) : location),
                Color.decode(replacer.apply(waypoint.getColor())).getRGB(),
                true,
                true
        ));
    }

    @Override
    public void handleJoin(Player player) {
            LunarClientAPIServerRule.setRule(ServerRule.LEGACY_COMBAT, true);
            LunarClientAPIServerRule.sendServerRule(player);
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

        LCPacketTeammates packet = new LCPacketTeammates(player.getUniqueId(), 1L, teamMap);
        LunarClientAPI.getInstance().sendTeammates(player, packet);
    }

    @Override
    public void clearTeamViewer(Player player) {
        LCPacketTeammates packet = new LCPacketTeammates(player.getUniqueId(), 1L, new HashMap<>());
        LunarClientAPI.getInstance().sendTeammates(player, packet);
    }

    @Override
    public void giveStaffModules(Player player) {
        LunarClientAPI.getInstance().giveAllStaffModules(player);
    }

    @Override
    public void disableStaffModules(Player player) {
        LunarClientAPI.getInstance().disableAllStaffModules(player);
    }
}