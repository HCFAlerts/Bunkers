package me.traduciendo.bunkers.utils.tablist;

import lombok.Getter;
import me.traduciendo.bunkers.utils.tablist.adapter.TabAdapter;
import me.traduciendo.bunkers.utils.tablist.layout.TabLayout;
import me.traduciendo.bunkers.utils.tablist.listener.TabListener;
import me.traduciendo.bunkers.utils.tablist.packet.TabPacket;
import me.traduciendo.bunkers.utils.tablist.runnable.TabRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class Tab {

    /*
    Forked from Hatsur API
    Links:
    -> https://github.com/norxir/seventab
    -> https://github.com/norxir/eighttab
     */

  @Getter private static Tab instance;

  private final TabAdapter adapter;

  private Version version;

  public Tab(JavaPlugin plugin, TabAdapter adapter, int delay1, int delay2) {
    instance = this;
    this.adapter = adapter;

    String packageName = Bukkit.getServer().getClass().getPackage().getName();
    String version = packageName.substring(packageName.lastIndexOf('.') + 1);
    try {
      this.version = Version.valueOf(version);
      plugin.getLogger().info("[Tab] Using " + this.version.name() + " version.");
    } catch (final Exception e) {
      e.printStackTrace();
      return;
    }

    handlerPacket(plugin);

    Bukkit.getServer().getPluginManager().registerEvents(new TabListener(this), plugin);
    if (Objects.requireNonNull(this.version) == Version.v1_8_R3) {
      Bukkit.getServer().getScheduler()
          .runTaskTimerAsynchronously(plugin, new TabRunnable(adapter), delay1,
              delay2); //TODO: async to run 1 millis
    }
  }

  private void handlerPacket(JavaPlugin plugin) {
    if (Objects.requireNonNull(this.version) == Version.v1_8_R3) {
      new TabPacket(plugin);
    }
  }

  public void onDisable() {
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      removePlayer(player);
    }
  }

  public void removePlayer(Player player) {
    boolean continueAt = false;
    if (Objects.requireNonNull(this.version) == Version.v1_8_R3) {
      if (TabLayout.getLayoutMapping().containsKey(player.getUniqueId())) {
        continueAt = true;
      }

      if (continueAt) {
        TabLayout.getLayoutMapping().remove(player.getUniqueId());
      }
    }
  }

  public enum Version {
    v1_8_R3
  }
}
