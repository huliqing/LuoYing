/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.network.Network;
import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.GameData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.LogicService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.mvc.service.TaskService;
import name.huliqing.core.mess.MessActionRun;
import name.huliqing.core.mess.MessActorFollow;
import name.huliqing.core.mess.MessActorSetTarget;
import name.huliqing.core.mess.MessAutoAttack;
import name.huliqing.core.mess.MessChatSell;
import name.huliqing.core.mess.MessChatSend;
import name.huliqing.core.mess.MessPlayActorSelect;
import name.huliqing.core.mess.MessChatShop;
import name.huliqing.core.mess.MessProtoRemove;
import name.huliqing.core.mess.MessProtoUse;
import name.huliqing.core.mess.MessMessage;
import name.huliqing.core.mess.MessPlayChangeGameState;
import name.huliqing.core.mess.MessTalentAddPoint;
import name.huliqing.core.mess.MessTaskAdd;
import name.huliqing.core.mess.MessTaskComplete;
import name.huliqing.core.mvc.service.GameService;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.task.Task;

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
    private final static Network network = Network.getInstance();
    private LogicService logicService;
    private SkillService skillService;
    private ActorService actorService;
    private StateService stateService;
    private PlayService playService;
    private ActionService actionService;
    private SkinService skinService;
    private TaskService taskService;
    private GameService gameService;
    private ProtoService protoService;
    
    private PlayNetwork playNetwork;
    private ActionNetwork actionNetwork;
    private ActorNetwork actorNetwork;
    private SkinNetwork skinNetwork;
    private SkillNetwork skillNetwork;
    private TalentNetwork talentNetwork;
    private ChatNetwork chatNetwork;
    private TaskNetwork taskNetwork;
    private ProtoNetwork protoNetwork;
    
    @Override
    public void inject() {
        logicService = Factory.get(LogicService.class);
        skillService = Factory.get(SkillService.class);
        actorService = Factory.get(ActorService.class);
        stateService = Factory.get(StateService.class);
        playService = Factory.get(PlayService.class);
        actionService = Factory.get(ActionService.class);
        skinService = Factory.get(SkinService.class);
        taskService = Factory.get(TaskService.class);
        gameService = Factory.get(GameService.class);
        protoService = Factory.get(ProtoService.class);
        
        skillNetwork = Factory.get(SkillNetwork.class);
        playNetwork = Factory.get(PlayNetwork.class);
        actionNetwork = Factory.get(ActionNetwork.class);
        actorNetwork = Factory.get(ActorNetwork.class);
        skinNetwork = Factory.get(SkinNetwork.class);
        talentNetwork = Factory.get(TalentNetwork.class);
        chatNetwork = Factory.get(ChatNetwork.class);
        taskNetwork = Factory.get(TaskNetwork.class);
        protoNetwork = Factory.get(ProtoNetwork.class);
    }

    @Override
    public void selectPlayer(String actorId, String actorName) {
        
        if (network.isClient()) {
            network.sendToServer(new MessPlayActorSelect(actorId, actorName));
        } else {
            Actor actor = actorService.loadActor(actorId);
            
            // TODO:这里有一部分重复的代码需要处理：一部分在上面的MessCSActorSelect中
            // network.sendToServer(new MessPlayActorSelect(actorId, actorName));
            logicService.resetPlayerLogic(actor);
            actorService.setName(actor, actorName);
            // 暂时以1作为默认分组
            actorService.setTeam(actor, 1);
            skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait), false);
            // 这是主机,所以要设置为当前主场景玩家,与actor.setPlayer(true)不同
            // 注:在设置名字之后再setAsPlayer,否则FacePanel中的player名字不会更新
            playService.setMainPlayer(actor);
            playNetwork.addActor(actor);
            
            // 通知
            if (network.hasConnections()) {
                String message = ResourceManager.get("lan.enterGame", new Object[] {actorName});
                MessageType type = MessageType.item;
                MessMessage notice = new MessMessage();
                notice.setMessage(message);
                notice.setType(type);
                network.broadcast(notice);              // 通知所有客户端
                playService.addMessage(message, type);  // 通知主机
            }
            
            // TODO:需要通知客户端,刷新角色名
        }
    }

    @Override
    public void addSimplePlayer(Actor actor) {
        ActorData data = actor.getData();
         if (network.isClient()) {
             
            network.sendToServer(new MessPlayActorSelect(data.getId(), data.getName()));
            
        } else {
            playNetwork.addSimplePlayer(actor);
            
            // 通知
            if (network.hasConnections()) {
                String message = ResourceManager.get("lan.enterGame", new Object[] {actor.getData().getName()});
                MessageType type = MessageType.item;
                MessMessage notice = new MessMessage();
                notice.setMessage(message);
                notice.setType(type);
                network.broadcast(notice);                          // 通知所有客户端
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
        
        if (network.isClient()) {
            MessActionRun runAction = new MessActionRun();
            runAction.setActorId(actor.getData().getUniqueId());
            runAction.setPos(worldPos);
            network.sendToServer(runAction);
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
        if (network.isClient()) {
            MessAutoAttack mess = new MessAutoAttack();
            mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
            network.sendToServer(mess);
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
            if (network.isClient()) {
                MessActorSetTarget mess = new MessActorSetTarget();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTargetId(target.getData().getUniqueId());
                network.sendToServer(mess);
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
        if (network.isClient()) {
            MessProtoUse mess = new MessProtoUse();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(data.getId());
            network.sendToServer(mess);
        } else {
            protoNetwork.useData(actor, data);
        }
    }

    @Override
    public void removeObject(Actor actor, String objectId, int amount) {
        // remove20160821
//        ObjectData data = protoService.getData(actor, objectId);
//        if (data == null)
//            return;
        
        if (network.isClient()) {
            
            // remove20160830 不再使用本地优先删除的方式
//            // 客户端本地先删除 
//            protoService.removeData(actor, objectId, amount);
            
            // 通知服务端
            MessProtoRemove mess = new MessProtoRemove();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(objectId);
            mess.setAmount(amount);
            network.sendToServer(mess);
        } else {
            protoNetwork.removeData(actor, objectId, amount);
        }
    }

    @Override
    public void follow(Actor actor, long targetId) {
        if (network.isClient()) {
            MessActorFollow mess = new MessActorFollow();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTargetId(targetId);
            network.sendToServer(mess);
        } else {
            actorNetwork.setFollow(actor, targetId);
        }
    }

    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        if (network.isClient()) {
            MessTalentAddPoint mess = new MessTalentAddPoint();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTalentId(talentId);
            mess.setPoints(points);
            network.sendToServer(mess);
        } else {
            talentNetwork.addTalentPoints(actor, talentId, points);
        }
    }
    
    @Override
    public void chatShop(Actor seller, Actor buyer, String itemId, int count, float discount) {
        // On client
        if (network.isClient()) {
            MessChatShop mess = new MessChatShop();
            mess.setSeller(seller.getData().getUniqueId());
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setItemId(itemId);
            mess.setCount(count);
            mess.setDiscount(discount);
            network.sendToServer(mess);
            return;
        } 
        
        chatNetwork.chatShop(seller, buyer, itemId, count, discount);
        
    }

    @Override
    public void chatSell(Actor seller, Actor buyer, String[] items, int[] counts, float discount) {
        // On client
        if (network.isClient()) {
            MessChatSell mess = new MessChatSell();
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setCounts(counts);
            mess.setDiscount(discount);
            mess.setItems(items);
            mess.setSeller(seller.getData().getUniqueId());
            network.sendToServer(mess);
            return;
        } 
        
        // On server
        chatNetwork.chatSell(seller, buyer, items, counts, discount);
    }

    @Override
    public void chatSend(Actor sender, Actor receiver, String[] items, int[] counts) {
        // on client
        if (network.isClient()) {
            MessChatSend mess = new MessChatSend();
            mess.setCounts(counts);
            mess.setItems(items);
            mess.setReceiver(receiver.getData().getUniqueId());
            mess.setSender(sender.getData().getUniqueId());
            network.sendToServer(mess);
            return;
        }
        
        // on server
        chatNetwork.chatSend(sender, receiver, items, counts);
    }

    @Override
    public void chatTaskAdd(Actor actor, Task task) {
        if (network.isClient()) {
            MessTaskAdd mess = new MessTaskAdd();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTaskData(task.getData());
            network.sendToServer(mess);
            return;
        }
        
        taskNetwork.addTask(actor, task);
    }

    @Override
    public void chatTaskComplete(Actor actor, Task task) {
        // 客户端向服务端提交“完成任务”的请求
        if (network.isClient()) {
            MessTaskComplete mess = new MessTaskComplete();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTaskId(task.getId());
            network.sendToServer(mess);
            return;
        }
        
        // 服务端直接执行“完成任务”的请求，并分派到客户端
        taskNetwork.completeTask(actor, task);
    }

    @Override
    public void changeGameState(String gameId) {
        GameData gameData = gameService.loadGameData(gameId);
        if (network.isClient()) {
            MessPlayChangeGameState mess = new MessPlayChangeGameState();
            mess.setGameData(gameData);
            network.sendToServer(mess);
        } else {
            playNetwork.changeGame(gameData);
        }
        
    }
    
    
}
