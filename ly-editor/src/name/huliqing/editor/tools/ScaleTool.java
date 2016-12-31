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
import name.huliqing.editor.forms.EditFormListener;
import name.huliqing.editor.forms.Mode;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.AxisNode;
import name.huliqing.editor.tiles.ScaleControlObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.luoying.manager.PickManager;

/**
 * 缩放编辑工具
 * @author huliqing
 */
public class ScaleTool extends EditTool implements EditFormListener{

//    private static final Logger LOG = Logger.getLogger(ScaleTool.class.getName());
    
    // 点击轴向进行缩放
    private final static String EVENT_SCALE = "scaleEvent";
    private final static String EVENT_SCALE_X = "scaleXEvent";
    private final static String EVENT_SCALE_Y = "scaleYEvent";
    private final static String EVENT_SCALE_Z = "scaleZEvent";
    // 点击按键进行全缩放
    private final static String EVENT_FULL_SCALE_START = "fullScaleStartEvent";
    private final static String EVENT_FULL_SCALE_APPLY = "fullScaleApplyEvent";
    private final static String EVENT_FULL_SCALE_CANCEL = "fullScaleCancelEvent";
    
    private final Ray ray = new Ray();
    
    // 变换控制物体
    private final ScaleControlObj controlObj = new ScaleControlObj();
    private SelectObj selectObj;
    
    private final Picker picker = new Picker();
    private final Vector3f startScale = new Vector3f();
    private final Vector3f afterScale = new Vector3f();
    
    // 全局缩放，整体缩放
    private boolean fullScale;
    // 局部缩放，按轴缩放
    private boolean axisScale;
    // 当前操作的轴向
    private AxisNode controlAxis;
    // 是否当前正在缩放操作
    private boolean transforming;

    public ScaleTool(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        super.initialize(); 
        form.getEditRoot().getParent().attachChild(controlObj);
        form.addEditFormListener(this);
        
        // 绑定三个按键：x,y,z用于在fullScale时转化为按指定轴缩放
        bindEvent(EVENT_SCALE_X).bindKey(KeyInput.KEY_X, true);
        bindEvent(EVENT_SCALE_Y).bindKey(KeyInput.KEY_Y, true);
        bindEvent(EVENT_SCALE_Z).bindKey(KeyInput.KEY_Z, true);
        
        updateMarkerState();
    }

    @Override
    public void cleanup() {
        controlObj.removeFromParent();
        form.removeEditFormListener(this);
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
    public JmeEvent bindFullScaleStartEvent() {
        return bindEvent(EVENT_FULL_SCALE_START);
    }
    
    /**
     * 绑定一个应用全局缩放的按键事件
     * @return 
     */
    public JmeEvent bindFullScaleApplyEvent() {
        return bindEvent(EVENT_FULL_SCALE_APPLY);
    }
    
    /**
     * 绑定一个取消全局缩放的按键事件
     * @return 
     */
    public JmeEvent bindFullScaleCancelEvent() {
        return bindEvent(EVENT_FULL_SCALE_CANCEL);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        selectObj = form.getSelected();
        if (selectObj == null) {
            return;
        }
        
        // 普通按轴缩放(一般为鼠标按着沿轴向缩放)
        if (EVENT_SCALE.equals(e.getName())) {
            if (e.isMatch()) {
                PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
                if (controlObj.isPickCenter(ray)) {
                    startFullScale();
                    return;
                }
                AxisNode scaleAxis = controlObj.getPickAxis(ray);
                if (scaleAxis != null) {
                    startScale(scaleAxis);
                }
            } else {
                endScale();
            }
        }
        
        // 整体缩放开始
        else if (EVENT_FULL_SCALE_START.equals(e.getName())) {
            if (e.isMatch()) {
                startFullScale();
            }
        }
        
        // 整体缩放应用
        else if (EVENT_FULL_SCALE_APPLY.equals(e.getName())) {
            if (e.isMatch() && fullScale) {
                endScale();
            }
        }
        
        // 整体缩放取消
        else if (EVENT_FULL_SCALE_CANCEL.equals(e.getName())) {
            if (e.isMatch()) {
                cancelScale();
            }
        }
        
        // 全局缩放时按X来转到X轴缩放
        else if (transforming && e.isMatch() && EVENT_SCALE_X.equals(e.getName())) {
            cancelScale();
            startScale(controlObj.getAxisX());
        }
        else if (transforming && e.isMatch() && EVENT_SCALE_Y.equals(e.getName())) {
            cancelScale();
            startScale(controlObj.getAxisY());
        }
        else if (transforming && e.isMatch() && EVENT_SCALE_Z.equals(e.getName())) {
            cancelScale();
            startScale(controlObj.getAxisZ());
        }
    }
    
    private void startFullScale() {
        axisScale = false;
        fullScale = true;
        transforming = true;
        controlAxis = null;
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
        picker.startPick(selectObj.getSelectedSpatial(), Mode.CAMERA, editor.getCamera()
                , editor.getInputManager().getCursorPosition(), editor.getCamera().getRotation());
        startScale.set(selectObj.getSelectedSpatial().getLocalScale());
    }
    
    // 普通的按轴缩放
    private void startScale(AxisNode scaleAxis) {
        axisScale = true;
        fullScale = false;
        transforming = true;
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
        picker.startPick(selectObj.getSelectedSpatial(), form.getMode(), editor.getCamera()
                , editor.getInputManager().getCursorPosition(), planRotation);
        startScale.set(selectObj.getSelectedSpatial().getLocalScale());
    }
    
    /**
     * 结束操作缩放
     */
    private void endScale() {
        if (transforming) {
            picker.endPick();
            form.addUndoRedo(new ScaleUndoRedo(selectObj.getSelectedSpatial(), startScale, afterScale));
        }
        fullScale = false;
        axisScale = false;
        transforming = false;
        controlAxis = null;
        controlObj.setAxisVisible(true);
        controlObj.setAxisLineVisible(false);
    }
    
    /**
     * 取消缩放操作 
     */
    private void cancelScale() {
        if (transforming) {
            endScale();
            selectObj.getSelectedSpatial().setLocalScale(startScale);
            transforming = false;
        }
    }

    @Override
    public void update(float tpf) {
        if (!transforming) {
            return;
        }

        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }

        if (fullScale) {
            
            TempVars tv = TempVars.get();
            Vector3f constraintAxis = picker.getStartOffset(tv.vect1).normalizeLocal();
            float diff = picker.getLocalTranslation(constraintAxis).dot(constraintAxis) + 1f;
            Vector3f scale = startScale.mult(diff, tv.vect2);
            selectObj.getSelectedSpatial().setLocalScale(scale);
            afterScale.set(scale);
            tv.release();
            
        } else {
            
            TempVars tv = TempVars.get();
            Vector3f axis = controlAxis.getDirection(tv.vect1);
            Vector3f newScale = tv.vect2.set(startScale);
            Vector3f diff = tv.vect3;
            Vector3f pickTranslation = picker.getTranslation(axis);
            Quaternion worldRotInverse = tv.quat1.set(selectObj.getSelectedSpatial().getWorldRotation()).inverse();
            worldRotInverse.mult(pickTranslation, diff);
            diff.multLocal(2);
            newScale.addLocal(diff);
            selectObj.getSelectedSpatial().setLocalScale(newScale);
            afterScale.set(newScale);
            tv.release();
            
//            LOG.log(Level.INFO, "Axis={0}, pickTranslation={1}, startScale={2}, diffScale={3}, finalScale={4}, worldRot={5}, worldRotInverse={6}"
//                    , new Object[]{axis, pickTranslation, startScale, diff, newScale, selectObj.getSelectedSpatial().getWorldRotation(), worldRotInverse});

        }
    }
    
    @Override
    public void onModeChanged(Mode mode) {
        updateMarkerState();
    }

    @Override
    public void onSelectChanged(SelectObj selectObj) {
        cancelScale(); // 如果选择的物体发生变化，则取消当前缩放
        updateMarkerState();
    }

    private void updateMarkerState() {
        if (form.getSelected() == null) {
            controlObj.setVisible(false);
            return;
        }
        controlObj.setVisible(true);
        controlObj.setLocalTranslation(form.getSelected().getSelectedSpatial().getWorldTranslation());
        Mode mode = form.getMode();
        switch (form.getMode()) {
            case GLOBAL:
                controlObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                controlObj.setLocalRotation(form.getSelected().getSelectedSpatial().getWorldRotation());
                break;
            case CAMERA:
                controlObj.setLocalRotation(editor.getCamera().getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
    
    private class ScaleUndoRedo implements UndoRedo {
        private final Spatial spatial;
        private final Vector3f beforeScale = new Vector3f();
        private final Vector3f afterScale = new Vector3f();
        
        public ScaleUndoRedo(Spatial spatial, Vector3f before, Vector3f after) {
            this.spatial = spatial;
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
