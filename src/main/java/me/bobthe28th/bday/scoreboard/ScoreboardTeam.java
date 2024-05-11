package me.bobthe28th.bday.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreboardTeam {

    private final String name;
    private final HashMap<ScoreboardController, Team> teams = new HashMap<>();
    private ChatColor color;
    private Location spawnLocation;
    private final List<Entity> globalMembers = new ArrayList<>();

    public ScoreboardTeam(String name) {
        this.name = name;
    }

    public ScoreboardTeam(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public Team getTeam(ScoreboardController controller) {
        return teams.get(controller);
    }

    public void addPlayer(ScoreboardController controller) {
        Team team = controller.getScoreboard().registerNewTeam(name);
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM);
        team.setColor(color);
        teams.put(controller, team);
        for (Entity entity : globalMembers) {
            if (entity instanceof Player player) {
                team.addEntry(player.getName());
            } else {
                team.addEntry(entity.getUniqueId().toString());
            }
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation.clone();
    }

    public void addMember(Entity member, ScoreboardController controller) {
        addMember(teams.get(controller),member);
    }

    public void removeMember(Entity member, ScoreboardController controller) {
        removeMember(teams.get(controller),member);
    }


    private void addMember(Team team, Entity member) {
        if (member instanceof Player player) {
            team.addEntry(player.getName());
        } else {
            team.addEntry(member.getUniqueId().toString());
        }
    }

    private void removeMember(Team team, Entity member) {
        if (member instanceof Player player) {
            team.removeEntry(player.getName());
        } else {
            team.removeEntry(member.getUniqueId().toString());
        }
    }

    public void addGlobalMember(Entity member) {
        if (globalMembers.contains(member)) return;
        globalMembers.add(member);
        for (Team team : teams.values()) {
            addMember(team,member);
        }
    }

    public void removeGlobalMember(Entity member) {
        globalMembers.remove(member);
        for (Team team : teams.values()) {
            removeMember(team, member);
        }
    }

    public void remove() {
        for (ScoreboardController controller : teams.keySet()) {
            if (controller != null) {
                removePlayer(controller, false);
            }
        }
        teams.clear();
    }

    public void removePlayer(ScoreboardController controller, boolean fromList) {
        if (teams.containsKey(controller)) {
            teams.get(controller).unregister();
        }
        if (fromList) teams.remove(controller);
    }
}
