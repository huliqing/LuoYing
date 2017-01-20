/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Map;
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
    
    protected FeatureHelper featureHelper;
    protected boolean ignoreChangedEvent;
    
    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public void setFeatures(Map<String, Object> features) {
        this.featureHelper = new FeatureHelper(features);
    }

    @Override
    public void setEdit(E edit) {
        this.jfxEdit = edit;
    }

    @Override
    public Node getLayout() {
        return root;
    }

    @Override
    public void initialize(DataConverter<E, T> parent) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        this.parent = parent;
        root.setText(property);
        root.setAnimated(false);
        root.setAlignment(Pos.CENTER_LEFT);
        
        // features
        boolean disabled = featureHelper.getAsBoolean(PropertyConverter.FEATURE_DISABLED);
        boolean collapsed = featureHelper.getAsBoolean(PropertyConverter.FEATURE_COLLAPSED);
        
        // Layout and features
        Node layout = createLayout();
        layout.setDisable(disabled);
        root.setExpanded(!collapsed);
        root.setContent(layout);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        root.setContent(null);
        initialized = false;
    }
    
    @Override
    public void notifyChangedToParent() {
//        LOG.log(Level.INFO, "PropertyConverter notify changed, PropertyConverter={0}, property={1}"
//                , new Object[] {getClass(), property});
        if (parent != null) {
            parent.notifyChangedToParent();
        }
    }
    
    @Override
    public void updateAttribute(Object propertyValue) {
        if (ignoreChangedEvent) {
            return;
        }
        parent.getData().setAttribute(property, propertyValue);
        notifyChangedToParent();
    }
    
    @Override
    public void updateView(Object propertyValue) {
        // ignoreChangedEvent确保在更新UI的时候不会再回调到3D编辑场景中
        ignoreChangedEvent = true;
        updateUI(propertyValue);
        ignoreChangedEvent = false;
    }
    
    protected void addUndoRedo(Object beforeValue, Object afterValue) {
        parent.addUndoRedo(property, beforeValue, afterValue);
    }
    
    /**
     * 创建Layout布局并返回
     * @return 
     */
    protected abstract Node createLayout();
    
    /**
     * 更新UI
     * @param propertyValue 属性值
     */
    protected abstract void updateUI(Object propertyValue);
    
}
