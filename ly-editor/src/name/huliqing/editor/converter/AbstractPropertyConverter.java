/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class AbstractPropertyConverter<E extends JfxAbstractEdit, T extends ObjectData> 
        implements PropertyConverter<E, T>{

    private static final Logger LOG = Logger.getLogger(AbstractPropertyConverter.class.getName());
    
    protected E jfxEdit;
    protected DataConverter<E, T> parent;
    protected String property; 
    
    protected final TitledPane root = new TitledPane();
    protected boolean initialized;

    @Override
    public Node getLayout() {
        return root;
    }
    
    @Override
    public void initialize(E editView, DataConverter<E, T> parent, String property) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        this.jfxEdit = editView;
        this.parent = parent;
        this.property = property;
        root.setText(property);
        root.setAnimated(false);
        root.setAlignment(Pos.CENTER_LEFT);
        // Child logics
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
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
