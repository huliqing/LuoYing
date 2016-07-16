/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.gamelogic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.data.GameLogicData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.game.Game;

/**
 * 用于检查主角是否已经死亡
 * @author huliqing
 * @param <T>
 */
public class PlayerDeadCheckerGameLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    private final PlayService playService = Factory.get(PlayService.class);

    private Game game;
    private Actor player;
    private boolean dead;
    private boolean displayed;

    @Override
    public void initialize(Game game) {
        super.initialize(game); 
        this.game = game;
    }
    
    public boolean isDead() {
        return dead;
    }

    @Override
    protected void doLogic(float tpf) {
        // 如果player没有载入，则不应该执行逻辑
        if (player == null) {
            player = playService.getPlayer();
            return;
        }
        
        if (!displayed && player.isDead()) {
            dead = true;
            displayed = true;
            playNetwork.addMessage(ResourceManager.get(ResConstants.TASK_FAILURE), MessageType.notice);
            playNetwork.addMessage(ResourceManager.get(ResConstants.COMMON_BACK_TO_TRY_AGAIN), MessageType.notice);
            playNetwork.addView(viewService.loadView(IdConstants.VIEW_TEXT_FAILURE));

            // 如果主角死亡，则停止游戏逻辑 
            game.setEnabled(false);
        }
    }

    @Override
    public void cleanup() {
        game = null;
        player = null;
        dead = false;
        displayed = false;
        super.cleanup(); 
    }
    
}
