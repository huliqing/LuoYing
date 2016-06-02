/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

/**
 *
 * @author huliqing
 */
public abstract class AbstractLogicObject implements PlayObject {
    
    protected boolean initialized = false;
    protected boolean enabled = true;

    @Override
    public void initialize() {
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
