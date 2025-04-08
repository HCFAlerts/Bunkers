package me.traduciendo.bunkers.listeners;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.enums.VillagerType;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

public class EntityListener implements Listener {
    private final Bunkers plugin = Bunkers.getInstance();

    String combat_title = Bunkers.getInstance().getLangConfig().getConfig().getString("TITLE.VILLAGER_KILLED.TITLE.COMBAT_SHOP");
    String combat_sub_title = Bunkers.getInstance().getLangConfig().getConfig().getString("TITLE.VILLAGER_KILLED.SUB_TITLE.COMBAT_SHOP");
    String build_title = Bunkers.getInstance().getLangConfig().getConfig().getString("TITLE.VILLAGER_KILLED.TITLE.BUILD_SHOP");
    String build_sub_title = Bunkers.getInstance().getLangConfig().getConfig().getString("TITLE.VILLAGER_KILLED.SUB_TITLE.BUILD_SHOP");
    String sell_title = Bunkers.getInstance().getLangConfig().getConfig().getString("TITLE.VILLAGER_KILLED.TITLE.SELL_SHOP");
    String sell_sub_title = Bunkers.getInstance().getLangConfig().getConfig().getString("TITLE.VILLAGER_KILLED.SUB_TITLE.SELL_SHOP");

    @EventHandler
    public void on(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player==null){
            return;
        }
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);

        switch (playerData.getPlayerState()){
            case LOBBY:
                break;
            case WAITING:
                break;
            case FIGHTING:
                Match match = plugin.getMatchManager().getMatch(player);
                Entity entity = event.getEntity();
                Team team = plugin.getTeamManager().getByLocation(event.getEntity().getLocation(), match);
                if(team==null){
                    break;
                }
                if(entity.getType() == EntityType.VILLAGER){
                    switch (ChatColor.stripColor(entity.getCustomName())){
                        case "Combat Shop":
                            team.getPlayers().forEach(members -> members.sendTitle(new Title(CC.translate(combat_title),CC.translate(combat_sub_title))));
                           new BukkitRunnable(){
                               @Override
                               public void run(){
                                   plugin.getMatchManager().createVillager(match,team.getTeamColor(),VillagerType.COMBAT);
                               }
                           }.runTaskLater(plugin,3600L);
                            break;
                        case "Build Shop":
                            team.getPlayers().forEach(members -> members.sendTitle(new Title(CC.translate(build_title),CC.translate(build_sub_title))));
                            new BukkitRunnable(){
                                @Override
                                public void run(){
                                    plugin.getMatchManager().createVillager(match,team.getTeamColor(),VillagerType.BUILD);
                                }
                            }.runTaskLater(plugin,3600L);
                            break;
                        case "Sell Items":
                            team.getPlayers().forEach(members -> members.sendTitle(new Title(CC.translate(sell_title),CC.translate(sell_sub_title))));
                            new BukkitRunnable(){
                                @Override
                                public void run(){
                                    plugin.getMatchManager().createVillager(match,team.getTeamColor(),VillagerType.SELL);
                                }
                            }.runTaskLater(plugin,3600L);
                            break;
                    }
                }
                break;
        }

    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}
