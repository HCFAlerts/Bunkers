package me.traduciendo.bunkers.managers;

import com.google.common.collect.Lists;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.Team;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.player.PlayerData;
import me.traduciendo.bunkers.player.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final Bunkers instance;
    public TeamManager(Bunkers instance){
       this.instance = instance;
    }

    public Team getByLocation(Location location, Match match){
        List<Team> teams = Lists.newArrayList();
        teams.add(match.getRedTeam());
        teams.add(match.getBlueTeam());
        teams.add(match.getYellowTeam());
        teams.add(match.getGreenTeam());


        for(Team team : teams){
            if(team.getCuboid().contains(location.getBlock()))return team;
        }
        return null;
    }

    public Team getByPlayer(Player player, Match match){
        List<Team> teams = Lists.newArrayList();
        teams.add(match.getRedTeam());
        teams.add(match.getBlueTeam());
        teams.add(match.getYellowTeam());
        teams.add(match.getGreenTeam());


        for(Team team : teams){
            if(team.getPlayers().contains(player))return team;
        }
        return null;
    }

    public Location getSpawn(Match match,Team team){
        if(team.getTeamColor()== TeamColor.Red){
            return match.getArena().getRedSpawn().toBukkitLocation();
        }else if(team.getTeamColor()== TeamColor.Blue){
            return match.getArena().getBlueSpawn().toBukkitLocation();
        }else if(team.getTeamColor()== TeamColor.Yellow){
            return match.getArena().getYellowSpawn().toBukkitLocation();
        }else if(team.getTeamColor()== TeamColor.Green){
            return match.getArena().getGreenSpawn().toBukkitLocation();
        }
        return null;
    }

    public Team getTeamByColor(Match match,String color){
        color = color.toUpperCase();
        TeamColor teamColor = TeamColor.valueOf(color);
        switch (teamColor){
            case Red:
                return match.getRedTeam();
            case Blue:
                return match.getBlueTeam();
            case Yellow:
                return match.getYellowTeam();
            case Green:
                return match.getGreenTeam();

        }

        return null;
    }
    public List<String> getMembersNames(Team team) {
        List<String> toReturn = new ArrayList<>();

        for (Player player : team.getPlayers()) {
            PlayerData playerData = instance.getPlayerManager().getPlayerData(player);

            if (player == null) {
                toReturn.add(ChatColor.GRAY + player.getName());
            } else if (playerData.getPlayerState() == PlayerState.FIGHTING) {
                toReturn.add(TeamColor.getChatColor(team.getTeamColor()) + player.getName());
            } else if (playerData.getPlayerState() == PlayerState.SPECTATING) {
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + player.getName());
            } else if (playerData.getPlayerState() == PlayerState.LOBBY) {
                toReturn.add(TeamColor.getChatColor(team.getTeamColor()) + player.getName());
            }
        }

        return toReturn;
    }

}
