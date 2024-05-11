package me.bobthe28th.bday.music;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {

    private final Main plugin;
    private Music currentlyPlaying;

    private BukkitTask musicLength;
    private final MusicQueue queue;

    List<Music> musicList = List.of();

    public MusicManager(Main plugin) {
        this.plugin = plugin;
        queue = new MusicQueue();
    }

    public void start() {
        if (currentlyPlaying == null) {
            currentlyPlaying = queue.nextInQueue();
            queue.advanceQueue();
        }
        if (musicLength == null || musicLength.isCancelled()) {
            playCurrent();
        }
    }

    public void playCurrent() {
        if (currentlyPlaying != null) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Now playing: " + ChatColor.RED + currentlyPlaying.getName());
            for (GamePlayer p : plugin.getGameManager().getGamePlayers().values()) {
                if (currentlyPlaying.getName().equals("winnersong")) {
                    p.getPlayer().playSound(p.getPlayer().getLocation(), currentlyPlaying.getName(), SoundCategory.MUSIC, 0.3f, 1);
                } else {
                    p.getPlayer().playSound(p.getPlayer().getLocation(), currentlyPlaying.getName(), SoundCategory.MUSIC, 1f, 1);
                }
            }
            musicLength = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!this.isCancelled()) {
                        playNext();
                    }
                }
            }.runTaskLater(plugin,currentlyPlaying.getLength() + 20L);
        }
    }

    public void playNext() {
//        stopCurrent();
        currentlyPlaying = queue.nextInQueue();
        playCurrent();
        queue.advanceQueue();
    }

    public void clearAndPlayLoop(Music m) {
        getQueue().clearQueue();
        getQueue().addLoopQueue(m);
        start();
    }

    public void stopAndClear() {
        stopCurrent();
        currentlyPlaying = null;
        queue.clearQueue();
    }

    public void stopCurrent() {
        if (musicLength != null) {
            musicLength.cancel();
        }
        if (currentlyPlaying != null) {
            for (GamePlayer p : plugin.getGameManager().getGamePlayers().values()) {
                p.getPlayer().stopSound(currentlyPlaying.getName(),SoundCategory.MUSIC);
            }
        }
    }

    public List<String> getMusicNameList() {
        List<String> nameList = new ArrayList<>();
        for (Music m : musicList) {
            nameList.add(m.getName());
        }
        return nameList;
    }

    public String getCurrentlyPlayingName() {
        if (currentlyPlaying == null) {
            return null;
        }
        return currentlyPlaying.getName();
    }

    public Music getMusicByName(String name) {
        for (Music m : musicList) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    public MusicQueue getQueue() {
        return queue;
    }

    public void disable() {
        stopCurrent();
    }
}
