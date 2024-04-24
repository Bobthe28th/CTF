package me.bobthe28th.bday.games.managers;

import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.GameState;

public class ClassSelectionManager {

    Game game;
    public ClassSelectionManager(Game game) {
        this.game = game;
    }

    public void start() {
        game.setState(GameState.CLASSSELECT);
    }

}
