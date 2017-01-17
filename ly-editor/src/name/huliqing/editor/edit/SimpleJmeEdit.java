/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import com.jme3.math.Ray;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.editor.undoredo.UndoRedoManager;

/**
 * 3D模型编辑器窗口
 * @author huliqing 
 * @param <T> 
 */
public abstract class SimpleJmeEdit<T extends SelectObj> extends JmeAbstractEdit {
    
    // 侦听器
    protected final List<SimpleJmeEditListener> editFormListeners = new ArrayList<SimpleJmeEditListener>();
    
    // 变换模式
    protected Mode mode = Mode.GLOBAL;
    
    // 当前选择的物体
    protected T selectObj;
    
    // 编辑场景的根节点
    protected final Node editRoot = new Node();
    
    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        editor.getRootNode().attachChild(editRoot);
    }

    @Override
    public void cleanup() {
        editRoot.removeFromParent();
        super.cleanup();
    }
    
    public Mode getMode() {
        return mode;
    }
    
    public void setMode(Mode mode) {
        boolean changed = this.mode != mode;
        this.mode = mode;
        if (changed) {
            editFormListeners.forEach(l -> {l.onModeChanged(mode);});
        }
    }
    
    public T getSelected() {
        return selectObj;
    }
    
    /**
     * 把一个物体设置为当前的选择的主物体
     * @param selectObj 
     */
    public void setSelected(T selectObj) {
        this.selectObj = selectObj;
        editFormListeners.forEach(l -> {l.onSelect(selectObj);});
    }
    
    /**
     * 获取编辑窗口根节点。
     * @return 
     */
    public Node getEditRoot() {
        return editRoot;
    }
    
    public void addSimpleEditListener(SimpleJmeEditListener listener) {
        if (!editFormListeners.contains(listener)) {
            editFormListeners.add(listener);
        }
    }
    
    public boolean removeEditFormListener(SimpleJmeEditListener listener) {
        return editFormListeners.remove(listener);
    }
    
    /**
     * 通过射线方式从场景中选择一个可选择的物体，如果存在这样一个物体则返回，否则返回null.
     * @param ray
     * @return 
     */
    public abstract T doPick(Ray ray);
}
