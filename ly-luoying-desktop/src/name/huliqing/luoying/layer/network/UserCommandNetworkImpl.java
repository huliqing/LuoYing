/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.ly.network.Network;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ActorData;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.ChatNetwork;
import name.huliqing.ly.layer.network.ObjectNetwork;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.network.TalentNetwork;
import name.huliqing.ly.layer.network.TaskNetwork;
import name.huliqing.ly.layer.service.ActionService;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.LogicService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.mess.MessActionRun;
import name.huliqing.ly.mess.MessActorFollow;
import name.huliqing.ly.mess.MessActorSetTarget;
import name.huliqing.ly.mess.MessAutoAttack;
import name.huliqing.luoying.mess.MessChatSell;
import name.huliqing.luoying.mess.MessChatSend;
import name.huliqing.ly.mess.MessPlayActorSelect;
import name.huliqing.luoying.mess.MessChatShop;
import name.huliqing.ly.mess.MessProtoRemove;
import name.huliqing.ly.mess.MessProtoUse;
import name.huliqing.luoying.mess.MessMessage;
import name.huliqing.luoying.mess.MessPlayChangeGameState;
import name.huliqing.ly.mess.MessTalentAddPoint;
import name.huliqing.ly.mess.MessTaskAdd;
import name.huliqing.ly.mess.MessTaskComplete;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.skill.Skill;
import name.huliqing.ly.object.task.Task;
import name.huliqing.ly.layer.service.ObjectService;

/**
 * 这个类主要是用来专门处理用户命令事件的。所有用户命令统一经过这个接口，
 * >>>>这个接口必须遵循以下逻辑：
 * 1.如果是client模式,则直接将用户命令发送到服务器，不处理任何事件逻辑。
 * 2.如果是server或single模式，则按如下逻辑处理该命令：
 *      > 2.1 向所有客户端广播事件
 *      > 2.2 执行自身逻辑
 * 注意：只有UserCommandNetwork会处理第1点，其它所有**Network类都不应该处理
 * 第1点。
 * 
 * >>>> 一些原则限制：
 * 1.只有用户命令允许调用这个接口，其它所有类Network,Service,Dao都不应该调用这个
 * 接口。
 * 2.允许UserCommandNetwork调用其它Network或service,返过来则不行。
 * 3.注意层级关系： UserCommandNetwork -> **Handler -> **OtherNetwork -> **Service -> **Loader -> **Dao
 * @author huliqing
 */
public class UserCommandNetworkImpl implements UserCommandNetwork {
    private final static Network NETWORK = Network.getInstance();
    private LogicService logicService;
    private SkillService skillService;
    private ActorService actorService;
    private PlayService playService;
    private ActionService actionService;
    private GameService gameService;
    private ObjectService protoService;
    
    private PlayNetwork playNetwork;
    private ActorNetwork actorNetwork;
    private TalentNetwork talentNetwork;
    private ChatNetwork chatNetwork;
    private TaskNetwork taskNetwork;
    private ObjectNetwork protoNetwork;
    
    @Override
    public void inject() {
        logicService = Factory.get(LogicService.class);
        skillService = Factory.get(SkillService.class);
        actorService = Factory.get(ActorService.class);
        playService = Factory.get(PlayService.class);
        actionService = Factory.get(ActionService.class);
        gameService = Factory.get(GameService.class);
        protoService = Factory.get(ObjectService.class);
        
        playNetwork = Factory.get(PlayNetwork.class);
        actorNetwork = Factory.get(ActorNetwork.class);
        talentNetwork = Factory.get(TalentNetwork.class);
        chatNetwork = Factory.get(ChatNetwork.class);
        taskNetwork = Factory.get(TaskNetwork.class);
        protoNetwork = Factory.get(ObjectNetwork.class);
    }

    @Override
    public void selectPlayer(String actorId, String actorName) {
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(new MessPlayActorSelect(actorId, actorName));
        } else {
            Actor actor = actorService.loadActor(actorId);
            
            logicService.resetPlayerLogic(actor);
            actorService.setName(actor, actorName);
            // 暂时以1作为默认分组
            actorService.setTeam(actor, 1);
            List<Skill> waitSkills = skillService.getSkillWait(actor);
            if (waitSkills != null && !waitSkills.isEmpty()) {
                skillService.playSkill(actor, waitSkills.get(0), false);
            }
            
            // 这是主机,所以要设置为当前主场景玩家,与actor.setPlayer(true)不同
            // 注:在设置名字之后再setAsPlayer,否则FacePanel中的player名字不会更新
            playService.setMainPlayer(actor);
            playNetwork.addActor(actor);
            
            // 通知
            if (NETWORK.hasConnections()) {
                String message = ResourceManager.get("lan.enterGame", new Object[] {actorName});
                MessageType type = MessageType.item;
                MessMessage mess = new MessMessage();
                mess.setMessage(message);
                mess.setType(type);
                NETWORK.broadcast(mess); // 通知所有客户端
                playService.addMessage(message, type); // 通知主机
            }
        }
    }

    @Override
    public void addSimplePlayer(Actor actor) {
        ActorData data = actor.getData();
         if (NETWORK.isClient()) {
            NETWORK.sendToServer(new MessPlayActorSelect(data.getId(), data.getName()));
        } else {
            playNetwork.addSimplePlayer(actor);
            
            // 通知
            if (NETWORK.hasConnections()) {
                String message = ResourceManager.get("lan.enterGame", new Object[] {actor.getData().getName()});
                MessageType type = MessageType.item;
                MessMessage notice = new MessMessage();
                notice.setMessage(message);
                notice.setType(type);
                NETWORK.broadcast(notice);                          // 通知所有客户端
                playService.addMessage(message, type);   // 通知主机
            }
        }
    }

    @Override
    public void playRunToPos(Actor actor, Vector3f worldPos) {
        // 死亡后不能再移动
        if (actorService.isDead(actor)) {
            return;
        }
        
        if (NETWORK.isClient()) {
            MessActionRun runAction = new MessActionRun();
            runAction.setActorId(actor.getData().getUniqueId());
            runAction.setPos(worldPos);
            NETWORK.sendToServer(runAction);
        } else {
                // for test
//            actor.getModule(ActorModule.class).setWalkDirection(worldPos.subtract(actor.getSpatial().getWorldTranslation()).normalizeLocal());

            actorNetwork.setFollow(actor, -1);
            actionService.playRun(actor, worldPos);
                        
            // 不需要广播
        }
    }

    @Override
    public void attack(Actor actor, Actor target) {
        if (NETWORK.isClient()) {
            MessAutoAttack mess = new MessAutoAttack();
            mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
            NETWORK.sendToServer(mess);
        } else {
            playNetwork.attack(actor, target);
        }
    }
    
    /**
     * @deprecated 
     * @param actor
     * @param data 
     */
    @Override
    public void useObject(Actor actor, ObjectData data) {
        
        // add20160406，只要是使用物品（玩家点家物品），则角色目标将重定向到当前
        // 界面的主目标,因为有很多物品在使用的时候都需要当前界面的主目标。
        Actor target = playService.getTarget();
        if (target != null) {
            if (NETWORK.isClient()) {
                MessActorSetTarget mess = new MessActorSetTarget();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTargetId(target.getData().getUniqueId());
                NETWORK.sendToServer(mess);
            } else {
                actorNetwork.setTarget(actor, target);
            }
        }
        
        // 对于本地物体不需要传递到服务端或客户端，比如“地图”的使用，当客户端打开地图的时候是不需要传递到服务端的。
        // localObject这是一种特殊的物品，只通过本地handler执行，所以使用后物品数量不会实时同步到其它客户端。需要注意
        // 这一点。
        if (data.isLocalObject()) {
            protoService.useData(actor, data);
            return;
        }
        
        // 使用物品
        if (NETWORK.isClient()) {
            MessProtoUse mess = new MessProtoUse();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(data.getId());
            NETWORK.sendToServer(mess);
        } else {
            protoNetwork.useData(actor, data);
        }
    }

    @Override
    public void removeObject(Actor actor, String objectId, int amount) {
        if (NETWORK.isClient()) {
            MessProtoRemove mess = new MessProtoRemove();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(objectId);
            mess.setAmount(amount);
            NETWORK.sendToServer(mess);
        } else {
            protoNetwork.removeData(actor, objectId, amount);
        }
    }

    @Override
    public void follow(Actor actor, long targetId) {
        if (NETWORK.isClient()) {
            MessActorFollow mess = new MessActorFollow();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTargetId(targetId);
            NETWORK.sendToServer(mess);
        } else {
            actorNetwork.setFollow(actor, targetId);
        }
    }

    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        if (NETWORK.isClient()) {
            MessTalentAddPoint mess = new MessTalentAddPoint();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTalentId(talentId);
            mess.setPoints(points);
            NETWORK.sendToServer(mess);
        } else {
            talentNetwork.addTalentPoints(actor, talentId, points);
        }
    }
    
    @Override
    public void chatShop(Actor seller, Actor buyer, String itemId, int count, float discount) {
        // On client
        if (NETWORK.isClient()) {
            MessChatShop mess = new MessChatShop();
            mess.setSeller(seller.getData().getUniqueId());
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setItemId(itemId);
            mess.setCount(count);
            mess.setDiscount(discount);
            NETWORK.sendToServer(mess);
            return;
        } 
        
        chatNetwork.chatShop(seller, buyer, itemId, count, discount);
        
    }

    @Override
    public void chatSell(Actor seller, Actor buyer, String[] items, int[] counts, float discount) {
        // On client
        if (NETWORK.isClient()) {
            MessChatSell mess = new MessChatSell();
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setCounts(counts);
            mess.setDiscount(discount);
            mess.setItems(items);
            mess.setSeller(seller.getData().getUniqueId());
            NETWORK.sendToServer(mess);
            return;
        } 
        
        // On server
        chatNetwork.chatSell(seller, buyer, items, counts, discount);
    }

    @Override
    public void chatSend(Actor sender, Actor receiver, String[] items, int[] counts) {
        // on client
        if (NETWORK.isClient()) {
            MessChatSend mess = new MessChatSend();
            mess.setCounts(counts);
            mess.setItems(items);
            mess.setReceiver(receiver.getData().getUniqueId());
            mess.setSender(sender.getData().getUniqueId());
            NETWORK.sendToServer(mess);
            return;
        }
        
        // on server
        chatNetwork.chatSend(sender, receiver, items, counts);
    }

    @Override
    public void chatTaskAdd(Actor actor, Task task) {
        if (NETWORK.isClient()) {
            MessTaskAdd mess = new MessTaskAdd();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTaskData(task.getData());
            NETWORK.sendToServer(mess);
            return;
        }
        taskNetwork.addTask(actor, task);
    }

    @Override
    public void chatTaskComplete(Actor actor, Task task) {
        // 客户端向服务端提交“完成任务”的请求
        if (NETWORK.isClient()) {
            MessTaskComplete mess = new MessTaskComplete();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTaskId(task.getId());
            NETWORK.sendToServer(mess);
            return;
        }
        
        // 服务端直接执行“完成任务”的请求，并分派到客户端
        taskNetwork.completeTask(actor, task);
    }

    @Override
    public void changeGameState(String gameId) {
        GameData gameData = gameService.loadGameData(gameId);
        if (NETWORK.isClient()) {
            MessPlayChangeGameState mess = new MessPlayChangeGameState();
            mess.setGameData(gameData);
            NETWORK.sendToServer(mess);
        } else {
            playNetwork.changeGame(gameData);
        }
        
    }
    
    
}
