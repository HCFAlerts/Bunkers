package me.traduciendo.bunkers.menus.arena;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.arena.Arena;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import me.traduciendo.bunkers.utils.menu.Button;
import me.traduciendo.bunkers.utils.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ArenaEditorMenu extends Menu {

    private final Bunkers plugin = Bunkers.getInstance();

    @Override
    public String getTitle(Player player) {
        return "Arena Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = Maps.newHashMap();
        AtomicInteger value = new AtomicInteger();
        for(Arena arena : plugin.getArenaManager().arenaMap.values()){
            buttons.put(value.getAndIncrement(),new ArenaEditButton(arena));
        }
        return buttons;
    }

    @Override
    public int getSize(){
        return 9 * 1;
    }


    @RequiredArgsConstructor
    private class ArenaEditButton extends Button{

        private final Arena arena;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(
                    arena.getIconMaterial()).
                    durability(arena.getIconData()).
                    name(CC.translate("&6&l" + arena.getName())).
                    lore(CC.YELLOW + "Click to edit.").build();
        }
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbar){

        }
    }
}
