package pacman.QLearning;

import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Maze;
import pacman.game.internal.Node;
import java.io.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static pacman.game.Constants.pathQLearning;

/**
 * Created by shubhimittal on 4/2/17.
 */

public class QStore {

    private Map<String, Double> store = new HashMap<>();
    private Maze currentMaze;
    private boolean firstTimeLoad = false;
    private final Double initialQValue = 0.0;

    private String generateKey(Node state, MOVE move) {
        return Integer.toString(state.nodeIndex) + ',' + move.toString();
    }

    // initializes the QStore hashMap with QValue set to default for each state-action pair
    // creates a file with name "qa.txt" for maze a in the data/qL directory and writes the
    // QStore data in the file for next run.
    // File system is used as permanent storage in the code.
    private void initializeQStore() {
        try {
            Node[] nodes = this.currentMaze.graph;
            String fileName = "q" + this.currentMaze.name ;
            FileWriter fw = new FileWriter(
                    pathQLearning + System.getProperty("file.separator") + fileName + ".txt");
            for(Node n : nodes) {

                // Only possible moves from the current node are added in the QStore, others are ignored
                for(MOVE m: n.neighbourhood.keySet()) {
                    String key = generateKey(n, m);
                    this.store.put(key, initialQValue);
                    fw.write(key + ":" + Double.toString(initialQValue) + "\n");
                }
            }
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    // if the QStore results file is present from the previous runs then this will use that
    // file to create the QStore hashMap
    private void restoreQStore(String fileName) {
        try
        {
            FileReader fr = new FileReader(
                    pathQLearning + System.getProperty("file.separator") + fileName + ".txt");

            BufferedReader br = new BufferedReader(fr);
            String input = br.readLine();
            while(input!=null) {
                String[] stateActionPair = input.split(":");
                String key = stateActionPair[0];
                String value = stateActionPair[1];
                this.store.put(key, Double.valueOf(value));
                input = br.readLine();
            }

            br.close();
        }
        catch(IOException ioe)
        {

            this.firstTimeLoad = true;
        }
    }

    public QStore(Game game) {

        this.currentMaze = game.getCurrentMaze();
        String mazeName = currentMaze.name;

        // if results from the previous run exists then it loads those
        // results in the Qstore
        // else it sets the firstTimeLoad flag to true and program starts
        // with 0 q values for all state-action pairs
        restoreQStore("q"+ mazeName);

        if (this.firstTimeLoad) {
            initializeQStore();
        }
    }

    public Double getQValue(Node state, MOVE move) {
        return store.get(generateKey(state, move));
    }

    Map<String, Double> getPossibleTuples(Node state) {

        Map<String, Double> tuples = new HashMap<>();

        for (MOVE move: MOVE.values()) {
            String key = generateKey(state, move);

            if(store.containsKey(key)) {
                tuples.put(key, store.get(key));
            }
        }

        return tuples;
    }

    public Map.Entry<String, Double> getMaxQValue(Node state) {


        Map<String, Double> tuples = getPossibleTuples(state);

        Object keys[] = tuples.keySet().toArray();
        String selectedKey = (String) keys[0];

        Double maxQValue = tuples.get(selectedKey);

        for(String key: tuples.keySet()) {
            if(tuples.get(key) > maxQValue) {
                selectedKey = key;
                maxQValue = tuples.get(key);
            }
        }
        return new AbstractMap.SimpleEntry<>(selectedKey, maxQValue);
    }


    // setsQvalue in the Qstore
    // updates the Qvalue in permanent storage for the given state-action pair
    public void setQValue(Node state, MOVE move, Double value) {
        store.put(generateKey(state, move), value);
        String fileName = "q" + this.currentMaze.name ;
        try {

            String s = "";
            FileReader fr = new FileReader(
                    pathQLearning + System.getProperty("file.separator") + fileName + ".txt");

            BufferedReader br = new BufferedReader(fr);
            String input = br.readLine();

            String keyToFind = generateKey(state, move);

            while(input!=null) {
                String[] stateActionPair = input.split(":");
                String key = stateActionPair[0];

                if(key.equals(keyToFind)) {
                    s = s + key + ":" + value + "\n";
                } else {
                    s = s + input + "\n";
                }
                String value1 = stateActionPair[1];


                this.store.put(key, Double.valueOf(value1));
                input = br.readLine();
            }

            br.close();
            FileWriter fw = new FileWriter(
                    pathQLearning + System.getProperty("file.separator") + fileName + ".txt");
            fw.write(s);
            fw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public Map.Entry<String, Double> getRandomQvalue(Node state) {

        Map<String, Double> tuples =  getPossibleTuples(state);

        int numberRange  = tuples.keySet().size();

        Random rand = new Random();

        // Randomly choosing any of the possible tuples
        // generating random index
        int randomNum = rand.nextInt(numberRange);

        System.out.println("random number for tuple size: " + randomNum);

        String selectedKey = (String) tuples.keySet().toArray()[randomNum];

        return new AbstractMap.SimpleEntry<>(selectedKey, tuples.get(selectedKey));
    }
}


// Test scenarios:
// First time load should create a file with state,action:Qvalue.
// sample for first time load is
// 0,LEFT:0.0
// 1,LEFT:0.0
// 2,LEFT:0.0
// 3,LEFT:0.0
// 3,RIGHT:0.0
//


// Unit Tests for the code
// Test cases:
// Testcase1:
// qa.txt not exist - firsttimeLoad = true
// qb.txt not exist - firsttimeLoad = true
// qc.txt not exist - firstTimeLoad = true
// qd.txt not exist - firstTimeLoad = true

// Number of lines in qa.txt = number of possible state-action pairs for a Maze

// qa.txt exists then firstTimeLoad = false, existing values should load

// get Q value should get the q value for the given key

// get possible tuples should return all possible state-action pairs for current state

// get max Q value should return state action will max Qvalue

// Set Q value should set the given Q value at the given state-action pair
// Set Q value should update the new Q value for the given state-action pair in the permanent storage as well


// check if the logic to fetch previous state is good - yes
// check if the logic to update the Q store initially with neighbourhood is right, (are all state-action pairs being
// captured)
// check if the logic to update Q values through Q learning algorithm is good
// check if the action with max value from the current state is getting chosen

