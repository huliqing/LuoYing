/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.log.ConsoleLogHandler;
import name.huliqing.luoying.log.LogFactory;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.entity.impl.ChaseCameraEntity;
import name.huliqing.luoying.object.game.SimpleGame;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIEventListener;
import name.huliqing.luoying.ui.state.PickListener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.LyConfig;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.manager.HUDManager;
import name.huliqing.ly.state.MenuTool;
import name.huliqing.ly.view.RpgMainUI;
import name.huliqing.ly.view.shortcut.ShortcutManager;
import name.huliqing.ly.view.talk.SpeakManager;
import name.huliqing.ly.view.talk.TalkManager;

/**
 *
 * @author huliqing
 */
public abstract class SimpleRpgGame extends SimpleGame implements UIEventListener{
    private static final Logger LOG = Logger.getLogger(SimpleRpgGame.class.getName());
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 基本界面
    protected RpgMainUI ui;
    
    /** 当前游戏主角 */
    protected Entity player;
    
    // 场景相机
    protected ChaseCameraEntity chaseCamera;

    private final List<Actor> tempActorsPicked = new ArrayList<Actor>();
    private final CollisionResults tempTerrainsPicked = new CollisionResults();
    
    // 最近一个选中的角色和最近一次选择的时间，主要用于处理双击选择攻击目标。
    private Actor lastPicked;
    private long lastPickTime;

    public SimpleRpgGame() {
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        
        // 事件绑定初始化
        bindPickListener();
        
        // UI全局事件监听器，主要处理当UI被点击或拖动时不要让镜头跟着转动。
        UIState.getInstance().addEventListener(this);
        
        // 用于支持角色“说话”、“谈话”的游戏逻辑
        addLogic(SpeakManager.getInstance());
        addLogic(TalkManager.getInstance());
        
        // UI消息管理器初始化
        HUDManager.init(this.app.getGuiNode());
        
        // 快捷方式功能初始化
        ShortcutManager.init();
        
        // 控制台消息处理器
        LogFactory.addHandler(new ConsoleLogHandler());
        
        // UI界面：头像、队伍、工具栏、攻击按钮
        // 这个UI需要依赖场景，所以放在这里初始化
        ui = new RpgMainUI();
        addLogic(ui);
    }

    @Override
    public void cleanup() {
        ShortcutManager.cleanup();
        UIState.getInstance().clearUI();
        HUDManager.cleanup();
        gameService.saveConfig(LyConfig.getConfigData());
        super.cleanup(); 
    }
    
    /**
     * 添加消息
     * @param message
     * @param messageType 
     */
    public void addMessage(String message, MessageType messageType) {
        HUDManager.showMessage(message, messageType.getColor());
    }
    
    /**
     * 获取当前游戏主角
     * @return 
     */
    public Entity getPlayer() {
        return player;
    }
    
    public void setPlayer(Entity newPlayer) {
        if (player != null) {
            gameService.setEssential(player, false);
            gameService.setPlayer(player, false);
        }
        player = newPlayer;
        gameService.setEssential(player, true);
        gameService.setPlayer(player, true);
        gameService.setAutoLogic(player, true);
        ui.getTeamView().setMainActor(newPlayer);
        ChaseCameraEntity cce = getChaseCamera();
        if (cce != null) {
            cce.setChase(newPlayer.getSpatial());
        }
    }
    
    public Entity getTarget() {
         return ui.getTargetFace().getActor();
    }
    
    public void setTarget(Entity target) {
        if (target != null) {
            ui.setTargetFace(target);
        }
    }
    
    public void attack() {
         if (player == null) 
            return;
        
        Entity temp = getTarget();
        
        // 没有目标，或者目标已经不在战场，则重新查找
        if (temp == null 
                || temp == player
                || temp.getScene() == null // 有可能角色已经被移出场景并已经被cleanup,所以这里需要比isDead优先判断
                || gameService.isDead(temp) 
                || !gameService.isEnemy(temp, player)
                ) {
            float distance = gameService.getViewDistance(player) * 2;
            temp = gameService.findNearestEnemies(player, distance);
            
            // 需要
            setTarget(temp);
        }

        if (temp != null) {
            gameNetwork.setFollow(player, -1);
            gameNetwork.setAutoAi(player, true);
            gameNetwork.setTarget(player, temp.getEntityId());
        }
        playNetwork.attack(player, temp);
    }
    
    protected ChaseCameraEntity getChaseCamera() {
        // 从场景中找到“跟随”相机
        if (chaseCamera == null) {
           List<ChaseCameraEntity> cces = scene.getEntities(ChaseCameraEntity.class, null);
           if (cces != null && !cces.isEmpty()) {
               chaseCamera = cces.get(0);
           } else {
               LOG.log(Level.WARNING, "Could not found any ChaseCamera from sence! sceneId={0}", scene.getData().getId());
           }
        }
        return chaseCamera;
    }
    
    /**
     * 绑定鼠标点击事件
     */
    private void bindPickListener() {
        UIState.getInstance().putPickListener("ObjectPick", new PickListener() {
            @Override
            public boolean pick(boolean isPressed, float tpf) {
                if (!isPressed) {
                    
                    // 1.----优先点选角色
                    tempActorsPicked.clear();
                    pickActors(tempActorsPicked);
                    if (onPickedActor(tempActorsPicked)) {
                        return true;
                    }
                    
                    // 2.---- 地面的选择
                    tempTerrainsPicked.clear();
                    List<TerrainEntity> terrains = scene.getEntities(TerrainEntity.class, null);
                    for (TerrainEntity te : terrains) {
                        PickManager.pick(app.getInputManager(), app.getCamera(), te.getSpatial(), tempTerrainsPicked);
                    }
                    if (onPickedTerrain(tempTerrainsPicked)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    private List<Actor> pickActors(List<Actor> store) {
        TempVars tv = TempVars.get();
        
        Vector2f v2d = tv.vect2d.set(app.getInputManager().getCursorPosition());
        Vector3f click3d = app.getCamera().getWorldCoordinates(v2d, 0, tv.vect1);
        Vector3f dir = app.getCamera().getWorldCoordinates(v2d, 1, tv.vect2)
                .subtractLocal(click3d).normalizeLocal();
        
        Ray ray = new Ray();
        ray.setOrigin(click3d);
        ray.setDirection(dir);
        
        List<Actor> temps = new ArrayList<Actor>();
        scene.getEntities(Actor.class, temps);
       BoundingVolume bv;
        for (Actor a : temps) {
            bv = a.getSpatial().getWorldBound();
            if (bv == null) {
                continue;
            }
            if (bv.intersects(ray)) {
                store.add(a);
            }
        }
        sortEntities(store);
        tv.release();
        return store;
    }
    
    // 根据entity的spatial与相机的距离，给entity排序
    private void sortEntities(List<? extends Entity> entities) {
        final Vector3f camLoc = app.getCamera().getLocation();
        Collections.sort(entities, new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                float dis1 = o1.getSpatial().getWorldTranslation().distanceSquared(camLoc);
                float dis2 = o2.getSpatial().getWorldTranslation().distanceSquared(camLoc);
                return dis1 < dis2 ? -1 : (dis1 > dis2 ? 1 : 0);
            }
        });
    }
    
    /**
     * 当角色被选中时的操作,如果方法返回true,则阻止后续的选择。如果略过当前
     * 角色的选择，则可返回false.
     * @param actorsPicked 被选中的角色。
     * @return 
     */
    protected boolean onPickedActor(List<Actor> actorsPicked) {
         if (!actorsPicked.isEmpty()) {
            Actor actorPicked = actorsPicked.get(0);
            // 界面选择目标
            setTarget(actorPicked);
            
            // 允许角色面板显示宠物的包裹
            // 注：只有debug时才允许显示其它角色的面板，否则会导致可以控制他人使用物品的问题
            if (Config.debug || 
                    (player != null && gameService.getOwner(actorPicked) == player.getData().getUniqueId())) {
                ui.getUserPanel().setActor(actorPicked);
            }
            // 判断是否双击,如果是双击，则调用攻击
            if (lastPicked == actorPicked && LuoYing.getGameTime() - lastPickTime <= 400) {
                attack();
            }
            lastPicked = actorPicked;
            lastPickTime = LuoYing.getGameTime();
            return true;
        }
        return false;
    }
    
    /**
     * 当选择了地面时该方法被调用。
     * @param terrainsPicked
     * @return 
     */
    protected boolean onPickedTerrain(CollisionResults terrainsPicked) {
        if (player != null && terrainsPicked.size() > 0) {
            // 当玩家点击走路时要去除跟随，并且去除自动AI
            gameNetwork.setFollow(player, -1);
            gameNetwork.setAutoAi(player, false);
            gameNetwork.playRunToPos(player, terrainsPicked.getClosestCollision().getContactPoint());
            return true;
        }
        return false;
    }
    
    @Override
    public void UIClick(UI ui, boolean isPressed, boolean dbclick) {
        setChaseEnabled(false);
    }

    @Override
    public void UIDragStart(UI ui) {
        setChaseEnabled(false);
    }
    
    @Override
    public void UIDragEnd(UI ui) {
        setChaseEnabled(true);
    }

    @Override
    public void UIRelease(UI ui) {
        setChaseEnabled(true);
    }
    
    private void setChaseEnabled(boolean enabled) {
        // 注意：这里只让相机停止旋转就可以,因为目前当UI打开时还不支持暂停(即打开
        // 用户界面时角色可能还在走动,镜头也可能还在跟随和旋转).
        // 为了避免在打开用户面板拖动一些UI组件时3D镜头跟着旋转的不舒服现象提供了这个方法．用于在打
        // 开UI面板时可以暂时关闭镜头跟随和旋转,在关闭UI后再重新开启，目前只要关闭旋转就可以，
        // 不能同时关闭跟随．
        // 因为如果人在走动，这时候如果按下鼠标不放,在关闭跟随的情况下会发现人
        // 一直远去(向前走动)，当相机重新跟随的时候，会突然移到角色旁边，过渡太过不自然．
        // 如果只关闭旋转，而保持跟随，就不会出现该现象，也就是该功能只是为了避免
        // 在按下鼠标拖动(UI)的时候同时出现3D镜头在旋转的问题．
        ChaseCameraEntity cce = getChaseCamera();
        if (cce != null) {
            cce.setEnabledRotation(enabled);
        }
    }
    
    /**
     * 获取界面菜单栏
     * @return 
     */
    public MenuTool getMenuTool() {
        return ui.getMenuTool();
    }
    
    /**
     * 切换显示当前界面的所有UI.注意:该方法将只影响当前已经存在的UI,对后续
     * 添加到场景中的UI不会有影响.也就是,如果想要只显示某个特殊UI,则先设置
     * setUIVisiable(false),然后再把特定UI添加到UI根节点中
     * @param visiable 
     */
    public void setUIVisiable(boolean visiable) {
        CullHint ch = visiable ? CullHint.Never : CullHint.Always;
        List<Spatial> children = UIState.getInstance().getUIRoot().getChildren();
        for (Spatial child : children) {
            child.setCullHint(ch);
        }
        if (ui.getTargetFace().getActor() == null) {
            ui.getTargetFace().setVisible(false);
        }
        // 快捷管理器中的“回收站”始终是关闭的，只有在拖动快捷方式时才可见
        ShortcutManager.setBucketVisible(false);
    }

    @Override
    public void setScene(Scene newScene) {
        // 切换场景前保存玩家data
        EntityData playerData = null;
        if (newScene != this.scene) {
            if (player != null) {
                player.updateDatas();
                playerData = player.getData();
                this.scene.removeEntity(player);
            }
        }
        
        super.setScene(newScene);
        
        // chaseCamera需要重新获取
        chaseCamera = null;
        
        // 重新载入data
        if (playerData != null) {
            Actor actor = Loader.load(playerData);
            scene.addEntity(actor);
            setPlayer(actor);
        }
        
    }
    
}
