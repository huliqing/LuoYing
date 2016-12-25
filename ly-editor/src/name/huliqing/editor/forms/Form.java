/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.EditorCamera;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.tiles.TransformObj;

/**
 *
 * @author huliqing
 */
public interface Form {
    
    void initialize(Editor editor);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    TransformMode getTransformMode();
    
    /**
     * 设置变换模式
     * @param tm 
     */
    void setTransformMode(TransformMode tm);
    
    /**
     * 设置行为
     * @param tt 
     */
    void setTransformType(TransformType tt);
    
    EditorCamera getEditorCamera();
    
    ChaseObj getChaseObj();
    
    /**
     * 获取当前的操作物体
     * @return 
     */
    TransformObj getTransformObj();
    
    /**
     * 获取当前正被选中的物体
     * @return 
     */
    SelectObj getSelected();
    
    /**
     * 当点击选择时该方法被调用，子类实现这个方法来选择场景物体
     */
    void onPick();
    
}
