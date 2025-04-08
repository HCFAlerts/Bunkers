package me.traduciendo.bunkers.menus.shop;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ProductButton extends Button {
    private final Material material;
    private final int data;
    private final String name;
    private final List<String> description;
    private final int price;
    private final boolean canbeRightCl;
    private final int amountIfRightCl;

    public ProductButton(Material material, int data, String name, List<String> description, int price){
        this.material = material;
        this.data = data;
        this.name = name;
        this.description = description;
        this.price = price;
        this.canbeRightCl = false;
        this.amountIfRightCl = 0;
    }

    public ProductButton(Material material, String name, List<String> description, int price){
        this.material = material;
        this.data = 0;
        this.name = name;
        this.description = description;
        this.price = price;
        this.canbeRightCl = false;
        this.amountIfRightCl = 0;
    }
    public ProductButton(Material material, int data, String name, int price){
        this.material = material;
        this.data = data;
        this.name = name;
        this.price = price;
        this.description = Lists.newArrayList(CC.LINE,"&71 x "+name,CC.LINE,"&7Price: &a$"+price);
        this.canbeRightCl = false;
        this.amountIfRightCl = 0;

    }
    public ProductButton(Material material, String name,List<String> description, int price, boolean canbeRightCl, int amountIfRightCl){
        this.material = material;
        this.data = 0;
        this.name = name;
        this.price = price;
        this.description = description;
        this.canbeRightCl = false;
        this.amountIfRightCl = 0;

    }
    public ProductButton(Material material, int data, String name,List<String> description, int price, boolean canbeRightCl, int amountIfRightCl){
        this.material = material;
        this.data = data;
        this.name = name;
        this.price = price;
        this.description =description;
        this.canbeRightCl = false;
        this.amountIfRightCl = 0;

    }
    public ProductButton(Material material, String name, int price){
        this.material = material;
        this.data = 0;
        this.name = name;
        this.price = price;
        this.description = Lists.newArrayList(CC.LINE,"&71 x "+name,CC.LINE,"&7Price: &a$"+price);
        this.canbeRightCl = false;
        this.amountIfRightCl = 0;

    }

    @Override
    public ItemStack getButtonItem(Player player) {

        for(String string: description){
            if(string.contains("%price%")){
                string.replace("%price%",String.valueOf(price));
            }
        }
        return new ItemBuilder(material).durability(data).name(CC.translate(name)).lore(CC.translate(description)).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType ,  int hotbar){
        if(!canbeRightCl){
            instance.getStoreManager().buy(this,player);
        }else {
            switch (clickType){
                case LEFT:
                case SHIFT_LEFT:
                    instance.getStoreManager().buy(this,player);
                    break;
                case RIGHT:
                case SHIFT_RIGHT:
                    instance.getStoreManager().buy(this,player);
            }
        }
    }
}
