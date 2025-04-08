package me.traduciendo.bunkers.commands;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BunkersCommand implements CommandExecutor {

    private Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 0) {
            this.sendMainHelp(player);
            return false;
        }
        return false;
    }

    public void sendMainHelp(Player player){
        player.sendMessage(CC.translate("&6============================================="));
        player.sendMessage(CC.translate("                                               "));
        player.sendMessage(CC.translate("&e                HCFBunkers &6┃ &e1.0         "));
        player.sendMessage(CC.translate("&e                                             "));
        player.sendMessage(CC.translate("&c                  &mA Bunkers Core&c         "));
        player.sendMessage(CC.translate("&c                 The Bunkers Core            "));
        player.sendMessage(CC.translate("                                               "));
        player.sendMessage(CC.translate("&6============================================="));
    }
}

