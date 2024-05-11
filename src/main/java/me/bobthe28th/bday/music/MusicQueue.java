package me.bobthe28th.bday.music;

import java.util.ArrayList;
import java.util.List;

public class MusicQueue {

    private final List<Music> queue = new ArrayList<>();
    private final List<Music> loopQueue = new ArrayList<>();
    private int loopIndex = 0;

    public void addQueue(Music music) {
        queue.add(music);
    }

    public void addQueueStart(Music music) {
        queue.add(0,music);
    }

    public void addLoopQueue(Music music) {
        loopQueue.add(music);
    }

    public void addQueue(List<Music> music) {
        queue.addAll(music);
    }

    public void addQueueStart(List<Music> music) {
        queue.addAll(0,music);
    }

    public void addLoopQueue(List<Music> music) {
        loopQueue.addAll(music);
    }

    public Music nextInQueue() {
        if (!queue.isEmpty()) {
            return queue.get(0);
        } else if (!loopQueue.isEmpty()) {
            return loopQueue.get(loopIndex);
        } else {
            return null;
        }
    }

    public void advanceQueue() {
        if (!queue.isEmpty()) {
            queue.remove(0);
        } else if (!loopQueue.isEmpty()) {
            loopIndex ++;
            loopIndex %= loopQueue.size();
        }
    }

    public void clearQueue() {
        queue.clear();
        loopIndex = 0;
        loopQueue.clear();
    }

    public List<Music> getQueue() {
        return queue;
    }

    public List<Music> getLoopQueue() {
        return loopQueue;
    }

    public List<String> getQueueName() {
        List<String> nameList = new ArrayList<>();
        for (Music m : queue) {
            nameList.add(m.getName());
        }
        return nameList;
    }

    public List<String> getLoopQueueName() {
        List<String> nameList = new ArrayList<>();
        for (Music m : loopQueue) {
            nameList.add(m.getName());
        }
        return nameList;
    }
}
