package me.traduciendo.bunkers.utils.extra;

import lombok.Getter;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.utils.Formatter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Cooldown {

    private final Map<UUID, Long> cooldowns;
    private final Bunkers instance;

    public Cooldown(Bunkers instance) {
        this.cooldowns = new HashMap<>();
        this.instance = instance;
        getInstance().getCooldowns().add(this);
    }

    public void clean() {
        cooldowns.values().removeIf(next -> next - System.currentTimeMillis() < 0L);
    }

    public void applyCooldown(Player player, int seconds) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public void applyCooldownTicks(Player player, int ticks) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + ticks);
    }

    public boolean hasCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId()) && (cooldowns.get(player.getUniqueId()) >= System.currentTimeMillis());
    }

    public void removeCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

    public String getRemaining(Player player) {
        long l = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis();
        return Formatter.getRemaining(l, true);
    }

    public String getRemainingOld(Player player) {
        long l = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis();
        return Formatter.getRemaining(l, false);
    }
}