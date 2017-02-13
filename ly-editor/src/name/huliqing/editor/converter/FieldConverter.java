/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 * 字段转换器
 * @author huliqing
 * @param <E>
 */
public abstract class FieldConverter<E extends JfxAbstractEdit> extends AbstractConverter<E, DataConverter> {
//    private static final Logger LOG = Logger.getLogger(AbstractFieldConverter.class.getName());
    
    /** 指定要DISABLED的字段，格式:"field1,field2,..." */
    public final static String FEATURE_DISABLED = "disabled"; 
    
    /** 让字段折叠 */
    public final static String FEATURE_COLLAPSED = "collapsed";
    
    protected String field; 
    
    protected final TitledPane root = new TitledPane();
    
    protected boolean ignoreChangedEvent;
    
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public Node getLayout() {
        return root;
    }

    @Override
    public void initialize() {
        super.initialize();
        root.setText(field);
        root.setAnimated(false);
        root.setAlignment(Pos.CENTER_LEFT);
        
        // features
        boolean disabled = featureHelper.getAsBoolean(FEATURE_DISABLED);
        boolean collapsed = featureHelper.getAsBoolean(FEATURE_COLLAPSED);
        
        // Layout and features
        Node layout = createLayout();
        layout.setDisable(disabled);
        root.setExpanded(!collapsed);
        root.setContent(layout);
    }

    @Override
    public void cleanup() {
        root.setContent(null);
        super.cleanup();
    }
    
    /**
     * 更新属性
     * @param propertyValue 
     */
    public void updateAttribute(Object propertyValue) {
        if (ignoreChangedEvent) {
            return;
        }
        parent.updateAttribute(field, propertyValue);
    }
    
    /**
     * 更新View组件
     * @param propertyValue 
     */
    public void updateView(Object propertyValue) {
        // ignoreChangedEvent确保在更新UI的时候不会再回调到3D编辑场景中
        ignoreChangedEvent = true;
        updateUI(propertyValue);
        ignoreChangedEvent = false;
    }
    
    protected void addUndoRedo(Object beforeValue, Object afterValue) {
        parent.addUndoRedo(field, beforeValue, afterValue);
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
