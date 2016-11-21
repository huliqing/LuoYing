/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkImpl;
import name.huliqing.ly.view.talk.TalkListener;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.logic.scene.ActorMultLoadHelper;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.constants.StoryConstants;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * task2中收集完树根之后，向古柏交任务。
 * @author huliqing
 */
public class StoryGbTask2End extends AbstractGameLogic {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
//    private final LogicService logicService = Factory.get(LogicService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private StoryGbGame game;
    private Entity player;
    // 任务面板
    private StoryGbTaskLogic taskPanel;
    // 古柏
    private Entity gb;
    // 任务完成时的对话
    private Talk talk;
    
    private final ActorMultLoadHelper gbLoader;
    private int stage;
    
    public StoryGbTask2End(StoryGbGame _game, final Entity player, Vector3f gbPos, StoryGbTaskLogic taskPanel) {
        interval = 0;
        this.game = _game;
        this.player = player;
        this.taskPanel = taskPanel;
        
        gbLoader = new ActorMultLoadHelper(IdConstants.ACTOR_GB) {
            @Override
            public void callback(Entity actor, int loadIndex) {
                // fix bug:先从场景中查找古柏，如果已经存在，则不需要使用载入的。
                // 避免出现两个古柏
                Entity oldGb = findGbOnScene();
                if (oldGb != null) {
                    gb = oldGb;
                    return;
                }
                
                gb = actor;
                gameService.setLevel(gb, 15);
                gameService.setGroup(gb, gameService.getGroup(player));
                actorService.setLocation(gb, game.getGbPosition());
                // 保护角色不死
                setProtected(gb, true);
                playNetwork.addEntity(gb);
            }
        };
    }
    
    private Entity findGbOnScene() {
        List<Entity> entities = game.getScene().getEntities(Entity.class, null);
        for (Entity e : entities) {
            if (e.getData().getId().equals(IdConstants.ACTOR_GB)) {
                return e;
            }
        }
        return null;
    }

    @Override
    protected void doLogic(float tpf) {
        // 载入gb
        if (stage == 0) {
            game.addLogic(gbLoader);
            stage = 1;
            return;
        }
        
        if (stage == 1) {
            if (gb != null) {
                game.removeLogic(gbLoader);
                stage = 2;
            }
            return;
        }
        
        if (stage == 2) {
            if (checkTaskOK()) {
                gameService.setAutoLogic(gb, false);
                actionService.playAction(gb, null);
                skillNetwork.playSkill(gb, skillService.getSkillWaitDefault(gb), false);
                createTalk();
                stage = 3;
            }
        }
    }
    
    private void createTalk() {
        talk = new TalkImpl();
        talk.face(gb, player, false);
        talk.speak(gb, get("talk7.gb1"));
        talk.face(player, gb, false);
        talk.speak(player, get("talk7.xy1"));
        talk.speak(gb, get("talk7.gb2"));
        talk.speak(gb, get("talk7.gb3"));
        talk.speak(gb, get("talk7.gb4"));
        // 如果“祭坛”未摧毁则提醒之。
        if (!isAltarDead()) {
            talk.face(gb, player, false);
            talk.speak(gb, get("talk7.gb5"));
        }
        talk.delay(2);
        talk.addListener(new TalkListener() {
            @Override
            public void onTalkEnd() {
                // 移除主角身上的树根
//                ObjectData stumpData = protoService.getData(player, IdConstants.ITEM_GB_STUMP);
//                protoNetwork.removeData(player, stumpData.getId(), taskPanel.getTotal());
                
                // 移除主角身上的树根
                ItemData itemData = player.getData().getObjectData(IdConstants.ITEM_GB_STUMP);
                entityNetwork.removeObjectData(player, itemData.getUniqueId(), itemData.getTotal());
                
                // 移除古柏
                playNetwork.removeEntity(gb);
                
                // 停止任务面板的更新及移除提示面板
                game.removeLogic(taskPanel);
                // 保存关卡完成状态
                gameService.saveCompleteStage(StoryConstants.STORY_NUM_GB);
                // 提示
                gameNetwork.addMessage(ResourceManager.get(ResConstants.TASK_SUCCESS), MessageType.item);
                playNetwork.addEntity((View)Loader.load(IdConstants.VIEW_TEXT_SUCCESS));
            }
        });
        gameNetwork.talk(talk);
    }
    
    // 检查任务是否完成，并且是否适合开始结束对话。
    // 这要求树根数完成，player不能死，player在gb附近，并且附近没有敌人
    private boolean checkTaskOK() {
        return taskPanel.isOk() 
                && !gameService.isDead(player)
                && actorService.distance(player, gb) < 10
                && !isEnemyNear(25);
    }
    
    private boolean isAltarDead() {
        List<Entity> entities = game.getScene().getEntities(Entity.class, null);
        for (Entity e : entities) {
            if (e.getData().getId().equals(IdConstants.ACTOR_ALTAR)) {
                return gameService.isDead(e);
            }
        }
        return true;
    }
    
    private boolean isEnemyNear(float distance) {
        List<Actor> actors = game.getScene().getEntities(Actor.class, null);
        for (Actor a : actors) {
            if (!gameService.isDead(a) 
                    && gameService.isEnemy(a, player) 
                    && actorService.distance(a, player) < distance) {
                return true;
            }
        }
        return false;
    }
    
    // 设置角色为保护状态或非保护状态
    private void setProtected(Entity actor, boolean bool) {
        if (bool) {
            entityNetwork.addObjectData(actor, IdConstants.STATE_SAFE, 1);
        } else {
            entityNetwork.removeObjectData(actor,  actor.getData().getObjectData(IdConstants.STATE_SAFE).getUniqueId(), 1);
        }
    }
    
    private String get(String rid, Object... param) {
//        return ResourceManager.getOther("resource_gb", rid, param);
        return ResourceManager.get("storyGb." +  rid, param);
    }
}
