/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import name.huliqing.editor.Editor;
import name.huliqing.editor.EditorCamera;
import name.huliqing.editor.select.EmptySelectObj;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.tiles.Grid;
import name.huliqing.editor.tiles.LocationObj;
import name.huliqing.editor.tiles.RotationObj;
import name.huliqing.editor.tiles.ScaleObj;
import name.huliqing.editor.tiles.TransformObj;

/**
 *
 * @author huliqing
 */
public abstract class BaseForm implements Form {
    
    // 网格
    protected Grid grid;
    
    // 变换模式
    protected TransformMode mode = TransformMode.GLOBAL;
    // 当前的选择模式
    protected TransformType type = TransformType.LOCATION;
    
    // 被镜头跟随的物体
    protected ChaseObj chaseObj;
    // 物体选择、操作标记（位置）
    protected LocationObj locationObj;
    // 物体选择、操作标记（旋转） 
    protected RotationObj rotationObj;
    // 缩放
    protected ScaleObj scaleObj;
    // 镜头
    protected EditorCamera editorCam;
    // 当前选择的物体
    protected SelectObj selectObj = new EmptySelectObj();
    
    protected Editor editor;
    protected boolean initialized;
    protected final Node localRoot = new Node();

    @Override
    public void initialize(Editor editor) {
        if (initialized) {
            throw new IllegalArgumentException();
        }
        initialized = true;
        this.editor = editor;
        this.editor.getFlyByCamera().setEnabled(false);
        this.editor.getInputManager().setCursorVisible(true);
        this.editor.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
        this.editor.getRootNode().attachChild(localRoot);
        
        grid = new Grid();
        locationObj = new LocationObj();
        rotationObj = new RotationObj();
        scaleObj = new ScaleObj();
        chaseObj = new ChaseObj();
        
        editorCam = new EditorCamera(editor.getCamera(), editor.getInputManager());
        editorCam.setChase(chaseObj);
        
        localRoot.attachChild(grid); 
        localRoot.attachChild(locationObj);
        localRoot.attachChild(rotationObj);
        localRoot.attachChild(scaleObj);
        localRoot.attachChild(chaseObj);
        
        setTransformType(TransformType.LOCATION);
        
        updateActionState();
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {}

    @Override
    public void cleanup() {
        editorCam.cleanup();
        localRoot.detachAllChildren();
        initialized = false;
    }
    
    @Override
    public void setTransformType(TransformType type) {
        this.type = type;
        updateActionState();
    }

    @Override
    public TransformMode getTransformMode() {
        return mode;
    }
    
    @Override
    public void setTransformMode(TransformMode mode) {
        this.mode = mode;
        updateActionState();
    }
    
    @Override
    public ChaseObj getChaseObj() {
        return chaseObj;
    }
    
    @Override
    public EditorCamera getEditorCamera() {
        return editorCam;
    }

    @Override
    public TransformObj getTransformObj() {
        switch (type) {
            case LOCATION:
                return locationObj;
            case ROTATION:
                return rotationObj;
            case SCALE:
                return scaleObj;
            default:
                throw new IllegalArgumentException("Unknow action type=" + type);
        }
    }
    
    @Override
    public SelectObj getSelected() {
        return selectObj;
    }
    
    /**
     * 把一个物体设置为当前的选择的主物体
     * @param object 
     */
    protected void setSelected(SelectObj object) {
        this.selectObj = object;
        updateActionState();
    }
    
    private void updateActionState() {
        locationObj.setVisible(false);
        rotationObj.setVisible(false);
        scaleObj.setVisible(false);
        
        TransformObj activeObj;
        switch (type) {
            case LOCATION:
                activeObj = locationObj;
                break;
            case ROTATION:
                activeObj = rotationObj;
                break;
            case SCALE:
                activeObj = scaleObj;
                break;
            default:
                throw new IllegalArgumentException("Unknow action type=" + type);
        }
        activeObj.setVisible(true);
        activeObj.setLocalTranslation(selectObj.getSelectedSpatial().getWorldTranslation());
        switch (mode) {
            case GLOBAL:
                activeObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                activeObj.setLocalRotation(selectObj.getSelectedSpatial().getWorldRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
}
