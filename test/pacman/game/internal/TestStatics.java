package pacman.game.internal;

import org.junit.Assert;
import org.junit.Test;
import pacman.game.Game;

public class TestStatics {
    public static void assertStaticEqual(PathStatics expect, PathStatics actual){
        Assert.assertEquals(expect.distance, actual.distance);
        Assert.assertEquals(expect.pillsOnRoad, actual.pillsOnRoad);
        Assert.assertEquals(expect.powerPillsOnRoad, actual.powerPillsOnRoad);
        Assert.assertEquals(expect.edibleGhostOnRoad, actual.edibleGhostOnRoad);
        Assert.assertEquals(expect.hasNonEdibleGhostOnRoad, actual.hasNonEdibleGhostOnRoad);
    }

    @Test
    public void testStaticAdd(){
        PathStatics statics1 = new PathStatics(8, 6, 3, 1, false);
        PathStatics statics2 = new PathStatics(9, 7, 1, 2, true);
        PathStatics expect = new PathStatics(8 + 9, 6 + 7, 3 + 1, 1 + 2, false || true);
        PathStatics actual = statics1.add(statics2);
        assertStaticEqual(expect, actual);
    }

    @Test
    public void testJunctionMapper(){
        Game game = new Game(0);
        JunctionData[] nearbyJunctions = PathFinder.junctionMappers[0].getPathsFromNodeIndex(834);
        int a = nearbyJunctions.length + 1;
        Assert.assertEquals(3, nearbyJunctions.length);
        Assert.assertEquals(716, nearbyJunctions[0].nodeID);
        Assert.assertEquals(960, nearbyJunctions[1].nodeID);
        Assert.assertEquals(810, nearbyJunctions[2].nodeID);
    }

    @Test
    public void testPathCalculator(){
        Game game = new Game(0);
        JunctionData[] nearbyJunctions = PathFinder.junctionMappers[0].getPathsFromNodeIndex(984);
        JunctionData target = nearbyJunctions[0];
        PathStaticCalculator calculator = new PathStaticCalculator(game);
        PathStatics actual = calculator.calculate(target);
        PathStatics expected = new PathStatics(12, 3, 0, 0, false);
    }
}
