package me.traduciendo.bunkers.menus.match.spectating;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class ChooseTeleportTeam extends Menu {
    private final Match match;

    @Override
    public String getTitle(Player player) {
        return Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("TEAM.TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = Maps.newHashMap();
        buttons.put(10, new TeleportTeamButton(match.getRedTeam()));
        buttons.put(12, new TeleportTeamButton(match.getBlueTeam()));
        buttons.put(14, new TeleportTeamButton(match.getYellowTeam()));
        buttons.put(16, new TeleportTeamButton(match.getGreenTeam()));
        return buttons;
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
                return new ItemBuilder(Material.WOOL).durability(14).name(CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("TEAM.BUTTONS.RED.TITLE")))
                        .lore(
                                Lists.newArrayList(
                                        CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getStringList("TEAM.BUTTONS.RED.LORE"))))
                                    .build();
                case Blue:
                    return new ItemBuilder(Material.WOOL).durability(11).name(CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("TEAM.BUTTONS.BLUE.TITLE")))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getStringList("TEAM.BUTTONS.BLUE.LORE"))))
                                    .build();
                case Yellow:
                    return new ItemBuilder(Material.WOOL).durability(4).name(CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("TEAM.BUTTONS.YELLOW.TITLE")))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getStringList("TEAM.BUTTONS.YELLOW.LORE"))))
                                    .build();
                case Green:
                    return new ItemBuilder(Material.WOOL).durability(13).name(CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getString("TEAM.BUTTONS.GREEN.TITLE")))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(Bunkers.getInstance().getTeleportMenuConfig().getConfig().getStringList("TEAM.BUTTONS.GREEN.LORE"))))
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
