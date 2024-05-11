package me.bobthe28th.bday.games.managers;

import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.GameState;

public class ClassSelectionManager {

    Game game;

    private int[][] positionIndex = new int[][]{{3},{2,4},{1,3,5},{0,2,4,6},{1,2,3,4,5}};

    public ClassSelectionManager(Game game) {
        this.game = game;
    }

    public void start() {
        game.setState(GameState.CLASSSELECT);
    }

}
