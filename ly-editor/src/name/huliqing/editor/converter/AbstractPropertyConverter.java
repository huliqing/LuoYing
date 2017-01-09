/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractPropertyConverter<T extends ObjectData> implements PropertyConverter<T>{

    private static final Logger LOG = Logger.getLogger(AbstractPropertyConverter.class.getName());

    protected DataConverter<T> parent;
    protected String property; 
    
    protected final TitledPane root = new TitledPane();
    protected boolean initialized;

    @Override
    public Node getNode() {
        return root;
    }
    
    @Override
    public void initialize(DataConverter<T> parent, String property) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        this.parent = parent;
        this.property = property;
        root.setText(property);
        root.setAnimated(false);
        
        // children
    }

    @Override
    public void notifyChangedToParent() {
        LOG.log(Level.INFO, "PropertyConverter notify changed, PropertyConverter={0}, property={1}"
                , new Object[] {getClass(), property});
        if (parent != null) {
            parent.notifyChangedToParent();
        }
    }
    
}
