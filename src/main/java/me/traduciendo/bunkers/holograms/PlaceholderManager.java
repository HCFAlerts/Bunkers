package me.traduciendo.bunkers.holograms;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.holograms.placeholders.ClassicPlayersPlaceHolder;

public class PlaceholderManager {

    public PlaceholderManager(Bunkers instance){
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(instance);
        api.registerGlobalPlaceholder("arena-players-classic",20,new ClassicPlayersPlaceHolder());
    }
}
