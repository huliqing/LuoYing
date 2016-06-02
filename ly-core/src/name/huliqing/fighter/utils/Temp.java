/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class Temp {
    private static final int STACK_SIZE = 5;
    
    private static class TempStack {
        int index = 0;
        Temp[] tempVars = new Temp[STACK_SIZE];
    }
    
    private static final ThreadLocal<TempStack> varsLocal = new ThreadLocal<TempStack>() {

        @Override
        public TempStack initialValue() {
            return new TempStack();
        }
    };
    
    private boolean isUsed = false;
    
    private Temp() {}
    
    public static Temp get() {
        TempStack stack = varsLocal.get();

        Temp instance = stack.tempVars[stack.index];

        if (instance == null) {
            // Create new
            instance = new Temp();

            // Put it in there
            stack.tempVars[stack.index] = instance;
        }

        stack.index++;

        instance.isUsed = true;

        return instance;
    }
    
    public void release() {
        if (!isUsed) {
            throw new IllegalStateException("This instance of Temp was already released!");
        }

        isUsed = false;

        TempStack stack = varsLocal.get();

        // Return it to the stack
        stack.index--;

        // Check if it is actually there
        if (stack.tempVars[stack.index] != this) {
            throw new IllegalStateException("An instance of Temp has not been released in a called method!");
        }
    }
    
    public final Vector3f vec1 = new Vector3f();
    public final CollisionResults results = new CollisionResults();
    public final Ray ray = new Ray(Vector3f.ZERO.clone(), Vector3f.UNIT_Z.clone());
    public final int[] array2 = new int[2];
    public final int[] array3 = new int[3];
    public final List list1 = new ArrayList();
}
