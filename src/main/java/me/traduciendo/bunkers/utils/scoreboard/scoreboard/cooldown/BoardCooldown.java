package me.traduciendo.bunkers.utils.scoreboard.scoreboard.cooldown;

import lombok.Getter;
import me.traduciendo.bunkers.utils.scoreboard.scoreboard.Board;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;

public class BoardCooldown {

    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    @Getter private final Board board;
    @Getter private final String id;
    @Getter private final double duration;
    @Getter private final long end;

    public BoardCooldown(Board board, String id, double duration) {
        this.board = board;
        this.id = id;
        this.duration = duration;
        this.end = (long) (System.currentTimeMillis() + (duration * 1000));

        board.getCooldowns().add(this);
    }

    public String getFormattedString(BoardFormat format) {
        if (format == null) throw new NullPointerException();
        if (format == BoardFormat.SECONDS) {
            return SECONDS_FORMATTER.format(((end - System.currentTimeMillis()) / 1000.0f));
        } else {
            return DurationFormatUtils.formatDuration(end - System.currentTimeMillis(), "mm:ss");
        }
    }

    public void cancel() {
        board.getCooldowns().remove(this);
    }
}
