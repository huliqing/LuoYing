/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import com.jme3.export.Savable;
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

    /**
     * 指定要隐藏的字段, 格式:"field1,field2,..."
     */
    public final static String FEATURE_HIDE_FIELDS = "hideFields";

    protected Map<String, FieldDefine> fieldDefines;

    protected final Map<String, FieldConverter> fieldConverters = new LinkedHashMap();

    protected final ScrollPane dataScroll = new ScrollPane();
    protected final VBox fieldPanel = new VBox();

    // 子面板，显示子联系的物体
    protected final TitledPane childPane = new TitledPane();
    protected DataConverter childDataConverter;

    public DataConverter() {
        dataScroll.setId(StyleConstants.ID_PROPERTY_PANEL);
        dataScroll.setContent(fieldPanel);
        childPane.managedProperty().bind(childPane.visibleProperty());
        // for debug
//        childPane.setStyle("-fx-border-style: solid inside;-fx-border-color:red;"); 
    }

    @Override
    public Region getLayout() {
        return dataScroll;
    }

    public void setFieldDefines(Map<String, FieldDefine> fieldDefines) {
        this.fieldDefines = fieldDefines;
    }

    public void updateAttribute(String property, Object value) {
        if (value == null) {
            data.setAttribute(property, (Savable) value); // 清除掉参数值. (Savable)用于调用特定的函数
            notifyChanged();
            return;
        }
        
        if (value instanceof Byte) {
            data.setAttribute(property, (Byte) value);
            
        } else if (value instanceof Short) {
            data.setAttribute(property, (Short) value);
            
        } else if (value instanceof Integer) {
            data.setAttribute(property, (Integer) value);
            
        } else if (value instanceof Float) {
            data.setAttribute(property, (Float) value);
            
        } else if (value instanceof Long) {
            data.setAttribute(property, (Long) value);
            
        } else if (value instanceof Double) {
            data.setAttribute(property, (Double) value);
            
        } else if (value instanceof Boolean) {
            data.setAttribute(property, (Boolean) value);
            
        } else if (value instanceof byte[]) {
            data.setAttribute(property, (byte[]) value);
            
        } else if (value instanceof short[]) {
            data.setAttribute(property, (short[]) value);
            
        } else if (value instanceof int[]) {
            data.setAttribute(property, (int[]) value);
            
        } else if (value instanceof float[]) {
            data.setAttribute(property, (float[]) value);
            
        } else if (value instanceof long[]) {
            data.setAttribute(property, (long[]) value);
            
        } else if (value instanceof double[]) {
            data.setAttribute(property, (double[]) value);
            
        } else if (value instanceof boolean[]) {
            data.setAttribute(property, (boolean[]) value);
            
        } else if (value instanceof Savable) {
            data.setAttribute(property, (Savable) value);
            
        } else if (value instanceof String) {
            data.setAttribute(property, (String) value);
            
        } else if (value instanceof String[]) {
            data.setAttributeStringArray(property, (String[]) value);
            
        } else if (value instanceof List) {
            // 列表类型目前只支持 List<String>和List<Savable>
            List listValue = (List) value;
            if (listValue.isEmpty()) {
//                data.setAttribute(property, (Savable) null); // 清除
                data.setAttributeStringList(property, listValue);
            } else {
                Object itemObject = listValue.get(0);
                if (itemObject instanceof String) {
                    data.setAttributeStringList(property, listValue);
                } else if (itemObject instanceof Savable) {
                    data.setAttributeSavableList(property, listValue);
                } else {
                    throw new UnsupportedOperationException("Unsupport data type of the list! object=" + itemObject);
                }
            }
            
        } else {
            throw new UnsupportedOperationException("Unsupport data type=" + value.getClass());
        }
        
        notifyChanged();
    }

    @Override
    public void initialize() {
        super.initialize();

        jfxEdit.getEditPanel().getChildren().add(childPane);
        childPane.setVisible(false);

        if (fieldDefines != null && !fieldDefines.isEmpty()) {
            fieldDefines.values().forEach(t -> {
                FieldConverter pc = ConverterManager.createPropertyConverter(jfxEdit, data, t, this);
                pc.initialize();
                pc.updateView();
                fieldPanel.getChildren().add(pc.getLayout());
                fieldConverters.put(t.getField(), pc);
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
        if (childDataConverter != null) {
            if (childDataConverter.isInitialized()) {
                childDataConverter.cleanup();
            }
            childDataConverter = null;
        }
        
        fieldConverters.values().stream().filter(t -> t.isInitialized()).forEach(
                t -> {
                    t.cleanup();
                }
        );
        fieldConverters.clear();
        fieldPanel.getChildren().clear();

        jfxEdit.getEditPanel().getChildren().remove(childPane);
        super.cleanup();
    }
    
    public void setChildLayout(String childTitle, DataConverter childConverter) {
        if (childDataConverter != null && childDataConverter != childConverter) {
            if (childDataConverter.isInitialized()) {
                childDataConverter.cleanup();
            }
        }
        childDataConverter = childConverter;
        childPane.setText(childTitle);
        childPane.setContent(childDataConverter.getLayout());
        childPane.setVisible(true);
    }
    
    
}
