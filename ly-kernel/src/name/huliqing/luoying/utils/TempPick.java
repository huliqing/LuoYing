/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.utils;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;

/**
 *
 * @author huliqing
 */
public class TempPick {
    private static final int STACK_SIZE = 5;
    
    private static class TempStack {
        int index = 0;
        TempPick[] tempVars = new TempPick[STACK_SIZE];
    }
    
    private static final ThreadLocal<TempStack> varsLocal = new ThreadLocal<TempStack>() {

        @Override
        public TempStack initialValue() {
            return new TempStack();
        }
    };
    
    private boolean isUsed = false;
    
    private TempPick() {}
    
    public static TempPick get() {
        TempStack stack = varsLocal.get();

        TempPick instance = stack.tempVars[stack.index];

        if (instance == null) {
            // Create new
            instance = new TempPick();

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
    
    public final CollisionResults results = new CollisionResults();
    public final Ray ray = new Ray(Vector3f.ZERO.clone(), Vector3f.UNIT_Z.clone());
}
