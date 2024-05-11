package me.bobthe28th.bday.games;

import me.bobthe28th.bday.games.ctf.CTFFlag;
import me.bobthe28th.bday.scoreboard.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GameTeam {


    protected final ScoreboardTeam team;
    protected final Material teamSelectBlock;

    public GameTeam(String name, ChatColor color, Material teamSelectBlock) {
        team = new ScoreboardTeam(name,color);
        this.teamSelectBlock = teamSelectBlock;
    }

    public Material getTeamSelectBlock() {
        return teamSelectBlock;
    }

    public ScoreboardTeam getTeam() {
        return team;
    }
}
