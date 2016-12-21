/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
//package name.huliqing.ly.object.game.story;
//
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.service.ActorService;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.layer.service.SkillService;
//import name.huliqing.ly.layer.service.GameService;
//import name.huliqing.ly.object.game.StoryServerNetworkRpgGame;
//
///**
// * 故事模式的游戏方式
// * @author huliqing
// */
//public abstract class StoryGame extends StoryServerNetworkRpgGame { 
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final SkillService skillService = Factory.get(SkillService.class);
//    private final GameService gameService = Factory.get(GameService.class);
////    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
//
////    private final TaskStepControl taskControl = new TaskStepControl();
//    
////    /**
////     * 主角的用户组id
////     */
////    public final static int GROUP_PLAYER = 1;
////    
////    // 存档数据
////    private SaveStory saveStory = new SaveStory();
////
////    public SaveStory getSaveStory() {
////        return saveStory;
////    }
////
////    public void setSaveStory(SaveStory saveStory) {
////        this.saveStory = saveStory;
////    }
//    
////    public void addTask(TaskStep task) {
////        taskControl.addTask(task);
////    }
////
////    @Override
////    public final void initialize(Application app) {
////        super.initialize(app);
////        loadPlayer();
////        doStoryInitialize();
////        taskControl.doNext();
////    }
//
////    @Override
////    protected void simpleUpdate(float tpf) {
////        super.simpleUpdate(tpf);
////        taskControl.update(tpf);
////    }
//    
////    /**
////     * 清理并结束当前游戏
////     */
////    @Override
////    public void cleanup() {
////        super.cleanup();
////    }
//    
////    /**
////     * 载入当前主角玩家存档，如果没有存档则新开游戏
////     */
////    private void loadPlayer() {
////        Actor actor;
////        if (saveStory.getPlayer() != null) {
////            // 载入玩家主角
////            actor = Loader.load(saveStory.getPlayer());
////            //List<ShortcutSave> ss = saveStory.getShortcuts();
////            //ShortcutManager.loadShortcut(ss, player);
////            
////            // 载入玩家主角的宠物(这里还不需要载入其他玩家的角色及宠物,由其他玩家重新连接的时候再载入)
////            ArrayList<ActorData> actors = saveStory.getActors();
////            for (ActorData ad : actors) {
////                Actor tempActor = Loader.load(ad);
////                if  (gameService.getOwner(tempActor) == tempActor.getData().getUniqueId()) {
////                    getScene().addEntity(tempActor);
////                }
////            }
////        } else {
////            if (Config.debug) {
////                actor = Loader.load(IdConstants.ACTOR_PLAYER_TEST);
////                gameService.setLevel(actor, 10);
////            } else {
////                actor = Loader.load(IdConstants.ACTOR_PLAYER);
////            }
//////            logicService.resetPlayerLogic(player);
////        }
////        getScene().addEntity(actor);
////        // 确保角色位置在地面上
////        Vector3f loc = actorService.getLocation(actor);
////        Vector3f terrainHeight = playService.getTerrainHeight(scene, loc.x, loc.z);
////        if (terrainHeight != null) {
////            actorService.setLocation(actor, terrainHeight.addLocal(0, 0.5f, 0));
////        }
////        // 给玩家指定分组
////        gameService.setGroup(actor, GROUP_PLAYER);
////        // 故事模式玩家始终队伍分组为1
////        gameService.setTeam(actor, 1);
////        // 让角色处于“等待”
////        skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
////        setPlayer(actor);
////    }
////    
////    /**
////     * 故事模式的初始化，该方法的调用在gameInitialize之后。并且是在主玩家(player)载入之后才会被调用。
////     */
////    protected abstract void doStoryInitialize();
//    
//    
//}
