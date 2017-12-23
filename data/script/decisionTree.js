function decisionTree(junction, newStatics, game, fromJunction){
    // this is the one branch tree, in following shape
    // _/__/__/__/___

    var endNode = junction.nodeID;
    var TreeAction = Java.type("pacman.game.internal.TreeAction");
    var PathFinder = Java.type("pacman.game.internal.PathFinder");
    return [
        {
            condition: function () {  return junction.nodeID == fromJunction.nodeStartedFrom; },
            action: TreeAction.reject
        },
        {
            condition: function () { return junction.firstMove == fromJunction.lastMove.opposite(); },
            action: TreeAction.reject
        },
        {
            condition: function () {
                var minGhostDistance = PathFinder.getMinGhostDistance(game, endNode);
                if(newStatics.powerPillsOnRoad > 0) return false;
                return minGhostDistance < newStatics.distance;
            },
            action: TreeAction.reject
        },
        {
            condition: function() { return newStatics.hasNonEdibleGhostOnRoad; },
            action: TreeAction.reject
        },
        {
            condition: function () { return newStatics.distance >= PathFinder.MAX_LENGTH },
            action: TreeAction.selectWithoutRecur
        },
        {
            condition: function () { return true; },
            action: TreeAction.selectWithRecur
        }
    ];
}
