/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.mess.network.ClientExitMess;
import name.huliqing.luoying.mess.ActorLoadSavedMess;
import name.huliqing.luoying.mess.ActorLoadSavedResultMess;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.save.ClientData;
import name.huliqing.luoying.save.SaveHelper;
import name.huliqing.luoying.save.SaveStory;
import name.huliqing.ly.LyConfig;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.object.game.story.TaskStep;
import name.huliqing.ly.object.game.story.TaskStepControl;
import name.huliqing.ly.view.shortcut.ShortcutManager;

/**
 * 服务模式服务端
 * @author huliqing
 */
public abstract class StoryServerNetworkRpgGame extends ServerNetworkRpgGame {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    /** 存档数据 */
    protected SaveStory saveStory = new SaveStory();
    
    /** 主角的用户组 */
    public final static int GROUP_PLAYER = 1;
    /** 主角的队伍分组 */
    public final static int TEAM_PLAYER = 1;
    
    private final TaskStepControl taskControl = new TaskStepControl();
    private boolean started;
    
    public StoryServerNetworkRpgGame() {}

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        setUIVisiable(false);
    }
    
    @Override
    public void onSceneLoaded(Scene scene) {
        super.onSceneLoaded(scene);
        loadPlayer();
        doStoryInitialize();
        taskControl.doNext();
        started = true;
        setUIVisiable(true);
    }
    
    @Override
    protected void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        if (!started) {
            return;
        }
        taskControl.update(tpf);
    }
    
    @Override
    public void cleanup() {
        // 在退出之前保存玩家资料
        saveStory();
        super.cleanup();
    }
    
    public SaveStory getSaveStory() {
        return saveStory;
    }

    public void setSaveStory(SaveStory saveStory) {
        if (saveStory == null) {
            throw new NullPointerException("SaveStory could not be null!");
        }
        this.saveStory = saveStory;
    }
    
    public void addTask(TaskStep task) {
        taskControl.addTask(task);
    }
    
    @Override
    public void kickClient(int connId) {
        // 踢出之前先把玩家的资料保存起来
        HostedConnection conn = gameServer.getServer().getConnection(connId);
        if (conn != null) {
            ConnData cd = conn.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            if (cd != null && cd.getClientId() != null && cd.getEntityId() > 0) {
                storeClient(saveStory, scene.getEntities(Actor.class, null), cd.getClientId(), cd.getEntityId(), data.getId());
                SaveHelper.saveStoryLast(saveStory);
            }
        }
        super.kickClient(connId);
    }
    
    @Override
    protected void onAddClientPlayer(ConnData cd, Entity actor) {
        // 改变客户端角色分组及队伍，注：这里不要依赖于player,因为客户端可能比服务端玩家还先载入。
        gameService.setGroup(actor, GROUP_PLAYER);
        gameService.setTeam(actor, TEAM_PLAYER);
        super.onAddClientPlayer(cd, actor);
    }
    
    @Override
    protected void processGameMess(GameServer gameServer, HostedConnection source, GameMess m) {
        // 如果客户端偿试要求载入存档，则从存档中载入，如果不存在存档（新游戏）或存档中没有指定客户端的资料则返回false
        // 以通知客户端自己去选择一个角色来进行游戏
        if (m instanceof ActorLoadSavedMess) {
            // 找出客户端的存档资料，注意saveStory可能为null（在“新游戏"情况下）
            if (saveStory != null) {
                List<ClientData> clientDatas = saveStory.getClientDatas();
                ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
                for (ClientData clientData : clientDatas) {
                    if (clientData.getClientId().equals(cd.getClientId())) {
                        // 载入角色客户端角色的资料
                        boolean result = loadClient(saveStory, clientData, cd);
                        if (result) {
                            cd.setEntityId(clientData.getActorId());
                        }

                        // 通知客户端,这样客户端可以确定是否需要弹出“角色选择面板”来重新选择一个角色进行游戏
                        ActorLoadSavedResultMess messResult = new ActorLoadSavedResultMess(result);
                        messResult.setActorId(clientData.getActorId());
                        gameServer.send(source, messResult);

                        // 记住客户端所选择的角色
                        Entity actor = scene.getEntity(cd.getEntityId());
                        cd.setEntityId(actor.getData().getUniqueId());
                        cd.setEntityName(gameService.getName(actor));
                        
                        // 更新本地（服务端）客户端列表
                        onClientListUpdated();
                        // 通知所有客户更新“客户端列表”
                        gameServer.broadcast(new ClientsMess(gameServer.getClients()));
                        
                        return;
                    }
                }
            }
            gameServer.send(source, new ActorLoadSavedResultMess(false));
            return;
        }

        // 当服务端（故事模式）接收到客户端退出游戏的消息时，服务端要保存客户端的资料,以便下次客户端连接时能够继续
        if (m instanceof ClientExitMess) {
            ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            if (cd.getClientId() != null && cd.getEntityId() > 0) {
                storeClient(saveStory, scene.getEntities(Actor.class, null), cd.getClientId(), cd.getEntityId(), data.getId());
                SaveHelper.saveStoryLast(saveStory);
            }
        }
        
        super.processGameMess(gameServer, source, m);
    }
    
    /**
     * 载入当前主角玩家存档，如果没有存档则新开游戏
     */
    private void loadPlayer() {
        Actor actor;
        if (saveStory.getPlayer() != null) {
            // 载入玩家主角
            actor = Loader.load(saveStory.getPlayer());
            
            // 载入快捷方式
            ShortcutManager.loadShortcut(saveStory.getShortcuts(), actor);
            
            // 载入玩家主角的宠物(这里还不需要载入其他玩家的角色及宠物,由其他玩家重新连接的时候再载入)
            ArrayList<EntityData> actors = saveStory.getActors();
            for (EntityData ad : actors) {
                if (ad.getUniqueId() == actor.getData().getUniqueId()) {
                    continue;
                }
                Actor tempActor = Loader.load(ad);
                if  (gameService.getOwner(tempActor) == actor.getEntityId()) {
                    playNetwork.addEntity(tempActor);
                }
            }
        } else {
            if (Config.debug) {
                actor = Loader.load(IdConstants.ACTOR_PLAYER_TEST);
                gameService.setLevel(actor, 10);
            } else {
                actor = Loader.load(IdConstants.ACTOR_PLAYER);
            }
        }

        // 给玩家指定分组
        gameService.setGroup(actor, GROUP_PLAYER);
        // 故事模式玩家队伍固定分组
        gameService.setTeam(actor, TEAM_PLAYER);
        // 确保角色位置在地面上
        gameService.setOnTerrain(actor);
        // 添加主玩家
        onAddServerPlayer(actor);
    }
    
     // 从saveStory中载入指定clientId的角色
    private boolean loadClient(SaveStory saveStory, ClientData clientData, ConnData cd) {
        List<EntityData> actors =  saveStory.getActors();
        EntityData clientPlayerData = null;
        for (EntityData ed : actors) {
            if (ed.getUniqueId() == clientData.getActorId()) {
                clientPlayerData = ed;
                break;
            }
        }
        if (clientPlayerData == null) {
            return false;
        }
        
        // 载入客户端玩家及其宠物，注：这里要用Network,因为服务端和客户端已经为running状态.
        Actor clientPlayer= Loader.load(clientPlayerData);
        onAddClientPlayer(cd, clientPlayer);
        
        for (EntityData entityData : actors) {
            Actor actor = Loader.load(entityData);
            if (gameService.getOwner(actor) == clientPlayerData.getUniqueId()) {
                
//                skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
                Skill waitSkill = skillService.getSkillWaitDefault(actor);
                if (waitSkill != null) {
                    entityService.useObjectData(actor, waitSkill.getData().getUniqueId());
                }
                
                playNetwork.addEntity(actor);
            }
        }
        return true;
    }
    
    /**
     * 保存整个存档，当故事模式在退出时要保存整个存档，包含主角，其它客户端玩家资料，及所有玩家的宠物等。
     */
    private void saveStory() {
        String gameId = data.getId();
        List<Actor> actors = scene.getEntities(Actor.class, null);
        
        // 保存所有故事模式下的主角
        Entity savePlayer = getPlayer();
        savePlayer.updateDatas();
        storeClient(saveStory, actors, configService.getClientId(), savePlayer.getEntityId(), gameId);
        
        // 保存当前所有客户端的资料
        Collection<HostedConnection> conns = gameServer.getServer().getConnections();
        if (conns != null && conns.size() > 0) {
            for (HostedConnection hc : conns) {
                ConnData cd = hc.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
                // 如果客户端刚刚连接或还没有选角色
                if (cd.getClientId() == null || cd.getEntityId() <= 0)
                    continue;
                storeClient(saveStory, actors, cd.getClientId(), cd.getEntityId(), gameId);
            }
        }
        
        // 单独保存主角，这是为了兼容v2.4及之前的版本,后续可能会取消这个特殊的保存方式.
        saveStory.setPlayer(savePlayer.getData());
        
        // 保存快捷方式
        saveStory.setShortcuts(ShortcutManager.getShortcutSaves());
        
        // 存档到系统文件 
        SaveHelper.saveStoryLast(saveStory);

        // 保存全局配置
        gameService.saveConfig(LyConfig.getConfigData());
    }
    
     /**
     * 单独保存一个客户端资料到存档中，但是并不立即保存到系统文件,其中包含客户端的宠物资料.
     * @param saveStory 存档资料，如果没有则应该生成一个空的存档
     * @param actors 场景中的所有角色
     * @param clientId 客户端标识
     * @param clientPlayerId 客户端玩家所控制的角色的唯一ID，必须包含在actors中
     * @param gameId 当前游戏id.
     */
    private void storeClient(SaveStory saveStory, List<Actor> actors, String clientId, long clientPlayerId, String gameId) {
        ArrayList<EntityData> savedActors = saveStory.getActors();
        List<ClientData> savedClients = saveStory.getClientDatas();
        
        // ---- 先移除客户端角色及宠物及对应关系
        // 移除客户端角色及其宠物从存档资料中移除
        Iterator<EntityData> sait = savedActors.iterator();
        while (sait.hasNext()) {
            EntityData actorData = sait.next();
            // 如果是玩家
            if (actorData.getUniqueId() == clientPlayerId) {
                sait.remove();
                continue;
            }
            // 如果是玩家的宠物
            Entity actor = Loader.load(actorData);
            if (gameService.getOwner(actor) == clientPlayerId) {
                sait.remove();
            }
        }
        
        // 移除对应关系
        Iterator<ClientData> scit = savedClients.iterator();
        while (scit.hasNext()) {
            ClientData clientData = scit.next();
            if (clientData.getClientId().equals(clientId)) {
                scit.remove();
            }
        }
        
        // ---- 重新保存客户端资料及宠物及对应关系
        // 重新添加客户端玩家及其宠物资料到存档中，注意宠物必须满足以下条件才可以:
        // 1.属于当前玩家
        // 2.必须是活着的（生命值大于0）
        // 3.必须是生物类角色，不要保存防御塔或其它建筑之类
        Actor clientPlayer = findActor(actors, clientPlayerId);
        clientPlayer.updateDatas();
        savedActors.add(clientPlayer.getData());
        for (Actor a : actors) {
            if(gameService.getOwner(a) != clientPlayerId) {
                continue;
            }
            if (gameService.isDead(a)) {
                continue;
            }
            if (!gameService.isBiology(a)) {
                continue;
            }
            a.updateDatas();
            savedActors.add(a.getData());
        }
        // 添加客户端与角色及场景的对应关系
        ClientData clientData = new ClientData();
        clientData.setClientId(clientId);
        clientData.setActorId(clientPlayerId);
        clientData.setGameId(gameId);
        savedClients.add(clientData);
    }
    
    private Actor findActor(List<Actor> actors, long actorId) {
        for (Actor actor : actors) {
            if (actor.getData().getUniqueId() == actorId) {
                return actor;
            }
        }
        return null;
    }
    
    /**
     * 故事模式的初始化，该方法的调用在gameInitialize之后。并且是在主玩家(player)载入之后才会被调用。
     */
    protected abstract void doStoryInitialize();
}
