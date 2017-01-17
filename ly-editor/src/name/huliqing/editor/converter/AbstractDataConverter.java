/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class AbstractDataConverter<E extends JfxAbstractEdit, T extends ObjectData> implements DataConverter<E, T> {
    private static final Logger LOG = Logger.getLogger(AbstractDataConverter.class.getName());
    
    /**
     * 字段转换器定义
     */
    protected Map<String, PropertyConverterDefine> propertyConvertDefines;
    protected FeatureHelper featureHelper;
    
    protected E jfxEdit;
    protected T data;
    protected PropertyConverter parent;
    protected final Map<String, PropertyConverter> propertyConverters = new LinkedHashMap();
    protected boolean initialized;
    protected final ScrollPane dataScroll = new ScrollPane();
    protected final VBox propertyPanel = new VBox();
    
    public AbstractDataConverter() {}
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public void setPropertyConverterDefines(Map<String, PropertyConverterDefine> propertyConverterDefines) {
        this.propertyConvertDefines = propertyConverterDefines;
    }
    
    @Override
    public void setFeatures(Map<String, Object> features) {
        featureHelper = new FeatureHelper(features);
    }
    
    @Override
    public void setEdit(E edit) {
        this.jfxEdit = edit;
    }
    
    @Override
    public void initialize(PropertyConverter parent) {
        if (initialized) {
            throw new IllegalStateException();
        }
        this.initialized = true;
        this.parent = parent;
        
        dataScroll.setId(StyleConstants.ID_PROPERTY_PANEL);
        dataScroll.setContent(propertyPanel);
        
        if (propertyConvertDefines != null && !propertyConvertDefines.isEmpty()) {
            propertyConvertDefines.values().forEach(t -> {
                PropertyConverter pc = ConverterManager.createPropertyConverter(jfxEdit, t);
                pc.initialize(this);
                pc.updateView(data.getAttribute(pc.getProperty()));
                propertyPanel.getChildren().add(pc.getLayout());
                propertyConverters.put(t.getPropertyName(), pc);
            });
        }
        
        // 隐藏指定的字段
        List<String> hideFields = featureHelper.getAsList(ConverterManager.FEATURE_HIDE_FIELDS);
        if (hideFields != null) {
            hideFields.forEach(t -> {
                PropertyConverter pc = propertyConverters.get(t);
                if (pc != null) {
                    // 隐藏的时候要同时把managed设置为false才不会占位
                    pc.getLayout().managedProperty().bind(pc.getLayout().visibleProperty());
                    pc.getLayout().setVisible(false);
                }
            });
        }
        
        dataScroll.setFitToWidth(true);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        propertyConverters.values().stream().filter(t -> t.isInitialized()).forEach(
            t -> {t.cleanup();}
        );
        propertyConverters.clear();
        propertyPanel.getChildren().clear();
        dataScroll.setContent(null);
        initialized = false;
    }
    
    @Override
    public void notifyChangedToParent() {
        LOG.log(Level.INFO, "DataConverter notify changed, DataConverter={0}, data={1}"
                , new Object[] {getClass(), data.getId()});
        if (parent != null) {
            parent.notifyChangedToParent();
        }
    }

    @Override
    public Node getLayout() {
        return dataScroll; 
    }
    
    @Override
    public void addUndoRedo(String property, Object beforeValue, Object afterValue) {
         jfxEdit.addUndoRedo(new JfxEditUndoRedo(property, beforeValue, afterValue));
    }
    
    private class JfxEditUndoRedo implements UndoRedo {

        private final String property;
        private final Object before;
        private final Object after;
        
        public JfxEditUndoRedo(String property, Object before, Object after) {
            this.property = property;
            this.before = before;
            this.after = after;
        }
        
        @Override
        public void undo() {
            data.setAttribute(property, before);
            Jfx.runOnJfx(() -> {
                notifyChangedToParent();
                PropertyConverter pc  = propertyConverters.get(property);
                if (pc != null) {
                    pc.updateView(before);
                }
            });
        }
        
        @Override
        public void redo() {
            data.setAttribute(property, after);
            Jfx.runOnJfx(() -> {
                notifyChangedToParent();
                PropertyConverter pc  = propertyConverters.get(property);
                if (pc != null) {
                    pc.updateView(after);
                }
            });
        }
        
    }
}
