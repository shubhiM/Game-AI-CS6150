package pacman.game.internal;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;
import sun.reflect.generics.tree.Tree;

import java.awt.*;
import java.util.ArrayList;

/**
 * PathFinder is the class that could calculate a alive path
 * gives a game instance to start the calculate
 */
public class PathFinder {
    public static final NearestJunctionMapper[] junctionMappers;
    public static int MAX_LENGTH = 37;
    public static boolean ENABLE_DEBUG_PAINT = true;

    static {
        junctionMappers = new NearestJunctionMapper[Game.caches.length];
        for(int i = 0; i < Game.caches.length; i++){
            junctionMappers[i] = new NearestJunctionMapper(Game.caches[i], i);
        }
    }

    private static class GhostNode{
        public final int index;
        public final MOVE lastMove;
        public final GHOST type;

        public GhostNode(int index, MOVE lastMove, GHOST type){
            this.index = index;
            this.lastMove = lastMove;
            this.type = type;
        }
    }

    private static void paintPath(Game game, JunctionData path, Color color){
        if(!PathFinder.ENABLE_DEBUG_PAINT) return;
        GameView.addPoints(game, color, path.path);
    }

    private Game game;
    private PathStaticCalculator calculator;
    private GhostNode[] ghosts;
    private PathSelector selector;
    private NodeSelector nodeSelector;

    public void setGame(Game game) {
        if(game == null) throw new NullPointerException();
        this.game = game;
        calculator = new PathStaticCalculator(game);

        // init ghosts array
        ArrayList<GhostNode> ghostList = new ArrayList<>(4);
        for(GHOST ghost : GHOST.values()){
            if(game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0){
                ghostList.add(new GhostNode(
                        game.getGhostCurrentNodeIndex(ghost),
                        game.getGhostLastMoveMade(ghost), ghost));
            }
        }
        ghosts = new GhostNode[ghostList.size()];
        ghostList.toArray(ghosts);
        selector.setGame(game);
    }

    public void setSelector(PathSelector selector) {
        this.selector = selector;
    }

    public void setNodeSelector(NodeSelector nodeSelector) {
        this.nodeSelector = nodeSelector;
    }

    public Game getGame() {
        return game;
    }

    public PathFinder(Game game, PathSelector selector, NodeSelector nodeSelector){
        setSelector(selector);
        setNodeSelector(nodeSelector);
        setGame(game);
    }

    private JunctionData[] getNearestJunction(int nodeIndex){
        int mazeIndex = game.getMazeIndex();
        final DNode dNode = Game.caches[mazeIndex].nodes[nodeIndex];
        if(dNode.isJunction){
            return PathFinder.junctionMappers[mazeIndex].getPathsFromNodeIndex(nodeIndex);
        }else{
            JunctionData[] result = new JunctionData[dNode.closestJunctions.size()];
            dNode.closestJunctions.toArray(result);
            return result;
        }
    }

    public static int getMinGhostDistance(Game game, int targetIndex){
        int minDistance = Integer.MAX_VALUE;
        for(GHOST ghost : GHOST.values()){
            if(game.getGhostEdibleTime(ghost) > 10 || game.getGhostLairTime(ghost) > 0) continue;
            int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
            MOVE lastMove = game.getGhostLastMoveMade(ghost);
            int ghostDistance = game.getShortestPathDistance(ghostIndex, targetIndex, lastMove);
            if(ghostDistance > 0 && ghostDistance <= minDistance){
                minDistance = ghostDistance;
            }
        }
        return minDistance;
    }

    public PathTree pathSearch(int startIndex, PathTree root){
        JunctionData[] nearbyJuncs = getNearestJunction(startIndex);
        PathStatics initialStatic = root.statics;
        ArrayList<PathTree> waitList = new ArrayList<>(nearbyJuncs.length);
        for(JunctionData junction : nearbyJuncs){
            PathStatics newStatic = calculator.calculate(junction).add(initialStatic);
            int endNode = junction.nodeID;

            TreeAction result = nodeSelector.selectBranch(junction, newStatic, game, root.junctionData);
            switch (result) {
                case reject: continue;
                case selectWithoutRecur:
                    waitList.add(new PathTree(newStatic, junction));
                    continue;
                case selectWithRecur:
            }

            // other condition, the distance is less than 70, recursive call and append
            PathTree newNode = new PathTree(newStatic, junction);
            newNode = pathSearch(endNode, newNode);
            // if there is no child in newNode, means unable to find a path in new node, ignore it
            if(newNode.getFirstChild() == null){
                if(newNode.statics.powerPillsOnRoad <= 0){
                    continue;
                }
            }
            waitList.add(newNode);
        }
        if(waitList.size() != 0) {
            PathTree remainNode = selector.select(waitList);
            for(PathTree tree : waitList){
                if(tree != remainNode){
                    paintPath(game, tree.junctionData, Color.gray);
                }
            }
            root.addChild(remainNode);
        }
        return root;
    }
}
