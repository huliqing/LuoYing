/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editforms;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.formview.FormView;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.editor.undoredo.UndoRedoManager;

/**
 * 3D模型编辑器窗口
 * @author huliqing
 */
public class SimpleEditForm extends AbstractForm {
    
    protected final UndoRedoManager undoRedoManager = new UndoRedoManager();
    
    // 侦听器
    protected final List<SimpleEditFormListener> editFormListeners = new ArrayList<SimpleEditFormListener>();
    
    // 变换模式
    protected Mode mode = Mode.GLOBAL;
    
    // 当前选择的物体
    protected SelectObj selectObj;
    
    // 编辑场景的根节点
    protected final Node editRoot = new Node();
    
    @Override
    public void initialize(FormView formView) {
        super.initialize(formView);
        formView.getEditor().getRootNode().attachChild(editRoot);
    }

    @Override
    public void cleanup() {
        editRoot.detachAllChildren();
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
    
    public SelectObj getSelected() {
        return selectObj;
    }
    
    /**
     * 把一个物体设置为当前的选择的主物体
     * @param selectObj 
     */
    public void setSelected(SelectObj selectObj) {
        boolean changed = this.selectObj != selectObj;
        this.selectObj = selectObj;
        if (changed) {
            editFormListeners.forEach(l -> {l.onSelectChanged(selectObj);});
        }
    }
    
    /**
     * 获取编辑窗口根节点。
     * @return 
     */
    public Node getEditRoot() {
        return editRoot;
    }
    
    public void addEditFormListener(SimpleEditFormListener listener) {
        if (!editFormListeners.contains(listener)) {
            editFormListeners.add(listener);
        }
    }
    
    public boolean removeEditFormListener(SimpleEditFormListener listener) {
        return editFormListeners.remove(listener);
    }

    public UndoRedoManager getUndoRedoManager() {
        return undoRedoManager;
    }
    
    public void addUndoRedo(UndoRedo undoRedo) {
        undoRedoManager.add(undoRedo);
    }
}
