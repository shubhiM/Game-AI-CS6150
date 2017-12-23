package pacman.qlearn;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.util.IO;

import java.util.EnumMap;
import java.util.Random;

import static pacman.game.Constants.DELAY;

public class Trainer {
    public static float testLevels(Controller<Constants.MOVE> pacManController, Controller<EnumMap<Constants.GHOST,Constants.MOVE>> ghostController, int trials){
        double avgScore=0;
        int avgLevel=0;

        Random rnd=new Random(0);
        Game game;

        for(int i=0;i<trials;i++)
        {
            game=new Game(rnd.nextLong());

            while(!game.gameOver())
            {
                game.advanceGame(pacManController.getMove(game.copy(),System.currentTimeMillis()+DELAY),
                        ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));
            }

            avgScore+=game.getScore();
            avgLevel+=game.getCurrentLevel();
            // System.out.println(i+"\t"+game.getScore());
        }

//        return (int)avgScore / trials;
        return avgLevel /trials;
    }

    public static void main(String[] args){
        QLPacman qlPacman = new QLPacman();
        for(int i = 0; i < 10; i++){
            qLearning(qlPacman);
        }
    }

    public static void qLearning(QLPacman qlPacman){
        Random random = new Random();
        int index = random.nextInt(4);
        float[] qValues = qlPacman.getQValues();
        float score = qValues[4];
        int mutation = random.nextInt(10) - 5;
        qValues[index] += mutation;

        float newScore = testLevels(qlPacman, new StarterGhosts(), 4);
        if(newScore > score + 1){
            IO.saveFile("result.txt", QLPacman.floatArrayToStr(qValues), false);
        }
    }
}
