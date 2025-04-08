package me.traduciendo.bunkers.game.match.events;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.traduciendo.bunkers.game.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



@RequiredArgsConstructor
@Getter
public class MatchEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Match match;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
