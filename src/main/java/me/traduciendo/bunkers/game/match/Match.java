package me.traduciendo.bunkers.game.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.traduciendo.bunkers.game.arena.Arena;
import me.traduciendo.bunkers.game.team.Team;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Match {
    private final String name;
    private final Arena arena;
    private Team redTeam;
    private Team blueTeam;
    private Team yellowTeam;
    private Team greenTeam;
    private MatchState matchState = MatchState.WAITING;
    private int gameTime;
    private int startTime = 60;
    private int kothTime = 600;
    private Team winner;
    private List<Block> placedBlocks = new ArrayList<>();
    private List<Player> playerList = new ArrayList<>();
    private List<Player> spectatorList = new ArrayList<>();
    private List<Team> teamsLeft = new ArrayList<>();
}
