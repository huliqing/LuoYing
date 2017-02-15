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
import name.huliqing.luoying.xml.ObjectData;

/**
 * 字段转换器
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class FieldConverter<E extends JfxAbstractEdit, T extends ObjectData> extends AbstractConverter<E, T, DataConverter> {
//    private static final Logger LOG = Logger.getLogger(AbstractFieldConverter.class.getName());
    
    /** 指定要DISABLED的字段，格式:"field1,field2,..." */
    public final static String FEATURE_DISABLED = "disabled"; 
    
    /** 让字段折叠 */
    public final static String FEATURE_COLLAPSED = "collapsed";
    
    protected String field; 
    
    protected final TitledPane root = new TitledPane();
    
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
     * 创建Layout布局并返回
     * @return 
     */
    protected abstract Node createLayout();
    
    /**
     * 更新View组件
     */
    public abstract void updateView();

    
}
