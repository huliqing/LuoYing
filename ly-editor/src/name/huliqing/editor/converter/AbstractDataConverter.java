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
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class AbstractDataConverter<E extends JfxAbstractEdit, T extends ObjectData> implements DataConverter<E, T> {

    private static final Logger LOG = Logger.getLogger(AbstractDataConverter.class.getName());

    protected E editView;
    protected T data;
    protected PropertyConverter parent;
    protected final Map<String, PropertyConverter> propertyConverters = new LinkedHashMap();
    
    protected final ScrollPane dataScroll = new ScrollPane();
    protected final VBox propertyPanel = new VBox();
    
    protected boolean initialized;
    
    public AbstractDataConverter() {}
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public Node getLayout() {
        return dataScroll; 
    }

    @Override
    public void initialize(E editView, T data, List<PropertyConverterDefine> propertyConvertDefines, PropertyConverter parent) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        this.editView = editView;
        this.data = data;
        this.parent = parent;
        
        dataScroll.setId(StyleConstants.ID_PROPERTY_PANEL);
        dataScroll.setContent(propertyPanel);
        dataScroll.setFitToWidth(true);
        
        if (propertyConvertDefines != null && !propertyConvertDefines.isEmpty()) {
            propertyConvertDefines.forEach(t -> {
                PropertyConverter pc = onCreatePropertyConverter(t);
                propertyPanel.getChildren().add(pc.getLayout());
                propertyConverters.put(t.propertyName, pc);
            });
        }
        
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
    
    protected PropertyConverter onCreatePropertyConverter(PropertyConverterDefine pcd) {
        try {
            PropertyConverter pc = pcd.propertyConverter.newInstance();
            pc.initialize(editView, this, pcd.propertyName);
            return pc;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Could not create PropertyConverter", ex);
        }
        return null;
    }

    @Override
    public void notifyChangedToParent() {
        LOG.log(Level.INFO, "DataConverter notify changed, DataConverter={0}, data={1}"
                , new Object[] {getClass(), data.getId()});
        if (parent != null) {
            parent.notifyChangedToParent();
        }
    }
    
}
