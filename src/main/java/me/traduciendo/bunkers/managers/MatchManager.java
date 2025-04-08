package me.traduciendo.bunkers.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.enums.VillagerType;
import me.traduciendo.bunkers.game.arena.Arena;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.match.MatchState;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.tasks.GameRunnable;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import me.traduciendo.bunkers.utils.Cuboid;
import me.traduciendo.bunkers.utils.item.ItemBuilder;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class MatchManager {
    public Map<String,Match> matchMap = Maps.newHashMap();
    private final Bunkers instance;

    public MatchManager(Bunkers instance) {
        this.instance = instance;
    }

    public void joinPlayer(Match match, Player player){
        PlayerData playerData = instance.getPlayerManager().getPlayerData(player);
        String match_full = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.JOIN.FULL");
        String match_started = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.JOIN.ALREADY_STARTED");
        String match_this = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.JOIN.ALREADY_IN_THE_MATCH");
        String match_on = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.JOIN.ALREADY_ON_MATCH");
        String match_lobby = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.JOIN.NOT_IN_LOBBY");
        if(playerData.getPlayerState() == PlayerState.LOBBY){
            if(playerData.getCurrentMatchName()==null){
                if(!match.getPlayerList().contains(player)){
                    if(match.getMatchState()==MatchState.STARTING || match.getMatchState() == MatchState.WAITING){
                        if(match.getPlayerList().size()<20){
                            playerData.setCurrentMatchName(match.getName());
                            playerData.setPlayerState(PlayerState.WAITING);
                            match.getPlayerList().add(player);
                            this.joinToEmpiestTeam(match,player);
                            match.getPlayerList().forEach(players -> {
                                String successJoined = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.JOIN.SUCCESSFULLY");
                                String message = successJoined
                                        .replace("%player%", player.getName())
                                        .replace("%size%", String.valueOf(match.getPlayerList().size()));
                                players.sendMessage(CC.translate(message));
                            });
                            player.getInventory().clear();
                            player.teleport(match.getArena().getLobby().toBukkitLocation());

                            ConfigurationSection leaveConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.MATCH.LEAVE");
                            int leaveSlot = leaveConfig.getInt("SLOT", 8);
                            if (leaveSlot < 0 || leaveSlot > 8) {
                                leaveSlot = 8;
                            }
                            player.getInventory().setItem(leaveSlot, leaveItem());

                            ConfigurationSection teamConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.MATCH.TEAM_SELECTOR");
                            int teamSlot = teamConfig.getInt("SLOT", 0);
                            if (teamSlot < 0 || teamSlot > 8) {
                                teamSlot = 0;
                            }
                            player.getInventory().setItem(teamSlot, chooseTeamItem());
                        }else {
                            player.sendMessage(CC.translate(match_full));
                        }
                    }else {
                        player.sendMessage(CC.translate(match_started));
                    }
                }else {
                    player.sendMessage(CC.translate(match_this));
                }
            }else {
                player.sendMessage(CC.translate(match_on));
            }
        }else {
            player.sendMessage(CC.translate(match_lobby));
        }
    }

    public void joinSpectator(Match match, Player player){
        PlayerData playerData = getInstance().getPlayerManager().getPlayerData(player);
        String raw_spectator_normal = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.SPECTATE.JOIN_FOR_PLAYERS");
        String raw_spectator_spectators = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.SPECTATE.JOIN_FOR_SPECTATORS");

        String spectatorNormal = raw_spectator_normal.replace("%player%", player.getName());
        String spectatorSpectators = raw_spectator_spectators.replace("%player%", player.getName());

        if(playerData.getPlayerState()==PlayerState.LOBBY){
            if(match.getMatchState()!=MatchState.WAITING && match.getMatchState()!=MatchState.STARTING){
                match.getPlayerList().forEach(matchPlayers -> matchPlayers.hidePlayer(player));
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().setItem(4,teleportItem());
                match.getSpectatorList().add(player);
                playerData.setCurrentMatchName(match.getName());
                playerData.setPlayerState(PlayerState.SPECTATING);
                final String finalSpectatorNormal = spectatorNormal;
                final String finalSpectatorSpectators = spectatorSpectators;
                match.getPlayerList().forEach(matchPlayers -> matchPlayers.sendMessage(CC.translate(finalSpectatorNormal)));
                match.getSpectatorList().forEach(spectators -> spectators.sendMessage(CC.translate(finalSpectatorSpectators)));
                player.teleport(match.getArena().getRedSpawn().toBukkitLocation());
            }
        }
    }

    public ItemStack teleportItem() {
        ConfigurationSection teleporterConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.SPECTATOR.TELEPORTER");

        String materialName = teleporterConfig.getString("ITEM", "COMPASS");
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null) {
            material = Material.COMPASS;
        }

        int data = teleporterConfig.getInt("DATA", 0);

        String displayName = CC.translate(teleporterConfig.getString("DISPLAY_NAME", "&cTeleport"));

        List<String> loreConfig = teleporterConfig.getStringList("LORE");
        List<String> lore = new ArrayList<>();
        for (String line : loreConfig) {
            lore.add(CC.translate(line));
        }

        ItemStack item = new ItemBuilder(material)
                .name(displayName)
                .lore(lore)
                .build();

        item.setDurability((short) data);

        return item;
    }

    private void joinToEmpiestTeam(Match match, Player player){
        PlayerData playerData = getInstance().getPlayerManager().getPlayerData(player);
        Team team = getTeamWithFewestPlayers(match.getRedTeam(),match.getBlueTeam(),match.getYellowTeam(),match.getGreenTeam());

        switch (team.getTeamColor()){
            case Red:
                match.getRedTeam().getPlayers().add(player);
                playerData.setCurrentTeamColor(TeamColor.Red);
                break;
            case Blue:
                match.getBlueTeam().getPlayers().add(player);
                playerData.setCurrentTeamColor(TeamColor.Blue);
                break;
            case Yellow:
                match.getYellowTeam().getPlayers().add(player);
                playerData.setCurrentTeamColor(TeamColor.Yellow);
                break;
            case Green:
                match.getGreenTeam().getPlayers().add(player);
                playerData.setCurrentTeamColor(TeamColor.Green);
                break;
        }
    }

    public void removeFromAllTeams(Match match, Player player){
        match.getRedTeam().getPlayers().remove(player);
        match.getBlueTeam().getPlayers().remove(player);
        match.getYellowTeam().getPlayers().remove(player);
        match.getGreenTeam().getPlayers().remove(player);
    }

    public static Team getTeamWithFewestPlayers(Team... teams) {
        Team teamWithFewestPlayers = null;
        int minPlayerCount = Integer.MAX_VALUE;

        for (Team team : teams) {
            int playerCount = team.getPlayers().size();
            if (playerCount < minPlayerCount) {
                minPlayerCount = playerCount;
                teamWithFewestPlayers = team;
            }
        }

        return teamWithFewestPlayers;
    }

    public void removePlayer(Match match, Player player){
        PlayerData playerData = getInstance().getPlayerManager().getPlayerData(player);
        String leave_not_in_this_match = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE_NOT_IN_THIS_MATCH");
        String leave_not_in_match = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE.NOT_IN_MATCH");
        String leave_in_lobby = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE.IN_LOBBY");
        if(playerData.getPlayerState() != PlayerState.LOBBY){
            if(playerData.getCurrentMatchName()!=null){
                if(match.getPlayerList().contains(player)){
                    playerData.setCurrentMatchName(null);
                    playerData.setCurrentTeamColor(null);
                    playerData.setPlayerState(PlayerState.LOBBY);
                    this.removeFromAllTeams(match,player);
                    match.getPlayerList().forEach(players -> {
                        String successLeaved = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE.SUCCESSFULLY");
                        String message = successLeaved
                                .replace("%player%", player.getName())
                                .replace("%size%", String.valueOf(match.getPlayerList().size()-1));
                        players.sendMessage(CC.translate(message));
                    });
                    match.getPlayerList().remove(player);
                    getInstance().getLobbyManager().sendToLobby(player);

                }else {
                    player.sendMessage(CC.translate(leave_not_in_this_match));
                }
            }else {
                player.sendMessage(CC.translate(leave_not_in_match));
            }
        }else {
            player.sendMessage(CC.translate(leave_in_lobby));
        }
    }

    public void removePlayer(Match match, Player player, boolean dead){
        PlayerData playerData = getInstance().getPlayerManager().getPlayerData(player);
        String leave_not_in_this_match = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE_NOT_IN_THIS_MATCH");
        String leave_not_in_match = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE.NOT_IN_MATCH");
        String leave_in_lobby = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE.IN_LOBBY");
        if(playerData.getPlayerState() != PlayerState.LOBBY){
            if(playerData.getCurrentMatchName()!=null){
                if(match.getPlayerList().contains(player)){
                    playerData.setCurrentMatchName(null);
                    playerData.setCurrentTeamColor(null);
                    playerData.setPlayerState(PlayerState.LOBBY);
                    this.removeFromAllTeams(match,player);
                    if(!dead) {
                        match.getPlayerList().forEach(players -> {
                            String successLeaved = Bunkers.getInstance().getLangConfig().getConfig().getString("MATCH.LEAVE.SUCCESSFULLY");
                            String message = successLeaved
                                    .replace("%player%", player.getName())
                                    .replace("%size%", String.valueOf(match.getPlayerList().size() - 1));
                            players.sendMessage(CC.translate(message));
                        });
                    }
                    match.getPlayerList().remove(player);
                    getInstance().getLobbyManager().sendToLobby(player);

                    if(dead){
                        getInstance().getMatchManager().joinSpectator(match,player);
                    }

                }else {
                    player.sendMessage(CC.translate(leave_not_in_this_match));
                }
            }else {
                player.sendMessage(CC.translate(leave_not_in_match));
            }
        }else {
            player.sendMessage(CC.translate(leave_in_lobby));
        }

    }

    public ItemStack chooseTeamItem(){
        ConfigurationSection chooseteamConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.MATCH.TEAM_SELECTOR");

        String materialName = chooseteamConfig.getString("ITEM", "WOOL");
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null) {
            material = Material.WOOL;
        }

        int data = chooseteamConfig.getInt("DATA", 0);

        String displayName = CC.translate(chooseteamConfig.getString("DISPLAY_NAME", "&aTeam Selector"));

        List<String> loreConfig = chooseteamConfig.getStringList("LORE");
        List<String> lore = new ArrayList<>();
        for (String line : loreConfig) {
            lore.add(CC.translate(line));
        }

        ItemStack item = new ItemBuilder(material)
                .name(displayName)
                .lore(lore)
                .build();

        item.setDurability((short) data);

        return item;
    }

    public ItemStack leaveItem(){
        ConfigurationSection leaveConfig = Bunkers.getInstance().getSettingsConfig().getConfig().getConfigurationSection("ITEMS.MATCH.LEAVE");

        String materialName = leaveConfig.getString("ITEM", "INK_SACK");
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null) {
            material = Material.INK_SACK;
        }

        int data = leaveConfig.getInt("DATA", 1);

        String displayName = CC.translate(leaveConfig.getString("DISPLAY_NAME", "&cLeave"));

        List<String> loreConfig = leaveConfig.getStringList("LORE");
        List<String> lore = new ArrayList<>();
        for (String line : loreConfig) {
            lore.add(CC.translate(line));
        }

        ItemStack item = new ItemBuilder(material)
                .name(displayName)
                .lore(lore)
                .build();

        item.setDurability((short) data);

        return item;
    }

    public Match getMatch(String name){
        return this.matchMap.get(name);
    }
    public Match getMatch(Player player){
        PlayerData playerData = getInstance().getPlayerManager().getPlayerData(player);
        return this.matchMap.get(playerData.getCurrentMatchName());
    }

    public void createMatch(Arena arena){
        Match match = new Match(arena.getName(),arena);

        Team redTeam = new Team(TeamColor.Red,new Cuboid(arena.getRedClaimMax().toBukkitLocation(),arena.getRedClaimMin().toBukkitLocation()), arena.getRedSpawn());
        Team blueTeam = new Team(TeamColor.Blue, new Cuboid(arena.getBlueClaimMax().toBukkitLocation(),arena.getBlueClaimMin().toBukkitLocation()), arena.getBlueSpawn());
        Team yellowTeam = new Team(TeamColor.Yellow,new Cuboid(arena.getYellowClaimMax().toBukkitLocation(),arena.getYellowClaimMin().toBukkitLocation()), arena.getYellowSpawn());
        Team greenTeam = new Team(TeamColor.Green, new Cuboid(arena.getGreenClaimMax().toBukkitLocation(),arena.getGreenClaimMin().toBukkitLocation()), arena.getGreenSpawn());

        match.setRedTeam(redTeam);
        match.setBlueTeam(blueTeam);
        match.setYellowTeam(yellowTeam);
        match.setGreenTeam(greenTeam);

        match.getTeamsLeft().addAll(Lists.newArrayList(match.getRedTeam(),match.getBlueTeam(),match.getYellowTeam(),match.getBlueTeam()));


        this.matchMap.put(arena.getName(),match);
        BukkitRunnable runnable = new GameRunnable(match,getInstance());
        runnable.runTaskTimer(getInstance(),20L,20L);
    }

    public void deleteVillager(UUID uuid){
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()){
            if(entity.getUniqueId().equals(uuid)){
                entity.remove();
            }
        }
    }

    public void setWinner(Match match, Team team){
        match.setWinner(team);
        match.setMatchState(MatchState.ENDING);
    }

    public void restartMatch(Match match){
        if(match.getMatchState() == MatchState.RESTARTING){
            for(Player player : match.getPlayerList()){
                instance.getLobbyManager().sendToLobby(player);
            }
            for(Player player : match.getSpectatorList()){
                instance.getLobbyManager().sendToLobby(player);
            }
            match.setMatchState(MatchState.WAITING);
        }
    }

    public void createVillager(Match match, TeamColor color ,VillagerType villagerType){
       switch (color){
           case Red:
               switch (villagerType){
                   case COMBAT:
                       Villager redCombat = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getRedVillagerCombatSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       redCombat.setFallDistance(0);
                       redCombat.setRemoveWhenFarAway(false);
                       redCombat.setAdult();
                       redCombat.setProfession(Villager.Profession.BLACKSMITH);
                       redCombat.setCanPickupItems(false);
                       redCombat.setMaxHealth(40);
                       redCombat.setHealth(40);
                       redCombat.setCustomName("Combat Shop");
                       redCombat.setCustomNameVisible(true);
                       redCombat.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getRedTeam().setCombatVillager(redCombat.getUniqueId());
                       break;
                   case BUILD:
                       Villager redBuild = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getRedVillagerBuildSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       redBuild.setFallDistance(0);
                       redBuild.setRemoveWhenFarAway(false);
                       redBuild.setAdult();
                       redBuild.setProfession(Villager.Profession.BLACKSMITH);
                       redBuild.setCanPickupItems(false);
                       redBuild.setMaxHealth(40);
                       redBuild.setHealth(40);
                       redBuild.setCustomName("Build Shop");
                       redBuild.setCustomNameVisible(true);
                       redBuild.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getRedTeam().setBuildVillager(redBuild.getUniqueId());
                       break;
                   case SELL:
                       Villager redSell = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getRedVillagerSellSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       redSell.setFallDistance(0);
                       redSell.setRemoveWhenFarAway(false);
                       redSell.setAdult();
                       redSell.setProfession(Villager.Profession.BLACKSMITH);
                       redSell.setCanPickupItems(false);
                       redSell.setMaxHealth(40);
                       redSell.setHealth(40);
                       redSell.setCustomName("Sell Items");
                       redSell.setCustomNameVisible(true);
                       redSell.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getRedTeam().setSellVillager(redSell.getUniqueId());
                       break;
               }
               break;
           case Blue:
               switch (villagerType){
                   case COMBAT:
                       Villager blueCombat = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getBlueVillagerCombatSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       blueCombat.setFallDistance(0);
                       blueCombat.setRemoveWhenFarAway(false);
                       blueCombat.setAdult();
                       blueCombat.setProfession(Villager.Profession.BLACKSMITH);
                       blueCombat.setCanPickupItems(false);
                       blueCombat.setMaxHealth(40);
                       blueCombat.setHealth(40);
                       blueCombat.setCustomName("Combat Shop");
                       blueCombat.setCustomNameVisible(true);
                       blueCombat.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getBlueTeam().setCombatVillager(blueCombat.getUniqueId());
                       break;
                   case BUILD:
                       Villager blueBuild = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getBlueVillagerBuildSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       blueBuild.setFallDistance(0);
                       blueBuild.setRemoveWhenFarAway(false);
                       blueBuild.setAdult();
                       blueBuild.setProfession(Villager.Profession.BLACKSMITH);
                       blueBuild.setCanPickupItems(false);
                       blueBuild.setMaxHealth(40);
                       blueBuild.setHealth(40);
                       blueBuild.setCustomName("Build Shop");
                       blueBuild.setCustomNameVisible(true);
                       blueBuild.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getBlueTeam().setBuildVillager(blueBuild.getUniqueId());
                       break;
                   case SELL:
                       Villager blueSell = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getBlueVillagerSellSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       blueSell.setFallDistance(0);
                       blueSell.setRemoveWhenFarAway(false);
                       blueSell.setAdult();
                       blueSell.setProfession(Villager.Profession.BLACKSMITH);
                       blueSell.setCanPickupItems(false);
                       blueSell.setMaxHealth(40);
                       blueSell.setHealth(40);
                       blueSell.setCustomName("Sell Items");
                       blueSell.setCustomNameVisible(true);
                       blueSell.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getBlueTeam().setSellVillager(blueSell.getUniqueId());
                       break;
               }
               break;
           case Yellow:
               switch (villagerType){
                   case COMBAT:
                       Villager yellowCombat = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getYellowVillagerCombatSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       yellowCombat.setFallDistance(0);
                       yellowCombat.setRemoveWhenFarAway(false);
                       yellowCombat.setAdult();
                       yellowCombat.setProfession(Villager.Profession.BLACKSMITH);
                       yellowCombat.setCanPickupItems(false);
                       yellowCombat.setMaxHealth(40);
                       yellowCombat.setHealth(40);
                       yellowCombat.setCustomName("Combat Shop");
                       yellowCombat.setCustomNameVisible(true);
                       yellowCombat.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getYellowTeam().setCombatVillager(yellowCombat.getUniqueId());
                       break;
                   case BUILD:
                       Villager yellowBuild = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getYellowVillagerBuildSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       yellowBuild.setFallDistance(0);
                       yellowBuild.setRemoveWhenFarAway(false);
                       yellowBuild.setAdult();
                       yellowBuild.setProfession(Villager.Profession.BLACKSMITH);
                       yellowBuild.setCanPickupItems(false);
                       yellowBuild.setMaxHealth(40);
                       yellowBuild.setHealth(40);
                       yellowBuild.setCustomName("Build Shop");
                       yellowBuild.setCustomNameVisible(true);
                       yellowBuild.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getYellowTeam().setBuildVillager(yellowBuild.getUniqueId());
                       break;
                   case SELL:
                       Villager yellowSell = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getYellowVillagerSellSpawn().toBukkitLocation(),  EntityType.VILLAGER);
                       yellowSell.setFallDistance(0);
                       yellowSell.setRemoveWhenFarAway(false);
                       yellowSell.setAdult();
                       yellowSell.setProfession(Villager.Profession.BLACKSMITH);
                       yellowSell.setCanPickupItems(false);
                       yellowSell.setMaxHealth(40);
                       yellowSell.setHealth(40);
                       yellowSell.setCustomName("Sell Items");
                       yellowSell.setCustomNameVisible(true);
                       yellowSell.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getYellowTeam().setSellVillager(yellowSell.getUniqueId());
                       break;
               }
               break;
           case Green:
               switch (villagerType){
                   case COMBAT:
                       Villager greenCombat = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getGreenVillagerCombatSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       greenCombat.setFallDistance(0);
                       greenCombat.setRemoveWhenFarAway(false);
                       greenCombat.setAdult();
                       greenCombat.setProfession(Villager.Profession.BLACKSMITH);
                       greenCombat.setCanPickupItems(false);
                       greenCombat.setMaxHealth(40);
                       greenCombat.setHealth(40);
                       greenCombat.setCustomName("Combat Shop");
                       greenCombat.setCustomNameVisible(true);
                       greenCombat.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getGreenTeam().setCombatVillager(greenCombat.getUniqueId());
                       break;
                   case BUILD:
                       Villager greenBuild = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getGreenVillagerBuildSpawn().toBukkitLocation(), EntityType.VILLAGER);
                       greenBuild.setFallDistance(0);
                       greenBuild.setRemoveWhenFarAway(false);
                       greenBuild.setAdult();
                       greenBuild.setProfession(Villager.Profession.BLACKSMITH);
                       greenBuild.setCanPickupItems(false);
                       greenBuild.setMaxHealth(40);
                       greenBuild.setHealth(40);
                       greenBuild.setCustomName("Build Shop");
                       greenBuild.setCustomNameVisible(true);
                       greenBuild.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getGreenTeam().setBuildVillager(greenBuild.getUniqueId());
                       break;
                   case SELL:
                       Villager greenSell = (Villager) Bukkit.getWorld("world").spawnEntity(match.getArena().getGreenVillagerSellSpawn().toBukkitLocation(),  EntityType.VILLAGER);
                       greenSell.setFallDistance(0);
                       greenSell.setRemoveWhenFarAway(false);
                       greenSell.setAdult();
                       greenSell.setProfession(Villager.Profession.BLACKSMITH);
                       greenSell.setCanPickupItems(false);
                       greenSell.setMaxHealth(40);
                       greenSell.setHealth(40);
                       greenSell.setCustomName("Sell Items");
                       greenSell.setCustomNameVisible(true);
                       greenSell.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
                       match.getGreenTeam().setSellVillager(greenSell.getUniqueId());
                       break;
               }
               break;
       }
    }
}
