package org.zycong.theHordes.helpers.PlaceHolder.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.zycong.theHordes.helpers.PDCHelper.*;

public class DefensePlaceholder extends PlaceholderExpansion {
  @Override
  public @NotNull String getIdentifier() {
    return "defense";
  }

  @Override
  public @NotNull String getAuthor() {
    return "zycong, Tonnam_101";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0";
  }

  @Override
  public String onRequest(OfflinePlayer player, @NotNull String params) {
    if(player.isOnline()){
      Player p = (Player) player;
      if (params.equalsIgnoreCase("current")) {
        return getPlayerPDC("Defense", p);
      }else if(params.equalsIgnoreCase("FuckYou")) {
        return "FUCK YOU!!!!!";
      } else{
        return getPlayerPDC("Defense", p);
      }
    }
    return "Player is not online";
  }
}
