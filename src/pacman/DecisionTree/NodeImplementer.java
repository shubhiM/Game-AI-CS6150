package pacman.DecisionTree;

/**
 * Created by shubhimittal on 4/12/17.
 */

abstract class  NodeImplementer implements Node {

	// Return true iff this is a Condition or Action
	
    public boolean isCondition() {
        return (this instanceof Condition);
    }

    public boolean isAction() {
        return (this instanceof Action);
    }

    public Condition asCondition() {
        return (Condition) this;
    }

    public Action asAction() {
        return (Action) this;
    }
}
