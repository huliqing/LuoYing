/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.UserCommandNetwork;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.object.animation.Animation;
import name.huliqing.fighter.object.animation.BounceMotion;
import name.huliqing.fighter.object.animation.CurveMove;
import name.huliqing.fighter.object.animation.LinearGroup;
import name.huliqing.fighter.game.view.ShortcutSkillView;
import name.huliqing.fighter.game.view.ShortcutView;
import name.huliqing.fighter.save.ShortcutSave;
import name.huliqing.fighter.ui.UIUtils;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.state.UIState;

/**
 * 关于“快捷方式”的管理
 * @author huliqing
 */
public class ShortcutManager {
//    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    
    private static ShortcutRoot shortcutRoot;
    
    // “删除”图标
    private static UI delete = UIUtils.createMultView(
            128, 128, "Interface/icon/skull.png", "Interface/icon/shortcut.png");

    // “回收”图标
    private static UI recycle = UIUtils.createMultView(
            128, 128, "Interface/icon/recycle.png", "Interface/icon/shortcut.png");
    
    private static float space = 40;
    
    public static void init() {
        shortcutRoot = new ShortcutRoot();
        shortcutRoot.attachChild(delete.getDisplay());
        shortcutRoot.attachChild(recycle.getDisplay());
        
        // 设置z值会有BUG，导致shortcut点不了
//        shortcutRoot.setLocalTranslation(0, 0, -0.1f);
        // 把shortcut放在最前面，不要挡在后续弹出的窗口前。,后续要把shortcutRoot
        // 添加到UIState中进行统一处理。
        UIState.getInstance().addUI(shortcutRoot);
        
        float marginTop = Common.getSettings().getHeight() * 0.1f;
        
        delete.setToCorner(Corner.CT);
        delete.setMargin(0, marginTop, (delete.getWidth() + space) * 0.5f, 0);
        delete.setVisible(false);
        
        recycle.setToCorner(Corner.CT);
        recycle.setMargin((recycle.getWidth() + space) * 0.5f, marginTop, 0, 0);
        recycle.setVisible(false);
    }
    
    /**
     * 添加快捷方式（无动画）
     * @param shortcut 
     */
    public static void addShortcutNoAnim(ShortcutView shortcut) {
        shortcutRoot.addShortcut(shortcut);
    }
    
    /**
     * 添加快捷方式，带动画
     * @param shortcut 
     */
    public static void addShortcut(ShortcutView shortcut) {
        addShortcutNoAnim(shortcut);
        
        Animation anim = createShortcutAddAnimation(shortcut);
        AnimationManager.startAnimation(anim);
    }
    
    /**
     * 检查是否删除快捷方式或是删除物品
     * @param shortcut 
     */
    public static void checkProcess(ShortcutView shortcut) {
        if (recycle.isVisible() && isRecycle(shortcut)) {
            shortcutRoot.removeShortcut(shortcut);
            String objectName = ResourceManager.getObjectName(shortcut.getData());
            Common.getPlayState().addMessage(ResourceManager.get("common.shortcutRecycle", new String[] {objectName})
                    , MessageType.info);
            
        } else if (delete.isVisible() && isDelete(shortcut)) {
            // delete object
            Actor actor = shortcut.getActor();
            ProtoData data = shortcut.getData();
            
            boolean delSuccess = Factory.get(UserCommandNetwork.class).removeObject(actor, data.getId(), data.getTotal());
            String objectName = ResourceManager.getObjectName(shortcut.getData());
            
            if (delSuccess) {
                // delete shortcut
                shortcutRoot.removeShortcut(shortcut);
                Common.getPlayState().addMessage(ResourceManager.get("common.deleteSuccess", new String[] {objectName})
                    , MessageType.info);
            } else {
                Common.getPlayState().addMessage(ResourceManager.get("common.deleteFail", new String[] {objectName})
                    , MessageType.notice);
            }
        }
    }
    
    /**
     * 显示或隐藏“回收”图标和“删除”图标
     * @param visible 
     */
    public static void setBucketVisible(boolean visible) {
        recycle.setVisible(visible);
        delete.setVisible(visible);
        if (visible) {
            recycle.setOnTop();
            delete.setOnTop();
        }
    }
    
    /**
     * 设置当前界面上所有快捷方式的大小
     * @param size 
     */
    public static void setShortcutSize(float size) {
        if(shortcutRoot == null) 
            return;
        List<ShortcutView> shortcuts = shortcutRoot.getShortcuts();
        for (ShortcutView s : shortcuts) {
            s.setLocalScale(size);
        }
    }
    
    /**
     * 锁定或解锁当前已经存在的快捷方式，锁定后快捷方式不能再拖动。
     * 该方法不会影响后续添加的快捷方式。
     * @param locked 
     */
    public static void setShortcutLocked(boolean locked) {
        List<ShortcutView> shortcuts = shortcutRoot.getShortcuts();
        for (ShortcutView s : shortcuts) {
            s.setDragEnabled(!locked);
        }
    }
    
    /**
     * 清理掉界面所有快捷方式
     */
    public static void clearShortcuts() {
        shortcutRoot.clearShortcuts();
    }
    
    /**
     * 获取当前界面上所有快捷方式，用于保存到存档
     * @return 
     */
    public static ArrayList<ShortcutSave> getShortcutSaves() {
        ArrayList<ShortcutSave> result = new ArrayList<ShortcutSave>();
        List<ShortcutView> scs = shortcutRoot.getShortcuts();
        if (!scs.isEmpty()) {
            for (ShortcutView sc : scs) {
                ShortcutSave ss = new ShortcutSave();
                ss.setItemId(sc.getData().getProto().getId());
                ss.setX(sc.getWorldTranslation().x);
                ss.setY(sc.getWorldTranslation().y);
                result.add(ss);
            }
        }
        return result;
    }
    
    public static ShortcutView createShortcut(Actor actor, ProtoData data) {
        ShortcutView shortcut;
        if (data instanceof SkillData) {
            shortcut = new ShortcutSkillView(64, 64, actor, data);
        } else {
            shortcut = new ShortcutView(64, 64, actor, data); // default
        }
        return shortcut;
    }
    
    public static void cleanup() {
        if (shortcutRoot != null) {
            shortcutRoot.removeFromParent();
            shortcutRoot = null;
        }
    }
    
    /**
     * 判断是否要进行回收，当快捷方式拖放到“回收站”上时
     */
    private static boolean isRecycle(ShortcutView shortcut) {
        TempVars tv = TempVars.get();
        Vector3f bucketCenter = tv.vect1.set(recycle.getDisplay().getWorldTranslation());
        bucketCenter.setX(bucketCenter.x + recycle.getWidth() * 0.5f);
        bucketCenter.setY(bucketCenter.y + recycle.getHeight() * 0.5f);
        bucketCenter.setZ(0);

        Vector3f shortcutCenter = tv.vect2.set(shortcut.getWorldTranslation());
        shortcutCenter.setX(shortcutCenter.x + shortcut.getWidth() * 0.5f);
        shortcutCenter.setY(shortcutCenter.y + shortcut.getHeight() * 0.5f);
        shortcutCenter.setZ(0);
        
        boolean result = bucketCenter.distance(shortcutCenter) < recycle.getWidth() * 0.5f;
        tv.release();
        return result;
    }
    
    /**
     * 判断是否要进行删除，当快捷方式拖放到删除图标上时。
     * @param shortcut
     * @return 
     */
    private static boolean isDelete(ShortcutView shortcut) {
        TempVars tv = TempVars.get();
        Vector3f bucketCenter = tv.vect1.set(delete.getDisplay().getWorldTranslation());
        bucketCenter.setX(bucketCenter.x + delete.getWidth() * 0.5f);
        bucketCenter.setY(bucketCenter.y + delete.getHeight() * 0.5f);
        bucketCenter.setZ(0);

        Vector3f shortcutCenter = tv.vect2.set(shortcut.getWorldTranslation());
        shortcutCenter.setX(shortcutCenter.x + shortcut.getWidth() * 0.5f);
        shortcutCenter.setY(shortcutCenter.y + shortcut.getHeight() * 0.5f);
        shortcutCenter.setZ(0);
        
        boolean result = bucketCenter.distance(shortcutCenter) < delete.getWidth() * 0.5f;
        tv.release();
        return result;
    }
    
    /**
     * 为shortcut的添加功能创建一个动画效果
     * @param shortcut
     * @return 
     */
    private static Animation createShortcutAddAnimation(ShortcutView shortcut) {
        TempVars tv = TempVars.get();
        Vector2f startPos = Common.getCursorPosition();
        tv.vect1.set(startPos.x, startPos.y, shortcut.getLocalTranslation().z); // z值不能改变，否则setOnTop会不正确
        tv.vect2.set(Common.getSettings().getWidth() - 64 - 20
                , (Common.getSettings().getHeight() - 64) * 0.5f
                , shortcut.getLocalTranslation().z);
        
        // 弧线移动效果
        CurveMove cm = new CurveMove();
        cm.setStartPosition(tv.vect1);
        cm.setEndPosition(tv.vect2);
        cm.setHeight(150);
        cm.setAnimateTime(0.5f);
        cm.setUseScale(true);
        cm.setScale(0.3f, Factory.get(ConfigService.class).getShortcutSize());
        tv.release();
        
        // 弹跳效果
        BounceMotion bm = new BounceMotion();
        bm.setHeight(100);
        bm.setAnimateTime(0.5f);
        
        // 再弹跳效果
        BounceMotion bm2 = new BounceMotion();
        bm2.setHeight(30);
        bm2.setAnimateTime(0.25f);
        
        LinearGroup lg = new LinearGroup();
        lg.addAnimation(cm);
        lg.addAnimation(bm);
        lg.addAnimation(bm2);
        lg.setTarget(shortcut);
        
        return lg;
    }
    
    private static class ShortcutRoot extends Node {
        
        private final SafeArrayList<ShortcutView> shortcuts = 
                new SafeArrayList<ShortcutView>(ShortcutView.class);
        
        public void addShortcut(ShortcutView shortcut) {
            if (!shortcuts.contains(shortcut)) {
                shortcuts.add(shortcut);
                attachChild(shortcut);
            }
        }

        @Override
        public void updateLogicalState(float tpf) {
            super.updateLogicalState(tpf);
            for (ShortcutView s : shortcuts.getArray()) {
                s.updateShortcut(tpf);
            }
        }
        
        /**
         * 获取界面所有shortcut
         * @return 
         */
        public List<ShortcutView> getShortcuts() {
            return shortcuts;
        }
        
        public void removeShortcut(ShortcutView shortcut) {
            shortcut.cleanup();
            shortcut.removeFromParent();
            shortcuts.remove(shortcut);
        }
        
        /**
         * 清理所有shortcut.(注：不清理其它类型的子节点)
         */
        public void clearShortcuts() {
            for (ShortcutView s : shortcuts.getArray()) {
                s.removeFromParent();
            }
            shortcuts.clear();
        }
    }
}
