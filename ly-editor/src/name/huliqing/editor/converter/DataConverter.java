/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.define.FieldDefine;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class DataConverter<E extends JfxAbstractEdit, T extends ObjectData> extends AbstractConverter<E, T, FieldConverter> {
//    private static final Logger LOG = Logger.getLogger(AbstractDataConverter.class.getName());
        
    /** 指定要隐藏的字段, 格式:"field1,field2,..." */
    public final static String FEATURE_HIDE_FIELDS = "hideFields";
    
    protected Map<String, FieldDefine> fieldDefines;
    
    protected final Map<String, FieldConverter> fieldConverters = new LinkedHashMap();
    
    protected final ScrollPane dataScroll = new ScrollPane();
    protected final VBox fieldPanel = new VBox();
    
    protected final TitledPane childPane = new TitledPane();
    
    public DataConverter() {
        dataScroll.setId(StyleConstants.ID_PROPERTY_PANEL);
        dataScroll.setContent(fieldPanel);
        
        childPane.managedProperty().bind(childPane.visibleProperty());
    }
    
    @Override
    public Region getLayout() {
        return dataScroll; 
    }
    
    public void setFieldDefines(Map<String, FieldDefine> fieldDefines) {
        this.fieldDefines = fieldDefines;
    }
    
    public void updateAttribute(String property, Object value) {
        data.setAttribute(property, value);
        notifyChanged();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        jfxEdit.getPropertyPanel().getChildren().addAll(childPane);
        childPane.setVisible(false);
        
        if (fieldDefines != null && !fieldDefines.isEmpty()) {
            fieldDefines.values().forEach(t -> {
                FieldConverter pc = ConverterManager.createPropertyConverter(jfxEdit, data, t, this);
                pc.initialize();
                pc.updateView();
                fieldPanel.getChildren().add(pc.getLayout());
                fieldConverters.put(t.getName(), pc);
            });
        }
        
        // 隐藏指定的字段
        List<String> hideFields = featureHelper.getAsList(FEATURE_HIDE_FIELDS);
        if (hideFields != null) {
            hideFields.forEach(t -> {
                FieldConverter pc = fieldConverters.get(t);
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
    public void cleanup() {
        fieldConverters.values().stream().filter(t -> t.isInitialized()).forEach(
            t -> {t.cleanup();}
        );
        fieldConverters.clear();
        fieldPanel.getChildren().clear();
        
        jfxEdit.getPropertyPanel().getChildren().removeAll(childPane);
        super.cleanup();
    }
    
    public void setChildContent(String childTitle, Node childContent) {
        childPane.setText(childTitle);
        childPane.setContent(childContent);
        childPane.setVisible(true);
    }
}
