package me.traduciendo.bunkers.listeners;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.events.MatchStartEvent;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.match.events.MatchEnterClaimEvent;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.tasks.KothTask;
import me.traduciendo.bunkers.tasks.VillagerSpawnRunnable;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.Cuboid;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.github.paperspigot.Title;

import java.util.function.UnaryOperator;

public class MatchListener implements Listener {

    private Bunkers plugin = Bunkers.getInstance();

    @EventHandler
    public void onMatchStart(MatchStartEvent event){
        if(event.getMatch()==null){
            return;
        }

        Match match = event.getMatch();

        for (Player player : match.getPlayerList()){
            player.sendTitle(new Title(CC.translate("&a&lMatch started!"),CC.translate("&fGood luck!"),20,70,20));
            plugin.getPlayerManager().getPlayerData(player).setPlayerState(PlayerState.FIGHTING);
            player.getInventory().clear();
            player.setFlying(false);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 4, true, false));
            player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
            player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
            plugin.getWaypointManager().getKothWaypoint().send(player,new Cuboid(match.getArena().getKothClaimMax().toBukkitLocation(),match.getArena().getKothClaimMin().toBukkitLocation()).getCenter(),UnaryOperator.identity());
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=Villager]");

        match.getRedTeam().getPlayers().forEach(player -> {
            player.teleport(match.getArena().getRedSpawn().toBukkitLocation());
            plugin.getWaypointManager().getHqWaypoint().send(player,match.getArena().getRedSpawn().toBukkitLocation(), UnaryOperator.identity());
        });
        match.getBlueTeam().getPlayers().forEach(player -> {
            player.teleport(match.getArena().getBlueSpawn().toBukkitLocation());
            plugin.getWaypointManager().getHqWaypoint().send(player,match.getArena().getBlueVillagerBuildSpawn().toBukkitLocation(), UnaryOperator.identity());
        });
        match.getYellowTeam().getPlayers().forEach(player -> {
            player.teleport(match.getArena().getYellowSpawn().toBukkitLocation());
            plugin.getWaypointManager().getHqWaypoint().send(player,match.getArena().getYellowSpawn().toBukkitLocation(), UnaryOperator.identity());
        });
        match.getGreenTeam().getPlayers().forEach(player ->{
            player.teleport(match.getArena().getGreenSpawn().toBukkitLocation());
            plugin.getWaypointManager().getHqWaypoint().send(player,match.getArena().getGreenSpawn().toBukkitLocation(), UnaryOperator.identity());
        });

        match.getRedTeam().setDTR(match.getRedTeam().getPlayers().size());
        match.getBlueTeam().setDTR(match.getBlueTeam().getPlayers().size());
        match.getYellowTeam().setDTR(match.getYellowTeam().getPlayers().size());
        match.getGreenTeam().setDTR(match.getGreenTeam().getPlayers().size());

        new VillagerSpawnRunnable(match).runTaskLater(plugin,120L);
        new KothTask(match,plugin).runTaskTimer(plugin,20L,20L);
    }

    @EventHandler
    public void enterClaimListener(MatchEnterClaimEvent event){
        Team from = event.getFromTeam();
        Team to = event.getToTeam();

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNow leaving: " + (from == null ? "&4Warzone" : TeamColor.getChatColor(from.getTeamColor()) + from.getTeamColor().name())));
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNow entering: " + (to == null ? "&4Warzone" : TeamColor.getChatColor(to.getTeamColor()) + to.getTeamColor().name())));

    }
}
