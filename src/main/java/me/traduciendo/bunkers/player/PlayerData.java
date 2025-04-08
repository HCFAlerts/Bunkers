package me.traduciendo.bunkers.player;

import lombok.Data;
import me.traduciendo.bunkers.game.team.TeamColor;

import java.util.UUID;

@Data
public class PlayerData {
    private final UUID uuid;
    private PlayerState playerState;
    private String currentMatchName;
    private TeamColor currentTeamColor;
    private int wins;
    private int loses;
    private int kills;
    private int deaths;
    private int pearlCooldown = 0;
    private int balance;
}
