package me.traduciendo.bunkers.commands;

import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.arena.Arena;
import me.traduciendo.bunkers.game.team.TeamColor;
import me.traduciendo.bunkers.menus.arena.ArenaEditorMenu;
import me.traduciendo.bunkers.utils.CustomLocation;
import me.traduciendo.bunkers.utils.chat.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ArenaCommand implements CommandExecutor {
    private Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        if (!commandSender.hasPermission("bunkers.command.arena")) {
            return false;
        }

        Player player = (Player) commandSender;

        if(args.length==0){
            this.sendMainHelp(player);
            return false;
        }

        if(args.length>0){
            if(args[0].equalsIgnoreCase("list")){
                this.sendArenaList(player);
            }else if(args[0].equalsIgnoreCase("edit") || (args[0].equalsIgnoreCase("editor") || (args[0].equalsIgnoreCase("manage")))){
                new ArenaEditorMenu().openMenu(player);
            }
        }

        if(args.length == 2){
            if(args[0].equalsIgnoreCase("create")){
                if(plugin.getArenaManager().getArena(args[1])!=null){
                    player.sendMessage(CC.RED+"This arena already exists");
                    return false;
                }
                plugin.getArenaManager().createArena(args[1]);
                player.sendMessage(CC.GREEN+"Created arena '"+args[1]+"'");
            }else if(args[0].equalsIgnoreCase("join")){

                if(plugin.getArenaManager().getArena(args[1])==null){
                    player.sendMessage(CC.RED+"This arena does not exist");
                    return false;
                }

            }else if(args[0].equalsIgnoreCase("info")){
                Arena arena = plugin.getArenaManager().getArena(args[1]);
                if(arena == null){
                    player.sendMessage(CC.RED+"This arena does not exists");
                    return false;
                }
                this.sendArenaInfo(arena,player);

            }

        } else if (args.length==3) {
            if(args[0].equalsIgnoreCase("set")){
                this.sendSetHelp(player);
                if(args[1].equalsIgnoreCase("lobby")){
                    Arena arena = plugin.getArenaManager().getArena(args[2]);
                    if(arena==null){
                        player.sendMessage(CC.RED+"This arena does not exist");
                        return false;
                    }
                    arena.setLobby(CustomLocation.fromBukkitLocation(player.getLocation()));
                    player.sendMessage(CC.GREEN+"Set lobby for arena "+arena.getName());
                }else if(args[1].equalsIgnoreCase("minKoth")){
                    Arena arena = plugin.getArenaManager().getArena(args[2]);
                    if(arena==null){
                        player.sendMessage(CC.RED+"This arena does not exist");
                        return false;
                    }
                    arena.setKothClaimMin(CustomLocation.fromBukkitLocation(player.getLocation()));
                    player.sendMessage(CC.GREEN+"Set minKoth for arena "+arena.getName());
                } else if (args[1].equalsIgnoreCase("maxKoth")) {
                    Arena arena = plugin.getArenaManager().getArena(args[2]);
                    if(arena==null){
                        player.sendMessage(CC.RED+"This arena does not exist");
                        return false;
                    }
                    arena.setKothClaimMax(CustomLocation.fromBukkitLocation(player.getLocation()));
                    player.sendMessage(CC.GREEN+"Set maxKoth for arena "+arena.getName());
                }  else if (args[1].equalsIgnoreCase("icon")) {
                    Arena arena = plugin.getArenaManager().getArena(args[2]);
                    if (arena == null) {
                        player.sendMessage(CC.RED + "This arena does not exist");
                        return false;
                    }
                    arena.setIconMaterial(player.getItemInHand().getType());
                    arena.setIconData(player.getItemInHand().getDurability());
                    player.sendMessage(CC.GREEN + "Set icon for arena " + arena.getName());
                }
            }
        } else if(args.length == 4){
            if(args[0].equalsIgnoreCase("set")){
                if(args[1].equalsIgnoreCase("spawn")){
                    TeamColor color = TeamColor.valueOf(args[2]);
                    Arena arena = plugin.getArenaManager().getArena(args[3]);
                    if(color == null){
                        player.sendMessage(CC.RED+"This team color does not exists");
                        return false;
                    }
                    if(arena == null){
                        player.sendMessage(CC.RED+"This arena does not exists");
                        return false;
                    }
                    if(color==TeamColor.Red){
                        arena.setRedSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Blue){
                        arena.setBlueSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Green){
                        arena.setGreenSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Yellow){
                        arena.setYellowSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }
                    player.sendMessage(CC.GREEN+"Set the spawn location of team "+color.name()+" in "+arena.getName());

                }else if(args[1].equalsIgnoreCase("maxClaimCorner")){
                    TeamColor color = TeamColor.valueOf(args[2]);
                    Arena arena = plugin.getArenaManager().getArena(args[3]);
                    if(color == null){
                        player.sendMessage(CC.RED+"This team color does not exists");
                        return false;
                    }
                    if(arena == null){
                        player.sendMessage(CC.RED+"This arena does not exists");
                        return false;
                    }
                    if(color==TeamColor.Red){
                        arena.setRedClaimMax(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Blue){
                        arena.setBlueClaimMax(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Green){
                        arena.setGreenClaimMax(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Yellow){
                        arena.setYellowClaimMax(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }
                    player.sendMessage(CC.GREEN+"Set the maxClaimCorner location of team "+color.name()+" in "+arena.getName());

                }else if(args[1].equalsIgnoreCase("minClaimCorner")){
                    TeamColor color = TeamColor.valueOf(args[2]);
                    Arena arena = plugin.getArenaManager().getArena(args[3]);
                    if(color == null){
                        player.sendMessage(CC.RED+"This team color does not exists");
                        return false;
                    }
                    if(arena == null){
                        player.sendMessage(CC.RED+"This arena does not exists");
                        return false;
                    }
                    if(color==TeamColor.Red){
                        arena.setRedClaimMin(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Blue){
                        arena.setBlueClaimMin(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Green){
                        arena.setGreenClaimMin(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Yellow){
                        arena.setYellowClaimMin(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }
                    player.sendMessage(CC.GREEN+"Set the minClaimCorner location of team "+color.name()+" in "+arena.getName());

                }else if(args[1].equalsIgnoreCase("combatVillager")){
                    TeamColor color = TeamColor.valueOf(args[2]);
                    Arena arena = plugin.getArenaManager().getArena(args[3]);
                    if(color == null){
                        player.sendMessage(CC.RED+"This team color does not exists");
                        return false;
                    }
                    if(arena == null){
                        player.sendMessage(CC.RED+"This arena does not exists");
                        return false;
                    }
                    if(color==TeamColor.Red){
                        arena.setRedVillagerCombatSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Blue){
                        arena.setBlueVillagerCombatSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Green){
                        arena.setGreenVillagerCombatSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Yellow){
                        arena.setYellowVillagerCombatSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }
                    player.sendMessage(CC.GREEN+"Set the combatVillager location of team "+color.name()+" in "+arena.getName());

                }else if(args[1].equalsIgnoreCase("buildVillager")){
                    TeamColor color = TeamColor.valueOf(args[2]);
                    Arena arena = plugin.getArenaManager().getArena(args[3]);
                    if(color == null){
                        player.sendMessage(CC.RED+"This team color does not exists");
                        return false;
                    }
                    if(arena == null){
                        player.sendMessage(CC.RED+"This arena does not exists");
                        return false;
                    }
                    if(color==TeamColor.Red){
                        arena.setRedVillagerBuildSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Blue){
                        arena.setBlueVillagerBuildSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Green){
                        arena.setGreenVillagerBuildSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Yellow){
                        arena.setYellowVillagerBuildSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }
                    player.sendMessage(CC.GREEN+"Set the buildVillager location of team "+color.name()+" in "+arena.getName());

                }else if(args[1].equalsIgnoreCase("sellVillager")){
                    TeamColor color = TeamColor.valueOf(args[2]);
                    Arena arena = plugin.getArenaManager().getArena(args[3]);
                    if(color == null){
                        player.sendMessage(CC.RED+"This team color does not exists");
                        return false;
                    }
                    if(arena == null){
                        player.sendMessage(CC.RED+"This arena does not exists");
                        return false;
                    }
                    if(color==TeamColor.Red){
                        arena.setRedVillagerSellSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Blue){
                        arena.setBlueVillagerSellSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Green){
                        arena.setGreenVillagerSellSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }else if(color==TeamColor.Yellow){
                        arena.setYellowVillagerSellSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
                    }
                    player.sendMessage(CC.GREEN+"Set the sellVillager location of team "+color.name()+" in "+arena.getName());

                } else {
                    this.sendSetHelp(player);
                }
            }
        }

        return false;
    }

    public void sendMainHelp(Player player){
        player.sendMessage(CC.translate("&7&m------------------------"));
        player.sendMessage(CC.translate("&6&lArena Help &7(1/1)"));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("&f/arena create <name>"));
        player.sendMessage(CC.translate("&f/arena join <name>"));
        player.sendMessage(CC.translate("&f/arena editor"));
        player.sendMessage(CC.translate("&f/arena list"));
        player.sendMessage(CC.translate("&f/arena info"));
        player.sendMessage(CC.translate("&f/arena set"));
        player.sendMessage(CC.translate("&7&m------------------------"));
    }

    public void sendSetHelp(Player player){
        player.sendMessage(CC.translate("&7&m----------------------------------"));
        player.sendMessage(CC.translate("&6&lArena Set Help &7(1/1)"));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("&f/arena set spawn <teamcolor> <arena>"));
        player.sendMessage(CC.translate("&f/arena set lobby <arena>"));
        player.sendMessage(CC.translate("&f/arena set maxClaimCorner <teamcolor> <arena>"));
        player.sendMessage(CC.translate("&f/arena set minClaimCorner <teamcolor> <arena>"));
        player.sendMessage(CC.translate("&f/arena set maxKothCorner <arena>"));
        player.sendMessage(CC.translate("&f/arena set minKothCorner <arena>"));
        player.sendMessage(CC.translate("&f/arena set combatVillager <teamcolor> <arena>"));
        player.sendMessage(CC.translate("&f/arena set buildVillager <teamcolor> <arena>"));
        player.sendMessage(CC.translate("&f/arena set sellVillager <teamcolor> <arena>"));
        player.sendMessage(CC.translate("&7&m----------------------------------"));
    }

    public void sendArenaList(Player player){
        player.sendMessage(CC.translate("&7&m------------------------"));
        player.sendMessage(CC.translate("&6&lArena List &7(1/1)"));
        player.sendMessage(CC.translate(" "));
        for(Map.Entry<String,Arena> entry : plugin.getArenaManager().arenaMap.entrySet()){
            Arena arena = entry.getValue();
            player.sendMessage(CC.translate("&e - &e&l"+arena.getName()));
        }
        player.sendMessage(CC.translate("&7&m------------------------"));
    }

    public void sendArenaInfo(Arena arena,Player player){
        player.sendMessage(CC.translate("&7&m------------------------"));
        player.sendMessage(CC.translate("&6&lArena Info &7- &e&l"+arena.getName()));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("&fName: &e"+arena.getName()));
        player.sendMessage(CC.translate("&fLobby: "+formatLocation(arena.getLobby())));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("&e&lTeams: "));
        player.sendMessage(CC.translate(" &c&lRED: "));
        player.sendMessage(CC.translate("  &fSpawn: "+formatLocation(arena.getRedSpawn())));
        player.sendMessage(CC.translate("  &fClaimMaxCorner: "+formatLocation(arena.getRedClaimMax())));
        player.sendMessage(CC.translate("  &fClaimMinCorner: "+formatLocation(arena.getRedClaimMin())));
        player.sendMessage(CC.translate("  &fSellVillager: "+formatLocation(arena.getRedVillagerSellSpawn())));
        player.sendMessage(CC.translate("  &fBuildVillager: "+formatLocation(arena.getRedVillagerBuildSpawn())));
        player.sendMessage(CC.translate("  &fCombatVillager: "+formatLocation(arena.getRedVillagerCombatSpawn())));
        player.sendMessage(CC.translate(" &9&lBLUE: "));
        player.sendMessage(CC.translate("  &fSpawn: "+formatLocation(arena.getBlueSpawn())));
        player.sendMessage(CC.translate("  &fClaimMaxCorner: "+formatLocation(arena.getBlueClaimMax())));
        player.sendMessage(CC.translate("  &fClaimMinCorner: "+formatLocation(arena.getBlueClaimMin())));
        player.sendMessage(CC.translate("  &fSellVillager: "+formatLocation(arena.getBlueVillagerSellSpawn())));
        player.sendMessage(CC.translate("  &fBuildVillager: "+formatLocation(arena.getBlueVillagerBuildSpawn())));
        player.sendMessage(CC.translate("  &fCombatVillager: "+formatLocation(arena.getBlueVillagerCombatSpawn())));
        player.sendMessage(CC.translate(" &a&lGREEN: "));
        player.sendMessage(CC.translate("  &fSpawn: "+formatLocation(arena.getGreenSpawn())));
        player.sendMessage(CC.translate("  &fClaimMaxCorner: "+formatLocation(arena.getGreenClaimMax())));
        player.sendMessage(CC.translate("  &fClaimMinCorner: "+formatLocation(arena.getGreenClaimMin())));
        player.sendMessage(CC.translate("  &fSellVillager: "+formatLocation(arena.getGreenVillagerSellSpawn())));
        player.sendMessage(CC.translate("  &fBuildVillager: "+formatLocation(arena.getGreenVillagerBuildSpawn())));
        player.sendMessage(CC.translate("  &fCombatVillager: "+formatLocation(arena.getGreenVillagerCombatSpawn())));
        player.sendMessage(CC.translate(" &e&lYELLOW: "));
        player.sendMessage(CC.translate("  &fSpawn: "+formatLocation(arena.getYellowSpawn())));
        player.sendMessage(CC.translate("  &fClaimMaxCorner: "+formatLocation(arena.getYellowClaimMax())));
        player.sendMessage(CC.translate("  &fClaimMinCorner: "+formatLocation(arena.getYellowClaimMin())));
        player.sendMessage(CC.translate("  &fSellVillager: "+formatLocation(arena.getYellowVillagerSellSpawn())));
        player.sendMessage(CC.translate("  &fBuildVillager: "+formatLocation(arena.getYellowVillagerBuildSpawn())));
        player.sendMessage(CC.translate("  &fCombatVillager: "+formatLocation(arena.getYellowVillagerCombatSpawn())));
        player.sendMessage(CC.translate("&7&m------------------------"));
    }
    public String formatLocation(CustomLocation location){
        return CC.translate("&6&lX: &e"+(int)location.getX()+", &6&lY: &e"+(int)location.getY()+", &6&lZ: &e"+(int)location.getZ());
    }
}
