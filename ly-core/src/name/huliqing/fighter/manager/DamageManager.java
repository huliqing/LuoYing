/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.object.AbstractPlayObject;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.effect.AbstractEffect;
import name.huliqing.fighter.utils.GeometryUtils;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.state.UIState;

/**
 * 在目标角色上展示伤害数字或其它信息。
 * @author huliqing
 */
public class DamageManager extends AbstractPlayObject {
    
    private final static DamageManager INSTANCE = new DamageManager();
    
    /**
     * 定义一个最远距离，当目标距离镜头多远时将不展示动态信息,这个目的是为了避
     * 免资源浪费。
     */
    private final static float MAX_DISTANCE_SQUARED = 40 * 40;
    
    /**
     * 缓存达到该数时进行提示
     */
    private final static int MAX_CACHE_SIZE = 50;
    
    /**
     * 缓存动态信息
     */
    private final static List<DynamicText> CACHES = new ArrayList<DynamicText>();
    
    public enum ResistType {
        /** 抵抗:这表示抗性是机会性的 */
        resisted,
        
        /** 免疫:这表示抗性是绝对的 */
        immunized,
    }
    
    private DamageManager() {}
    
    public static DamageManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(float tpf) {
        // ignore
    }
    
    @Override
    public void cleanup() {
        CACHES.clear();
        super.cleanup();
    }
 
    /**
     * 动态显示状态抵抗结果
     * @param target
     * @param type 
     */
    public void showResist(Actor target, ResistType type) {
        if (!checkDisplay(target)) {
            return;
        }
        ColorRGBA color;
        String mess;
        switch (type) {
            case resisted:
                color = ColorRGBA.Yellow;
                mess = ResourceManager.get("resist.resisted");
                break;
            case immunized:
                color = ColorRGBA.Gray;
                mess = ResourceManager.get("resist.immunized");
                break;
            default :
                color = ColorRGBA.White;
                mess = "Unknow Resist";
        }
        display(target, mess, color, 1);
    }
    
    /**
     * 动态效果显示数字在屏幕
     * @param victim 目标角色
     * @param value 目标值
     */
    public void show(Actor victim, float value) {
        int intValue = (int) value;
        if (intValue == 0 || !checkDisplay(victim)) {
            return;
        }
        
        display(victim, String.valueOf(intValue), intValue < 0 ? ColorRGBA.Red : ColorRGBA.Green, 0);
    }
    
    /**
     * 在目标角色头顶上展示信息
     * @param target
     * @param text 
     */
    private void display(Actor target, String text, ColorRGBA color, int level) {
        DynamicText dt = getFromCache();
        Common.getApp().getGuiNode().attachChild(dt);
        Vector3f pos = dt.text.getLocalTranslation();
        pos.set(target.getModel().getWorldTranslation()).addLocal(0, 2.5f, 0);
        Common.getApp().getCamera().getScreenCoordinates(pos, pos);
        dt.startPos.set(pos);
        dt.text.setText(text);
        dt.text.setFontColor(color);
        dt.totalAnimDistance = 50 + getFontSize()  * 0.55f * level;
        dt.start();
    }
    
    /**
     * 检查是否需要显示伤害或输出信息，距离太远或在镜头之外的信息应该剔除掉,
     * 避免资源浪费
     * @param target
     * @return 
     */
    private boolean checkDisplay(Actor target) {
        // 在距离太远也不显示
        Camera cam = Common.getApp().getCamera();
        if (cam.getLocation().distanceSquared(target.getModel().getWorldTranslation()) > MAX_DISTANCE_SQUARED) {
            if (Config.debug) {
                Logger.getLogger(DamageManager.class.getName()).log(Level.INFO
                        , "The distance is too far, do not need to show damageText, target={0}", target.getModel().getName());
            }
            return false;
        }
        
        // 不在镜头内也不显示
        if (!GeometryUtils.intersectCamera(target.getModel())) {
            if (Config.debug) {
                Logger.getLogger(DamageManager.class.getName()).log(Level.INFO
                        , "Target not in camera, do not need to show damageText, target={0}", target.getModel().getName());
            }
            return false;
        }
        return true;
    }
    
    private float getFontSize() {
        return Common.getSettings().getHeight() * 0.15f;
    }
    
    public void showDebugInfo() {
        if (Config.debug) {
            Logger.getLogger(DamageManager.class.getName())
                    .log(Level.INFO, "DamageManager cache DamageText size={0}", CACHES.size());
        }
    }
    
    private DynamicText getFromCache() {
        if (CACHES.size() > MAX_CACHE_SIZE) {
            // 如果出错，防止部分资源意外没有释放，则提出警告
            Logger.getLogger(DamageManager.class.getName())
                    .log(Level.WARNING, "DamageText cache to more >>>>>>>> size={0}"
                    , CACHES.size());
        }
        
        // 从缓存获取
        for (DynamicText dt : CACHES) {
            if (dt.isEnd()) {
                return dt;
            }
        }
        
        // new one
        DynamicText dt =  new DynamicText("", getFontSize());
        CACHES.add(dt);
        
        return dt;
    }
    
    // ==== private class
    
    private class DynamicText extends AbstractEffect {
        private Text text;

        // 总的移动距离
        private float totalAnimDistance = 50;
        // 总的移动时间
        private static final float ANIM_TIME = 0.3f;
        // 使用正弦计算减速效果，移动减速和缩放减速
        private static final float MAX_ANGLE = 140;
        // 动画结速后经过多久才清理掉该效果
        private static final float DELAY_END = 0.5f;
        private final static float TEMP_VALUE = ANIM_TIME / MAX_ANGLE / FastMath.DEG_TO_RAD;
        
        private Vector3f startPos = new Vector3f();

        public DynamicText(String text, float fontSize) {
            super();
            this.data = new EffectData();
            this.text = new Text(text);
            this.text.setAlignment(BitmapFont.Align.Center);
            this.text.setWidth(300);
            this.text.setFontSize(fontSize);
            this.data.setPhaseTimeDisplay(ANIM_TIME + DELAY_END);
        }

        @Override
        protected void doInit() {
            super.doInit(); 
            UIState.getInstance().addUI(this.text);
        }

        @Override
        protected void updatePhaseAll(float tpf) {
            if (data.getTimeUsed() <= ANIM_TIME) {
                float factor = FastMath.sin(data.getTimeUsed() / TEMP_VALUE);
                text.setLocalScale(factor * factor);
                Vector3f pos = text.getLocalTranslation();
                pos.set(startPos);
                pos.addLocal(-0.5f * text.getWidth() * text.getLocalScale().x
                        , factor * totalAnimDistance
                        , -1f); // -1可以不让弹出的数字盖在其它UI上，比如当打开包裹或地图时
                text.setLocalTranslation(pos);
            }
        }

        @Override
        public void cleanup() {
            this.text.removeFromParent();
            super.cleanup(); 
        }

    } // end damage text class
    
}
