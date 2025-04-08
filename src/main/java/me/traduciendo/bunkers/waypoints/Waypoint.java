package me.traduciendo.bunkers.waypoints;

import lombok.Getter;
import me.traduciendo.bunkers.Bunkers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.UnaryOperator;

@Getter
public class Waypoint {

    private final String name;
    private final WaypointType waypointType;
    private final String color;
    private final boolean enabled;

    private final Bunkers instance;
    public Waypoint(WaypointManager manager, String name, WaypointType waypointType, String color, boolean enabled) {
        this.instance = manager.getInstance();
        this.name = name;
        this.waypointType = waypointType;
        this.color = color;
        this.enabled = enabled;
    }

    public void remove(Player player, Location location, UnaryOperator<String> replacer) {
        if (location == null) return;
        if (!enabled) return;
        instance.getClientHook().removeWaypoint(player, location, this, replacer);
    }

    public void send(Player player, Location location, UnaryOperator<String> replacer) {
        if (location == null) return;
        if (!enabled) return;
        getInstance().getClientHook().sendWaypoint(player, location, this, replacer);
    }
}