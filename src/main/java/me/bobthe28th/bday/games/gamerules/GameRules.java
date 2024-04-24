package me.bobthe28th.bday.games.gamerules;

import org.bukkit.GameRule;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameRules {

    private HashMap<String, GameRuleValue<?>> rules = new HashMap<>();

    public HashMap<String, GameRuleValue<?>> getRules() {
        return rules;
    }

    public GameRuleValue<?> getRule(String name) {
        return rules.get(name);
    }

    public void setRule(String name, Object value) {
        rules.put(name, new GameRuleValue<>(value));
    }

}
