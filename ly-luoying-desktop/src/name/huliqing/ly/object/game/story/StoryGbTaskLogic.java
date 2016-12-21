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
package name.huliqing.ly.object.game.story;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.TextPanelView;

/** 
 * 任务面板，显示已经获得的树根的数目
 * @author huliqing
 */
public class StoryGbTaskLogic extends AbstractGameLogic {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);

    // 需要获得树根的数量
    private final int total;
    // 当前已经获得的数量
    private int count;
    // player
    private final Entity player;
    
    private TextPanelView tpv;
    
    public StoryGbTaskLogic(int total, Entity player) {
        this.total = total;
        this.player = player;
        this.interval = 1.0f;  // 频率：每秒计算一次
    }
    
    @Override
    protected void logicInit(Game game) {
        if (tpv == null) {
            tpv = (TextPanelView) Loader.load(IdConstants.VIEW_TEXT_PANEL_VIEW_GB);
            tpv.setTitle(ResourceManager.getObjectName(IdConstants.GAME_STORY_GB));
        }
        playNetwork.addEntity(tpv);
    }
    
    @Override
    protected void logicUpdate(float tpf) { 
        ItemData item = player.getData().getObjectData(IdConstants.ITEM_GB_STUMP);
        count = item != null ? item.getTotal() : 0;
        tpv.setText(get("taskSave.saveCount", count, total));
    }

    @Override
    public void cleanup() {
        playNetwork.removeEntity(tpv);
        super.cleanup(); 
    }
    
    /**
     * 判断树根数是否已经回收完毕
     * @return 
     */
    public boolean isOk() {
        return count >= total;
    }
    
    /**
     * 返回任务中需要回收的树根总数
     * @return 
     */
    public int getTotal() {
        return total;
    }
    
    private String get(String rid, Object... param) {
        return ResourceManager.get("storyGb." +  rid, param);
    }
}
