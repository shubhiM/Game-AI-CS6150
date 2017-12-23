package pacman.qlearn;

import pacman.game.Game;
import pacman.game.internal.DTPacman;
import pacman.game.internal.PathSelector;
import pacman.game.internal.PathStatics;
import pacman.game.internal.PathTree;

import java.util.ArrayList;

public class QLSelector implements PathSelector {
    private float[] qQvalues;

    public QLSelector(float[] qQvalues){
        this.qQvalues = qQvalues;
    }

    @Override
    public PathTree select(ArrayList<PathTree> list) {
        PathTree selected = null;
        float evaluateValue = Integer.MIN_VALUE;
        for(PathTree pathTree : list){
            float newEvaluate = evaluateResult(DTPacman.getTotalStatics(pathTree));
            if(newEvaluate > evaluateValue){
                selected = pathTree;
                evaluateValue = newEvaluate;
            }
        }
        return selected;
    }

    @Override
    public void setGame(Game game) {
    }

    public float evaluateResult(PathStatics p){
        int enemyGhostOnRoad = p.hasNonEdibleGhostOnRoad ? 1 : 0;
        return (
                p.pillsOnRoad * qQvalues[0]
                + p.powerPillsOnRoad * qQvalues[1]
                + p.edibleGhostOnRoad * qQvalues[2]
                + enemyGhostOnRoad * qQvalues[3]
        );
    }
}
