package pacman.RAP;

import org.omg.CORBA.INTERNAL;
import pacman.game.Constants.*;
//import pacman.game.Constants.MOVE;

import pacman.game.Game;

import java.util.*;

/**
 * Created by shubhimittal on 2/27/17.
 */
public class API {

    private Game game;
    private int pacmanIndex;
    private static final int MIN_DISTANCE = 10;

    public API(Game game) {
        this.game = game;
        this.pacmanIndex = game.getPacmanCurrentNodeIndex();
    }

    private int[] convertToArrayFromArrayList (ArrayList<Integer> targets) {
        int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
        for(int i=0;i<targetsArray.length;i++)
            targetsArray[i]=targets.get(i);
        return  targetsArray;
    }



    private List<GHOST> edibleGhosts() {
        List<GHOST> edibleGhosts = new ArrayList<>();
        for (GHOST ghost : GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) > 0) {
                edibleGhosts.add(ghost);
            }
        }
        return edibleGhosts;
    }

    private List<GHOST> nonEdibleGhosts() {
        List<GHOST> nonEdibleGhosts = new ArrayList<>();
        for (GHOST ghost : GHOST.values()) {
            if ((game.getGhostEdibleTime(ghost) == 0) && (game.getGhostLairTime(ghost) == 0) &&
            (game.getShortestPathDistance(pacmanIndex, game.getGhostCurrentNodeIndex(ghost)) < MIN_DISTANCE)) {
                nonEdibleGhosts.add(ghost);
            }
        }
        return nonEdibleGhosts;
    }

    private ArrayList<Integer> nearestPowerPills() {
        int[] powerPills=game.getPowerPillIndices();
        ArrayList<Integer> availablePowerPills = new ArrayList<>();

        for(int i:powerPills) {
            if (game.isPowerPillStillAvailable(i)) {
                for (GHOST g: nonEdibleGhosts()) {
                    if (game.getShortestPathDistance(pacmanIndex, i) <
                            game.getShortestPathDistance(pacmanIndex, game.getGhostCurrentNodeIndex(g))) {
                        availablePowerPills.add(i);
                    }
                }
            }
        }
        return availablePowerPills;
    }


    public boolean nonEdibleGhostsInProximity() {
        return nonEdibleGhosts().size() > 0;
    }



    public boolean thereExistsPowerPillNearBy() {
        return nearestPowerPills().size() > 0;
    }

    public boolean thereExistsEdibleGhosts() {
        return edibleGhosts().size() > 0;
    }

    public MOVE moveToNearestEdibleGhost() {

        int maxDistance = Integer.MAX_VALUE;
        GHOST minGhost=null;
        for(GHOST g: edibleGhosts()) {
            int ghostDistance = game.getShortestPathDistance(pacmanIndex, game.getGhostCurrentNodeIndex(g));
            if (ghostDistance < maxDistance) {
                maxDistance = ghostDistance;
                minGhost = g;
            }
        }
        MOVE moveToGhost = game.getNextMoveTowardsTarget(
                pacmanIndex, game.getGhostCurrentNodeIndex(minGhost),DM.PATH);

        return game.getNextMoveTowardsTarget(
                pacmanIndex, game.getGhostCurrentNodeIndex(minGhost),DM.PATH);
    }

    public MOVE moveToNearestPill() {
        int[] pills=game.getPillIndices();
        ArrayList<Integer> targets=new ArrayList<Integer>();
        for(int i=0;i<pills.length;i++)
            if(game.isPillStillAvailable(i))
              targets.add(pills[i]);

        int pillIndex = game.getClosestNodeIndexFromNodeIndex(
                pacmanIndex,
                convertToArrayFromArrayList(targets),
                DM.PATH);

        MOVE move  = game.getNextMoveTowardsTarget(
                pacmanIndex,
                pillIndex,
                DM.PATH);

        return game.getNextMoveTowardsTarget(
                pacmanIndex,
                game.getClosestNodeIndexFromNodeIndex(
                        pacmanIndex,
                        convertToArrayFromArrayList(targets),
                        DM.PATH),
                DM.PATH);
    }


    public MOVE moveToNearestPowerPill() {
        return game.getNextMoveTowardsTarget(
                pacmanIndex,
                game.getClosestNodeIndexFromNodeIndex(
                        pacmanIndex,
                        convertToArrayFromArrayList(nearestPowerPills()),
                        DM.PATH),
                DM.PATH);
    }

    public MOVE moveAwayFromNonEdibleGhosts() {
        EnumMap<GHOST,MOVE> moves =new EnumMap<GHOST,MOVE>(GHOST.class);
        for (GHOST g: nonEdibleGhosts()) {
            moves.put(g, game.getNextMoveAwayFromTarget(
                    game.getPacmanCurrentNodeIndex(),
                    game.getGhostCurrentNodeIndex(g),
                    DM.PATH));
        }
        Random random = new Random();
        int index = random.nextInt(moves.size());
        MOVE move = moves.get(index);
        return move;
    }

    public MOVE moveAwayFromNearestGhost() {
        GHOST minGhost = null;
        int distance = Integer.MAX_VALUE;
        for (GHOST g: nonEdibleGhosts()) {
            int ghostDistance = game.getShortestPathDistance(pacmanIndex, game.getGhostCurrentNodeIndex(g));
            if(ghostDistance < distance) {
                distance = ghostDistance;
                 minGhost = g;
            }
        }
        return game.getNextMoveAwayFromTarget(
                pacmanIndex, game.getGhostCurrentNodeIndex(minGhost),DM.PATH);
    }

    public boolean pillsNotExists() {
        //Strategy 3: go after the pills and power pills
        int[] pills=game.getPillIndices();
        int[] powerPills=game.getPowerPillIndices();

        ArrayList<Integer> targets=new ArrayList<Integer>();

        for(int i=0;i<pills.length;i++)					//check which pills are available
            if(game.isPillStillAvailable(i))
                targets.add(pills[i]);

        for(int i=0;i<powerPills.length;i++)			//check with power pills are available
            if(game.isPowerPillStillAvailable(i))
                targets.add(powerPills[i]);

        int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array

        for(int i=0;i<targetsArray.length;i++)
            targetsArray[i]=targets.get(i);

        return targetsArray.length == 0;
    }
}

