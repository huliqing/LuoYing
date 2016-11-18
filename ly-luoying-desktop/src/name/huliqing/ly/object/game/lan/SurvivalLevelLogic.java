/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.lan;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.layer.network.GameNetwork;

/**
 * 用于按时间计算等级
 * @author huliqing
 * @param <T>
 */
public class SurvivalLevelLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);

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
            gameNetwork.addMessage(message, MessageType.notice);
            TextView levelView = (TextView) Loader.load(IdConstants.VIEW_TEXT_SUCCESS);
            levelView.setText(message);
            playNetwork.addEntity(levelView);
        }
    }

    @Override
    public void cleanup() {
        level = 0;
        timeIntervalUsed = 0;
        super.cleanup();
    }
    
}
