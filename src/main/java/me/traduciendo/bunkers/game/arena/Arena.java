package me.traduciendo.bunkers.game.arena;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.utils.CustomLocation;
import org.bukkit.Material;


@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Arena {
    private final String name;
    private CustomLocation redSpawn;
    private CustomLocation redClaimMax;
    private CustomLocation redClaimMin;
    private CustomLocation redVillagerSellSpawn;
    private CustomLocation redVillagerBuildSpawn;
    private CustomLocation redVillagerCombatSpawn;

    private CustomLocation blueSpawn;
    private CustomLocation blueClaimMax;
    private CustomLocation blueClaimMin;
    private CustomLocation blueVillagerSellSpawn;
    private CustomLocation blueVillagerBuildSpawn;
    private CustomLocation blueVillagerCombatSpawn;

    private CustomLocation yellowSpawn;
    private CustomLocation yellowClaimMax;
    private CustomLocation yellowClaimMin;
    private CustomLocation yellowVillagerSellSpawn;
    private CustomLocation yellowVillagerBuildSpawn;
    private CustomLocation yellowVillagerCombatSpawn;

    private CustomLocation greenSpawn;
    private CustomLocation greenClaimMax;
    private CustomLocation greenClaimMin;
    private CustomLocation greenVillagerSellSpawn;
    private CustomLocation greenVillagerBuildSpawn;
    private CustomLocation greenVillagerCombatSpawn;



    private CustomLocation kothClaimMax;
    private CustomLocation kothClaimMin;

    private CustomLocation lobby;

    private Material iconMaterial;
    private int iconData;






}
