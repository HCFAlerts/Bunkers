package me.traduciendo.bunkers.managers;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.CustomLocation;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LobbyManager  {
    private CustomLocation lobbyLocation;

    private final Bunkers instance;
    public LobbyManager(Bunkers instance){
        this.instance = instance;
        this.load();
    }
    private void load(){
        FileConfiguration config = instance.getSettingsConfig().getConfig();

        CustomLocation lobbyLocation = CustomLocation.stringToLocation(config.getString("LOBBY.SPAWN"));
        this.lobbyLocation = lobbyLocation;
    }

    public void sendToLobby(Player player) {
        PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

        playerData.setPlayerState(PlayerState.LOBBY);
        playerData.setCurrentMatchName(null);
        playerData.setCurrentTeamColor(null);
        playerData.setPearlCooldown(0);
        playerData.setBalance(0);

        player.setFlying(false);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.teleport(lobbyLocation.toBukkitLocation());
        player.getInventory().clear();

        ConfigurationSection itemConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.LOBBY.PLAY");
        int slot = itemConfig.getInt("SLOT", 4);

        if (slot < 0 || slot > 8) {
            slot = 4;
        }

        player.getInventory().setItem(slot, joinMatchItem());
    }

    public ItemStack joinMatchItem(){
        ConfigurationSection leaveConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.LOBBY.PLAY");

        String materialName = leaveConfig.getString("ITEM", "NETHER_STAR");
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null) {
            material = Material.NETHER_STAR;
        }

        int data = leaveConfig.getInt("DATA", 0);

        String displayName = CC.translate(leaveConfig.getString("DISPLAY_NAME", "&aPlay"));

        List<String> loreConfig = leaveConfig.getStringList("LORE");
        List<String> lore = new ArrayList<>();
        for (String line : loreConfig) {
            lore.add(CC.translate(line));
        }

        ItemStack item = new ItemBuilder(material)
                .name(displayName)
                .lore(lore)
                .build();

        item.setDurability((short) data);

        return item;
    }

}
