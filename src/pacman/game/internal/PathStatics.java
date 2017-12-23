package pacman.game.internal;

/**
 * PathStatics store the information of a path from start to current end
 * store the cost from pacman to the current place, pills on the road, power pills on the road
 * and also the edible ghost on the road and so on
 */
public class PathStatics {
    public final int distance;          // total distance from pacman
    public final int pillsOnRoad;       // pillsOnRoad for this path
    public final int powerPillsOnRoad;  // the power pills on road from pacman to this path
    public final int edibleGhostOnRoad; // the edibleGhost on road
    public final boolean hasNonEdibleGhostOnRoad;   // the danger ghost on the path

    public PathStatics(int distance, int pillsOnRoad, int powerPillsOnRoad, int edibleGhostOnRoad, boolean hasNonEdibleGhostOnRoad){
        this.distance = distance;
        this.pillsOnRoad = pillsOnRoad;
        this.powerPillsOnRoad = powerPillsOnRoad;
        this.edibleGhostOnRoad = edibleGhostOnRoad;
        this.hasNonEdibleGhostOnRoad = hasNonEdibleGhostOnRoad;
    }

    /**
     * like 1 + 1 = 2, or (1, 3) + (2, 4) = (3, 7), add every fields and return a new PathStatics
     * @param other the other PathStatics
     * @return a new PathStatics instance with all the fields values sum of the previous two PathStatics.
     */
    public PathStatics add(PathStatics other){
        return new PathStatics(
                this.distance + other.distance,
                this.pillsOnRoad + other.pillsOnRoad,
                this.powerPillsOnRoad + other.powerPillsOnRoad,
                this.edibleGhostOnRoad + other.edibleGhostOnRoad,
                this.hasNonEdibleGhostOnRoad || other.hasNonEdibleGhostOnRoad
        );
    }
}
