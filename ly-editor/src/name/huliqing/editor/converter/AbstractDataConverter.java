/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
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
    
    protected final TitledPane root = new TitledPane();
    protected final ScrollPane body = new ScrollPane();
    
    protected boolean initialized;
    
    public AbstractDataConverter() {}
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public Node getNode() {
        return root; 
    }

    @Override
    public void initialize(T data, List<PropertyConverterDefine> propertyConvertDefines, PropertyConverter parent) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        this.data = data;
        this.parent = parent;
        
        VBox layout = new VBox();
        if (propertyConvertDefines != null && !propertyConvertDefines.isEmpty()) {
            propertyConvertDefines.forEach(t -> {
                try {
                    PropertyConverter pc = t.propertyConverter.newInstance();
                    pc.initialize(this, t.propertyName);
                    layout.getChildren().add(pc.getNode());
                } catch (InstantiationException | IllegalAccessException ex) {
                    LOG.log(Level.SEVERE, "Could not create PropertyConverter"
                            + ", propertyName={0}, propertyConverterClass={1}, dataConverter={2}"
                        , new Object[] {t.propertyName, t.propertyConverter, getClass()});
                    LOG.log(Level.SEVERE, "", ex);
                }
            });
        }
        
        root.setId(StyleConstants.ID_PROPERTY_PANEL);
        root.setText(data.getId());
        root.setContent(body);
        body.setContent(layout);
        body.setFitToWidth(true);
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
