package me.traduciendo.bunkers.game.team;

import lombok.Getter;
import lombok.Setter;
import me.traduciendo.bunkers.utils.Cuboid;
import me.traduciendo.bunkers.utils.CustomLocation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Team {
    private List<Player> players = new ArrayList<>();
    private TeamColor teamColor;
    private double DTR = 6.0;
    private Cuboid cuboid;
    private UUID combatVillager = null;
    private UUID buildVillager = null;
    private UUID sellVillager = null;
    private UUID enchantVillager = null;
    private CustomLocation spawn;
    public Team(TeamColor teamColor, Cuboid cuboid, CustomLocation spawn){
        this.teamColor = teamColor;
        this.cuboid = cuboid;
        this.spawn = spawn;
    }
}
