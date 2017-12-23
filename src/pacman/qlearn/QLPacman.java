package pacman.qlearn;

import pacman.game.internal.DTPacman;
import pacman.game.util.IO;

import java.util.ArrayList;

public class QLPacman extends DTPacman {
    private float[] qValues;

    public QLPacman(){
        super();
        qValues = strToFloatArray(IO.loadFile("result.txt"));
        selector = new QLSelector(qValues);
    }

    public float[] getQValues(){
        return qValues;
    }

    public static float[] strToFloatArray(String str){
        String[] strs = str.split(" ");
        float[] result = new float[strs.length];
        for(int i = 0; i < result.length; i++){
            result[i] = Float.parseFloat(strs[i]);
        }
        return result;
    }

    public static String floatArrayToStr(float[] floats){
        ArrayList<String> arrayList = new ArrayList<>(floats.length);
        for(int i = 0; i < floats.length; i++){
            arrayList.add(String.valueOf(floats[i]));
        }
        return String.join(" ", arrayList);
    }
}
