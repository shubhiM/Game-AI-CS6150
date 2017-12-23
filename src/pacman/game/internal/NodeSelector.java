package pacman.game.internal;

import pacman.game.Game;

public interface NodeSelector {
    TreeAction selectBranch(JunctionData junction, PathStatics newStatics, Game game, JunctionData fromJunction);
}
