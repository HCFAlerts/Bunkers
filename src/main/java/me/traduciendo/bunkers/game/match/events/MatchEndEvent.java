package me.traduciendo.bunkers.game.match.events;


import me.traduciendo.bunkers.game.match.Match;

public class MatchEndEvent extends MatchEvent {
    public MatchEndEvent(Match match){
        super(match);
    }
}
