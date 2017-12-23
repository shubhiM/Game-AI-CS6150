function execute(junction, newStatics, game, fromJunction){
    var tree = decisionTree(junction, newStatics, game, fromJunction);
    for(var i = 0; i < tree.length; i++){
        if(tree[i].condition() == true){
            return tree[i].action;
        }
    }
}
