package me.traduciendo.bunkers.commands;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.arena.Arena;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.match.MatchState;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MatchCommand implements CommandExecutor {

    private Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        if (!commandSender.hasPermission("bunkers.command.match")) {
            return false;
        }

        Player player = (Player) commandSender;

        if(args.length==0){
            this.sendMainHelp(player);
            return false;
        }
        
        if(args.length==1){
            if(args[0].equalsIgnoreCase("list")){
                this.sendMatchlist(player);
            }
            return false;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("forcestart")) {
                Match match = plugin.getMatchManager().getMatch(args[1]);

                if (match == null) {
                    player.sendMessage(CC.RED + "This match does not exist.");
                    return false;
                }

                if (match.getMatchState().equals(MatchState.ACTIVE)) {
                    player.sendMessage(CC.RED + "This match has already started.");
                    return false;
                }

                match.setStartTime(3);
                player.sendMessage(CC.GREEN + "You have force-started the match " + args[1] + ".");
                return true;
            }
        }

        if(args.length==2){
            if(args[0].equalsIgnoreCase("create")){
                Arena arena = plugin.getArenaManager().getArena(args[1]);
                if(arena==null){
                    player.sendMessage(CC.RED+"This arena does not exists");
                    return false;
                }

                if(plugin.getMatchManager().getMatch(arena.getName())!=null){
                    player.sendMessage(CC.RED+"This match already exists");
                    return false;
                }

                player.sendMessage(CC.GREEN+"You made the match for the arena "+args[1]);
                plugin.getMatchManager().createMatch(arena);

            }else if(args[0].equalsIgnoreCase("join")) {
                Match match = plugin.getMatchManager().getMatch(args[1]);
                if (match == null) {
                    player.sendMessage(CC.RED + "This match does not exists");
                    return false;
                }
                plugin.getMatchManager().joinPlayer(match, player);
            }
        }
        return false;
    }

    public void sendMainHelp(Player player){
            player.sendMessage(CC.translate("&7&m------------------------"));
            player.sendMessage(CC.translate("&6&lMatch Help &7(1/1)"));
            player.sendMessage(CC.translate(" "));
            player.sendMessage(CC.translate("&f/match create <arena>"));
            player.sendMessage(CC.translate("&f/match join <arena>"));
            player.sendMessage(CC.translate("&f/match forcestart <arena>"));
            player.sendMessage(CC.translate("&f/match list"));
            player.sendMessage(CC.translate("&7&m------------------------"));
    }

    public void sendMatchlist(Player player){
        player.sendMessage(CC.translate("&7&m------------------------"));
        player.sendMessage(CC.translate("&6&lMatch List &7(1/1)"));
        player.sendMessage(CC.translate(" "));
        for (Map.Entry<String, Match> entry : plugin.getMatchManager().matchMap.entrySet()){
            Match match = entry.getValue();
            player.sendMessage(CC.translate("&e"+match.getName()+" &7- &aEnabled"));
        }
        player.sendMessage(CC.translate("&7&m------------------------"));
    }

}
