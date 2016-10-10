/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.xml.DataProcessor;
import name.huliqing.ly.object.gamelogic.GameLogic;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.task.Task;

/**
 * 任务控制器，主要用于控制任务的执行。通过 {@link #addTask(Task) }
 * 来添加任务，多个任务形成一个任务链。每个任务执行完毕后会继续下一个任务的
 * 执行。
 * @author huliqing
 * @param <T>
 */
public interface Game<T extends GameData> extends DataProcessor<T> {
   
    /**
     * 初始化游戏
     */
    void initialize();
    
    /**
     * 判断游戏是否已经初始化
     * @return 
     */
    boolean isInitialized();

    /**
     * 更新游戏逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
    /**
     * 设置游戏场景
     * @param scene 
     */
    void setScene(Scene scene);

    /**
     * 获取当前游戏场景
     * @return 
     */
    Scene getScene();
    
    /**
     * 设置GUI场景
     * @param scene 
     */
    void setGuiScene(Scene scene);
    
    /**
     * 获取GUI场景
     * @return 
     */
    Scene getGuiScene();
    
    /**
     * 添加一个游戏逻辑
     * @param logic 
     */
    void addLogic(GameLogic logic);
    
    /**
     * 移除一个游戏逻辑
     * @param logic 
     * @return  
     */
    boolean removeLogic(GameLogic logic);
    
    /**
     * 获取当前游戏中运行的所有逻辑,返回的列表不能直接修改。增加或删除逻辑需要使用 <br>
     * {@link #addLogic(name.huliqing.ly.object.gamelogic.GameLogic) }<br>
     * {@link #removeLogic(name.huliqing.ly.object.gamelogic.GameLogic) }<br>
     * @return 
     */
    List<GameLogic> getLogics();
    
    /**
     * 添加游戏侦听器
     * @param listener 
     */
    void addListener(GameListener listener);
    
    /**
     * 移除指定的游戏侦听器，如果不存在则返回false.
     * @param listener
     * @return 
     */
    boolean removeListener(GameListener listener);

}
