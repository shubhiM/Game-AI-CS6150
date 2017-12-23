package pacman.RAP;


import Tests.RAPTest;
import pacman.controllers.Controller;

import pacman.game.Constants.*;
import pacman.game.Game;
import pacman.RAP.API;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by shubhimittal on 3/11/17.
 */
public class RAPPacMan  extends Controller<MOVE> {
    private final Invocable invocable;


    public RAPPacMan() {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        invocable = (Invocable) engine;
        try {

//            engine.eval(new FileReader("data/script/RAPDataStructure.js"));
//            engine.eval(new FileReader("data/script/RAPExecutor.js"));

            String rapStructureFile = "data/script/RAPDataStructure.js";
            String rapExecutorFile = "data/script/RAPExecutor.js";

            engine.eval("load(\"" + rapStructureFile + "\")");
            engine.eval("load(\"" + rapExecutorFile + "\")");

        } catch (ScriptException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    public MOVE runRapExecutor(Game game) {
        try {
            // print the RAP tree initially
            invocable.invokeFunction("printInitialRAP", game, new API(game));
            Object result = invocable.invokeFunction("rapExecutor", game, new API(game));
            return (MOVE) result;

        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            System.out.println("rap failed");
            throw new RuntimeException(e);
        }
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {

        return runRapExecutor(game);
    }


}

