/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object;

import com.jme3.app.Application;

/**
 *
 * @author huliqing
 */
public abstract class AbstractPlayObject implements PlayObject {
    
    protected boolean initialized = false;

    @Override
    public void initialize(Application app) {
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
}
