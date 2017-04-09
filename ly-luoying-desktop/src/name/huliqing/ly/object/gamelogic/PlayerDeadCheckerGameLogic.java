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
package name.huliqing.ly.object.gamelogic;

import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
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

    private Entity player;
    private boolean dead;
    private boolean displayed;
    private BooleanAttribute deadAttribute;
    
    public boolean isDead() {
        return dead;
    }

    @Override
    protected void logicInit(Game game) {}

    @Override
    protected void logicUpdate(float tpf) {
        if (player == null) {
            player = gameService.getPlayer();
        }
        if (player != null && deadAttribute == null) {
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
//            game.setEnabled(false);
        }
    }

    @Override
    public void cleanup() {
        player = null;
        dead = false;
        displayed = false;
        super.cleanup(); 
    }
    
}
