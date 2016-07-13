/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.app.Application;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ActorControl;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.state.GameState;
import name.huliqing.fighter.game.state.LoadingState;
import name.huliqing.fighter.game.state.PlayState;
import name.huliqing.fighter.game.state.SimpleGameState;
import name.huliqing.fighter.game.state.StoryServerPlayState;
import name.huliqing.fighter.manager.ShortcutManager;
import name.huliqing.fighter.object.NetworkObject;
import name.huliqing.fighter.object.PlayObject;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.bullet.Bullet;
import name.huliqing.fighter.object.effect.Effect;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.view.View;
import name.huliqing.fighter.save.SaveHelper;
import name.huliqing.fighter.save.SaveStory;

/**
 *
 * @author huliqing
 */
public class PlayServiceImpl implements PlayService {
    
    private StateService stateService;
    private ActorService actorService;
    private ItemService itemService;
    private SkillService skillService;
    private GameService gameService;
    private LogicService logicService;
    
    @Override
    public void inject() {
        stateService = Factory.get(StateService.class);
        actorService = Factory.get(ActorService.class);
        itemService = Factory.get(ItemService.class);
        gameService = Factory.get(GameService.class);
        skillService = Factory.get(SkillService.class);
        logicService = Factory.get(LogicService.class);
    }
    
    @Override
    public void addPlayObject(PlayObject playObject) {
        Common.getPlayState().addObject(playObject, false);
    }

    @Override
    public void removePlayObject(PlayObject playObject) {
        Common.getPlayState().removeObject(playObject);
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
            if (ss.getControl(ActorControl.class) != null) {
                addActor(ss.getControl(ActorControl.class));
                return;
            }
        }
        
        Common.getPlayState().addObject(object, gui);
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
        PlayState ps = Common.getPlayState();
        if (ps == null)
            return;
        ps.removeObject(object);
    }
    
    @Override
    public void addActor(Actor actor) {
        Common.getPlayState().addObject(actor, false);
    }

    @Override
    public void addSimplePlayer(Actor actor) {
        logicService.resetPlayerLogic(actor);
        // 暂时以1作为默认分组
        actorService.setTeam(actor, 1);
        skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);
        actor.setPlayer(true);
        addActor(actor);
    }

    @Override
    public void addEffect(Effect effect) {
        Common.getPlayState().addObject(effect.getDisplay(), false);
        effect.start();
    }

    @Override
    public void addBullet(Bullet bullet) {
        Common.getPlayState().addObject(bullet.getDisplay(), false);
        bullet.start();
    }
    
    @Override
    public void addView(View view) {
        Common.getPlayState().addObject(view, true);
    }

    @Override
    public void addAnimation(Anim animation) {
        Common.getPlayState().addObject(animation, false);
        animation.start();
    }

    @Override
    public void removeAnimation(Anim animation) {
        Common.getPlayState().removeObject(animation);
    }

    @Override
    public void addMessage(String message, MessageType type) {
        Common.getPlayState().addMessage(message, type);
    }

    @Override
    public void addMessage(Actor actor, String message, MessageType type) {
        Common.getPlayState().addMessage(message, type);
    }

    @Override
    public void addShortcut(Actor actor, ProtoData data) {        
        ShortcutManager.addShortcut(ShortcutManager.createShortcut(actor, data));
    }
    
    @Override
    public List<Actor> findAllActor() {
        PlayState ps = Common.getPlayState();
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
        List<Actor> all = Common.getPlayState().getActors();
        for (Actor actor : all) {
            if (actorService.getGroup(actor) == group) {
                // 一般质量大于０(能移动)的都可视为有机生命
                if (actor.getMass() > 0) {
                    store.add(actor);
                }
            }
        }
        return store;
    }
    
    @Override
    public Actor findActor(String id) {
        List<Actor> actors = Common.getPlayState().getActors();
        if (actors == null || actors.isEmpty())
            return null;
        for (Actor actor : actors) {
            if (actor.getData().getProto().getId().equals(id)) {
                return actor;
            }
        }
        return null;
    }
    
    @Override
    public Actor findActor(long actorUniqueId) {
        List<Actor> actors = Common.getPlayState().getActors();
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
        return Common.getPlayState().getViews();
    }

    @Override
    public View findView(long uniqueId) {
        List<View> views = Common.getPlayState().getViews();
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i).getData().getUniqueId() == uniqueId) {
                return views.get(i);
            }
        }
        return null;
    }

    @Override
    public NetworkObject findSyncObject(long objectId) {
        return Common.getPlayState().getSyncObjects(objectId);
    }
    
    @Override
    public NavMeshPathfinder createPathfinder() {
//        return Common.getPlayState().createPathfinder();
        // 暂不支持寻路
        return null;
    }

    @Override
    public Spatial getTerrain() {
        PlayState playState = Common.getPlayState();
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
        Vector3f dirByLength = tv.vect1.set(actor.getViewDirection())
                .normalizeLocal().multLocal(distance);
        store.set(actor.getModel().getWorldTranslation()).addLocal(dirByLength);
        store.y = getTerrainHeight(store.x, store.z);
        tv.release();
        
        return store;
    }

    @Override
    public Actor getPlayer() {
        return Common.getPlayState().getPlayer();
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
    public void moveObject(Spatial spatial, Vector3f position) {
        ActorControl ac = spatial.getControl(ActorControl.class);
        if (ac != null) {
            ac.setLocation(position);
            return;
        }
        RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
        if (rbc != null) {
            rbc.setPhysicsLocation(position);
            return;
        }
        spatial.setLocalTranslation(position);
    }

    @Override
    public Scene getScene() {
        PlayState ps = Common.getPlayState();
        if (ps == null || ps.getGameState().getGame() == null) 
            return null;
        
        return ps.getGameState().getScene();
    }

    @Override
    public boolean isInScene(Actor actor) {
        return Common.getPlayState().isInScene(actor.getModel());
    }

    @Override
    public Actor getTarget() {
        return Common.getPlayState().getTarget();
    }

    @Override
    public void setTarget(Actor target) {
        Common.getPlayState().setTarget(target);
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
        return Common.getSettings().getWidth();
    }

    @Override
    public float getScreenHeight() {
        return Common.getSettings().getHeight();
    }

    @Override
    public void setMainPlayer(Actor actor) {
        Common.getPlayState().setPlayer(actor);
    }

    @Override
    public boolean isInScene(Spatial spatial) {
        return Common.getPlayState().isInScene(spatial);
    }

    @Override
    public void saveCompleteStage(int stage) {
        PlayState ps = Common.getPlayState();
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
        return Common.getApp();
    }

    @Override
    public void changeGame(String gameId) {
        changeGame(gameService.loadGameData(gameId));
    }

    @Override
    public void changeGame(GameData gameData) {
        GameState gameState = new SimpleGameState(gameData);
        LoadingState loadingState = new LoadingState(Common.getPlayState(), gameState);
        Common.getApp().getStateManager().attach(loadingState);
    }

    @Override
    public String getGameId() {
        PlayState ps = Common.getPlayState();
        if (ps == null)
            return null;
        
        GameState gameState = ps.getGameState();
        if (gameState == null)
            return null;
        
        return gameState.getGame().getData().getId();
    }

    
}
