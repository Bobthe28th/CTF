package me.bobthe28th.bday.music;

public class Music {

    private final String name;
    private final Long length;

    public Music(String name, Long length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public Long getLength() {
        return length;
    }
}
