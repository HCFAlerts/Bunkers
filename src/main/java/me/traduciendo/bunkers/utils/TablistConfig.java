package me.traduciendo.bunkers.utils;

import me.traduciendo.bunkers.utils.file.FileConfig;

import java.util.List;
import java.util.stream.Collectors;

public class TablistConfig {

    public static List<String> LOBBY_TAB_SKIN_CACHE_LEFT;
    public static List<String> LOBBY_TAB_SKIN_CACHE_MIDDLE;
    public static List<String> LOBBY_TAB_SKIN_CACHE_RIGHT;
    public static List<String> LOBBY_TAB_SKIN_CACHE_FAR_RIGHT;


    public static List<String> WAITING_TAB_SKIN_CACHE_LEFT;
    public static List<String> WAITING_TAB_SKIN_CACHE_MIDDLE;
    public static List<String> WAITING_TAB_SKIN_CACHE_RIGHT;
    public static List<String> WAITING_TAB_SKIN_CACHE_FAR_RIGHT;

    public static List<String> FIGHTING_TAB_SKIN_CACHE_LEFT;
    public static List<String> FIGHTING_TAB_SKIN_CACHE_MIDDLE;
    public static List<String> FIGHTING_TAB_SKIN_CACHE_RIGHT;
    public static List<String> FIGHTING_TAB_SKIN_CACHE_FAR_RIGHT;

    public static List<String> SPECTATING_TAB_SKIN_CACHE_LEFT;
    public static List<String> SPECTATING_TAB_SKIN_CACHE_MIDDLE;
    public static List<String> SPECTATING_TAB_SKIN_CACHE_RIGHT;
    public static List<String> SPECTATING_TAB_SKIN_CACHE_FAR_RIGHT;







    public static void load(FileConfig fileConfig){
        LOBBY_TAB_SKIN_CACHE_LEFT = fileConfig.getConfig().getStringList("LOBBY.LEFT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        LOBBY_TAB_SKIN_CACHE_MIDDLE = fileConfig.getConfig().getStringList("LOBBY.MIDDLE").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        LOBBY_TAB_SKIN_CACHE_RIGHT = fileConfig.getConfig().getStringList("LOBBY.RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        LOBBY_TAB_SKIN_CACHE_FAR_RIGHT = fileConfig.getConfig().getStringList("LOBBY.FAR_RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());


        WAITING_TAB_SKIN_CACHE_LEFT = fileConfig.getConfig().getStringList("WAITING.LEFT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        WAITING_TAB_SKIN_CACHE_MIDDLE = fileConfig.getConfig().getStringList("WAITING.MIDDLE").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        WAITING_TAB_SKIN_CACHE_RIGHT = fileConfig.getConfig().getStringList("WAITING.RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        WAITING_TAB_SKIN_CACHE_FAR_RIGHT = fileConfig.getConfig().getStringList("WAITING.FAR_RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());

        FIGHTING_TAB_SKIN_CACHE_LEFT = fileConfig.getConfig().getStringList("FIGHTING.LEFT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        FIGHTING_TAB_SKIN_CACHE_MIDDLE = fileConfig.getConfig().getStringList("FIGHTING.MIDDLE").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        FIGHTING_TAB_SKIN_CACHE_RIGHT = fileConfig.getConfig().getStringList("FIGHTING.RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        FIGHTING_TAB_SKIN_CACHE_FAR_RIGHT = fileConfig.getConfig().getStringList("FIGHTING.FAR_RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());

        SPECTATING_TAB_SKIN_CACHE_LEFT = fileConfig.getConfig().getStringList("SPECTATING.LEFT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        SPECTATING_TAB_SKIN_CACHE_MIDDLE = fileConfig.getConfig().getStringList("SPECTATING.MIDDLE").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        SPECTATING_TAB_SKIN_CACHE_RIGHT = fileConfig.getConfig().getStringList("SPECTATING.RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        SPECTATING_TAB_SKIN_CACHE_FAR_RIGHT = fileConfig.getConfig().getStringList("SPECTATINGFAR_RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());



    }
}
