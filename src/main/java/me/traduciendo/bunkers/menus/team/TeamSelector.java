package me.traduciendo.bunkers.menus.team;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
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
@AllArgsConstructor
public class TeamSelector extends Menu {

    private Match match;

    @Override
    public String getTitle(Player player) {
        return "Team Selector";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(10,new TeamButton(TeamColor.Red,match));
        buttons.put(12,new TeamButton(TeamColor.Blue,match));
        buttons.put(14,new TeamButton(TeamColor.Yellow,match));
        buttons.put(16,new TeamButton(TeamColor.Green,match));

        return buttons;
    }

    @Override
    public int getSize(){
        return 9*3;
    }

    @AllArgsConstructor
    private class TeamButton extends Button {

        private TeamColor color;
        private Match match;

        @Override
        public ItemStack getButtonItem(Player player) {

            switch (color){
                case Red:
                    return new ItemBuilder(Material.WOOL).durability(14).name(CC.translate("&c&lRed"))
                            .lore(Lists.newArrayList(
                                    formatPlayerSlot(0,TeamColor.Red,match),
                                    formatPlayerSlot(1,TeamColor.Red,match),
                                    formatPlayerSlot(2,TeamColor.Red,match),
                                    formatPlayerSlot(3,TeamColor.Red,match),
                                    CC.translate("&eClick to join"))).build();
                case Blue:
                    return new ItemBuilder(Material.WOOL).durability(11).name(CC.translate("&1&lBlue"))
                            .lore(Lists.newArrayList(
                                    formatPlayerSlot(0,TeamColor.Blue,match),
                                    formatPlayerSlot(1,TeamColor.Blue,match),
                                    formatPlayerSlot(2,TeamColor.Blue,match),
                                    formatPlayerSlot(3,TeamColor.Blue,match),
                                    CC.translate("&eClick to join"))).build();
                case Yellow:
                    return new ItemBuilder(Material.WOOL).durability(4).name(CC.translate("&e&lYellow"))
                            .lore(Lists.newArrayList(
                                    formatPlayerSlot(0,TeamColor.Yellow,match),
                                    formatPlayerSlot(1,TeamColor.Yellow,match),
                                    formatPlayerSlot(2,TeamColor.Yellow,match),
                                    formatPlayerSlot(3,TeamColor.Yellow,match),
                                CC.translate("&eClick to join"))).build();
                case Green:
                    return new ItemBuilder(Material.WOOL).durability(5).name(CC.translate("&2&lGreen"))
                            .lore(Lists.newArrayList(
                                    formatPlayerSlot(0,TeamColor.Green,match),
                                    formatPlayerSlot(1,TeamColor.Green,match),
                                    formatPlayerSlot(2,TeamColor.Green,match),
                                    formatPlayerSlot(3,TeamColor.Green,match),
                                CC.translate("&eClick to join"))).build();
                default:
                    return null;
            }

        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarbutton){
            PlayerData playerData = instance.getPlayerManager().getPlayerData(player);
            Match match = instance.getMatchManager().getMatch(playerData.getCurrentMatchName());
            if(match == null){
                player.sendMessage(CC.RED+"You are not currently in a match");
            }else {
                switch (this.color){
                    case Red:
                        instance.getMatchManager().removeFromAllTeams(match,player);
                        match.getRedTeam().getPlayers().add(player);
                        playerData.setCurrentTeamColor(TeamColor.Red);

                        break;
                    case Blue:
                        instance.getMatchManager().removeFromAllTeams(match,player);
                        match.getBlueTeam().getPlayers().add(player);
                        playerData.setCurrentTeamColor(TeamColor.Blue);

                        break;
                    case Yellow:
                        instance.getMatchManager().removeFromAllTeams(match,player);
                        match.getYellowTeam().getPlayers().add(player);
                        playerData.setCurrentTeamColor(TeamColor.Yellow);

                        break;
                    case Green:
                        instance.getMatchManager().removeFromAllTeams(match,player);
                        match.getGreenTeam().getPlayers().add(player);
                        playerData.setCurrentTeamColor(TeamColor.Green);

                        break;
                }
                player.sendMessage(CC.GREEN+"You joined team "+this.color.name());
            }
            new TeamSelector(match).openMenu(player);
        }
    }

    private String formatPlayerSlot(int slot, TeamColor color, Match match){
        Team team = null;

        switch (color){
            case Red:
                team = match.getRedTeam();
                break;
            case Blue:
                team = match.getBlueTeam();
                break;
            case Yellow:
                team = match.getYellowTeam();
                break;
            case Green:
                team = match.getGreenTeam();
                break;

        }

        if(slot+1>team.getPlayers().size()){
            return CC.translate("&7- Available");

        }else {
            Player player = team.getPlayers().get(slot);
            return CC.translate("&e- "+player.getName());
        }
    }
}
