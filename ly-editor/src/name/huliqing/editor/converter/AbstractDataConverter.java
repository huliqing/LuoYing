/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractDataConverter<T extends ObjectData> implements DataConverter<T> {

    private static final Logger LOG = Logger.getLogger(AbstractDataConverter.class.getName());

    protected T data;
    protected PropertyConverter parent;
    
    protected final HBox layout = new HBox();
    protected final TitledPane dataPanel = new TitledPane();
    protected final ScrollPane dataScroll = new ScrollPane();
    protected final VBox propertyPanel = new VBox();
    
    protected boolean initialized;
    
    public AbstractDataConverter() {}
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public Pane getLayout() {
        return layout; 
    }

    @Override
    public void initialize(T data, List<PropertyConverterDefine> propertyConvertDefines, PropertyConverter parent) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        this.data = data;
        this.parent = parent;
        
        layout.getChildren().add(dataPanel);
        
        dataPanel.setId(StyleConstants.ID_PROPERTY_PANEL);
        dataPanel.setText(data.getId());
        dataPanel.setContent(dataScroll);
        
        dataScroll.setContent(propertyPanel);
        dataScroll.setFitToWidth(true);
        
        if (propertyConvertDefines != null && !propertyConvertDefines.isEmpty()) {
            propertyConvertDefines.forEach(t -> {
                PropertyConverter pc = createPropertyConverter(t);
                propertyPanel.getChildren().add(pc.getLayout());
            });
        }
    }
    
    protected PropertyConverter createPropertyConverter(PropertyConverterDefine pcd) {
        try {
            PropertyConverter pc = pcd.propertyConverter.newInstance();
            pc.initialize(this, pcd.propertyName);
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
