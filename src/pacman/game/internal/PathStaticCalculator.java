package pacman.game.internal;

import pacman.game.Constants.GHOST;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class is used to calculate PathStatics, each calculation result is stored in cache,
 * the result is first retried from cache, if miss then calculate
 */
public class PathStaticCalculator {
    private final Game game;
    private final HashMap<JunctionData, PathStatics> map;

    private final int[] edibleGhostIndex;
    private final int[] nonEdibleGhostIndex;

    public PathStaticCalculator(Game game){
        this.game = game;
        this.map = new HashMap<>();

        // calculate edibleGhostIndex
        ArrayList<Integer> indexes = new ArrayList<>(4);
        ArrayList<Integer> nonEdibleIndexes = new ArrayList<>(4);
        for(GHOST ghost : GHOST.values()){
            if(game.isGhostEdible(ghost)){
                indexes.add(game.getGhostCurrentNodeIndex(ghost));
            }else{
                nonEdibleIndexes.add(game.getGhostCurrentNodeIndex(ghost));
            }
        }
        edibleGhostIndex = new int[indexes.size()];
        for(int i = 0; i < edibleGhostIndex.length; i++){
            edibleGhostIndex[i] = indexes.get(i);
        }
        nonEdibleGhostIndex = new int[nonEdibleIndexes.size()];
        for(int i = 0; i < nonEdibleGhostIndex.length; i++){
            nonEdibleGhostIndex[i] = nonEdibleIndexes.get(i);
        }
    }

    /**
     * this method first use cache, if not found, compute the cache
     * @param junctionData the junctionData that have path to compute path statics
     * @return a pathStatics computed from the path
     */
    public PathStatics calculate(JunctionData junctionData){
        PathStatics cache = map.get(junctionData);
        // if can fetch from cache, directly fetch from cache
        if(cache != null) return cache;
        // else compute the result and store in cache
        cache = calculateWithOutCache(junctionData);
        map.put(junctionData, cache);
        return cache;
    }

    private PathStatics calculateWithOutCache(JunctionData junctionData){
        int distance = junctionData.path.length;
        int pillsOnRoad = 0;
        int powerPillsOnRoad = 0;
        int edibleGhostsOnRoad = 0;
        boolean hasNonedibleGhostOnRoad = false;
        Node[] nodes = game.getCurrentMaze().graph;
        for(int nodeIndex : junctionData.path){
            Node node = nodes[nodeIndex];
            if(node.pillIndex >= 0 && game.isPillStillAvailable(node.pillIndex)) pillsOnRoad++;
            if(node.powerPillIndex >= 0 && game.isPowerPillStillAvailable(node.powerPillIndex)) powerPillsOnRoad++;
            for(int edibleIndex : edibleGhostIndex){
                if(edibleIndex == nodeIndex) edibleGhostsOnRoad++;
            }
            for(int nonEdibleIndex : nonEdibleGhostIndex){
                if(nonEdibleIndex == nodeIndex) hasNonedibleGhostOnRoad = true;
            }
        }
        return new PathStatics(distance, pillsOnRoad, powerPillsOnRoad, edibleGhostsOnRoad, hasNonedibleGhostOnRoad);
    }
}
