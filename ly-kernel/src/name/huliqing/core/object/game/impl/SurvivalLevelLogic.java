/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.game.impl;

import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.GameLogicData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ViewService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.gamelogic.AbstractGameLogic;
import name.huliqing.core.object.view.TextView;

/**
 * 用于按时间计算等级
 * @author huliqing
 * @param <T>
 */
public class SurvivalLevelLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ViewService viewService = Factory.get(ViewService.class);

    // 当前等级
    private int level = 1;
    private final int maxLevel;
    // 当前的运行时间,单位秒
    private float timeIntervalUsed;
    // 每隔多长时间提升敌军等级,单位秒
    private final float levelUpBySec;
    
    public SurvivalLevelLogic(float levelUpBySec, int maxLevel) {
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
