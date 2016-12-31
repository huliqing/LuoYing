/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.select.EmptySelectObj;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.editor.undoredo.UndoRedoManager;

/**
 * 3D模型编辑器窗口
 * @author huliqing
 */
public abstract class EditForm extends AbstractForm {
    
    protected final UndoRedoManager undoRedoManager = new UndoRedoManager();
    
    // 侦听器
    protected final List<EditFormListener> editFormListeners = new ArrayList<EditFormListener>();
    
    // 变换模式
    protected Mode mode = Mode.GLOBAL;
    
    // 当前选择的物体
    protected SelectObj selectObj = new EmptySelectObj();
    
    // 编辑场景的根节点
    protected final Node localRoot = new Node();

    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        editor.getFlyByCamera().setEnabled(false);
        editor.getInputManager().setCursorVisible(true);
        editor.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
        editor.getRootNode().attachChild(localRoot);
        
        // 编辑用的工具栏
        setToolbar(new EditToolbar());
        
    }

    @Override
    public void cleanup() {
        localRoot.detachAllChildren();
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
        return localRoot;
    }
    
    public void addEditFormListener(EditFormListener listener) {
        if (!editFormListeners.contains(listener)) {
            editFormListeners.add(listener);
        }
    }
    
    public boolean removeEditFormListener(EditFormListener listener) {
        return editFormListeners.remove(listener);
    }

    public UndoRedoManager getUndoRedoManager() {
        return undoRedoManager;
    }
    
    public void addUndoRedo(UndoRedo undoRedo) {
        undoRedoManager.add(undoRedo);
    }
}
