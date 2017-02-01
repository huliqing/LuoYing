/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.base;

import com.jme3.input.KeyInput;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.editor.action.Picker;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.tiles.AxisNode;
import name.huliqing.editor.tiles.ScaleControlObj;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.editor.edit.SimpleJmeEditListener;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.tools.EditTool;

/**
 * 缩放编辑工具
 * @author huliqing
 */
public class ScaleTool extends EditTool implements SimpleJmeEditListener{

//    private static final Logger LOG = Logger.getLogger(ScaleTool.class.getName());
    
    // 点击轴向进行缩放
    private final static String EVENT_SCALE = "scaleEvent";
    private final static String EVENT_SCALE_X = "scaleXEvent";
    private final static String EVENT_SCALE_Y = "scaleYEvent";
    private final static String EVENT_SCALE_Z = "scaleZEvent";
    // 点击按键进行全缩放
    private final static String EVENT_FREE_SCALE_START = "fullScaleStartEvent";
    private final static String EVENT_FREE_SCALE_CANCEL = "fullScaleCancelEvent";
    
    private final Ray ray = new Ray();
    
    // 变换控制物体
    private final ScaleControlObj controlObj = new ScaleControlObj();
    private ControlTile selectObj;
    
    private final Picker picker = new Picker();
    private Vector3f startScale;
    private Vector3f afterScale;
    
    // 是否当前正在缩放操作
    private boolean transforming;
    // 自由操作，整体缩放
    private boolean freeScale;
    // 按轴缩放
    private boolean axisScale;
    
    // 当前操作的轴向
    private AxisNode controlAxis;

    public ScaleTool(String name, String tips, String icon) {
        super(name, tips, icon);
        // 绑定三个按键：x,y,z用于在fullScale时转化为按指定轴缩放
        bindEvent(EVENT_SCALE_X).bindKey(KeyInput.KEY_X, true);
        bindEvent(EVENT_SCALE_Y).bindKey(KeyInput.KEY_Y, true);
        bindEvent(EVENT_SCALE_Z).bindKey(KeyInput.KEY_Z, true);
    }

    @Override
    public void initialize(SimpleJmeEdit jmeEdit, EditToolbar toolbar) {
        super.initialize(jmeEdit, toolbar);
        edit.getEditRoot().getParent().attachChild(controlObj);
        edit.addSimpleEditListener(this);
        updateMarkerState();
    }

    @Override
    public void cleanup() {
        endScale();
        controlObj.removeFromParent();
        edit.removeEditFormListener(this);
        super.cleanup(); 
    }
    
    /**
     * 绑定移动按键
     * @return 
     */
    public JmeEvent bindScaleEvent() {
        return bindEvent(EVENT_SCALE);
    }
    
    /**
     * 绑定一个开始全局缩放的按键事件
     * @return 
     */
    public JmeEvent bindFreeScaleStartEvent() {
        return bindEvent(EVENT_FREE_SCALE_START);
    }
    
    /**
     * 绑定一个取消全局缩放的按键事件
     * @return 
     */
    public JmeEvent bindFreeScaleCancelEvent() {
        return bindEvent(EVENT_FREE_SCALE_CANCEL);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        selectObj = edit.getSelected();
        if (selectObj == null || selectObj.getControlSpatial() == null) {
            endScale();
            return;
        }
        
        // 普通按轴缩放(一般为鼠标按着沿轴向缩放)
        if (EVENT_SCALE.equals(e.getName())) {
            if (e.isMatch()) {
                // 重要优化：如果当前正在自由操作，则直接结束操作，不要启动新的按轴操作。
                // 因为这个操作可能是要结束自由操作，而不是希望启动按轴操作, 如果是这样则会多出一个多余的历史记录。
                if (freeScale) {
                    endScale();
                    return;
                }
                
                PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
                if (controlObj.isPickCenter(ray)) {
                    startFreeScale();
                    e.setConsumed(true);
                    return;
                }
                AxisNode scaleAxis = controlObj.getPickAxis(ray);
                if (scaleAxis != null) {
                    startAxisScale(scaleAxis);
                    e.setConsumed(true);
                }
            } else {
                endScale();
            }
        }
        
        // 整体缩放开始
        else if (EVENT_FREE_SCALE_START.equals(e.getName())) {
            if (e.isMatch()) {
                startFreeScale();
                e.setConsumed(true);
            }
        }
         // 整体缩放取消
        else if (transforming && EVENT_FREE_SCALE_CANCEL.equals(e.getName())) {
            if (e.isMatch()) {
                cancelScale();
                e.setConsumed(true);
            }
        }
        
        // 全局缩放时按X来转到X轴缩放
        else if (transforming && e.isMatch() && EVENT_SCALE_X.equals(e.getName())) {
            cancelScale();
            startAxisScale(controlObj.getAxisX());
            e.setConsumed(true);
        }
        else if (transforming && e.isMatch() && EVENT_SCALE_Y.equals(e.getName())) {
            cancelScale();
            startAxisScale(controlObj.getAxisY());
            e.setConsumed(true);
        }
        else if (transforming && e.isMatch() && EVENT_SCALE_Z.equals(e.getName())) {
            cancelScale();
            startAxisScale(controlObj.getAxisZ());
            e.setConsumed(true);
        }
    }
    
    private void startFreeScale() {
        // 重要：这要避免自由操作和按轴操作时的冲突,因为自由操作按键可能和按轴操作按键冲突。
        // 比如：当自由操作通过按下“左键”来应用操作的时候，而这个按键又和按轴操作冲突，
        // 这时自由操作会直接变成按轴操作，而自由操作又来不及保存历史记录，
        // 这时候就导致“自由操作”丢失历史记录，而按轴操作又没有启动. 所以这里要FIX这个BUG。
        // 在按轴操作启动时要确保如果自由操作正在进行则需要保存历史记录再退出。
        if (axisScale) {
            endScale();
        }
        transforming = true;
        freeScale = true;
        axisScale = false;
        controlAxis = null;
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
        picker.startPick(selectObj.getControlSpatial(), Mode.CAMERA, editor.getCamera()
                , editor.getInputManager().getCursorPosition(), editor.getCamera().getRotation());
        startScale = selectObj.getControlSpatial().getLocalScale().clone();
        afterScale = new Vector3f(startScale);
    }
    
    // 普通的按轴缩放
    private void startAxisScale(AxisNode scaleAxis) {
        // 重要：这要避免自由操作和按轴操作时的冲突,因为自由操作按键可能和按轴操作按键冲突。
        // 比如：当自由操作通过按下“左键”来应用操作的时候，而这个按键又和按轴操作冲突，
        // 这时自由操作会直接变成按轴操作，而自由操作又来不及保存历史记录，
        // 这时候就导致“自由操作”丢失历史记录，而按轴操作又没有启动. 所以这里要FIX这个BUG。
        // 在按轴操作启动时要确保如果自由操作正在进行则需要保存历史记录再退出。
        if (freeScale) {
            endScale();
        }
        transforming = true;
        freeScale = false;
        axisScale = true;
        controlAxis = scaleAxis;
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
        controlAxis.setAxisLineVisible(true);
        Quaternion planRotation = Picker.PLANE_XY;
        switch (controlAxis.getType()) {
            case x:
                planRotation = Picker.PLANE_XY;
                break;
            case y:
                planRotation = Picker.PLANE_YZ;
                break;
            case z:
                planRotation = Picker.PLANE_XZ;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        picker.startPick(selectObj.getControlSpatial(), edit.getMode(), editor.getCamera()
                , editor.getInputManager().getCursorPosition(), planRotation);
        startScale = selectObj.getControlSpatial().getLocalScale().clone();
        afterScale = new Vector3f(startScale);
    }
    
    /**
     * 结束操作缩放
     */
    private void endScale() {
        if (transforming) {
            picker.endPick();
            edit.addUndoRedo(new ScaleUndoRedo(selectObj, startScale, afterScale));
        }
        startScale = null;
        afterScale = null;
        transforming = false;
        freeScale = false;
        axisScale = false;
        controlAxis = null;
        controlObj.setAxisVisible(true);
        controlObj.setAxisLineVisible(false);
    }
    
    /**
     * 取消缩放操作 
     */
    private void cancelScale() {
        if (transforming) {
            picker.endPick();
            selectObj.setLocalScale(startScale);
            transforming = false;
            // 更新一次位置,因为操作取消了
            updateMarkerState();
        }
        endScale();
    }

    @Override
    public void update(float tpf) {
        // 对于相机视角，Marker必须实时随着相机的移动旋转而更新
        if (edit.getMode() == Mode.CAMERA) {
            updateMarkerState();
        }
        
        if (!transforming) {
            return;
        }
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }

        if (freeScale) {
            
            TempVars tv = TempVars.get();
            Vector3f constraintAxis = picker.getStartOffset(tv.vect1).normalizeLocal();
            float diff = picker.getLocalTranslation(constraintAxis).dot(constraintAxis) * 0.1f + 1f; // * 0.1减少一些缩放强度
            Vector3f scale = startScale.mult(diff, tv.vect2);
            selectObj.setLocalScale(scale);
            afterScale.set(scale);
            tv.release();
            
        } else {
            
            TempVars tv = TempVars.get();
            Vector3f axis = controlAxis.getDirection(tv.vect1);
            Vector3f newScale = tv.vect2.set(startScale);
            Vector3f diff = tv.vect3;
            Vector3f pickTranslation = picker.getTranslation(axis);
            Quaternion worldRotInverse = tv.quat1.set(selectObj.getControlSpatial().getWorldRotation()).inverse();
            worldRotInverse.mult(pickTranslation, diff);
            diff.multLocal(0.5f);
            newScale.addLocal(diff);
            selectObj.setLocalScale(new Vector3f(newScale));
            afterScale.set(newScale);
            tv.release();
        }
    }
    
    @Override
    public void onModeChanged(Mode mode) {
        updateMarkerState();
    }

    @Override
    public void onSelect(ControlTile selectObj) {
        cancelScale(); // 如果选择的物体发生变化，则取消当前缩放
        updateMarkerState();
    }

    private void updateMarkerState() {
        if (edit.getSelected() == null) {
            controlObj.setVisible(false);
            return;
        }
        controlObj.setVisible(true);
        controlObj.setLocalTranslation(edit.getSelected().getControlSpatial().getWorldTranslation());
        Mode mode = edit.getMode();
        switch (edit.getMode()) {
            case GLOBAL:
                controlObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                controlObj.setLocalRotation(edit.getSelected().getControlSpatial().getWorldRotation());
                break;
            case CAMERA:
                controlObj.setLocalRotation(editor.getCamera().getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
    
    private class ScaleUndoRedo implements UndoRedo {
        private final ControlTile spatial;
        private final Vector3f beforeScale = new Vector3f();
        private final Vector3f afterScale = new Vector3f();
        
        public ScaleUndoRedo(ControlTile selectObj, Vector3f before, Vector3f after) {
            this.spatial = selectObj;
            beforeScale.set(before);
            afterScale.set(after);
        }
        
        @Override
        public void undo() {
            spatial.setLocalScale(beforeScale);
            updateMarkerState();
        }

        @Override
        public void redo() {
            spatial.setLocalScale(afterScale);
            updateMarkerState();
        }
    
    }
}
