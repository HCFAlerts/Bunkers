package me.traduciendo.bunkers.managers;

import com.google.common.collect.Maps;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;


public class PlayerManager {

    public Map<UUID, PlayerData> playerDataMap = Maps.newHashMap();

    private final Bunkers instance;
    public PlayerManager(Bunkers instance) {
        this.instance =instance;
    }


    public PlayerData getPlayerData(UUID uuid){

        if (Bukkit.getPlayer(uuid) != null) {
            if (!this.playerDataMap.containsKey(uuid)) {
                createPlayerData(Bukkit.getPlayer(uuid));
            }
        }

        return this.playerDataMap.get(uuid);
    }
    public PlayerData getPlayerData(Player player){
        return getPlayerData(player.getUniqueId());
    }

    public void createPlayerData(Player player){
        PlayerData playerData = new PlayerData(player.getUniqueId());
        playerData.setPlayerState(PlayerState.LOBBY);
        playerData.setCurrentMatchName(null);
        this.playerDataMap.put(player.getUniqueId(),playerData);

    }
}
