/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly;

import com.jme3.app.Application;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.AppSettings;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.loader.GameDataLoader;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.ly.data.ChatData;
import name.huliqing.ly.data.ViewData;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.ly.layer.service.ChatServiceImpl;
import name.huliqing.ly.loader.ChatDataLoader;
import name.huliqing.ly.loader.ViewDataLoader;
import name.huliqing.ly.mess.MessActorSpeak;
import name.huliqing.ly.mess.MessChatSell;
import name.huliqing.ly.mess.MessChatSend;
import name.huliqing.ly.mess.MessChatShop;
import name.huliqing.ly.mess.MessMessage;
import name.huliqing.ly.mess.MessPlayInitGame;
import name.huliqing.ly.mess.MessSyncObject;
import name.huliqing.ly.object.chat.GroupChat;
import name.huliqing.ly.object.chat.SellChat;
import name.huliqing.ly.object.chat.SendChat;
import name.huliqing.ly.object.chat.ShopItemChat;
import name.huliqing.ly.object.chat.TaskChat;
import name.huliqing.ly.object.game.story.StoryGbGame;
import name.huliqing.ly.object.game.story.StoryGuardGame;
import name.huliqing.ly.object.game.story.StoryTreasureGame;
import name.huliqing.ly.object.game.lan.SurvivalGame;
import name.huliqing.ly.object.gamelogic.PlayerDeadCheckerGameLogic;
import name.huliqing.ly.object.module.ChatModule;
import name.huliqing.ly.object.view.TextPanelView;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.object.view.TimerView;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.network.GameNetworkImpl;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.layer.service.GameServiceImpl;
import name.huliqing.ly.mess.MessActionRun;

/**
 * @author huliqing
 */
public class Init {
    
    /**
     * 初始化数据
     * @param app
     * @param settings 
     */
    public static void initialize(Application app, AppSettings settings) {
        LuoYing.initialize(app, settings);
        
        registerService();
        registerData();
        registerProcessor();
        loadData();
        
        // 载入资源,作为默认
        ResManager.loadResource("/data/font/en_US/resource",                "utf-8", null);
        ResManager.loadResource("/data/font/en_US/resource_object",    "utf-8", null);
        ResManager.loadResource("/data/font/en_US/story_gb",                "utf-8", null);
        ResManager.loadResource("/data/font/en_US/story_guard",          "utf-8", null);
        ResManager.loadResource("/data/font/en_US/story_treasure",      "utf-8", null);
        
        // 载入资源,作为中文环境
        ResManager.loadResource("/data/font/zh_CN/resource",                "utf-8", "zh_CN");
        ResManager.loadResource("/data/font/zh_CN/resource_object",    "utf-8", "zh_CN");
        ResManager.loadResource("/data/font/zh_CN/story_gb",                "utf-8", "zh_CN");
        ResManager.loadResource("/data/font/zh_CN/story_guard",           "utf-8", "zh_CN");
        ResManager.loadResource("/data/font/zh_CN/story_treasure",       "utf-8", "zh_CN");
        
    }
    
    private static void registerData() {
        Serializer.registerClass(ChatData.class);
        Serializer.registerClass(ViewData.class);
        
        Serializer.registerClass(MessActionRun.class);
        Serializer.registerClass(MessActorSpeak.class);
        Serializer.registerClass(MessChatSell.class);
        Serializer.registerClass(MessChatSend.class);
        Serializer.registerClass(MessChatShop.class);
        Serializer.registerClass(MessMessage.class);
        Serializer.registerClass(MessPlayInitGame.class);
        Serializer.registerClass(MessSyncObject.class);
//        Serializer.registerClass(xxxxxxx.class);
    }
    
    private static void registerService() {
        Factory.register(GameService.class, GameServiceImpl.class);
        Factory.register(ChatService.class, ChatServiceImpl.class);
        
        Factory.register(GameNetwork.class, GameNetworkImpl.class);
    }

    private static void registerProcessor() {
        // 自定义的游戏类型
        DataFactory.register("gameStoryTreasure", GameData.class, GameDataLoader.class, StoryTreasureGame.class);
        DataFactory.register("gameStoryGb", GameData.class, GameDataLoader.class, StoryGbGame.class);
        DataFactory.register("gameStoryGuard", GameData.class, GameDataLoader.class, StoryGuardGame.class);
        DataFactory.register("gameSurvival", GameData.class, GameDataLoader.class, SurvivalGame.class);
        
        // 额外的游戏逻辑
        DataFactory.register("gameLogicPlayerDeadChecker", GameLogicData.class, null, PlayerDeadCheckerGameLogic.class);
        
        // Chat
        DataFactory.register("chatGroup",  ChatData.class, ChatDataLoader.class, GroupChat.class);
        DataFactory.register("chatSend",  ChatData.class, ChatDataLoader.class, SendChat.class);
        DataFactory.register("chatShopItem",  ChatData.class, ChatDataLoader.class, ShopItemChat.class);
        DataFactory.register("chatSell",  ChatData.class, ChatDataLoader.class, SellChat.class);
        DataFactory.register("chatTask",  ChatData.class, ChatDataLoader.class, TaskChat.class);
        
        // actor module
        DataFactory.register("moduleChat", ModuleData.class, null, ChatModule.class);
        
        // View
        DataFactory.register("viewText",  ViewData.class, ViewDataLoader.class, TextView.class);
        DataFactory.register("viewTextPanel",  ViewData.class, ViewDataLoader.class, TextPanelView.class);
        DataFactory.register("viewTimer",  ViewData.class, ViewDataLoader.class, TimerView.class);
    }
    
    private static void loadData() throws LuoYingException {
        LuoYing.loadData("/data/object/action.xml");
        LuoYing.loadData("/data/object/actor.xml");
        LuoYing.loadData("/data/object/actorAnim.xml");
        LuoYing.loadData("/data/object/anim.xml");
        LuoYing.loadData("/data/object/attribute.xml");
        LuoYing.loadData("/data/object/bullet.xml");
        LuoYing.loadData("/data/object/channel.xml");
        LuoYing.loadData("/data/object/chat.xml");
        LuoYing.loadData("/data/object/config.xml");
        LuoYing.loadData("/data/object/define.xml");
        LuoYing.loadData("/data/object/drop.xml");
        LuoYing.loadData("/data/object/effect.xml");
        LuoYing.loadData("/data/object/el.xml");
        LuoYing.loadData("/data/object/emitter.xml");
        LuoYing.loadData("/data/object/env.xml");
        LuoYing.loadData("/data/object/game.xml");
        LuoYing.loadData("/data/object/gameLogic.xml");
        LuoYing.loadData("/data/object/hitChecker.xml");
        LuoYing.loadData("/data/object/item.xml");
        LuoYing.loadData("/data/object/logic.xml");
        LuoYing.loadData("/data/object/magic.xml");
        LuoYing.loadData("/data/object/module.xml");
        LuoYing.loadData("/data/object/position.xml");
        LuoYing.loadData("/data/object/resist.xml");
        LuoYing.loadData("/data/object/scene.xml");
        LuoYing.loadData("/data/object/shape.xml");

        // 技能
        LuoYing.loadData("/data/object/skill.xml");
        LuoYing.loadData("/data/object/skill_monster.xml");
        LuoYing.loadData("/data/object/skill_skin.xml");

        // 装备、武器
        LuoYing.loadData("/data/object/skin.xml");
        LuoYing.loadData("/data/object/skin_male.xml");
        LuoYing.loadData("/data/object/skin_weapon.xml");

        // 武器槽位配置
        LuoYing.loadData("/data/object/slot.xml");

        LuoYing.loadData("/data/object/sound.xml");
        LuoYing.loadData("/data/object/state.xml");
        LuoYing.loadData("/data/object/talent.xml");
        LuoYing.loadData("/data/object/task.xml");
        LuoYing.loadData("/data/object/view.xml");

    }
}
