package me.traduciendo.bunkers.tasks;

import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.enums.VillagerType;
import me.traduciendo.bunkers.game.match.Match;
import me.traduciendo.bunkers.game.team.TeamColor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class VillagerSpawnRunnable extends BukkitRunnable {

    private  final Bunkers plugin = Bunkers.getInstance();

    private final Match match;

    @Override
    public void run() {

        plugin.getMatchManager().createVillager(match, TeamColor.Red, VillagerType.COMBAT);
        plugin.getMatchManager().createVillager(match, TeamColor.Red, VillagerType.BUILD);
        plugin.getMatchManager().createVillager(match, TeamColor.Red, VillagerType.SELL);


        plugin.getMatchManager().createVillager(match, TeamColor.Blue, VillagerType.COMBAT);
        plugin.getMatchManager().createVillager(match, TeamColor.Blue, VillagerType.BUILD);
        plugin.getMatchManager().createVillager(match, TeamColor.Blue, VillagerType.SELL);

        plugin.getMatchManager().createVillager(match, TeamColor.Yellow, VillagerType.COMBAT);
        plugin.getMatchManager().createVillager(match, TeamColor.Yellow, VillagerType.BUILD);
        plugin.getMatchManager().createVillager(match, TeamColor.Yellow, VillagerType.SELL);

        plugin.getMatchManager().createVillager(match, TeamColor.Green, VillagerType.COMBAT);
        plugin.getMatchManager().createVillager(match, TeamColor.Green, VillagerType.BUILD);
        plugin.getMatchManager().createVillager(match, TeamColor.Green, VillagerType.SELL);

    }

}
