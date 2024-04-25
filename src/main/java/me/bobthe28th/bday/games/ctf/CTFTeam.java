package me.bobthe28th.bday.games.ctf;

import me.bobthe28th.bday.games.GameTeam;
import me.bobthe28th.bday.scoreboard.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CTFTeam extends GameTeam {

    private int score = 0;
    private final Material banner;

    private final CTFFlag flag;

    public CTFTeam(String name, ChatColor color, Material teamSelectBlock, Material banner) {
        super(name, color, teamSelectBlock);
        this.banner = banner;
        this.flag = new CTFFlag();
    }

}
