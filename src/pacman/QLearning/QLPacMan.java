package pacman.QLearning;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static pacman.game.Constants.Q_ACTION_RANDOMNESS;

/**
 * Created by shubhimittal on 4/3/17.
 */
public class QLPacMan extends Controller<MOVE> {

    @Override
    public MOVE getMove(Game game, long timeDue) {

        // Q store will load the q values for all possible
        // state-action pairs
        QStore qStore = new QStore(game);

        int pacManIndex = game.getPacmanCurrentNodeIndex();

        Node currentState = game.getCurrentMaze().graph[pacManIndex];

        // searches all possible actions from the current state and then selectes
        // the state-action pair with maximum q value from the store

        Random rnd = new Random();
        Map.Entry<String, Double> stateActionTuple = null;


        double chosenRandom = rnd.nextDouble();
        System.out.println("random number for state-action selection: " + chosenRandom);

        if(chosenRandom < Q_ACTION_RANDOMNESS) {
            stateActionTuple = qStore.getRandomQvalue(currentState);
        } else {
            stateActionTuple = qStore.getMaxQValue(currentState);
        }

        // stateActionTuple will contain 0,LEFT as key and 0.0 as value (default value)
        // 0 index will have the node index
        // 1 index will have the MOVE
        String[] stateAction = stateActionTuple.getKey().split(",");


        //System.out.println(stateAction.toString());

        MOVE nextMove = MOVE.valueOf(stateAction[1]);
        return nextMove;

    }
}
