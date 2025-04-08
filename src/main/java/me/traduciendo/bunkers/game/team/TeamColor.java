package me.traduciendo.bunkers.game.team;

import me.traduciendo.bunkers.utils.chat.CC;

public enum TeamColor {
    Red,Blue,Yellow,Green;

    public static String getChatColor(TeamColor color){
        switch (color){
            case Red:
                return CC.RED;
            case Blue:
                return CC.BLUE;
            case Yellow:
                return CC.YELLOW;
            case Green:
                return CC.GREEN;
        }
        return null;
    }
}
