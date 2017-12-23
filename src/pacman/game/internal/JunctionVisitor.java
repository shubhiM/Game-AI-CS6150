package pacman.game.internal;

import pacman.game.Constants;

public class JunctionVisitor {
    public int nodeID;
    public int nodeStartedFrom;
    public Constants.MOVE firstMove;
    public Constants.MOVE lastMove;
    public int[] path;
    public int[] reversePath;

    public JunctionVisitor(JunctionData junctionData){
        nodeID = junctionData.nodeID;
        nodeStartedFrom = junctionData.nodeStartedFrom;
        firstMove = junctionData.firstMove;
        lastMove = junctionData.lastMove;
        path = junctionData.path;
        reversePath = junctionData.reversePath;
    }
}
