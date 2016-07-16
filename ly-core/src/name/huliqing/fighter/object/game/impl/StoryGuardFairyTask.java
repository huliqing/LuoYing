/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game.impl;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.logic.scene.ActorMultLoadHelper;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 妖精的支线任务
 * @author huliqing
 */
public class StoryGuardFairyTask extends IntervalLogic {
    
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private StoryGuardGame game;
    
    // 这个距离定义了当player离妖精地点多近时就触发妖精任务 － 载入妖精角色
    private float distanceSquare = 30 * 30;
    
    // ---- 妖精设定
    // 妖精
    private Actor fairy;
    // 妖精的奴仆
    private Actor servant;
    private ActorMultLoadHelper loader;
    private String[] actorIds = {IdConstants.ACTOR_FAIRY, IdConstants.ACTOR_SCORPION_BIG};

    // 阶段
    private int stage;

    public StoryGuardFairyTask(StoryGuardGame game) {
        this.game = game;
    }
    
    @Override
    protected void doLogic(float tpf) {
        // 检查是否触发任务
        if (stage == 0) {
            if (checkToEnableFairTask()) {
                startLoadFairy();
                stage = 1;
            }
            return;
        }
        
        // 载入妖精
        if (stage == 1) {
            if (fairy != null && servant != null) {
                playService.removeObject(loader);
                stage = 2;
            }
        }
        
        // 战斗
        
    }

    // remove20160305
//    @Override
//    public void onRemove() {
//        playService.removeObject(loader);
//        super.onRemove();
//    }

    @Override
    public void cleanup() {
        playService.removeObject(loader);
        super.cleanup(); 
    }
    
    private void startLoadFairy() {
        loader = new ActorMultLoadHelper(actorIds) {
            @Override
            public void callback(Actor actor, int loadIndex) {
                String id = actor.getData().getId();
                actorService.setGroup(actor, StoryGuardGame.GROUP_FAIRY);
                actor.setLocation(game.getFairyPosition());
                if (id.equals(IdConstants.ACTOR_FAIRY)) {
                    fairy = actor;
                    actorService.setLevel(fairy, game.getFairyLevel());
                    
                } else if (id.equals(IdConstants.ACTOR_SCORPION_BIG)) {
                    servant = actor;
                    actorService.setLevel(servant, game.getServantLevel());
                    actorService.setPartner(fairy, servant);
                }
                playNetwork.addActor(actor);
            }
        };
        playService.addObject(loader, false);
    }
    
    // 检查是否打开“妖精”任务
    private boolean checkToEnableFairTask() {
        return game.getFairyPosition().distanceSquared(
                playService.getPlayer().getModel().getWorldTranslation()) < distanceSquare;
    }
}
