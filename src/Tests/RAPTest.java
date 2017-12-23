package Tests;

import org.junit.*;
import org.junit.Assert;
import org.omg.PortableInterceptor.INACTIVE;
import pacman.RAP.API;
import pacman.RAP.RAPPacMan;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.*;

import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

import static pacman.game.Constants.DELAY;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by shubhimittal on 3/15/17.
 */
public class RAPTest {

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private RAPPacMan pacmanController1;
    private RAPPacMan pacmanController2;
    private RAPPacMan pacmanController3;
    private RAPPacMan pacmanController4;
    private MOVE nextMove1;
    private MOVE nextMove2;
    private MOVE nextMove3;
    private MOVE nextMove4;
    private Integer pacmanIndex1;
    private Integer pacmanIndex2;
    private Integer pacmanIndex3;
    private Integer pacmanIndex4;


    private static final Integer proximity = 10;

    @Before
    public void initializeTestEnvironment() {

        // instantiating different game states for testing

        this.game1 = new Game(0);
        this.game2 = new Game(1);
        this.game3 = new Game(2);
        this.game4 = new Game(3);


        this.pacmanController1 = new RAPPacMan();
        this.pacmanController2 = new RAPPacMan();
        this.pacmanController3 = new RAPPacMan();
        this.pacmanController4 = new RAPPacMan();

        this.pacmanIndex1 = game1.getPacmanCurrentNodeIndex();
        this.pacmanIndex2 = game2.getPacmanCurrentNodeIndex();
        this.pacmanIndex3 = game3.getPacmanCurrentNodeIndex();
        this.pacmanIndex4 = game4.getPacmanCurrentNodeIndex();


        // start thread and update controller with game state
        new Thread(pacmanController1).start();
        new Thread(pacmanController2).start();
        new Thread(pacmanController3).start();
        new Thread(pacmanController4).start();

        pacmanController1.update(game1, System.currentTimeMillis() + DELAY);
        pacmanController2.update(game2, System.currentTimeMillis() + DELAY);
        pacmanController3.update(game3, System.currentTimeMillis() + DELAY);
        pacmanController4.update(game4, System.currentTimeMillis() + DELAY);

        this.nextMove1 = pacmanController1.getMove();
        this.nextMove2 = pacmanController2.getMove();
        this.nextMove3 = pacmanController3.getMove();
        this.nextMove4 = pacmanController4.getMove();


    }


    public List<GHOST> getedibleGhosts(Game game) {
        List<GHOST> ghosts = new ArrayList<GHOST>();
        for(GHOST g: GHOST.values()) {
            if(game.getGhostEdibleTime(g) > 0) {
                ghosts.add(g);
            }
        }
        return ghosts;

    }
    public List<MOVE> getMovesToGhosts(Game game, int pacmanIndex) {
        List<MOVE> moves = new ArrayList<MOVE>();

        // pacMan should move towards ghost
        if(game.wasPowerPillEaten() || (getedibleGhosts(game).size() > 0)) {
            for(GHOST g: GHOST.values()) {
                moves.add(game.getNextMoveTowardsTarget(
                        pacmanIndex, game.getGhostCurrentNodeIndex(g), DM.PATH));
            }
        }
        return moves;
    }

    public List<GHOST> getNonEdibleGhosts(Game game) {

        List<GHOST> ghosts = new ArrayList<>();
        for(GHOST g: GHOST.values()) {
            if(game.getGhostEdibleTime(g) == 0) {
                ghosts.add(g);
            }
        }
        return ghosts;
    }

    public List<MOVE> getMovesAwayFromGhosts(Game game, int pacmanIndex) {
        List<MOVE> moves = new ArrayList<MOVE>();

        // pacMan should move towards ghost
        if(getNonEdibleGhosts(game).size() > 0) {
            for(GHOST g: GHOST.values()) {
                moves.add(game.getNextMoveAwayFromTarget(
                        pacmanIndex,
                        game.getGhostCurrentNodeIndex(g), DM.PATH));
            }
        }
        return moves;
    }


    @Test
    public void moveTowardsAnyGhost() {

        // if a pill was eaten then pacman should move towards any of the Ghost

        List<MOVE> moves1 = getMovesToGhosts(game1, pacmanIndex1);
        List<MOVE> moves2 = getMovesToGhosts(game2, pacmanIndex2);
        List<MOVE> moves3 = getMovesToGhosts(game3, pacmanIndex3);
        List<MOVE> moves4 = getMovesToGhosts(game4, pacmanIndex4);

        if(moves1.size() > 0 && nextMove1 != null) {
            assertThat(moves1, hasItems(this.nextMove1));
        }
        if(moves2.size() > 0 && nextMove2 != null) {
            assertThat(moves2, hasItems(this.nextMove2));
        }
        if(moves3.size() > 0 && nextMove3 != null) {
            assertThat(moves3, hasItems(this.nextMove3));
        }
        if(moves4.size() > 0 && nextMove4 != null) {
            assertThat(moves4, hasItems(this.nextMove4));
        }
    }



    @Test
    public void moveAwayFromNearestGhost() {
        // if there are non edible ghosts in proximity then pacman should move
        // away from the nearest non-edible ghost

        List<MOVE> moves1 = getMovesAwayFromGhosts(game1, pacmanIndex1);
        List<MOVE> moves2 = getMovesAwayFromGhosts(game2, pacmanIndex2);
        List<MOVE> moves3 = getMovesAwayFromGhosts(game3, pacmanIndex3);
        List<MOVE> moves4 = getMovesAwayFromGhosts(game4, pacmanIndex4);

        System.out.println(nextMove1);
        System.out.println(nextMove2);
        System.out.println(nextMove3);
        System.out.println(nextMove4);



        if(moves1.size() > 0 && nextMove1 != null) {
            assertThat(moves1, hasItems(nextMove1));
        }
        if(moves2.size() > 0 && nextMove2 != null) {
            assertThat(moves2, hasItems(nextMove2));
        }
        if(moves3.size() > 0 && nextMove3 != null) {
            assertThat(moves3, hasItems(nextMove3));
        }
        if(moves4.size() > 0 && nextMove4 != null) {
            assertThat(moves4, hasItems(nextMove4));
        }

    }

    @After
    public void destroyEnvironment() {
        pacmanController1.terminate();
        pacmanController2.terminate();
        pacmanController3.terminate();
        pacmanController4.terminate();

    }

}
