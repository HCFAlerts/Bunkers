package me.traduciendo.bunkers.menus.arena;

import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.menus.match.spectating.TeleportPlayerMenu;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ArenaTeamChooseMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return null;
    }

    @Override
    public int getSize(){
        return 9 * 3;
    }

    @RequiredArgsConstructor
    private class TeleportTeamButton extends Button{

        private final Team team;

        @Override
        public ItemStack getButtonItem(Player player) {
            switch (this.team.getTeamColor()){
                case Red:
                    return new ItemBuilder(Material.WOOL).durability(14).name(CC.translate("&c&lRed Team"))
                            .build();
                case Blue:
                    return new ItemBuilder(Material.WOOL).durability(11).name(CC.translate("&1&lBlue Team"))
                            .build();
                case Yellow:
                    return new ItemBuilder(Material.WOOL).durability(4).name(CC.translate("&e&lYellow Team"))
                            .build();
                case Green:
                    return new ItemBuilder(Material.WOOL).durability(13).name(CC.translate("&a&lGreen Team"))
                           .build();
            }
            return null;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbar){
            player.closeInventory();
            new TeleportPlayerMenu(this.team).openMenu(player);

        }
    }
}
