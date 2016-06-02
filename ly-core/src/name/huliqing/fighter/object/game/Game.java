/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.util.SafeArrayList;
import com.sun.jmx.snmp.tasks.Task;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.object.PlayObject;

/**
 * 任务控制器，主要用于控制任务的执行。通过 {@link #addTask(Task) }
 * 来添加任务，多个任务形成一个任务链。每个任务执行完毕后会继续下一个任务的
 * 执行。
 * @author huliqing
 */
public abstract class Game extends IntervalLogic {

    // 其它任何逻辑
    protected final SafeArrayList<PlayObject> logics = new SafeArrayList<PlayObject>(PlayObject.class);
    // 任务列表
    protected final List<GameTask> tasks = new ArrayList<GameTask>();
    
    // 当前正在执行的任务
    private GameTask current;
    
    private int finishCount;
    
    protected GameData data;
    protected boolean started;
    
    public Game() {
        super(0);
    }
    
    public Game(GameData data) {
        super(0);
        this.data = data;
    }
    
    public void start() {
        finishCount = 0;
        current = null;
        started = true;
        doInit();
        doNext();
    }
    
    public void addTask(GameTask task) {
        tasks.add(task);
    }
    
    protected void doEnd() {
        started = false;
    }
    
    /**
     * 是否有下一个任务
     * @return 
     */
    public boolean hasNext() {
        return finishCount < tasks.size();
    }
    
    /**
     * 执行下一个任务
     */
    public void doNext() {
        GameTask previous = current;
        if (current == null) {
            current = tasks.get(0);
        } else {
            int i = tasks.indexOf(current) + 1; // 由外部判断是否有hasNext
            current = tasks.get(i);
        }
//        Log.get(getClass()).log(Level.INFO, "Do next task:{0}", current.getTaskName());
        current.start(previous);
    }

    @Override
    protected void doLogic(float tpf) {
        if (!started) return;
        
        // update tasks
        if (current.isFinished()) {
            finishCount++;
            if (hasNext()) {
                doNext();
            } else {
                doEnd();
            }
        } else {
            current.update(tpf);
        }
        
        // other logic
        for (PlayObject logic : logics.getArray()) {
            logic.update(tpf);
        }
    }
    
    /**
     * 清理并结束当前游戏
     */
    @Override
    public void cleanup() {
        // Clear task
        for (GameTask task : tasks) {
            if (!task.isFinished()) {
                task.cleanup();
            }
        }
        tasks.clear();
        
        // Clear logics
        for (PlayObject l : logics.getArray()) {
            l.cleanup();
        }
        logics.clear();
        started = false;
    }
    
    protected abstract void doInit();
    
}
