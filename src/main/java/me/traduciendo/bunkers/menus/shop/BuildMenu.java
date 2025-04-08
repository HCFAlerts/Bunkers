package me.traduciendo.bunkers.menus.shop;

import com.google.common.collect.Maps;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class BuildMenu extends Menu {

    public BuildMenu(){
        this.setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Build Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = Maps.newHashMap();

        buttons.put(48,new ProductButton(Material.DIAMOND_PICKAXE,"&aDiamond Pickaxe",50));
        buttons.put(49,new ProductButton(Material.DIAMOND_AXE,"&aDiamond Axe",50));
        buttons.put(50,new ProductButton(Material.DIAMOND_SPADE,"&aDiamond Shovel",50));

        return buttons;
    }

    @Override
    public int getSize(){
        return 9*6;
    }
}