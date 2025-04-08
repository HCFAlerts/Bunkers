package me.traduciendo.bunkers.holograms.placeholders;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplaceFunction;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.game.match.Match;
import org.jetbrains.annotations.Nullable;

public class ClassicPlayersPlaceHolder implements GlobalPlaceholderReplaceFunction {

    private final Bunkers plugin =  Bunkers.getInstance();

    @Override
    public @Nullable String getReplacement(@Nullable String s) {
        Match match = plugin.getMatchManager().getMatch("Classic");
        if (match == null) {
            return String.valueOf(0);
        } else {
            return String.valueOf(match.getPlayerList().size());
        }
    }
}
