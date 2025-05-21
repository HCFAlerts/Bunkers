package me.traduciendo.bunkers;

import lombok.Getter;
import me.traduciendo.bunkers.clients.ClientHook;
import me.traduciendo.bunkers.commands.ArenaCommand;
import me.traduciendo.bunkers.commands.BunkersCommand;
import me.traduciendo.bunkers.commands.MatchCommand;
import me.traduciendo.bunkers.commands.TeamCommand;
import me.traduciendo.bunkers.holograms.PlaceholderManager;
import me.traduciendo.bunkers.listeners.*;
import me.traduciendo.bunkers.managers.*;
import me.traduciendo.bunkers.nametags.CheatBreakerNametag;
import me.traduciendo.bunkers.providers.board.ScoreboardProvider;
import me.traduciendo.bunkers.providers.tab.TablistProvider;
import me.traduciendo.bunkers.utils.scoreboard.Aether;
import me.traduciendo.bunkers.utils.tablist.Tab;
import me.traduciendo.bunkers.timer.TimerManager;
import me.traduciendo.bunkers.utils.extra.Cooldown;
import me.traduciendo.bunkers.utils.file.FileConfig;
import me.traduciendo.bunkers.utils.menu.ButtonListener;
import me.traduciendo.bunkers.waypoints.WaypointManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Bunkers extends JavaPlugin {

    @Getter private static Bunkers instance;

    private List<Cooldown> cooldowns;

    private MatchManager matchManager;
    private PlayerManager playerManager;
    private ArenaManager arenaManager;
    private TeamManager teamManager;
    private LobbyManager lobbyManager;
    private WaypointManager waypointManager;
    private StoreManager storeManager;
    private NametagListener nameTagListener;
    private CheatBreakerNametag cheatBreakerNametag;

    private TimerManager timerManager;
    private Tab bunkersTablist;

    private Aether aether;
    private FileConfig arenaConfig, settingsConfig, langConfig, tabConfig, boardConfig, playMenuConfig, teleportMenuConfig;

    private ClientHook clientHook;
    //private MongoHandler mongoHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.cooldowns = new ArrayList<>();
        this.arenaConfig = new FileConfig(this,"data/arenas.yml");
        this.settingsConfig = new FileConfig(this,"settings.yml");
        this.langConfig = new FileConfig(this,"lang.yml");
        this.tabConfig = new FileConfig(this,"providers/tablist.yml");
        this.boardConfig = new FileConfig(this,"providers/scoreboard.yml");
        this.playMenuConfig = new FileConfig(this,"menus/playmenu.yml");
        this.teleportMenuConfig = new FileConfig(this,"menus/teleportmenu.yml");
        this.bunkersTablist = new Tab(this,new TablistProvider(),0,5);
        this.playerManager = new PlayerManager(this);
        this.arenaManager = new ArenaManager(this);
        this.teamManager = new TeamManager(this);
        this.lobbyManager = new LobbyManager(this);
        this.matchManager = new MatchManager(this);
        this.storeManager = new StoreManager(this);
        this.aether = new Aether(this,new ScoreboardProvider(this));
        this.clientHook = new ClientHook(this);
        this.waypointManager = new WaypointManager(this);
        this.timerManager = new TimerManager(this);

//        Plugin lunarclientAPI = Bukkit.getPluginManager().getPlugin("LunarClient-API");
//        Plugin cheatbreakerAPI = Bukkit.getPluginManager().getPlugin("CheatBreakerAPI");
//
//        if (lunarclientAPI != null) {
//            NametagListener nametagListener = new NametagListener(this);
//            getServer().getPluginManager().registerEvents(nametagListener, this);
//            nametagListener.startNametagUpdateTask();
//        }
//        if (cheatbreakerAPI != null) {
//            CheatBreakerNametag cheatBreakerNametag = new CheatBreakerNametag(this);
//            getServer().getPluginManager().registerEvents(cheatBreakerNametag, this);
//            cheatBreakerNametag.startNametagUpdateTask();
//        }

        PluginManager manager = this.getServer().getPluginManager();
        if (Bukkit.getPluginManager().isPluginEnabled("LunarClient-API")) {
            manager.registerEvents(new NametagListener(), this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("CheatBreakerAPI")) {
            if (getConfig().getBoolean("NAMETAGS.CHEATBREAKER.ENABLED")) {
                manager.registerEvents(new CBNametagListener(), this);
            }
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamerule randomTickSpeed 550");

        getCommand("bunkers").setExecutor(new BunkersCommand());
        getCommand("match").setExecutor(new MatchCommand());
        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("faction").setExecutor(new TeamCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(),this);
        getServer().getPluginManager().registerEvents(new ButtonListener(), this);
        getServer().getPluginManager().registerEvents(new MatchListener(),this);
        getServer().getPluginManager().registerEvents(new EntityListener(),this);

        new PlaceholderManager(this);
    }

    public NametagListener getNameTagListener() {
        return this.nameTagListener;
    }

    public CheatBreakerNametag getCheatBreakerNametag() {
        return this.cheatBreakerNametag;
    }

    @Override
    public void onDisable() {
        this.arenaManager.saveAll();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=Villager]");
    }
}
