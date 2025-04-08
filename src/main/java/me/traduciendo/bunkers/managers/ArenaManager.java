package me.traduciendo.bunkers.managers;

import com.google.common.collect.Maps;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.arena.Arena;
import me.traduciendo.bunkers.utils.CustomLocation;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.Map;


public class ArenaManager  {

    private final Bunkers instance;

    public ArenaManager(Bunkers instance){

        this.instance = instance;
        this.loadArenas();
    }

    public Map<String, Arena> arenaMap = Maps.newHashMap();

    public void createArena(String name){
        this.arenaMap.put(name,new Arena(name));
    }
    public Arena getArena(String name){
        return this.arenaMap.get(name);
    }

    public void register(Arena arena){

    }

    public void save(Arena arena){
        FileConfiguration config = instance.getArenaConfig().getConfig();
            config.set("ARENAS."+arena.getName()+".NAME",arena.getName());

            config.set("ARENAS."+arena.getName()+".TEAMS.RED.SPAWN",arena.getRedSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.RED.MAXCLAIM",arena.getRedClaimMax().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.RED.MINCLAIM",arena.getRedClaimMin().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.RED.VILLAGERS.SELL",arena.getRedVillagerSellSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.RED.VILLAGERS.BUILD",arena.getRedVillagerBuildSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.RED.VILLAGERS.COMBAT",arena.getRedVillagerCombatSpawn().toAString());

            config.set("ARENAS."+arena.getName()+".TEAMS.BLUE.SPAWN",arena.getBlueSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.BLUE.MAXCLAIM",arena.getBlueClaimMax().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.BLUE.MINCLAIM",arena.getBlueClaimMin().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.BLUE.VILLAGERS.SELL",arena.getBlueVillagerSellSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.BLUE.VILLAGERS.BUILD",arena.getBlueVillagerBuildSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.BLUE.VILLAGERS.COMBAT",arena.getBlueVillagerCombatSpawn().toAString());

            config.set("ARENAS."+arena.getName()+".TEAMS.YELLOW.SPAWN",arena.getYellowSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.YELLOW.MAXCLAIM",arena.getYellowClaimMax().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.YELLOW.MINCLAIM",arena.getYellowClaimMin().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.YELLOW.VILLAGERS.SELL",arena.getYellowVillagerSellSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.YELLOW.VILLAGERS.BUILD",arena.getYellowVillagerBuildSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.YELLOW.VILLAGERS.COMBAT",arena.getYellowVillagerCombatSpawn().toAString());

            config.set("ARENAS."+arena.getName()+".TEAMS.GREEN.SPAWN",arena.getGreenSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.GREEN.MAXCLAIM",arena.getGreenClaimMax().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.GREEN.MINCLAIM",arena.getGreenClaimMin().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.GREEN.VILLAGERS.SELL",arena.getGreenVillagerSellSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.GREEN.VILLAGERS.BUILD",arena.getGreenVillagerBuildSpawn().toAString());
            config.set("ARENAS."+arena.getName()+".TEAMS.GREEN.VILLAGERS.COMBAT",arena.getGreenVillagerCombatSpawn().toAString());

            config.set("ARENAS."+arena.getName()+".KOTH.MAXCLAIM",arena.getKothClaimMax().toAString());
            config.set("ARENAS."+arena.getName()+".KOTH.MINCLAIM",arena.getKothClaimMin().toAString());

            config.set("ARENAS."+arena.getName()+".LOBBY",arena.getLobby().toAString());

            config.set("ARENAS."+arena.getName()+".ICON.MATERIAL",arena.getIconMaterial().name());
            config.set("ARENAS."+arena.getName()+".ICON.DATA",(int)arena.getIconData());


            instance.getArenaConfig().save();
        
    }

    public void saveAll(){
        for (Map.Entry<String,Arena> entry : this.arenaMap.entrySet()){
            instance.getArenaManager().save(entry.getValue());
        }
    }

    public void loadArenas(){
        FileConfiguration config = instance.getArenaConfig().getConfig();

        for(String path : config.getConfigurationSection("ARENAS").getKeys(false)){

            String name = config.getString("ARENAS."+path+".NAME");
            CustomLocation redSpawn = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.RED.SPAWN"));
            CustomLocation redMaxClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.RED.MAXCLAIM"));
            CustomLocation redMinClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.RED.MINCLAIM"));
            CustomLocation redVillagerSell = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.RED.VILLAGERS.SELL"));
            CustomLocation redVillagerBuild = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.RED.VILLAGERS.BUILD"));
            CustomLocation redVillagerCombat = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.RED.VILLAGERS.COMBAT"));

            CustomLocation blueSpawn = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.BLUE.SPAWN"));
            CustomLocation blueMaxClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.BLUE.MAXCLAIM"));
            CustomLocation blueMinClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.BLUE.MINCLAIM"));
            CustomLocation blueVillagerSell = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.BLUE.VILLAGERS.SELL"));
            CustomLocation blueVillagerBuild = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.BLUE.VILLAGERS.BUILD"));
            CustomLocation blueVillagerCombat = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.BLUE.VILLAGERS.COMBAT"));

            CustomLocation yellowSpawn = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.YELLOW.SPAWN"));
            CustomLocation yellowMaxClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.YELLOW.MAXCLAIM"));
            CustomLocation yellowMinClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.YELLOW.MINCLAIM"));
            CustomLocation yellowVillagerSell = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.YELLOW.VILLAGERS.SELL"));
            CustomLocation yellowVillagerBuild = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.YELLOW.VILLAGERS.BUILD"));
            CustomLocation yellowVillagerCombat = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.YELLOW.VILLAGERS.COMBAT"));

            CustomLocation greenSpawn = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.GREEN.SPAWN"));
            CustomLocation greenMaxClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.GREEN.MAXCLAIM"));
            CustomLocation greenMinClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.GREEN.MINCLAIM"));
            CustomLocation greenVillagerSell = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.GREEN.VILLAGERS.SELL"));
            CustomLocation greenVillagerBuild = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.GREEN.VILLAGERS.BUILD"));
            CustomLocation greenVillagerCombat = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".TEAMS.GREEN.VILLAGERS.COMBAT"));

            CustomLocation kothMaxClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".KOTH.MAXCLAIM"));
            CustomLocation kothMinClaim = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".KOTH.MINCLAIM"));

            CustomLocation lobby = CustomLocation.stringToLocation(config.getString("ARENAS."+path+".LOBBY"));

            Material material = Material.getMaterial(config.getString("ARENAS."+path+".ICON.MATERIAL"));
            int data = config.getInt("ARENAS."+path+".ICON.DATA");


            Arena arena = new Arena(name,
                    redSpawn,redMaxClaim,redMinClaim,redVillagerSell,redVillagerBuild,redVillagerCombat,
                    blueSpawn,blueMaxClaim,blueMinClaim,blueVillagerSell,blueVillagerBuild,blueVillagerCombat,
                    yellowSpawn, yellowMaxClaim,yellowMinClaim,yellowVillagerSell,yellowVillagerBuild,yellowVillagerCombat,
                    greenSpawn,greenMaxClaim,greenMinClaim,greenVillagerSell,greenVillagerBuild,greenVillagerCombat,
                    kothMaxClaim,kothMinClaim,lobby,material,data);

            this.arenaMap.put(arena.getName(),arena);
            
        }
    }

    public boolean isDone(Arena arena) throws IllegalAccessException{
        int count = 0;
        for(Field field : arena.getClass().getDeclaredFields()){
            if(field.get(arena)==null){
                count++;
            }

        }
        if(count>0){
            return false;
        }else {
            return true;
        }
    }


}
