package me.traduciendo.bunkers.menus.match.lobby;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class JoinMatchMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return Bunkers.getInstance().getPlayMenuConfig().getConfig().getString("TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        int slot = 0;
        for(Match match : plugin.getMatchManager().matchMap.values()){
            buttons.put(slot,new JoinMatchButton(match));
            slot++;
        }

        return buttons;
    }

    @Override
    public int getSize(){
        return 9 * 1;
    }

    @AllArgsConstructor
    private class JoinMatchButton extends Button{
        private final Match match;

        @Override
        public ItemStack getButtonItem(Player player) {
            switch (match.getMatchState()){
                case WAITING:
                    return new ItemBuilder(Material.STAINED_CLAY).durability(5).name(CC.translate(Bunkers.getInstance().getPlayMenuConfig().getConfig().getString("BUTTONS.WAITING.TITLE").replace("%match_name%", match.getName().replace("Ranked", " &7(Ranked)"))))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(" "),
                                            CC.translate("&eStatus: &6Waiting"),
                                            CC.translate("&ePlayers: &f" + match.getPlayerList().size() + "&7/&f20"),
                                            CC.translate(""),
                                            CC.translate("&6Click to join!")))
                            .build();
                case STARTING:
                    return new ItemBuilder(Material.STAINED_CLAY).durability(4).name(CC.translate(Bunkers.getInstance().getPlayMenuConfig().getConfig().getString("BUTTONS.STARTING.TITLE").replace("%match_name%", match.getName().replace("Ranked", " &7(Ranked)"))))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(" "),
                                            CC.translate("&eStatus: &aStarting"),
                                            CC.translate("&ePlayers: &f" + match.getPlayerList().size() + "&7/&f20"),
                                            CC.translate(""),
                                            CC.translate("&6Click to join!")))
                            .build();
                case ACTIVE:
                    return new ItemBuilder(Material.STAINED_CLAY).durability(14).name(CC.translate(Bunkers.getInstance().getPlayMenuConfig().getConfig().getString("BUTTONS.PLAYING.TITLE").replace("%match_name%", match.getName().replace("Ranked", " &7(Ranked)"))))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(" "),
                                            CC.translate("&eStatus: &cIn-Game"),
                                            CC.translate("&ePlayers: &f" + match.getPlayerList().size() + "&7/&f20"),
                                            CC.translate(""),
                                            CC.translate("&6Click to spectate!")))
                            .build();
                case ENDING:
                    return new ItemBuilder(Material.STAINED_CLAY).durability(7).name(CC.translate(Bunkers.getInstance().getPlayMenuConfig().getConfig().getString("BUTTONS.ENDING.TITLE").replace("%match_name%", match.getName().replace("Ranked", " &7(Ranked)"))))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(" "),
                                            CC.translate("&eStatus: &4Ending"),
                                            CC.translate("&ePlayers: &f" + match.getPlayerList().size() + "&7/&f20"),
                                            CC.translate(""),
                                            CC.translate("&6Click to queue for next match!")))
                            .build();
                case RESTARTING:
                    return new ItemBuilder(Material.STAINED_CLAY).durability(7).name(CC.translate(Bunkers.getInstance().getPlayMenuConfig().getConfig().getString("BUTTONS.RESTARTING.TITLE").replace("%match_name%", match.getName().replace("Ranked", " &7(Ranked)"))))
                            .lore(
                                    Lists.newArrayList(
                                            CC.translate(" "),
                                            CC.translate("&eStatus: &4Restarting"),
                                            CC.translate("&ePlayers: &f" + match.getPlayerList().size() + "&7/&f20"),
                                            CC.translate(""),
                                            CC.translate("&6Click to queue for next match!")))
                            .build();
            }
            return null;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbar){
            PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

            switch (this.match.getMatchState()){
                case STARTING:
                case WAITING:
                    player.closeInventory();
                    instance.getMatchManager().joinPlayer(match,player);
                    break;
                case ACTIVE:
                    player.closeInventory();
                    instance.getMatchManager().joinSpectator(match,player);
                    break;
                case ENDING:
                case RESTARTING:
                    break;
            }

        }
    }
}
