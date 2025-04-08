package me.traduciendo.bunkers.tasks;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.events.MatchStartEvent;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.match.MatchState;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

public class GameRunnable extends BukkitRunnable {
    private final Match match;
    private final Bunkers plugin;
    private int endingTime = 10;
    public GameRunnable(Match match, Bunkers plugin){
        this.match = match;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        switch (match.getMatchState()){
            case WAITING:
                if (match.getPlayerList().size()>=1) {
                    match.setMatchState(MatchState.STARTING);
                } else {
                    match.setStartTime(60);
                }
                break;

            case STARTING:
                if (match.getPlayerList().size()>=1) {
                    if(match.getStartTime()!=0) {
                        match.setStartTime(match.getStartTime() - 1);
                        if (match.getStartTime() == 30 || match.getStartTime() == 20 || match.getStartTime() == 10 || match.getStartTime() == 5 || match.getStartTime() == 3 || match.getStartTime() == 2 || match.getStartTime() == 1) {
                            match.getPlayerList().forEach(player -> player.sendMessage(CC.translate("&eThe game will begin in &6" + match.getStartTime() + "s&e.")));
                        }
                        if (match.getStartTime() == 0) {
                            match.getPlayerList().forEach(player -> player.sendMessage(CC.translate("&eThe game will begin in &6NOW&e.")));
                        }
                    } else {
                        match.setMatchState(MatchState.ACTIVE);

                        plugin.getServer().getPluginManager().callEvent(new MatchStartEvent(match));
                    }
                } else {
                    match.setMatchState(MatchState.WAITING);
                }
                break;
            case ACTIVE:
                match.setGameTime(match.getGameTime()+1);
                this.lowerEnderPearlCooldown(match);
                break;
            case ENDING:
               if (endingTime>0) {
                   match.getWinner().getPlayers().forEach(player -> {
                       Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                       FireworkMeta fireworkMeta = firework.getFireworkMeta();
                       fireworkMeta.setPower(2);
                       fireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).withColor(Color.SILVER).build());
                       player.sendTitle(new Title(CC.translate("&6&lCongratulations!"),CC.translate("&aYou win!"),20,70,20));
                   });
                   endingTime--;
               } else {
                   match.setMatchState(MatchState.RESTARTING);
               }
            case RESTARTING:
        }
    }

    private void lowerEnderPearlCooldown(Match match){
        match.getPlayerList().forEach(player -> {
            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
            if(playerData.getPearlCooldown()>0){
                playerData.setPearlCooldown(playerData.getPearlCooldown()-1);
            }
        });
    }
}
