package me.traduciendo.bunkers.clients;

import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.waypoints.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.UnaryOperator;

public interface Client {

    void overrideNametags(Player target, Player viewer, List<String> tag);

    void sendWaypoint(Player player, Location location, Waypoint waypoint, UnaryOperator<String> replacer);

    void removeWaypoint(Player player, Location location, Waypoint waypoint, UnaryOperator<String> replacer);

    void handleJoin(Player player);

    void sendTeamViewer(Player player, List<Player> allPlayers, Match match);

    void clearTeamViewer(Player player);

    void giveStaffModules(Player player);

    void disableStaffModules(Player player);
}