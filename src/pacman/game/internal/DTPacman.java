package pacman.game.internal;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

import java.awt.*;

public class DTPacman extends Controller<MOVE> {
    private PathFinder pathFinder;
    protected PathSelector selector;
    private MOVE prevMove = MOVE.NEUTRAL;
    private NodeSelector nodeSelector;

    private PathTree cachedTree;

    public static PathStatics getTotalStatics(PathTree node){
        PathStatics result = node.statics;
        while (node.getFirstChild() != null){
            node = node.getFirstChild();
            result = node.statics;
        }
        return result;
    }

    public DTPacman(){
        nodeSelector = new JSNodeSelector("data/script/treeExecutor.js", "data/script/decisionTree.js");
        selector = new PillMostPathSelector();
    }

    public PathTree searchPath(Game game, long timeDue){
        if(pathFinder == null) pathFinder = new PathFinder(game, selector, nodeSelector);
        else pathFinder.setGame(game);

        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        int pacmanX = game.getNodeXCood(pacmanIndex);
        int pacmanY = game.getNodeYCood(pacmanIndex);
        MOVE lastMove = game.getPacmanLastMoveMade();
        int prevPlace = game.getNeighbour(pacmanIndex, lastMove.opposite());

        PathTree start = new PathTree(new PathStatics(0, 0, 0, 0, false)
                , new JunctionData(pacmanIndex, lastMove, prevPlace, new int[]{pacmanIndex}, lastMove));

        if(pacmanIndex == 691){
            int aaa = 9;
            System.currentTimeMillis();
            System.currentTimeMillis();
        }

        PathTree finalTree = pathFinder.pathSearch(pacmanIndex, start);
        finalTree = finalTree.getFirstChild();
        return finalTree;
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        cachedTree = searchPath(game, timeDue);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        int pacmanX = game.getNodeXCood(pacmanIndex);
        int pacmanY = game.getNodeYCood(pacmanIndex);
        MOVE lastMove = game.getPacmanLastMoveMade();
        paintDebugPath(cachedTree, game, Color.BLUE);
        int minGhostIndex = 0;
        int minGhostDistance = Integer.MAX_VALUE;
        if (cachedTree == null) {

            pathFinder.setNodeSelector(new LessStandardNodeSelector());
            cachedTree = searchPath(game, timeDue);
            pathFinder.setNodeSelector(nodeSelector);
            paintDebugPath(cachedTree, game, Color.GREEN);
            if(cachedTree != null) return cachedTree.junctionData.firstMove;
            for(Constants.GHOST ghost : Constants.GHOST.values()){
                int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
                int distance = game.getShortestPathDistance(pacmanIndex, ghostIndex);
                if(distance < minGhostDistance){
                    minGhostIndex = ghostIndex;
                    minGhostDistance = distance;
                }
            }
            return game.getNextMoveAwayFromTarget(pacmanIndex, minGhostIndex, Constants.DM.PATH);
        }

        prevMove = cachedTree.junctionData.firstMove;
        return prevMove;
    }

    private void paintDebugPath(PathTree finalTree, Game game, Color color){
        if(!PathFinder.ENABLE_DEBUG_PAINT) return;
        if (finalTree != null){
            for(int index : finalTree.junctionData.path){
                GameView.addPoints(game, color, index);
            }
            for(PathTree subTree : finalTree){
                paintDebugPath(subTree, game, color);
            }
        }else{
            GameView.addPoints(game, Color.RED, game.getPacmanCurrentNodeIndex());
        }
    }
}
