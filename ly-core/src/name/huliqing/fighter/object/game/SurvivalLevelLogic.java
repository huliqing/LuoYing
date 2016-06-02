/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.network.StateNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.object.view.TextView;

/**
 * 用于按时间计算等级
 * @author huliqing
 */
public class SurvivalLevelLogic extends IntervalLogic {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ViewService viewService = Factory.get(ViewService.class);

    // 当前等级
    private int level = 1;
    private int maxLevel;
    // 当前的运行时间,单位秒
    private float timeIntervalUsed;
    // 每隔多长时间提升敌军等级,单位秒
    private float levelUpBySec;
    
    public SurvivalLevelLogic(float levelUpBySec, int maxLevel) {
        super(0);
        this.levelUpBySec = levelUpBySec;
        this.maxLevel = maxLevel;
    }

    /**
     * 获取当前的运行时间
     * @return 
     */
    public int getLevel() {
        return level;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (level >= maxLevel) {
            return;
        }
        
        timeIntervalUsed += tpf;
        if (timeIntervalUsed >= levelUpBySec) {
            level++;
            timeIntervalUsed = 0;
            String message = ResourceManager.get(ResConstants.COMMON_LEVEL) + " " + level;
            playNetwork.addMessage(message, MessageType.notice);
            TextView levelView = (TextView) viewService.loadView(IdConstants.VIEW_TEXT_SUCCESS);
            levelView.setText(message);
            playNetwork.addView(levelView);
        }
    }

    @Override
    public void cleanup() {
        level = 0;
        timeIntervalUsed = 0;
        super.cleanup();
    }
    
}
