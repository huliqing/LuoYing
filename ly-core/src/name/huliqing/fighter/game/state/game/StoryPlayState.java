/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.StateNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.game.state.lan.mess.MessPlayActorSelect;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.game.view.ShortcutView;
import name.huliqing.fighter.manager.ShortcutManager;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.game.Game.GameListener;
import name.huliqing.fighter.save.ClientData;
import name.huliqing.fighter.save.SaveConfig;
import name.huliqing.fighter.save.SaveHelper;
import name.huliqing.fighter.save.SaveStory;
import name.huliqing.fighter.save.ShortcutSave;

/**
 *
 * @author huliqing
 */
public class StoryPlayState extends LanPlayState {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 服务端是否准备好
    private GameServer gameServer;    

    // 存档数据
    private SaveStory saveStory;
    
    public StoryPlayState(GameData gameData) {
        super(new SimpleGameState(gameData));
    }
    
    public SaveStory getSaveStory() {
        return saveStory;
    }

    public void setSaveStory(SaveStory saveStory) {
        this.saveStory = saveStory;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        openGameServer();
        
        // ==== 在场景打开后载入玩家及打开服务端
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                // 1.场景载入后再载入角色
                loadStory();
            }
        });
    }

    @Override
    public void cleanup() {
        super.cleanup(); 
    }

    @Override
    public void exit() {
        // 在退出之前保存玩家资料
        saveStory();
        super.exit(); 
    }
    
    @Override
    public void changeGameState(String gameId) {
        // 先把玩家资料存档
        saveStory();
        
        // remove20160615
//        // 需要重新载入存档
//        saveStory = SaveHelper.loadStoryLast();
        
        app.getStateManager().detach(gameState);
        // 切换到另一个游戏
        gameState = new SimpleGameState(gameId);
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                loadStory();
            }
        });
        // 需要更新到gameServer，否则客户端登入的时候会获取到旧的游戏。
        gameServer.setGameData(gameState.getGame().getData());
        app.getStateManager().attach(gameState);
    }
    
    private void saveStory() {
        // 保存玩家资料
        Actor player = gameState.getPlayer();
        if (player != null) {
            // 只在”新游戏“这里才会为null,则需要创建一个新的存档
            if (saveStory == null) {
                saveStory = new SaveStory();
            }
            
            // 1.保存玩家资料及快捷方式
            saveStory.setPlayer(player.getData());
            saveStory.setShortcuts(ShortcutManager.getShortcutSaves());
            
            // 2.保存所有需要保存的角色，这里面包含当前场景中所有玩家和玩家的宠物
            List<Actor> actors = gameState.getActors();
            List<Actor> needSaved = new ArrayList<Actor>();
            List<Long> playerIds = new ArrayList<Long>();
            for (Actor a : actors) {
                if (a.isPlayer()) {
                    playerIds.add(a.getData().getUniqueId());
                }
            }
            for (Actor a : actors) {
                // 玩家角色
                if (a.isPlayer()) {
                    needSaved.add(a);
                    continue;
                }
                // 不是玩家并且已经死亡的角色不能保存，即使是宠物，如果已经死亡
                if (a.isDead()) {
                    continue;
                }
                // 玩家的宠物
                if (a.getData().getOwnerId() > 0 && playerIds.contains(a.getData().getOwnerId())) {
                    needSaved.add(a);
                }
            }
            
            // 这里面可能存在上一次其它玩家或宠物的存档，不能随便移除,只能替换
            ArrayList<ActorData> savedActors = saveStory.getActors();
            for (Actor actor : needSaved) {
                replaceSaveActor(savedActors, actor.getData());
            }
            
            // 3.保存所有客户端与角色及游戏ID的关系
            String gameId = gameState.getGame().getData().getId();
            Collection<HostedConnection> conns = gameServer.getServer().getConnections();
            List<ClientData> savedClients = saveStory.getClientDatas();
            if (!conns.isEmpty()) {
                for (HostedConnection hc : conns) {
                    ConnData cd = hc.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
                    if (cd == null)
                        continue;
                    ClientData clientData = new ClientData();
                    clientData.setClientId(cd.getClientId());
                    clientData.setActorId(cd.getActorId());
                    clientData.setGameId(gameId);
                    replaceClientData(savedClients, clientData);
                }
            }
            
            SaveHelper.saveStoryLast(saveStory);
        }
        
        // 保存全局配置
        SaveConfig saveConfig = new SaveConfig();
        saveConfig.setConfig(configService.getConfig());
        SaveHelper.saveConfig(saveConfig);
    }
    
    // 移除并替换ClientData
    private void replaceClientData(List<ClientData> savedClients, ClientData replacer) {
        Iterator<ClientData> it = savedClients.iterator();
        while (it.hasNext()) {
            ClientData cd = it.next();
            if (cd.getClientId().equals(replacer.getClientId())) {
                it.remove();
            }
        }
        savedClients.add(replacer);
    }
    
    // 用新data代替掉存档中的data.
    private void replaceSaveActor(ArrayList<ActorData> savedActors, ActorData replacer) {
        Iterator<ActorData> it = savedActors.iterator();
        while (it.hasNext()) {
            ActorData ad = it.next();
            if (ad.getUniqueId() == replacer.getUniqueId()) {
                it.remove();
            }
        }
        savedActors.add(replacer);
    }
    
     /**
     * 载入存档，如果没有存档则新开游戏
     */
    private void loadStory() {
        Actor player;
        if (saveStory != null) {
            // 载入玩家主角
            player = actorService.loadActor(saveStory.getPlayer());
            List<ShortcutSave> ss = saveStory.getShortcuts();
            loadShortcut(ss, player);
            
            // 载入玩家主角的宠物(这里还不需要载入其他玩家的角色及宠物,由其他玩家重新连接的时候再载入)
            ArrayList<ActorData> actors = saveStory.getActors();
            for (ActorData data : actors) {
                if  (data.getOwnerId() == player.getData().getUniqueId()) {
                    Actor actor = actorService.loadActor(data);
                    addObject(actor.getModel(), false);
                }
            }
        } else {
            player = actorService.loadActor(Config.debug ? IdConstants.ACTOR_PLAYER_TEST : IdConstants.ACTOR_PLAYER);
            logicService.resetPlayerLogic(player);
        }
        // 故事模式玩家始终队伍分组为1
        actorService.setTeam(player, 1);
        skillService.playSkill(player, skillService.getSkill(player, SkillType.wait).getId(), false);
        
        addObject(player.getModel(), false);
        setPlayer(player);
    }
    
    /**
     * 载入快捷方式
     * @param ss
     * @param player 
     */
    private void loadShortcut(List<ShortcutSave> ss, Actor player) {
        if (ss == null)
            return;
        float shortcutSize = configService.getShortcutSize();
        for (ShortcutSave s : ss) {
            String itemId = s.getItemId();
            ProtoData data = actorService.getItem(player, itemId);
            if (data == null) {
                data = DataLoaderFactory.createData(itemId);
            }
            // 防止物品被删除
            if (data == null) {
                continue;
            }
            // 包裹中只允许存放限定的物品
            DataType type = data.getDataType();
            if (type != DataType.item && type != DataType.skin && type != DataType.skill) {
                continue;
            }
            
            // 由于skill的创建过程比较特殊，SkillData只有在创建了AnimSkill之后
            // 才能获得skillType,所以不能直接使用createProtoData方式获得的SkillData
            // 这会找不到SkillData中的skillType,所以需要从角色身上重新找回SkillData
            if (data.getDataType() == DataType.skill) {
                data = player.getData().getSkillStore().getSkillById(data.getId());
            }
            
            ShortcutView shortcut = ShortcutManager.createShortcut(player, data);
            shortcut.setLocalScale(shortcutSize);
            shortcut.setLocalTranslation(s.getX(), s.getY(), 0);
            ShortcutManager.addShortcutNoAnim(shortcut);
        }
        
        // 如果系统设置锁定快捷方式，则锁定它
        if (configService.isShortcutLocked()) {
            ShortcutManager.setShortcutLocked(true);
        }
    }
    
    // 开启主机模式，这允许故事模式下其它玩家连接到该游戏
    private void openGameServer() {
        // 创建server
        if (gameServer == null) {
            try {
                gameServer = network.createGameServer(gameState.getGame().getData());
                gameServer.setServerListener(new StoryServerListener());
            } catch (IOException ex) {
                Logger.getLogger(StoryPlayState.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        if (!gameServer.isRunning()) {
            gameServer.start();
            gameServer.setServerState(GameServer.ServerState.running);
        }
    }

    @Override
    public List<MessPlayClientData> getClients() {
        return gameServer.getClients();
    }

    @Override
    public void kickClient(int connId) {
        gameServer.kickClient(connId, "kick!");
    }
    
    // ----------------------------------
     private class StoryServerListener extends LanServerListener {
        
        public StoryServerListener() {
            super(app);
        }

        @Override
        protected void processServerMessage(GameServer gameServer, HostedConnection source, Message m) {
            if (m instanceof MessPlayActorSelect) {
                // 响应客户端角色选择
                ((MessPlayActorSelect)m).applyOnServer(gameServer, source);
                
                // remove20160615,不再改变等级
                // 重新设置玩家角色的等级和派别。故事模式下客户端玩家角色进入时
                // 的等级要比主玩家的低一些.
//                Long actorUniqueId = source.getAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID);
//                if (actorUniqueId != null) {
//                    int playerLevel = actorService.getLevel(gameState.getPlayer());
//                    playerLevel *= 0.75f;
//                    if (playerLevel < 1) {
//                        playerLevel = 1;
//                    }
//                    Actor actor = playService.findActor(actorUniqueId);
//                    actorNetwork.setGroup(actor, actorService.getGroup(gameState.getPlayer()));
//                    actorNetwork.setLevel(actor, playerLevel);
//                }
                
                ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
                long actorUniqueId = cd.getActorId();
                if (actorUniqueId > 0) {
                    Actor actor = playService.findActor(actorUniqueId);
                    actorNetwork.setGroup(actor, actorService.getGroup(gameState.getPlayer()));
                }
                return;
            } 
            super.processServerMessage(gameServer, source, m); 
        }
    }
     
}
