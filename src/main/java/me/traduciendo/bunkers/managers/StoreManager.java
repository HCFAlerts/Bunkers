package me.traduciendo.bunkers.managers;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.menus.shop.ProductButton;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class StoreManager  {
    private final Bunkers instance;

    public StoreManager(Bunkers instance) {
       this.instance = instance;
    }


    public void buy(ProductButton button, Player player){
        if(!this.hasAvaliableSlot(player)){
            player.sendMessage(CC.RED+"You don't have empty slots in your inventory");
            return;
        }
        if(!this.hasEnoughMoney(player,button.getPrice())){
            player.sendMessage(CC.RED+"You don't have enough money");
            return;
        }
        player.sendMessage(CC.translate("&aYou've bought a &e"+button.getName()+" &afor &e$"+button.getPrice()));
        instance.getPlayerManager().getPlayerData(player).setBalance(instance.getPlayerManager().getPlayerData(player).getBalance()-button.getPrice());
        player.getInventory().addItem(new ItemStack(button.getMaterial(),1,(short) button.getData()));
    }

    public void buyAmount(ProductButton button, Player player){
        if(!this.hasAvaliableSlot(player)){
            player.sendMessage(CC.RED+"You don't have empty slots in your inventory");
            return;
        }
        if(!this.hasEnoughMoney(player,button.getPrice()*button.getAmountIfRightCl())){
            player.sendMessage(CC.RED+"You don't have enough money");
            return;
        }
        player.sendMessage(CC.translate("&aYou've bought &ex"+button.getAmountIfRightCl()+" "+button.getName()+"s &afor &e$"+button.getPrice()*button.getAmountIfRightCl()));
        instance.getPlayerManager().getPlayerData(player).setBalance(instance.getPlayerManager().getPlayerData(player).getBalance()-button.getPrice());
        player.getInventory().addItem(new ItemStack(button.getMaterial(),button.getAmountIfRightCl(),(short) button.getData()));
    }


    private boolean hasAvaliableSlot(Player player){
        Inventory inv = player.getInventory();
        Boolean check =false;
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                check = true;
                break;
            }
        }

        return check;
    }

    private boolean hasEnoughMoney(Player player, int price){
        PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

        if(playerData.getBalance()>=price){
            return true;
        }else {
            return false;
        }


    }
}
