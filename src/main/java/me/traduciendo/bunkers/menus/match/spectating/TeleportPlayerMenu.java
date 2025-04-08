package me.traduciendo.bunkers.menus.match.spectating;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TeleportPlayerMenu extends Menu {
    private final Team team;

    @Override
    public String getTitle(Player player) {
        return Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("PLAYER.TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = Maps.newHashMap();

        AtomicInteger slot = new AtomicInteger(10);
        team.getPlayers().forEach(teamMembers -> {
            buttons.put(slot.getAndAdd(2),new TeleportPlayerButton(teamMembers));
        });
        return buttons;
    }

    @Override
    public int getSize(){
        return 9 * 3;
    }

    @RequiredArgsConstructor
    private class TeleportPlayerButton extends Button{
        private final Player teleportPlayer;
        String name = Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("PLAYER.PLAYER_BUTTON.TITLE");

        @Override
        public ItemStack getButtonItem(Player player) {
            ConfigurationSection leaveConfig = Bunkers.getInstance().getTeleportMenuConfig().getConfig().getConfigurationSection("PLAYER.PLAYER_BUTTON");

            List<String> loreConfig = leaveConfig.getStringList("LORE");
            List<String> lore = new ArrayList<>();
            for (String line : loreConfig) {
                lore.add(CC.translate(line));
            }

            return new ItemBuilder(Material.SKULL_ITEM).
                    durability(3).
                    owner(player.getName()).
                    name(CC.translate(name)+teleportPlayer.getName()).
                    lore(CC.translate(lore)).
                    build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbar){
            player.closeInventory();
            player.teleport(teleportPlayer.getLocation());
        }
    }
}
