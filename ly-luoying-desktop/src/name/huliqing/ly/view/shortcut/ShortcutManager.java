/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.shortcut;

import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.save.ShortcutSave;
import name.huliqing.luoying.ui.UIUtils;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.luoying.layer.service.ObjectService;
import name.huliqing.luoying.object.shortcut.Shortcut;
import name.huliqing.ly.Config;

/**
 * 关于“快捷方式”的管理
 * @author huliqing
 */
public class ShortcutManager {
//    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    private final static ShortcutRoot SHORTCUT_ROOT = new ShortcutRoot();
    
    
    // “删除”图标
    private final static UI DELETE = UIUtils.createMultView(
            128, 128, "Interface/icon/skull.png", "Interface/icon/shortcut.png");

    // “回收”图标
    private final static UI RECYCLE = UIUtils.createMultView(
            128, 128, "Interface/icon/recycle.png", "Interface/icon/shortcut.png");
    
    private final static float SPACE = 40;
    
    /**
     * 快捷方式默认的宽度
     */
    public final static float SHORTCUT_SIZE_WIDTH = 64;
    
    /**
     * 快捷方式默认的高度
     */
    public final static float SHORTCUT_SIZE_HEIGHT = 64;
    
    public final static void init() {

        float marginTop = LuoYing.getSettings().getHeight() * 0.1f;
        
        // setMargin后再setToCorner,因为setToCorner会立即计算位置
        DELETE.setMargin(0, marginTop, (DELETE.getWidth() + SPACE) * 0.5f, 0);
        DELETE.setToCorner(Corner.CT);
        DELETE.setVisible(false);
        
        RECYCLE.setMargin((RECYCLE.getWidth() + SPACE) * 0.5f, marginTop, 0, 0);
        RECYCLE.setToCorner(Corner.CT);
        RECYCLE.setVisible(false);
        
        UIState.getInstance().addUI(DELETE.getDisplay());
        UIState.getInstance().addUI(RECYCLE.getDisplay());
        
    }
    
    /**
     * 添加快捷方式，带动画
     * @param actor
     * @param data
     */
    public static void addShortcut(Actor actor, ObjectData data) {
        ConfigService configService = Factory.get(ConfigService.class);
        float size = Config.getShortcutSize();
        if (size < 0.1f) {
            size = 0.1f;
        }
        Shortcut shortcut = createShortcut(actor, data
                , new Vector3f()
                , SHORTCUT_SIZE_WIDTH * size
                , SHORTCUT_SIZE_HEIGHT * size
                , true);
        SHORTCUT_ROOT.addShortcut(shortcut);
   
        // remove20161001不再依赖于这个动画包
//        Animation anim = createShortcutAddAnimation(shortcut);
//        AnimationManager.getInstance().startAnimation(anim);
    }
    
    /**
     * 载入快捷方式给指定的角色。
     * @param ss
     * @param player 
     */
    public static void loadShortcut(List<ShortcutSave> ss, Actor player) {
        if (ss == null || ss.isEmpty())
            return;
        ConfigService configService = Factory.get(ConfigService.class);
        SkillService skillService = Factory.get(SkillService.class);
        ObjectService protoService = Factory.get(ObjectService.class);
        
        float shortcutSize = Config.getShortcutSize();
        for (ShortcutSave s : ss) {
            String objectId = s.getObjectId();
            ObjectData data = protoService.getData(player, objectId);
            if (data == null) {
                data = DataFactory.createData(objectId);
            }
            
            // 防止物品被删除
            if (data == null) {
                continue;
            }
            
            // 包裹中只允许存放限定的物品
            if ((!(data instanceof ItemData)) 
                    && (!(data instanceof SkinData)) 
                    && (!(data instanceof SkillData))
                    ) {
                continue;
            }
            
            // 由于skill的创建过程比较特殊，SkillData只有在创建了Skill之后
            // 才能获得skillType,所以不能直接使用createProtoData方式获得的SkillData
            // 这会找不到SkillData中的skillType,所以需要从角色身上重新找回SkillData
            if (data instanceof SkillData) {
                data = skillService.getSkill(player, data.getId()).getData();
            }
            
            Shortcut shortcut = createShortcut(player, data
                    , new Vector3f(s.getX(), s.getY(), 0)
                    , SHORTCUT_SIZE_WIDTH * shortcutSize
                    , SHORTCUT_SIZE_HEIGHT * shortcutSize
                    , true);
            SHORTCUT_ROOT.addShortcut(shortcut);
        }
        
        // 如果系统设置锁定快捷方式，则锁定它
        if (Config.isShortcutLocked()) {
            ShortcutManager.setShortcutLocked(true);
        }
    }
    
    private static Shortcut createShortcut(Actor actor, ObjectData data
            , Vector3f location, float width, float height, boolean dragEnabled) {
        Shortcut shortcut = null;
        if (data instanceof SkillData) {
            shortcut = new SkillShortcut();
            
        } else if (data instanceof SkinData) {
            shortcut = new SkinShortcut();
            
        } else if (data instanceof ItemData) {
            shortcut = new ItemShortcut();
            
        } else {
            throw new UnsupportedOperationException("Could not createShortcut for objectData, objectId=" + data.getId());
        }
        shortcut.setActor(actor);
        shortcut.setObjectData(data);
        shortcut.setWidth(width);
        shortcut.setHeight(height);
        shortcut.setLocation(location);
        shortcut.setDragEnagled(dragEnabled);
        return shortcut;
        
    }
        
    /**
     * 设置当前界面上所有快捷方式的大小
     * @param size 
     */
    public static void setShortcutSize(float size) {
        List<Shortcut> shortcuts = SHORTCUT_ROOT.getShortcuts();
        for (Shortcut s : shortcuts) {
            s.setWidth(SHORTCUT_SIZE_WIDTH * size);
            s.setHeight(SHORTCUT_SIZE_HEIGHT * size);
        }
    }
    
    /**
     * 锁定或解锁当前已经存在的快捷方式，锁定后快捷方式不能再拖动。
     * 该方法不会影响后续添加的快捷方式。
     * @param locked 
     */
    public static void setShortcutLocked(boolean locked) {
        List<Shortcut> shortcuts = SHORTCUT_ROOT.getShortcuts();
        for (Shortcut s : shortcuts) {
            s.setDragEnagled(!locked);
        }
    }
    
    /**
     * 检查是否删除快捷方式或是删除物品
     * @param shortcut 
     */
    public static void checkProcess(Shortcut shortcut) {
        if (RECYCLE.isVisible() && isRecycle(shortcut)) {
            SHORTCUT_ROOT.removeShortcut(shortcut);
   
            // remove20161010
//            String objectName = ResourceManager.getObjectName(shortcut.getObjectData());
//            Ly.getPlayState().addMessage(ResourceManager.get("common.shortcutRecycle", new String[] {objectName})
//                    , MessageType.info);
            
        } else if (DELETE.isVisible() && isDelete(shortcut)) {
            shortcut.removeObject();
        }
    }
    
    /**
     * 显示或隐藏“回收”图标和“删除”图标
     * @param visible 
     */
    public static void setBucketVisible(boolean visible) {
        RECYCLE.setVisible(visible);
        DELETE.setVisible(visible);
        if (visible) {
            RECYCLE.setOnTop();
            DELETE.setOnTop();
        }
    }
    
    /**
     * 获取当前界面上所有快捷方式，用于保存到存档
     * @return 
     */
    public static ArrayList<ShortcutSave> getShortcutSaves() {
        ArrayList<ShortcutSave> result = new ArrayList<ShortcutSave>();
        List<Shortcut> scs = SHORTCUT_ROOT.getShortcuts();
        if (!scs.isEmpty()) {
            for (Shortcut sc : scs) {
                ShortcutSave ss = new ShortcutSave();
                ss.setObjectId(sc.getObjectData().getId());
                ss.setX(sc.getLocation().x);
                ss.setY(sc.getLocation().y);
                result.add(ss);
            }
        }
        return result;
    }

    /**
     * 清理界面上的所有快捷方式
     */
    public static void cleanup() {
        if (SHORTCUT_ROOT != null) {
            SHORTCUT_ROOT.clearShortcuts();
        }
    }
    
    /**
     * 判断是否要进行回收，当快捷方式拖放到“回收站”上时
     */
    private static boolean isRecycle(Shortcut shortcut) {
        TempVars tv = TempVars.get();
        Vector3f bucketCenter = tv.vect1.set(RECYCLE.getDisplay().getWorldTranslation());
        bucketCenter.setX(bucketCenter.x + RECYCLE.getWidth() * 0.5f);
        bucketCenter.setY(bucketCenter.y + RECYCLE.getHeight() * 0.5f);
        bucketCenter.setZ(0);

        Vector3f shortcutCenter = tv.vect2.set(shortcut.getLocation());
        shortcutCenter.setX(shortcutCenter.x + shortcut.getWidth() * 0.5f);
        shortcutCenter.setY(shortcutCenter.y + shortcut.getHeight() * 0.5f);
        shortcutCenter.setZ(0);
        
        boolean result = bucketCenter.distance(shortcutCenter) < RECYCLE.getWidth() * 0.5f;
        tv.release();
        return result;
    }
    
    /**
     * 判断是否要进行删除，当快捷方式拖放到删除图标上时。
     * @param shortcut
     * @return 
     */
    private static boolean isDelete(Shortcut shortcut) {
        TempVars tv = TempVars.get();
        Vector3f bucketCenter = tv.vect1.set(DELETE.getDisplay().getWorldTranslation());
        bucketCenter.setX(bucketCenter.x + DELETE.getWidth() * 0.5f);
        bucketCenter.setY(bucketCenter.y + DELETE.getHeight() * 0.5f);
        bucketCenter.setZ(0);

        Vector3f shortcutCenter = tv.vect2.set(shortcut.getLocation());
        shortcutCenter.setX(shortcutCenter.x + shortcut.getWidth() * 0.5f);
        shortcutCenter.setY(shortcutCenter.y + shortcut.getHeight() * 0.5f);
        shortcutCenter.setZ(0);
        
        boolean result = bucketCenter.distance(shortcutCenter) < DELETE.getWidth() * 0.5f;
        tv.release();
        return result;
    }
    
    /**
     * 快捷方式的根节点<br>
     * 1.这个节点并不直接保持快捷方式等子节点。只保留对其引用。<br>
     * 2.快捷方式节点是直接保存在UIState中的.<br>
     */
    private static class ShortcutRoot {
        
        private final SafeArrayList<Shortcut> shortcuts = new SafeArrayList<Shortcut>(Shortcut.class);
        
        public void addShortcut(Shortcut shortcut) {
            if (!shortcuts.contains(shortcut)) {
                shortcut.initialize();
                shortcuts.add(shortcut);
                UIState.getInstance().addUI(shortcut.getView());
            }
        }

        /**
         * 获取界面所有shortcut
         * @return 
         */
        public List<Shortcut> getShortcuts() {
            return shortcuts;
        }
        
        public void removeShortcut(Shortcut shortcut) {
            shortcuts.remove(shortcut);
            shortcut.cleanup();
        }
        
        /**
         * 清理所有shortcut.(注：不清理其它类型的子节点)
         */
        public void clearShortcuts() {
            for (Shortcut s : shortcuts.getArray()) {
                removeShortcut(s);
            }
        }
    }
}
