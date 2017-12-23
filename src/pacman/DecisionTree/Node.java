package pacman.DecisionTree;

/**
 * Created by shubhimittal on 4/12/17.
 */

// The node of decision tree

interface  Node {

	// The node can be either Condition or Action
	
    boolean isCondition();

    boolean isAction();

    Condition asCondition();

    Action asAction();

    void execute();

}
