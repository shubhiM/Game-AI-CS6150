package pacman.DecisionTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubhimittal on 4/18/17.
 */
public class DTCreator {


    private List<String> examples;
    private List<String> attributes;
    private Node node;

    public DTCreator(List<String> examples, List<String> attributes, Node node) {
        this.examples = examples;
        this.attributes = attributes;
        this.node = node;
    }


    public double getInitialEntrop(){
        return 0L;
    }


    public List<String> splitByAttribute() {

        return new ArrayList<>();
    }

    public double getOverallEntropy() {
        return 0L:
    }
}
