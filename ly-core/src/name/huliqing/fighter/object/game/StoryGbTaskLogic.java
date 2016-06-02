/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.view.TextPanelView;

/** 
 * 任务面板，显示已经获得的树根的数目
 * @author huliqing
 */
public class StoryGbTaskLogic extends IntervalLogic {
    private final ViewService viewService = Factory.get(ViewService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);

    // 需要获得树根的数量
    private int total;
    // 当前已经获得的数量
    private int count;
    // player
    private Actor player;
    
    private TextPanelView tpv;
    
    public StoryGbTaskLogic(int total, Actor player) {
        super(1.0f); // 频率：每秒计算一次
        this.total = total;
        this.player = player;
    }

    @Override
    public void initialize() {
        super.initialize();
        tpv = (TextPanelView) viewService.loadView(IdConstants.VIEW_TEXT_PANEL_VIEW_GB);
        tpv.setTitle(ResourceManager.getObjectName(IdConstants.GAME_STORY_GB));
        playNetwork.addView(tpv);
    }
    
    @Override
    protected void doLogic(float tpf) {
        ProtoData data = actorNetwork.getItem(player, IdConstants.ITEM_GB_STUMP);
        count = data != null ? data.getTotal() : 0;
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
