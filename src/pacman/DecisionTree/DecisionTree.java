package pacman.DecisionTree;


/**
 * Created by shubhimittal on 4/12/17.
 */

public class DecisionTree {

    private Node node;
    private DecisionTree left;
    private DecisionTree right;
    private Action actionExecuted;

    // Constructor for DecisionTree

    DecisionTree(Node node , DecisionTree left, DecisionTree right ) {

        this.node = node;
        this.left = left;
        this.right = right;
    }

    // Getter
    
    public Action getActionExecuted() {
        return actionExecuted;
    }

    public void executeDecisionTree() {

        Node startNode = node;

        // if startNode is Condition node
        
        while(startNode.isCondition()) {
              startNode.execute();

              Condition cond = startNode.asCondition();

            if(cond.getIsTestSuccessful()) {
                startNode = cond.getTrueValue();
             } else {
                startNode = cond.getFalseValue();
             }
        }
        
        // startNode becoming Action node means this node is the leaf node
        actionExecuted = startNode.asAction();
        actionExecuted.execute();
    }
}