package pacman.game.internal;

import pacman.game.Game;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSNodeSelector implements NodeSelector {
    private ScriptEngine engine;
    private Invocable invocable;

    public JSNodeSelector(String fileExecutor, String fileTree){
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        invocable = (Invocable)engine;
        try {
            engine.eval("load(\"" + fileExecutor + "\")");
            engine.eval("load(\"" + fileTree + "\")");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TreeAction selectBranch(JunctionData junction, PathStatics newStatics, Game game, JunctionData fromJunction) {
        try {
            JunctionVisitor junctionVisitor = new JunctionVisitor(junction);
            JunctionVisitor fromVisitor = new JunctionVisitor(fromJunction);
            Object result = invocable.invokeFunction("execute", junctionVisitor, newStatics, game, fromVisitor);
            return (TreeAction)result;
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
