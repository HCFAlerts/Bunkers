package me.traduciendo.bunkers.commands;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.chat.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TeamCommand implements CommandExecutor {

    private final Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
        if(playerData.getPlayerState() == PlayerState.FIGHTING){
            Match match = plugin.getMatchManager().getMatch(player);

            if(args.length == 1){
                if(args[0].equalsIgnoreCase("hq") || args[0].equalsIgnoreCase("home")){
                    Team team = plugin.getTeamManager().getByPlayer(player,match);

                    player.sendMessage(CC.translate("&eTeleporting to your team's HQ in &615 seconds&e..."));
                    if (plugin.getTeamManager().getByLocation(player.getLocation(),match) != null) {
                        plugin.getTimerManager().getTeleportTimer().setCooldown(player, player.getUniqueId(), TimeUnit.SECONDS.toMillis(15), true);
                    } else {
                        plugin.getTimerManager().getTeleportTimer().setCooldown(player, player.getUniqueId());
                    }

                }
            }

            if(args.length==2){
                if(args[0].equalsIgnoreCase("who")||args[0].equalsIgnoreCase("i")||args[0].equalsIgnoreCase("show")){

                    Team team = null;
                    if(args[1].equalsIgnoreCase("RED")||args[1].equalsIgnoreCase("BLUE")||args[1].equalsIgnoreCase("YELLOW") || args[1].equalsIgnoreCase("GREEN")){
                       team = plugin.getTeamManager().getTeamByColor(match,args[1]);
                    }

                    if(team == null){
                        Player target = Bukkit.getPlayer(args[0]);
                        if(target == null){
                            player.sendMessage(CC.RED+"This player does not exists");
                        }else {
                            PlayerData targetData = plugin.getPlayerManager().getPlayerData(player);
                            if(targetData.getCurrentMatchName() == playerData.getCurrentMatchName()){
                                Team targetTeam = plugin.getTeamManager().getByPlayer(player,match);
                                player.sendMessage(CC.translate("&7&m----------------------"));
                                player.sendMessage(CC.translate("&eTeam&7: " + TeamColor.getChatColor(targetTeam.getTeamColor()) + targetTeam.getTeamColor().name()));
                                player.sendMessage(CC.translate(" &eDTR&7: &f" + targetTeam.getDTR()));
                                player.sendMessage(CC.translate(" &eHome&7: &f" + (int) targetTeam.getSpawn().getX() + ", " + (int) targetTeam.getSpawn().getZ()));
                                player.sendMessage("");
                                player.sendMessage(CC.translate("&eMembers&7: &f" + StringUtils.join(plugin.getTeamManager().getMembersNames(targetTeam), ", ")));
                                player.sendMessage(CC.translate("&7&m----------------------"));
                            }else {
                                player.sendMessage(CC.RED+"This player is not in your match");
                                return false;
                            }
                            return false;
                        }
                        player.sendMessage(CC.RED+"This team does not exists");

                        return false;
                    }else {
                        player.sendMessage(CC.translate("&7&m----------------------"));
                        player.sendMessage(CC.translate("&eTeam&7: " + TeamColor.getChatColor(team.getTeamColor()) + team.getTeamColor().name()));
                        player.sendMessage(CC.translate(" &eDTR&7: &f" + team.getDTR()));
                        player.sendMessage(CC.translate(" &eHome&7: &f" + (int) team.getSpawn().getX() + ", " + (int) team.getSpawn().getZ()));
                        player.sendMessage("");
                        player.sendMessage(CC.translate("&eMembers&7: &f" + StringUtils.join(plugin.getTeamManager().getMembersNames(team), ", ")));
                        player.sendMessage(CC.translate("&7&m----------------------"));
                    }

                }
            }

        }else {
            player.sendMessage(CC.RED+"You are not in a match!");
        }

        return false;
    }
}
