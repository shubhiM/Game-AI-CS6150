package pacman.DecisionTree;

import java.util.function.Supplier;

/**
 * Created by shubhimittal on 4/12/17.
 */

// Action Node class

public class Action extends NodeImplementer {

    private boolean executed;
    private Object result;

    private Supplier<Object> action;

    
    // Constructor for Action
    
    Action(Supplier<Object> s) {
        this.action = s;
    }
    
    // Getter
    
    public boolean getExecuted() {
        return this.executed;
    }

    public Object getResult() {
        return this.result;
    }
    
    // Implementation of abstract method
    
    public void execute() {
    	
    	// create a new object of Object class and assign it to result
    	
        this.result = action.get();
        this.executed = true;
    }

}