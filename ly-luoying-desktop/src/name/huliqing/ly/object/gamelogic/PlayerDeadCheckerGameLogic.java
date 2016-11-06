/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.gamelogic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 用于检查主角是否已经死亡
 * @author huliqing
 */
public class PlayerDeadCheckerGameLogic extends AbstractGameLogic {
    private final GameService gameService = Factory.get(GameService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);

    private Game game;
    private Entity player;
    private boolean dead;
    private boolean displayed;
    private BooleanAttribute deadAttribute;

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
        if (player == null) {
            player = gameService.getPlayer();
        }
        if (player != null) {
            deadAttribute = player.getAttributeManager().getAttribute(AttrConstants.DEAD, BooleanAttribute.class);
            if (deadAttribute == null) {
                setEnabled(false);
                return;
            }
        }
        
        if (deadAttribute == null) {
            return;
        }
        if (!displayed && deadAttribute.getValue()) {
            dead = true;
            displayed = true;
            gameNetwork.addMessage(ResourceManager.get(ResConstants.TASK_FAILURE), MessageType.notice);
            gameNetwork.addMessage(ResourceManager.get(ResConstants.COMMON_BACK_TO_TRY_AGAIN), MessageType.notice);
            playNetwork.addEntity((View) Loader.load(IdConstants.VIEW_TEXT_FAILURE));

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
