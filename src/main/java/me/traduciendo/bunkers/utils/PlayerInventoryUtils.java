package me.traduciendo.bunkers.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerInventoryUtils {

    public static int getDiamonds(Player player){
        AtomicInteger amount = new AtomicInteger();
        player.getInventory().forEach(itemStack -> {
            if(itemStack == null){
                return;
            }
            if(itemStack.getType()== Material.AIR){
                return;
            }
            if(itemStack.getType()!=Material.DIAMOND){
                return;
            }
            amount.set(amount.get()+itemStack.getAmount());
        });
        return amount.get();
    }
    public static int getIron(Player player){
        AtomicInteger amount = new AtomicInteger();
        player.getInventory().forEach(itemStack -> {
            if(itemStack == null){
                return;
            }
            if(itemStack.getType()==Material.AIR){
                return;
            }
            if(itemStack.getType()!=Material.IRON_INGOT){
                return;
            }
            amount.set(amount.get()+itemStack.getAmount());
        });
        return amount.get();
    }
    public static  int getCoal(Player player){
        AtomicInteger amount = new AtomicInteger();
        player.getInventory().forEach(itemStack -> {
            if(itemStack == null){
                return;
            }
            if(itemStack.getType()==Material.AIR){
                return;
            }
            if(itemStack.getType()!=Material.COAL){
                return;
            }
            amount.set(amount.get()+itemStack.getAmount());
        });
        return amount.get();
    }
    public static int getGold(Player player){
        AtomicInteger amount = new AtomicInteger();
        player.getInventory().forEach(itemStack -> {
            if(itemStack == null){
                return;
            }
            if(itemStack.getType()==Material.AIR){
                return;
            }
            if(itemStack.getType()!=Material.GOLD_INGOT){
                return;
            }
            amount.set(amount.get()+itemStack.getAmount());
        });
        return amount.get();
    }

    public static boolean hasResources(Player player){
        int amount = 0;
        amount = amount+getDiamonds(player)+getGold(player)+getIron(player)+getCoal(player);

        if(amount>0){
            return true;
        }else {
            return false;
        }
    }


    @SuppressWarnings("deprecation")
    public static boolean consumeItem(Player player, int count, Material mat) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;
    }


    public static void removeDiamond(Player player, int amount){
       consumeItem(player,amount,Material.DIAMOND);
    }
    public static void removeGold(Player player, int amount){
        consumeItem(player,amount,Material.GOLD_INGOT);
    }
    public static void removeIron(Player player, int amount){
        consumeItem(player,amount,Material.IRON_INGOT);
    }
    public static void removeCoal(Player player, int amount){
        consumeItem(player,amount,Material.COAL);
    }

}
