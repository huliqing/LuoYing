/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.game.impl;

import com.jme3.app.Application;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.GameLogicData;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.ViewService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.game.Game;
import name.huliqing.core.object.gamelogic.AbstractGameLogic;
import name.huliqing.core.object.view.TextPanelView;

/** 
 * 任务面板，显示已经获得的树根的数目
 * @author huliqing
 * @param <T>
 */
public class StoryGbTaskLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final ViewService viewService = Factory.get(ViewService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);

    // 需要获得树根的数量
    private final int total;
    // 当前已经获得的数量
    private int count;
    // player
    private final Actor player;
    
    private TextPanelView tpv;
    
    public StoryGbTaskLogic(int total, Actor player) {
        this.total = total;
        this.player = player;
        this.interval = 1.0f;  // 频率：每秒计算一次
    }

    @Override
    public void initialize(Game game) {
        super.initialize(game);
        tpv = (TextPanelView) viewService.loadView(IdConstants.VIEW_TEXT_PANEL_VIEW_GB);
        tpv.setTitle(ResourceManager.getObjectName(IdConstants.GAME_STORY_GB));
        playNetwork.addView(tpv);
    }
    
    @Override
    protected void doLogic(float tpf) {
        ProtoData pd = actorNetwork.getItem(player, IdConstants.ITEM_GB_STUMP);
        count = pd != null ? pd.getTotal() : 0;
        tpv.setText(get("taskSave.saveCount", count, total));
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
    
    @Override
    public void cleanup() {
        playNetwork.removeObject(tpv);
        super.cleanup(); 
    }
    
    private String get(String rid, Object... param) {
        return ResourceManager.getOther("resource_gb", rid, param);
    }
}
