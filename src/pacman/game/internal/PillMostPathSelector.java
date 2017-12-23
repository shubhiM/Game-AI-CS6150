package pacman.game.internal;

import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;

import java.awt.*;
import java.util.ArrayList;

public class PillMostPathSelector implements PathSelector {
    private Game game;
    private int nearestPill;

    @Override
    public PathTree select(ArrayList<PathTree> list) {
        PathTree selected = list.get(0);
        PathStatics selectStatic = DTPacman.getTotalStatics(selected);

        for (PathTree item : list) {
            PathStatics toCompare = DTPacman.getTotalStatics(item);
            int compareMark = (toCompare.powerPillsOnRoad + toCompare.pillsOnRoad) + toCompare.edibleGhostOnRoad * 10;
            int selectedMark = (selectStatic.powerPillsOnRoad + selectStatic.pillsOnRoad) + selectStatic.edibleGhostOnRoad * 10;

            if (compareMark > selectedMark) {
                selected = item;
                selectStatic = toCompare;
            }else if(compareMark == selectedMark){
                int toCompareIndex = item.junctionData.nodeID;
                int selectedIndex = selected.junctionData.nodeID;
                int toCompareDistance = game.getShortestPathDistance(toCompareIndex, nearestPill);
                int selectedDistance = game.getShortestPathDistance(selectedIndex, nearestPill);

                if(toCompareDistance < selectedDistance){
                    if(PathFinder.ENABLE_DEBUG_PAINT) {
                        GameView.addPoints(game, Color.YELLOW, nearestPill);
                    }
                    selected = item;
                    selectStatic = toCompare;
                }
            }
        }
        return selected;
    }

    @Override
    public void setGame(Game game) {
        if(game == null) return;
        this.game = game;
        int[] pills = game.getActivePillsIndices();
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        nearestPill = game.getClosestNodeIndexFromNodeIndex(pacmanIndex, pills, Constants.DM.PATH);
    }
}
