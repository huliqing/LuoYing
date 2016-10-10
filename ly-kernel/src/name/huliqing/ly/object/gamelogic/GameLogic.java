/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.gamelogic;

import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.xml.DataProcessor;
import name.huliqing.ly.object.game.Game;

/**
 * 游戏逻辑
 * @author huliqing
 * @param <T>
 */
public interface GameLogic<T extends GameLogicData> extends DataProcessor<T> {

    @Override
    void setData(T data);

    @Override
    T getData();
    
    /**
     * 初始化物体
     * @param game
     */
    public void initialize(Game game);

    /**
     * 判断是否已经初始化
     * @return
     */
    public boolean isInitialized();

    /**
     * 更新Object的logic
     *
     * @param tpf
     */
    void update(float tpf);

    /**
     * 清理Logic产生的资源，当该方法被调用的时候Object已经被移出更新队列。
     */
    void cleanup();

    /**
     * 设置是否打开逻辑
     * @param enabled
     */
    void setEnabled(boolean enabled);

    /**
     * 判断逻辑是否打开
     * @return
     */
    boolean isEnabled();

}
