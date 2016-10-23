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
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.env.ChaseCameraEnv;
import name.huliqing.luoying.object.game.SimpleGame;
import name.huliqing.luoying.ui.state.PickListener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.view.LanPlayStateUI;

/**
 *
 * @author huliqing
 */
public abstract class SimpleRpgGame extends SimpleGame {
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    // 基本界面
    protected LanPlayStateUI ui;
    
    /** 当前游戏主角 */
    protected Entity player;
    
    /** 当前游戏主目标 */
    protected Entity target;
    
    private final List<Actor> tempActorsPicked = new ArrayList<Actor>();
    private final CollisionResults tempTerrainsPicked = new CollisionResults();
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // UI逻辑
        ui = new LanPlayStateUI();
        addLogic(ui);
        
        // ==== 3.初始化事件绑定
        bindPickListener();
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
        // 从场景中找到“跟随”相机
        List<ChaseCameraEnv> cces = scene.getEntities(ChaseCameraEnv.class, null);
        if (cces != null) {
            cces.get(0).setChase(player.getSpatial());
        } 
    }
    
    public Entity getTarget() {
        return target;
    }
    
    public void setTarget(Entity target) {
        this.target = target;
    }
    
    public void attack() {
        throw new UnsupportedOperationException();
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
}
