package pacman.game.internal;

import pacman.game.Game;

import java.util.ArrayList;

public class NearestJunctionMapper {
    /**
     * the pathsCache to build junctionMapper
     */
    public final PathsCache pathsCache;
    /**
     * the index of maze for this NearestJunctionMapper
     */
    public final int mazeIndex;
    /**
     * the internal representation of mapper that map from the index of junctions in pathsCache
     * to a array of path to nearby junctions
     */
    public final JunctionData[][] mapper;
    // to array of path to nearby junctions

    public NearestJunctionMapper(PathsCache pathsCache, int mazeIndex){
        this.pathsCache = pathsCache;
        this.mazeIndex = mazeIndex;
        mapper = new JunctionData[pathsCache.junctions.length][];
        initialMapper();
    }

    /**
     * @param junctionIndex, the index of target node in pathsCache.junctions
     * @return the array of paths to nearby junctions, the path is in node index
     */
    public JunctionData[] getPathsFromJunctionIndex(int junctionIndex){
        return mapper[junctionIndex];
    }

    /**
     * @param nodeIndex the index of node in maze
     * @return the array to paths to nearby junctions, the path is in node index
     */
    public JunctionData[] getPathsFromNodeIndex(int nodeIndex){
        return getPathsFromJunctionIndex(pathsCache.junctionIndexConverter.get(nodeIndex));
    }

    /*
        init the initial value for mapper array, compute every path to nearby junction
     */
    private void initialMapper(){
        Game game = pathsCache.game;
        ArrayList<JunctionData> preResult = new ArrayList<>(5);
        for(int i = 0; i < pathsCache.junctions.length; i++){
            Junction junction = pathsCache.junctions[i];
            preResult.clear();
            int[] neibourIndex = game.getNeighbouringNodes(junction.nodeId);
            for(int index : neibourIndex){
                DNode dNode = pathsCache.nodes[index];
                JunctionData first = null;
                JunctionData second = null;
                for(JunctionData junctionData : dNode.closestJunctions){
                    if(junctionData.nodeID != junction.nodeId){
                        first = junctionData;
                    }else{
                        second = junctionData;
                    }
                }

                //create the new junction with first and second data
                int[] combinePath = new int[first.path.length + second.reversePath.length];
                System.arraycopy(second.reversePath, 0, combinePath, 0, second.reversePath.length);
                System.arraycopy(first.path, 0, combinePath, second.reversePath.length, first.path.length);
                JunctionData combined = new JunctionData(first.nodeID, first.firstMove, junction.nodeId, combinePath, first.lastMove);
                preResult.add(combined);
            }
            mapper[i] = new JunctionData[preResult.size()];
            preResult.toArray(mapper[i]);
        }
    }
}
