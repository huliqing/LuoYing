/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

/**
 *
 * @author huliqing
 */
public abstract class AbstractKeyMapping implements KeyMapping {

    protected boolean initialized;
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("KeyMapping already initialized, KeyMapping=" + this);
        }
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
