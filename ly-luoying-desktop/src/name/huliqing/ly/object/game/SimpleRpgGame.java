/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.env.ChaseCameraEnv;
import name.huliqing.luoying.object.game.SimpleGame;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIEventListener;
import name.huliqing.luoying.ui.state.PickListener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.view.LanPlayStateUI;
import name.huliqing.ly.view.shortcut.ShortcutManager;

/**
 *
 * @author huliqing
 */
public abstract class SimpleRpgGame extends SimpleGame implements UIEventListener {
    private static final Logger LOG = Logger.getLogger(SimpleRpgGame.class.getName());
    private final ActorService actorService = Factory.get(ActorService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 基本界面
    protected LanPlayStateUI ui;
    
    /** 当前游戏主角 */
    protected Entity player;
    
    private final List<Actor> tempActorsPicked = new ArrayList<Actor>();
    private final CollisionResults tempTerrainsPicked = new CollisionResults();
    
    // 场景相机
    private ChaseCameraEnv chaseCamera;
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // UI逻辑
        ui = new LanPlayStateUI();
        addLogic(ui);
        
        // 初始化事件绑定
        bindPickListener();
        
        // UI全局事件监听器，主要处理当UI被点击或拖动时不要让镜头跟着转动。
        UIState.getInstance().addEventListener(this);
    }

    @Override
    public void cleanup() {
        ShortcutManager.cleanup();
        UIState.getInstance().clearUI();
        super.cleanup(); 
    }
    
    /**
     * 获取当前游戏主角
     * @return 
     */
    public Entity getPlayer() {
        return player;
    }
    
    public void setPlayer(Entity player) {
        this.player = player;
        int teamId = gameService.getTeam(player);
        ui.getTeamView().clearPartners();
        ui.getTeamView().setMainActor(player);
        ui.getTeamView().setTeamId(teamId);
        if (teamId > 0) {
            List<Actor> actors = scene.getEntities(Actor.class, null);
            for (Actor a : actors) {
                ui.getTeamView().checkAddOrRemove(a);
            }
        }
        ChaseCameraEnv cce = getChaseCamera();
        if (cce != null) {
            cce.setChase(player.getSpatial());
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
            temp = gameService.findNearestEnemyExcept(player, distance);
            
            // 需要
            setTarget(temp);
        }
        
//        // 即使temp为null也可以攻击，这允许角色转入自动攻击（等待）状态
//        if (temp == null) {
//            addMessage(ResourceManager.get(ResConstants.COMMON_NO_TARGET), MessageType.notice);
//        }
        
        playNetwork.attack(player, temp);
    }
    
    private ChaseCameraEnv getChaseCamera() {
        // 从场景中找到“跟随”相机
        if (chaseCamera == null) {
           List<ChaseCameraEnv> cces = scene.getEntities(ChaseCameraEnv.class, null);
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
        for (Actor a : temps) {
            if (a.getSpatial().getWorldBound().intersects(ray)) {
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
            // 界面选择目标
            setTarget(actorsPicked.get(0));
//            // 允许角色面板显示宠物的包裹
//            // 注：只有debug时才允许显示其它角色的面板，否则会导致可以控制他人使用物品的问题
//            if (Config.debug || 
//                    (player != null && actorService.getOwner(actor) == player.getData().getUniqueId())) {
//                ui.getUserPanel().setActor(actor);
//            }
//            // 判断是否双击,如果是双击，则调用攻击
//            if (lastPicked == actor && Ly.getGameTime() - lastPickTime <= 400) {
//                attack();
//            }
//            lastPicked = actor;
//            lastPickTime = Ly.getGameTime();
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
        ChaseCameraEnv cce = getChaseCamera();
        if (cce != null) {
            cce.setEnabledRotation(enabled);
        }
    }

//    public MenuTool getMenuTool() {
//        return ui.getMenuTool();
//    }
}
