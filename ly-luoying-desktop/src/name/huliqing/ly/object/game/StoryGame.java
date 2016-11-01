/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.save.SaveStory;
import name.huliqing.ly.constants.IdConstants;

/**
 * 故事模式的游戏方式
 * @author huliqing
 */
public abstract class StoryGame extends SimpleRpgGame {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);

    private final TaskStepControl taskControl = new TaskStepControl();
    
    /**
     * 主角的用户组id
     */
    public final static int GROUP_PLAYER = 1;
    
    // 存档数据
    private SaveStory saveStory = new SaveStory();

    public SaveStory getSaveStory() {
        return saveStory;
    }

    public void setSaveStory(SaveStory saveStory) {
        this.saveStory = saveStory;
    }
    
    public void addTask(TaskStep task) {
        taskControl.addTask(task);
    }

    @Override
    public final void initialize(Application app) {
        super.initialize(app);
        loadPlayer();
        doStoryInitialize();
        taskControl.doNext();
    }

    @Override
    protected void simpleUpdate(float tpf) {
        taskControl.update(tpf);
    }
    
    /**
     * 清理并结束当前游戏
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    /**
     * 载入当前主角玩家存档，如果没有存档则新开游戏
     */
    private void loadPlayer() {
        Actor actor;
        if (saveStory.getPlayer() != null) {
            // 载入玩家主角
            actor = Loader.load(saveStory.getPlayer());
            //List<ShortcutSave> ss = saveStory.getShortcuts();
            //ShortcutManager.loadShortcut(ss, player);
            
            // 载入玩家主角的宠物(这里还不需要载入其他玩家的角色及宠物,由其他玩家重新连接的时候再载入)
            ArrayList<ActorData> actors = saveStory.getActors();
            for (ActorData ad : actors) {
                Actor tempActor = Loader.load(ad);
                if  (actorService.getOwner(tempActor) == tempActor.getData().getUniqueId()) {
                    getScene().addEntity(tempActor);
                }
            }
        } else {
            if (Config.debug) {
                actor = Loader.load(IdConstants.ACTOR_PLAYER_TEST);
                actorService.setLevel(actor, 10);
            } else {
                actor = Loader.load(IdConstants.ACTOR_PLAYER);
            }
//            logicService.resetPlayerLogic(player);
        }
        getScene().addEntity(actor);
        // 确保角色位置在地面上
        Vector3f loc = actorService.getLocation(actor);
        Vector3f terrainHeight = playService.getTerrainHeight(scene, loc.x, loc.z);
        if (terrainHeight != null) {
            actorService.setLocation(actor, terrainHeight.addLocal(0, 0.5f, 0));
        }
        // 给玩家指定分组
        actorService.setGroup(actor, GROUP_PLAYER);
        // 故事模式玩家始终队伍分组为1
        actorService.setTeam(actor, 1);
        // 让角色处于“等待”
        skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
        setPlayer(actor);
    }
    
    /**
     * 故事模式的初始化，该方法的调用在gameInitialize之后。并且是在主玩家(player)载入之后才会被调用。
     */
    protected abstract void doStoryInitialize();
    
    
}
