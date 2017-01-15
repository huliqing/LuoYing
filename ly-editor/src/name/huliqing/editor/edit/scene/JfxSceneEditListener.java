/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.luoying.data.EntityData;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface JfxSceneEditListener<T extends EntitySelectObj> {
    
    /**
     * 当编辑模式发生变化时该方法被调用
     * @param mode 
     */
    void onModeChanged(Mode mode);
    
    /**
     * 当选择发生变化时该方法被调用
     * @param selectObj 
     */
    void onSelectChanged(T selectObj);
    
    void onEntityAdded(EntityData ed);
    
    void onEntityRemoved(EntityData ed);
}
