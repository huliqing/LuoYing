/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 用于检查主角是否已经死亡
 * @author huliqing
 */
public class PlayerDeadChecker extends IntervalLogic {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    private final PlayService playService = Factory.get(PlayService.class);

    private final Actor player;
    private boolean dead = false;
    
    public PlayerDeadChecker(Actor player) {
        super(1);
        this.player = player;
    }
    
    public boolean isDead() {
        return dead;
    }

    @Override
    protected void doLogic(float tpf) {
        if (player.isDead()) {
            dead = true;
            playNetwork.addMessage(ResourceManager.get(ResConstants.TASK_FAILURE), MessageType.notice);
            playNetwork.addMessage(ResourceManager.get(ResConstants.COMMON_BACK_TO_TRY_AGAIN), MessageType.notice);
            playNetwork.addView(viewService.loadView(IdConstants.VIEW_TEXT_FAILURE));
            // 显示提示后直接退出
            playService.removeObject(this);
        }
    }
    
}
