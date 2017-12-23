package pacman.game.internal;

import pacman.game.Game;

import java.util.ArrayList;

public interface PathSelector {
    PathTree select(ArrayList<PathTree> list);
    void setGame(Game game);
}
