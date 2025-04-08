package me.traduciendo.bunkers.waypoints.listener;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.waypoints.WaypointManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class WaypointListener  {
    private final Bunkers instance;
    private final WaypointManager manager;
    public WaypointListener(WaypointManager manager) {
        this.instance = manager.getInstance();
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (instance.getClientHook().getClients().isEmpty()) return;

        Player player = e.getPlayer();
    }
}