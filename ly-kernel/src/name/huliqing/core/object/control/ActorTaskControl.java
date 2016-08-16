/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TaskListener;
import name.huliqing.core.object.task.Task;
import name.huliqing.core.xml.DataFactory;

/**
 * 任务管理
 * @author huliqing
 */
public class ActorTaskControl extends ActorControl {

    private Actor actor;
    
    private final SafeArrayList<Task> tasks = new SafeArrayList<Task>(Task.class);
    private final List<TaskData> taskDatas = new ArrayList<TaskData>();
    private List<TaskListener> taskListeners;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;

        // 从存档中获取Datas，如果不是存档，则从原始配置的参数xml中获取
        List<TaskData> taskInits = (List<TaskData>) data.getAttribute("taskDatas");
        if (taskInits == null) {
            String[] taskArr = data.getAsArray("tasks");
            if (taskArr != null) {
                taskInits = new ArrayList<TaskData>(taskArr.length);
                for (String taskId : taskArr) {
                    taskInits.add((TaskData) DataFactory.createData(taskId));
                }
            }
        }

        if (taskInits != null) {
            for (TaskData td : taskInits) {
                addTask((Task) Loader.load(td));
            }
        }

        // 重新设置data.
        data.setAttribute("taskDatas", taskDatas);

    }

    @Override
    public void cleanup() {
        for (Task task : tasks) {
            task.cleanup();
        }
        tasks.clear();
        taskDatas.clear();
        super.cleanup();
    }
    
    @Override
    public void actorUpdate(float tpf) {}

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {}
    
    /**
     * 添加任务
     * @param task 
     */
    public void addTask(Task task) {
        if (tasks.contains(task)) {
            return;
        }
        
        task.setActor(actor);
        task.initialize();

        tasks.add(task);
        taskDatas.add(task.getData());
        
        // 侦听器
        if (taskListeners != null && !taskListeners.isEmpty()) {
            for (TaskListener tl : taskListeners) {
                tl.onTaskAdded(actor, task);
            }
        }
    }
    
    /**
     * 移除任务
     * @param task
     * @return 
     */
    public boolean removeTask(Task task) {
        if (tasks == null || tasks.isEmpty()) 
            return false;
        
        boolean result = tasks.remove(task);
        taskDatas.remove(task.getData());
        task.cleanup();
        
        // 侦听器
        if (result) {
            if (taskListeners != null && !taskListeners.isEmpty()) {
                for (TaskListener tl : taskListeners) {
                    tl.onTaskRemoved(actor, task);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 获取指定ID的任务，如果不存在则返回null.
     * @param taskId
     * @return 
     */
    public Task getTask(String taskId) {
        if (tasks == null) 
            return null;
        
        for (Task t : tasks) {
            if (t.getId().equals(taskId)) {
                return t;
            }
        }
        return null;
    }
    
    /**
     * 获取角色的任务列表，如果不存在任务列表则返回null
     * @return 
     */
    public List<Task> getTasks() {
        return tasks;
    }
    
    public List<TaskData> getTaskDatas() {
        return taskDatas;
    }
    
    /**
     * 完成一个指定的任务
     * @param task 
     */
    public void completeTask(Task task) {
        // 执行“任务完成”如奖励经验、金钱、物品等。。。
        task.doCompletion();
        // 清理任务处理器，释放资源，当任务完成之后就不需要这些东西了，如各种侦听器等
        task.cleanup();
        
        // 侦听器
        if (taskListeners != null && !taskListeners.isEmpty()) {
            for (TaskListener tl : taskListeners) {
                tl.onTaskCompleted(actor, task);
            }
        }
    }
    
    public void addTaskListener(TaskListener taskListener) {
        if (taskListeners == null) {
            taskListeners = new ArrayList<TaskListener>();
        }
        if (!taskListeners.contains(taskListener)) {
            taskListeners.add(taskListener);
        }
    }

    public boolean removeTaskListener(TaskListener taskListener) {
        return taskListeners != null && taskListeners.remove(taskListener);
    }

    public List<TaskListener> getTaskListeners() {
        return taskListeners;
    }
}
