/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.input.KeyInput;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.editor.action.Picker;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.AxisNode;
import name.huliqing.editor.tiles.LocationControlObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.editor.edit.SimpleJmeEditListener;

/**
 * 移动编辑工具
 * @author huliqing
 */
public class MoveTool extends EditTool implements SimpleJmeEditListener{
//    private static final Logger LOG = Logger.getLogger(MoveTool.class.getName());
    
    private final static String EVENT_MOVE = "moveEvent";
    private final static String EVENT_MOVE_X = "moveXEvent";
    private final static String EVENT_MOVE_Y = "moveYEvent";
    private final static String EVENT_MOVE_Z = "moveZEvent";
    
    private final static String EVENT_FREE_MOVE_START = "freeMoveStartEvent";
    private final static String EVENT_FREE_MOVE_CANCEL = "freeMoveCancelEvent";
    
    private final Ray ray = new Ray();
    private final Picker picker = new Picker();
    // 物体选择、操作标记（位置）
    private final LocationControlObj controlObj = new LocationControlObj();

    // 当前操作的轴向
    private AxisNode moveAxis;
    // 行为操作开始时编辑器中的被选择的物体，以及该物体的位置
    private SelectObj selectObj;
    
    // 开始移动时和结束移动时物体的位置(local)
    private final Vector3f startSpatialLoc = new Vector3f();
    private final Vector3f lastSpatialLoc = new Vector3f();
    
    // 自由移动
    private boolean transforming;
    private boolean freeMove;
    
    public MoveTool(String name) {
        super(name);
        // 绑定三个按键：x,y,z用于在自由旋转的时候转化到按轴旋转
        bindEvent(EVENT_MOVE_X).bindKey(KeyInput.KEY_X, false);
        bindEvent(EVENT_MOVE_Y).bindKey(KeyInput.KEY_Y, false);
        bindEvent(EVENT_MOVE_Z).bindKey(KeyInput.KEY_Z, false);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        form.getEditRoot().getParent().attachChild(controlObj);
        form.addSimpleEditListener(this);
        updateMarkerState();
    }
    
    @Override
    public void cleanup() {
        endMove();
        form.addSimpleEditListener(this);
        controlObj.removeFromParent();
        super.cleanup();
    }
    
    /**
     * 绑定移动按键
     * @return 
     */
    public JmeEvent bindMoveEvent() {
        return bindEvent(EVENT_MOVE);
    }
    
    public JmeEvent bindFreeMoveStartEvent() {
        return bindEvent(EVENT_FREE_MOVE_START);
    }
    public JmeEvent bindFreeMoveCancelEvent() {
        return bindEvent(EVENT_FREE_MOVE_CANCEL);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        selectObj = form.getSelected();
        if (selectObj == null) {
            endMove();
            return;
        }
        
        if (e.getName().equals(EVENT_MOVE)) {
            if (e.isMatch()) {
                PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
                AxisNode axis = controlObj.getPickAxis(ray);
                if (axis != null) {
                    startMove(axis);
                }
            } else {
                endMove();
            }
        }
        
        // 开始自由操作
        else if (EVENT_FREE_MOVE_START.equals(e.getName())) {
            if (e.isMatch()) {
                startFreeMove();
            }
        // 取消操作
        } else if (transforming && EVENT_FREE_MOVE_CANCEL.equals(e.getName())) { 
            if (e.isMatch()) {
                cancelMove();
                e.setConsumed(true);
            }
        }
        
        // 从自由操作转到约束操作
        else if (transforming && e.isMatch() && EVENT_MOVE_X.equals(e.getName())) {
            cancelMove();
            startMove(controlObj.getAxisX());
        }
        else if (transforming && e.isMatch() && EVENT_MOVE_Y.equals(e.getName())) {
            cancelMove();
            startMove(controlObj.getAxisY());
        }
        else if (transforming && e.isMatch() && EVENT_MOVE_Z.equals(e.getName())) {
            cancelMove();
            startMove(controlObj.getAxisZ());
        }
    }
    
    /**
     * 自由移动操作
     */
    public void startFreeMove() {
        freeMove = true;
        transforming = true;
        picker.startPick(selectObj.getReadOnlySelectedSpatial(), Mode.CAMERA
                , editor.getCamera(), editor.getInputManager().getCursorPosition(), Picker.PLANE_XY);
        startSpatialLoc.set(selectObj.getReadOnlySelectedSpatial().getLocalTranslation());
        lastSpatialLoc.set(startSpatialLoc);
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
    }

    private void startMove(AxisNode axis) {
        moveAxis = axis;
        transforming = true;
        freeMove = false;
        Quaternion planRotation = Picker.PLANE_XY;
        switch (moveAxis.getType()) {
            case x:
                planRotation = Picker.PLANE_XY;
                break;
            case y:
//                planRotation = Picker.PLANE_YZ;
                planRotation = Picker.PLANE_XY;
                break;
            case z:
                planRotation = Picker.PLANE_XZ;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        picker.startPick(selectObj.getReadOnlySelectedSpatial(), form.getMode()
                , editor.getCamera(), editor.getInputManager().getCursorPosition(), planRotation);
        startSpatialLoc.set(selectObj.getReadOnlySelectedSpatial().getLocalTranslation());
        lastSpatialLoc.set(startSpatialLoc);
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
        moveAxis.setAxisLineVisible(true);
    }

    private void endMove() {
        if (transforming) {
            picker.endPick();
            MoveUndo undoRedo = new MoveUndo(selectObj, startSpatialLoc, lastSpatialLoc);
            form.addUndoRedo(undoRedo);
        }
        transforming = false;
        freeMove = false;
        moveAxis = null;
        controlObj.setAxisVisible(true);
        controlObj.setAxisLineVisible(false);
        
    }
    
    private void cancelMove() {
        if (transforming) {
            picker.endPick();
            selectObj.setLocalTranslation(startSpatialLoc);
            transforming = false;
            // 更新一次位置,因为操作取消了
            updateMarkerState();
        }
        endMove();
    }
    
    @Override
    public void update(float tpf) {
        // 对于相机视角，Marker必须实时随着相机的移动旋转而更新
        if (form.getMode() == Mode.CAMERA) {
            updateMarkerState();
        }
        
        if (!transforming)
            return;
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }
        
        TempVars tv = TempVars.get();
        
        Vector3f diff;
        if (freeMove) {
            diff = picker.getTranslation();
        } else {
            diff = picker.getTranslation(moveAxis.getDirection(tv.vect2));
        }
        
        Spatial parent = selectObj.getReadOnlySelectedSpatial().getParent();
        if (parent != null) {
            tv.quat1.set(parent.getWorldRotation()).inverseLocal().mult(diff, diff);
            diff.divideLocal(parent.getWorldScale());
        }
        
        Vector3f finalLocalPos = tv.vect1.set(startSpatialLoc).addLocal(diff);
        selectObj.setLocalTranslation(finalLocalPos);
        controlObj.setLocalTranslation(selectObj.getReadOnlySelectedSpatial().getWorldTranslation());
        lastSpatialLoc.set(finalLocalPos);
        tv.release();
    }

    @Override
    public void onModeChanged(Mode mode) {
        updateMarkerState();
    }

    @Override
    public void onSelect(SelectObj selectObj) {
        updateMarkerState();
    }
    
    private void updateMarkerState() {
        selectObj = form.getSelected();
        if (selectObj == null) {
            controlObj.setVisible(false);
            return;
        }
        controlObj.setVisible(true);
        controlObj.setLocalTranslation(form.getSelected().getReadOnlySelectedSpatial().getWorldTranslation());
        Mode mode = form.getMode();
        switch (form.getMode()) {
            case GLOBAL:
                controlObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                controlObj.setLocalRotation(form.getSelected().getReadOnlySelectedSpatial().getWorldRotation());
                break;
            case CAMERA:
                controlObj.setLocalRotation(editor.getCamera().getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }

    private class MoveUndo implements UndoRedo {

        private final SelectObj selectObj;
        private final Vector3f before = new Vector3f();
        private final Vector3f after = new Vector3f();
        
        public MoveUndo(SelectObj selectObj, Vector3f startPosition, Vector3f lastPosition) {
            this.selectObj = selectObj;
            this.before.set(startPosition);
            this.after.set(lastPosition);
        }
        
        @Override
        public void undo() {
            selectObj.setLocalTranslation(before);
            updateMarkerState();
        }

        @Override
        public void redo() {
            selectObj.setLocalTranslation(after);
            updateMarkerState();
        }

        @Override
        public String toString() {
            return "MoveUndo:" + selectObj + ", before=" + before + ", after=" + after;
        }
        
    }
}
