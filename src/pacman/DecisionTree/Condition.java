package pacman.DecisionTree;

import java.util.function.Supplier;

/**
 * Created by shubhimittal on 4/12/17.
 */

// Condition Node class

class Condition extends NodeImplementer {

    private Supplier<Boolean> test;

    private Node trueValue;

    private Node falseValue;

    private boolean isTestSuccessful;

    // Constructor for Condition
    
    Condition (Supplier<Boolean> s, Node t, Node f) {
        this.test = s;
        this.trueValue = t;
        this.falseValue = f;
    }

    // Getter
    
    public Node getTrueValue() {
        return this.trueValue;
    }

    public Node getFalseValue() {
        return this.falseValue;
    }

    public boolean getIsTestSuccessful() {
        return this.isTestSuccessful;
    }

    // Implementation of abstract method
    
    public void execute() {
        if (test.get()) {
        	// an object of Boolean class
        	
            this.isTestSuccessful = true;
            
        } else {
            this.isTestSuccessful = false;
        }
    }

}