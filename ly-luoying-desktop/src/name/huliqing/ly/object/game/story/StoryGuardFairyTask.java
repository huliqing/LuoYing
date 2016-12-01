/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.logic.scene.ActorMultLoadHelper;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 妖精的支线任务
 * @author huliqing
 */
public class StoryGuardFairyTask extends AbstractGameLogic {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
//    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final StoryGuardGame _game;
    
    // 这个距离定义了当player离妖精地点多近时就触发妖精任务 － 载入妖精角色
    private final float distanceSquare = 30 * 30;
    
    // ---- 妖精设定
    // 妖精
    private Entity fairy;
    // 妖精的奴仆
    private Entity servant;
    private ActorMultLoadHelper loader;
    private final String[] actorIds = {IdConstants.ACTOR_FAIRY, IdConstants.ACTOR_SCORPION_BIG};

    // 阶段
    private int stage;

    public StoryGuardFairyTask(StoryGuardGame game) {
        super(1.0f);
        this._game = game;
    }
    
    @Override
    protected void doLogicUpdate(float tpf) {
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
                _game.removeLogic(loader);
                stage = 2;
            }
        }
        
        // 战斗
        
    }

    @Override
    public void cleanup() {
        _game.removeLogic(loader);
        super.cleanup(); 
    }
    
    private void startLoadFairy() {
        loader = new ActorMultLoadHelper(actorIds) {
            @Override
            public void callback(Entity actor, int loadIndex) {
                String id = actor.getData().getId();
                gameService.setGroup(actor, StoryGuardGame.GROUP_FAIRY);
                actorService.setLocation(actor, _game.getFairyPosition());
                if (id.equals(IdConstants.ACTOR_FAIRY)) {
                    fairy = actor;
                    gameService.setLevel(fairy, _game.getFairyLevel());
                    
                } else if (id.equals(IdConstants.ACTOR_SCORPION_BIG)) {
                    servant = actor;
                    gameService.setLevel(servant, _game.getServantLevel());
                    gameService.setPartner(fairy, servant);
                }
                playNetwork.addEntity(actor);
            }
        };
        _game.addLogic(loader);
    }
    
    // 检查是否打开“妖精”任务
    private boolean checkToEnableFairTask() {
        return _game.getFairyPosition().distanceSquared(
                _game.getPlayer().getSpatial().getWorldTranslation()) < distanceSquare;
    }
} 
