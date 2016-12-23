/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import name.huliqing.editor.Editor;
import name.huliqing.editor.EditorCamera;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.tiles.LocationObj;
import name.huliqing.editor.tiles.RotationObj;
import name.huliqing.editor.tiles.ScaleObj;

/**
 *
 * @author huliqing
 */
public interface Form {
    
    void initialize(Editor editor);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    LocationObj getLocationObj();
    
    RotationObj getRotationObj();
    
    ScaleObj getScaleObj();
    
    ChaseObj getChaseObj();
    
    void setSelectObj(SelectObj object);
    
    SelectObj getSelectObj();
    
    EditorCamera getEditorCamera();
    
    /**
     * 设置行为
     * @param action 
     */
    void setAction(Action action);
    
    /**
     * 设置变换模式
     * @param mode 
     */
    void setMode(Mode mode);
    
    /**
     * 当点击选择时该方法被调用，子类实现这个方法来选择场景物体
     */
    void onPick();
}
