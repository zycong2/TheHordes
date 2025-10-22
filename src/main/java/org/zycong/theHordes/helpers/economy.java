package org.zycong.theHordes.helpers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class economy {
    public void setEco(Player p , float amount, String Economy){
        PDCHelper.setPlayerPDC("eco." + Economy, p, String.valueOf(amount));
    }

    public void addEco(Player p , float amount, String Economy){
        PDCHelper.setPlayerPDC("eco." + Economy, p, String.valueOf(Float.valueOf(PDCHelper.getPlayerPDC("eco." + Economy, p))+amount));
    }
    public void removeEco(Player p , float amount, String Economy){
        PDCHelper.setPlayerPDC("eco." + Economy, p, String.valueOf(Float.valueOf(PDCHelper.getPlayerPDC("eco." + Economy, p))-amount));
    }

}
