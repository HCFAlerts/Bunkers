package me.traduciendo.bunkers.waypoints;

import lombok.Getter;
import lombok.Setter;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.utils.Utils;
import me.traduciendo.bunkers.waypoints.listener.WaypointListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
@Setter
public class WaypointManager  {

    private Waypoint kothWaypoint;
    private Waypoint hqWaypoint;
    private Waypoint focusWaypoint;
    private Waypoint rallyWaypoint;

    private final Bunkers instance;

    public WaypointManager(Bunkers instance) {
        this.instance = instance;

        this.kothWaypoint = new Waypoint(this,"KoTH",WaypointType.KOTH, "#BD3D21" ,true);
        this.hqWaypoint = new Waypoint(this,"HQ",WaypointType.HQ, "#75E319" ,true);
        this.focusWaypoint = new Waypoint(this,"Focus",WaypointType.FOCUS, "#FFC900",true);
        this.rallyWaypoint = new Waypoint(this,"Rally",WaypointType.RALLY_POINT, "#0FD5D5",true);

        this.loadWorlds();
        new WaypointListener(this);
    }

    public void enableStaffModules(Player player) {
        getInstance().getClientHook().giveStaffModules(player);
    }

    public void disableStaffModules(Player player) {
        getInstance().getClientHook().disableStaffModules(player);
    }

    public void loadWorlds() {
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
            for (World world : Bukkit.getServer().getWorlds()) {
                world.setWeatherDuration(Integer.MAX_VALUE);
                world.setThundering(false);
                world.setStorm(false);
                world.setGameRuleValue("mobGriefing", "false");

                if (Utils.isModernVer()) {
                    world.setGameRuleValue("maxEntityCramming", "0");
                    world.setGameRuleValue("doTraderSpawning", "false");
                    world.setGameRuleValue("doPatrolSpawning", "false");
                    world.setGameRuleValue("doInsomnia", "false");
                    world.setGameRuleValue("disableRaids", "true");
                }
            }
        }, 20 * 10L);
    }
}