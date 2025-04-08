package me.traduciendo.bunkers.menus.shop;

import com.google.common.collect.Maps;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class CombatMenu extends Menu {

    public CombatMenu(){
        this.setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Combat Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = Maps.newHashMap();

        buttons.put(9,new ProductButton(Material.ARROW,"&aArrow",10));
        buttons.put(9+9,new ProductButton(Material.DIAMOND_SWORD,"&aDiamond Sword",150));
        buttons.put(11,new ProductButton(Material.BOW,"&aBow",150));
        buttons.put(11+9,new ProductButton(Material.ENDER_PEARL,"&aEnder Pearl",25));

        buttons.put(22-9,new ProductButton(Material.POTION,8238,"&aInvisibility Potion",1000));
        buttons.put(22,new ProductButton(Material.POTION,16421,"&aInstant Health II Potion",10));
        buttons.put(22+9,new ProductButton(Material.POTION,8259,"&aFire Resistance Potion",100));
        buttons.put(22+9+9,new ProductButton(Material.POTION,8226,"&aSpeed II Potion",50));
        buttons.put(22+9+9+1,new ProductButton(Material.COOKED_BEEF,"&aCooked Beef",5));

        buttons.put(10,new ProductButton(Material.DIAMOND_HELMET,"&aDiamond Helmet",200));
        buttons.put(10+9,new ProductButton(Material.DIAMOND_CHESTPLATE,"&aDiamond Chestplate",400));
        buttons.put(10+9+9,new ProductButton(Material.DIAMOND_LEGGINGS,"&aDiamond Leggings",300));
        buttons.put(10+9+9+9,new ProductButton(Material.DIAMOND_BOOTS,"&aDiamond Boots",100));

        return buttons;
    }

    @Override
    public int getSize(){
        return 9*6;
    }
}
