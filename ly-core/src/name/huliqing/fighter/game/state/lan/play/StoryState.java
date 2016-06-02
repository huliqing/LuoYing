/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.play;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.renderer.queue.RenderQueue;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.enums.MessageType;
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
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.game.state.lan.mess.MessPlayActorSelect;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.manager.ShortcutManager;
import name.huliqing.fighter.object.game.Game;
import name.huliqing.fighter.game.view.ShortcutView;
import name.huliqing.fighter.save.SaveConfig;
import name.huliqing.fighter.save.SaveHelper;
import name.huliqing.fighter.save.SaveStory;
import name.huliqing.fighter.save.ShortcutSave;

/**
 * 故事模式，每个故事都定义了一个关卡数stageNum,完成后应该调用方法保存
 * 该关卡数。
 * 注意：关卡数stageNum从1开始。如果当前关卡定义为3，当完成当前关卡后，关卡列表
 * 的关卡1,2,3都会被打开（即使1,2可能未完成），同时激活下一关卡4.
 * @author huliqing
 */
public class StoryState extends LanPlayState {
    private final static Logger logger = Logger.getLogger(StoryState.class.getName());
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    private GameServer gameServer;
    // 存档数据
    private SaveStory saveStory;
    
    public StoryState(GameData gameData) {
        super(gameData);
    }

    public SaveStory getSaveStory() {
        return saveStory;
    }

    public void setSaveStory(SaveStory saveStory) {
        this.saveStory = saveStory;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, final Application app) {
        long start = System.currentTimeMillis();
        super.initialize(stateManager, app); 
        
        // 载入场景
        initScene(gameData.getSceneData());
        Game game = Loader.loadGame(gameData);
        addObject(game, false);
        
        // ==== 载入player
        loadPlayer();
        
        // 必须在载入player后才可以开始逻辑,因为部分游戏逻辑init需要获得玩家引用
        game.start();
        addMessage("Load time used: " + ((System.currentTimeMillis() - start) / 1000), MessageType.info);
        
        // 打开局域网服务,这允许故事模式进行联机
        openGameServer();
    }
    
    // 开启主机模式，这允许故事模式下其它玩家连接到该游戏
    private void openGameServer() {
        // 添加network的逻辑
//        addLogic(Network.getInstance());
        addObject(Network.getInstance(), false);
        
        // 创建server
        if (gameServer == null) {
            try {
                gameServer = Network.getInstance().createGameServer(gameData);
                gameServer.setServerListener(new StoryServerListener());
            } catch (IOException ex) {
                Logger.getLogger(StoryState.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        if (!gameServer.isRunning()) {
            gameServer.start();
            gameServer.setServerState(GameServer.ServerState.running);
        }
    }
    
    /**
     * 载入玩家角色
     * @return 
     */
    protected void loadPlayer() {
        // 载入玩家角色
        Actor actor;
        if (saveStory != null) {
            actor = actorService.loadActor(saveStory.getPlayer());
            List<ShortcutSave> ss = saveStory.getShortcuts();
            loadShortcut(ss, actor);
        } else {
            actor = actorService.loadActor(Config.debug ? IdConstants.ACTOR_PLAYER_TEST : IdConstants.ACTOR_PLAYER);
            logicService.resetPlayerLogic(actor);
        }
        // 故事模式玩家始终队伍分组为1
        actorService.setTeam(actor, 1);
        skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);
        
//        actor.getModel().setShadowMode(RenderQueue.ShadowMode.Cast);
        
        addObject(actor.getModel(), false);
        setPlayer(actor);
    }

    @Override
    public List<MessPlayClientData> getClients() {
        if (gameServer != null) {
            return gameServer.getClients();
        }
        return null;
    }

    @Override
    public void kickClient(int connId) {
        if (gameServer != null) {
            gameServer.kickClient(connId, "kick");
        }
    }
    
    @Override
    public void exit() {
        
        // 保存玩家资料
        if (player != null) {
            logger.log(Level.INFO, "Save player info.");
            SaveStory lastSave = SaveHelper.loadStoryLast();
            if (lastSave == null) {
                lastSave = new SaveStory();
            }
            lastSave.setPlayer(player.getData());
            lastSave.setShortcuts(ShortcutManager.getShortcutSaves());
            SaveHelper.saveStoryLast(lastSave);
        }
        
        // 保存全局配置
        SaveConfig saveConfig = new SaveConfig();
        saveConfig.setConfig(configService.getConfig());
        SaveHelper.saveConfig(saveConfig);
        
        // 退出
        super.exit();
    }
    
    @Override
    public void cleanup() {
        // 关闭网络服务，释放端口和资源，必要的。如果没有创建网络主机或客户端，
        // 则该方法什么也不做。
        Network.getInstance().cleanup();
        super.cleanup(); 
    }
    
    private class StoryServerListener extends LanServerListener {
        
        public StoryServerListener() {
            super(app);
        }

        @Override
        protected void processServerMessage(GameServer gameServer, HostedConnection source, Message m) {
            if (m instanceof MessPlayActorSelect) {
                // 响应客户端角色选择
                ((MessPlayActorSelect)m).applyOnServer(gameServer, source);
                
                // 重新设置玩家角色的等级和派别。故事模式下客户端玩家角色进入时
                // 的等级要比主玩家的低一些.
                Long actorUniqueId = source.getAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID);
                if (actorUniqueId != null) {
                    int playerLevel = actorService.getLevel(player);
                    playerLevel *= 0.75f;
                    if (playerLevel < 1) {
                        playerLevel = 1;
                    }
                    Actor actor = playService.findActor(actorUniqueId);
                    actorNetwork.setGroup(actor, actorService.getGroup(player));
                    actorNetwork.setLevel(actor, playerLevel);
                }
                return;
            } 
            
            super.processServerMessage(gameServer, source, m); 
        }
        
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
            DataType type = data.getProto().getDataType();
            if (type != DataType.item && type != DataType.skin && type != DataType.skill) {
                continue;
            }
            
            // 由于skill的创建过程比较特殊，SkillData只有在创建了AnimSkill之后
            // 才能获得skillType,所以不能直接使用createProtoData方式获得的SkillData
            // 这会找不到SkillData中的skillType,所以需要从角色身上重新找回SkillData
            if (data.getProto().getDataType() == DataType.skill) {
                data = player.getData().getSkillStore().getSkillById(data.getId());
            }
            
            // remove20160131
//            Shortcut shortcut = new Shortcut(player, data);
            
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
}
