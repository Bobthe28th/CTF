package me.bobthe28th.bday.games.managers;

import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.GameState;
import me.bobthe28th.bday.games.GameTeam;
import me.bobthe28th.bday.games.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamSelectionManager implements Listener {

    private final Game game;

    private BoundingBox[] boxes = new BoundingBox[]{
            BoundingBox.of(new Vector(-157.5, 91, -18.5),2.5,1,2.5),
            BoundingBox.of(new Vector(-147.5, 91, -18.5),2.5,1,2.5)
    };
    private ArrayList<GameTeam> teamId = new ArrayList<>();
    private final Location spawn;
    private BukkitTask timer;

    public TeamSelectionManager(Game game) {
        this.game = game;
        this.spawn = new Location(game.getWorld(), -152.5, 91.0, -12.5, 180.0f, 0.0f);
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
    }

    public void start() {
        game.setState(GameState.TEAMSELECT);
        // set up team select zone
        int i = 0;
        for (GameTeam team : game.getTeams().values()) {
            for (int j = 0; j < boxes[i].getWidthX(); j++) {
                for (int k = 0; k < boxes[i].getWidthZ(); k++) {
                    game.getWorld().getBlockAt((int) (j + boxes[i].getMinX()), (int) boxes[i].getMinY(),(int) (k + boxes[i].getMinZ())).setType(team.getTeamSelectBlock());
                }
            }
            teamId.set(i,team);
            i++;
            if (i >= boxes.length) break;
        }
        // teleport everyone
        for (GamePlayer player : game.getManager().getGamePlayers().values()) {
            player.getPlayer().teleport(spawn);
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null) { return; }
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
        if (!game.getManager().getGamePlayers().containsKey(event.getPlayer())) return;
        GamePlayer player = game.getManager().getGamePlayers().get(event.getPlayer());
        Location blockPos = event.getTo().getBlock().getLocation().clone().add(0.5, 0, 0.5);
        blockPos.setY(boxes[0].getCenterY());
        boolean inSelection = false;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i].contains(blockPos.toVector())) {
                inSelection = true;
                player.setTeam(teamId.get(i));
                boolean allSelected = true;
                for (GamePlayer other : game.getManager().getGamePlayers().values()) {
                    if (other.getTeam() == null) {
                        allSelected = false;
                        break;
                    }
                }
                if (allSelected) {
                    timer = new BukkitRunnable() {
                        @Override
                        public void run() {
                            //TODO timer things
                        }
                    }.runTaskTimer(game.getPlugin(),0L,20L);
                }
            }
        }
        if (!inSelection) {
            if (player.getTeam() != null) {
                player.leaveTeam();
            }
            timer.cancel();
        }
    }

    public void playerJoin() {
        // or make a fake player manager that can sync with everyone (join / leave)
    }

    public void end() {
        HandlerList.unregisterAll(this);
    }

}
