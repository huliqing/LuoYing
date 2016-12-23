/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.editor.Editor;
import name.huliqing.editor.EditorCamera;
import name.huliqing.editor.select.EmptySelectObj;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.AbstractActionObj;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.tiles.Grid;
import name.huliqing.editor.tiles.LocationObj;
import name.huliqing.editor.tiles.RotationObj;
import name.huliqing.editor.tiles.ScaleObj;

/**
 *
 * @author huliqing
 */
public abstract class BaseForm implements Form {
    
    // 网格
    protected Grid grid;
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
    // 当前的选择模式
    protected Action action = Action.LOCATION;
    // 变换模式
    protected Mode mode = Mode.GLOBAL;
    
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
    public LocationObj getLocationObj() {
        return locationObj;
    }

    @Override
    public RotationObj getRotationObj() {
        return rotationObj;
    }

    @Override
    public ScaleObj getScaleObj() {
        return scaleObj;
    }
    
    @Override
    public ChaseObj getChaseObj() {
        return chaseObj;
    }
    
    @Override
    public void setSelectObj(SelectObj object) {
        this.selectObj = object;
        updateActionState();
    }

    @Override
    public SelectObj getSelectObj() {
        return selectObj;
    }

    @Override
    public EditorCamera getEditorCamera() {
        return editorCam;
    }

    @Override
    public void setAction(Action action) {
        this.action = action;
        updateActionState();
    }
    
    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
        updateActionState();
    }
    
    protected void updateActionState() {
        locationObj.setVisible(false);
        rotationObj.setVisible(false);
        scaleObj.setVisible(false);
        
        AbstractActionObj activeObj;
        switch (action) {
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
                throw new IllegalArgumentException("Unknow action type=" + action);
        }
        activeObj.setVisible(true);
        activeObj.setLocalTranslation(selectObj.getLocation());
        switch (mode) {
            case GLOBAL:
                activeObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                activeObj.setLocalRotation(selectObj.getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
}
