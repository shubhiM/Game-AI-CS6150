package pacman.game.internal;

import pacman.game.Game;

public class LessStandardNodeSelector implements NodeSelector {
    @Override
    public TreeAction selectBranch(JunctionData junction, PathStatics newStatics, Game game, JunctionData fromJunction) {
        int endNode = junction.nodeID;
        if(junction.nodeID == fromJunction.nodeStartedFrom) return TreeAction.reject;
        if(junction.firstMove == fromJunction.lastMove.opposite()) return TreeAction.reject;
        //int minGhostDistance = PathFinder.getMinGhostDistance(game, endNode);
        //if(minGhostDistance < newStatics.distance * 1.5) return TreeAction.reject;
        if(newStatics.hasNonEdibleGhostOnRoad) return TreeAction.reject;
        if(newStatics.distance >= PathFinder.MAX_LENGTH * 0.75) return TreeAction.selectWithoutRecur;
        return TreeAction.selectWithRecur;
    }
}
