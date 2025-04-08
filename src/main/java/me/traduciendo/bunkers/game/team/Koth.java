package me.traduciendo.bunkers.game.team;

import lombok.Getter;
import lombok.Setter;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Koth  {
    @Getter @Setter private int seconds = 600;
    @Getter @Setter private int capSeconds = 600;
    @Getter @Setter
    Player controller;

    private final Match match;

    public Koth(Match match) {
        String decreased = Bunkers.getInstance().getLangConfig().getConfig().getString("KOTH.DECREASED");
        this.match = match;
        new BukkitRunnable() {
            @Override public void run() {
                if (match.getGameTime() == 900) {
                    setSeconds(300);
                    match.setKothTime(300);
                    if (capSeconds > 300) {
                        setCapSeconds(300);
                    }
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', decreased));
                }
            }
        }.runTaskTimerAsynchronously(Bunkers.getInstance(), 0L, 20L);
    }

    public boolean isInsideArea(Location location) {
        return new Cuboid(match.getArena().getKothClaimMax().toBukkitLocation(),match.getArena().getKothClaimMin().toBukkitLocation()).contains(location);
    }
}