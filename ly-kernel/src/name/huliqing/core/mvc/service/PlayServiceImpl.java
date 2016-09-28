/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.app.Application;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.data.GameData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.state.GameState;
import name.huliqing.core.state.LoadingState;
import name.huliqing.core.state.PlayState;
import name.huliqing.core.state.SimpleGameState;
import name.huliqing.core.state.StoryServerPlayState;
import name.huliqing.core.object.shortcut.ShortcutManager;
import name.huliqing.core.object.NetworkObject;
import name.huliqing.core.object.PlayObject;
import name.huliqing.core.object.anim.Anim;
import name.huliqing.core.object.bullet.Bullet;
import name.huliqing.core.object.bullet.BulletManager;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.effect.EffectManager;
import name.huliqing.core.object.scene.Scene;
import name.huliqing.core.object.view.View;
import name.huliqing.core.save.SaveHelper;
import name.huliqing.core.save.SaveStory;

/**
 *
 * @author huliqing
 */
public class PlayServiceImpl implements PlayService {
    
    private ActorService actorService;
    private SkillService skillService;
    private GameService gameService;
    private LogicService logicService;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
        gameService = Factory.get(GameService.class);
        skillService = Factory.get(SkillService.class);
        logicService = Factory.get(LogicService.class);
    }
    
    @Override
    public void addPlayObject(PlayObject playObject) {
        LY.getPlayState().addObject(playObject, false);
    }

    @Override
    public void removePlayObject(PlayObject playObject) {
        LY.getPlayState().removeObject(playObject);
    }
    
    @Override
    public void addObject(Object object, boolean gui) {
        if (object instanceof Actor) {
            addActor((Actor) object);
            return;
        }
        
        if (object instanceof Effect) {
            addEffect((Effect) object);
            return;
        }
        
        if (object instanceof Bullet) {
            addBullet((Bullet) object);
            return;
        }
        
        if (object instanceof View) {
            addView((View) object);
            return;
        }
        
        // 最后判断是不是Actor
        if (object instanceof Spatial) {
            Spatial ss = (Spatial) object;
            if (ss.getControl(Actor.class) != null) {
                addActor(ss.getControl(Actor.class));
                return;
            }
        }
        
        LY.getPlayState().addObject(object, gui);
    }

    @Override
    public void addObject(Object object) {
        addObject(object, false);
    }

    @Override
    public void addObjectGui(Object object) {
        addObject(object, true);
    }
    
    @Override
    public void removeObject(Object object) {
        PlayState ps = LY.getPlayState();
        if (ps == null)
            return;
        ps.removeObject(object);
    }
    
    @Override
    public void addActor(Actor actor) {
        LY.getPlayState().addObject(actor, false);
    }

    @Override
    public void addSimplePlayer(Actor actor) {
        logicService.resetPlayerLogic(actor);
        // 暂时以1作为默认分组
        actorService.setTeam(actor, 1);
        actorService.setPlayer(actor, true);
        skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
        addActor(actor);
    }

    @Override
    public void addEffect(Effect effect) {
        EffectManager.getInstance().addEffect(effect);
    }

    @Override
    public void addBullet(Bullet bullet) {
        BulletManager.getInstance().addBullet(bullet);
    }
    
    @Override
    public void addView(View view) {
        LY.getPlayState().addObject(view, true);
    }

    @Override
    public void addAnimation(Anim animation) {
        LY.getPlayState().addObject(animation, false);
        animation.start();
    }

    @Override
    public void removeAnimation(Anim animation) {
        LY.getPlayState().removeObject(animation);
    }

    @Override
    public void addMessage(String message, MessageType type) {
        LY.getPlayState().addMessage(message, type);
    }

    @Override
    public void addMessage(Actor actor, String message, MessageType type) {
        LY.getPlayState().addMessage(message, type);
    }

    @Override
    public void addShortcut(Actor actor, ObjectData data) {        
        ShortcutManager.addShortcut(actor, data);
    }
    
    @Override
    public List<Actor> findAllActor() {
        PlayState ps = LY.getPlayState();
        if (ps != null) {
            return ps.getActors();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Actor> findOrganismActors(int group, List<Actor> store) {
        if (store == null) {
            store = new ArrayList<Actor>();
        }
        List<Actor> all = LY.getPlayState().getActors();
        for (Actor actor : all) {
            if (actorService.getGroup(actor) == group) {
                // 一般质量大于０(能移动)的都可视为有机生命
                if (actorService.getMass(actor) > 0) {
                    store.add(actor);
                }
            }
        }
        return store;
    }
    
    @Override
    public Actor findActor(String id) {
        List<Actor> actors = LY.getPlayState().getActors();
        if (actors == null || actors.isEmpty())
            return null;
        for (Actor actor : actors) {
            if (actor.getData().getId().equals(id)) {
                return actor;
            }
        }
        return null;
    }
    
    @Override
    public Actor findActor(long actorUniqueId) {
        if (actorUniqueId <= 0) {
            return null;
        }
        List<Actor> actors = LY.getPlayState().getActors();
        if (actors == null || actors.isEmpty())
            return null;
        for (Actor actor : actors) {
            if (actor.getData().getUniqueId() == actorUniqueId) {
                return actor;
            }
        }
        return null;
    }

    @Override
    public List<View> findAllViews() {
        return LY.getPlayState().getViews();
    }

    @Override
    public View findView(long uniqueId) {
        List<View> views = LY.getPlayState().getViews();
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i).getData().getUniqueId() == uniqueId) {
                return views.get(i);
            }
        }
        return null;
    }

    @Override
    public NetworkObject findSyncObject(long objectId) {
        return LY.getPlayState().getSyncObjects(objectId);
    }
    
    @Override
    public NavMeshPathfinder createPathfinder() {
//        return Common.getPlayState().createPathfinder();
        // 暂不支持寻路
        return null;
    }

    @Override
    public Spatial getTerrain() {
        PlayState playState = LY.getPlayState();
        if (playState == null || playState.getGameState().getGame() == null) 
            return null;
        
        Scene scene = playState.getGameState().getScene();
        if (scene != null) {
            return scene.getTerrain();
        }
        return null;
    }
    
    @Override
    public float getTerrainHeight(float x, float z) {
        Spatial terrain = getTerrain();
        float height = 0;
        if (terrain == null) {
            return height;
        }
        
        // remove20160705
//        TempVars tv = TempVars.get();
//        Temp tp = Temp.get();
//        Vector3f start = tv.vect1.set(x, 0, z);
//        Vector3f dir = tv.vect2.set(x, 1, z).subtractLocal(start).normalizeLocal();
//        tp.ray.setOrigin(start);
//        tp.ray.setDirection(dir);
//        
//        CollisionResults results = tp.results;
//        results.clear();
//        terrain.collideWith(tp.ray, results);
//        if (results.size() > 0) {
//            height = results.getFarthestCollision().getContactPoint().getY();
//        }
//        tv.release();
//        tp.release();
//        return height;

        TempVars tv = TempVars.get();
        Vector3f origin = tv.vect1.set(x, Float.MIN_VALUE, z);
        Vector3f dir = tv.vect2.set(0,1,0);
        Ray ray = new Ray(origin, dir);
        CollisionResults results = new CollisionResults();
        terrain.collideWith(ray, results);
        if (results.size() > 0) {
            height = results.getFarthestCollision().getContactPoint().getY();
        }
        tv.release();
        return height;
    }

    @Override
    public Vector3f getActorForwardPosition(Actor actor, float distance, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        TempVars tv = TempVars.get();
        Vector3f dirByLength = tv.vect1.set(actorService.getViewDirection(actor))
                .normalizeLocal().multLocal(distance);
        store.set(actor.getSpatial().getWorldTranslation()).addLocal(dirByLength);
        store.y = getTerrainHeight(store.x, store.z);
        tv.release();
        
        return store;
    }

    @Override
    public Actor getPlayer() {
        return LY.getPlayState().getPlayer();
    }

    @Override
    public Vector3f getRandomTerrainPoint(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        Spatial terrain = getTerrain();
        if (terrain == null) {
            store.set(0, 0, 0);
            return store;
        }
        BoundingBox bb = (BoundingBox) terrain.getWorldBound();
        float xe = bb.getXExtent() * 0.5f; // x 0.5防止掉出边界
        float ze = bb.getZExtent() * 0.5f;
        
        store.set(FastMath.nextRandomFloat() * xe * 2 - xe, 0, FastMath.nextRandomFloat() * ze * 2 - ze);
        store.setY(getTerrainHeight(store.x, store.z));
        return store;
        
        // remove20160203
//        return getTerrainPoint(
//                FastMath.nextRandomFloat() * xe * 2 - xe
//                , FastMath.nextRandomFloat() * ze * 2 - ze
//                , store);
        
    }

    @Override
    public void moveObject(Actor actor, Vector3f position) {
        actorService.setLocation(actor, position);
    }

    @Override
    public Scene getScene() {
        PlayState ps = LY.getPlayState();
        if (ps == null || ps.getGameState().getGame() == null) 
            return null;
        
        return ps.getGameState().getScene();
    }

    @Override
    public boolean isInScene(Actor actor) {
        return LY.getPlayState().isInScene(actor.getSpatial());
    }

    @Override
    public Actor getTarget() {
        return LY.getPlayState().getTarget();
    }

    @Override
    public void setTarget(Actor target) {
        LY.getPlayState().setTarget(target);
    }

    @Override
    public Vector3f moveUpToTerrain(Vector3f position) {
        float terrainHeight = getTerrainHeight(position.x, position.z);
        if (terrainHeight > position.y) {
            // 注意：如果该点已经在地面上，则不要作多余的修改
            position.setY(terrainHeight);
        }
        return position;
    }

    @Override
    public float getScreenWidth() {
        return LY.getSettings().getWidth();
    }

    @Override
    public float getScreenHeight() {
        return LY.getSettings().getHeight();
    }

    @Override
    public void setMainPlayer(Actor actor) {
        LY.getPlayState().setPlayer(actor);
    }

    @Override
    public boolean isInScene(Spatial spatial) {
        return LY.getPlayState().isInScene(spatial);
    }

    @Override
    public void saveCompleteStage(int stage) {
        PlayState ps = LY.getPlayState();
        if (!(ps instanceof StoryServerPlayState)) {
            return;
        }
        StoryServerPlayState ss = (StoryServerPlayState) ps;
        SaveStory saveStory = ss.getSaveStory();
        if (saveStory == null) {
            saveStory = new SaveStory();
        }
        saveStory.setStoryCount(stage);
        saveStory.setPlayer(ps.getPlayer().getData());
        SaveHelper.saveStoryLast(saveStory);
    }

    @Override
    public Application getApplication() {
        return LY.getApp();
    }

    @Override
    public void changeGame(String gameId) {
        changeGame(gameService.loadGameData(gameId));
    }

    @Override
    public void changeGame(GameData gameData) {
        GameState gameState = new SimpleGameState(gameData);
        LoadingState loadingState = new LoadingState(LY.getPlayState(), gameState);
        LY.getApp().getStateManager().attach(loadingState);
    }

    @Override
    public String getGameId() {
        PlayState ps = LY.getPlayState();
        if (ps == null)
            return null;
        
        GameState gameState = ps.getGameState();
        if (gameState == null)
            return null;
        
        return gameState.getGame().getData().getId();
    }

    
}
