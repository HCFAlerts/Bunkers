package me.traduciendo.bunkers.utils.tablist.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.traduciendo.bunkers.utils.tablist.skin.Skin;

@Setter
@Accessors(chain = true)
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TabEntry {

  private final int column;
  private final int row;

  private final String text;

  private int ping = -1;
  private Skin skin = Skin.DEFAULT_SKIN;
}