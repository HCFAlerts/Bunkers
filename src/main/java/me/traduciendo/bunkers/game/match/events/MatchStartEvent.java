package me.traduciendo.bunkers.game.match.events;


import me.traduciendo.bunkers.game.match.Match;

public class MatchStartEvent extends MatchEvent {
    public MatchStartEvent(Match match){
        super(match);
    }
}
