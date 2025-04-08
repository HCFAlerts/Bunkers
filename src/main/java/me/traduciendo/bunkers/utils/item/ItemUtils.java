package me.traduciendo.bunkers.utils.item;


import org.bukkit.Material;
import org.bukkit.block.Block;

public class ItemUtils {

    public static boolean isOreCanMine(Block block){
        if(block.getType().isBlock()){
            switch (block.getType()){
                case DIAMOND_ORE:
                    return true;
                case COAL_ORE:
                    return true;
                case GOLD_ORE:
                    return true;
                case IRON_ORE:
                    return true;
                default:
                    return false;
            }
        }else {
            return false;
        }
    }

    public static Material getTypeByOre(Material material){
            switch (material){
                case DIAMOND_ORE:
                    return Material.DIAMOND;
                case COAL_ORE:
                    return Material.COAL;
                case GOLD_ORE:
                    return Material.GOLD_INGOT;
                case IRON_ORE:
                    return Material.IRON_INGOT;
                default:
                    return null;
            }

    }
}
