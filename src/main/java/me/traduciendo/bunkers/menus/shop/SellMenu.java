package me.traduciendo.bunkers.menus.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.PlayerInventoryUtils;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SellMenu extends Menu {

    public SellMenu(){
        this.setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Sell Items";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = Maps.newHashMap();
        buttons.put(1,new SellOreButton(Material.DIAMOND));
        buttons.put(1+2,new SellOreButton(Material.GOLD_INGOT));
        buttons.put(1+4,new SellOreButton(Material.IRON_INGOT));
        buttons.put(1+6,new SellOreButton(Material.COAL));

        return buttons;
    }

    @Override
    public int getSize(){
        return 9 * 1;
    }


    @RequiredArgsConstructor
    private class SellOreButton extends Button {

        private final Material material;

        @Override
        public ItemStack getButtonItem(Player player) {
            switch (this.material){
                case DIAMOND:
                    if(PlayerInventoryUtils.getDiamonds(player)==0){
                        return new ItemBuilder(Material.DIAMOND).name(CC.AQUA+"Sell Diamond")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Diamonds for &e$50"),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }else {
                        return new ItemBuilder(Material.DIAMOND).name(CC.AQUA+"Sell Diamond")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Diamonds for &e$50"),
                                                CC.translate("&7Left-Click to sell &fx"+PlayerInventoryUtils.getDiamonds(player)+" &7Diamonds for &e$"+PlayerInventoryUtils.getDiamonds(player)*50),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }
                case GOLD_INGOT:
                    if(PlayerInventoryUtils.getGold(player)==0){
                        return new ItemBuilder(Material.GOLD_INGOT).name(CC.YELLOW+"Sell Gold Ingots")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Gold Ingots for &e$35"),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }else {
                        return new ItemBuilder(Material.GOLD_INGOT).name(CC.YELLOW+"Sell Gold Ingots")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Gold Ingots for &e$35"),
                                                CC.translate("&7Left-Click to sell &fx"+PlayerInventoryUtils.getGold(player)+" &7Gold Ingots for &e$"+PlayerInventoryUtils.getGold(player)*35),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }
                case IRON_INGOT:
                    if(PlayerInventoryUtils.getIron(player)==0){
                        return new ItemBuilder(Material.IRON_INGOT).name(CC.WHITE+"Sell Iron Ingots")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Iron Ingots for &e$20"),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }else {
                        return new ItemBuilder(Material.IRON_INGOT).name(CC.WHITE+"Sell Iron Ingots")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Iron Ingots for &e$20"),
                                                CC.translate("&7Left-Click to sell &fx"+PlayerInventoryUtils.getIron(player)+" &7Iron Ingots for &e$"+PlayerInventoryUtils.getIron(player)*20),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }

                case COAL:
                    if(PlayerInventoryUtils.getCoal(player)==0){
                        return new ItemBuilder(Material.COAL).name(CC.GRAY+"Sell Coal")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Coal for &e$10"),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }else {
                        return new ItemBuilder(Material.COAL).name(CC.GRAY+"Sell Coal")
                                .lore(
                                        Lists.newArrayList(
                                                CC.translate("&7&m--------------------------------"),
                                                CC.translate("&7Left-Click to sell &fx1 &7Coal for &e$10"),
                                                CC.translate("&7Left-Click to sell &fx"+ PlayerInventoryUtils.getCoal(player)+"&7Coal for &e$"+PlayerInventoryUtils.getCoal(player)*10),
                                                CC.translate("&7&m--------------------------------")
                                        )).build();
                    }
            }
            return null;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbar){
            PlayerData playerData = instance.getPlayerManager().getPlayerData(player);
            switch (clickType){
                case LEFT:
                case SHIFT_LEFT:
                    switch (this.material){
                        case DIAMOND:
                            if(PlayerInventoryUtils.getDiamonds(player)==0){
                                player.sendMessage(CC.RED+"You don't have any diamonds");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x1 diamond for $50");
                                playerData.setBalance(playerData.getBalance()+50);
                                PlayerInventoryUtils.removeDiamond(player,1);
                            }
                        break;
                        case GOLD_INGOT:
                            if(PlayerInventoryUtils.getGold(player)==0){
                                player.sendMessage(CC.RED+"You don't have any gold ingots");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x1 gold ingot for $35");
                                playerData.setBalance(playerData.getBalance()+35);
                                PlayerInventoryUtils.removeGold(player,1);
                            }
                            break;
                        case IRON_INGOT:
                            if(PlayerInventoryUtils.getIron(player)==0){
                                player.sendMessage(CC.RED+"You don't have any iron ingots");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x1 iron ingot for $20");
                                playerData.setBalance(playerData.getBalance()+20);
                                PlayerInventoryUtils.removeIron(player,1);
                            }
                            break;
                        case COAL:
                            if(PlayerInventoryUtils.getCoal(player)==0){
                                player.sendMessage(CC.RED+"You don't have any coal");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x1 coal for $10");
                                playerData.setBalance(playerData.getBalance()+10);
                                PlayerInventoryUtils.removeCoal(player,1);

                            }
                            break;

                    }
                    break;
                case RIGHT:
                case SHIFT_RIGHT:
                    switch (this.material){
                        case DIAMOND:
                            if(PlayerInventoryUtils.getDiamonds(player)==0){
                                player.sendMessage(CC.RED+"You don't have any diamonds");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x"+PlayerInventoryUtils.getDiamonds(player)+" diamonds for $"+PlayerInventoryUtils.getDiamonds(player)*50);
                                playerData.setBalance(playerData.getBalance()+PlayerInventoryUtils.getDiamonds(player)*50);
                                PlayerInventoryUtils.removeDiamond(player,PlayerInventoryUtils.getDiamonds(player));
                            }
                            break;
                        case GOLD_INGOT:
                            if(PlayerInventoryUtils.getGold(player)==0){
                                player.sendMessage(CC.RED+"You don't have any gold ingots");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x"+PlayerInventoryUtils.getGold(player)+" gold ingots for $"+PlayerInventoryUtils.getGold(player)*50);
                                playerData.setBalance(playerData.getBalance()+PlayerInventoryUtils.getGold(player)*50);
                                PlayerInventoryUtils.removeGold(player,PlayerInventoryUtils.getGold(player));
                            }
                            break;
                        case IRON_INGOT:
                            if(PlayerInventoryUtils.getIron(player)==0){
                                player.sendMessage(CC.RED+"You don't have any iron ingots");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x"+PlayerInventoryUtils.getIron(player)+" iron ingots for $"+PlayerInventoryUtils.getIron(player)*50);
                                playerData.setBalance(playerData.getBalance()+PlayerInventoryUtils.getIron(player)*50);
                                PlayerInventoryUtils.removeIron(player,PlayerInventoryUtils.getIron(player));
                            }
                            break;
                        case COAL:
                            if(PlayerInventoryUtils.getCoal(player)==0){
                                player.sendMessage(CC.RED+"You don't have any coal");
                            }else {

                                player.sendMessage(CC.GREEN+"You sold x"+PlayerInventoryUtils.getCoal(player)+" coals for $"+PlayerInventoryUtils.getCoal(player)*50);
                                playerData.setBalance(playerData.getBalance()+PlayerInventoryUtils.getCoal(player)*50);
                                PlayerInventoryUtils.removeCoal(player,PlayerInventoryUtils.getCoal(player));
                            }
                            break;

                    }
                    break;
            }

            new SellMenu().openMenu(player);
        }
    }

}
